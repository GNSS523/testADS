package com.liveco.gateway.constant.command;

import java.util.HashMap;
import java.util.Map;

public enum IrrigationSystemCommand {

	MANUAL((byte)1, "MANUAL"),
	SEMI_AUTOMATIC((byte)2, "SEMI_AUTOMATIC"),
	AUTOMATIC((byte)4,"AUTOMATIC"),
	CIRCULATE((byte)8,"CIRCULATE"),
	INLET((byte)16,"INLET"),
	OUTLET((byte)32,"OUTLET");
	
	private final byte value;
	private final String descritpion;
	IrrigationSystemCommand(byte value, String descritpion){
		this.value = value;
		this.descritpion = descritpion;
	}
		
	public byte getValue(){
		return this.value;
	}	
	public String getDescritpion(){
		return this.descritpion;
	}
	private static final Map<String, IrrigationSystemCommand> lookup = new HashMap<String, IrrigationSystemCommand>();
	
	static {
		for(IrrigationSystemCommand command : IrrigationSystemCommand.values()){
			lookup.put(command.name(),command );
		}
	}
	
	public static IrrigationSystemCommand get(String name){
		return lookup.get(name);
	}

	public static byte getValue(String name){
		return lookup.get(name).getValue();
	}			
	
}
