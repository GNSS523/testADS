package com.liveco.gateway.plc;

import de.beckhoff.jni.AdsConstants;
import de.beckhoff.jni.Convert;
import de.beckhoff.jni.JNIByteBuffer;
import de.beckhoff.jni.JNILong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import de.beckhoff.jni.tcads.*;
public class ADSConnection {

    private static final Logger LOG = LogManager.getLogger(ADSConnection.class);
	
	
	private static final int BUFFERSIZE_BOOLEAN = 1;
	private static final int BUFFERSIZE_INT = 1;
	private static final int BUFFERSIZE_FLOAT = 4;
	
	AmsAddr addr = new AmsAddr();
	private long port = 0;
	private String netId;
	
	public boolean isConnected(){
		return port !=0;
	}

	public String getAddress() {
		return addr.getNetIdString() + ":" + addr.getPort();
	}	
	
	public long getPort(){
		return this.port;
	}
	
	public String getNetId(){
		return this.netId;
	}
	
	public String getAdsVersion() {

		AdsVersion lAdsVersion = AdsCallDllFunction.adsGetDllVersion();
		return new Integer(lAdsVersion.getVersion()) + "." + new Integer(lAdsVersion.getRevision()) + "."
				+ lAdsVersion.getBuild();
	}	

	
	public long openPort(boolean localNetAddr, String txtNetString, int Prt) {
		
		  LOG.info("openport "+localNetAddr+"  "+txtNetString+" "+Prt);
	
	      if (port == 0) {
	        AdsVersion lAdsVersion = AdsCallDllFunction.adsGetDllVersion();
	        LOG.debug("AdsVersion: " + new Integer(lAdsVersion.getVersion())
	                           + "." + new Integer(lAdsVersion.getRevision())
	                           + "." + lAdsVersion.getBuild());

	        port = AdsCallDllFunction.adsPortOpen();
	        // real PLC
	        if (localNetAddr == true) {
	          this.netId = txtNetString;
	          addr.setNetIdString(txtNetString);
	        }// computer simulator
	        else {
	          long nErr = AdsCallDllFunction.getLocalAddress(addr); //local netid
	          if (nErr != 0) {
	        	  LOG.error("getLocalAddress() failed with " + nErr);
	            AdsCallDllFunction.adsPortClose();
	            return 0;
	          }
	        }
	        addr.mPort = Prt;
	      }
	    return port;
	}	
	
	
	public void close(){

        long err;
        
        // Close communication
        err = AdsCallDllFunction.adsPortClose();
        if(err!=0) { 
        	 LOG.error("Error: Close Communication: 0x" + Long.toHexString(err)); 
                    
        }else{
        	 LOG.info("close successfully");
        }		
		
	}	
	
	

	public synchronized void closePort() throws AdsException {
		if (port != 0) {
		
			long nErr = AdsCallDllFunction.adsPortClose();

			port = 0;

			if (nErr != 0)
				throw new AdsException((int)nErr, "closePort() failed with code: " + nErr);
		}
	}


	public synchronized void readDeviceInfo(AdsDevName devName, AdsVersion adsVersion) throws AdsException {

		if (port == 0)
			throw new AdsException(1864, "ADS-Port not opened");

		long nErr = AdsCallDllFunction.adsSyncReadDeviceInfoReq(addr, devName, adsVersion);
		
		if (nErr != 0)
			throw new AdsException((int)nErr, "readDeviceInfo() failed with code: " + nErr);
	}	
	

