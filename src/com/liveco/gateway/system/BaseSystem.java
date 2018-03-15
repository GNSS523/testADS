package com.liveco.gateway.system;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.ICommand;
import com.liveco.gateway.mqtt.MqttCommand;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

public class BaseSystem {

    private static final Logger LOG = LogManager.getLogger(BaseSystem.class);
	
	
	protected ADSConnection ads;
	private String system_id;
	private int index;
	private long base_address;
	private byte byte_array[];
	
	BaseSystem(ADSConnection ads, int index, String system_id){
		this.index = index;
		this.system_id = system_id;
		this.ads = ads;
	}
	
	BaseSystem(ADSConnection ads, int index, String system_id, long base_address){
		this.index = index;
		this.system_id = system_id;
		this.base_address = base_address;
		this.ads = ads;
	}

	BaseSystem(ADSConnection ads, int index, String system_id, long base_address, byte byte_array[]){
		this.index = index;
		this.system_id = system_id;
		this.base_address = base_address;
		this.byte_array = byte_array;
		this.ads = ads;
	}	
	
	public boolean isConnected(){
		if(this.ads == null) return false;
		if(this.ads.isConnected()) return true;
		else return false;
	}
	
	
	public int getIndex(){
		return this.index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public String getSystemId(){
		return this.system_id;
	}
	
	public void setSystemId(String id){
		this.system_id = id;
	}
	
	public void setBaseAddress(int address){
		this.base_address = address;
	}
	
	public long getBaseAddress(){
		return this.base_address;
	}
	
	public void setByteArray(byte byte_array[]){
		this.byte_array = byte_array;
	}
	
	public byte[] getByteArray(){
		return this.byte_array;
	}	
	
	public void test(byte test_array[]){
		LOG.debug("-----------------------");
		LOG.debug("address : "+base_address);
		for(int i = 0; i<test_array.length;i++){
			System.out.print(test_array[i]+"  ");
		}
		System.out.println("  ");
		LOG.debug("-----------------------");
	}

	public void test(){
		LOG.debug("-----------------------");
		LOG.debug("address : "+base_address);
		for(int i = 0; i<byte_array.length;i++){
			System.out.print(byte_array[i]+"  ");
		}
		System.out.println("  ");
		
		LOG.debug("-----------------------");
	}	
	
	public byte[] readByteArray( int buffersize) throws AdsException{
		
		return ads.readAddress(base_address, buffersize);
	}
	
	public void writeByteArray(  byte value[]) throws AdsException{
		ads.writeAddress(base_address, value);
	}
	
	
	public byte[] readByteArray(long address, int buffersize) throws AdsException{
		return ads.readAddress(address, buffersize);
	}
	
	public void writeByteArray(  long address, byte value[]) throws AdsException{
		ads.writeAddress(address, value);
	}	
	
	public void setADSConnection(ADSConnection ads){
		this.ads = ads;
	}
	
	public ADSConnection getADSConnection(){
		return this.ads;
	}
	
	public int getTableOffset(){
		return -1000;
	}

	public void test111(){
		System.out.println( this.getTableOffset()  );
	}
	


	public byte getTableFieldOffset(String type , int id){
		return (byte)0;
	}	

	public byte getTableFieldOffset(String name){
		return (byte)0;
	}
	
	public byte getTableFieldNumberOfByte(String name){
		return (byte)0;
	}
	
	public long getSensorAddress(String type, int id) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(type, id);
		LOG.debug("getSensorAddress   " +address+"    "+(long)this.getTableFieldOffset(type, id));
		return address;
	}
	
	
	/*  access device  */
	public void accessDeviceControl(String type, int id, ICommand command ) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(type, id);
		System.out.println(this.getBaseAddress() + "  "+(long)this.getTableFieldOffset(type, id)+"  "+command.getValue());
		byte values[] = {command.getValue()};
		this.writeByteArray(address, values);	
	}

	public void accessDeviceControl(String name, byte values[] ) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(name);
		this.writeByteArray(address, values);
	}

	// applied for the pumb or valve control (through ICommand)
	public void accessDeviceControl(int offset, ICommand command ) throws AdsException{
		long address = this.getBaseAddress() + offset;
		byte values[] = {command.getValue()};
		LOG.debug("accessDeviceControl    "+values[0]);
		this.writeByteArray(address, values);
	}
	
	// applied for the LED tube lighting
	public void accessDeviceControl(int offset, byte values[] ) throws AdsException{
		long address = this.getBaseAddress() + offset;
		LOG.debug("accessDeviceControl    "+values[0]);
		this.writeByteArray(address, values);
	}	
	
	
	public byte[] accessDeviceStatus(String type, int id) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(type, id) + 1;
		return this.readByteArray(address, 1);
	}	

	public byte[] accessDeviceStatus(String type) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(type);
		return this.readByteArray(address, 1);
	}	
	
	public byte[] accessDeviceStatus(String type, int id , int offset, int length) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(type, id) + offset;
		return this.readByteArray(address, length);
	}
	
	
	
	
	/*   
	 "config.mode"    "config.fill"   
	 */
	public void configMode(String name, ICommand command) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(name);
		byte values[] = {command.getValue()};		
		LOG.debug("configMode   :"+this.getBaseAddress() + "  "+(long)this.getTableFieldOffset(name)+"  "+command.getValue());		
		this.writeByteArray(address, values);
	}
	
	public byte[] getModeStatus(String name, int numberOfBytes, int offset) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(name) + offset;
		System.out.println("get mode status   "+  (long)this.getTableFieldOffset(name)   +"   "+this.getBaseAddress()+"   "+address );
		return this.readByteArray(address, numberOfBytes);		
	}
	
	
	
	
	/*
	 *   "config.attr"
	 */
	public void configAttribute(String name, byte values[]) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(name);
		long number =(long)this.getTableFieldNumberOfByte(name);
		this.writeByteArray(address, values);
	}

	public byte[] getAttributedStatus(String name) throws AdsException{		
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(name);
		long number =(long)this.getTableFieldNumberOfByte(name);		
		//System.out.println("getAttributedStatus   "+name+"    "+number);
		return this.readByteArray(address, (int)number);
	}	
	
	public byte[] getAttributedStatus(String name, int numberOfBytes) throws AdsException{
		long address = this.getBaseAddress() + (long)this.getTableFieldOffset(name);
		System.out.println( this.getTableFieldOffset(name)  + "   "+name);
		int number = numberOfBytes;
		return this.readByteArray(address, number);			
	}	
	

	public byte[] getRawArray(int begin, int numberOfBytes) throws AdsException{
		long address = this.getBaseAddress() + begin ;
		int number = numberOfBytes;
		return this.readByteArray(address, number);			
	}		
	
	public void parseCommand(MqttCommand webcommand) throws AdsException, DeviceTypeException{

		LOG.debug("basesystem parseCommand");
	}
	
	public void parseState(MqttCommand webcommand) throws AdsException, DeviceTypeException{
		LOG.debug("basesystem parseState");

	}	
	
	public String getType(){
		return "";
	}	
	
}
