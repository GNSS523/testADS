package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.AirConditionerConstant;
import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.mqtt.MqttCommand;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;
import com.liveco.gateway.plc.DeviceTypeException;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class AirConditioner extends BaseSystem{

    private static final Logger LOG = LogManager.getLogger(AirConditioner.class);

	public static final SystemStructure type = SystemStructure.AIR_CONDITIONING_SYSTEM;
	
	public static int control_data_length = AirConditionerConstant.ControlTable.getTotalLength();
	public static int status_data_length = AirConditionerConstant.StatusTable.getTotalLength();

	
	public AirConditioner(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public AirConditioner(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public AirConditioner(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}

	public String getType(){
		return SystemStructure.AIR_CONDITIONING_SYSTEM.name();
	}
	
	
	
	public byte getTableFieldOffset(String type, int id){
		return AirConditionerConstant.ControlTable.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return AirConditionerConstant.ControlTable.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return AirConditionerConstant.ControlTable.getNumber(name);
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
	
	
	
	
	
	
	/* 	

  
	     

	private void setControl(String name, AirConditionerConstant.Command command) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name);
		byte values[] = {command.getValue()};
		this.writeByteArray(address, values);
	}
	
	private byte getControlStatus(String name) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name) + 1;
		return this.readByteArray(address, 1)[0];		
	}
	 */	
	/*
	 * 

	public void setTemperature(String name, int value) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name);
		long number = this.getBaseAddress() + (long)AirConditionerConstant.Table.getNumber(name);
		
	}
	
	public int getTemperature(String name) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.Table.getOffset(name) + 1;
		long number = this.getBaseAddress() + (long)AirConditionerConstant.Table.getNumber(name);
		return 0;	
	}	
	
	
	 */	

	public void setWorkMode(String name, AirConditionerConstant.WorkCommand command) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.ControlTable.getOffset(name);
		byte values[] = {command.getValue()};
		System.out.println(".....................................setWorkMode   "+ this.getBaseAddress()+ "     " +address+"    "+name+"    "+values[0]);
		this.writeByteArray(address, values);
	}
	
	public void setOperationMode(String name, AirConditionerConstant.OperationCommand command) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.ControlTable.getOffset(name);
		byte values[] = {command.getValue()};
		this.writeByteArray(address, values);
	}	

	public void setFanMode(String name, AirConditionerConstant.FanCommand command) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.ControlTable.getOffset(name);
		byte values[] = {command.getValue()};
		System.out.println(".....................................setFanMode   "+ this.getBaseAddress()+ "     " +address+"    "+name+"    "+values[0]);
		this.writeByteArray(address, values);
	}
	
	public void setTemperature(String name, int temp) throws AdsException{
		long address = this.getBaseAddress() + (long)AirConditionerConstant.ControlTable.getOffset(name);
		//byte values[] = temp.;
		//this.writeByteArray(address, values);
	}	
	
	public float getWorkMode() throws AdsException, DeviceTypeException{
		byte values[]= this.getRawArray(4,2);
		LOG.debug("getWorkMode   "+ values[0]+ " "+ values[1]+"   "+ "   " +"    "+Convert.ByteArrToFloat(values) );
		return Convert.ByteArrToFloat(values);
	}	
	
	public float getFanMode() throws AdsException, DeviceTypeException{
		byte values[]= this.getRawArray(6,2);
		LOG.debug("getFanMode   "+ values[0]+ " "+ values[1]+"   "+"   " +"    "+Convert.ByteArrToFloat(values) );
		return Convert.ByteArrToFloat(values);
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
	
	
	public void test1() throws AdsException{


		
	}
	
}
