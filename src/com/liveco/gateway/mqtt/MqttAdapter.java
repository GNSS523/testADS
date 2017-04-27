package com.liveco.gateway.mqtt;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.liveco.gateway.system.BaseSystem;
import com.liveco.gateway.system.FogponicsSystem;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttAdapter {

    public static final String MQTT_TOPIC_COMMAND = "command";

    public static final String MQTT_TOPIC_ONLINE = "online";

    public static final String MQTT_TOPIC_STATE_CODE = "stateCode";
    
    public static final String MQTT_TOPIC_ERROR_CODE = "errorCode";

    public static final String MQTT_TOPIC_STATE_MESSAGE = "stateMessage";

    public static final String MQTT_TOPIC_STATE_LEVEL = "stateLevel";

    private static final Logger LOG = LogManager.getLogger(MqttAdapter.class);
	
    /** the retainment flag for the data topics */
    private static final boolean RETAINMENT = true;

    /** the configuration to use */
    private final MqttAdapterConfiguration configuration;    
  
    /** a reference to the worker thread */    
    private Thread threadWorker;

    /** this is used for indicating, that we are request to shutdown */    
    private AtomicBoolean flagShutdown = new AtomicBoolean(false);
    
    /** semaphore for waking worker thread up */
    private Semaphore semThreadWaker;
    
    private MqttCallback callback;
    
    
    

    
    /**
     * Initializes the MqttAdapter.
     * 
     * @param configuration_ the configuration to use
     */
    public MqttAdapter (MqttAdapterConfiguration configuration_, MqttCallback callback)
    {
        // Get a clone, since we don't want to be manipulated during runtime
        configuration = (MqttAdapterConfiguration) configuration_.clone();
        this.callback = callback;
    }    
    
    public MqttAdapterConfiguration getConfiguration(){
    	return configuration;
    }
    
    /**
     * This starts the adapter.
     */
    public synchronized void startup ()
    {
        if ( threadWorker != null ) {
            throw new IllegalStateException("The adapter is already started");
        }
        flagShutdown.set(false);
        semThreadWaker = new Semaphore(0);
        threadWorker = new Thread(new Runnable() {

            @Override
            public void run ()
            {
                runOuter();
            }
        });
        LOG.debug("Starting worker thread");
        try {
            threadWorker.start();
            LOG.debug("Worker thread started");
        }
        catch (RuntimeException ex) {
            LOG.error("Failed to start worker thread", ex);
            threadWorker = null;
            throw ex;
        }
    }

    /**
     * This shuts down the adapter.
     */
    public synchronized void shutdown ()
    {
        if ( threadWorker == null ) {
            throw new IllegalStateException("The adapter is not started");
        }
        try {
            LOG.debug("Requesting worker thread to shut down");
            flagShutdown.set(true);
            //semThreadWaker.release();
            LOG.debug("Waiting for worker thread");
            while ( true ) {
                try {
                    threadWorker.join();
                    break;
                }
                catch (InterruptedException ex) {
                    // Ignored
                }
            }
            LOG.debug("Worker thread terminated, shutdown complete");
        }
        finally {
            threadWorker = null;
            semThreadWaker = null;
            flagShutdown.set(false);
        }
    }    
    
    
    /**
     * This is the initial entry point for the worker thread.
     */
    private void runOuter ()
    {
        LOG.debug("Worker thread started");
        try {
            while ( !flagShutdown.get() ) {
                try {
                    runInternal();
                }
                catch (Exception ex) {
                    LOG.fatal("Unhandled exception thrown! Trying a restart...", ex);
                }
            }
        }
        finally {
            LOG.debug("Closing worker thread");
        }
    }
    
    
    
    private void runInternal ()
    {
        MqttClient mqttClient = null;
        //
        Semaphore semWakeup = semThreadWaker;

        try {
            while ( !flagShutdown.get() ) {
                if ( mqttClient == null ) {
                    LOG.info("No MQTT connection. Creating connection.");
                    mqttClient = connectMqtt(this.callback);
                    if ( mqttClient == null ) {
                        LOG.warn("Was not able to connect to MQTT broker.");
                    }
                }

                if ( mqttClient != null ) {
                	/*
                    try {
                        if ( currentState != null ) {
                            pushMqttStateOnline(mqttClient, currentState);
                        }
                        else {
                            pushMqttStateOffline(mqttClient);
                        }

                        String deviceCommand = fetchDeviceCommand(mqttClient, callback);
                        if ( indegoController != null && deviceCommand != null ) {
                            LOG.info(String.format("Processing command '%s'", deviceCommand));
                            try {
                                try {
                                    indegoController.sendCommand(DeviceCommand.valueOf(deviceCommand));
                                }
                                catch (IndegoInvalidCommandException ex) {
                                    LOG.warn(String.format("The sent command '%s' was not be processed by the "
                                            + "server because it's invalid in the current device state, ignoring it", deviceCommand), ex);
                                }
                                catch (IllegalArgumentException ex) {
                                    LOG.warn(String.format("Received invalid command '%s', ignoring it", deviceCommand));
                                }
                                clearMqttDeviceCommand(mqttClient);
                            }
                            catch (Exception ex) {
                                LOG.error("Exception during sending command to Indego", ex);

                            }
                        }
                    }
                    catch (Exception ex) {
                        LOG.error("Exception during pushing state to MQTT or fetching device command from MQTT", ex);
                        disconnect(mqttClient);
                        mqttClient = null;
                    }
                    */
                }

                try {
                    semWakeup.tryAcquire(configuration.getPollingIntervalMs(), TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException ex) {
                    // Ignored
                }
            }
        }
        finally {
            disconnect(mqttClient);
        }
    }
    
    
    /**
     * Connects to the MQTT broker by using a given notification callback.
     * 
     * @param callback the callback to use
     * @return a connected client instance; null, if the connection was not successful.
     */
    private MqttClient connectMqtt (MqttCallback callback)
    {
        MqttClient client = null;
        try {
            LOG.info("Connecting to MQTT broker");
            client = new MqttClient(configuration.getMqttBroker(), configuration.getMqttClientId(), new MemoryPersistence());
      
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
           // options.setUserName(configuration.getMqttUsername());
            //options.setPassword(configuration.getMqttPassword().toCharArray());
            //options.setWill(configuration.getMqttTopicRoot() + MQTT_TOPIC_ONLINE, "0".getBytes(), 1, true);
            client.setCallback(callback);
            client.connect(options);
            LOG.info("Connection to MQTT broker established");
            LOG.info("Subscribing to MQTT command topics");
            //client.subscribe(configuration.getMqttTopicRoot() + MQTT_TOPIC_COMMAND);
            //System.out.println("connection "+configuration.getMqttTopicRoot() + MQTT_TOPIC_COMMAND);
            String plc_id = "abc";
            
            client.subscribe("/device/plc/"+plc_id+"/system/+/+/command");
            return client;
        }
        catch (MqttException ex) {
            LOG.error("Connection to MQTT broker failed", ex);
            try {
                if ( client.isConnected() ) {
                	//client.disconnectForcibly();
                }
            }
            catch (Exception ex2) {
                // Ignored
            }
            return null;
        }
    }
    
    /**
     * Disconnects a connected MQTT client.
     * 
     * @param mqttClient the client to disconnect
     */
    private void disconnect (MqttClient mqttClient)
    {
    	
    
        try {
            if ( mqttClient != null ) {
                LOG.info("Disconnecting from MQTT broker");
                try {
                    LOG.debug("Resetting online state topic");
                    publish(mqttClient, MQTT_TOPIC_ONLINE, false, true);
                }
                catch (MqttException ex) {
                    LOG.warn("Was not able to reset the online state topic.", ex);
                }
                try {
                    LOG.debug("Doing MQTT disconnect");
                    mqttClient.disconnect();
                }
                catch (MqttException ex) {
                    LOG.warn("Was not able to disconnect from MQTT broker normally, forcing disconnect.", ex);
                    //mqttClient.disconnectForcibly();
                }
            }
        }
        catch (Exception ex) {
            LOG.warn("Something strange happened while disconnecting from MQTT broker", ex);
        }
    }    
    
    
    
    /**
     * Publishes a single topic on the MQTT broker
     * 
     * @param mqttClient the broker connection
     * @param topic the topic to publish (relative to configured topic root)
     * @param data the data to publish
     * @param retained if the data should be retained
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    private void publish (MqttClient mqttClient, String topic, boolean data, boolean retained)
            throws MqttPersistenceException, MqttException
    {
        publish(mqttClient, topic, data ? "1" : "0", retained);
    }

    /**
     * Publishes a single topic on the MQTT broker
     * 
     * @param mqttClient the broker connection
     * @param topic the topic to publish (relative to configured topic root)
     * @param data the data to publish
     * @param retained if the data should be retained
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    private void publish (MqttClient mqttClient, String topic, int data, boolean retained) throws MqttPersistenceException, MqttException
    {
        publish(mqttClient, topic, Integer.toString(data), retained);
    }

    /**
     * Publishes a single topic on the MQTT broker
     * 
     * @param mqttClient the broker connection
     * @param topic the topic to publish (relative to configured topic root)
     * @param data the data to publish
     * @param retained if the data should be retained
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    private void publish (MqttClient mqttClient, String topic, long data, boolean retained) throws MqttPersistenceException, MqttException
    {
        publish(mqttClient, topic, Long.toString(data), retained);
    }

    /**
     * Publishes a single topic on the MQTT broker
     * 
     * @param mqttClient the broker connection
     * @param topic the topic to publish (relative to configured topic root)
     * @param data the data to publish
     * @param retained if the data should be retained
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    private void publish (MqttClient mqttClient, String topic, String data, boolean retained) throws MqttPersistenceException, MqttException
    {
        if ( LOG.isDebugEnabled() ) {
            LOG.debug(String.format("Publishing '%s' to topic '%s' (retained = %s)", data, topic, retained));
        }

        MqttMessage msg = new MqttMessage(data.getBytes());
        msg.setQos(configuration.getMqttQos());
        msg.setRetained(retained);
        mqttClient.publish(configuration.getMqttTopicRoot() + topic, msg);
    }

    /**
     * Fetches the last unprocessed command, which was sent to the adapter.
     * 
     * @param mqttClient the connection to use
     * @param callback the callback, which processes published messages
     * @return the command to executre (null, if there is none)
     */
    private String fetchDeviceCommand (MqttClient mqttClient, MqttCommandCallback callback)
    {
        return callback.getLastCommand();
    }

    /**
     * This clears the command topic after processing.
     * 
     * @param mqttClient the connection to use
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    private void clearMqttDeviceCommand (MqttClient mqttClient) throws MqttPersistenceException, MqttException
    {
        publish(mqttClient, MQTT_TOPIC_COMMAND, "", true);
    }    
    
}
