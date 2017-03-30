package testADS;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.JNIByteBuffer;
import de.beckhoff.jni.tcads.AmsAddr;
import de.beckhoff.jni.tcads.AdsCallDllFunction;
import de.beckhoff.jni.tcads.AdsSymbolEntry;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Test {

	public static AmsAddr open(){
		
        long err;
		
        AmsAddr addr = new AmsAddr();
        System.out.println( addr.getNetId().toString() );
        // Open communication            
        AdsCallDllFunction.adsPortOpen();
        err = AdsCallDllFunction.getLocalAddress(addr);
        addr.setPort(851);
        System.out.println("netid:"+AdsCallDllFunction.ADSERR_INV_AMS_NETID +" "+ AdsCallDllFunction.AMSPORT_R0_PLC_RTS1);
        if (err != 0) {
            System.out.println("Error: Open communication: 0x"
                    + Long.toHexString(err));
        } else {
            System.out.println("Success: Open communication!");
        }
        
        return addr;
	
	}
	
	public static void readSymbol(AmsAddr addr, String symbol){
        long err;
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(
                Convert.StringToByteArr(symbol,true));
        JNIByteBuffer dataBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);
        
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_HNDBYNAME,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        // Handle: byte[] to int
        int hdlBuffToInt = Convert.ByteArrToInt(handleBuff.getByteArray());
        
        if(err!=0) { 
            System.out.println("Error: Get handle: 0x" 
                    + Long.toHexString(err)); 
        } else {
            System.out.println("Success: Get handle!, "+hdlBuffToInt);
        }		
        // Read value by handle
        err = AdsCallDllFunction.adsSyncReadReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_VALBYHND,
                                            hdlBuffToInt,
                                            0x1,
                                            dataBuff);
        if(err!=0)
        {
            System.out.println("Error: Read by handle: 0x"
                                + Long.toHexString(err));
        }
        else
        {
            // Data: byte[] to int
            int intVal = Convert.ByteArrToInt(dataBuff.getByteArray());
            System.out.println("Success: PLCVar value: " + intVal);
        }

        // Release handle
        err = AdsCallDllFunction.adsSyncWriteReq(addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err!=0) {
            System.out.println("Error: Release Handle: 0x"
                    + Long.toHexString(err));
        } else {
            System.out.println("Success: Release Handle!");
        }		
	}
	
	public static void writeSymbol(AmsAddr addr, String symbol, byte value){
        JNIByteBuffer handleBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);      
        // 
        long nErr = 0;
        long symHandle = 0;        
        byte[] btSymName = symbol.getBytes();

        JNIByteBuffer symbuff = new JNIByteBuffer(btSymName.length);
        symbuff.setByteArray(btSymName);
        
        nErr = AdsCallDllFunction.adsSyncReadWriteReq(addr, 0xF003, 0x0,
        		handleBuff.getUsedBytesCount(), handleBuff, //buffer for getting handle
                symbuff.getUsedBytesCount(), symbuff); //buffer containg symbolpath

       //get handle
        byte[] byteArr = new byte[4];
        byteArr = handleBuff.getByteArray();
        symHandle = Convert.ByteArrToInt(byteArr);        
        if(nErr!=0) { 
            System.out.println("Error: Get handle: 0x" 
                    + Long.toHexString(nErr)); 
        } else {
            System.out.println("Success: Get handle!"+symHandle);
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
        nErr = AdsCallDllFunction.adsSyncWriteReq(addr, 0xf005, symHandle,databuff.getUsedBytesCount(), databuff);
                                  
        final int buffsize2 = 1;  //int
        JNIByteBuffer databuff2 = new JNIByteBuffer(buffsize2);        
        if(nErr == 0)
        {
          Integer integerVal = new Integer(intVal);
          System.out.println("Write by Symbol success : " + integerVal);
        }
        else
        {
          System.out.println("Write by Symbol error : " + nErr);
        }		
	}

	public static void readStructSymbol(AmsAddr addr, String symbol){

        long err;
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(
                Convert.StringToByteArr(symbol,true));
        JNIByteBuffer dataBuff = new JNIByteBuffer(2);
                
        System.out.println(Integer.SIZE / Byte.SIZE);

        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_HNDBYNAME,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        // Handle: byte[] to int
        int hdlBuffToInt = Convert.ByteArrToInt(handleBuff.getByteArray());
        
        if(err!=0) { 
            System.out.println("Error: Get handle: 0x" 
                    + Long.toHexString(err)); 
        } else {
            System.out.println("Success: Get handle!, "+hdlBuffToInt);
        }		
        // Read value by handle
        err = AdsCallDllFunction.adsSyncReadReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_VALBYHND,
                                            hdlBuffToInt,
                                            0x1,
                                            dataBuff);
        if(err!=0)
        {
            System.out.println("Error: Read by handle: 0x"
                                + Long.toHexString(err));
        }
        else
        {
            // Data: byte[] to int
        	System.out.println("get used bytes   "+dataBuff.getUsedBytesCount());
        	 byte lowByte = dataBuff.getByteArray()[0];
             byte highByte = dataBuff.getByteArray()[1];
             byte[] valBytes = { lowByte, highByte };
             int valInt = Convert.ByteArrToShort(valBytes);
             System.out.println("Success:"+  symbol  +" value: " + lowByte+"  "+highByte);
        	
        }

        // Release handle
        err = AdsCallDllFunction.adsSyncWriteReq(addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err!=0) {
            System.out.println("Error: Release Handle: 0x"
                    + Long.toHexString(err));
        } else {
            System.out.println("Success: Release Handle!");
        }		
	}	

	public static void writeStructSymbol(AmsAddr addr, String symbol, byte value){
        JNIByteBuffer handleBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);      
        // 
        long nErr = 0;
        long symHandle = 0;        
        byte[] btSymName = symbol.getBytes();

        JNIByteBuffer symbuff = new JNIByteBuffer(btSymName.length);
        symbuff.setByteArray(btSymName);
        
        nErr = AdsCallDllFunction.adsSyncReadWriteReq(addr, 0xF003, 0x0,
        		handleBuff.getUsedBytesCount(), handleBuff, //buffer for getting handle
                symbuff.getUsedBytesCount(), symbuff); //buffer containg symbolpath

       //get handle
        byte[] byteArr = new byte[4];
        byteArr = handleBuff.getByteArray();
        symHandle = Convert.ByteArrToInt(byteArr);        
        if(nErr!=0) { 
            System.out.println("Error: Get handle: 0x" 
                    + Long.toHexString(nErr)); 
        } else {
            System.out.println("Success: Get handle!"+symHandle);
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
        nErr = AdsCallDllFunction.adsSyncWriteReq(addr, 0xf005, symHandle,databuff.getUsedBytesCount(), databuff);
                                  
        final int buffsize2 = 1;  //int
        JNIByteBuffer databuff2 = new JNIByteBuffer(buffsize2);        
        if(nErr == 0)
        {
          Integer integerVal = new Integer(intVal);
          System.out.println("Write by Symbol success : " + integerVal);
        }
        else
        {
          System.out.println("Write by Symbol error : " + nErr);
        }		
	}	

	

	// good
	public static void readSymbolAndValue(AmsAddr addr, String symbol, int size){

        long err;
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(0xFFFF);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(
                Convert.StringToByteArr(symbol,true));
        JNIByteBuffer dataBuff = new JNIByteBuffer(size);
                
        
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_INFOBYNAMEEX,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        AdsSymbolEntry adsSymbolEntry =
                new AdsSymbolEntry(handleBuff.getByteArray());

        // Write information to stdout
        System.out.println("Name:\t\t"
                            + adsSymbolEntry.getName());
        System.out.println("Index Group:\t"
                            + adsSymbolEntry.getiGroup());
        System.out.println("Index Offset:\t"
                            + adsSymbolEntry.getiOffs());
        System.out.println("Size:\t\t"
                            + adsSymbolEntry.getSize());
        System.out.println("Type:\t\t"
                            + adsSymbolEntry.getType());
        System.out.println("Comment:\t"
                            + adsSymbolEntry.getComment());

        
        
     // Read value by IndexGroup and IndexOffset
        err = AdsCallDllFunction.adsSyncReadReq(addr,
                                            0x4020,     // Index Group
                                            adsSymbolEntry.getiOffs(),        // Index Offset
                                            2,
                                            dataBuff);        
        
        if(err!=0)
        {
            System.out.println("Error: Read by handle: 0x" 
                    + Long.toHexString(err));
        }
        else
        {
        	System.out.println("get used bytes   "+dataBuff.getUsedBytesCount());
       	 	byte firstByte = dataBuff.getByteArray()[0];
            byte secondByte = dataBuff.getByteArray()[1];
            System.out.println("Success:"+  symbol  +" value: " + firstByte+"  "+secondByte+"  ");
        }        
        
        
        
        // Release handle
        err = AdsCallDllFunction.adsSyncWriteReq(addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err!=0) {
            System.out.println("Error: Release Handle: 0x"
                    + Long.toHexString(err));
        } else {
            System.out.println("Success: Release Handle!");
        }		
	}	
	
	

	
		

	/*
	public static void readSymbolArrayWrong(AmsAddr addr, String symbol, int size){

        long err;
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(Integer.SIZE / Byte.SIZE);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(
                Convert.StringToByteArr(symbol,true));
        JNIByteBuffer dataBuff = new JNIByteBuffer(size);
                
        
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_HNDBYNAME,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        // Handle: byte[] to int
        int hdlBuffToInt = Convert.ByteArrToInt(handleBuff.getByteArray());
        
        if(err!=0) { 
            System.out.println("Error: Get handle: 0x" 
                    + Long.toHexString(err)); 
        } else {
            System.out.println("Success: Get handle!, "+hdlBuffToInt);
        }		
        // Read value by handle
        err = AdsCallDllFunction.adsSyncReadReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_VALBYHND,
                                            hdlBuffToInt,
                                            0x1,
                                            dataBuff);
        if(err!=0)
        {
            System.out.println("Error: Read by handle: 0x"
                                + Long.toHexString(err));
        }
        else
        {
            // Data: byte[] to int
        	System.out.println("get used bytes   "+dataBuff.getUsedBytesCount());
        	 byte firstByte = dataBuff.getByteArray()[0];
             byte secondByte = dataBuff.getByteArray()[1];
             byte thirdByte = dataBuff.getByteArray()[1];
             byte[] valBytes = { firstByte, secondByte,thirdByte };
             //int valInt = Convert.ByteArrToShort(valBytes);
             System.out.println("Success:"+  symbol  +" value: " + firstByte+"  "+secondByte+"  "+thirdByte);
        }

        // Release handle
        err = AdsCallDllFunction.adsSyncWriteReq(addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err!=0) {
            System.out.println("Error: Release Handle: 0x"
                    + Long.toHexString(err));
        } else {
            System.out.println("Success: Release Handle!");
        }		
	}		
    */

	public static void writeSymbolArray(AmsAddr addr, String symbol, byte []values){

        long err;
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(0xFFFF);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(
                Convert.StringToByteArr(symbol,true));
        JNIByteBuffer dataBuff = new JNIByteBuffer(values.length);
                
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_INFOBYNAMEEX,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        AdsSymbolEntry adsSymbolEntry =
                new AdsSymbolEntry(handleBuff.getByteArray());

        // Write information to stdout
        System.out.println("Name:\t\t"
                            + adsSymbolEntry.getName());
        System.out.println("Index Group:\t"
                            + adsSymbolEntry.getiGroup());
        System.out.println("Index Offset:\t"
                            + adsSymbolEntry.getiOffs());
        System.out.println("Size:\t\t"
                            + adsSymbolEntry.getSize());
        System.out.println("Type:\t\t"
                            + adsSymbolEntry.getType());
        System.out.println("Comment:\t"
                            + adsSymbolEntry.getComment());	
        
        
        // Use JNIByteBuffer as a backing array for ByteBuffer
        ByteBuffer bb = ByteBuffer.wrap(dataBuff.getByteArray());
        // Write elements to buffer. Litte Endian!
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(values);
        
        err = AdsCallDllFunction.adsSyncWriteReq(addr,
                0x4020,     // Index Group
                adsSymbolEntry.getiOffs(),
                values.length,
                dataBuff);
        
        if(err!=0)
        {
            System.out.println("Error: write by handle: 0x"
                                + Long.toHexString(err));
        }
        else
        {
            // Data: byte[] to int
            System.out.println("successfully write by handle: 0x" + Long.toHexString(err));
                           	
        
        }

        // Release handle
        err = AdsCallDllFunction.adsSyncWriteReq(addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err!=0) {
            System.out.println("Error: Release Handle: 0x"
                    + Long.toHexString(err));
        } else {
            System.out.println("Success: Release Handle!");
        }
        
	}
	
	public static void readArraySymbol(AmsAddr addr, String symbol, int size){

        long err;
		
        JNIByteBuffer handleBuff = new JNIByteBuffer(0xFFFF);
        JNIByteBuffer symbolBuff = new JNIByteBuffer(
                Convert.StringToByteArr(symbol,true));
        JNIByteBuffer dataBuff = new JNIByteBuffer(size);
                
        
        // Get handle by symbol name
        err = AdsCallDllFunction.adsSyncReadWriteReq(addr,
                                            AdsCallDllFunction.ADSIGRP_SYM_INFOBYNAMEEX,
                                            0x0,
                                            handleBuff.getUsedBytesCount(),
                                            handleBuff,
                                            symbolBuff.getUsedBytesCount(),
                                            symbolBuff);
        
        AdsSymbolEntry adsSymbolEntry =
                new AdsSymbolEntry(handleBuff.getByteArray());

        // Write information to stdout
        System.out.println("Name:\t\t"
                            + adsSymbolEntry.getName());
        System.out.println("Index Group:\t"
                            + adsSymbolEntry.getiGroup());
        System.out.println("Index Offset:\t"
                            + adsSymbolEntry.getiOffs());
        System.out.println("Size:\t\t"
                            + adsSymbolEntry.getSize());
        System.out.println("Type:\t\t"
                            + adsSymbolEntry.getType());
        System.out.println("Comment:\t"
                            + adsSymbolEntry.getComment());		


        // Read value by IndexGroup and IndexOffset
        err = AdsCallDllFunction.adsSyncReadReq(addr,
                                            0x4020,     // Index Group
                                            adsSymbolEntry.getiOffs(),        // Index Offset
                                            3,
                                            dataBuff);        
        
        if(err!=0)
        {
            System.out.println("Error: Read by handle: 0x" 
                    + Long.toHexString(err));
        }
        else
        {
        	System.out.println("get used bytes   "+dataBuff.getUsedBytesCount());
       	 	byte firstByte = dataBuff.getByteArray()[0];
            byte secondByte = dataBuff.getByteArray()[1];
            byte thirdByte = dataBuff.getByteArray()[2];
            System.out.println("Success read:"+  symbol  +" value: " + firstByte+"  "+secondByte+"  "+thirdByte);
        }        
        
        
        
        // Release handle
        err = AdsCallDllFunction.adsSyncWriteReq(addr,
                AdsCallDllFunction.ADSIGRP_SYM_RELEASEHND,
                0,
                handleBuff.getUsedBytesCount(),
                handleBuff);

        if(err!=0) {
            System.out.println("Error: Release Handle: 0x"
                    + Long.toHexString(err));
        } else {
            System.out.println("Success: Release Handle!");
        }        
	
	}	
	
	public static void close(AmsAddr addr){

        long err;
        
        // Close communication
        err = AdsCallDllFunction.adsPortClose();
        if(err!=0) { 
            System.out.println("Error: Close Communication: 0x" 
                    + Long.toHexString(err)); 
        }else{
        	System.out.println("close successfully");
        }		
		
	}

