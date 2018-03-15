package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.FogponicsConstant;
import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

import de.beckhoff.jni.Convert;

public class FogponicsSystem extends HydroponicsSystem{

    private static final Logger LOG = LogManager.getLogger(FogponicsSystem.class);
	
	public static final SystemStructure type = SystemStructure.FOGPONICS_SYSTEM;
	public static int data_length = FogponicsConstant.Table.getTotalLength();
	
	public FogponicsSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public FogponicsSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public FogponicsSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}

	public String getType(){
		return SystemStructure.FOGPONICS_SYSTEM.name();
	}
	
	public byte getTableFieldOffset(String type, int id){
		return FogponicsConstant.Table.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		LOG.debug("fogponics getTableFieldOffset");
		return FogponicsConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return FogponicsConstant.Table.getNumber(name);
	}	
	

	/***************  Pump, Valve Control and Status 
	 * 
	 * open("actuator.atomizer",1)
	 * close("actuator.atomizer",1)
	 * setControl("actuator.atomizer",1,OnOffActuatorConstant.Command.ON )
	 * getControlStatus("actuator.atomizer",1 )
	 * @throws AdsException 
	 * 
	 * *************/	

	
	/*************** set Pump Running and Stop time  
	 * 
	 * setAttribute("config.attr.atomizer.time.run",  25 )
	 * getAttribute("config.attr.atomizer.time.stop")
	 * 
	 * *************/	
	public void setAttribute(String type,  int value) throws AdsException{
		byte[] values = Convert.IntToByteArr(value);

		this.configAttribute( type , values  );
	}
	
	// INT == 2
	public int getAttribute(String type) throws AdsException{
		byte values[]= this.getAttributedStatus(type,2);
		LOG.debug("FogponicsConstant get attribute   "+ values[0]+ " "+ values[1]+"   "+ Convert.ByteArrToShort(values)  );
		return Convert.ByteArrToShort(values);
	}	
	
	
	
	/**************    
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
		return mode_value;						
	}
	
	public int [] getPumpTimeValues(){
		return new int[]{pump_runtime_value,pump_stoptime_value};
	}	
	**************/
	
	String atomizer_value;
	int atomizer_runtime_value;
	int atomizer_stoptime_value;
	
	public String getAtmoizerValue(){
		return atomizer_value;
	}
		
	public int [] getAtmoizerTimeValues(){
		return new int[]{atomizer_runtime_value,atomizer_stoptime_value};
	}	
	
	public void refreshSystemStatus(){
		try {
			 inlet_valve_value  = getControlStatus("actuator.valve", 1);

			 outlet_valve_value  = getControlStatus("actuator.valve", 2);
			 pump_value  = getControlStatus("actuator.pump", 1); 
    		 atomizer_value  =  getControlStatus("actuator.atomizer", 1);
    		
			 mode_value  = getMode("config.system.mode");  
			
			 pump_runtime_value = getAttribute("attribute.pump.runtime");
			 pump_stoptime_value =  getAttribute("attribute.pump.stoptime");
			
    		 atomizer_runtime_value = getAttribute("attribute.atomizer.runtime");
    		 atomizer_stoptime_value =  getAttribute("attribute.atomizer.stoptime");
			
		} catch (AdsException | DeviceTypeException e) {
			e.printStackTrace();
		}		
	}	
}
