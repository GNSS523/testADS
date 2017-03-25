package com.liveco.gateway.constant.command;

import java.util.HashMap;
import java.util.Map;

public enum AirConditionerCommand {
	
	ON((byte)1),
	OFF((byte)2),
	COLD((byte)4),
	HOT((byte)8);
	
	private final byte value;
	AirConditionerCommand(byte value){
		this.value = value;
	}
		
	public byte getValue(){
		return this.value;
	}	

	private static final Map<String, AirConditionerCommand> lookup = new HashMap<String, AirConditionerCommand>();
	
	static {
		for(AirConditionerCommand command : AirConditionerCommand.values()){
			lookup.put(command.name(),command );
		}
	}
	
	public static AirConditionerCommand get(String name){
		return lookup.get(name);
	}

	public static byte getValue(String name){
		return lookup.get(name).getValue();
	}		
	
}
