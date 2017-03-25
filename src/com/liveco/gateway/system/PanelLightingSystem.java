package com.liveco.gateway.system;

import com.liveco.gateway.constant.structure.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;

public class PanelLightingSystem extends BaseSystem{

	public static final SystemStructure type = SystemStructure.PANEL_LIGHTING_SYSTEM;
			
	public PanelLightingSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public PanelLightingSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public PanelLightingSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}	
}
