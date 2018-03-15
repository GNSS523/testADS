package com.liveco.gateway.ui;

import com.liveco.gateway.constant.FogponicsConstant;
import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.HydroponicsSystem;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;


public class HydroponicsSystem_UI extends JPanel{

    private static final Logger LOG = LogManager.getLogger(HydroponicsSystem_UI.class);
		
	private HydroponicsSystem system;
    private SystemRepository respository;
    
    //
    private JComboBox modeSelect;
    private JComboBox systemSelect;    
    private int number;
     
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


    String mode_value;   
    String valve1_value;
    String valve2_value;
    String pump_value;
    int pump_attr__runtime_value, pump_attr_stoptime_value;     
    
    public HydroponicsSystem_UI() {
 
        initGui();
    }    
    
    public HydroponicsSystem_UI(HydroponicsSystem system) {
        this.system = system;
 
        initGui();
    }
    
    public HydroponicsSystem_UI(SystemRepository respository) {
        this.respository = respository;
        
        initGui();
        refreshSystemStatus(0);
    } 
    
    public void addSystems(SystemRepository respository){
        this.respository = respository;
        refreshSystemStatus(0);
        systemListUI();        
    }    
    
    private void refreshSystemStatus(int index)  {
    	// select the system
    	try{		
	        system = (HydroponicsSystem) respository.findSystem(SystemStructure.HYDROPONICS,   0);
		}catch(java.lang.IndexOutOfBoundsException e){
			showError("System Not Found",false);
			e.printStackTrace();
		}        
    	// debug the system values 
        this.system.test();
        
        
    	try {
    		
    		// get the system status
            system.refreshSystemStatus();
            
    		mode_value = system.getSysteModeValue();
    		
    		valve1_value = system.getInletValveValue();
    		if(valve1_value == "ON") toClose(valveStatus1,valveOn1); 
    		else if(valve1_value == "OFF") toOpen(valveStatus1,valveOn1);
    		else if(valve1_value == "ERROR") toError(valveStatus1,valveOn1);
    		
    		valve2_value = system.getOutletValveValue();
    		if(valve2_value == "ON") toClose(valveStatus2,valveOn2); 
    		else if(valve2_value == "OFF") toOpen(valveStatus2,valveOn2);
    		else if(valve2_value == "ERROR") toError(valveStatus2,valveOn2);
    		    		
    		pump_value = system.getPumpValue();
    		if(pump_value == "ON") toClose(pumpStatus,pumpOn);  
    		else if(pump_value == "OFF") toOpen(pumpStatus,pumpOn);
    		else if(pump_value == "ERROR") toError(pumpStatus,pumpOn);
            
            int pump_attr_values[] = system.getPumpTimeValues();
            pump_attr__runtime_value = pump_attr_values[0];
            pump_attr_stoptime_value = pump_attr_values[1];    		
    		LOG.info("refreshSystemStatus  " +valve1_value+"  "+valve2_value+"  "+pump_value+"  "+mode_value+"  "+pump_attr__runtime_value+" "+pump_attr_stoptime_value);

    		// assign the system status to UI
     		modeSelect.setSelectedItem(mode_value);
    		valveStatus1.setText(valve1_value);
    		valveStatus2.setText(valve2_value);
    		pumpStatus.setText(pump_value);    		
            runTimeInput.setText(""+pump_attr__runtime_value);
            stopTimeInput.setText(""+pump_attr_stoptime_value);
            	
			// subscribe to water sensor		  		
	    	water_level_listener = new WaterLevelAdsListener();
	    	system.subscribeToWaterLevel(1,water_level_listener);
			
		} catch (AdsException e) {
			
			e.printStackTrace();
		} catch (DeviceTypeException e) {
			
			e.printStackTrace();
		}    	

    }     
    

