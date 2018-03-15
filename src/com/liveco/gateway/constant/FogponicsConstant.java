package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.liveco.gateway.constant.HydroponicsConstant.FillInCommand;
import com.liveco.gateway.constant.HydroponicsConstant.FillInStatus;

public class FogponicsConstant extends IrrigationSystemConstant{
	
	public enum Table{

	/*
		TYPE ST_SingleCircSysInterface2 :
		STRUCT
			stPump:ST_PumpOrValueInterfacs;8
			stA1ToA6:ST_PumpOrValueInterfacs;
			stSVIn:ST_PumpOrValueInterfacs;
			stSVOut:ST_PumpOrValueInterfacs;
			byHMICmd:BYTE;          //Cmd:=1_Manual;=2_Config;=4_Work;=8_Clean Up;=16_Fill in锛�=32_check
			byHMIStatus:BYTE;       //Status:=1_Manual;=2_Config;=4_Work;=8_Clean up;=16_Fill in;=32_check
			byWaterTankLevel:BYTE; //bit 3=1:HH(highest level);bit 2:H(high level);bit 1:L(low level);bit 0(lowwest level)
			byRequestFill:BYTE;    //=1 request
			byAgreeFill:BYTE;      //=1 agree
			iPumpRunTime:INT;      //minute
			iPumpStopTime:INT;     //minute
			iA1ToA6RunTime:INT;      //minute
			iA1ToA6StopTime:INT;     //minute	
		END_STRUCT
		END_TYPE
	*/	
		
		PUMP(           (byte)0, (byte)2,  "actuator.pump",    "pumping"    ,  (byte)1),
		ATOMIZER (      (byte)2, (byte)2,  "actuator.atomizer",   "atomizer", (byte)1 ),
		VALVE_IN(       (byte)4, (byte)2,  "actuator.valve",   "inlet valve",  (byte)1),
		VALVE_OUT(      (byte)6, (byte)2,  "actuator.valve",   "outlet valve", (byte)2),
		CONFIG_MODE(    (byte)8, (byte)2,  "config.system.mode"   ,   "config the mode ", (byte)-1),

		LEVEL_SENSOR(   (byte)10, (byte)1,  "sensor.water_level_sensor",   "water level sensorr", (byte)1 ),
		
		RPC_FILL_WATER(    (byte)12, (byte)2,  "rpc.fill_water"  ,    "command to fill in the tank", (byte)-1),
		CONFIG_PUMP_RUN_TIME(  (byte)14, (byte)2,  "attribute.pump.runtime" ,"config.pump.runtime", (byte)-1),
		CONFIG_PUMP_STOP_TIME( (byte)16, (byte)2,  "attribute.pump.stoptime","config.pump.stoptime", (byte)-1 ),
		CONFIG_PUMP_ELAPSED_WORK_TIME( (byte)18, (byte)2,  "attribute.pump.elapsed_runtime","config.pump.elapsed.runtime", (byte)-1 ),
		CONFIG_PUMP_ELAPSED_STOP_TIME( (byte)20, (byte)2,  "attribute.pump.elapsed_stoptime","config.pump.elapsed.stoptime", (byte)-1 ),
		CONFIG_ATOMIZER_RUN_TIME(  (byte)22, (byte)2,  "attribute.atomizer.runtime" ,"config.atomizer.runtime", (byte)-1),
		CONFIG_ATOMIZER_STOP_TIME( (byte)24, (byte)2,  "attribute.atomizer.stoptime","config.atomizer.stoptime", (byte)-1 ),
		CONFIG_ATOMIZER_ELAPSED_WORK_TIME(  (byte)26, (byte)2,  "attribute.atomizer.elapsed_runtime" ,"config.atomizer.elapsed.runtime", (byte)-1),
		CONFIG_ATOMIZER_ELAPSED_STOP_TIME( (byte)28, (byte)2,  "attribute.atomizer.elapsed_stoptime","config.atomizer.elapsed.stoptime", (byte)-1 );		

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
		
