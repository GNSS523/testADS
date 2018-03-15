package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.NutrientSystemConstant;
import com.liveco.gateway.constant.OnOffActuatorConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;
import de.beckhoff.jni.tcads.CallbackListenerAdsState;

public class NurientSystem  extends BaseSystem{

    private static final Logger LOG = LogManager.getLogger(NurientSystem.class);
	
	public static final SystemStructure type = SystemStructure.NUTRIENT_SYSTEM;
	
	public NurientSystem(ADSConnection ads, int index, String system_id){
		super(ads,index, system_id);
	}
	
	public NurientSystem(ADSConnection ads, int index, String system_id, long base_address){
		super(ads,index, system_id,base_address);
	}

	public NurientSystem(ADSConnection ads, int index, String system_id, long base_address, byte array[]){
		super(ads,index, system_id,base_address,array);
	}
	
	public String getType(){
		return SystemStructure.NUTRIENT_SYSTEM.name();
	}
	
	public byte getTableFieldOffset(String type, int id){
		return NutrientSystemConstant.Table.getOffset(type,id);
	}
	
	public byte getTableFieldOffset(String name){
		return NutrientSystemConstant.Table.getOffset(name);
	}	
	
	public byte getTableFieldNumberOfByte(String name){
		return NutrientSystemConstant.Table.getNumber(name);
	}

	
	/**************    *****************/
    String  SV1_value,SV2_value,SV3_value,SV4_value,SV5_value,SV6_value;
    String pump_value;
    String uv_led_value;
    String meter_value;
    
	String mode_value;	
	
	float EC_setting;
	float PH_setting;
	
	public void refreshSystemStatus(){
		try {

			SV1_value  = getControlStatus("actuator.valve", 1);
			SV2_value  = getControlStatus("actuator.valve", 2);
			SV3_value  = getControlStatus("actuator.valve", 3);
			SV4_value  = getControlStatus("actuator.valve", 4);
			SV5_value  = getControlStatus("actuator.valve", 5);
			SV6_value  = getControlStatus("actuator.valve", 6);

			mode_value  = getMode("config.system.mode");  
			
			pump_value  = getControlStatus("actuator.pump", 1);			
			meter_value = getControlStatus("actuator.meter_pump", 1);
			EC_setting = getPHSetting();
			PH_setting =  getECSetting();
			uv_led_value  = getControlStatus("actuator.uv_led", 1);

			
		} catch (AdsException | DeviceTypeException e) {
			e.printStackTrace();
		}		
	}	
	
	
	/*******
	 * 
	 *  SV1 自来水进
	 *  SV6 输出液
	 *  SV5清理液体
	 *  P1 出液水泵
	 *  
	 *  PE流量计
	 * @throws DeviceTypeException 
	 * @throws AdsException 
	 * 
	 *******/
	// SV1
	public void openValveToTank() throws AdsException, DeviceTypeException{
		open("actuator.valve",1);
	}
	
	public void closeValveToTank() throws AdsException, DeviceTypeException{
		close("actuator.valve",1);
	}
	
	public void pumpWaterToGrowingArea() throws AdsException, DeviceTypeException{
		open("actuator.pump",1);
		open("actuator.valve",6);
	}
	
	public void stopPumpToGrowingArea() throws AdsException, DeviceTypeException{
		close("actuator.pump",1);
		close("actuator.valve",6);
	}
	

	
	public void startCleaningValve() throws AdsException, DeviceTypeException{
		open("actuator.valve",5);
	}
	
	public void stopCleaningValve() throws AdsException, DeviceTypeException{
		close("actuator.valve",5);
	}
	
	
	
	
	
	
	/*********
	 * SV2 清洗
	 * SV4 进液
	 * SV3出液
	 * 
	 * PH
	 * EC
	 * 
	 *******/
	
	public void pumpWaterToTestingArea() throws AdsException, DeviceTypeException{
		open("actuator.pump",1);
		open("actuator.valve",4);

	}
	
