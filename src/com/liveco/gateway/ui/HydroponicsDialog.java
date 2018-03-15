package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.liveco.gateway.constant.FogponicsConstant;
import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.FogponicsSystem;
import com.liveco.gateway.system.HydroponicsSystem;
import com.liveco.gateway.system.SystemRepository;
import com.liveco.gateway.ui.FogponicsDialog.WaterLevelAdsListener;

import de.beckhoff.jni.Convert;
import de.beckhoff.jni.tcads.AdsNotificationHeader;
import de.beckhoff.jni.tcads.AmsAddr;
import de.beckhoff.jni.tcads.CallbackListenerAdsState;

import javax.swing.UIManager;
import javax.swing.JTextField;

public class HydroponicsDialog extends JDialog {
    private static final Logger LOG = LogManager.getLogger(HydroponicsDialog.class);
	private HydroponicsSystem system;
    private SystemRepository respository;
    int system_index;
    
	private final JPanel contentPanel = new JPanel();
	/**
	 * @wbp.nonvisual location=167,17
	 */
	private JPanel inlet_valve_panel;
	private JComboBox modeSelect;


	
	
	private JButton outlet_valve_btn, inlet_valve_btn, pump_btn, atomizer_btn;
	private JLabel outlet_valve_status, inlet_valve_status, pump_status, atomizer_status,location_label, water_level_status;
	
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
	 	   	
