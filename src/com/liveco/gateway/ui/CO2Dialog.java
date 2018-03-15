package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


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

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class CO2Dialog extends JDialog {
    private static final Logger LOG = LogManager.getLogger(CO2Dialog.class);

	private CO2System system;
    private SystemRepository respository;
    private int index;
	
	private final JPanel contentPanel = new JPanel();
	JPanel manual_control_panel, attribute_panel;
	
	private JPanel valve_control_panel;
	private JComboBox modeSelect;
	private JButton valve_control_btn, updateButton;
	private JLabel valve_control_status, location_label;
	private JLabel CO2_value;	
	private JTextField CO2_min_input;
	private JTextField CO2_max_input;
	
    String mode_value;
    String valve_value;
    float CO2_high_value, CO2_low_value, real_CO2_value;  
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG); 		
		try {
	 	   	ADSConnection ads = new ADSConnection();
	 	   	ads.openPort(true,"5.42.203.215.1.1",851);
			CO2Dialog dialog = new CO2Dialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.addSystems(new SystemRepository(ads),2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initGUI(){
		setTitle("CO2 System");
		setBounds(100, 100, 450, 393);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		 this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("windows closing ");
                unsubscribeCO2Sensor();
            }
        });		
		
		
		 manual_control_panel = new JPanel();
		 sl_contentPanel.putConstraint(SpringLayout.WEST, manual_control_panel, 10, SpringLayout.WEST, contentPanel);
		 sl_contentPanel.putConstraint(SpringLayout.EAST, manual_control_panel, -15, SpringLayout.EAST, contentPanel);
		manual_control_panel.setBorder(new TitledBorder(null, "Manual Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(manual_control_panel);
		manual_control_panel.setLayout(null);
		{
			valve_control_panel = new JPanel();
			valve_control_panel.setBounds(30, 27, 148, 33);
			manual_control_panel.add(valve_control_panel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, valve_control_panel, 20, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, valve_control_panel, 209, SpringLayout.WEST, contentPanel);
			valve_control_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			JLabel lblNewLabel_2 = new JLabel("Valve:");
			valve_control_panel.add(lblNewLabel_2);
			
			 valve_control_btn = new JButton("ON");
			valve_control_btn.setVerticalAlignment(SwingConstants.TOP);
			valve_control_panel.add(valve_control_btn);
			
			 valve_control_status = new JLabel("OFF");
			valve_control_panel.add(valve_control_status);
			
			
			valve_control_btn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
		            try {
		            	
			            	if(valve_value == "ON"){
								system.close("actuator.valve",1);
								valve_value = "OFF";
								closingState(valve_control_status,valve_control_btn);
								
			            	}else if(valve_value == "OFF"){
								system.open("actuator.valve",1);
								valve_value = "ON";
								openingState(valve_control_status,valve_control_btn);
								
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
		}
		
		JPanel panel_1 = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, panel_1, -235, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, manual_control_panel, 6, SpringLayout.SOUTH, panel_1);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel_1, -264, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, panel_1, 5, SpringLayout.NORTH, contentPanel);
		panel_1.setBorder(null);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		
		
		JLabel lblNewLabel = new JLabel("Mode:");
		lblNewLabel.setBounds(6, 10, 47, 15);
		panel_1.add(lblNewLabel);
		{
			
	        Vector<String> vs = new Vector<String>();
	        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.AUTOMATIC));
	        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.MANUAL));			
	        modeSelect = new JComboBox(vs);
	        modeSelect.setBounds(49, 7, 125, 21);
	        
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
	        
			panel_1.add(modeSelect);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, modeSelect, 7, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, modeSelect, 46, SpringLayout.WEST, contentPanel);
		}
		
		JPanel panel_5 = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel_5, 0, SpringLayout.SOUTH, panel_1);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, panel_5, 5, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, panel_5, 200, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, panel_5, -15, SpringLayout.EAST, contentPanel);
		contentPanel.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(10, 10, 70, 15);
		panel_5.add(lblLocation);
		
		location_label = new JLabel("");
		location_label.setBounds(90, 10, 55, 15);
		panel_5.add(location_label);
		
		 attribute_panel = new JPanel();
		 sl_contentPanel.putConstraint(SpringLayout.SOUTH, manual_control_panel, -6, SpringLayout.NORTH, attribute_panel);
		 sl_contentPanel.putConstraint(SpringLayout.NORTH, attribute_panel, 140, SpringLayout.NORTH, contentPanel);
		 sl_contentPanel.putConstraint(SpringLayout.WEST, attribute_panel, 0, SpringLayout.WEST, manual_control_panel);
		 sl_contentPanel.putConstraint(SpringLayout.SOUTH, attribute_panel, 0, SpringLayout.SOUTH, contentPanel);
		 sl_contentPanel.putConstraint(SpringLayout.EAST, attribute_panel, -15, SpringLayout.EAST, contentPanel);
		attribute_panel.setBorder(new TitledBorder(null, "Attributes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(attribute_panel);
		attribute_panel.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("CO2 Min");
		lblNewLabel_4.setBounds(38, 80, 59, 15);
		attribute_panel.add(lblNewLabel_4);
		
		CO2_min_input = new JTextField();
		CO2_min_input.setBounds(124, 77, 150, 22);
		attribute_panel.add(CO2_min_input);
		CO2_min_input.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("CO2 Max");
		lblNewLabel_5.setBounds(38, 116, 59, 15);
		attribute_panel.add(lblNewLabel_5);
		
		CO2_max_input = new JTextField();
		CO2_max_input.setBounds(124, 113, 150, 21);
		attribute_panel.add(CO2_max_input);
		CO2_max_input.setColumns(10);
		{
			updateButton = new JButton("Update");
			updateButton.setBounds(240, 138, 125, 23);
			attribute_panel.add(updateButton);
			updateButton.setActionCommand("OK");
			getRootPane().setDefaultButton(updateButton);
		}
		
		JLabel lblNewLabel_6 = new JLabel("CO2:");
		lblNewLabel_6.setBounds(38, 29, 39, 15);
		attribute_panel.add(lblNewLabel_6);
		
		CO2_value = new JLabel("");
		CO2_value.setBounds(122, 29, 86, 15);
		attribute_panel.add(CO2_value);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 61, 379, 9);
		attribute_panel.add(separator);
		
		lblPpm = new JLabel("ppm");
		lblPpm.setBounds(203, 29, 39, 15);
		attribute_panel.add(lblPpm);
		
		label = new JLabel("ppm");
		label.setBounds(297, 80, 39, 15);
		attribute_panel.add(label);
		
		label_1 = new JLabel("ppm");
		label_1.setBounds(297, 116, 39, 15);
		attribute_panel.add(label_1);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("OK");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						unsubscribeCO2Sensor();
		            	dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		
        // Size dialog to components.
        //pack();
		
	}
	/**
	 * Create the dialog.
	 */
	public CO2Dialog() {
		initGUI();
	}

	public CO2Dialog(SystemRepository respository,int index) {
		initGUI();
        this.index = index;
		refreshSystemStatus(0); 		
	}
	
    public void addSystems(SystemRepository respository,int index){
        this.respository = respository;
        this.index = index;
        refreshSystemStatus(index);
    }
    
	public CO2Dialog(Frame parent) {
		super(parent, true);
	    		
		initGUI();
	}

	public CO2Dialog(Frame parent,SystemRepository respository,int index) {
		super(parent, true);
	     
		initGUI();
        this.index = index;
		refreshSystemStatus(0); 		
	}
	
    public void addSystems(Frame parent,SystemRepository respository,int index){

    	this.respository = respository;
        this.index = index;
        refreshSystemStatus(index);
    }    
    
    
    
    private void refreshSystemStatus(int index){
    	
    	// select the system
    	try{					
	        system = (CO2System) respository.findSystem(SystemStructure.CO2_SYSTEM,  index );
	        System.out.println("------------------------------------------------"+SystemStructure.CO2_SYSTEM);
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
        
        location_label.setText(system.getSystemId());
        
        
        float CO2ThresholdValues[] = system.getCO2ThresholdValues();
        CO2_high_value = CO2ThresholdValues[0];
        CO2_low_value = CO2ThresholdValues[1];

		// assign the system status to UI
		valve_value = system.getOutletValveValue();
		if(valve_value == "ON") openingState(valve_control_status,valve_control_btn); 
		else if(valve_value == "OFF") closingState(valve_control_status,valve_control_btn);
		else if(valve_value == "ERROR") errorState(valve_control_status,valve_control_btn);
				
		CO2_min_input.setText(""+CO2_high_value);
		CO2_max_input.setText(""+CO2_low_value);  
		
		refreshUIState();
		
		System.out.println("refreshSystemStatus  " +valve_value+"  "+mode_value+"  "+CO2_high_value+" "+CO2_low_value);
		
		// subscribe to CO2 sensor	
		/**/
		subscribeCO2Sensor();
     
    }    

    private void setMode(String m) throws AdsException {
    	
    	if(system.isConnected()){
            if (Objects.equals(m, CO2SystemConstant.ModeCommand.AUTOMATIC.getDescritpion())) {
            	valve_control_btn.setEnabled(false);
                system.configMode("config.system.mode", CO2SystemConstant.ModeCommand.AUTOMATIC);
                automaticState();
            } else if (Objects.equals(m, CO2SystemConstant.ModeCommand.MANUAL.getDescritpion())) {
            	valve_control_btn.setEnabled(true);               
                system.configMode("config.system.mode", CO2SystemConstant.ModeCommand.MANUAL);
                manualState();
            }    		
    	}else{
    				
    	}
    }    
    private void manualState(){
    	manual_control_panel.setEnabled(true);
    	attribute_panel.setEnabled(true);
    	updateButton.setEnabled(true);
    	valve_control_btn.setEnabled(true);    
    }
    
    private void automaticState(){
    	manual_control_panel.setEnabled(false);
    	attribute_panel.setEnabled(false);
    	updateButton.setEnabled(false);
    	valve_control_btn.setEnabled(false);
    }
    
    private void refreshUIState(){
		if(valve_value == "ON") openingState(valve_control_status,valve_control_btn); 
		else if(valve_value == "OFF") closingState(valve_control_status,valve_control_btn);
		else if(valve_value == "ERROR") errorState(valve_control_status,valve_control_btn); 	
		
        if (Objects.equals(mode_value, CO2SystemConstant.ModeCommand.AUTOMATIC.getDescritpion())) {
            automaticState();
        } else if (Objects.equals(mode_value, CO2SystemConstant.ModeCommand.MANUAL.getDescritpion())) {
            manualState();
        } 
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
    
    protected void subscribeCO2Sensor(){
		CO2_listener = new CO2AdsListener();
		try {
			system.subscribeToCO2(1,CO2_listener);
		} catch (AdsException e) {		
			e.printStackTrace();
		}    
    }
    
    protected void unsubscribeCO2Sensor(){
		try {
			system.unsubscribeToCO2(1, CO2_listener);
			CO2_listener = null;
		} catch (AdsException e) {			
			e.printStackTrace();
		}    	
    }
    
    CallbackListenerAdsState CO2_listener = null;
    private JLabel lblPpm;
    private JLabel label;
    private JLabel label_1;
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
	     
		        real_CO2_value = Convert.ByteArrToFloat(notification.getData());
		        CO2_value.setText(  ""+ real_CO2_value);
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
}
