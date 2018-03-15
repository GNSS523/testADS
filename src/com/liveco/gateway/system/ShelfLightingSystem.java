package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.constant.OnOffActuatorConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.mqtt.MqttCommand;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

public class ShelfLightingSystem extends BaseSystem{

    private static final Logger LOG = LogManager.getLogger(ShelfLightingSystem.class);
	
	public static final SystemStructure type = SystemStructure.SHELF_LIGHTING_SYSTEM;
		
	public ShelfLightingSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public ShelfLightingSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public ShelfLightingSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
		number =  this.getByteArray().length;
	}

	public String getType(){
		return SystemStructure.SHELF_LIGHTING_SYSTEM.name();
	}	
	
	// light number
	int number = 0;

	/***************  Panel lighting Control on all the channels 
	 * 
	 * open(0)
	 * close(0)
	 * 
	 * *************/
	public void open(int index) throws AdsException{
		this.setControl( index, OnOffActuatorConstant.Command.ON);
	}
	
	
	/**
	 *  can't use OnOffActuatorConstant.Command.OFF, since OnOffActuatorConstant.Command.OFF == 2, so we use just 0 as the OFF command
	 * @param index
	 * @throws AdsException
	 * @throws DeviceTypeException
	 */
	public void close(int index) throws AdsException{
		this.setControl(index, new byte[]{0});
	}
	
	// controlSimpleDevice("actuator.pump",1,OnOffActuatorConstant.Command.ON )
	private void setControl(int index, OnOffActuatorConstant.Command command) throws AdsException{
		this.accessDeviceControl(index, command);
	}
	
	private void setControl(int index, byte []command) throws AdsException{
		this.accessDeviceControl(index, command);
	}	
	
	public byte[] getLightingStatus(){
		number =  this.getByteArray().length;
		return this.getByteArray();
	}
	
	public byte getLightDriverStatus(int index){
		return this.getByteArray()[index];
	}
	
	public int getLightNumber(){
		return number;
	}
	
	/*******************   Mode control
	 * @throws AdsException
	 */
	
	public void controlAllOn() throws AdsException{
		for (int i=0;i<number;i++){
			open(i);
		}
	}
	
	public void controlAllOff() throws AdsException{
		for (int i=0;i<number;i++){
			close(i);
		}		
	}
	
	/***************  Light Control and Status 
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
		
		LOG.debug("ShelfLightingSystem parseCommand  "+type+"  "+command+"  "+long_name);
		/*
		switch(type){
			case "device":
				cmd = OnOffActuatorConstant.Command.get(command);			
				String parts[] = long_name.split(".");
				id = Integer.parseInt(parts[3]);
				name = parts[1]+'.'+parts[2];
				System.out.println(    "Type:"+name  +"    Command:"+ id    );			
				this.setControl(   id  ,  (OnOffActuatorConstant.Command)cmd);
				break;		
		}
		*/
	}	
	
}
