package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class SystemConstant {

	public enum ModeCommand  implements ICommand{

		RUNNING((byte)1),
		MAINTAINENCE((byte)2);
		
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

		RUNNING((byte)1),
		MAINTAINENCE((byte)2);
		
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
