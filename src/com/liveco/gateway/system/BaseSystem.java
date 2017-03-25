package com.liveco.gateway.system;

import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.AdsListener;

import de.beckhoff.jni.AdsConstants;
import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallDllFunction;
import de.beckhoff.jni.tcads.AdsCallbackObject;
import de.beckhoff.jni.tcads.AdsNotificationAttrib;

public class BaseSystem {
	
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
		System.out.println("address : "+base_address);
		for(int i = 0; i<test_array.length;i++){
			System.out.print(test_array[i]+"  ");
		}
		System.out.println();
		System.out.println("-----------------------");
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
	
	

 	
}
