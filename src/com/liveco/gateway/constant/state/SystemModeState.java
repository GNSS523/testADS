package com.liveco.gateway.constant.state;

import java.util.HashMap;
import java.util.Map;


public enum SystemModeState {

	RUNNING((byte)1),
	MAINTAINENCE((byte)2);
	
	private final byte value;
	SystemModeState(byte value){
		this.value = value;
	}

		
	public byte getValue(){
		return this.value;
	}	

	private static final Map<Byte, SystemModeState> lookup = new HashMap<Byte, SystemModeState>();
	
	static {
		for(SystemModeState command : SystemModeState.values()){
			lookup.put(command.getValue(),command );
		}
	}
	
	public static SystemModeState get(byte value){
		return lookup.get(value);
	}

	public static String getName(byte value){
		return lookup.get(value).name();
	}	
	
}