    private void valveUI() {
        valveName1 = new JLabel("" + HydroponicsConstant.Table.VALVE_IN.getDescritpion());
        valveOn1 = new JButton("Valve 1 ON");
        valveStatus1 =  new JLabel();
        valveOn1.setBackground(Color.gray);

        valveName2 = new JLabel("" + HydroponicsConstant.Table.VALVE_OUT.getDescritpion());
        valveOn2 = new JButton("Valve 2 ON");
        valveStatus2 =  new JLabel();
        valveOn2.setBackground(Color.gray);

        valveName1.setBounds(100, 200, 300, 30);
        valveOn1.setBounds(100, 230, 150, 30);
        valveStatus1.setBounds(100, 260, 150, 30);
        valveOn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            try {
	            	
		            	if(valve1_value == "ON"){
							system.close("actuator.valve",1);
							valve1_value = "OFF";
							 toClose(valveStatus1,valveOn1);
							
		            	}else if(valve1_value == "OFF"){
							system.open("actuator.valve",1);
							valve1_value = "ON";
							toOpen(valveStatus1,valveOn1);
							
		            	}else{
		            		showError("System not connected", false);
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


        valveName2.setBounds(100, 290, 300, 30);
        valveOn2.setBounds(100, 320, 150, 30);
        valveStatus2.setBounds(100, 350, 150, 30);
        

        valveOn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            try {
	            	
	            	if(valve2_value == "ON"){
						system.close("actuator.valve",1);
						valve2_value = "OFF";
						 toClose(valveStatus2,valveOn2);
	            	}else if(valve2_value == "OFF"){
						system.open("actuator.valve",1);
						valve2_value = "ON";
						toOpen(valveStatus2,valveOn2);
	            	}else{
	            		showError("System not connected", false);
	            		valveOn2.setBackground(Color.gray);
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
        pumpName = new JLabel("" + HydroponicsConstant.Table.PUMP.getDescritpion());
        pumpOn = new JButton("Pump ON");
        pumpStatus =  new JLabel("");
        pumpOn.setBackground(Color.gray);

        pumpOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
	            	if(pump_value == "ON"){
						system.close("actuator.pump",1);
						pump_value = "OFF";
						toClose(pumpStatus,pumpOn);

	            	}else if(pump_value == "OFF"){
						system.open("actuator.pump",1);
						pump_value = "ON";	
						toOpen(pumpStatus,pumpOn);

	            	}else{
	            		showError("System not connected", false);
	            		pumpOn.setBackground(Color.gray);
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

        pumpName.setBounds(100, 380, 300, 30);
        pumpOn.setBounds(100, 420, 150, 30);
        pumpStatus.setBounds(100, 460, 150, 30);
        add(pumpName);
        add(pumpOn);
        add(pumpStatus);
    }

    private void toClose(JLabel status_component, JButton control_component){
    	status_component.setText("OFF");
    	control_component.setText("ON");
    	control_component.setBackground(Color.green);    	
    }
    
    private void toOpen(JLabel status_component, JButton control_component){
    	status_component.setText("ON");
    	control_component.setText("OFF");
    	control_component.setBackground(Color.white);    	
    }  
    
    private void toError(JLabel status_component, JButton control_component){
    	status_component.setText("ON");
    	control_component.setText("OFF");
    	control_component.setBackground(Color.red);    	
    }    
    
    private void sensorUI() {
       sensorName = new JLabel("Sensor name: " + HydroponicsConstant.Table.LEVEL_SENSOR.getDescritpion());
       sensorValue = new JLabel("Sensor value: " + HydroponicsConstant.WaterLevelState.H);
       sensorName.setBounds(20, 500, 400, 30);
       sensorValue.setBounds(600, 500, 200, 30);
       add(sensorName);
       add(sensorValue);
    }

    private void timeUI() {
        runTime = new JLabel("running time value: " + HydroponicsConstant.Table.CONFIG_PUMP_RUN_TIME.getNumber());
        stopTime = new JLabel("stop time value: " + HydroponicsConstant.Table.CONFIG_PUMP_STOP_TIME.getNumber());
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
					system.setAttribute("config.attr.pump.time.run", Integer.parseInt(runTime.getText()));
				} catch (AdsException e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             
            }
        });
        stopTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTime.setText("stop time value: " + stopTimeInput.getText());
                try {
					system.setAttribute("config.attr.pump.time.stop",Integer.parseInt(stopTime.getText()));
				} catch (AdsException e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
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
            }
        });
        add(addWater);
        add(addExecute);
    }

