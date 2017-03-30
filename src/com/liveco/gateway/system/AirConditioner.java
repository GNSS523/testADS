package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.AirConditionerConstant;
import com.liveco.gateway.constant.OnOffActuatorConstant;
import com.liveco.gateway.constant.SystemStructure;
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