			HydroponicsDialog dialog = new HydroponicsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			
			dialog.addSystems(new SystemRepository(ads));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initGUI(){
		setBounds(100, 100, 482, 445);
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
		sl_contentPanel.putConstraint(SpringLayout.NORTH, manual_control_panel, 111, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, manual_control_panel, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, manual_control_panel, -15, SpringLayout.EAST, contentPanel);
		manual_control_panel.setBorder(new TitledBorder(null, "Manual Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(manual_control_panel);
		manual_control_panel.setLayout(null);
		{
			inlet_valve_panel = new JPanel();
			inlet_valve_panel.setBounds(28, 22, 182, 33);
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
		out_valve_panel.setBounds(222, 22, 175, 33);
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
		
		JPanel outlet_valve_panel= new JPanel();
		outlet_valve_panel.setBounds(28, 62, 187, 33);
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
		
		
		
		JPanel mode_panel = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.WEST, mode_panel, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, mode_panel, -319, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, mode_panel, 5, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, mode_panel, -276, SpringLayout.EAST, contentPanel);
		mode_panel.setBorder(null);
		contentPanel.add(mode_panel);
		mode_panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Mode:");
		lblNewLabel.setBounds(6, 10, 57, 15);
		mode_panel.add(lblNewLabel);
		{
	        Vector<String> vs = HydroponicsConstant.ModeCommand.getConstantVector();		
			modeSelect = new JComboBox(vs);
			modeSelect.setBounds(63, 7, 97, 21);
			
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
			
			mode_panel.add(modeSelect);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, modeSelect, 7, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, modeSelect, 46, SpringLayout.WEST, contentPanel);
		}
		
		JPanel sensor_panel = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, sensor_panel, 1, SpringLayout.SOUTH, mode_panel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, sensor_panel, -6, SpringLayout.NORTH, manual_control_panel);
		sensor_panel.setBorder(new TitledBorder(null, "Water level", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		sl_contentPanel.putConstraint(SpringLayout.EAST, sensor_panel, -15, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, sensor_panel, 0, SpringLayout.WEST, manual_control_panel);
		contentPanel.add(sensor_panel);
		sensor_panel.setLayout(null);
		
		water_level_status = new JLabel("");
		water_level_status.setBounds(56, 15, 74, 30);
		sensor_panel.add(water_level_status);
		
		JButton btnNewButton_1 = new JButton("Refill water");
		btnNewButton_1.setBounds(283, 19, 105, 23);
		sensor_panel.add(btnNewButton_1);
		
		JPanel attribute_panel = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, manual_control_panel, -6, SpringLayout.NORTH, attribute_panel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, attribute_panel, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, attribute_panel, -15, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, attribute_panel, 230, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, attribute_panel, -10, SpringLayout.SOUTH, contentPanel);
		attribute_panel.setBorder(new TitledBorder(null, "Attributes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(attribute_panel);
		attribute_panel.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("Pump Run Time");
		lblNewLabel_4.setBounds(53, 22, 115, 15);
		attribute_panel.add(lblNewLabel_4);
		
		pump_run_input = new JTextField();
		pump_run_input.setBounds(178, 22, 211, 22);
		attribute_panel.add(pump_run_input);
		pump_run_input.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Pump Stop Time");
		lblNewLabel_5.setBounds(53, 60, 115, 15);
		attribute_panel.add(lblNewLabel_5);
		
		pump_stop_input = new JTextField();
		pump_stop_input.setBounds(178, 57, 211, 21);
		attribute_panel.add(pump_stop_input);
		pump_stop_input.setColumns(10);
		{
			JButton okButton = new JButton("OK");
			okButton.setBounds(291, 92, 98, 23);
			attribute_panel.add(okButton);
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		
		JLabel label_1 = new JLabel("min");
		label_1.setBounds(399, 25, 32, 15);
		attribute_panel.add(label_1);
		
		JLabel label_2 = new JLabel("min");
		label_2.setBounds(399, 60, 32, 15);
		attribute_panel.add(label_2);
		
		JPanel panel = new JPanel();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, panel, 5, SpringLayout.NORTH, mode_panel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.EAST, mode_panel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, mode_panel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, panel, 188, SpringLayout.EAST, mode_panel);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("Location:");
		label.setBounds(10, 10, 54, 15);
		panel.add(label);
		
		 location_label = new JLabel("");
		location_label.setBounds(74, 10, 55, 15);
		panel.add(location_label);
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
		               
		                 unsubscribeWaterLevelSensor();
		            	 dispose();
		            }
		        });					
			}
		}	
		
        // Size dialog to components.
        //pack();
	}
	
	/**
	 * Create the dialog.
	 */
	
	
	public HydroponicsDialog() {
		initGUI();
	}
	
    public HydroponicsDialog(SystemRepository respository) {
        initGUI();
        this.respository = respository;
        try {
			refreshSystemStatus(4);
		} catch (AdsException e) {
			e.printStackTrace();
		} catch (DeviceTypeException e) {
			e.printStackTrace();
		}
    }		

    public void addSystems(SystemRepository respository){
        this.respository = respository;
        try {
			refreshSystemStatus(4);
		} catch (AdsException e) {
			e.printStackTrace();
		} catch (DeviceTypeException e) {
			e.printStackTrace();
		}             
    }    
    
    public void addSystems(SystemRepository respository, int index) throws AdsException, DeviceTypeException{
        this.respository = respository;
        this.system_index = index;
        refreshSystemStatus(index);             
    }    
	
    private void refreshSystemStatus(int index) throws AdsException, DeviceTypeException{
    	// select the system
    	try{		
    		this.system = (HydroponicsSystem) respository.findSystem(SystemStructure.HYDROPONICS,  index ); 
		}catch(java.lang.IndexOutOfBoundsException e){
			showError("System Not Found",false);
			e.printStackTrace();
		}         
        
    	// debug the system values        
        this.system.test();
        
        // refresh the system
    	system.refreshSystemStatus();		
		mode_value = system.getSysteModeValue();		

		/**/
		valve1_value = system.getInletValveValue(); 		
		valve2_value = system.getOutletValveValue();		
		pump_value = system.getPumpValue();						
		int pump_attr_values[] =   system.getPumpTimeValues();                       
		pump_attr__runtime_value = pump_attr_values[0];
		pump_attr_stoptime_value = pump_attr_values[1];

		refreshUIState();
 		LOG.debug("refreshSystemStatus  " +valve1_value+"  "+valve2_value+"  "+pump_value+"  "+mode_value+"  "+pump_attr__runtime_value+" "+pump_attr_stoptime_value+ "  ");
 			
		// subscribe to water sensor		  		
 		subscribeWaterLevelSensor();
		 
		
    } 	
	
    private void setMode(String mode) throws AdsException {
        if (Objects.equals(mode, HydroponicsConstant.ModeCommand.MANUAL.getDescritpion())) {
        	setDisabled();
			system.setMode( HydroponicsConstant.ModeCommand.MANUAL);          
        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.CONFIG.getDescritpion())) {
        	setEnabled();
			system.setMode( HydroponicsConstant.ModeCommand.CONFIG);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.RUNNING.getDescritpion())) {
        	setEnabled();
			system.setMode( HydroponicsConstant.ModeCommand.RUNNING);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.CLEANUP.getDescritpion())) {
        	setEnabled();
			system.setMode( HydroponicsConstant.ModeCommand.CLEANUP);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.FILLIN.getDescritpion())) {
        	setEnabled();
			system.setMode( HydroponicsConstant.ModeCommand.FILLIN);

        } else if (Objects.equals(mode, HydroponicsConstant.ModeCommand.CHECK.getDescritpion())) {
        	setEnabled();
			system.setMode( HydroponicsConstant.ModeCommand.CHECK);
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
		
 		// assign the system status to UI
 		modeSelect.setSelectedItem(mode_value);
 		location_label.setText(system.getSystemId());
 		
 		pump_run_input.setText(""+pump_attr__runtime_value);
 		pump_stop_input.setText(""+pump_attr_stoptime_value);	
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
	    System.out.println("unsubscribeWaterLevelSensor ");
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

		        System.out.println("water Value:\t\t"
		                + Convert.ByteArrToInt(notification.getData())+"   LENGTH: "+notification.getData().length+ "   DATA:"+ data[0]+"  "+data[1]+"  "+data[2]+"  "+data[3]);		        
		        
		        byte level = data[0];
		        System.out.println("onEvent  "+level);
		       // water_level_value = HydroponicsConstant.WaterLevelState.getName(level);
		       // String str = Integer.toBinaryString((level & 0xFF) + 0x100 ).substring(1);
		      //  LOG.debug("water level:\t\t  "+level +"   "+ str +  "   "+ water_level_value);
		       // water_level_status.setText(water_level_value);
		        
		        
		        //Convert.ByteArrToDouble(data)  ByteArrToShort  ByteArrToInt  
		        
		        //LOG.debug("Notification:\t" + notification.getHNotification());
		        //LOG.debug("Time:\t\t" + notificationDate.toString());
		        //LOG.debug("User:\t\t" + user);
		        //LOG.debug("ServerNetID:\t" + addr.getNetIdString() + "\n");
		    }
	}     
}
