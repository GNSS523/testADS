package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.constant.SystemConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;

import de.beckhoff.jni.Convert;

public class MainSystem extends BaseSystem{
    private static final Logger LOG = LogManager.getLogger(HydroponicsSystem.class);
    
	public static final SystemStructure type = SystemStructure.MAIN;

	public MainSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public MainSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public MainSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}
	
	public int getTableOffset(){
		return 4;
	}	
	
	public byte getTableFieldOffset(String name){
		return SystemConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return SystemConstant.Table.getNumber(name);
	}
	
	public void setMode(ICommand command) throws AdsException{
		this.configMode("config.system.mode.set", command);
	}
	public String getMode() throws AdsException{
		byte values[] = this.getModeStatus("config.system.mode.get", 2,0);
		System.out.println(values[0]+"   "+values[1]);
		int value = Convert.ByteArrToShort(values);
		return SystemConstant.ModeState.getName((byte)value);
	}	
}
