package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.OnOffActuatorConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

public class ShelfLightingSystem extends BaseSystem{

    private static final Logger LOG = LogManager.getLogger(ShelfLightingSystem.class);
	
	public static final SystemStructure type = SystemStructure.SHELF_LIGHTING_SYSTEM;
		
	public ShelfLightingSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public ShelfLightingSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public ShelfLightingSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}
	

	/***************  Panel lighting Control on all the channels 
	 * 
	 * open(0)
	 * close(0)
	 * 
	 * *************/
	public void open(int index) throws AdsException, DeviceTypeException{
		this.setControl( index, OnOffActuatorConstant.Command.ON);
	}
	
	public void close(int index) throws AdsException, DeviceTypeException{
		this.setControl(index, OnOffActuatorConstant.Command.OFF);
	}
	
	// controlSimpleDevice("actuator.pump",1,OnOffActuatorConstant.Command.ON )
	private void setControl(int index, OnOffActuatorConstant.Command command) throws AdsException{
		this.accessDeviceControl(index, command);
	}	
	
}
