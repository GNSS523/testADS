package com.liveco.gateway.constant.command;

import java.util.HashMap;
import java.util.Map;

public enum OnOffCommand {

	ON((byte)1),
	OFF((byte)2);
	
	private final byte value;
	OnOffCommand(byte value){
		this.value = value;
	}

		
	public byte getValue(){
		return this.value;
	}	

	private static final Map<String, OnOffCommand> lookup = new HashMap<String, OnOffCommand>();
	
	static {
		for(OnOffCommand command : OnOffCommand.values()){
			lookup.put(command.name(),command );
		}
	}
	
	public static OnOffCommand get(String name){
		return lookup.get(name);
	}

	public static byte getValue(String name){
		return lookup.get(name).getValue();
	}	
	
}
