package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;


public class CO2SystemConstant {

	public enum ModeCommand  implements ICommand{

		MANUAL((byte)1, "MANUAL"),
		AUTOMATIC((byte)2,"AUTOMATIC");
		
		private final byte value;
		private final String descritpion;
		ModeCommand(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;
		}
			
		public byte getValue(){
			return this.value;
		}	
		public String getDescritpion(){
			return this.descritpion;
		}
		private static final Map<String, ModeCommand> lookup = new HashMap<String, ModeCommand>();
		
		static {
			for(ModeCommand command : ModeCommand.values()){
				lookup.put(command.name(),command );
			}
		}
		
		public static ModeCommand get(String name){
			return lookup.get(name);
		}

		public static byte getValue(String name){
			return lookup.get(name).getValue();
		}			
		
	}
	
	
	public enum ModeState {
		
		MANUAL((byte)0,"MANUAL"),
		AUTO((byte)1, "AUTO");
		
		private final byte value;
		private final String descritpion;
		ModeState(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;
		}
			
		public byte getValue(){
			return this.value;
		}

		public String getDescritpion(){
			return this.descritpion;
		}	
		
		private static final Map<Byte, ModeState> lookup = new HashMap<Byte, ModeState>();
		
		static {
			for(ModeState command : ModeState.values()){
				lookup.put(command.getValue(),command );
			}
		}
		
		public static ModeState get(byte value){
			return lookup.get(value);
		}

		public static String getName(byte value){
			return lookup.get(value).name();
		}
	}
		

	/*
		STRUCT
			fCDLowerLimit:REAL;//CD Min:Start Fill In
			fCDUpperLimit:REAL;//CD Max:Stop Fill In
		    fCDActualValue:REAL;//CD actual value
		END_STRUCT
		END_TYPE
		
		
		STRUCT
			iCarbonDioxideModeCMD:BYTE;//Carbon Dioxide:=1_Manual;=2_Aoto
			iCarbonDioxideModeStatus:BYTE;//Carbon Dioxide status:=1_auto;=0_manual
			stCarbonDioxidevalue:ARRAY [1..4] OF ST_CarbonDioxideValue;
			pump: ARRAY [1..4] OF ST_ValveOrPump
		END_STRUCT
		END_TYPE		
	 */	
	
	public enum Table {
		
		VALVE(              (byte)2, (byte)2 , "actuator.valve",   "in valve", (byte)1),
		CO2     (           (byte)2, (byte)4 , "sensor.CO2",       "CO2 sensor", (byte)1),
		CONFIG_MODE(        (byte)6, (byte)2,  "config.set.mode"   ,   "config the mode", (byte)-1),
		CO2_LOWER_LIMIT(    (byte)8, (byte)4,  "config.attr.CO2.threshold.low"   ,   "config the attribute", (byte)-1),
		CO2_HIGHER_LIMIT(   (byte)12, (byte)4, "config.attr.CO2.threshold.high"   ,   "config the attribute", (byte)-1);
				
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
				if(ads.getId()!=-1)
					lookup.put(ads.getType()+"_"+ads.getId() ,  ads );
				else
					lookup.put(ads.getType(),ads );
			}
		}
		
		public static Table get(String name, int id){
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
	
	
	public enum Value {
		
		
	}
	
}
