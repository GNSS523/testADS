package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class AirConditionerConstant {

/*
 * 
TYPE ST_IndoorCondition :
STRUCT

	                  
END_STRUCT
END_TYPE


 * 	
 */
	
	public enum WorkCommand implements ICommand{
		OFF((byte)0),
		COLD((byte)1),		
		DRY((byte)2),
		DEHUMIDITY((byte)3),
		HOT((byte)4);
		
		private final byte value;
		WorkCommand(byte value){
			this.value = value;
		}
			
		public byte getValue(){
			return this.value;
		}	

		private static final Map<String, WorkCommand> lookup = new HashMap<String, WorkCommand>();
		
		static {
			for(WorkCommand command : WorkCommand.values()){
				lookup.put(command.name(),command );
			}
		}
		
		public static WorkCommand get(String name){
			return lookup.get(name);
		}

		public static byte getValue(String name){
			return lookup.get(name).getValue();
		}		
		
	}
	
	public enum FanCommand implements ICommand{
		OFF((byte)0),
		LOW((byte)1),		
		MIDDLE((byte)2),
		HIGH((byte)3),
		AUTO((byte)4);
		
		private final byte value;
		FanCommand(byte value){
			this.value = value;
		}
			
		public byte getValue(){
			return this.value;
		}	

		private static final Map<String, FanCommand> lookup = new HashMap<String, FanCommand>();
		
		static {
			for(FanCommand command : FanCommand.values()){
				lookup.put(command.name(),command );
			}
		}
		
		public static FanCommand get(String name){
			return lookup.get(name);
		}

		public static byte getValue(String name){
			return lookup.get(name).getValue();
		}		
		
	}	
	
	public enum OperationCommand implements ICommand{
		OFF((byte)0),
		COLD((byte)1),		
		DRY((byte)3);
		
		private final byte value;
		OperationCommand(byte value){
			this.value = value;
		}
			
		public byte getValue(){
			return this.value;
		}	

		private static final Map<String, OperationCommand> lookup = new HashMap<String, OperationCommand>();
		
		static {
			for(OperationCommand command : OperationCommand.values()){
				lookup.put(command.name(),command );
			}
		}
		
		public static OperationCommand get(String name){
			return lookup.get(name);
		}

		public static byte getValue(String name){
			return lookup.get(name).getValue();
		}		
		
	}	

	
	public enum WorkState {

		OFF((byte)0),
		COLD((byte)1),		
		DRY((byte)2),
		DEHUMIDITY((byte)3),
		HOT((byte)4);
		
		private final byte value;
		WorkState(byte value){
			this.value = value;
		}
			
		public byte getValue(){
			return this.value;
		}	
	}
	
	public enum FanState {

		OFF((byte)0),
		LOW((byte)1),		
		MIDDLE((byte)2),
		HIGH((byte)3),
		AUTO((byte)4);
		
		private final byte value;
		FanState(byte value){
			this.value = value;
		}
			
		public byte getValue(){
			return this.value;
		}	
	}
	
	public enum OperationState {

		SLEEP((byte)0), UNSLEEP((byte)1),
		SWING((byte)0),	UNSWING((byte)1),		
		LOCK((byte)0),UNLOCK((byte)1),
		WAIT((byte)0),UNWAIT((byte)1),
		TEST((byte)0),UNTEST((byte)1);
		
		private final byte value;
		OperationState(byte value){
			this.value = value;
		}
			
		public byte getValue(){
			return this.value;
		}	
	}		

	/*
		TYPE ST_AirCondition :			
		STRUCT			
			iSetTemper:INT;//HMIè®¾ç½®æ¸©åº¦		
			iCommand:INT;//HMIå‘½ä»¤:å�¯åŠ¨=1;å�œæ­¢=2;åˆ¶å†·=4;åˆ¶çƒ­=8;		
			iActualTemper:INT;//å®žé™…æ¸©åº¦		
			iStatus:INT;//å®žé™…çŠ¶æ€�:å�œæ­¢=0;è¿�è¡Œ=1;åˆ¶å†·=4;åˆ¶çƒ­=8;		
					
		END_STRUCT			
		END_TYPE				

	
	public enum Table {
		
		CONFIG_MODE (            (byte)0, (byte)4,  "config.mode"   ,   "CONFIG_MODE",        (byte)-1),
		CONFIG_MODE_STATUS(      (byte)4, (byte)4,  "config.mode"  ,    "CONFIG_MODE_STATUS", (byte)-1),
		CONFIG_TEMPERATURE(      (byte)8, (byte)4,  "command.set.temperature" ,     "CONFIG_TEMPERATURE", (byte)-1),
		GET_TEMPERATURE(         (byte)12, (byte)4, "command.get.temperature",      "GET_TEMPERATURE",    (byte)-1);
		
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
	 */	
	
	public enum ControlTable {
		
		CONTROL_INDOOR_ADDRESS (            (byte)0, (byte)2,  "command.get.control.address"   ,   "CONFIG_MODE",        (byte)-1),
		OPERATION_MODE(                     (byte)2, (byte)2,  "command.set.operation.mode"  ,             "CONFIG_MODE_STATUS", (byte)-1),
		CONTROL_MODE(                       (byte)4, (byte)2,  "command.set.control.mode" ,           "CONFIG_TEMPERATURE", (byte)-1),
		CONTROL_FAN_MODE(                   (byte)6, (byte)2,  "command.set.fan.mode" ,               "CONFIG_TEMPERATURE", (byte)-1),
		SET_TEMPERATURE(                    (byte)8, (byte)4,  "command.set.temperature",        "GET_TEMPERATURE",    (byte)-1);
		
		private final byte offset;
		private final byte number;
		private final String type;
		private final String descritpion;
		private final byte id;
		ControlTable(byte offset, byte number, String type, String descritpion, byte id){
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
		private static final Map<String, ControlTable> lookup = new HashMap<String, ControlTable>();
		
		static {
			for(ControlTable ads : ControlTable.values()){
				if(ads.getId()!=-1)
					lookup.put(ads.getType()+"_"+ads.getId() ,  ads );
				else
					lookup.put(ads.getType(),ads );
			}
		}
		
		public static ControlTable get(String name, int id){
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

		
		public static ControlTable get(String name){
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
			for(ControlTable ads : ControlTable.values()){
				total += ads.getNumber();
			}		
			return total;
		}
	}	
	

	public enum StatusTable {
		
		MODE_STATUS (            (byte)0, (byte)2,  "command.get.mode.status"   ,   "CONFIG_MODE",        (byte)-1),
		SET_TEMPERATURE(         (byte)4, (byte)4,  "command.set.temperature"  ,    "CONFIG_MODE_STATUS", (byte)-1),
		GET_TEMPERATURE(         (byte)8, (byte)4,  "command.get.temperature" ,     "CONFIG_TEMPERATURE", (byte)-1),
		OPERATION_STATUS(        (byte)12, (byte)2, "command.get.operation.status",      "GET_TEMPERATURE",    (byte)-1),
		FAN_STATUS(              (byte)14, (byte)2, "command.get.fan.status",      "GET_TEMPERATURE",    (byte)-1),
		ERROR(                   (byte)16, (byte)4, "command.get.error",      "GET_TEMPERATURE",    (byte)-1);
		
		private final byte offset;
		private final byte number;
		private final String type;
		private final String descritpion;
		private final byte id;
		StatusTable(byte offset, byte number, String type, String descritpion, byte id){
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
		private static final Map<String, StatusTable> lookup = new HashMap<String, StatusTable>();
		
		static {
			for(StatusTable ads : StatusTable.values()){
				if(ads.getId()!=-1)
					lookup.put(ads.getType()+"_"+ads.getId() ,  ads );
				else
					lookup.put(ads.getType(),ads );
			}
		}
		
		public static StatusTable get(String name, int id){
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

		
		public static StatusTable get(String name){
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
			for(StatusTable ads : StatusTable.values()){
				total += ads.getNumber();
			}		
			return total;
		}
	}		
}