/*	
    public static void main(String[] args)
    {
    	
    	AmsAddr addr = Test.open();
    	//Test.readSymbol( addr , "GVL_HMI.pump");
    	//Test.writeSymbol(addr, "GVL_HMI.pump", (byte)77);
    	
    	//Test.readStructSymbol(addr,  "GVL_HMI.p1.HMICmd");
    	//Test.readStructSymbol(addr,  "GVL_HMI.p1.PLCStatus");
    	//Test.readStructSymbol(addr,  "GVL_HMI.p1");
        //Test.writeStructSymbol(addr,  "GVL_HMI.p1.PLCStatus", (byte)44);
    	//Test.writeStructSymbol(addr,  "GVL_HMI.p1", (byte)23);
    	

    	//byte array[] = { (byte)1, (byte)2, (byte)3};
    	//Test.writeSymbolArray(addr , "GVL_HMI.b",array );	
    	//Test.readArraySymbol(addr,   "GVL_HMI.b", 3);
    	
    	byte array2[] = { (byte)12, (byte)11, (byte)3,(byte)4,(byte)5,(byte)6,(byte)7};
    	Test.writeSymbolArray(addr , "GVL_HMI.p1",array2 );    	
    	Test.readSymbolAndValue(addr, "GVL_HMI.p1",2);
    	
    	Test.readSymbolAndValue(addr, "GVL_HMI.p1.PumpST",2);
    	Test.readArraySymbol( addr, "GVL_HMI.p1.aARRAY", 3);
    	
    	Test.close(addr);

    	//Test.connectMQTT();

 }
 */
  
	   public static void connectMQTT(){

		   
		   
		    class MqttCommandCallback implements MqttCallback {

		        private String lastCommand;

		        @Override
		        public void connectionLost (Throwable arg0)
		        {
		        }

		        @Override
		        public void deliveryComplete (IMqttDeliveryToken arg0)
		        {
		        }

		        @Override
		        public void messageArrived (String topic, MqttMessage message) throws Exception
		        {
		        	System.out.println(message.getPayload());

		        }

		        public String getLastCommand ()
		        {
		            return lastCommand;
		        }

		    }		   
		   
		   
		   
		   
	       String topic        = "/device/plc/abc/system/hydroponics/1/command";
	       String content      = "Message from MqttPublishSample";
	       int qos             = 2;
	       String broker       = "tcp://139.59.170.74:1883";
	       String clientId     = "JavaSample";
	       MemoryPersistence persistence = new MemoryPersistence();

	       try {
	           MqttClient client = new MqttClient(broker, clientId, persistence);
	           
	           MqttConnectOptions options = new MqttConnectOptions();
	            options.setCleanSession(true);
	            //options.setUserName(configuration.getMqttUsername());
	            //options.setPassword(configuration.getMqttPassword().toCharArray());
	            //options.setWill(configuration.getMqttTopicRoot() + MQTT_TOPIC_ONLINE, "0".getBytes(), 1, true);
	               
	     
	           System.out.println("Connecting to broker: "+broker);
	           client.connect(options);
	           System.out.println("Connected");
	           System.out.println("Publishing message: "+content);
	           MqttMessage message = new MqttMessage(content.getBytes());
	           message.setQos(qos);
	           client.publish(topic, message);
	           System.out.println("Message published");
	           
	           
	           client.subscribe("/device/plc/abc/system/hydroponics/1/command");
	           client.subscribe("/device/plc/abc/system/hydroponics/1/command");

	           client.setCallback(new MqttCommandCallback());
	           //client.disconnect();
	           //System.out.println("Disconnected");
	           //System.exit(0);
	       } catch(MqttException me) {
	           System.out.println("reason "+me.getReasonCode());
	           System.out.println("msg "+me.getMessage());
	           System.out.println("loc "+me.getLocalizedMessage());
	           System.out.println("cause "+me.getCause());
	           System.out.println("excep "+me);
	           me.printStackTrace();
	       }	   
		   
	   } 
    
    
    
  public static void testWriteAndRead(){
/*	  
      final int buffsize = 4;  //int
      JNIByteBuffer databuff = new JNIByteBuffer(buffsize);
      //i.e. convert int to byte[]
      int intVal = 36;
      byte[] byteArr2 = new byte[buffsize];
      byteArr2 = Convert.IntToByteArr(intVal);
      databuff.setByteArray(byteArr2);
      
      nErr = AdsCallDllFunction.adsSyncWriteReq(addr, 0x4020, 0 ,databuff.getUsedBytesCount(), databuff);
                      
      final int buffsize2 = 1;  //int
      JNIByteBuffer databuff2 = new JNIByteBuffer(buffsize2);        
      if(nErr == 0)
      {
        Integer integerVal = new Integer(intVal);
        System.out.println("Write by Symbol success : " + integerVal);
      }
      else
      {
        System.out.println("Write by Symbol error : " + nErr);
      }
      
      // Read value by IndexGroup and IndexOffset
      err = AdsCallDllFunction.adsSyncReadReq(addr,
                                          0x4020,     // Index Group
                                          0x0,        // Index Offset
                                          1,
                                          databuff2);        
      
      if(err == 0)
      {
          byte b = Convert.ByteArrToByte(databuff2.getByteArray());
          System.out.println("Success: PLCVa2r value: " + b);
      }
      else
      {
        System.out.println("Write by Symbol error : " + nErr);
      }	 
      */ 
  }
    
}
