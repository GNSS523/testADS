package com.liveco.gateway.ui;

import com.liveco.gateway.constant.FogponicsConstant;
import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.FogponicsSystem;
import com.liveco.gateway.system.SystemRepository;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.tcads.AdsNotificationHeader;
import de.beckhoff.jni.tcads.AmsAddr;
import de.beckhoff.jni.tcads.CallbackListenerAdsState;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.event.*;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

public class FogponicsSystem_UI extends JPanel {

    private static final Logger LOG = LogManager.getLogger(FogponicsSystem_UI.class);
	
	private FogponicsSystem system;
    private static SystemRepository respository;
    
    // 妯″紡
    private JComboBox modeSelect;
    private JComboBox systemSelect;    
    private int number;

    // 涓や釜姘撮榾
    // 1
    private JLabel valveName1;
    private JButton valveOn1;
    private JLabel valveStatus1;
    // 2
    private JLabel valveName2;
    private JButton valveOn2;
    private JLabel valveStatus2;
    // 姘存车
    private JLabel pumpName;
    private JButton pumpOn;
    private JLabel pumpStatus;
    // 姘翠綅浼犳劅鍣�
    private JLabel sensorName;
    private JLabel sensorValue;
    // 姘存车杩愯鏃堕棿
    private JLabel runTime;
    private JLabel stopTime;
    private JTextField runTimeInput;
    private JTextField stopTimeInput;
    // 鐢宠鍔犳按
    private JButton addWater;
    private JLabel addExecute;
    // 瓒呭０娉㈤浘鍖栧櫒
    private JLabel atomizerName;
    private JButton atomizerOn;
    private JLabel atomizerStatus;
    
    // 闆惧寲鍣ㄨ繍琛屾椂闂�
    private JLabel fogrunTime;
    private JLabel fogstopTime;
    private JTextField fogrunTimeInput;
    private JTextField fogstopTimeInput;

    String mode_value;    
	String valve1_value;
	String valve2_value;
	String pump_value;
	String atomizer_value;
	
	int pump_attr__runtime_value, pump_attr_stoptime_value;
	int atomizer_attr__runtime_value, atomizer_attr__stoptime_value;    
    
    public FogponicsSystem_UI() {
        initGui();
    }

    public FogponicsSystem_UI(FogponicsSystem system) {
        this.system = system;
        initGui();
    }
    
    public FogponicsSystem_UI(SystemRepository respository) {
        this.respository = respository;      
        initGui();
        refreshSystemStatus(0);
    }
    
    public void addSystems(SystemRepository respository){
        this.respository = respository;
        refreshSystemStatus(0);
        systemListUI();        
    }     
    
    private void refreshSystemStatus(int index){
    	// select the system
    	try{		
    		this.system = (FogponicsSystem) respository.findSystem(SystemStructure.FOGPONICS_SYSTEM,  index ); 
		}catch(java.lang.IndexOutOfBoundsException e){
			showError("System Not Found",false);
			e.printStackTrace();
		}         
        
    	// debug the system values        
        this.system.test();
        
        // refresh the system
    	system.refreshSystemStatus();		
		mode_value = system.getSysteModeValue();		
		valve1_value = system.getInletValveValue(); 		
		valve2_value = system.getOutletValveValue();		
		pump_value = system.getPumpValue();						
		atomizer_value  = system.getAtmoizerValue();		
		int pump_attr_values[] =   system.getPumpTimeValues();                       
		pump_attr__runtime_value = pump_attr_values[0];
		pump_attr_stoptime_value = pump_attr_values[1];
		int atomizer_attr_values[] =  system.getAtmoizerTimeValues();
		atomizer_attr__runtime_value = atomizer_attr_values[0];
		atomizer_attr__stoptime_value = atomizer_attr_values[1];
		refreshUIState();
 		LOG.debug("refreshSystemStatus  " +valve1_value+"  "+valve2_value+"  "+pump_value+"  "+mode_value+"  "+pump_attr__runtime_value+" "+pump_attr_stoptime_value+ "  "+atomizer_attr__runtime_value+"  "+atomizer_attr__stoptime_value);
 		
 		// assign the system status to UI
 		modeSelect.setSelectedItem(mode_value);
 		valveStatus1.setText(valve1_value);
 		valveStatus2.setText(valve2_value);
 		pumpStatus.setText(pump_value);
 		
         runTimeInput.setText(""+pump_attr__runtime_value);
         stopTimeInput.setText(""+pump_attr_stoptime_value);
         fogrunTimeInput.setText(""+atomizer_attr__runtime_value);
         fogstopTimeInput.setText(""+atomizer_attr__stoptime_value);
    	
		// subscribe to water sensor		  		
    	water_level_listener = new WaterLevelAdsListener();
		try {
			system.subscribeToWaterLevel(1,water_level_listener);
		} catch (AdsException e) {
			
			e.printStackTrace();
		} 
    	
    }    
    
