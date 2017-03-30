package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.NutrientSystemConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class NurientSystem  extends BaseSystem{

    private static final Logger LOG = LogManager.getLogger(NurientSystem.class);
	
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
	

	public byte getTableFieldOffset(String type, int id){
		return NutrientSystemConstant.Table.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return NutrientSystemConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return NutrientSystemConstant.Table.getNumber(name);
	}
	
	
	
	
	
	
	
		
	
	/*************** subscribe to the PH sensor  
	 * 
	 * subscribeToPH(1 )
	 * unsubscribeToPH(1)
	 * 
	 * *************/	

	public void subscribeToPH(int id) throws AdsException{
		
		long address = this.getSensorAddress("sensor.PH", id);
		this.createPHNotification(address);
	}
	
	public void unsubscribeToPH(int id) throws AdsException{
		
		long address = this.getSensorAddress("sensor.PH", id);
		this.deletePHNotification();
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

	/*************** subscribe to the EC sensor  
	 * 
	 * subscribeToEC(1 )
	 * unsubscribeToEC(1)
	 * 
	 * *************/	

	public void subscribeToEC(int id) throws AdsException{
		
		long address = this.getSensorAddress("sensor.EC", id);
		this.createECNotification(address);
	}
	
	public void unsubscribeToEC(int id) throws AdsException{
		
		long address = this.getSensorAddress("sensor.EC", id);
		this.deleteECNotification();
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
