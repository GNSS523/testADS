package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import com.liveco.gateway.constant.FogponicsConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.FogponicsSystem;
import com.liveco.gateway.system.SystemRepository;
import com.liveco.gateway.ui.FogponicsSystem_UI.WaterLevelAdsListener;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.tcads.AdsNotificationHeader;
import de.beckhoff.jni.tcads.AmsAddr;
import de.beckhoff.jni.tcads.CallbackListenerAdsState;

public class FogponicsDialog extends JDialog {
	
    private static final Logger LOG = LogManager.getLogger(FogponicsDialog.class);
	
	private FogponicsSystem system;
    private SystemRepository respository;
    int system_index;
    
    private JComboBox modeComboBox;
    
	private final JPanel contentPanel = new JPanel();

	private JPanel inlet_valve_panel;
	
	private JButton outlet_valve_btn, inlet_valve_btn, pump_btn, atmozier_btn;
	private JLabel outlet_valve_status, inlet_valve_status, pump_status, atomizer_status, water_level_status,location_label;
	
	private JTextField atomizer_run_input;
	private JTextField atomizer_stop_input;
	private JTextField pump_run_input;
	private JTextField pump_stop_input;

	int pump_attr__runtime_value, pump_attr_stoptime_value;
	int atomizer_attr__runtime_value, atomizer_attr__stoptime_value;
	
