package com.liveco.gateway.constant.structure;

import java.util.HashMap;
import java.util.Map;

public enum AirConditionerADS {
	
	CONFIG_TEMPERATURE(    (byte)0, (byte)4,  "config.mode"   ,   "out valve", (byte)-1),
	GET_TEMPERATURE(    (byte)4, (byte)4,  "config.fill"  ,    "out valve", (byte)-1),
	CONFIG_MODE(  (byte)8, (byte)4,  "config.pump.runtime" ,"config.pump.runtime", (byte)-1),
	CONFIG_PUMP_STOP_TIME( (byte)12, (byte)4,  "config.pump.stoptime","config.pump.stoptim", (byte)-1 );
	
	private final byte offset;
	private final byte number;
	private final String type;
	private final String descritpion;
	private final byte id;
	AirConditionerADS(byte offset, byte number, String type, String descritpion, byte id){
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
	private static final Map<String, AirConditionerADS> lookup = new HashMap<String, AirConditionerADS>();
	
	static {
		for(AirConditionerADS ads : AirConditionerADS.values()){
			if(ads.getId()!=-1)
				lookup.put(ads.getType()+"_"+ads.getId() ,  ads );
			else
				lookup.put(ads.getType(),ads );
		}
	}
	
	public static AirConditionerADS get(String name, int id){
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

	
	public static AirConditionerADS get(String name){
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
		for(AirConditionerADS ads : AirConditionerADS.values()){
			total += ads.getNumber();
		}		
		return total;
	}
	
}
