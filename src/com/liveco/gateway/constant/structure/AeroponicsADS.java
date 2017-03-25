package com.liveco.gateway.constant.structure;

import java.util.HashMap;
import java.util.Map;

public enum AeroponicsADS{

	/*
    stPump:ST_PumpOrValve;
	stValveIn:ST_PumpOrValve;
	stValveOut:ST_PumpOrValve;
	
	byHMICmd:BYTE;          //Cmd:=1_Manual;=2_Config;=4_Work;=8_Clean Up;=16_Fill in£»=32_check
	byHMIStatus:BYTE;       //Status:=1_Manual;=2_Config;=4_Work;=8_Clean up;=16_Fill in;=32_check
	byRequestFill:BYTE;    //=1 request
	byAgreeFill:BYTE;      //=1 agree
	iPumpRunTime:INT;      //minute
	iPumpStopTime:INT;     //minute
*/	
	
	PUMP(           (byte)0, (byte)2,  "actuator.pump",    "pump"    , (byte)1),
	VALVE_IN(       (byte)2, (byte)2 , "actuator.valve",   "in valve", (byte)1),
	VALVE_OUT(      (byte)4, (byte)2,  "actuator.valve",   "out valve", (byte)2),
	LEVEL_SENSOR_1( (byte)6, (byte)1,  "sensor.water_level_sensor",   "sensor.water_level_sensor", (byte)1 ),
	LEVEL_SENSOR_2( (byte)7, (byte)1,  "sensor.water_level_sensor",   "sensor.water_level_sensor", (byte)2 ),
	ATOMIZER_1 ( (byte)7, (byte)1,  "sensor.water_level_sensor",   "sensor.water_level_sensor", (byte)2 ),
	
	
	CONFIG_MODE(    (byte)8, (byte)2,  "config.mode"   ,   "out valve", (byte)-1),
	CONFIG_FILL(    (byte)10, (byte)2,  "config.fill"  ,    "out valve", (byte)-1),
	CONFIG_PUMP_RUN_TIME(  (byte)12, (byte)4,  "config.pump.runtime" ,"config.pump.runtime", (byte)-1),
	CONFIG_PUMP_STOP_TIME( (byte)16, (byte)4,  "config.pump.stoptime","config.pump.stoptim", (byte)-1 );
	

	
	private final byte offset;
	private final byte number;
	private final String type;
	private final String descritpion;
	private final byte id;
	AeroponicsADS(byte offset, byte number, String type, String descritpion, byte id){
		this.offset = offset;
		this.number = number;
		this.type = type;
		this.descritpion = descritpion;
		this.id = id;
	}
		
	public byte getOffset(){
		return this.offset;
	}
	public byte getNumber(){
		return this.number;
	}
	public String getType(){
		return this.type;
	}		
	public String getDescritpion(){
		return this.descritpion;
	}	
	public byte getId(){
		return this.id;
	}	
	private static final Map<String, AeroponicsADS> lookup = new HashMap<String, AeroponicsADS>();
	
	static {
		for(AeroponicsADS ads : AeroponicsADS.values()){
			if(ads.getId()!=-1)
				lookup.put(ads.getType()+"_"+ads.getId() ,  ads );
			else
				lookup.put(ads.getType(),ads );
		}
	}
	
	public static AeroponicsADS get(String name, int id){
		return lookup.get(name+"_"+id);
	}

	public static byte getOffset(String name, int id){
		return lookup.get(name+"_"+id).getOffset();
	}	
	
	public static byte getNumber(String name, int id){
		return lookup.get(name+"_"+id).getNumber();
	}	

	public static String getType(String name, int id){
		return lookup.get(name+"_"+id).getType();
	}	

	
	public static AeroponicsADS get(String name){
		return lookup.get(name);
	}

	public static byte getOffset(String name){
		return lookup.get(name).getOffset();
	}	
	
	public static byte getNumber(String name){
		return lookup.get(name).getNumber();
	}	

	public static String getType(String name){
		return lookup.get(name).getType();
	}	
	
	public static int getTotalLength(){
		int total = 0;
		for(AeroponicsADS ads : AeroponicsADS.values()){
			total += ads.getNumber();
		}		
		return total;
	}	
}
