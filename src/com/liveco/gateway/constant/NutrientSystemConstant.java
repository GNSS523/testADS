package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class NutrientSystemConstant {

	public enum ModeCommand implements ICommand {
		
		MANUAL((byte)1, "MANUAL"),
		SEMI_AUTOMATIC((byte)2, "SEMI_AUTOMATIC"),
		AUTOMATIC((byte)4,"AUTOMATIC");
		
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
		AUTOMATIC((byte)4,"AUTOMATIC");
		
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
     *  混液模式
     */
	
	public enum MixingCommand implements ICommand{
		
		START((byte)1, "START"),
		STOP((byte)2, "STOP");
		
		private final byte value;
		private final String descritpion;

		MixingCommand(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;

		}
			
		public byte getValue(){
			return this.value;
		}	
		public String getDescritpion(){
			return this.descritpion;
		}
		private static final Map<String, MixingCommand> lookup = new HashMap<String, MixingCommand>();
		
		static {
			for(MixingCommand command : MixingCommand.values()){
				lookup.put(command.name(),command );
			}
		}
		
		public static MixingCommand get(String name){
			return lookup.get(name);
		}

		public static byte getValue(String name){
			return lookup.get(name).getValue();
		}	
	}	
	
	
	public enum MixingState {
		
		READY((byte)1, "READY"),
		CONFIG((byte)2, "CONFIG"),
		CONFIG_DONE((byte)4, "CONFIG_DONE"),
		LIQUID_OUTPUT((byte)8, "LIQUID_OUTPUT"),
		LIQUID_OUTPUT_DONE((byte)16, "LIQUID_OUTPUT_DONE");
		
		private final byte value;
		private final String descritpion;

		MixingState(byte value, String descritpion){
			this.value = value;
			this.descritpion = descritpion;

		}
			
		public byte getValue(){
			return this.value;
		}	
		public String getDescritpion(){
			return this.descritpion;
		}
		
		private static final Map<Byte, MixingState> lookup = new HashMap<Byte, MixingState>();
		
		static {
			for(MixingState command : MixingState.values()){
				lookup.put(command.getValue(),command );
			}
		}
		
		public static MixingState get(byte value){
			return lookup.get(value);
		}

		public static String getName(byte value){
			return lookup.get(value).name();
		}	
	}

    /*
     *  补液模式
     */	
	
	public enum FillInCommand implements ICommand{
		
		NO1((byte)1, "NO1"),
		NO2((byte)2, "NO2"),
		NO3((byte)4, "NO3"),
		NO4((byte)8, "NO4"),
		NO5((byte)16, "NO5"),
		NO6((byte)32, "NO6");		
		
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
	}	

	public enum FillInStatus {
		
		NO1((byte)1, "NO1"),
		NO2((byte)2, "NO2"),
		NO3((byte)4, "NO3"),
		NO4((byte)8, "NO4"),
		NO5((byte)16, "NO5"),
		NO6((byte)32, "NO6");		
		
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

		public static byte getValue(Byte name){
			return lookup.get(name).getValue();
		}	
	}	
	
	
    /*
     *  检测模式
     */
	
	
	
	public enum  TABLE{
		
	}
}
