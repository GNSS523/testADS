package com.liveco.gateway.system;

import com.liveco.gateway.constant.structure.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class NurientSystem  extends BaseSystem{

	public static final String type = SystemStructure.NUTRIENT_SYSTEM.getSymbol();
	
	public NurientSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public NurientSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public NurientSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}
	
	public void configMode(byte mode){
		
	}
	
	
	AdsCallbackObject PHObject;
	AdsListener PHlistener = new AdsListener();
	JNILong PHNotification;
	public void createPHNotification(long indexOffset) throws AdsException{	 
        // Create and add listener
		PHObject = new AdsCallbackObject();
		PHObject.addListenerCallbackAdsState(PHlistener);  
		PHNotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, PHNotification, PHlistener);
	}

	public void deletePHNotification() throws AdsException{
        // Delete listener
		PHObject.removeListenerCallbackAdsState(PHlistener);
        getADSConnection().deleteNotification(PHNotification);
	}	
	
	AdsCallbackObject ECObject;
	AdsListener EClistener = new AdsListener();
	JNILong ECNotification;
	public void createECNotification(long indexOffset) throws AdsException{	 
        // Create and add listener
		ECObject = new AdsCallbackObject();
		ECObject.addListenerCallbackAdsState(EClistener);  
		ECNotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, ECNotification, EClistener);
	}

	public void deleteECNotification() throws AdsException{
        // Delete listener
		ECObject.removeListenerCallbackAdsState(EClistener);
        getADSConnection().deleteNotification(ECNotification);
	}	
	
}
