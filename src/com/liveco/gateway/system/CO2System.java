package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.constant.OnOffActuatorConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.mqtt.MqttCommand;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;
import com.liveco.gateway.plc.DeviceTypeException;

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class CO2System extends BaseSystem {

    private static final Logger LOG = LogManager.getLogger(CO2System.class);
	
	public static final SystemStructure type = SystemStructure.CO2_SYSTEM;
	
	public CO2System(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public CO2System(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public CO2System(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}
	

	
	public byte getTableFieldOffset(String type, int id){
		return CO2SystemConstant.Table.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return CO2SystemConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return CO2SystemConstant.Table.getNumber(name);
	}	
	
	/***************  Pump, Valve Control and Status 
	 * 
	 *   parse the web json into the command or status
	 * 
	 * *************/	
	
	public void parseCommand(MqttCommand webcommand) throws AdsException, DeviceTypeException{
		ICommand cmd = null;
		
		String type = webcommand.getType();
		String command = webcommand.getValue();
		String long_name = webcommand.getName();
		
		int id;
		String name;
		
		LOG.debug("CO2System parseCommand  "+type+"  "+command+"  "+long_name);
		/*
		switch(type){
			case "device": 
				// 
				cmd = OnOffActuatorConstant.Command.get(command);			
				String parts[] = long_name.split(".");
				id = Integer.parseInt(parts[3]);
				name = parts[1]+'.'+parts[2];
				System.out.println(    "Type:"+name  +"    Command:"+ id    );			
				this.setControl(  name, id  ,  (OnOffActuatorConstant.Command)cmd);
				break;
				
			case "attribute": 
				// CO2.threshold.low, CO2.threshold.high
				name = type+"."+long_name;
				this.setAttribute( name , Integer.parseInt(command) );
				break;
				
			case "mode": 
				// MANUAL, AUTOMATIC
				cmd = CO2SystemConstant.ModeCommand.get(command);
				name = type;
				this.setMode(name, (CO2SystemConstant.ModeCommand)cmd);
				break;
						
		}
		*/
	}	
	

	public void parseState(MqttCommand webcommand) throws AdsException, DeviceTypeException{

		String type = webcommand.getType();
		String command = webcommand.getValue();
		String long_name = webcommand.getName();

		int id;
		String name;

		LOG.debug("CO2System parseState  "+type+"  "+command+"  "+long_name);
		
		/*
		switch(type){
		
			case "device":
				String parts[] = long_name.split(".");
				id = Integer.parseInt(parts[3]);
				name = parts[1]+'.'+parts[2];
				System.out.println(    "Type:"+name  +"    Command:"+ id    );
				this.getControlStatus(type, id);
				break;
								
			case "mode":
				this.getMode(type); 
				System.out.println(" get the mode  : ");
				break;
				
			case "attribute":
				name = type;				
				this.getAttributedStatus(name);
				System.out.println(" get the attribute  : ");
				break;
				
			case "config":
				name = type+"."+long_name;				
				this.getFillReplyStatus(name);
				System.out.println(" get the command ack  : ");
				break;
		}
		*/		
	}	
	
	
	/***************   Valve Control and Status 
	 * 
	 * open("actuator.valve",1)
	 * close("actuator.valve",1)
	 * setControl("actuator.valve",1,OnOffActuatorConstant.Command.ON )
	 * getControlStatus("actuator.valve",1 )
	 * 
	 * *************/
	
	
	public void open(String type, int id) throws AdsException, DeviceTypeException{
		this.setControl(type, id, OnOffActuatorConstant.Command.ON);
	}
	
	public void close(String type, int id) throws AdsException, DeviceTypeException{
		this.setControl(type, id, OnOffActuatorConstant.Command.OFF);
	}
	
	
	// 
	private void setControl(String type, int id, OnOffActuatorConstant.Command command) throws AdsException, DeviceTypeException{
		this.accessDeviceControl(type, id, command);
	}
	
	// 
	public String getControlStatus(String type, int id) throws AdsException, DeviceTypeException{
		byte value = this.accessDeviceStatus(type, id, 1,1)[0];
		return OnOffActuatorConstant.State.getName(value);

	}	
	

	/***************  Mode Control and Status 
	 * 
	 * 	 setMode("config.mode",CO2SystemConstant.ModeCommand.AUTOMATIC);
	 *   getMode("config.mode") 
	 *   
	 *  ***************/
		
	// 
	public void setMode(String name, CO2SystemConstant.ModeCommand command) throws AdsException{
		this.configMode(name, command);
	}
	// 
	public String getMode(String name) throws AdsException{
		byte value = this.getModeStatus(name, 1)[0];
		return CO2SystemConstant.ModeState.getName(value);
	}	
	

	/*************** set CO2 LOW and HIGH limit  
	 * 
	 *   setAttribute("config.attr.CO2.threshold.low",  25 ) 
	 *   setAttribute("config.attr.CO2.threshold.high",  125 ) 
	 *
	 * *************/
		
	//     
	public void setAttribute(String type,  int value) throws AdsException{
		this.configAttribute( type , value  );
	}
	
	// getAttribute("config.attr.CO2.threshold")
	public int getAttribute(String type) throws AdsException, DeviceTypeException{
		return this.getAttributedStatus(type);

	}	
	
	
	/*************** subscribe to the water sensor  
	 * 
	 * subscribeToCO2(1 )
	 * unsubscribeToCO2(1)
	 * 
	 * *************/

	public void subscribeToCO2(int id) throws AdsException{
		
		long address = this.getSensorAddress("sensor.CO2", id);
		this.createNotification(address);
	}
	
	public void unsubscribeToCO2(int id) throws AdsException{
		
		long address = this.getSensorAddress("sensor.CO2", id);
		this.deleteNotification();
	}	
	
	AdsCallbackObject CO2Object;
	AdsListener CO2listener = new AdsListener();
	JNILong CO2Notification;
	public void createNotification(long indexOffset) throws AdsException{	 
        // Create and add listener
		CO2Object = new AdsCallbackObject();
		CO2Object.addListenerCallbackAdsState(CO2listener);  
		CO2Notification = new JNILong();       
        getADSConnection().createNotification(indexOffset, CO2Notification, CO2listener);
	}

	public void deleteNotification() throws AdsException{
        // Delete listener
		CO2Object.removeListenerCallbackAdsState(CO2listener);
        getADSConnection().deleteNotification(CO2Notification);
	}	
	
}
