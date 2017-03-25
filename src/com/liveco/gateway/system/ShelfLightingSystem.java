package com.liveco.gateway.system;

import com.liveco.gateway.constant.structure.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;

public class ShelfLightingSystem extends BaseSystem{

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
}
