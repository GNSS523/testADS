package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class OnOffActuatorConstant {

	public enum Command  implements ICommand{

		ON((byte)1),
		OFF((byte)2);
		
		private final byte value;
		Command(byte value){
			this.value = value;
		}

			
		public byte getValue(){
			return this.value;
		}	

		private static final Map<String, Command> lookup = new HashMap<String, Command>();
		
		static {
			for(Command command : Command.values()){
				lookup.put(command.name(),command );
			}
		}
		
		public static Command get(String name){
			return lookup.get(name);
		}

		public static byte getValue(String name){
			return lookup.get(name).getValue();
		}	
		
	}
	
	public enum State {
		
		OFF((byte)0),
		ON((byte)1),
		ERROR((byte)2);
		
		private final byte value;
		State(byte value){
			this.value = value;
		}
		
		public byte getValue(){
			return this.value;
		}	

		private static final Map<Byte, State> lookup = new HashMap<Byte, State>();
		
		static {
			for(State command : State.values()){
				lookup.put(command.getValue(),command );
			}
		}
		
		public static State get(byte value){
			
			State state = lookup.get(value);
			if(state == null) throw new NullPointerException();
			return state;
		}

		public static String getName(byte value){
			return lookup.get(value).name();
		}
	}
	
	public static void test(){
		
		System.out.println(OnOffActuatorConstant.Command.OFF + "   "+ OnOffActuatorConstant.Command.getValue("ON"));
		System.out.println(OnOffActuatorConstant.State.OFF+ "   "+ OnOffActuatorConstant.State.getName((byte)1) );		

	}
}
