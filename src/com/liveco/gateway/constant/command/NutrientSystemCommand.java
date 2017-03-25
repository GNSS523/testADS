package com.liveco.gateway.constant.command;

import java.util.HashMap;
import java.util.Map;

public enum NutrientSystemCommand {
	
	MANUAL((byte)1, "MANUAL"),
	SEMI_AUTOMATIC((byte)2, "SEMI_AUTOMATIC"),
	AUTOMATIC((byte)4,"AUTOMATIC");
	
	private final byte value;
	private final String descritpion;

	NutrientSystemCommand(byte value, String descritpion){
		this.value = value;
		this.descritpion = descritpion;

	}
		
	public byte getValue(){
		return this.value;
	}	
	public String getDescritpion(){
		return this.descritpion;
	}
	private static final Map<String, NutrientSystemCommand> lookup = new HashMap<String, NutrientSystemCommand>();
	
	static {
		for(NutrientSystemCommand command : NutrientSystemCommand.values()){
			lookup.put(command.name(),command );
		}
	}
	
	public static NutrientSystemCommand get(String name){
		return lookup.get(name);
	}

	public static byte getValue(String name){
		return lookup.get(name).getValue();
	}	
}
