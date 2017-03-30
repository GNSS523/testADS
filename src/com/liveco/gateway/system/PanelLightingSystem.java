package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.constant.PanelLightingConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.mqtt.MqttCommand;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

public class PanelLightingSystem extends BaseSystem{

    private static final Logger LOG = LogManager.getLogger(PanelLightingSystem.class);
	
	public static final SystemStructure type = SystemStructure.PANEL_LIGHTING_SYSTEM;
			
	public PanelLightingSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public PanelLightingSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}
	
	public PanelLightingSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}	
	

	public byte getTableFieldOffset(String type){
		return PanelLightingConstant.Table.getOffset(type);
	}	

	
	/***************  Panel lighting Control on one single channel 
	 * 
	 * setIntensityOnColor("RED",40)
	 * getIntensityOnColor("GREEN",60)
	 * 
	 * *************/	
	public void setIntensityOnColor(String type,int value) throws AdsException, DeviceTypeException{
		if(value <=0) value = 0;
		else if(value >=0 ) value = 100;
		
		if(type !="RED" || type !="GREEN" || type !="BLUE" || type !="FAR_RED" || type !="GREEN"){
			throw new DeviceTypeException("");
		}
		byte[] values = { (byte)value };
		this.accessDeviceControl( type , values);
	}
	
	public int getIntensityOnColor(String type) throws AdsException, DeviceTypeException{
		
		if(type !="RED" || type !="GREEN" || type !="BLUE" || type !="FAR_RED" || type !="GREEN"){
			throw new DeviceTypeException("");
		}
		return (int)this.accessDeviceStatus(type)[0];
		
	}
	
	/***************  Panel lighting Control on all the channels 
	 * 
	 * setIntensityOnColor(40，50，60，70，80)
	 * getIntensityOnColor()
	 * 
	 * *************/	
	public void setLightIntensity(int red, int blue, int green, int far_red, int uv) throws AdsException, DeviceTypeException{
	
		setIntensityOnColor("RED",red);
		setIntensityOnColor("BLUE",blue);
		setIntensityOnColor("GREEN",green);
		setIntensityOnColor("FAR_RED",far_red);
		setIntensityOnColor("GREEN",uv);
		
	}

	public void getLightIntensity() throws AdsException, DeviceTypeException{
		
		int red = getIntensityOnColor("RED");
		int blue = getIntensityOnColor("BLUE");
		int green = getIntensityOnColor("GREEN");
		int far_red = getIntensityOnColor("FAR_RED");
		int uv = getIntensityOnColor("uv");
		
		// 组合成json
		
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
		
		LOG.debug("PanelLightingSystem parseCommand  "+type+"  "+command+"  "+long_name);
		/*
		switch(type){
			case "device":
				String parts[] = long_name.split(".");
				String channel = parts[3];
				System.out.println(    "Type:"+name  +"    Command:"+ channel    );			
				this.setIntensityOnColor(  channel, Integer.parseInt(command));
			break;					
		}
		*/
	}	
	
	
	
	
}
