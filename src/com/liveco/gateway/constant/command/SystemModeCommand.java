package com.liveco.gateway.constant.command;

import java.util.HashMap;
import java.util.Map;

public enum SystemModeCommand {

	RUNNING((byte)1),
	MAINTAINENCE((byte)2);
	
	private final byte value;
	SystemModeCommand(byte value){
		this.value = value;
	}
		
	public byte getValue(){
		return this.value;
	}	

	private static final Map<String, SystemModeCommand> lookup = new HashMap<String, SystemModeCommand>();
	
	static {
		for(SystemModeCommand command : SystemModeCommand.values()){
			lookup.put(command.name(),command );
		}
	}
	
	public static SystemModeCommand get(String name){
		return lookup.get(name);
	}

	public static byte getValue(String name){
		return lookup.get(name).getValue();
	}		
	
}
