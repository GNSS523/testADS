package com.liveco.gateway.plc;

public class DeviceTypeException extends Exception{
	public DeviceTypeException(String errMessage){
		super(errMessage);
	}

	public DeviceTypeException(long err){
		
	}	
	
	private static final long serialVersionUID= 1L;
	
	public String getMessage(){
		return super.getMessage();
	}
}