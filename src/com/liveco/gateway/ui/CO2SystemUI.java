package com.liveco.gateway.ui;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.CO2System;
import com.liveco.gateway.system.SystemRepository;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.tcads.AdsNotificationHeader;
import de.beckhoff.jni.tcads.AmsAddr;
import de.beckhoff.jni.tcads.CallbackListenerAdsState;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class CO2SystemUI extends JPanel{
    /**
	 * 
	 */
    private static final Logger LOG = LogManager.getLogger(CO2System.class);
	
	private static final long serialVersionUID = -3403675278480479931L;
	
	private CO2System system;
    private static SystemRepository respository;

    private int number;
    private JComboBox co2combobox;
        
    private JLabel modeName;
    private JComboBox systemSelect;
    private JComboBox modeSelect;
    
    private JLabel valveName;
    private JLabel valveStatus;
    private JButton valveOn;
    
    private JLabel CO2SensorName;
    private JLabel CO2SensorValue;
    
    private JLabel minText;
    private JLabel maxText;
    private JTextField minValue;
    private JTextField maxValue;

    
    String mode_value;
    String valve_value;
    float CO2_high_value, CO2_low_value;    
    
    public CO2SystemUI() {
        super();

        initGui();
      
    }    
    
    public CO2SystemUI(CO2System system) {
        super();
        this.system = system;

        initGui();
        
    }
    
    public CO2SystemUI(SystemRepository respository) {
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
	        system = (CO2System) respository.findSystem(SystemStructure.CO2_SYSTEM,  index );
		}catch(java.lang.IndexOutOfBoundsException e){
			showError("System Not Found",false);
					
			e.printStackTrace();
		}        
        
    	// debug the system values 
        system.test();
        
        // get the system status
        system.refreshSystemStatus();        
        mode_value = system.getSysteModeValue();
        modeSelect.setSelectedItem(mode_value);
        
        float CO2ThresholdValues[] = system.getCO2ThresholdValues();
        CO2_high_value = CO2ThresholdValues[0];
        CO2_low_value = CO2ThresholdValues[1];
		System.out.println("refreshSystemStatus  " +valve_value+"  "+mode_value+"  "+CO2_high_value+" "+CO2_low_value);

		// assign the system status to UI
		valve_value = system.getOutletValveValue();
		if(valve_value == "ON") toClose(valveStatus,valveOn); 
		else if(valve_value == "OFF") toOpen(valveStatus,valveOn);
		else if(valve_value == "ERROR") toError(valveStatus,valveOn);
		
		minValue.setText(""+CO2_high_value);
		maxValue.setText(""+CO2_low_value);     
		
		// subscribe to CO2 sensor	
		/**/
		CO2_listener = new CO2AdsListener();
		try {
			system.subscribeToCO2(1,CO2_listener);
		} catch (AdsException e) {
			
			e.printStackTrace();
		}
        
    }    
    
	
	 
	 
    private void initGui()  {
    	
 	    setLayout(null); 	   
        modeUI();
        CO2ValveControlUI();
        CO2SensorUI();
        systemListUI();
        
        CO2ValveSettingUI();
    }

    private void modeUI() {
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.MANUAL));
        modeSelect = new JComboBox(vs);
        // 璁剧疆妯″紡
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                
                try {
					setMode(mode);
				} catch (AdsException e1) {
					e1.printStackTrace();
				}
            }
        });
        modeName = new JLabel("Modes:");

        modeName.setBounds(  10, 30, 200, 30);
        modeSelect.setBounds( 80,20, 200, 50);

        add(modeName);
        add(modeSelect);
    }

    private void CO2ValveControlUI() {
        valveName = new JLabel("" + CO2SystemConstant.Table.VALVE.getDescritpion());
        valveOn = new JButton("Valve On");
        valveStatus = new JLabel("");
        
        valveName.setBounds( 10,  60, 200, 50);
        valveOn.setBounds(   100, 70, 100, 25);
        valveStatus.setBounds(100, 150, 150, 30);
        
        valveOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            try {
	            	
		            	if(valve_value == "ON"){
							system.close("actuator.valve",1);
							valve_value = "OFF";
							 toClose(valveStatus,valveOn);
							
		            	}else if(valve_value == "OFF"){
							system.open("actuator.valve",1);
							valve_value = "ON";
							toOpen(valveStatus,valveOn);
							
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

        add(valveName);
        add(valveOn);
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
    
    private void CO2SensorUI() {
        CO2SensorName = new JLabel("" + CO2SystemConstant.Table.CO2.getDescritpion());
        CO2SensorValue = new JLabel("Value: " + String.valueOf(CO2SystemConstant.Table.CO2.getNumber()));
        CO2SensorName.setBounds(10, 110, 300, 30);
        CO2SensorValue.setBounds(100, 110, 300, 30);
        add(CO2SensorName);
        add(CO2SensorValue);
    }

    private void CO2ValveSettingUI() {
        byte minLimit = CO2SystemConstant.Table.CO2_LOWER_LIMIT.getNumber();
        byte maxLimit = CO2SystemConstant.Table.CO2_HIGHER_LIMIT.getNumber();
        // minLimt 鍜� maxLimit 鐨勫�间竴鏍�
        maxLimit += 10;
        minText = new JLabel("min: " + String.valueOf(minLimit));
        maxText = new JLabel("max: " + String.valueOf(maxLimit));
        minValue = new JTextField();
        maxValue = new JTextField();
        minText.setBounds(10, 150, 100, 30);
        minValue.setBounds(100, 150, 100, 30);
        
        maxText.setBounds(250, 150, 100, 30);
        maxValue.setBounds(360, 150, 100, 30);
        minValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minText.setText("min: " + minValue.getText());
                int min = Integer.parseInt( maxValue.getText() );                
           	    try {
					system.setAttribute("config.attr.CO2.threshold.low",  min ) ;
				} catch (AdsException e1) {
					e1.printStackTrace();
				}
            }
        });
        maxValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxText.setText("max: " + maxValue.getText());
                int max = Integer.parseInt( maxValue.getText() );
           	    try {
					system.setAttribute("config.attr.CO2.threshold.high",  max ) ;
				} catch (AdsException e1) {
					e1.printStackTrace();
				}     
            }
        });
        add(minText);
        add(maxText);
        add(minValue);
        add(maxValue);
    }

    private void setMode(String m) throws AdsException {
    	
    	if(system.isConnected()){
            if (Objects.equals(m, CO2SystemConstant.ModeCommand.AUTOMATIC.getDescritpion())) {
                valveOn.setEnabled(false);
                minValue.setEnabled(false);
                maxValue.setEnabled(false);
                system.configMode("config.system.mode", CO2SystemConstant.ModeCommand.AUTOMATIC);
                
            } else if (Objects.equals(m, CO2SystemConstant.ModeCommand.MANUAL.getDescritpion())) {
                valveOn.setEnabled(true);
                minValue.setEnabled(true);
                maxValue.setEnabled(true);
                
                system.configMode("config.system.mode", CO2SystemConstant.ModeCommand.MANUAL);

            }    		
    	}else{
    		
    		
    		
    	}
    }

    private void systemListUI(){
    	if(respository!=null){    		  	

	    	number = respository.getCO2Systems().size();
	    	
	        Vector<String> vs = new Vector<String>();
	        for (int i=0;i< number;i++){
	            vs.add(""+i);
	        }
	        
	        systemSelect = new JComboBox(vs);
	        systemSelect.setBounds(10,10,100,30);
	                     
	        systemSelect.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	int index = systemSelect.getSelectedIndex();
	                String name = "" + systemSelect.getItemAt(index);
	                
	                System.out.println( name +"  "+ index  );
	                
	                refreshSystemStatus(index);
	                
	            }
	        });
	        add(systemSelect); 
    	}
    }  
    
     CallbackListenerAdsState CO2_listener = null;
	 class CO2AdsListener implements CallbackListenerAdsState {
		    private final static long SPAN = 11644473600000L;

		    public void onEvent(AmsAddr addr,AdsNotificationHeader notification,long user) {	                    

		        // The PLC timestamp is coded in Windows FILETIME.
		        long dateInMillis = notification.getNTimeStamp();
		        // Date accepts millisecs since 01.01.1970.
		        Date notificationDate = new Date(dateInMillis / 10000 - SPAN);
		        byte data [] = notification.getData();	       
		        LOG.debug("co2 Value:\t\t"
		                + Convert.ByteArrToFloat(notification.getData())+"    "+notification.getData().length+ "   "+ data[0]+"  "+data[1]+"  "+data[2]+"  "+data[3]);
	     
		        //Convert.ByteArrToDouble(data)  ByteArrToShort  ByteArrToInt  
		        
		        //System.out.println("Notification:\t" + notification.getHNotification());
		        //System.out.println("Time:\t\t" + notificationDate.toString());
		        //System.out.println("User:\t\t" + user);
		        //System.out.println("ServerNetID:\t" + addr.getNetIdString() + "\n");
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
        // write your code here
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG); 
 		
	
 		 	   	
 	    CO2SystemUI UI = new CO2SystemUI();

 	   	
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("CO2 system");
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
 	    
 	    SystemRepository repository = null;
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
