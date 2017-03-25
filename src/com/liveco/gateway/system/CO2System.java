package com.liveco.gateway.system;

import com.liveco.gateway.constant.structure.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class CO2System extends BaseSystem {
	
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