	public void stopPumpToTestingArea() throws AdsException, DeviceTypeException{
		close("actuator.pump",1);
		close("actuator.valve",4);

	}	
	
	public void startTestCleaningValve() throws AdsException, DeviceTypeException{
		open("actuator.valve",2);
	}
	
	public void stopTestCleaningValve() throws AdsException, DeviceTypeException{
		close("actuator.valve",2);
	}	
	
	public void startTestToWasteValve() throws AdsException, DeviceTypeException{
		open("actuator.valve",3);
	}
	
	public void stopTestToWasteValve() throws AdsException, DeviceTypeException{
		close("actuator.valve",3);
	}		

	/********
	 * 
	 * 
	 * 
	 * 
	 */
	public String getSysteModeValue(){
		return mode_value;						
	}	
	
	public String getPumpValue(){
		return pump_value;				
	}
	
	public String getUVLEDValue(){
		return uv_led_value;
	}
	
	public String getMeterValue(){
		return meter_value;
	}
	
	public String getValveValue(int index){
		switch(index){
			case 1:
				return SV1_value;
			case 2:
				return SV2_value;
			case 3:
				return SV3_value;
			case 4:
				return SV4_value;
			case 5:
				return SV5_value;
			case 6:
				return SV6_value;		
		}
		return "";
	}
	
	/***********
	 * 
	 * 
	 * 
	 * 
	 * 
	 ***********/
	public void open(String type, int id) throws AdsException, DeviceTypeException{
		this.setControl(type, id, OnOffActuatorConstant.Command.ON);
	}
	
	public void close(String type, int id) throws AdsException, DeviceTypeException{
		this.setControl(type, id, OnOffActuatorConstant.Command.OFF);
	}
	
	
	// 
	private void setControl(String type, int id, OnOffActuatorConstant.Command command) throws AdsException, DeviceTypeException{
		this.accessDeviceControl(type, id, command);
	}
	
	// 
	public String getControlStatus(String type, int id) throws AdsException, DeviceTypeException{
		byte value = this.accessDeviceStatus(type, id, 1,1)[0];
		
		System.out.println(type +  "   "+ id +"   "+ value);
		return OnOffActuatorConstant.State.getName(value);

	}	
		

	
	public void setMode(String name, NutrientSystemConstant.ModeCommand command) throws AdsException{
		this.configMode("config.system.mode.set", command);
	}
	public String getMode(String name) throws AdsException{
		byte value = this.getModeStatus("config.system.mode.get", 1,0)[0];
		return NutrientSystemConstant.ModeState.getName(value);
	}	
	

	
	
	public void requestToFill( NutrientSystemConstant.FillInCommand command) throws AdsException{
		this.configMode( "command.fill.set", command);
	}
	public String getFillReplyStatus() throws AdsException{
		//if(name == "actuator.pump" || name == "actuator.valve"){}
		byte value = this.getModeStatus( "command.fill.get", 1,0)[0];
		return NutrientSystemConstant.FillInStatus.getName(value);
	}	
	
	public void setPH(float value) throws AdsException{
		byte[] values = Convert.FloatToByteArr(value);
		this.configAttribute("command.PH", values);
	}
	
	public void setEC(float value) throws AdsException{
		byte[] values = Convert.FloatToByteArr(value);
		this.configAttribute("command.EC", values);
	}	

	
	/*************** set Pump Running and Stop time  
	 * 
	 * getAttribute("config.attr.atomizer.time.stop")
	 * 
	 * *************/	

	
	// INT == 2
	public float getPHSetting() throws AdsException{
		byte values[]= this.getAttributedStatus("attribute.PH",4);
		LOG.debug("NutrientSystem getPH  attribute   "+ values[0]+ " "+ values[1]+"   "+ Convert.ByteArrToFloat(values)  );
		return Convert.ByteArrToFloat(values);
	}		
	
