package com.liveco.gateway.system;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.AirConditionerConstant.ControlTable;
import com.liveco.gateway.constant.AirConditionerConstant.FanCommand;
import com.liveco.gateway.constant.AirConditionerConstant.WorkCommand;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.JNILong;
import de.beckhoff.jni.tcads.AdsCallbackObject;
import de.beckhoff.jni.tcads.AdsNotificationHeader;
import de.beckhoff.jni.tcads.AdsSymbolEntry;
import de.beckhoff.jni.tcads.AmsAddr;
import de.beckhoff.jni.tcads.CallbackListenerAdsState;

public class SystemRepository {

    private static final Logger LOG = LogManager.getLogger(SystemRepository.class);

	public static String symbol = "GVL_HMI";

	private MainSystem main_system;
	private ArrayList<FogponicsSystem> fogponics_systems = new ArrayList<FogponicsSystem>();
	private ArrayList<HydroponicsSystem> hydroponic_systems = new ArrayList<HydroponicsSystem>();
	private ArrayList<PanelLightingSystem> panelLighting_systems = new ArrayList<PanelLightingSystem>();
	private ArrayList<ShelfLightingSystem> shelfLighting_systems = new ArrayList<ShelfLightingSystem>();
	private ArrayList<CO2System> co2_systems = new ArrayList<CO2System>();
	private ArrayList<AirConditioner> air_conditioner_systems = new ArrayList<AirConditioner>();
	private ArrayList<AirConditionerInner> air_conditioner_inner_systems = new ArrayList<AirConditionerInner>();
	private ArrayList<NurientSystem> nutrient_systems = new ArrayList<NurientSystem>();

	private ADSConnection ads;

	String system_status[] = new String[7];
	Boolean[] system_error = new Boolean[7]; //{false,false,false,false,false,false,false};



	public SystemRepository(){
		//test
	}



	public SystemRepository(ADSConnection ads) throws AdsException{
		this.ads = ads;
		scanSystems();
	}

	public void addScanSystem(ADSConnection ads) throws AdsException{
		this.ads = ads;
		scanSystems();
	}

	public ADSConnection getADSConnection(){
		return this.ads;
	}
	/*
		// "G_HMI.stModeChage"
		// "G_HMI.stHalfAeroponicCirculationSystem"
		// "G_HMI.stConfig"
		// "G_HMI.stCD"
		// "G_HMI.stIndoorCondition"
		// "G_HMI.nLedTube[6]"
		// "G_HMI.nLedTubeA1"
		// "G_HMI.nLedPanelA1[6]"
		// "G_HMI.nLedPanelA2[12]"
	 */
	private void initRepository(){
		hydroponic_systems = new ArrayList<HydroponicsSystem>();
		fogponics_systems = new ArrayList<FogponicsSystem>();
		shelfLighting_systems = new ArrayList<ShelfLightingSystem>();
		panelLighting_systems = new ArrayList<PanelLightingSystem>();
		air_conditioner_systems  = new ArrayList<AirConditioner>();
		co2_systems = new ArrayList<CO2System>();
		nutrient_systems = new ArrayList<NurientSystem>();
		main_system = null;
	}

