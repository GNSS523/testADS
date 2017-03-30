package com.liveco.gateway.mqtt;

public class MqttCommand {

	private String name;
	private String type;
	private String value;
	private int ack;
	
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public void setACK(int ack){
		this.ack = ack;
	}
	
	public int getACK(){
		return this.ack;
	}
}
