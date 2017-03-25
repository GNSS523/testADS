package com.liveco.gateway.constant.state;

public enum AirConditonerState {

	OFF((byte)0),
	ON((byte)1),
	COLD((byte)4),
	HOT((byte)8);
	
	private final byte value;
	AirConditonerState(byte value){
		this.value = value;
	}
		
	public byte getValue(){
		return this.value;
	}
	
}