	public void scanSystems() throws AdsException{

		initRepository();
		/*	*/
		this.getMode("G_HMI.stModeChage");

        // nutrient system
		this.discover( "G_HMI.stConfig", NurientSystem.type,
				new ArrayList<>(Arrays.asList(  "C1"))   );

 		this.discover( "G_HMI.stWaterCirculationSystem", HydroponicsSystem.type,
	            new ArrayList<>(Arrays.asList(   "A1","A2","A3,A4","B3,B4","B2"   ))
	          );

		this.discover( "G_HMI.stHalfAeroponicCirculationSystem",  FogponicsSystem.type,
		                new ArrayList<>(Arrays.asList(   "B1"   ))
		              );


		this.discover( "G_HMI.nLedPanalA1", PanelLightingSystem.type,
                			new ArrayList<>(Arrays.asList( "A1_33","A1_43","A1_32","A1_42","A1_31","A1_41"	  ))
				          );


		this.discover( "G_HMI.nLedPanalA2", PanelLightingSystem.type,
				new ArrayList<>(Arrays.asList(  "A2_43","A2_33","A2_23","A2_13",
												"A2_42","A2_32","A2_22","A2_12",
												"A2_41","A2_31","A2_21","A2_11"
						                     ))
			             );

		// CO2 system
		this.discover( "G_HMI.stCD", CO2System.type,
				new ArrayList<>(Arrays.asList(  "A1,A2","A3,A4","B1,B2","B3,B4"))  );

		// led tube
		this.discover( "G_HMI.nLedTube", ShelfLightingSystem.type,
				new ArrayList<>(Arrays.asList(  "A3","A4","B1","B2","B3","B4"))   );



		// air conditioner system
		this.discover( "G_HMI.stCondition", AirConditioner.type,
				new ArrayList<>(Arrays.asList(  "A1,A2"))   );	 //,"A3,A4","B1,B2","B3,B4","show"



	}

	public void getMode(String root_system_symbol) {
		try {

			AdsSymbolEntry ads_symbol = ads.readArraySymbol(root_system_symbol);
			int total_size = ads_symbol.getSize();
			long address = ads_symbol.getiOffs();
			byte []byte_array = ads.readSymbolByteArray(ads_symbol,total_size );

			int data_length =  4;

			LOG.debug("scanSubSystem "+address+"  "+byte_array  + " "+byte_array.length);
			for(int i = 0; i<byte_array.length;i++){
				System.out.print("getMode  -------  "+byte_array[i]+"  ");
			}

			main_system = new MainSystem(ads,
						                    0,
						                    "main system",
						                   address,
						                   Arrays.copyOfRange(byte_array, 0, data_length));

		} catch (AdsException e) {
			e.printStackTrace();
		}
	}



	public void discover( String root_system_symbol , SystemStructure system_type,  ArrayList list ) throws AdsException{
		long address = 0;
		byte []byte_array = null;
		int total_size;
		/*                      test                       */

		LOG.debug( "scanSubSystem"+ root_system_symbol  );
		System.out.println("scanSubSystem "+root_system_symbol);

			AdsSymbolEntry ads_symbol = ads.readArraySymbol(root_system_symbol);
			total_size = ads_symbol.getSize();
			address = ads_symbol.getiOffs();
			byte_array = ads.readSymbolByteArray(ads_symbol,total_size );

			LOG.debug("scanSubSystem "+address+"  "+byte_array  + " "+byte_array.length);
			System.out.println("scanSubSystem "+root_system_symbol+"    "+address+"  "+byte_array  + " "+byte_array.length);
			for(int i = 0; i<byte_array.length;i++){
				System.out.print(byte_array[i]+"  ");
			}

			/*             test2               */

	    int data_length =  0;

		for(int i = 0 ; i< list.size() ; i++){

			switch(system_type) {

				case HYDROPONICS:
					data_length =  20; //HydroponicsSystem.data_length;
					HydroponicsSystem system = new HydroponicsSystem(ads,
							                                          i,
							                                          (String) list.get(i),
									                                 address+i*data_length,
									                                 Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length)
                            );
					hydroponic_systems.add(system);
					break;

				case FOGPONICS_SYSTEM:
					data_length =  30; //FogponicsSystem.data_length;
					FogponicsSystem system1 = new FogponicsSystem(ads,
																	 i,
																	 (String) list.get(i),
									                                address+i*data_length,
									                                Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length)
                            );
					fogponics_systems.add( system1);

				break;

				case PANEL_LIGHTING_SYSTEM:
					data_length =  5;
					PanelLightingSystem system4 = new PanelLightingSystem(ads,
																 i,
																 (String) list.get(i),
								                                address+i*data_length,
								                                Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length));

