package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.FogponicsConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;

import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;

public class FogponicsSystem extends HydroponicsSystem{

    private static final Logger LOG = LogManager.getLogger(FogponicsSystem.class);
	
	public static final SystemStructure type = SystemStructure.FOGPONICS_SYSTEM;
	public static int data_length = FogponicsConstant.Table.getTotalLength();
	
	public FogponicsSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public FogponicsSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public FogponicsSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}

	
	public byte getTableFieldOffset(String type, int id){
		return FogponicsConstant.Table.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return FogponicsConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return FogponicsConstant.Table.getNumber(name);
	}	
	

	/***************  Pump, Valve Control and Status 
	 * 
	 * open("actuator.atomizer",1)
	 * close("actuator.atomizer",1)
	 * setControl("actuator.atomizer",1,OnOffActuatorConstant.Command.ON )
	 * getControlStatus("actuator.atomizer",1 )
	 * 
	 * *************/	

	
	
	
	
	
	/*************** set Pump Running and Stop time  
	 * 
	 * setAttribute("config.attr.atomizer.time.run",  25 )
	 * getAttribute("config.attr.atomizer.time.stop")
	 * 
	 * *************/	
}
