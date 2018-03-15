package com.liveco.gateway.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingUtilities;
import javax.swing.JButton;

import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.SystemConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.job.SystemHandler;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.system.BaseSystem;
import com.liveco.gateway.system.FogponicsSystem;
import com.liveco.gateway.system.HydroponicsSystem;
import com.liveco.gateway.system.SystemListenerImpl;
import com.liveco.gateway.system.SystemRepository;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.DateBuilder;
import org.quartz.SchedulerException;

import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class Main extends JFrame {

	private JPanel contentPane, mainpanel;
	JPanel irrigation_panel, air_conditioner_panel, CO2_panel;
    private static final Logger LOG = LogManager.getLogger(Main.class);
    
    private SystemRepository repository;
    private SystemHandler systemScheduleHandler;
    private SystemListenerImpl systemListener;
    private String system_mode = "";
    private boolean PLC_connected = false;
    
    
    private PLCConnectDialog plc_dialog = null;
    private MQTTConnectDialog mqtt_dialog = null;    
    private CO2Dialog CO2_dialog = null;
    private LEDPanelDialog led_panel_dialog = null;
    private FogponicsDialog fogponics_dialog = null;
    private HydroponicsDialog hydroponics_dialog = null;
    private ShelfLightingPanel shelf_lighting_panel = null;
    
    private JComboBox chamberComboBox = null;
    private JButton connectMQTTBtn,connectPlcBtn,systemModeBtn;
    private JLabel mqttStatusLabel,plcStatusLabel,system_status_label;	
    

    private JSeparator separator_2;	
    
    private List room1 = new ArrayList();
    private List room2 = new ArrayList();
    private List room3 = new ArrayList();
    private List room4 = new ArrayList();
    
    String selectedRoom;
    int CO2_system_index = -1;
    int air_conditioner_inner_index = -1;
    int air_conditioner_index = -1;
    int irrigation_index = -1;
      
    List hydro_list = new ArrayList();
    List fogpo_list = new ArrayList();
    List shelf_list = new ArrayList();
    List panel_list = new ArrayList();    
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);  
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {      
					
					SystemRepository a= new SystemRepository();
					Main frame = new Main(a);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	    	
	}
	
	/*
	 *  start SV5, SV6 , pum --  circulate 
	 *  start SV1            --   add water
	 * 
	 */
	
	
	private void refreshDashboardUI(){
		
		if(PLC_connected) {  
			plcStatusLabel.setText("Connected");  
			connectPlcBtn.setEnabled(false);
		}
		else {  
			plcStatusLabel.setText("Disconnected");  
			connectPlcBtn.setEnabled(true);
		}
		
    	if(system_mode == "RUNNING"){
			systemModeBtn.setBackground(Color.green);
    	}else if(system_mode == "MAINTAINENCE"){
			systemModeBtn.setBackground(Color.YELLOW);
    	}else{
    		showError("System not connected", false);
    		systemModeBtn.setBackground(Color.red);
    	}
		system_status_label.setText(system_mode);
		
		searchRoomDevices();

	}
	
	private void initPLCConnectionPanel(){
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(846, 10, 205, 39);
		contentPane.add(panel_1);
		
		connectPlcBtn = new JButton("Connect PLC");
		panel_1.add(connectPlcBtn);
		
		plcStatusLabel = new JLabel("Connected");
		panel_1.add(plcStatusLabel);
				
		connectPlcBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	connectPLC();            	
            	System.out.println("connectPLC  result  "  +repository.getSystemStatus()); 
            	try {					
					system_mode = repository.getMainSystem().getMode();
					System.out.println("  main system    "+ system_mode);
					
					PLC_connected = repository.isPLCConnected();
					
					refreshDashboardUI();
					
					systemScheduleHandler = new SystemHandler(repository);
					systemListener = new SystemListenerImpl(repository, systemScheduleHandler);
					
					try {
						systemListener.onStartUP();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
				} catch (AdsException e1) {
					
					e1.printStackTrace();
				}
            }
        });		
		
	}

	private void initMQTTConnectionPanel(){
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(897, 565, 203, 42);
		contentPane.add(panel_2);
		
		connectMQTTBtn = new JButton("Connect Server");
		panel_2.add(connectMQTTBtn);
		
		mqttStatusLabel = new JLabel("Connected");
		panel_2.add(mqttStatusLabel);
     
		connectMQTTBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	connectMQTT();
            	System.out.println("connectMQTT  result");
            }
        });			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	private void initSystemModePanel(){
	}	
	
	private void initGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1126, 657);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		 mainpanel = new JPanel();
		mainpanel.setBounds(5, 77, 1095, 477);
		contentPane.add(mainpanel);
		mainpanel.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(5, 10, 267, 42);
		contentPane.add(panel_3);
		
		JLabel lblNewLabel_2 = new JLabel("DASHBOARD");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 18));
		panel_3.add(lblNewLabel_2);
		
		/////////////////////////////////////////////		
		initPLCConnectionPanel();		
		/////////////////////////////////////////////
		initMQTTConnectionPanel();		
		/////////////////////////////////////////////
		initRoomSelectionPanel();	
		/////////////////////////////////////////////
		initCO2DialogPanel();		
		/////////////////////////////////////////////
		initAirContidionerPanel();	
		/////////////////////////////////////////////
		initIrrigationPanel();
		initIrrigationPanel2();
		///////////////////////////////////		
		initSystemModePanel();
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 475, 980, 2);
		mainpanel.add(separator);
		

		
		separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(821, 7, 2, 457);
		mainpanel.add(separator_2);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 63, 1090, 3);
		contentPane.add(separator_1);
		
				
				JPanel room_select_panel = new JPanel();
				room_select_panel.setBounds(282, 10, 216, 39);
				contentPane.add(room_select_panel);
				room_select_panel.setLayout(null);
				
				JLabel lblRoom = new JLabel("Room:");
				lblRoom.setBounds(10, 10, 54, 15);
				room_select_panel.add(lblRoom);
				lblRoom.setFont(new Font("宋体", Font.PLAIN, 15));
				
		chamberComboBox = new JComboBox(vs);
		chamberComboBox.setBounds(56, 4, 150, 27);
		room_select_panel.add(chamberComboBox);
		
		JPanel systemModePanel = new JPanel();
		systemModePanel.setBounds(508, 10, 330, 39);
		contentPane.add(systemModePanel);
		systemModePanel.setLayout(null);
		
		JLabel label = new JLabel("Mode:");
		label.setBounds(10, 11, 40, 18);
		label.setFont(new Font("宋体", Font.PLAIN, 15));
		systemModePanel.add(label);
		

		
		systemModeBtn = new JButton("");
		systemModeBtn.setBounds(60, 11, 131, 23);
		systemModePanel.add(systemModeBtn);
		
		system_status_label = new JLabel("");
		system_status_label.setBounds(201, 14, 115, 15);
		systemModePanel.add(system_status_label);
		
				systemModeBtn.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		
		                int selectionOption = JOptionPane.showConfirmDialog(null,
		                        "Are you going to change the system mode",
		                        "",
		                        JOptionPane.YES_NO_OPTION,
		                        JOptionPane.QUESTION_MESSAGE);             	
		            	
		                if(selectionOption == JOptionPane.YES_OPTION){
		                	
		                	changeSystemModeUI();
		               	
		                }else{
		                	
		                }         	            	
		            }
		        });			
		chamberComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = "" + chamberComboBox.getItemAt(chamberComboBox.getSelectedIndex());
                System.out.println(location+ "  "+room1.size()+"   "+room2.size()+"   "+room3.size()+"   "+room4.size());
                getRoomDevices(location);
            }
        });			
		
		/////////////////////////////////////////////

	}
	
    private void showError(String message, boolean exit) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        if (exit)
            System.exit(0);
    }  
    
    private void showExit() throws InterruptedException {
		int option = JOptionPane.showConfirmDialog(null,
				"Do you want to quit the system "
				, "Not found",
				JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {				 
				}
			});
			if(repository !=null && systemListener!=null)
			systemListener.onShutDown();
			
		}else if(option == JOptionPane.CANCEL_OPTION){
			
		}
    }		

	/**
	 * Create the frame.
	 */
	public Main() {
		 initGUI();
	}
	
	public Main(SystemRepository repository){
        
        this.repository = repository;
        initGUI();   
	}	
	
	private void connectPLC(){
    	
    	try{
    		plc_dialog = new PLCConnectDialog(this, 2, repository);
    		if(!plc_dialog.display()){
    			return;
    		}
    	}catch(Exception e){
    		System.err.println("unabled to send ");
    		showError("Unable to connect", true);
    		return;
    	}  	
	}
	
	private void searchRoomDevices(){
		ArrayList system_list = null;
		BaseSystem system = null;
		String id = null, type = null;
		system_list = new ArrayList();
		system_list.addAll(repository.getCO2Systems());
		system_list.addAll(repository.getFogponicsSystems());
		system_list.addAll(repository.getHydroponicsSystems());
		system_list.addAll(repository.getPanelLightingSystems());
		system_list.addAll(repository.getShelfLightingSystems());
		system_list.addAll(repository.getAirConditioners());
		for(int i=0;i<system_list.size();i++){
			system = (BaseSystem) system_list.get(i);
			id = system.getSystemId();
			type = system.getType();
			System.out.println("searchRoomDevices    "+id+"   "+type);
			
			if(id.startsWith("A1,A2")||id.startsWith("A1")||id.startsWith("A2"))
				room1.add(system);
			else if(id.startsWith("A3,A4")||id.startsWith("A3")||id.startsWith("A4"))
				room2.add(system);
			else if(id.startsWith("B1,B2")||id.startsWith("B1")||id.startsWith("B2"))
				room3.add(system);
			else if(id.startsWith("B3,B4")||id.startsWith("B3")||id.startsWith("B4"))
				room4.add(system);
		}
		
		getRoomDevices("A1,A2");
	}
	
	private void getRoomDevices(String roomName){
		
		List list = null;
		switch(roomName){
		
			case "A1,A2":
				list = room1;
				break;
			case "A3,A4":
				list = room2;
				break;
			case "B1,B2":
				list = room3;
				break;
			case "B3,B4":
				list=  room4;
				break;
		}
		
		System.out.println( "-----------------------------------------------------------------------"+list.size() );
		System.out.println( "-----------------------------------------------------------------------"+list.size() );
		System.out.println( "-----------------------------------------------------------------------"+list.size() );
		System.out.println( "-----------------------------------------------------------------------"+list.size() );
		System.out.println( "-----------------------------------------------------------------------"+list.size() );

	     hydro_list = new ArrayList();
	     fogpo_list = new ArrayList();
	     shelf_list = new ArrayList();
	     panel_list = new ArrayList();
		
		BaseSystem system = null;
		for(int i=0;i<list.size();i++){
			system = (BaseSystem) list.get(i);
			System.out.println(system.getIndex()+ "  "+system.getType() + "   "+system.getSystemId() );
			if(system.getType()=="AIR_CONDITIONING_SYSTEM"){
				air_conditioner_index = system.getIndex();
			}else if(system.getType() == "CO2_SYSTEM"){
				CO2_system_index = system.getIndex();
				System.out.println("-------------------------------CO2 index "+system.getIndex());				
			}else if(system.getType() == "HYDROPONICS" ){
				hydro_list.add(system.getIndex());
				System.out.println("-------------------------------hydro index "+system.getIndex());
			}else if(system.getType() == "FOGPONICS_SYSTEM" ){
				fogpo_list.add(system.getIndex());
			}else if(system.getType() == "SHELF_LIGHTING_SYSTEM"){
				System.out.println("lighting index   "+system.getIndex());
				shelf_list.add(system.getIndex());
			}else if(system.getType() == "PANEL_LIGHTING_SYSTEM"){
				panel_list.add(system.getIndex());
			}
		}
		
		System.out.println( "------"+ "hydro:"+hydro_list.size()+"  fogoponics:"+fogpo_list.size()+"  shelf:"+shelf_list.size()+"   panels:"+panel_list.size() );
		
		for(int i=0;i<shelf_list.size();i++) {
			System.out.println("shelf index     "+shelf_list.get(i));
		}
		
		for(int i=0;i<fogpo_list.size();i++) {
			System.out.println("fogpo_list index     "+fogpo_list.get(i));
		}

		for(int i=0;i<hydro_list.size();i++) {
			System.out.println("hydro_list index     "+hydro_list.get(i));
		}
		
		buildRoomIrrigationSystemUI();
		buildRoomLedSystemUI();
	}

	
	


	
	
	
	
	
	
	
	
	
	
	
	
	
    private JLabel water_level_value_label, irrigation_system_mode, air_conditioner_value_label, air_conditioning_system_mode, CO2_system_mode,CO2_value_label;
    private JButton irrigation_settting_btn, airconditioner_settting_btn,CO2_settting_btn;	
    private JPanel irrigation_panel2;
    private JLabel label_1;
    private JButton irrigation_settting_btn2;
    private JLabel raddda;
    private JLabel water_level_value_label2;
    private JLabel irrigation_system_mode2;	
    AirConditionerDialog conditon_dialog;
	
	Vector<String> vs;
	private void initRoomSelectionPanel(){

		vs = new Vector<String>();
        vs.add("A1,A2");
        vs.add("A3,A4");
        vs.add("B1,B2");
        vs.add("B3,B4");
	}
	
	private void initCO2DialogPanel(){
		 CO2_panel = new JPanel();
		CO2_panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "CO2 System", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		CO2_panel.setBounds(833, 11, 252, 81);
		mainpanel.add(CO2_panel);
		CO2_panel.setLayout(null);
		
		JLabel lblCo = new JLabel("CO2");
		lblCo.setFont(new Font("宋体", Font.PLAIN, 14));
		lblCo.setBounds(10, 48, 38, 17);
		CO2_panel.add(lblCo);
		
		 CO2_settting_btn = new JButton("Setting");
		CO2_settting_btn.setBounds(167, 45, 75, 23);
		CO2_panel.add(CO2_settting_btn);
		
		JLabel lblNewLabel_3 = new JLabel("Mode");
		lblNewLabel_3.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(10, 23, 38, 15);
		CO2_panel.add(lblNewLabel_3);
		
		JLabel CO2_value_label = new JLabel("");
		CO2_value_label.setFont(new Font("宋体", Font.PLAIN, 14));
		CO2_value_label.setBounds(66, 49, 38, 17);
		CO2_panel.add(CO2_value_label);
		
		 CO2_system_mode = new JLabel("Automatic");
		CO2_system_mode.setFont(new Font("宋体", Font.PLAIN, 14));
		CO2_system_mode.setBounds(58, 23, 80, 15);
		CO2_panel.add(CO2_system_mode);	
		
		
		CO2_settting_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	showCO2Dialog();
            }
        });				
	}
	
	private void initAirContidionerPanel(){
		air_conditioner_panel = new JPanel();
		air_conditioner_panel.setLayout(null);
		air_conditioner_panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "AirConditioner System", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		air_conditioner_panel.setBounds(833, 103, 252, 81);
		mainpanel.add(air_conditioner_panel);
		
		airconditioner_settting_btn = new JButton("Setting");
		airconditioner_settting_btn.setBounds(167, 46, 75, 23);
		air_conditioner_panel.add(airconditioner_settting_btn);
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setFont(new Font("宋体", Font.PLAIN, 14));
		lblMode.setBounds(10, 24, 47, 15);
		air_conditioner_panel.add(lblMode);
		
		JLabel lblTemperature = new JLabel("Temperature");
		lblTemperature.setFont(new Font("宋体", Font.PLAIN, 14));
		lblTemperature.setBounds(10, 49, 84, 17);
		air_conditioner_panel.add(lblTemperature);
		
		air_conditioner_value_label = new JLabel("");
		air_conditioner_value_label.setFont(new Font("宋体", Font.PLAIN, 14));
		air_conditioner_value_label.setBounds(103, 49, 38, 17);
		air_conditioner_panel.add(air_conditioner_value_label);
		
		air_conditioning_system_mode = new JLabel("Automatic");
		air_conditioning_system_mode.setFont(new Font("宋体", Font.PLAIN, 14));
		air_conditioning_system_mode.setBounds(67, 24, 75, 15);
		air_conditioner_panel.add(air_conditioning_system_mode);	
		
		airconditioner_settting_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	showAirConditionerDialog();
            }
        });			
	}
	
	private void initIrrigationPanel(){
		
		irrigation_panel = new JPanel();
		irrigation_panel.setLayout(null);
		irrigation_panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Irrigation System", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		irrigation_panel.setBounds(43, 368, 252, 81);
		mainpanel.add(irrigation_panel);
		
		JLabel lblWaterLevel = new JLabel("Water level:");
		lblWaterLevel.setFont(new Font("宋体", Font.PLAIN, 14));
		lblWaterLevel.setBounds(10, 48, 96, 17);
		irrigation_panel.add(lblWaterLevel);
		
		irrigation_settting_btn = new JButton("Setting");
		irrigation_settting_btn.setBounds(167, 45, 75, 23);
		irrigation_panel.add(irrigation_settting_btn);
		
		JLabel lblMode_1 = new JLabel("Mode");
		lblMode_1.setFont(new Font("宋体", Font.PLAIN, 14));
		lblMode_1.setBounds(10, 23, 44, 15);
		irrigation_panel.add(lblMode_1);		
		
      
		
		water_level_value_label = new JLabel("");
		water_level_value_label.setFont(new Font("宋体", Font.PLAIN, 14));
		water_level_value_label.setBounds(104, 49, 63, 17);
		irrigation_panel.add(water_level_value_label);
		
		irrigation_system_mode = new JLabel("Automatic");
		irrigation_system_mode.setFont(new Font("宋体", Font.PLAIN, 14));
		irrigation_system_mode.setBounds(52, 23, 86, 15);
		irrigation_panel.add(irrigation_system_mode);		
	}
	
	private void initIrrigationPanel2(){
		irrigation_panel2 = new JPanel();
		irrigation_panel2.setLayout(null);
		irrigation_panel2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Irrigation System", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		irrigation_panel2.setBounds(435, 368, 252, 81);
		mainpanel.add(irrigation_panel2);
		
		label_1 = new JLabel("Water level:");
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));
		label_1.setBounds(10, 48, 96, 17);
		irrigation_panel2.add(label_1);
		
		irrigation_settting_btn2 = new JButton("Setting");
		irrigation_settting_btn2.setBounds(167, 45, 75, 23);
		irrigation_panel2.add(irrigation_settting_btn2);
		
		raddda = new JLabel("Mode");
		raddda.setFont(new Font("宋体", Font.PLAIN, 14));
		raddda.setBounds(10, 23, 44, 15);
		irrigation_panel2.add(raddda);
		
		water_level_value_label2 = new JLabel("");
		water_level_value_label2.setFont(new Font("宋体", Font.PLAIN, 14));
		water_level_value_label2.setBounds(104, 49, 63, 17);
		irrigation_panel2.add(water_level_value_label2);
		
		irrigation_system_mode2 = new JLabel("Automatic");
		irrigation_system_mode2.setFont(new Font("宋体", Font.PLAIN, 14));
		irrigation_system_mode2.setBounds(52, 23, 86, 15);
		irrigation_panel2.add(irrigation_system_mode2);		
	}	
	
	
	private void buildRoomLedSystemUI(){
		
		if(panel_list.size()> 0){
		    try{
		    	led_panel_dialog = new LEDPanelDialog();
		    	led_panel_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		    	led_panel_dialog.setVisible(true);
		    	led_panel_dialog.addSystems(repository);
		   	}catch(Exception e){
		   		System.err.println("unabled to send "+e.toString());
		   		showError("Unable to connect", true);
		   		return;
		   	}
		}
		
		System.out.println("shelf_list   size    "+shelf_list.size());
		
		if(shelf_list.size() == 2){
			System.out.println("system  index  ===  2");
			if(shelf_lighting_panel == null){
				shelf_lighting_panel = new ShelfLightingPanel(repository, (int)shelf_list.get(0), (int)shelf_list.get(1));
				mainpanel.add(shelf_lighting_panel);
				shelf_lighting_panel.setBounds(52, 50, 600, 400);
			}else{
				shelf_lighting_panel.refreshSystemStatus((int)shelf_list.get(0), (int)shelf_list.get(1));
			}	
		}
	}
	
	private void buildRoomIrrigationSystemUI(){

		if(irrgationDialogListener1!=null) { irrigation_settting_btn.removeActionListener(irrgationDialogListener1); irrgationDialogListener1 = null;}
		if(irrgationDialogListener2!=null) {irrigation_settting_btn2.removeActionListener(irrgationDialogListener2); irrgationDialogListener2=null;}
		
		System.out.println("buildRoomIrrigationSystemUI   hydro num:"+hydro_list.size() +"    fogpo num:"+ fogpo_list.size());
		
		if(hydro_list.size() == 2){
			
			irrgationDialogListener1 = new HydroActionListener((int)hydro_list.get(0));
			irrgationDialogListener2 = new HydroActionListener((int)hydro_list.get(1));
						
			irrigation_settting_btn.addActionListener(irrgationDialogListener1);
			irrigation_settting_btn2.addActionListener(irrgationDialogListener2);

			irrigation_panel.setVisible(true);
			irrigation_panel2.setVisible(true);			
			
		}else if(hydro_list.size() == 1 && fogpo_list.size() ==1){

			irrgationDialogListener1 = new HydroActionListener((int)hydro_list.get(0));
			irrgationDialogListener2 = new FogpoActionListener((int)fogpo_list.get(0));
			
			irrigation_settting_btn.addActionListener(irrgationDialogListener1);
			irrigation_settting_btn2.addActionListener(irrgationDialogListener2);
			
			irrigation_panel.setVisible(true);
			irrigation_panel2.setVisible(true);

			
		}else if(hydro_list.size() == 1 && fogpo_list.size() == 0){
			irrgationDialogListener1 = new HydroActionListener((int)hydro_list.get(0));
			irrigation_settting_btn.addActionListener(irrgationDialogListener1);
			irrigation_panel2.setVisible(false);
		}		
	}
	
	ActionListener irrgationDialogListener1 = null;
	class HydroActionListener implements ActionListener {
		
		int system_index ;
		public HydroActionListener(int index){
			this.system_index = index;
		}
		
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.out.println(system_index);
        	showHydroponicsDialog(system_index);           	
        }
    }
	
	ActionListener irrgationDialogListener2 = null;
	class FogpoActionListener implements ActionListener {

		int system_index ;
		public FogpoActionListener(int index){
			this.system_index = index;
		}
		
		@Override
        public void actionPerformed(ActionEvent e) {
			showFogponicsDialog( system_index);
         	            	
        }
    }	
	
	
	private void changeSystemModeUI(){
        try {             	
        	if(system_mode == "RUNNING"){
        		repository.getMainSystem().setMode(SystemConstant.ModeCommand.MAINTAINENCE);
				
        		system_mode = "MAINTAINENCE";
				systemModeBtn.setBackground(Color.YELLOW);
				system_status_label.setText(system_mode);
        	}else if(system_mode == "MAINTAINENCE"){
        		repository.getMainSystem().setMode(SystemConstant.ModeCommand.RUNNING);
        		
				system_mode = "RUNNING";
				systemModeBtn.setBackground(Color.green);
				system_status_label.setText(system_mode);

				systemListener.onChangeToRunningMode();

        	}else{
        		showError("System not connected", false);
        		systemModeBtn.setBackground(Color.red);
        	}
        	
		} catch (AdsException  e1) {
			showError(e1.getErrMessage(), false);
			e1.printStackTrace();
		}  catch(NullPointerException e1){
			showError("System not connected", false);
			systemModeBtn.setBackground(Color.red);
		} 		
	}	
	

	private void getSystemStatus(){
		
	}
	

	

	private void showFogponicsDialog(int index){
	    try{
	    	fogponics_dialog = new FogponicsDialog();
	    	fogponics_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    	fogponics_dialog.setVisible(true);
	    	fogponics_dialog.addSystems(repository,index);
	   	}catch(Exception e){
	   		System.err.println("unabled to send "+e.toString());
	   		showError("Unable to connect", true);
	   		return;
	   	}		
	}
	
	private void showHydroponicsDialog(int index){
	    try{
	    	hydroponics_dialog = new HydroponicsDialog();
	    	hydroponics_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    	hydroponics_dialog.setVisible(true);
	    	hydroponics_dialog.addSystems(repository,index);
	   	}catch(Exception e){
	   		System.err.println("unabled to send "+e.toString());
	   		showError("Unable to connect", true);
	   		return;
	   	}		
	}
	
	private void showCO2Dialog(){
    	try{
    		 CO2_dialog = new CO2Dialog();
    		   		 
    		 CO2_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		 CO2_dialog.setVisible(true);
    		 CO2_dialog.addSystems(repository,CO2_system_index);
    	}catch(Exception e){
    		System.err.println("unabled to send "+e.toString());
    		showError("Unable to connect", true);
    		return;
    	}			
	}	
	
	private void showAirConditionerDialog(){
    	try{
    		 conditon_dialog = new AirConditionerDialog();
    		   		 
    		conditon_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		conditon_dialog.setVisible(true);
    		conditon_dialog.addSystems(repository,air_conditioner_index);
    	}catch(Exception e){
    		System.err.println("unabled to send "+e.toString());
    		showError("Unable to connect", true);
    		return;
    	}			
	}		
	
	private void connectMQTT(){ 	
    	try{
    		mqtt_dialog = new MQTTConnectDialog(this, 2, repository);
    		
    		
    		if(!mqtt_dialog.display()){
    			return;
    		}
    	}catch(Exception e){
    		System.err.println("unabled to send ");
    		showError("Unable to connect", true);
    		return;
    	}		
	}
	

}