		public static Vector<String> getConstantVector(){
	        Vector<String> vs = new Vector<String>();
	        for(Table ads : Table.values()){
	        	vs.add(ads.name());
	        }
	        return vs;
		}
	}
	
	
	
	public enum ModeCommand  implements ICommand{

		MANUAL((byte)1, "MANUAL"),
		CONFIG((byte)2, "CONFIG"),
		RUNNING((byte)4,"RUNNING"),
		CLEANUP((byte)8,"CLEANUP"),
		FILLIN((byte)16,"FILLIN"),
		CHECK((byte)32,"CHECK");
		
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
		
		public static Vector<String> getConstantVector(){
	        Vector<String> vs = new Vector<String>();
	        for(ModeCommand ads : ModeCommand.values()){
	        	vs.add(ads.name());
	        }
	        return vs;
		}		
		
	}
	
	
	public enum ModeState {
		
		MANUAL((byte)1, "MANUAL"),
		CONFIG((byte)2, "CONFIG"),
		RUNNING((byte)4,"RUNNING"),
		CLEANUP((byte)8,"CLEANUP"),
		FILLIN((byte)16,"FILLIN"),
		CHECK((byte)32,"CHECK");
		
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
		
		public static Vector<String> getConstantVector(){
	        Vector<String> vs = new Vector<String>();
	        for(ModeState ads : ModeState.values()){
	        	vs.add(ads.name());
	        }
	        return vs;
		}
		
	}
	
	public enum FillInCommand implements ICommand{
		
		REQUEST((byte)1, "REQUEST"),
		NO_REQUEST((byte)2, "NO_REQUEST");		
		
		private final byte value;
		private final String descritpion;

		FillInCommand(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;

		}
			
		public byte getValue(){
			return this.value;
		}	
		public String getDescritpion(){
			return this.descritpion;
		}
		private static final Map<String, FillInCommand> lookup = new HashMap<String, FillInCommand>();
		
		static {
			for(FillInCommand command : FillInCommand.values()){
				lookup.put(command.name(),command );
			}
		}
		
		public static FillInCommand get(String name){
			return lookup.get(name);
		}

		public static byte getValue(String name){
			return lookup.get(name).getValue();
		}	
		
		public static Vector<String> getConstantVector(){
	        Vector<String> vs = new Vector<String>();
	        for(FillInCommand ads : FillInCommand.values()){
	        	vs.add(ads.name());
	        }
	        return vs;
		}			
	}	

	public enum FillInStatus {
		
		AGREE((byte)1, "AGREE"),
		REJECT((byte)2, "REJECT");		
		
		private final byte value;
		private final String descritpion;

		FillInStatus(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;

		}
			
		public byte getValue(){
			return this.value;
		}	
		public String getDescritpion(){
			return this.descritpion;
		}
		private static final Map<Byte, FillInStatus> lookup = new HashMap<Byte, FillInStatus>();
		
		static {
			for(FillInStatus command : FillInStatus.values()){
				lookup.put(command.getValue(),command );
			}
		}
		
		public static FillInStatus get(Byte name){
			return lookup.get(name);
		}

		public static String getName(Byte name){
			return lookup.get(name).name();
		}	
		
	

		public static Vector<String> getConstantVector(){
	        Vector<String> vs = new Vector<String>();
	        for(FillInStatus ads : FillInStatus.values()){
	        	vs.add(ads.name());
	        }
	        return vs;
		}			
	}	
	public enum WaterLevelState{
		
		HH((byte)15, "HH"),
		H((byte)7, "H"),
		L((byte)3,"LL"),
		LL((byte)1,"L");
		
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
		
		public static Vector<String> getConstantVector(){
	        Vector<String> vs = new Vector<String>();
	        for(WaterLevelState ads : WaterLevelState.values()){
	        	vs.add(ads.name());
	        }
	        return vs;
		}		
		
	}
	
	
}
