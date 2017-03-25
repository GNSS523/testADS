package com.liveco.gateway.constant.state;

import java.util.HashMap;
import java.util.Map;


public enum NutrientSystemState {
	
	MANUAL((byte)1, "MANUAL"),
	SEMI_AUTOMATIC((byte)2, "SEMI_AUTOMATIC"),
	AUTOMATIC((byte)4,"AUTOMATIC");
	
	private final byte value;
	private final String descritpion;

	NutrientSystemState(byte value, String descritpion){
		this.value = value;
		this.descritpion = descritpion;

	}
		
	public byte getValue(){
		return this.value;
	}	
	public String getDescritpion(){
		return this.descritpion;
	}
	
	private static final Map<Byte, NutrientSystemState> lookup = new HashMap<Byte, NutrientSystemState>();
	
	static {
		for(NutrientSystemState command : NutrientSystemState.values()){
			lookup.put(command.getValue(),command );
		}
	}
	
	public static NutrientSystemState get(byte value){
		return lookup.get(value);
	}

	public static String getName(byte value){
		return lookup.get(value).name();
	}	
}
