import java.util.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.OnOffActuatorConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.mqtt.MqttAdapter;
import com.liveco.gateway.mqtt.MqttAdapterConfiguration;
import com.liveco.gateway.mqtt.MqttCommandCallback;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.FogponicsSystem;
import com.liveco.gateway.system.AirConditioner;
import com.liveco.gateway.system.BaseSystem;
import com.liveco.gateway.system.CO2System;
import com.liveco.gateway.system.SystemRepository;
import com.liveco.gateway.system.HydroponicsSystem;
import com.liveco.gateway.system.PanelLightingSystem;
import com.liveco.gateway.system.ShelfLightingSystem;

import com.google.gson.*;


import de.beckhoff.jni.tcads.AdsSymbolEntry;

public class Main {
	

	
	ADSConnection ads;
	
	Main(){
		
	}
	
	public ADSConnection openConnection(){
		ads = new ADSConnection();
		try {
			ads.open(851);		
		} catch (AdsException e) {	
			e.printStackTrace();
		}	
		return ads;
	}
		
	
	public void test(){
				
		this.openConnection();
		
    	byte array2[] = { (byte)12, (byte)11, (byte)3,(byte)4,(byte)5,(byte)6,(byte)7};
    	try {
			ads.writeSymbolByteArray("GVL_HMI.p1",array2 );
		} catch (AdsException e1) {
			e1.printStackTrace();
		}
    	
    	try {
			ads.readSymbolByteArray("GVL_HMI.p1",2);
		} catch (AdsException e1) {
			e1.printStackTrace();
		}		
    	
    	try {
			ads.readSymbol("GVL_HMI.p1");
		} catch (AdsException e) {
			e.printStackTrace();
		}	

	}    	

	
	
	
	
	
	
	
	
	

	
	
	
	
	
    public static void main(String[] args)
    { 
	   Logger logger = Logger.getLogger("com.liveco.gateway");
	   logger.setLevel(Level.DEBUG);    	
    	
    	System.out.println("hello world");
    	Main main = new Main();
    	//ADSConnection ads = main.openConnection();
    	
    	
    	HydroponicsSystem a = new HydroponicsSystem(null,0,"af" );
    	System.out.println( a.getTableOffset() );
    	a.test111();
    	

/*    	
        File configFile = new File(configFileName);

        if ( !configFile.exists() ) {
            System.err.println(String.format("The specified config file (%s) does not exist", configFileName));
            System.err.println();
            System.exit(2);
            return;
        }

        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(configFile)) {
            properties.load(in);
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.err.println(String.format("Was not able to load the properties file (%s)", configFileName));
            System.err.println();
        }

        MqttAdapterConfiguration config = new MqttAdapterConfiguration();
        config.setIndegoBaseUrl(properties.getProperty("indego.mqtt.device.base-url"));
        config.setIndegoUsername(properties.getProperty("indego.mqtt.device.username"));
        config.setIndegoPassword(properties.getProperty("indego.mqtt.device.password"));
        config.setMqttBroker(properties.getProperty("indego.mqtt.broker.connection"));
        config.setMqttClientId(properties.getProperty("indego.mqtt.broker.client-id"));
        config.setMqttUsername(properties.getProperty("indego.mqtt.broker.username"));
        config.setMqttPassword(properties.getProperty("indego.mqtt.broker.password"));
        config.setMqttTopicRoot(properties.getProperty("indego.mqtt.broker.topic-root"));
        config.setPollingIntervalMs(Integer.parseInt(properties.getProperty("indego.mqtt.polling-interval-ms")));    	
    	
*/    	
    	
    	SystemRepository system = new SystemRepository();
    	
    	MqttAdapterConfiguration configuration = new MqttAdapterConfiguration();
    	configuration.setMqttBroker("tcp://139.59.170.74:1883");
    	configuration.setMqttClientId("JavaSample");
    	MqttAdapter adapter = new MqttAdapter(configuration, new MqttCommandCallback(system));
    	adapter.startup();

    	
    	//System.out.println(   );
    	//main.openConnection();
    	//main.scanSystems();
    	//main.test();  
    	//System.out.println( AirConditionerCommand.OFF.getValue() +"   "+ AirConditionerCommand.getValue("ON" ) );
    }
}