    private void initGui() {
        setLayout(null);
        modeUI();
        valveUI();
        pumpUI();
        sensorUI();
        timeUI();
        addWaterUI();
        fogvalveUI();
        fogtimeUI();
        systemListUI();
        setVisible(true);
    }     
    
    private void refreshUIState(){
		if(valve1_value == "ON") openingState(valveStatus1,valveOn1); 
		else if(valve1_value == "OFF") closingState(valveStatus1,valveOn1);
		else if(valve1_value == "ERROR") errorState(valveStatus1,valveOn1); 
		
		if(valve2_value == "ON") openingState(valveStatus2,valveOn2); 
		else if(valve2_value == "OFF") closingState(valveStatus2,valveOn2);
		else if(valve2_value == "ERROR") errorState(valveStatus2,valveOn2);
		
		if(pump_value == "ON") openingState(pumpStatus,pumpOn);  
		else if(pump_value == "OFF") closingState(pumpStatus,pumpOn);
		else if(pump_value == "ERROR") errorState(pumpStatus,pumpOn);
		
		if(atomizer_value == "ON") openingState(atomizerStatus,atomizerOn);  
		else if(atomizer_value == "OFF") closingState(atomizerStatus,atomizerOn);
		else if(atomizer_value == "ERROR") errorState(atomizerStatus,atomizerOn);		
    }
    
    private void closingState(JLabel status_component, JButton control_component){
    	status_component.setText("OFF");
    	control_component.setText("ON");
    	status_component.setBackground(Color.white);    	
    }
    
    private void openingState(JLabel status_component, JButton control_component){
    	status_component.setText("ON");
    	control_component.setText("OFF");
    	status_component.setBackground(Color.green);
    }

    private void errorState(JLabel status_component, JButton control_component){
    	status_component.setText("ON");
    	control_component.setText("OFF");
    	status_component.setBackground(Color.red);
    }    

