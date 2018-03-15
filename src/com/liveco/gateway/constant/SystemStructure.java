package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

/*
 * 
VAR_GLOBAL
	stModeChage:ST_ModeChange;
	stWaterCirculationSystem:ARRAY[1..5] OF   ST_SingleCircSysInterface;
	stHalfAeroponicCirculationSystem:ST_SingleCircSysInterface2;
	stConfig:ST_Config;
	stCD:ARRAY[1..4] OF ST_CarbonDioxide;
	stIndoorCondition:ARRAY[1..5] OF ST_IndoorCondition;
	
	nLedTube:ARRAY[1..6] OF ST_LEDLampTube;     //A3,A4,B1~B4 room;=1,OPEN;=0,CLOSE
	nLedTubeA1: ST_LEDLampTube;         //A1
    nLedPanalA1:ARRAY[1..6] OF ST_LEDLampPanel;     //A1
    nLedPanalA2:ARRAY[1..12] OF ST_LEDLampPanel;     //A2
END_VAR
 * 
 */


public enum SystemStructure {
	
	MAIN("main_system"),
	SHELF_LIGHTING_SYSTEM("shelf_lighting"),
	PANEL_LIGHTING_SYSTEM("panel_lighting"),
	NUTRIENT_SYSTEM("nutrient_system"),
	HYDROPONICS("hydroponics"),
	AEROPONICS_SYSTEM("aeroponics"),
	FOGPONICS_SYSTEM("fogponics"),
	CO2_SYSTEM("co2_system"),
	AIR_CONDITIONING_SYSTEM("air_conditioning"),
	AIR_CONDITIONING_INNER_SYSTEM("air_conditioning_inner");

	private final String symbol;
	SystemStructure(String symbol){
		this.symbol = symbol;
	}
		
	public String getSymbol(){
		return this.symbol;
	}
	
}
