import java.util.*;


import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.system.AirConditioner;
import com.liveco.gateway.system.CO2System;
import com.liveco.gateway.system.HydroponicsSystem;
import com.liveco.gateway.system.PanelLightingSystem;
import com.liveco.gateway.system.ShelfLightingSystem;


import de.beckhoff.jni.tcads.AdsSymbolEntry;

public class Main {
	
	public static String symbol = "GVL_HMI";

	ArrayList<HydroponicsSystem> hydroponics = new ArrayList<HydroponicsSystem>();
	ArrayList<ShelfLightingSystem> shelfLightings = new ArrayList<ShelfLightingSystem>();
	ArrayList<PanelLightingSystem> panelLightings = new ArrayList<PanelLightingSystem>();
	ArrayList<AirConditioner> air_conditioners = new ArrayList<AirConditioner>();
	ArrayList<CO2System> co2s = new ArrayList<CO2System>();
	
	ADSConnection ads;
	
	Main(){
		
		ads = new ADSConnection();
		ads.openPort(false, null, 851);		
				
		//this.scanSubSystem(HydroponicsSystem.type, 3);

		
	}

	
	public void scanSubSystem(com.liveco.gateway.constant.SystemStructure system_symbol , int number){

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
					for(int i = 0 ; i< number ; i++){
						int data_length = HydroponicsSystem.data_length;
						HydroponicsSystem system = new HydroponicsSystem(ads,	
																		 i,HydroponicsSystem.type+"_"+i,
								                                         address+i*data_length,
								                                         Arrays.copyOfRange(byte_array, i*data_length, (i+1)*data_length)
								                                         );
						//hydroponics.add(system);
						//system.test(system.getByteArray());
						//system.accessDeviceControl("actuator.pump",1,OnOffCommand.ON);
						//system.accessDeviceControl("actuator.valve",2,OnOffCommand.ON);
						//System.out.println(  "status :" +  system.accessDeviceStatus2("actuator.pump",1,OnOffCommand.ON)   );
						//System.out.println(  "status :" +  system.accessDeviceStatus2("actuator.valve",2,OnOffCommand.ON)   );
						//System.out.println(data_length);
						system.test(system.readByteArray( data_length));
					}
	
				break;
				

				
				case AIR_CONDITIONING_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						AirConditioner a = new AirConditioner(ads,i,"H1");
						air_conditioners.add(i, a);
					}
				break;
				
				case CO2_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						CO2System a = new CO2System(ads,i,"H1");
						co2s.add(i, a);
					}
				break;
				
				case SHELF_LIGHTING_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						ShelfLightingSystem a = new ShelfLightingSystem(ads,i,"H1");
						shelfLightings.add(i, a);
					}
				break;
				
				case PANEL_LIGHTING_SYSTEM:
					for(int i = 0 ; i < 4; i++){
						PanelLightingSystem a = new PanelLightingSystem(ads,i,"H1");
						panelLightings.add(i, a);
					}
				break;
			}			
			
			
		} catch (AdsException e) {
			
			e.printStackTrace();
		}		
		

	}
	


	
	
	public void configMode(byte mode){
		
	}
	
	public byte getMode(){
		return (byte)1;
	}	
	
	
	public void test(){
				
    	byte array2[] = { (byte)12, (byte)11, (byte)3,(byte)4,(byte)5,(byte)6,(byte)7};
    	try {
			ads.writeSymbolByteArray("GVL_HMI.p1",array2 );
		} catch (AdsException e1) {
			e1.printStackTrace();
		}
    	
    	try {
			ads.readSymbolByteArray("GVL_HMI.p1",2);
		} catch (AdsException e1) {
			e1.printStackTrace();
		}		
    	
    	try {
			ads.readSymbol("GVL_HMI.p1");
		} catch (AdsException e) {
			e.printStackTrace();
		}	

	}    	

    public static void main(String[] args)
    {    	

    }
}
