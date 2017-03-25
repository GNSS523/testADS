package com.liveco.gateway.system;

import com.liveco.gateway.constant.structure.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;

public class AirConditioner  extends BaseSystem{

	public static final SystemStructure type = SystemStructure.AIR_CONDITIONING_SYSTEM;
	
	public AirConditioner(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public AirConditioner(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public AirConditioner(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}
	
	public void open(){
		
	}
	
	public void close(){
		
	}
	
	public void getStatus(){
		
	}
	
	public void setTemperature(){
		
	}
	
	public int getTemperature(){
		return 0;
	}
	
}