    private void valveUI() {
        valveName1 = new JLabel("" + FogponicsConstant.Table.VALVE_IN.getDescritpion());
        valveOn1 = new JButton("Valve in ON");
        valveStatus1 =  new JLabel("");
        //valveOn1.setBackground(Color.gray);

        valveName2 = new JLabel("" + FogponicsConstant.Table.VALVE_OUT.getDescritpion());
        valveOn2 = new JButton("Valve out ON");
        valveStatus2 =  new JLabel("");
        //valveOn2.setBackground(Color.gray);

        valveName1.setBounds(40, 100, 300, 30);
        valveOn1.setBounds(40, 130, 150, 30);
        valveStatus1.setBounds(40, 160, 150, 30);
        valveOn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            try {
	            	
		            	if(valve1_value == "ON"){
		            		LOG.debug("CLOSE");
							system.close("actuator.valve",1);
							valve1_value = "OFF";
							closingState(valveStatus1,valveOn1);
							
		            	}else if(valve1_value == "OFF"){
		            		LOG.debug("OPEN");
							system.open("actuator.valve",1);
							valve1_value = "ON";
							openingState(valveStatus1,valveOn1);
							
		            	}else{
		            		showError("System not connected", false);
		            		valve1_value = "ERROR";
		            	}
	            	
					} catch (AdsException e1) {
						showError(e1.getErrMessage(), false);
						e1.printStackTrace();
					} catch (DeviceTypeException e1) {
						e1.printStackTrace();
					} catch(NullPointerException e1){
						showError("System not connected", false);
					}         
            }
        });

        valveName2.setBounds(40, 190, 300, 30);
        valveOn2.setBounds(40, 220, 150, 30);
        valveStatus2.setBounds(40, 250, 150, 30);
        valveOn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

	            try {
	            	
	            	if(valve2_value == "ON"){
						system.close("actuator.valve",1);
						valve2_value = "OFF";
						closingState(valveStatus2,valveOn2);
	            	}else if(valve2_value == "OFF"){
						system.open("actuator.valve",1);
						valve2_value = "ON";
						openingState(valveStatus2,valveOn2);
	            	}else{
	            		showError("System not connected", false);
	            		//valveOn2.setBackground(Color.gray);
	            		valve2_value = "ERROR";
	            	}
	                        
	            } catch (AdsException e1) {
						showError(e1.getErrMessage(), false);
						e1.printStackTrace();
					} catch (DeviceTypeException e1) {
						e1.printStackTrace();
					} catch(NullPointerException e1){
						showError("System not connected", false);
					}                                  
            }
        });

        add(valveName1);
        add(valveOn1);
        add(valveStatus1);

        add(valveName2);
        add(valveOn2);
        add(valveStatus2);
    }

    private void pumpUI() {
        pumpName = new JLabel("" + FogponicsConstant.Table.PUMP.getDescritpion());
        pumpOn = new JButton("Pump ON");
        pumpStatus =  new JLabel("Pump status: ");
        //pumpOn.setBackground(Color.gray);

        pumpOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
	            	if(pump_value == "ON"){
						system.close("actuator.pump",1);
						pump_value = "OFF";
						closingState(pumpStatus,pumpOn);
	            	}else if(pump_value == "OFF"){
						system.open("actuator.pump",1);
						pump_value = "ON";	
						openingState(pumpStatus,pumpOn);
	            	}else{
	            		showError("System not connected", false);
	            		//pumpOn.setBackground(Color.gray);
	            		pump_value = "ERROR";	
	            	}
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {					
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				} 
            }
        });

        pumpName.setBounds(40, 280, 300, 30);
        pumpOn.setBounds(40, 320, 150, 30);
        pumpStatus.setBounds(40, 360, 150, 30);
        add(pumpName);
        add(pumpOn);
        add(pumpStatus);
    }
    
    private void fogvalveUI() {
    	atomizerName = new JLabel("" + HydroponicsConstant.Table.VALVE_IN.getDescritpion());
        atomizerOn = new JButton("Fog Valve ON");
        atomizerStatus = new JLabel("");

        atomizerName.setBounds(500, 100, 300, 30);
        atomizerOn.setBounds(500, 130, 150, 30);
        atomizerStatus.setBounds(500, 150, 150, 30);
        //atomizerOn.setBackground(Color.gray);
        
        atomizerOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(atomizer_value == "ON"){
						system.close("actuator.atomizer",1);
						atomizer_value = "OFF";
						closingState(atomizerStatus,atomizerOn);
	            	}else if(valve2_value == "OFF"){
						system.open("actuator.atomizer",1);
						atomizer_value = "ON";
						openingState(atomizerStatus,atomizerOn);
	            	}else{
	            		showError("System not connected", false);
	            		
	            		//atomizerOn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
        });

        add(atomizerName);
        add(atomizerOn);
    } 
    


    private void sensorUI() {
        sensorValue = new JLabel("Water level:");
        sensorValue.setBounds(160, 30, 200, 30);
        add(sensorValue);
    }

    private void timeUI() {
        // 鑾峰彇鍊肩殑鏂规硶鏄摢涓紵
        runTime = new JLabel("running time value: " + FogponicsConstant.Table.CONFIG_PUMP_RUN_TIME.getNumber());
        stopTime = new JLabel("stop time value: " + FogponicsConstant.Table.CONFIG_PUMP_STOP_TIME.getNumber());
        runTimeInput = new JTextField();
        stopTimeInput = new JTextField();
        runTime.setBounds(100, 550, 300, 30);
        runTimeInput.setBounds(400, 550, 300, 30);
        stopTime.setBounds(100, 580, 300, 30);
        stopTimeInput.setBounds(400, 580, 300, 30);
        runTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runTime.setText("running time value: " + runTimeInput.getText());
                try {
					system.setAttribute("config.attr.atomizer.time.run", Integer.parseInt(runTimeInput.getText()));
				} catch (AdsException e1) {
					e1.printStackTrace();
				}
            }
        });
        stopTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTime.setText("stop time value: " + stopTimeInput.getText());
                try {
					system.setAttribute("config.attr.atomizer.time.stop",Integer.parseInt(stopTimeInput.getText()));
				} catch (AdsException e1) {
					e1.printStackTrace();
				}
            }
        });
        add(runTime);
        add(runTimeInput);
        add(stopTime);
        add(stopTimeInput);
    }

    private void addWaterUI() {
        addWater = new JButton("add water");
        addWater.setBounds(100, 630, 200, 30);
        addExecute = new JLabel("0");
        addExecute.setBounds(100, 660, 100, 30);
        addWater.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 瀵瑰簲绯荤粺鎸囦护
            }
        });
        add(addWater);
        add(addExecute);
    }



    private void fogtimeUI() {
        fogrunTime = new JLabel("fog running time value: " + FogponicsConstant.Table.CONFIG_PUMP_RUN_TIME.getNumber());
        fogstopTime = new JLabel("fog stop time value: " + FogponicsConstant.Table.CONFIG_PUMP_STOP_TIME.getNumber());
        fogrunTimeInput = new JTextField();
        fogstopTimeInput = new JTextField();
        fogrunTime.setBounds(600, 200, 300, 30);
        fogrunTimeInput.setBounds(900, 200, 300, 30);
        fogstopTime.setBounds(600, 230, 300, 30);
        fogstopTimeInput.setBounds(900, 230, 300, 30);
        fogrunTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fogrunTime.setText("fog running time value: " + fogrunTimeInput.getText());
                // TODO: 瀵瑰簲鐨� system 璁剧疆
            }
        });
        fogstopTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fogstopTime.setText("fog stop time value: " + fogstopTimeInput.getText());
                // TODO: 瀵瑰簲鐨� system 璁剧疆
            }
        });
        add(fogrunTime);
        add(fogrunTimeInput);
        add(fogstopTime);
        add(fogstopTimeInput);
    }



    private void modeUI() {
        Vector<String> vs = FogponicsConstant.ModeCommand.getConstantVector();

        modeSelect = new JComboBox(vs);
        modeSelect.setBounds(40,30,120,40);
        // 璁剧疆妯″紡
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                LOG.info("fogponics choose mode  " +mode+"   "+modeSelect.getSelectedIndex());
                setMode(mode);
        
            }
        });
        add(modeSelect);
    }
    
    private void setEnabled(){
   	
    }
    
    private void setDisabled(){
    	
    }

    private void setMode(String mode) {
        if (Objects.equals(mode, FogponicsConstant.ModeCommand.MANUAL.getDescritpion())) {
        	setDisabled();
            try {
				system.configMode("config.system.mode", FogponicsConstant.ModeCommand.MANUAL);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
            
        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.CONFIG.getDescritpion())) {
        	setEnabled();
            try {
				system.configMode("config.system.mode", FogponicsConstant.ModeCommand.CONFIG);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.RUNNING.getDescritpion())) {
        	setEnabled();
            try {
				system.configMode("config.system.mode", FogponicsConstant.ModeCommand.RUNNING);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.CLEANUP.getDescritpion())) {
        	setEnabled();
            try {
				system.configMode("config.system.mode", FogponicsConstant.ModeCommand.CLEANUP);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.FILLIN.getDescritpion())) {
        	setEnabled();
            try {
				system.configMode("config.system.mode", FogponicsConstant.ModeCommand.FILLIN);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        }else if (Objects.equals(mode, FogponicsConstant.ModeCommand.CHECK.getDescritpion())) {
        	setEnabled();
            try {
				system.configMode("config.system.mode", FogponicsConstant.ModeCommand.CHECK);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        }    
    }
    
    private void systemListUI(){
    	if(respository!=null){    		  	
    	
	    	number = respository.getFogponicsSystems().size();
	    	
	        Vector<String> vs = new Vector<String>();
	        for (int i=0;i< number;i++){
	            vs.add(""+i);     	
	        }
	        systemSelect = new JComboBox(vs);
	        systemSelect.setBounds(300,100,100,50); 
	        
	        systemSelect.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String mode = "" + systemSelect.getItemAt(systemSelect.getSelectedIndex());	
	                System.out.println(mode);	                
	                system = (FogponicsSystem) respository.findSystem(SystemStructure.FOGPONICS_SYSTEM,   Integer.parseInt(mode));	                
	                try {
						system.test(system.readByteArray( 22));
					} catch (AdsException e1) {
						showError(e1.getErrMessage(), false);
						e1.printStackTrace();
					}
	                
	            }
	        });
	        add(systemSelect); 
    	}
    }    
    
   
    CallbackListenerAdsState water_level_listener = null;
	 class WaterLevelAdsListener implements CallbackListenerAdsState {
		    private final static long SPAN = 11644473600000L;

		    public void onEvent(AmsAddr addr,AdsNotificationHeader notification,long user) {	                    
		        // The PLC timestamp is coded in Windows FILETIME.
		        long dateInMillis = notification.getNTimeStamp();
		        // Date accepts millisecs since 01.01.1970.
		        Date notificationDate = new Date(dateInMillis / 10000 - SPAN);
		        byte data [] = notification.getData();	        
		        LOG.debug("water Value:\t\t"
		                + Convert.ByteArrToInt(notification.getData())+"    "+notification.getData().length+ "   "+ data[0]+"  "+data[1]+"  "+data[2]+"  "+data[3]);
	     
		        //Convert.ByteArrToDouble(data)  ByteArrToShort  ByteArrToInt  
		        
		        //LOG.debug("Notification:\t" + notification.getHNotification());
		        //LOG.debug("Time:\t\t" + notificationDate.toString());
		        //LOG.debug("User:\t\t" + user);
		        //LOG.debug("ServerNetID:\t" + addr.getNetIdString() + "\n");
		    }
	}    
	 
	 
	 
    
    private void showError(String message, boolean exit) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        if (exit)
            System.exit(0);
        
        
    } 
    
    private void showExit() {
		int option = JOptionPane.showConfirmDialog(null,
				"Do you want to quit the system "
				, "Not found",
				JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {				 
				}
			});
			
			try {
				respository.getADSConnection().closePort();
			} catch (AdsException e) {
				e.printStackTrace();
			}finally{
				System.exit(0);
			}
		    
			
		}else if(option == JOptionPane.CANCEL_OPTION){
			
		} 	
    }
    
    public static void main(String[] args) {
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);    
        
  	    FogponicsSystem_UI UI = new FogponicsSystem_UI();
        
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("FogponicsSystem");
 	    main_frame.setLocationRelativeTo(null);
 	    main_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 	    main_frame.setResizable(true);
 	    main_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                UI.showExit();
            }
        });

 	    main_frame.add(UI); 	    
 	    
		try {
	 	   	ADSConnection ads = new ADSConnection();
	 	   	ads.openPort(true,"5.42.203.215.1.1",851); 
			
			UI.addSystems(new SystemRepository(ads));
		} catch (AdsException e1) {
			e1.printStackTrace();
		}        

 	    
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	main_frame.setVisible(true);
            }
        });        
        
    }    
}