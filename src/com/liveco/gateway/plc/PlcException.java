package com.liveco.gateway.plc;



public class PlcException extends Exception{
	public PlcException(String errMessage){
		super(errMessage);
	}

	public PlcException(long err){
		
	}	
	
	private static final long serialVersionUID= 1L;
	
	public String getMessage(){
		return super.getMessage();
	}
}
