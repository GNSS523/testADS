package com.liveco.gateway.constant.state;

import java.util.HashMap;
import java.util.Map;

public enum OnOffState {
	
	OFF((byte)0),
	ON((byte)1),
	ERROR((byte)2);
	
	private final byte value;
	OnOffState(byte value){
		this.value = value;
	}
	
	public byte getValue(){
		return this.value;
	}	

	private static final Map<Byte, OnOffState> lookup = new HashMap<Byte, OnOffState>();
	
	static {
		for(OnOffState command : OnOffState.values()){
			lookup.put(command.getValue(),command );
		}
	}
	
	public static OnOffState get(byte value){
		
		OnOffState state = lookup.get(value);
		if(state == null) throw new NullPointerException();
		return state;
	}

	public static String getName(byte value){
		return lookup.get(value).name();
	}		
	
}
