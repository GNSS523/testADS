package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class PanelLightingConstant {

	public enum Table {
	
			RED(       (byte)0, (byte)2,  "actuator.led.red",    "actuator.led red ",     (byte)1),
			BLUE(      (byte)2, (byte)2 , "actuator.led.blue",   "actuator.led blue",     (byte)2),
			GREEN(     (byte)4, (byte)2,  "actuator.led.green",  "actuator.led green",    (byte)3),
			FAR_RED(   (byte)6, (byte)2,  "actuator.led.far_red","actuator.led far_red",  (byte)4),
			UV(        (byte)8, (byte)2,  "actuator.led.uv",     "actuator.led uv",       (byte)5);
			
			private final byte offset;
			private final byte number;
			private final String type;
			private final String descritpion;
			private final byte id;
			Table(byte offset, byte number, String type, String descritpion, byte id){
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
			private static final Map<String, Table> lookup = new HashMap<String, Table>();
			
			static {
				for(Table ads : Table.values()){
					lookup.put(ads.getType() ,  ads );
				}
			}
			
			public static Table get(String name){
				return lookup.get(name);
			}

			public static byte getOffset(String name){
				return lookup.get(name).getOffset();
			}	
			
			public static byte getNumber(String name){
				return lookup.get(name).getNumber();
			}	

			public static String getType(String name){
				return lookup.get(name).getType();
			}	
					
			public static int getTotalLength(){
				int total = 0;
				for(Table ads : Table.values()){
					total += ads.getNumber();
				}		
				return total;
			}	
		}
}
