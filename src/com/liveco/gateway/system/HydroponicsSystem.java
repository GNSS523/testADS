package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.constant.OnOffActuatorConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.mqtt.MqttCommand;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;
import de.beckhoff.jni.tcads.CallbackListenerAdsState;

public class HydroponicsSystem extends BaseSystem{
	
    private static final Logger LOG = LogManager.getLogger(HydroponicsSystem.class);

	public static final SystemStructure type = SystemStructure.HYDROPONICS;
	
	public static int data_length = HydroponicsConstant.Table.getTotalLength();
	
	public HydroponicsSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public HydroponicsSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public HydroponicsSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}	
	
	/*
	public void test111(){
		System.out.println( this.getTableOffset()  );
	}
	*/
	public String getType(){
		return SystemStructure.HYDROPONICS.name();
	}
	
	public int getTableOffset(){
		return 35;
	}	
	
	public byte getTableFieldOffset(String type, int id){
		return HydroponicsConstant.Table.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return HydroponicsConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return HydroponicsConstant.Table.getNumber(name);
	}
	
	
	/***************  Pump, Valve Control and Status 
	 * 
	 *   parse the web json into the command or status
	 * 
	 * *************/	
	
	public void parseCommand(MqttCommand webcommand) throws AdsException, DeviceTypeException{
		ICommand cmd = null;
		
		String type = webcommand.getType();
		String command = webcommand.getValue();
		String long_name = webcommand.getName();
		
		int id;
		String name;
		
		LOG.debug("Hydroponics parseCommand  "+type+"  "+command+"  "+long_name);
		/*
		switch(type){
			case "device":
				cmd = OnOffActuatorConstant.Command.get(command);			
				String parts[] = long_name.split(".");
				id = Integer.parseInt(parts[3]);
				name = parts[1]+'.'+parts[2];
				System.out.println(    "Type:"+name  +"    Command:"+ id    );			
				this.setControl(  name, id  ,  (OnOffActuatorConstant.Command)cmd);
				break;
				
			case "attribute":
				name = type+"."+long_name;
				this.setAttribute( name , Integer.parseInt(command) );
				break;
				
			case "mode":
				cmd = HydroponicsConstant.ModeCommand.get(command);
				name = type;
				this.setMode(name, (HydroponicsConstant.ModeCommand)cmd);
				break;
				
			case "config":
				cmd = HydroponicsConstant.FillInCommand.get(command);
				name = type+"."+long_name;
				this.requestToFill( name , (HydroponicsConstant.FillInCommand)cmd);
				break;			
		}
		*/
	}
	
	public void parseState(MqttCommand webcommand) throws AdsException, DeviceTypeException{

		String type = webcommand.getType();
		String command = webcommand.getValue();
		String long_name = webcommand.getName();

		int id;
		String name;

		LOG.debug("Hydroponics parseState  "+type+"  "+command+"  "+long_name);
		
		/*
		switch(type){
		
			case "device":
				String parts[] = long_name.split(".");
				id = Integer.parseInt(parts[3]);
				name = parts[1]+'.'+parts[2];
				System.out.println(    "Type:"+name  +"    Command:"+ id    );
				this.getControlStatus(type, id);
				break;
								
			case "mode":
				this.getMode(type); 
				System.out.println(" get the mode  : ");
				break;
				
			case "attribute":
				name = type;				
				this.getAttributedStatus(name);
				System.out.println(" get the attribute  : ");
				break;
				
			case "config":
				name = type+"."+long_name;				
				this.getFillReplyStatus(name);
				System.out.println(" get the command ack  : ");
				break;
		}
		*/		
	}
	
	
	
	/***************  Pump, Valve Control and Status 
	 * 
	 * open("actuator.valve",1)
	 * close("actuator.valve",1)
	 * setControl("actuator.valve",1,OnOffActuatorConstant.Command.ON )
	 * getControlStatus("actuator.valve",1 )
	 * 
	 * *************/
	
	public void open(String type, int id) throws AdsException, DeviceTypeException{
		this.setControl(type, id, OnOffActuatorConstant.Command.ON);
	}
	
	public void close(String type, int id) throws AdsException, DeviceTypeException{
		this.setControl(type, id, OnOffActuatorConstant.Command.OFF);
	}
	
	
	// controlSimpleDevice("actuator.pump",1,OnOffActuatorConstant.Command.ON )
	private void setControl(String type, int id, OnOffActuatorConstant.Command command) throws AdsException, DeviceTypeException{
		this.accessDeviceControl(type, id, command);
	}
	
	// getSimpleDeviceStatus("actuator.pump",1 )
	public String getControlStatus(String type, int id) throws AdsException, DeviceTypeException{
		byte value = this.accessDeviceStatus(type, id, 1,1)[0];
		return OnOffActuatorConstant.State.getName(value);
	}

	
	/***************   Pump, Valve Control and Status 
	 * 
	 * 	 setMode("config.mode",HydroponicsConstant.ModeCommand.AUTOMATIC);
	 *   getMode("config.mode") 
	 * 
	 * *************/	
	
	public void setMode(ICommand command) throws AdsException{
		this.configMode("config.system.mode", command);
	}
	public String getMode(String name) throws AdsException{
		byte value = this.getModeStatus(name, 1,1)[0];
		System.out.println("getMode   "+ name + "  value:"+value  );
		return HydroponicsConstant.ModeState.getName(value);
	}


	/*************** set Pump Running and Stop time  
	 * 
	 * setAttribute("config.attr.pump.time.run",  25 )
	 * getAttribute("config.attr.pump.time.stop")
	 * 
	 * *************/
	
	
	public void setAttribute(String type,  int value) throws AdsException{
		byte[] values = Convert.IntToByteArr(value);
		this.configAttribute( type , values  );
	}
	
	public int getAttribute(String type) throws AdsException{
		byte values[]= this.getAttributedStatus(type,2);
		LOG.debug("hydroponics get attribute   "+ values[0]+ " "+ values[1]+"   "+values.length+"  "+Convert.ByteArrToShort(values) );
		return Convert.ByteArrToShort(values);
		
	}	
	
	
	/*************** request to fill the tank from the nutrient system  
	 * 
	 * requestToFill("config.fill",  25 )
	 * getFillReplyStatus("config.fill")
	 * 
	 * *************/	
	public void requestToFill(String name, HydroponicsConstant.FillInCommand command) throws AdsException{
		this.configMode(name, command);
	}
	public String getFillReplyStatus(String name) throws AdsException{
		//if(name == "actuator.pump" || name == "actuator.valve"){}
		byte value = this.getModeStatus(name, 1,1)[0];
		return HydroponicsConstant.FillInStatus.getName(value);
	}

	
	/*************** subscribe to the water sensor  
	 * 
	 * subscribeToWaterLevel(1 )
	 * unsubscribeToWaterLevel(1)
	 * 
	 * *************/

	public void subscribeToWaterLevel(int id, CallbackListenerAdsState listener) throws AdsException{
		
		long address = this.getSensorAddress("sensor.water_level_sensor", id);
		System.out.println("subscribeToWaterLevel   "+address );
		this.createNotification(address, listener);
	}
	
	public void unsubscribeToWaterLevel(int id, CallbackListenerAdsState listener) throws AdsException{
		
		long address = this.getSensorAddress("sensor.water_level_sensor", id);
		this.deleteNotification(listener);
	}
	
	
	AdsCallbackObject waterLevelObject;
	JNILong waterLevelNotification;
		
	public void createNotification(long indexOffset, CallbackListenerAdsState listener) throws AdsException{	 
        // Create and add listener
		waterLevelObject = new AdsCallbackObject();
		waterLevelObject.addListenerCallbackAdsState(listener);  
		waterLevelNotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, waterLevelNotification, listener);
	}

	public void deleteNotification(CallbackListenerAdsState listener) throws AdsException{
        // Delete listener
		if(waterLevelObject!=null){
			waterLevelObject.removeListenerCallbackAdsState(listener);
			getADSConnection().deleteNotification(waterLevelNotification);
			waterLevelObject = null;
			listener = null;
		}
	}	
	
	
	/**************    *****************/
	String inlet_valve_value;
	String outlet_valve_value;
	String pump_value;
	String mode_value;
	
	int pump_runtime_value;
	int pump_stoptime_value;
	
	public String getInletValveValue(){
		return inlet_valve_value;
	}
	
	public String getOutletValveValue(){
		return outlet_valve_value;		
	}
	
	public String getPumpValue(){
		return pump_value;				
	}
	
	public String getSysteModeValue(){
		System.out.println("getSysteModeValue   "+mode_value);
		return mode_value;						
	}
	
	public int [] getPumpTimeValues(){
		return new int[]{pump_runtime_value,pump_stoptime_value};
	}	
	
	public void refreshSystemStatus() throws AdsException, DeviceTypeException{
		
			inlet_valve_value  = getControlStatus("actuator.valve", 1);

			outlet_valve_value  = getControlStatus("actuator.valve", 2);
			pump_value  = getControlStatus("actuator.pump", 1); 
			
			 
			
			pump_runtime_value = getAttribute("attribute.pump.runtime");
			pump_stoptime_value =  getAttribute("attribute.pump.stoptime");
			
			mode_value  = getMode("config.system.mode"); 
		
	}
	
	
	
	/*************** Test  *************/	
	
	public void test1() throws AdsException{

		try {
			this.setControl(  "actuator.pump",  1,   OnOffActuatorConstant.Command.ON);
		} catch (DeviceTypeException e2) {
			e2.printStackTrace();
		}
		try {
			System.out.println(  "status :" +  this.getControlStatus("actuator.pump",1 )   );
		} catch (DeviceTypeException e) {
			e.printStackTrace();
		}
		
		
	}

}
