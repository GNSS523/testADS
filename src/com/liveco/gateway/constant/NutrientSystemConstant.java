
package com.liveco.gateway.constant;

import java.util.HashMap;
import java.util.Map;

public class NutrientSystemConstant {

	public enum Table{

		/*
		TYPE ST_Config :
		STRUCT
			stPump:ST_PumpOrValueInterfacs;
			stSV1:ST_PumpOrValueInterfacs;
			stSV2:ST_PumpOrValueInterfacs;
			stSV3:ST_PumpOrValueInterfacs;
			stSV4:ST_PumpOrValueInterfacs;
			stSV5:ST_PumpOrValueInterfacs;
			stSV6:ST_PumpOrValueInterfacs;
			stUGL1:ST_PumpOrValueInterfacs;
			iConfigModeCmd	:INT;	//ConfigMode:=1Manual;=2Half Auto;=4:Auto
			iConfigModeStatus:INT;	//ConfigMode:=1Manual;=2Half Auto;=4Auto
			iConfigCommand:INT;//config Cmd锛�1-start config锛�2-stop锛�4-config OK
			iConfigStatus:INT;//config status锛�1-ready锛�2-config run锛�4-config complete锛�8-config OK;16-trans锛�32-trans complete
				byMeterPumpCmd	:BYTE;	//Meter Pump Cmd:=1_power on;=2_power off
				byMeterPumpStatus	:BYTE;	//Meter Pump Status:=1_Power on
			rPHActualValue	:REAL;	//PH actual value
		    rECActualValue	:REAL;	//EC actual value
		    rPEActualValue	:REAL;	//PE actual value
			rPHCheckActualValue	:REAL;	//Check complete:PH  Actual Value
		    rECCheckActualValue	:REAL;	//Check complete:EC  Actual Value
		    stRecipe:ST_LiquorRecipe;
			stActual:ST_LiquorActual;
		END_STRUCT
		END_TYPE
		 */	
		
		PUMP(                  (byte)0, (byte)2,  "actuator.pump",    "pump"    ,                                  (byte)1),
		VALVE_IN_TANK(         (byte)2, (byte)2 , "actuator.valve",   "VALVE_IN_TANK",                             (byte)1),
		VALVE_IN_TEST_CLEAN(   (byte)4, (byte)2,  "actuator.valve",   "VALVE_IN_TEST_CLEAN",                       (byte)2),
		VALVE_OUT_TANK(        (byte)6, (byte)2,  "actuator.valve",   "VALVE_OUT_TANK",                            (byte)3 ),
		VALVE_IN_TEST(         (byte)8, (byte)2,  "actuator.valve",   "VALVE_IN_TEST",                             (byte)4 ),
		VALVE_OUT_WASTE(       (byte)10, (byte)2,  "actuator.valve",   "VALVE_OUT_WASTE",                          (byte)5 ),
		VALVE_OUT_GROWING(     (byte)12, (byte)2,  "actuator.valve",   "VALVE_OUT_GROWING",                        (byte)6 ),
		UV_LED(                (byte)14, (byte)2,  "actuator.uv_led",   "UV_LED",                                   (byte)1 ),
		METER_PUMP(            (byte)16, (byte)2,  "actuator.meter_pump",   "METER_PUMP",                          (byte)1 ),
		
		CONFIG_MODE_SET(           (byte)18, (byte)2,  "config.system.mode.set"   ,   "config the mode ",                     (byte)-1),
		CONFIG_MODE_GET(           (byte)20, (byte)2,  "config.system.mode.get"   ,   "config the mode ",                     (byte)-1),
		COMMAND_FILL_SET(          (byte)22, (byte)2, "command.fill.set"  ,    "command to fill in the tank",          (byte)-1),
		COMMAND_FILL_GET(          (byte)24, (byte)2, "command.fill.get"  ,    "command to fill in the tank",          (byte)-1),
				
		PHActualValue(         (byte)26, (byte)4, "sensor.PH"  ,    "PHActualValue",                           (byte)1),
		ECActualValue(         (byte)30, (byte)4, "sensor.EC"  ,    "ECActualValue",                           (byte)1),
		PEActualValue(         (byte)34, (byte)4, "sensor.PE"  ,    "PEActualValue",                           (byte)1),		
		COMMAND_PH(            (byte)38, (byte)4, "attribute.PH"  ,    "COMMAND_PH",                             (byte)-1),
		COMMAND_EC(            (byte)42, (byte)4, "attribute.EC"  ,    "COMMAND_EC",                             (byte)-1);
		
		
		
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
     *  娣锋恫妯″紡
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
     *  琛ユ恫妯″紡
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

		public static String getName(Byte name){
			return lookup.get(name).name();
		}
	
	}	
	
	
    /*
     *  妫�娴嬫ā寮�
	
	public enum  TABLE{
		
	}	
	     */


}