	public AdsSymbolEntry readArraySymbol( String symbol) throws AdsException{
	
        JNIByteBuffer handleBuff = new JNIByteBuffer(0xFFFF);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(Convert.StringToByteArr(symbol,true));
        
        LOG.debug("readArraySymbol    "+symbol);
        
        long err, err1;        
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq( addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_INFOBYNAMEEX,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        AdsSymbolEntry adsSymbolEntry = new AdsSymbolEntry(handleBuff.getByteArray());
                
        // Write information to stdout
        LOG.debug("Name:\t\t"+ adsSymbolEntry.getName());                            
        LOG.debug("Index Group:\t" + adsSymbolEntry.getiGroup());                           
        LOG.debug("Index Offset:\t"+ adsSymbolEntry.getiOffs());                            
        LOG.debug("Size:\t\t" + adsSymbolEntry.getSize());                           
        LOG.debug("Type:\t\t"+ adsSymbolEntry.getType());                           
        LOG.debug("Comment:\t"+ adsSymbolEntry.getComment());		
                
        // Release handle
        err1 = AdsCallDllFunction.adsSyncWriteReq( addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err1!=0) {
            System.out.println("Error: Release Handle: 0x"+ Long.toHexString(err));
                    
        } else {
            System.out.println("Success: Release Handle!");
        }

        if(err!=0){
        	throw new AdsException((int)err);
        }        
        
        return adsSymbolEntry;
	
	}

	public long readSymbolAddress( String symbol) throws AdsException{
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(0xFFFF);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(Convert.StringToByteArr(symbol,true));
                
        long err, err1;        
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq( addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_INFOBYNAMEEX,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        AdsSymbolEntry adsSymbolEntry = new AdsSymbolEntry(handleBuff.getByteArray());
                
        // Write information to stdout
        LOG.debug("Name:\t\t"+ adsSymbolEntry.getName());                            
        LOG.debug("Index Group:\t" + adsSymbolEntry.getiGroup());                           
        LOG.debug("Index Offset:\t"+ adsSymbolEntry.getiOffs());                            
        LOG.debug("Size:\t\t" + adsSymbolEntry.getSize());                           
        LOG.debug("Type:\t\t"+ adsSymbolEntry.getType());                           
        LOG.debug("Comment:\t"+ adsSymbolEntry.getComment());		
                                   
        // Release handle
        err1 = AdsCallDllFunction.adsSyncWriteReq( addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err1!=0) {
        	 LOG.error("Error: Release Handle: 0x"+ Long.toHexString(err));
                    
        } else {
        	 LOG.info("Success: Release Handle!");
        }

        if(err!=0){
        	throw new AdsException((int)err);
        }        
                
        return adsSymbolEntry.getiOffs();
	
	}	

	public byte[] readSymbolByteArray( AdsSymbolEntry adsSymbolEntry, int size) throws AdsException{
		        
        long err;
			
		JNIByteBuffer dataBuff = new JNIByteBuffer(size);
        err = AdsCallDllFunction.adsSyncReadReq( addr,
                                            0x4020,     // Index Group
                                            adsSymbolEntry.getiOffs(),        // Index Offset
                                            size,
                                            dataBuff);        
        
        if(err!=0)
        {
        	LOG.error("Error: Read by handle: 0x" + Long.toHexString(err)); 
            throw new AdsException((int)err);
        }
        else
        {
        	 LOG.debug("get used bytes   "+dataBuff.getUsedBytesCount());         
        }        
        
        byte bytearray[] = dataBuff.getByteArray();
        
        return bytearray;
	
	}	
	
	
	public byte[] readSymbolByteArray( String symbol, int size) throws AdsException{
	
        AdsSymbolEntry adsSymbolEntry = readArraySymbol(symbol);
        
        long err;
			
		JNIByteBuffer dataBuff = new JNIByteBuffer(size);
        err = AdsCallDllFunction.adsSyncReadReq( addr,
                                            0x4020,     // Index Group
                                            adsSymbolEntry.getiOffs(),        // Index Offset
                                            size,
                                            dataBuff);        
        
        if(err!=0)
        {
        	 LOG.error("Error: Read by handle: 0x" + Long.toHexString(err)); 
            throw new AdsException((int)err);
        }
        else
        {
        	 LOG.debug("get used bytes   "+dataBuff.getUsedBytesCount());         
        }        
        
        byte bytearray[] = dataBuff.getByteArray();
        
        return bytearray;
	
	}

	public void writeSymbolByteArray( AdsSymbolEntry adsSymbolEntry, byte []values) throws AdsException{

        JNIByteBuffer dataBuff = new JNIByteBuffer(values.length);
                
        // Use JNIByteBuffer as a backing array for ByteBuffer
        ByteBuffer bb = ByteBuffer.wrap(dataBuff.getByteArray());
        // Write elements to buffer. Litte Endian!
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(values);
        
        long err;
        err = AdsCallDllFunction.adsSyncWriteReq( addr,
                0x4020,     // Index Group
                adsSymbolEntry.getiOffs(),
                values.length,
                dataBuff);
        
        if(err!=0)
        {
        	 LOG.error("Error: write by handle: 0x" + Long.toHexString(err));                            
            throw new AdsException((int)err);
        }
        else
        {
        	 LOG.debug("successfully write by handle: 0x" + Long.toHexString(err));
        }
    
	}	
	
	public void writeSymbolByteArray( String symbol, byte []values) throws AdsException{

        AdsSymbolEntry adsSymbolEntry = readArraySymbol(symbol);
        JNIByteBuffer dataBuff = new JNIByteBuffer(values.length);
                
        // Use JNIByteBuffer as a backing array for ByteBuffer
        ByteBuffer bb = ByteBuffer.wrap(dataBuff.getByteArray());
        // Write elements to buffer. Litte Endian!
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(values);
        
        long err;
        err = AdsCallDllFunction.adsSyncWriteReq( addr,
                0x4020,     // Index Group
                adsSymbolEntry.getiOffs(),
                values.length,
                dataBuff);
        
        if(err!=0)
        {
        	LOG.error("Error: write by handle: 0x" + Long.toHexString(err));                            
            throw new AdsException((int)err);
        }
        else
        {
        	 LOG.debug("successfully write by handle: 0x" + Long.toHexString(err));
        }
    
	}	
	
	public void writeSymbol(String symbol, byte value) throws AdsException{

		JNIByteBuffer handleBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);      
        // 
        long nErr = 0;
        long symHandle = 0;        
        byte[] btSymName = symbol.getBytes();

        JNIByteBuffer symbuff = new JNIByteBuffer(btSymName.length);
        symbuff.setByteArray(btSymName);
        
        nErr = AdsCallDllFunction.adsSyncReadWriteReq( addr, 0xF003, 0x0,
        		handleBuff.getUsedBytesCount(), handleBuff, //buffer for getting handle
                symbuff.getUsedBytesCount(), symbuff); //buffer containg symbolpath

       //get handle
        byte[] byteArr = new byte[4];
        byteArr = handleBuff.getByteArray();
        symHandle = Convert.ByteArrToInt(byteArr);        
        if(nErr!=0) { 
        	LOG.error("Error: Get handle: 0x"+ Long.toHexString(nErr));                     
            throw new AdsException((int)nErr);
        } else {
        	 LOG.debug("Success: Get handle!"+symHandle);
        }       
              
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        final int buffsize = 1;  //int
        JNIByteBuffer databuff = new JNIByteBuffer(buffsize);
        //i.e. convert int to byte[]
        int intVal = value;
        byte[] byteArr2 = new byte[buffsize];
        byteArr2 = Convert.IntToByteArr(intVal);
        databuff.setByteArray(byteArr2);

        //write variable by handle
        nErr = AdsCallDllFunction.adsSyncWriteReq( addr, 0xf005, symHandle,databuff.getUsedBytesCount(), databuff);
                                  
        final int buffsize2 = 1;  //int
        JNIByteBuffer databuff2 = new JNIByteBuffer(buffsize2);        
        if(nErr == 0)
        {
          Integer integerVal = new Integer(intVal);
          LOG.debug("Write by Symbol success : " + integerVal);
          
        }
        else
        {
        	LOG.error("Write by Symbol error : " + nErr);        	
            throw new AdsException((int)nErr);
        }		
	}	


	public void readSymbol( String symbol) throws AdsException{

		long err;
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(Convert.StringToByteArr(symbol,true));
        JNIByteBuffer dataBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);
                       
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq( addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_HNDBYNAME,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        // Handle: byte[] to int
        int hdlBuffToInt = Convert.ByteArrToInt(handleBuff.getByteArray());
        
        if(err!=0) { 
        	LOG.error("Error: Get handle: 0x"+ Long.toHexString(err));               
        } else {
        	 LOG.debug("Success: Get handle!, "+hdlBuffToInt);
        }		
        // Read value by handle
        err = AdsCallDllFunction.adsSyncReadReq( addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_VALBYHND,
                                            hdlBuffToInt,
                                            0x1,
                                            dataBuff);
        if(err!=0)
        {
        	LOG.error("Error: Read by handle: 0x" + Long.toHexString(err));                             
            throw new AdsException((int)err);

        }
        else
        {
            // Data: byte[] to int
            int intVal = Convert.ByteArrToInt(dataBuff.getByteArray());
            LOG.debug("Success: PLCVar value: " + intVal);
        }

        // Release handle
        err = AdsCallDllFunction.adsSyncWriteReq( addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err!=0) {
        	LOG.error("Error: Release Handle: 0x"+ Long.toHexString(err));
            throw new AdsException((int)err);
                   
        } else {
        	 LOG.debug("Success: Release Handle!");
        }		
	}	
	
	  public void writeAddress(long address,  byte values[]) throws AdsException{
		  
	        JNIByteBuffer databuff = new JNIByteBuffer(values.length);
	        
	        ByteBuffer bb = ByteBuffer.wrap(databuff.getByteArray());
	        bb.order(ByteOrder.LITTLE_ENDIAN);
	        bb.put(values);
	              
	        long err;
	        err = AdsCallDllFunction.adsSyncWriteReq(addr, 0x4020, address ,databuff.getUsedBytesCount(), databuff);
	                        
	        if(err == 0){      
	        	LOG.debug("Write by Symbol success : " );
	        }
	        else{
	        	LOG.error("Write by Symbol error : " + err);
	          throw new AdsException((int)err);	          
	        }		  
	
	  }
	  
	  public byte[] readAddress(long address, int buffersize) throws AdsException{
		  	          

	  	long err;
        JNIByteBuffer databuff = new JNIByteBuffer(buffersize);
	  	
        // Read value by IndexGroup and IndexOffset
        err = AdsCallDllFunction.adsSyncReadReq(addr,
                                            0x4020,     // Index Group
                                            address,        // Index Offset
                                            buffersize,
                                            databuff);        
        
        if(err == 0){
            return databuff.getByteArray();        
        }
        else{
        	 LOG.error("Write by Symbol error : " + err);
          throw new AdsException((int)err);
        }	 
		        
	}	
 
	public void createNotification(long indexOffset, JNILong notification ,  CallbackListenerAdsState listener)throws AdsException{
		
        // Specify attributes of the notificationRequest
        AdsNotificationAttrib attr = new AdsNotificationAttrib();
        attr.setCbLength(Integer.SIZE / Byte.SIZE);
        attr.setNTransMode(AdsConstants.ADSTRANS_SERVERONCHA);
        attr.setDwChangeFilter(10000000);   // 1 sec
        attr.setNMaxDelay(20000000);        // 2 sec

        long err;
        // Create notificationHandle
        err = AdsCallDllFunction.adsSyncAddDeviceNotificationReq(
            addr,
            0x4020,     // IndexGroup
            indexOffset,        // IndexOffset
            attr,       // The defined AdsNotificationAttrib object
            42,         // Choose arbitrary number
            notification);
        if(err!=0) { 
        	LOG.error("Error: Add notification: 0x"  + Long.toHexString(err));
            throw new AdsException((int)err);       
        }		
	}
	
	public void deleteNotification(JNILong notification) throws AdsException{
        long err;
		// Delete notificationHandle
        err = AdsCallDllFunction.adsSyncDelDeviceNotificationReq(
                addr,
                notification);
        if(err!=0) { 
        	LOG.error("Error: Remove notification: 0x"
                    + Long.toHexString(err)); 
        }		
	}

}
