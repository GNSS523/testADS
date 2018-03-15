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
	
	public int values [] = new int[5];
			
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
	public String getType(){
		return SystemStructure.PANEL_LIGHTING_SYSTEM.name();
	}
	
	public void setIntensityOnColor(String type,int value) throws AdsException, DeviceTypeException{
		if(value <0) value = 0;
		else if(value >100 ) value = 100;
		
        //System.out.println("setIntensityOnColor "+value);
		
		byte[] values = { (byte)value };
		this.accessDeviceControl( type , values);
	}
	
	public int getIntensityOnColor(String type) throws AdsException, DeviceTypeException{
		/**/
		return (int)this.accessDeviceStatus(type)[0];
		
	}
	
	/***************  Panel lighting Control on all the channels 
	 * 
	 * setIntensityOnColor(40锛�50锛�60锛�70锛�80)
	 * getIntensityOnColor()
	 * 
	 * *************/	
	public void setLightIntensity(int red, int blue, int green, int far_red, int uv) throws AdsException, DeviceTypeException{
	
		setIntensityOnColor("actuator.panel_led.red",red);
		setIntensityOnColor("actuator.panel_led.blue",blue);
		setIntensityOnColor("actuator.panel_led.green",green);
		setIntensityOnColor("actuator.panel_led.far_red",far_red);
		setIntensityOnColor("actuator.panel_led.uv",uv);
		
	}

	public int[] getLightIntensity() throws AdsException, DeviceTypeException{
		
		int red = getIntensityOnColor("actuator.panel_led.red");
		int blue = getIntensityOnColor("actuator.panel_led.blue");
		int green = getIntensityOnColor("actuator.panel_led.green");
		int far_red = getIntensityOnColor("actuator.panel_led.far_red");
		int uv = getIntensityOnColor("actuator.panel_led.uv");
		
		System.out.println("PanelLightingSystem   "+red+"  "+blue+"  "+green+"  "+far_red+"   "+uv);
		values[0] = red;
		values[1] =blue;
		values[2] =green;
		values[3] =far_red;
		values[4] =uv;
		return values;
		
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
