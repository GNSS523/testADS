package com.liveco.gateway.system;

import com.liveco.gateway.constant.structure.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class AeroponicsSystem extends BaseSystem{
	
	public static final SystemStructure type = SystemStructure.AEROPONICS_SYSTEM;
	
	public AeroponicsSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public AeroponicsSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public AeroponicsSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}
	
	public void configMode(byte mode){
		
	}
	
	
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
