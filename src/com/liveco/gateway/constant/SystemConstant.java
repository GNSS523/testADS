package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

import com.liveco.gateway.constant.PanelLightingConstant.Table;

public class SystemConstant {

	public enum Table {
		
		SET_MODE(      (byte)0, (byte)2,  "config.system.mode.set",    " ",     (byte)-1),
		GET_MODE(      (byte)2, (byte)2 , "config.system.mode.get",   "",     (byte)-1);
		
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
	
	public enum ModeCommand  implements ICommand{

		RUNNING((byte)2),
		MAINTAINENCE((byte)1);
		
		private final byte value;
		ModeCommand(byte value){
			this.value = value;
		}
			
		public byte getValue(){
			return this.value;
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

		RUNNING((byte)2),
		MAINTAINENCE((byte)1);
		
		private final byte value;
		ModeState(byte value){
			this.value = value;
		}
		
		public byte getValue(){
			return this.value;
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
	
	public static void test(){
    	System.out.println( SystemConstant.ModeCommand.MAINTAINENCE + " "+  SystemConstant.ModeCommand.getValue("MAINTAINENCE") );		
	}
}
