package com.liveco.gateway.system;
import java.util.ArrayList;
import java.util.Arrays;

import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;

import de.beckhoff.jni.tcads.AdsSymbolEntry;

public class SystemRepository {

	public static String symbol = "GVL_HMI";

	ArrayList<HydroponicsSystem> hydroponic_systems = new ArrayList<HydroponicsSystem>();
	ArrayList<FogponicsSystem> fogponics_systems = new ArrayList<FogponicsSystem>();
	ArrayList<ShelfLightingSystem> shelfLighting_systems = new ArrayList<ShelfLightingSystem>();
	ArrayList<PanelLightingSystem> panelLighting_systems = new ArrayList<PanelLightingSystem>();
	ArrayList<AirConditioner> air_conditioner_systems = new ArrayList<AirConditioner>();
	ArrayList<CO2System> co2_systems = new ArrayList<CO2System>();
	
	
	ADSConnection ads;

	public SystemRepository(){
		//test
    	HydroponicsSystem system = new HydroponicsSystem(null,0,"af" );
    	hydroponic_systems.add(system);
	}	
	
	public SystemRepository(ADSConnection ads){
		this.ads = ads;
		scanSystems();
	}

	
	public void scanSystems(){
		this.scanSubSystem(HydroponicsSystem.type, 4);
		//this.scanSubSystem(AeroponicsSystem.type, 4);
		//this.scanSubSystem(ShelfLightingSystem.type, 4);
		//this.scanSubSystem(PanelLightingSystem.type, 4);
		//this.scanSubSystem(AirConditioner.type , 4);
		//this.scanSubSystem(CO2System.type, 4);		
	}

	
	public void scanSubSystem(SystemStructure system_symbol , int number){

		try {
			System.out.println(  system_symbol.getSymbol()  );
			AdsSymbolEntry ads_symbol = ads.readArraySymbol(symbol+"."+system_symbol.getSymbol());
			int total_size = ads_symbol.getSize();
			long address = ads_symbol.getiOffs();
			byte []byte_array = ads.readSymbolByteArray(ads_symbol,total_size );
			
			System.out.println("scanSubSystem "+address+"  "+byte_array);
			for(int i = 0; i<byte_array.length;i++){
				System.out.print(byte_array[i]+"  ");
			}
			System.out.println();
			
			switch(system_symbol) {
				
				case HYDROPONICS:
					for(int i = 0 ; i< number-1 ; i++){
						int data_length = HydroponicsSystem.data_length;
						HydroponicsSystem system = new HydroponicsSystem(ads,	
																		 i,HydroponicsSystem.type+"_"+i,
								                                         address+i*data_length,
								                                         Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length)
								                                         );
						hydroponic_systems.add(system);
						//system.test(system.getByteArray());
						//System.out.println(data_length);
						//system.test(system.readByteArray( data_length));						
				
					}
	
				break;
				
				case FOGPONICS_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						FogponicsSystem a = new FogponicsSystem(ads,i,"H1");
						fogponics_systems.add(i, a);
					}
				break;
				
				case AIR_CONDITIONING_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						AirConditioner a = new AirConditioner(ads,i,"H1");
						air_conditioner_systems.add(i, a);
					}
				break;
				
				case CO2_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						CO2System a = new CO2System(ads,i,"H1");
						co2_systems.add(i, a);
					}
				break;
				
				case SHELF_LIGHTING_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						ShelfLightingSystem a = new ShelfLightingSystem(ads,i,"H1");
						shelfLighting_systems.add(i, a);
					}
				break;
				
				case PANEL_LIGHTING_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						PanelLightingSystem a = new PanelLightingSystem(ads,i,"H1");
						panelLighting_systems.add(i, a);
					}
				break;
			}				
		} catch (AdsException e) {
			e.printStackTrace();
		}		
	}
	
    
    public BaseSystem findSystem(String system_type , int id) throws NullPointerException,IndexOutOfBoundsException {
    	BaseSystem system = null;
    	try{

        	switch(system_type){
	    		case "fogponics":
	    			 system = fogponics_systems.get(0);
	    		break;
	    		
	    		case "co2_system":
	    			 system = co2_systems.get(0);
	    		break;
	    		
	    		case "hydroponics":
	    			 system = hydroponic_systems.get(0);
	    		break;
	    		
	    		case "air_conditioning":
	    			system = air_conditioner_systems.get(0);
	    		break;
	    		
	    		case "panel_lighting":
	    			system = panelLighting_systems.get(0);
	    		break;
	    		
	    		case "shelf_lighting":
	    			system = panelLighting_systems.get(0);
	    		break;
        	}    		
    		
    		
    	}catch(NullPointerException error){
    		throw error;
    	}catch(IndexOutOfBoundsException error){
    		throw error;
    	}	
    	return system;
    }	
}