    private void initGui() {
        setSize(800, 600);
        setLayout(null);
        modeUI();
        valveUI();
        pumpUI();
        sensorUI();
        timeUI();
        addWaterUI();
        systemListUI();
        setVisible(true);
    }



    private void modeUI() {
        Vector<String> vs = HydroponicsConstant.ModeCommand.getConstantVector();;

        modeSelect = new JComboBox(vs);
        modeSelect.setBounds(100,100,100,50);
        // 璁剧疆妯″紡
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                LOG.info("hydroponics choose mode  " +mode+"   "+modeSelect.getSelectedIndex()+"  "+(String)modeSelect.getSelectedItem());
                try {
					setMode(mode);
				} catch (AdsException e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				}
            }
        });
        add(modeSelect);
    }
    
    private void systemListUI(){
    	if(respository!=null){    		  	
	    	number = respository.getHydroponicsSystems().size();
	    	
	        Vector<String> vs = new Vector<String>();
	        for (int i=0;i< number;i++){
	            vs.add(""+i);     
	            System.out.println("system list "+i);
	        }
	   
	        systemSelect = new JComboBox(vs);
	        systemSelect.setBounds(300,100,100,50); 
	        
	        systemSelect.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String mode = "" + systemSelect.getItemAt(systemSelect.getSelectedIndex());
	
	                LOG.debug(mode);
	                
	                system = (HydroponicsSystem) respository.findSystem(SystemStructure.HYDROPONICS,   Integer.parseInt(mode));
	                try {
						system.test(system.readByteArray( 16));
					} catch (AdsException e1) {
						showError(e1.getErrMessage(), false);
						e1.printStackTrace();
					} catch(NullPointerException e1){
						showError("System not connected", false);
					}
	                
	            }
	        });
	        add(systemSelect);  
    	}
    }

    private void setEnabled(){
       	
    }
    
    private void setDisabled(){
    	
    }    
    
    private void setMode(String mode) throws AdsException {
        if (Objects.equals(mode, HydroponicsConstant.ModeCommand.MANUAL.getDescritpion())) {
        	setDisabled();
			system.configMode("config.system.mode", HydroponicsConstant.ModeCommand.MANUAL);          
        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.CONFIG.getDescritpion())) {
        	setEnabled();
			system.configMode("config.system.mode", HydroponicsConstant.ModeCommand.CONFIG);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.RUNNING.getDescritpion())) {
        	setEnabled();
			system.configMode("config.system.mode", HydroponicsConstant.ModeCommand.RUNNING);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.CLEANUP.getDescritpion())) {
        	setEnabled();
			system.configMode("config.system.mode", HydroponicsConstant.ModeCommand.CLEANUP);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.FILLIN.getDescritpion())) {
        	setEnabled();
			system.configMode("config.system.mode", HydroponicsConstant.ModeCommand.FILLIN);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.CHECK.getDescritpion())) {
        	setEnabled();
			system.configMode("config.system.mode", HydroponicsConstant.ModeCommand.CHECK);
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

 	    HydroponicsSystem_UI UI = new HydroponicsSystem_UI( ); 	   	
 	   	
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("HydroponicsSystem");
 	    main_frame.setLocationRelativeTo(null);
 	    main_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 	    main_frame.setResizable(true);
 	    main_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                UI.showExit();
                //System.exit(1);
            }
        });
 	    

 	    
 	    
		try {
	 	   	ADSConnection ads = new ADSConnection();
	 	   	ads.openPort(true,"5.42.203.215.1.1",851);

	 	    main_frame.add(UI);
			UI.addSystems(new SystemRepository(ads));
		} catch (AdsException e1) {
			// TODO Auto-generated catch block
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
