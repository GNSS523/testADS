package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;



public class FogponicsConstant extends IrrigationSystemConstant{
	

	public enum Table{

		/*
	    stPump:ST_PumpOrValve;
		stValveIn:ST_PumpOrValve;
		stValveOut:ST_PumpOrValve;
		
		byHMICmd:BYTE;          //Cmd:=1_Manual;=2_Config;=4_Work;=8_Clean Up;=16_Fill in��=32_check
		byHMIStatus:BYTE;       //Status:=1_Manual;=2_Config;=4_Work;=8_Clean up;=16_Fill in;=32_check
		byRequestFill:BYTE;    //=1 request
		byAgreeFill:BYTE;      //=1 agree
		iPumpRunTime:INT;      //minute
		iPumpStopTime:INT;     //minute
		*/	
		
		PUMP(           (byte)0, (byte)2,  "actuator.pump",    "pump"    ,  (byte)1),
		VALVE_IN(       (byte)2, (byte)2,  "actuator.valve",   "in valve",  (byte)1),
		VALVE_OUT(      (byte)4, (byte)2,  "actuator.valve",   "out valve", (byte)2),
		LEVEL_SENSOR(   (byte)6, (byte)1,  "sensor.water_level_sensor",   "sensor.water_level_sensor", (byte)1 ),
		ATOMIZER (      (byte)7, (byte)2,  "actuator.atomizer",   "actuator.atomizer", (byte)1 ),
		
		
		CONFIG_MODE(    (byte)9, (byte)2,  "config.mode"   ,   "config the mode ", (byte)-1),
		COMMAND_FILL(    (byte)11, (byte)2,  "command.set.fill"  ,    "command to fill in the tank", (byte)-1),
		CONFIG_PUMP_RUN_TIME(  (byte)13, (byte)4,  "config.attr.pump.runtime" ,"config.pump.runtime", (byte)-1),
		CONFIG_PUMP_STOP_TIME( (byte)17, (byte)4,  "config.attr.pump.stoptime","config.pump.stoptime", (byte)-1 );
		

		
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
	
	
	
	public enum ModeCommand  implements ICommand{

		MANUAL((byte)1, "MANUAL"),
		SEMI_AUTOMATIC((byte)2, "SEMI_AUTOMATIC"),
		AUTOMATIC((byte)4,"AUTOMATIC"),
		CIRCULATE((byte)8,"CIRCULATE"),
		INLET((byte)16,"INLET"),
		OUTLET((byte)32,"OUTLET");
		
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
		
		MANUAL((byte)1, "MANUAL"),
		SEMI_AUTOMATIC((byte)2, "SEMI_AUTOMATIC"),
		AUTOMATIC((byte)4,"AUTOMATIC"),
		CIRCULATE((byte)8,"CIRCULATE"),
		INLET((byte)16,"INLET"),
		OUTLET((byte)32,"OUTLET");
		
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
	
	
	public enum WaterLevelState{
		
		HH((byte)3, "HH"),
		H((byte)2, "H"),
		L((byte)1,"LL"),
		LL((byte)0,"L");
		
		private final byte value;
		private final String descritpion;
		WaterLevelState(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;
		}
			
		public byte getValue(){
			return this.value;
		}

		public String getDescritpion(){
			return this.descritpion;
		}	
		
		private static final Map<Byte, WaterLevelState> lookup = new HashMap<Byte, WaterLevelState>();
		
		static {
			for(WaterLevelState command : WaterLevelState.values()){
				lookup.put(command.getValue(),command );
			}
		}
		
		public static WaterLevelState get(byte value){
			return lookup.get(value);
		}

		public static String getName(byte value){
			return lookup.get(value).name();
		}	
	}
	
	
}
