package com.liveco.gateway.mqtt;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.system.BaseSystem;
import com.liveco.gateway.system.SystemRepository;

public class MqttCommandCallback implements MqttCallback {

    private static final Logger LOG = LogManager.getLogger(MqttCommandCallback.class);
	
	
    private String lastCommand;
    Gson gson = new Gson();
    
    // "/device/plc/+/system/+/+/config"
	private static final String TOPIC_REGEX = "/device/plc/[^/]+/system/[^/]+/[^/]+/config";
	
	private SystemRepository system_repository;
	public MqttCommandCallback(SystemRepository system){
		this.system_repository = system;
	}
	
    @Override
    public void connectionLost (Throwable arg0)
    {
    	System.out.println("connectionLost   "+arg0.toString());
    }

    @Override
    public void deliveryComplete (IMqttDeliveryToken arg0)
    {
    	System.out.println("deliveryComplete   "+arg0.getMessageId());
    }

    @Override
    public void messageArrived (String topic, MqttMessage message) throws Exception
    {
    	
    	String parts[] = topic.split("/");
    	//LOG.debug(parts[0] + "  "+ parts[1]+" "+parts[2]+" "+parts[3]+" "+parts[4]+" "+parts[5]);
    	String plc_id = parts[3];
    	String system_type = parts[5];
    	String system_id = parts[6];
    	String command_type = parts[7];
    	/*
    	Pattern pattern = Pattern.compile(TOPIC_REGEX);
		Matcher m = pattern.matcher( topic);
    	
		if (m.matches()) {
			String deviceId = m.group(1);
			String sensorId = m.group(2);
			System.out.println(deviceId+"  "+sensorId);
		}	
		*/    	
    	//LOG.debug(plc_id+" "+system_type+" "+system_id+" "+command_type);
    	    
    	MqttCommand webcommand = this.parseContent(message);
    	BaseSystem system;
    	try{
    		system = this.system_repository.findSystem(SystemStructure.HYDROPONICS, Integer.parseInt(system_id));
    		
        	if(command_type.equals("command")){
        		system.parseCommand(webcommand);
        		
        	}else if(command_type.equals("status")){
        		system.parseCommand(webcommand);
        	}    		
        	
    	}catch(NullPointerException error){
    		LOG.error("System not found "+error);
    	}catch(IndexOutOfBoundsException error){
    		LOG.error("System not found "+error);
    	}
    }
    
    public MqttCommand parseContent(MqttMessage message){
    	String content = new String(message.getPayload());
        MqttCommand command = gson.fromJson(content, MqttCommand.class);
        //LOG.debug(command.getType()+"  "+command.getName()+" "+command.getValue());    
        return command;
    }
    
    public String getLastCommand ()
    {
        return lastCommand;
    }

}
