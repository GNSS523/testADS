package com.liveco.gateway.constant.state;

import java.util.HashMap;
import java.util.Map;

public enum IrrigationSystemState {
	
	MANUAL((byte)1, "MANUAL"),
	SEMI_AUTOMATIC((byte)2, "SEMI_AUTOMATIC"),
	AUTOMATIC((byte)4,"AUTOMATIC"),
	CIRCULATE((byte)8,"CIRCULATE"),
	INLET((byte)16,"INLET"),
	OUTLET((byte)32,"OUTLET");
	
	private final byte value;
	private final String descritpion;
	IrrigationSystemState(byte value, String descritpion){
		this.value = value;
		this.descritpion = descritpion;
	}
		
	public byte getValue(){
		return this.value;
	}

	public String getDescritpion(){
		return this.descritpion;
	}	
	
	private static final Map<Byte, IrrigationSystemState> lookup = new HashMap<Byte, IrrigationSystemState>();
	
	static {
		for(IrrigationSystemState command : IrrigationSystemState.values()){
			lookup.put(command.getValue(),command );
		}
	}
	
	public static IrrigationSystemState get(byte value){
		return lookup.get(value);
	}

	public static String getName(byte value){
		return lookup.get(value).name();
	}
}
