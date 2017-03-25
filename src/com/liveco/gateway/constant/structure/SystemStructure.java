package com.liveco.gateway.constant.structure;

import java.util.HashMap;
import java.util.Map;


public enum SystemStructure {
	
	SHELF_LIGHTING_SYSTEM("shelf_lighting"),
	PANEL_LIGHTING_SYSTEM("panel_lighting"),
	NUTRIENT_SYSTEM("nutrient_system"),
	HYDROPONICS("hydroponics"),
	AEROPONICS_SYSTEM("aeroponics"),
	FOGPONICS_SYSTEM("fogponics"),
	CO2_SYSTEM("co2_system"),
	AIR_CONDITIONING_SYSTEM("air_conditioning");

	private final String symbol;
	SystemStructure(String symbol){
		this.symbol = symbol;
	}
		
	public String getSymbol(){
		return this.symbol;
	}
	
}