          System.out.println("led panel lighting add "+ panelLighting_systems.size());                                      
					panelLighting_systems.add(system4);

				break;


				case CO2_SYSTEM:
					data_length =  20;
					CO2System system2 = new CO2System(ads,
												 i,
												 (String) list.get(i),
				                               address+i*data_length,
				                               Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length)
					);
					co2_systems.add(system2);

				break;

				case SHELF_LIGHTING_SYSTEM:
					data_length =  6;
					ShelfLightingSystem system3 = new ShelfLightingSystem(ads,
																	 i,
																	 (String) list.get(i),
									                                address+i*data_length,
									                                Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length));
					shelfLighting_systems.add(system3);

				break;

				case AIR_CONDITIONING_SYSTEM:
					data_length =  10;

					AirConditioner              air_1 = new AirConditioner(ads,0,"A1", address, Arrays.copyOfRange(byte_array, 0, 10));
					/*
					byte[] tests = Arrays.copyOfRange(byte_array, 0, 10);
					System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ");
					for(int k=0;k<tests.length;k++) {
						System.out.print(tests[k]+" ");
					}
					*/


					AirConditionerInner air_inner1 =new AirConditionerInner(ads,0,"A1", address+10, Arrays.copyOfRange(byte_array, 10,30));
					AirConditionerInner air_inner2 =new AirConditionerInner(ads,1,"A1", address+30, Arrays.copyOfRange(byte_array, 30, 52 ));
					AirConditioner           air_2 = new AirConditioner(ads,1,"A1", address+52, Arrays.copyOfRange(byte_array, 52,  62));
					AirConditionerInner air_inner3 =new AirConditionerInner(ads,2,"A1", address+62, Arrays.copyOfRange(byte_array, 62, 82));
					AirConditionerInner air_inner4 =new AirConditionerInner(ads,3,"A1", address+82, Arrays.copyOfRange(byte_array, 82, 102));
					AirConditionerInner air_inner5 =new AirConditionerInner(ads,4,"A1", address+102, Arrays.copyOfRange(byte_array, 102, 124));

					System.out.println();
					System.out.println(".....................................AIR_CONDITIONING_SYSTEM   "+address);




					air_conditioner_systems.add(air_1);
					air_conditioner_systems.add(air_2);
					air_conditioner_inner_systems.add(air_inner1);
					air_conditioner_inner_systems.add(air_inner2);
					air_conditioner_inner_systems.add(air_inner3);
					air_conditioner_inner_systems.add(air_inner4);
					air_conditioner_inner_systems.add(air_inner5);


				break;


				case NUTRIENT_SYSTEM:
					data_length =  18;
					NurientSystem system6 = new NurientSystem(ads,
															 i,
															 (String) list.get(i),
							                               address+i*data_length,
							                               Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length));
					nutrient_systems.add(system6);

				break;
			}
		}
	}



    public BaseSystem findSystem(SystemStructure system_symbol , int id) throws NullPointerException,IndexOutOfBoundsException {
    	BaseSystem system = null;
    	try{

        	switch(system_symbol){
	    		case FOGPONICS_SYSTEM:
	    			 system = fogponics_systems.get(id);
	    		break;

	    		case CO2_SYSTEM:
	    			 system = co2_systems.get(id);
	    		break;

	    		case HYDROPONICS:
	    			 system = hydroponic_systems.get(id);
	    		break;

	    		case AIR_CONDITIONING_SYSTEM:
	    			System.out.println("AIR_CONDITIONING_SYSTEM   "+id);
	    			system = air_conditioner_systems.get(id);
	    		break;

	    		case AIR_CONDITIONING_INNER_SYSTEM:
	    			system = air_conditioner_inner_systems.get(id);
	    		break;

	    		case PANEL_LIGHTING_SYSTEM:
	    			system = panelLighting_systems.get(id);
	    		break;

	    		case SHELF_LIGHTING_SYSTEM:
	    			system = shelfLighting_systems.get(id);
	    		break;

	    		case NUTRIENT_SYSTEM:
	    			system = nutrient_systems.get(id);
	    		break;

	    		case MAIN:
	    			system = main_system;
        	}

    	}catch(NullPointerException error){
    		throw error;
    	}catch(IndexOutOfBoundsException error){
    		throw error;
    	}
    	return system;
    }

    private void saveSystemError(SystemStructure system_symbol, String error){
    	switch(system_symbol){
    		case FOGPONICS_SYSTEM:
    			system_status[0] = error;
    			system_error[0] = true;
    		break;

    		case HYDROPONICS:
    			system_status[1] = error;
    			system_error[1] = true;
    		break;

    		case PANEL_LIGHTING_SYSTEM:
    			system_status[2] = error;
    			system_error[2] = true;
    		break;

    		case SHELF_LIGHTING_SYSTEM:
    			system_status[3] = error;
    			system_error[3] = true;
    		break;

    		case CO2_SYSTEM:
    			system_status[4] = error;
    			system_error[4] = true;
    		break;

    		case AIR_CONDITIONING_SYSTEM:
    			system_status[5] = error;
    			system_error[5] = true;
    		break;

    		case NUTRIENT_SYSTEM:
    			system_status[6] = error;
    			system_error[6] = true;
    		break;
    	}
    }

    public String getSystemStatus(){

    	System.out.println(system_error);

    	//if(!system_error[0])
    		system_status[0] = "fogponic system found successfully with number :"+ hydroponic_systems.size() +"\n";
    	//if(!system_error[1])
    		system_status[1] = "hydroponics system found successfully with number :"+  hydroponic_systems.size()+"\n";
    	//if(!system_error[2])
    		system_status[2] = "panel lighting system found successfully with number :"+ panelLighting_systems.size()+"\n";
    	//if(!system_error[3])
    		system_status[3] = "shelf lighting system found  successfully with number :"+ shelfLighting_systems.size()+"\n";
    	//if(!system_error[4])
    		system_status[4] = "CO2 system found successfully with number :"+ co2_systems.size()+"\n";
    	//if(!system_error[5])
    		system_status[5] = "Air conditioner system found successfully with number :"+   air_conditioner_systems.size()+"\n";
    	//if(!system_error[6])
    		system_status[6] = "nutrient system found successfully with number :"+   nutrient_systems.size()+"\n";
    	return system_status[0] + system_status[1] + system_status[2] + system_status[3] + system_status[4] + system_status[5] + system_status[6];

    }

	public ArrayList<CO2System> getCO2Systems(){
		return co2_systems;
	}

	public ArrayList<AirConditioner> getAirConditioners(){
		return air_conditioner_systems;
	}

	public ArrayList<AirConditionerInner> getAirConditionerInners(){
		return air_conditioner_inner_systems;
	}

	public ArrayList<PanelLightingSystem> getPanelLightingSystems(){
		return panelLighting_systems;
	}

	public ArrayList<ShelfLightingSystem> getShelfLightingSystems(){
		return shelfLighting_systems;
	}

	public ArrayList<FogponicsSystem> getFogponicsSystems(){
		return fogponics_systems;
	}

	public ArrayList<HydroponicsSystem> getHydroponicsSystems(){
		return hydroponic_systems;
	}

	public ArrayList<NurientSystem> getNurientSystem(){
		return nutrient_systems;
	}

	public MainSystem getMainSystem(){
		return main_system;
	}


	public boolean isPLCConnected(){
		if(ads.isConnected() && (panelLighting_systems.size()>0 || shelfLighting_systems.size() >0 || hydroponic_systems.size()>0 ))
			return true;
		else
			return false;
	}



	/*****  test NUTRIENT SYSTEM SENSOR *****/
	// G_HMI.stConfig.rPHActualValue     G_HMI.stConfig.rPHCheckActualValue    G_HMI.stConfig.rECCheckActualValue
	public void getPHNotification(String sensor_symbol) {
		try {

			AdsSymbolEntry ads_symbol = ads.readArraySymbol(sensor_symbol);
			int total_size = ads_symbol.getSize();
			long address = ads_symbol.getiOffs();
			byte []byte_array = ads.readSymbolByteArray(ads_symbol,total_size );


			LOG.debug("getSensorNotification "+sensor_symbol+"      "+address+"  "+byte_array  + " "+byte_array.length);
			for(int i = 0; i<byte_array.length;i++){
				System.out.print(byte_array[i]+"  ");
			}

			AdsCallbackObject ECObject;
			PHAdsListener PHlistener = new PHAdsListener();
			JNILong ECNotification;
			ECObject = new AdsCallbackObject();
			ECObject.addListenerCallbackAdsState(PHlistener);
			ECNotification = new JNILong();
			ads.createNotification(address, ECNotification, PHlistener);

		} catch (AdsException e) {
			e.printStackTrace();
		}
	}

	public void getECNotification(String sensor_symbol) {
		try {

			AdsSymbolEntry ads_symbol = ads.readArraySymbol(sensor_symbol);
			int total_size = ads_symbol.getSize();
			long address = ads_symbol.getiOffs();
			byte []byte_array = ads.readSymbolByteArray(ads_symbol,total_size );


			LOG.debug("getSensorNotification "+sensor_symbol+"      "+address+"  "+byte_array  + " "+byte_array.length);
			for(int i = 0; i<byte_array.length;i++){
				System.out.print(byte_array[i]+"  ");
			}

			AdsCallbackObject ECObject;
			ECAdsListener EClistener = new ECAdsListener();
			JNILong ECNotification;
			ECObject = new AdsCallbackObject();
			ECObject.addListenerCallbackAdsState(EClistener);
			ECNotification = new JNILong();
			ads.createNotification(address, ECNotification, EClistener);

		} catch (AdsException e) {
			e.printStackTrace();
		}
	}

	 class PHAdsListener implements CallbackListenerAdsState {
	    private final static long SPAN = 11644473600000L;

	    public void onEvent(AmsAddr addr,AdsNotificationHeader notification,long user) {

	        // The PLC timestamp is coded in Windows FILETIME.
	        long dateInMillis = notification.getNTimeStamp();
	        // Date accepts millisecs since 01.01.1970.
	        Date notificationDate = new Date(dateInMillis / 10000 - SPAN);
	        byte data [] = notification.getData();
	        LOG.debug("PH Value:\t\t"
	                + Convert.ByteArrToFloat(notification.getData())+"    "+notification.getData().length+ "   "+ data[0]+"  "+data[1]+"  "+data[2]+"  "+data[3]);

	        //System.out.println("Notification:\t" + notification.getHNotification());
	        //System.out.println("Time:\t\t" + notificationDate.toString());
	        //System.out.println("User:\t\t" + user);
	        //System.out.println("ServerNetID:\t" + addr.getNetIdString() + "\n");
	    }
	}

	 class ECAdsListener implements CallbackListenerAdsState {
		    private final static long SPAN = 11644473600000L;

		    public void onEvent(AmsAddr addr,AdsNotificationHeader notification,long user) {

		        // The PLC timestamp is coded in Windows FILETIME.
		        long dateInMillis = notification.getNTimeStamp();
		        // Date accepts millisecs since 01.01.1970.
		        Date notificationDate = new Date(dateInMillis / 10000 - SPAN);
		        byte data [] = notification.getData();
		        LOG.debug("EC Value:\t\t"
		                + Convert.ByteArrToFloat(notification.getData())+"    "+notification.getData().length+ "   "+ data[0]+"  "+data[1]+"  "+data[2]+"  "+data[3]);

		        //System.out.println("Notification:\t" + notification.getHNotification());
		        //System.out.println("Time:\t\t" + notificationDate.toString());
		        //System.out.println("User:\t\t" + user);
		        //System.out.println("ServerNetID:\t" + addr.getNetIdString() + "\n");
		    }
		}
}
