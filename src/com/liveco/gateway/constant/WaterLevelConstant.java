package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class WaterLevelConstant {

	public enum State{
		
		HH((byte)1, "HH"),
		H((byte)2, "H"),
		L((byte)4,"LL"),
		LL((byte)8,"L");
		
		private final byte value;
		private final String descritpion;
		State(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;
		}
			
		public byte getValue(){
			return this.value;
		}

		public String getDescritpion(){
			return this.descritpion;
		}	
		
		private static final Map<Byte, State> lookup = new HashMap<Byte, State>();
		
		static {
			for(State command : State.values()){
				lookup.put(command.getValue(),command );
			}
		}
		
		public static State get(byte value){
			return lookup.get(value);
		}

		public static String getName(byte value){
			return lookup.get(value).name();
		}		
	
	}
	
	public static void test(){
    	System.out.println( WaterLevelConstant.State.HH + " "+ WaterLevelConstant.State.getName((byte)2)  );		
	}	
	
}
