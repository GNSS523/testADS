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

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class AirConditionerInner  extends BaseSystem{
    private static final Logger LOG = LogManager.getLogger(AirConditioner.class);

	public static final SystemStructure type = SystemStructure.AIR_CONDITIONING_SYSTEM;
	
	public static int control_data_length = AirConditionerConstant.ControlTable.getTotalLength();
	public static int status_data_length = AirConditionerConstant.StatusTable.getTotalLength();

	
	public AirConditionerInner(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public AirConditionerInner(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public AirConditionerInner(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}

	public String getType(){
		return SystemStructure.AIR_CONDITIONING_SYSTEM.name();
	}
	
	public byte getControlTableFieldOffset(String type, int id){
		return AirConditionerConstant.ControlTable.getOffset(type,id);
	}
	
	public byte getControlTableFieldOffset(String name){
		return AirConditionerConstant.ControlTable.getOffset(name);
	}	
	
	public byte getControlTableFieldNumberOfByte(String name){
		return AirConditionerConstant.ControlTable.getNumber(name);
	}	
	
	public byte getTableFieldOffset(String type, int id){
		return AirConditionerConstant.StatusTable.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return AirConditionerConstant.StatusTable.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return AirConditionerConstant.StatusTable.getNumber(name);
	}
	
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
	
	public String getModeStatus() {
		return "";
	}
	
	public void setTemperatureStatus() {
		
	}
	
	public int getTemperature() {
		
		return 0;
	}
	
	public String getOperationStatus() {
		return "";
	}
	
	public String getFanStatus() {
		return "";
	}
	
	public String getErrorInformation() {
		return "";
	}
	
}
