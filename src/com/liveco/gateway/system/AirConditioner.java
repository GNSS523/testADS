package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.AirConditionerConstant;
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

public class AirConditioner extends BaseSystem{

    private static final Logger LOG = LogManager.getLogger(AirConditioner.class);

	public static final SystemStructure type = SystemStructure.AIR_CONDITIONING_SYSTEM;
	
	public static int data_length = AirConditionerConstant.Table.getTotalLength();
	
	public AirConditioner(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public AirConditioner(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public AirConditioner(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}


	
	public byte getTableFieldOffset(String type, int id){
		return AirConditionerConstant.Table.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return AirConditionerConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return AirConditionerConstant.Table.getNumber(name);
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
		
		LOG.debug("AirConditioner parseCommand  "+type+"  "+command+"  "+long_name);
		/*
		switch(type){
				
			case "attribute":
				name = type+"."+long_name;
				this.setAttribute( name , Integer.parseInt(command) );
				break;
				
			case "mode":
				cmd = HydroponicsConstant.ModeCommand.get(command);
				name = type;
				this.setMode(name, (HydroponicsConstant.ModeCommand)cmd);
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

		LOG.debug("AirConditioner parseState  "+type+"  "+command+"  "+long_name);
		
		/*
		switch(type){
								
			case "mode":
				this.getMode(type); 
				System.out.println(" get the mode  : ");
				break;
				
			case "attribute":
				name = type;				
				this.getAttributedStatus(name);
				System.out.println(" get the attribute  : ");
				break;
				
		}
		*/		
	}
	
	
	
	
	
	
	public void open(String type) throws AdsException{
		this.setControl(type, AirConditionerConstant.Command.ON);
	}
	
	public void close(String type) throws AdsException{
		this.setControl(type, AirConditionerConstant.Command.OFF);
	}

	public void setHot(String type) throws AdsException{
		this.setControl(type, AirConditionerConstant.Command.HOT);
	}
	
	public void setCold(String type) throws AdsException{
		this.setControl(type, AirConditionerConstant.Command.COLD);
	}	
	
	public void getStatus(){
		
	}
	
	/*   
	     
	 */
	private void setControl(String name, AirConditionerConstant.Command command) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name);
		byte values[] = {command.getValue()};
		this.writeByteArray(address, values);
	}
	
	private byte getControlStatus(String name) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name) + 1;
		return this.readByteArray(address, 1)[0];		
	}
	
	/*
	 * 
	 */
	public void setTemperature(String name, int value) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name);
		long number = this.getBaseAddress() + (long)AirConditionerConstant.Table.getNumber(name);
		
	}
	
	public int getTemperature(String name) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name) + 1;
		long number = this.getBaseAddress() + (long)AirConditionerConstant.Table.getNumber(name);
		return 0;	
	}	
	
	
	/*  notification  */
	AdsCallbackObject temperatureObject;
	AdsListener temperaturelistener = new AdsListener();
	JNILong temperatureNotification;
	public void createNotification(long indexOffset) throws AdsException{	 
        // Create and add listener
		temperatureObject = new AdsCallbackObject();
		temperatureObject.addListenerCallbackAdsState(temperaturelistener);  
		temperatureNotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, temperatureNotification, temperaturelistener);
	}

	public void deleteNotification() throws AdsException{
        // Delete listener
		temperatureObject.removeListenerCallbackAdsState(temperaturelistener);
        getADSConnection().deleteNotification(temperatureNotification);
	}	
	
	
	public void test() throws AdsException{

		this.setControl(  "config.mode",  AirConditionerConstant.Command.ON);

		this.setControl(  "config.mode",  AirConditionerConstant.Command.HOT);

		this.getControlStatus("config.mode");
		
		//this.setTemperature();
		
		//this.getTemperature();
		
		
	}
	
}
