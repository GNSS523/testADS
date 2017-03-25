package com.liveco.gateway.constant.structure;

import java.util.HashMap;
import java.util.Map;

public enum PanelLighting {

/*

*/	
	
	RED(           (byte)0, (byte)1,  "actuator.led",  "actuator.led", (byte)1),
	BLUE(          (byte)1, (byte)1 , "actuator.led",  "actuator.led", (byte)2),
	GREEN(         (byte)2, (byte)1,  "actuator.led",  "actuator.led", (byte)3),
	FAR_RED(       (byte)3, (byte)1,  "actuator.led",  "actuator.led", (byte)4),
	UV(            (byte)4, (byte)1,  "actuator.led",  "actuator.led", (byte)5 );
	
	private final byte offset;
	private final byte number;
	private final String type;
	private final String descritpion;
	private final byte id;
	PanelLighting(byte offset, byte number, String type, String descritpion, byte id){
		this.offset = offset;
		this.number = number;
		this.type = type;
		this.descritpion = descritpion;
		this.id = id;
	}
		
	public byte getOffset(){
		return this.offset;
	}
	public byte getNumber(){
		return this.number;
	}
	public String getType(){
		return this.type;
	}		
	public String getDescritpion(){
		return this.descritpion;
	}	
	public byte getId(){
		return this.id;
	}	
	private static final Map<String, PanelLighting> lookup = new HashMap<String, PanelLighting>();
	
	static {
		for(PanelLighting ads : PanelLighting.values()){
			if(ads.getId()!=-1)
				lookup.put(ads.getType()+"_"+ads.getId() ,  ads );
			else
				lookup.put(ads.getType(),ads );
		}
	}
	
	public static PanelLighting get(String name, int id){
		return lookup.get(name+"_"+id);
	}

	public static byte getOffset(String name, int id){
		return lookup.get(name+"_"+id).getOffset();
	}	
	
	public static byte getNumber(String name, int id){
		return lookup.get(name+"_"+id).getNumber();
	}	

	public static String getType(String name, int id){
		return lookup.get(name+"_"+id).getType();
	}	
	
	
	public static int getTotalLength(){
		int total = 0;
		for(PanelLighting ads : PanelLighting.values()){
			total += ads.getNumber();
		}		
		return total;
	}	
	
}
