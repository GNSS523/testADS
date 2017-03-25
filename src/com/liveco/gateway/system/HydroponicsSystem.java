package com.liveco.gateway.system;

import com.liveco.gateway.constant.command.IrrigationSystemCommand;
import com.liveco.gateway.constant.command.OnOffCommand;
import com.liveco.gateway.constant.state.OnOffState;
import com.liveco.gateway.constant.structure.HydroponicsADS;
import com.liveco.gateway.constant.structure.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class HydroponicsSystem extends BaseSystem{

	public static final SystemStructure type = SystemStructure.HYDROPONICS;
	public static int data_length = HydroponicsADS.getTotalLength();

	public HydroponicsSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public HydroponicsSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public HydroponicsSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}	
		
	/*  access device */
	
	public void accessDeviceControl(String type, int id, OnOffCommand command ) throws AdsException{
		long address = this.getBaseAddress() + (long)HydroponicsADS.getOffset(type,id);
		byte values[] = {command.getValue()};
		this.writeByteArray(address, values);
	}
	
	public byte accessDeviceStatus(String type, int id, OnOffCommand command ) throws AdsException{
		long address = this.getBaseAddress() + (long)HydroponicsADS.getOffset(type,id) + 1;
		return this.readByteArray(address, 1)[0];
	}	
	
	public String accessDeviceStatus2(String type, int id, OnOffCommand command ) throws AdsException{
		byte value = accessDeviceStatus(type, id, command);
		System.out.println("accessDeviceStatus2   "+value);
		return OnOffState.getName(value);
	}	
	
	/*   
	 "config.mode"    "config.fill"   
	 */
	public void configDevice(String name, IrrigationSystemCommand command) throws AdsException{
		long address = this.getBaseAddress() + (long)HydroponicsADS.getOffset(name);
		byte values[] = {command.getValue()};
		this.writeByteArray(address, values);
	}
	
	public byte getCommandResponse(String name) throws AdsException{
		long address = this.getBaseAddress() + (long)HydroponicsADS.getOffset(name) + 1;
		return this.readByteArray(address, 1)[0];		
	}
	
	/*
	 *   "config.pump.runtime"    "config.pump.stoptime"
	 * 
	 */
	public void configAttribute(String name, int value){
		long address = this.getBaseAddress() + (long)HydroponicsADS.getOffset(name);
		long number = this.getBaseAddress() + (long)HydroponicsADS.getNumber(name);
		
	}
	
	public int getAttributedResponse(String name){
		long address = this.getBaseAddress() + (long)HydroponicsADS.getOffset(name) + 1;
		long number = this.getBaseAddress() + (long)HydroponicsADS.getNumber(name);
		return 0;	
	}	
	

	/*  notification  */
	AdsCallbackObject waterLevelObject;
	AdsListener waterLevellistener = new AdsListener();
	JNILong waterLevelNotification;
	public void createNotification(long indexOffset) throws AdsException{	 
        // Create and add listener
		waterLevelObject = new AdsCallbackObject();
		waterLevelObject.addListenerCallbackAdsState(waterLevellistener);  
		waterLevelNotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, waterLevelNotification, waterLevellistener);
	}

	public void deleteNotification() throws AdsException{
        // Delete listener
		waterLevelObject.removeListenerCallbackAdsState(waterLevellistener);
        getADSConnection().deleteNotification(waterLevelNotification);
	}	
	
}