	public float getECSetting() throws AdsException{
		byte values[]= this.getAttributedStatus("attribute.EC",4);
		LOG.debug("NutrientSystem get EC attribute   "+ values[0]+ " "+ values[1]+"   "+ Convert.ByteArrToFloat(values)  );
		return Convert.ByteArrToFloat(values);
	}	
	
	
	/*************** subscribe to the PH sensor  
	 * subscribeToPH(1 )
	 * unsubscribeToPH(1)
	 * *************/	
	AdsCallbackObject PHObject;
	JNILong PHNotification;
	public void subscribeToPH(int id,  CallbackListenerAdsState listener) throws AdsException{		
		long address = this.getSensorAddress("sensor.PH", id);
		this.createPHNotification(address,listener);
	}
	
	public void unsubscribeToPH(int id,  CallbackListenerAdsState listener) throws AdsException{
		
		long address = this.getSensorAddress("sensor.PH", id);
		this.deletePHNotification(listener);
	}	
	

	public void createPHNotification(long indexOffset,CallbackListenerAdsState listener) throws AdsException{	 
        // Create and add listener
		PHObject = new AdsCallbackObject();
		PHObject.addListenerCallbackAdsState(listener);  
		PHNotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, PHNotification, listener);
	}

	public void deletePHNotification(CallbackListenerAdsState listener) throws AdsException{
        // Delete listener
		PHObject.removeListenerCallbackAdsState(listener);
        getADSConnection().deleteNotification(PHNotification);
	}	

	/*************** subscribe to the EC sensor  
	 * 
	 * subscribeToEC(1 )
	 * unsubscribeToEC(1)
	 * 
	 * *************/	
	AdsCallbackObject ECObject;
	JNILong ECNotification;
	
	public void subscribeToEC(int id,  CallbackListenerAdsState listener) throws AdsException{
		
		long address = this.getSensorAddress("sensor.EC", id);
		this.createECNotification(address,listener);
	}
	
	public void unsubscribeToEC(int id,  CallbackListenerAdsState listener) throws AdsException{
		
		long address = this.getSensorAddress("sensor.EC", id);
		this.deleteECNotification(listener);
	}	
	
	public void createECNotification(long indexOffset, CallbackListenerAdsState listener) throws AdsException{	 
        // Create and add listener
		ECObject = new AdsCallbackObject();
		ECObject.addListenerCallbackAdsState(listener);  
		ECNotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, ECNotification, listener);
	}

	public void deleteECNotification(CallbackListenerAdsState listener) throws AdsException{
        // Delete listener
		ECObject.removeListenerCallbackAdsState(listener);
        getADSConnection().deleteNotification(ECNotification);
	}	

	
	/*************** subscribe to the EC sensor  
	 * 
	 * subscribeToEC(1 )
	 * unsubscribeToEC(1)
	 * 
	 * *************/	
	AdsCallbackObject PEObject;
	JNILong PENotification;
	
	public void subscribeToPE(int id,  CallbackListenerAdsState listener) throws AdsException{
		
		long address = this.getSensorAddress("sensor.EC", id);
		this.createECNotification(address,listener);
	}
	
	public void unsubscribeToPE(int id,  CallbackListenerAdsState listener) throws AdsException{
		
		long address = this.getSensorAddress("sensor.EC", id);
		this.deleteECNotification(listener);
	}	
	
	public void createPENotification(long indexOffset, CallbackListenerAdsState listener) throws AdsException{	 
		PEObject = new AdsCallbackObject();
		PEObject.addListenerCallbackAdsState(listener);  
		PENotification = new JNILong();       
        getADSConnection().createNotification(indexOffset, PENotification, listener);
	}

	public void deletePENotification(CallbackListenerAdsState listener) throws AdsException{
        // Delete listener
		PEObject.removeListenerCallbackAdsState(listener);
        getADSConnection().deleteNotification(PENotification);
	}	
}