    String mode_value;    
	String valve1_value;
	String valve2_value;
	String pump_value;
	String atomizer_value;
	String water_level_value;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);    		
		
		try {
	 	   	ADSConnection ads = new ADSConnection();
	 	   	ads.openPort(true,"5.42.203.215.1.1",851); 			
			
			FogponicsDialog dialog = new FogponicsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true); 
			
			dialog.addSystems(new SystemRepository(ads));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	private void initGUI(){
		setBounds(100, 100, 490, 474);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("windows closing ");
                unsubscribeWaterLevelSensor();
            }
        });		
		
		
		JPanel manual_control_panel = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.WEST, manual_control_panel, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, manual_control_panel, -15, SpringLayout.EAST, contentPanel);
		manual_control_panel.setBorder(new TitledBorder(null, "Manual Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(manual_control_panel);
		manual_control_panel.setLayout(null);
		{
			inlet_valve_panel = new JPanel();
			inlet_valve_panel.setBounds(40, 22, 155, 33);
			manual_control_panel.add(inlet_valve_panel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, inlet_valve_panel, 20, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, inlet_valve_panel, 209, SpringLayout.WEST, contentPanel);
			inlet_valve_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			JLabel lblNewLabel_2 = new JLabel("Inlet Valve:");
			inlet_valve_panel.add(lblNewLabel_2);
			
			inlet_valve_btn = new JButton("ON");
			inlet_valve_btn.setVerticalAlignment(SwingConstants.TOP);
			inlet_valve_panel.add(inlet_valve_btn);
			
			inlet_valve_status = new JLabel("OFF");
			inlet_valve_panel.add(inlet_valve_status);			
			inlet_valve_btn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
		            try {
		            	
			            	if(valve1_value == "ON"){
			            		LOG.debug("CLOSE");
								system.close("actuator.valve",1);
								valve1_value = "OFF";
								closingState(inlet_valve_status,inlet_valve_btn);
								
			            	}else if(valve1_value == "OFF"){
			            		LOG.debug("OPEN");
								system.open("actuator.valve",1);
								valve1_value = "ON";
								openingState(inlet_valve_status,inlet_valve_btn);
								
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
		}
		
		JPanel out_valve_panel = new JPanel();
		out_valve_panel.setBounds(200, 22, 175, 33);
		manual_control_panel.add(out_valve_panel);
		out_valve_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblInletValve = new JLabel("Outlet Valve:");
		out_valve_panel.add(lblInletValve);
		
		outlet_valve_btn = new JButton("ON");
		outlet_valve_btn.setVerticalAlignment(SwingConstants.TOP);
		out_valve_panel.add(outlet_valve_btn);
		
		outlet_valve_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

	            try {
	            	
	            	if(valve2_value == "ON"){
						system.close("actuator.valve",1);
						valve2_value = "OFF";
						closingState(outlet_valve_status,outlet_valve_btn);
	            	}else if(valve2_value == "OFF"){
						system.open("actuator.valve",1);
						valve2_value = "ON";
						openingState(outlet_valve_status,outlet_valve_btn);
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
	
		
		
		outlet_valve_status = new JLabel("OFF");
		out_valve_panel.add(outlet_valve_status);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, out_valve_panel, 0, SpringLayout.NORTH, inlet_valve_panel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, out_valve_panel, 14, SpringLayout.EAST, inlet_valve_panel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, out_valve_panel, 193, SpringLayout.EAST, inlet_valve_panel);
		
		JPanel outlet_valve_panel = new JPanel();
		outlet_valve_panel.setBounds(40, 60, 155, 33);
		manual_control_panel.add(outlet_valve_panel);
		outlet_valve_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblPump = new JLabel("Pump:");
		outlet_valve_panel.add(lblPump);
		
		pump_btn = new JButton("ON");
		pump_btn.setVerticalAlignment(SwingConstants.TOP);
		outlet_valve_panel.add(pump_btn);
		
		pump_status = new JLabel("OFF");
		outlet_valve_panel.add(pump_status);
		
		pump_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
	            	if(pump_value == "ON"){
						system.close("actuator.pump",1);
						pump_value = "OFF";
						closingState(pump_status,pump_btn);
	            	}else if(pump_value == "OFF"){
						system.open("actuator.pump",1);
						pump_value = "ON";	
						openingState(pump_status,pump_btn);
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
		
		
		
		JPanel panel_1 = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel_1, -344, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, panel_1, 5, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, panel_1, -275, SpringLayout.EAST, contentPanel);
		panel_1.setBorder(null);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Mode:");
		lblNewLabel.setBounds(6, 10, 42, 15);
		panel_1.add(lblNewLabel);
		{
			
	        Vector<String> vs = FogponicsConstant.ModeCommand.getConstantVector();
			
			
			modeComboBox = new JComboBox(vs);
			modeComboBox.setBounds(51, 7, 118, 21);
			
			modeComboBox.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String mode = "" + modeComboBox.getItemAt(modeComboBox.getSelectedIndex());
	                LOG.info("fogponics choose mode  " +mode+"   "+modeComboBox.getSelectedIndex());
	                //setMode(mode);
	        
	            }
	        });			
			
			panel_1.add(modeComboBox);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, modeComboBox, 7, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, modeComboBox, 46, SpringLayout.WEST, contentPanel);
		}
		
		JPanel panel_6 = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, manual_control_panel, -6, SpringLayout.NORTH, panel_6);
		sl_contentPanel.putConstraint(SpringLayout.WEST, panel_6, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, panel_6, -15, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, panel_6, 194, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel_6, 0, SpringLayout.SOUTH, contentPanel);
		panel_6.setBorder(new TitledBorder(null, "Attributes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_7 = new JPanel();
		panel_7.setBounds(200, 60, 175, 33);
		manual_control_panel.add(panel_7);
		panel_7.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblAtomizierValve = new JLabel("Atomizier:");
		panel_7.add(lblAtomizierValve);
		
	   atmozier_btn = new JButton("ON");
		atmozier_btn.setVerticalAlignment(SwingConstants.TOP);
		panel_7.add(atmozier_btn);
		
		atmozier_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	System.out.println("click  "+atomizer_value);
            	
                try {
	            	if(atomizer_value == "ON"){
						system.close("actuator.atomizer",1);
						atomizer_value = "OFF";
						closingState(atomizer_status,atmozier_btn);
	            	}else if(atomizer_value == "OFF"){
	                	System.out.println("click  fuck 1");

						system.open("actuator.atomizer",1);
						atomizer_value = "ON";	
						openingState(atomizer_status,atmozier_btn);
	            	}else{
	            		System.out.println("click  fuck error");
	            		showError("System not connected", false);
	            		//pumpOn.setBackground(Color.gray);
	            		atomizer_value = "ERROR";	
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
		
		
		 atomizer_status = new JLabel("OFF");
		panel_7.add(atomizer_status);
		contentPanel.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("Pump Run Peroid");
		lblNewLabel_4.setBounds(21, 25, 114, 15);
		panel_6.add(lblNewLabel_4);
		
		pump_run_input = new JTextField();
		pump_run_input.setBounds(157, 22, 215, 22);
		panel_6.add(pump_run_input);
		pump_run_input.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Pump Stop Peroid");
		lblNewLabel_5.setBounds(21, 54, 114, 15);
		panel_6.add(lblNewLabel_5);
		
		pump_stop_input = new JTextField();
		pump_stop_input.setBounds(157, 51, 215, 21);
		panel_6.add(pump_stop_input);
		pump_stop_input.setColumns(10);
		
		JLabel lblAtomizerRunTime = new JLabel("Atomizer Run Peroid");
		lblAtomizerRunTime.setBounds(21, 101, 136, 15);
		panel_6.add(lblAtomizerRunTime);
		
		atomizer_run_input = new JTextField();
		atomizer_run_input.setBounds(157, 98, 215, 21);
		atomizer_run_input.setColumns(10);
		panel_6.add(atomizer_run_input);
		
		JLabel lblAtomizerStopTime = new JLabel("Atomizer Stop Peroid");
		lblAtomizerStopTime.setBounds(21, 126, 136, 15);
		panel_6.add(lblAtomizerStopTime);
		
		atomizer_stop_input = new JTextField();
		atomizer_stop_input.setBounds(157, 123, 215, 21);
		atomizer_stop_input.setColumns(10);
		panel_6.add(atomizer_stop_input);
		{
			JButton updateAttribute = new JButton("OK");
			updateAttribute.setBounds(178, 165, 215, 23);
			panel_6.add(updateAttribute);
			updateAttribute.setActionCommand("OK");
			getRootPane().setDefaultButton(updateAttribute);
		}
		
		JLabel lblMin = new JLabel("min");
		lblMin.setBounds(375, 25, 31, 15);
		panel_6.add(lblMin);
		
		JLabel label = new JLabel("min");
		label.setBounds(375, 54, 31, 15);
		panel_6.add(label);
		
		JLabel label_1 = new JLabel("min");
		label_1.setBounds(375, 101, 31, 15);
		panel_6.add(label_1);
		
		JLabel label_2 = new JLabel("min");
		label_2.setBounds(375, 126, 31, 15);
		panel_6.add(label_2);
		
		JPanel panel_5 = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, manual_control_panel, 1, SpringLayout.SOUTH, panel_5);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, panel_5, 47, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, panel_5, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, panel_5, -15, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel_5, -302, SpringLayout.SOUTH, contentPanel);
		contentPanel.add(panel_5);
		panel_5.setBorder(new TitledBorder(null, "Water level", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setLayout(null);
		
		water_level_status = new JLabel("");
		water_level_status.setBounds(88, 14, 33, 15);
		panel_5.add(water_level_status);
		
		JButton fillWaterBtn = new JButton("Refill water");
		fillWaterBtn.setBounds(265, 10, 106, 23);
		panel_5.add(fillWaterBtn);
		
		JLabel lblLocation = new JLabel("Location:");
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblLocation, 33, SpringLayout.EAST, panel_1);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblLocation, -16, SpringLayout.NORTH, panel_5);
		contentPanel.add(lblLocation);
		
		location_label = new JLabel("");
		sl_contentPanel.putConstraint(SpringLayout.WEST, location_label, 3, SpringLayout.EAST, lblLocation);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, location_label, 0, SpringLayout.SOUTH, lblLocation);
		contentPanel.add(location_label);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.setActionCommand("Close");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		               
		            	 dispose();
		            }
		        });	
				
			}
		}	
		
        // Size dialog to components.
       // pack();
	}
	
	public FogponicsDialog() {
		
		initGUI();
		
	}
	
    public FogponicsDialog(SystemRepository respository) {
        initGUI();
        this.respository = respository;
        refreshSystemStatus(0);
    }	
	
    public void addSystems(SystemRepository respository){
        this.respository = respository;
        refreshSystemStatus(0);             
    } 

    public void addSystems(SystemRepository respository, int index){
        this.respository = respository;
        this.system_index = index;
        refreshSystemStatus(index);             
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
 		LOG.debug("refreshSystemStatus  " +valve1_value+"  "+valve2_value+"  "+pump_value+"  "+atomizer_value +"  "+mode_value+"  "+pump_attr__runtime_value+" "+pump_attr_stoptime_value+ "  "+atomizer_attr__runtime_value+"  "+atomizer_attr__stoptime_value);
 		
 		// assign the system status to UI

    	
		// subscribe to water sensor		  		
 		subscribeWaterLevelSensor();
    	
    } 	
	
    private void setMode(String mode) throws AdsException {
        if (Objects.equals(mode, FogponicsConstant.ModeCommand.MANUAL.getDescritpion())) {
        	setDisabled();
			system.setMode(FogponicsConstant.ModeCommand.CONFIG);
          
        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.CONFIG.getDescritpion())) {
        	setEnabled();
			system.setMode( FogponicsConstant.ModeCommand.CONFIG);

        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.RUNNING.getDescritpion())) {
        	setEnabled();
			system.setMode( FogponicsConstant.ModeCommand.RUNNING);

        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.CLEANUP.getDescritpion())) {
        	setEnabled();
			system.setMode( FogponicsConstant.ModeCommand.CLEANUP);

        } else if (Objects.equals(mode, FogponicsConstant.ModeCommand.FILLIN.getDescritpion())) {
        	setEnabled();
			system.setMode( FogponicsConstant.ModeCommand.FILLIN);

        }else if (Objects.equals(mode, FogponicsConstant.ModeCommand.CHECK.getDescritpion())) {

			system.setMode( FogponicsConstant.ModeCommand.CHECK);

        }    
    }	
	
    private void refreshUIState(){
    	
		if(valve1_value == "ON") openingState(inlet_valve_status,inlet_valve_btn); 
		else if(valve1_value == "OFF") closingState(inlet_valve_status,inlet_valve_btn);
		else if(valve1_value == "ERROR") errorState(inlet_valve_status,inlet_valve_btn); 
		
		if(valve2_value == "ON") openingState(outlet_valve_status,outlet_valve_btn); 
		else if(valve2_value == "OFF") closingState(outlet_valve_status,outlet_valve_btn);
		else if(valve2_value == "ERROR") errorState(outlet_valve_status,outlet_valve_btn);
		
		if(pump_value == "ON") openingState(pump_status,pump_btn);  
		else if(pump_value == "OFF") closingState(pump_status,pump_btn);
		else if(pump_value == "ERROR") errorState(pump_status,pump_btn);
		
		if(atomizer_value == "ON") openingState(atomizer_status,atmozier_btn);  
		else if(atomizer_value == "OFF") closingState(atomizer_status,atmozier_btn);
		else if(atomizer_value == "ERROR") errorState(atomizer_status,atmozier_btn);
		
 		modeComboBox.setSelectedItem(mode_value);
 		location_label.setText(system.getSystemId());

 		//inlet_valve_status.setText(valve1_value);
 		//outlet_valve_status.setText(valve2_value);
 		//pump_status.setText(pump_value);
 		
 		pump_run_input.setText(""+pump_attr__runtime_value);
 		pump_stop_input.setText(""+pump_attr_stoptime_value);
 		atomizer_run_input.setText(""+atomizer_attr__runtime_value);
 		atomizer_stop_input.setText(""+atomizer_attr__stoptime_value);		
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

    private void setEnabled(){
       	
    }
    
    private void setDisabled(){
    	
    }
    
    private void showError(String message, boolean exit) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        if (exit)
            System.exit(0);        
    } 

    
	   private void unsubscribeWaterLevelSensor(){
	    	water_level_listener = new WaterLevelAdsListener();
			try {
				system.unsubscribeToWaterLevel(1,water_level_listener);
				water_level_listener = null;
			} catch (AdsException e) {			
				e.printStackTrace();
			}		   
	   }   
	   
	   private void subscribeWaterLevelSensor(){
	    	water_level_listener = new WaterLevelAdsListener();
			try {
				system.subscribeToWaterLevel(1,water_level_listener);
			} catch (AdsException e) {
				
				e.printStackTrace();
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
	     
		        byte level = data[0];
		         water_level_value = FogponicsConstant.WaterLevelState.getName(level);
		        LOG.debug("water level:\t\t  "+level+ "    "+water_level_value);
		        
		        water_level_status.setText(water_level_value);
		        
		        //Convert.ByteArrToDouble(data)  ByteArrToShort  ByteArrToInt  
		        
		        //LOG.debug("Notification:\t" + notification.getHNotification());
		        //LOG.debug("Time:\t\t" + notificationDate.toString());
		        //LOG.debug("User:\t\t" + user);
		        //LOG.debug("ServerNetID:\t" + addr.getNetIdString() + "\n");
		    }
	}  
	 
	
		public String getAtmoizerValue(){
			return atomizer_value;
		}
			
		public int [] getAtmoizerTimeValues(){
			return new int[]{atomizer_attr__runtime_value,atomizer_attr__stoptime_value};
		}	 
}
