package com.liveco.gateway.ui;


import com.liveco.gateway.constant.NutrientSystemConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;

import com.liveco.gateway.system.NurientSystem;
import com.liveco.gateway.system.SystemRepository;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Vector;

public class NurientSystemUI extends JPanel{

    private static final Logger LOG = LogManager.getLogger(NurientSystemUI.class);
	
	
	private NurientSystem system;
    private static SystemRepository respository;

    
    // PH浼犳劅鍣�
    private JLabel phValue;
    // EC浼犳劅鍣�
    private JLabel ecValue;
    // PE娴侀噺璁�
    private JLabel peValue;
    // meter姘撮榾
    private JLabel meterName;
    private JButton meterOn;
    private JLabel meterStatus;
    // mode
    private JComboBox modeSelect;
    private JLabel curmode;
    // 鎺у埗鐘舵��
    private JButton start;
    private JButton stop;
    private JButton config;
    private JLabel configStatus;
    // 涓�涓皬姘存车
    private JLabel pumpName;
    private JButton pumpOn;
    private JLabel pumpStatus;
    // 6涓惊鐜按闃�
    private Vector<JLabel> valveName;
    private Vector<JButton> valveOn;
    private Vector<JLabel> valveStstus;
    // 1涓狶Ed鏉�鑿岀伅
    private JLabel ledName;
    private JButton ledOn;
    private JLabel ledStatus;
   
    //

    
    
    public NurientSystemUI() {
        initGui();
    }    
    
    public NurientSystemUI(NurientSystem system) {
        this.system = system;
        initGui();
    }
    
    public NurientSystemUI(SystemRepository respository) {
        this.respository = respository;
        
        initGui();
        refreshSystemStatus(0);
    }  
    
    
    public void addSystems(SystemRepository respository){
        this.respository = respository;
        initGui();
        refreshSystemStatus(0);             
    }   

    

    JLabel j1,j2,j3,j4,j5,j6;
    JButton on1,on2,on3,on4,on5,on6;
    JLabel s1,s2,s3,s4,s5,s6;
    
    private String SV1_value,SV2_value,SV3_value,SV4_value,SV5_value,SV6_value;
    private String uv_led_value, pump_value,meter_value;        
    String mode_value;

    
    private void refreshSystemStatus(int index){
    	
    	try{		
	        system = (NurientSystem) respository.findSystem(SystemStructure.NUTRIENT_SYSTEM,   0);
		}catch(java.lang.IndexOutOfBoundsException e){
			showError("System Not Found",false);
			e.printStackTrace();
		}
    	
        this.system.test();
        
        //respository.getPHNotification("G_HMI.stConfig.rPHActualValue");
        respository.getECNotification("G_HMI.stConfig.rECActualValue");
        
        // refresh the system
    	system.refreshSystemStatus();
		
		mode_value = system.getSysteModeValue();
		
		
		//valve1_value = system.getInletValveValue();
		//if(valve1_value == "ON") toClose(valveStatus1,valveOn1); else toOpen(valveStatus1,valveOn1);
		
		SV1_value = system.getValveValue(1);
		SV2_value = system.getValveValue(1);
		SV3_value = system.getValveValue(1);
		SV4_value = system.getValveValue(1);
		SV5_value = system.getValveValue(1);
		SV6_value = system.getValveValue(1);
		
		pump_value = system.getPumpValue();
		if(pump_value == "ON") toClose(pumpStatus,pumpOn);  else toOpen(pumpStatus,pumpOn);        
		uv_led_value = system.getUVLEDValue();
		if(uv_led_value == "ON") toClose(ledStatus,ledOn);  else toOpen(ledStatus,ledOn);        
		meter_value = system.getMeterValue();
		if(meter_value == "ON") toClose(meterStatus,meterOn);  else toOpen(meterStatus,meterOn); 
		
		System.out.println(mode_value+"   "+pump_value+"  "+uv_led_value+"    "+meter_value);
    }     

    private void initGui() {
        setLayout(null);
        phUI();
        ecUI();
        peUI();
        meterUI();
        modeUI();
        configUI();
        pumpUI();
        valveUI();
        ledUI();
        setVisible(true);
    }    
    
    private void ledUI() {
        // where is led???
        ledName = new JLabel("led name: ");
        ledOn = new JButton("led on");
        ledStatus = new JLabel();
        ledName.setBounds(0, 650, 200, 50);
        ledOn.setBounds(400, 650, 100, 50);
        ledStatus.setBounds(600, 650, 200, 50);
        ledOn.setBackground(Color.gray);

        ledOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(uv_led_value == "ON"){
						system.close("actuator.uv_led",1);
						uv_led_value = "OFF";
						toClose(ledStatus,ledOn);
	            	}else if(uv_led_value == "OFF"){
						system.open("actuator.uv_led",1);
						uv_led_value = "ON";
						toOpen(ledStatus,ledOn);
	            	}else{
	            		showError("System not connected", false);
	            		ledOn.setBackground(Color.gray);
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

        add(ledName);
        add(ledOn);
        add(ledStatus);
    }
   
    
    private void valveUI() {

        j1 = new JLabel("valve NO1 name: " + NutrientSystemConstant.FillInCommand.NO1.getDescritpion());
        on1 = new JButton("NO 1 On");
        s1 = new JLabel("");
        on1.setBackground(Color.gray);

        int i = 1;
        j1.setBounds(0, 300+(i)*50, 200, 50);
        on1.setBounds(400, 300+(i)*50, 100, 50);
        s1.setBounds(600, 300+(i)*50, 200, 50);
        on1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV1_value == "ON"){
						system.close("actuator.valve",1);
						SV1_value = "OFF";
						toClose(s1,on1);
	            	}else if(SV1_value == "OFF"){
						system.open("actuator.valve",1);
						SV1_value = "ON";
						toOpen(s1,on1);
	            	}else{
	            		showError("System not connected", false);
	            		on1.setBackground(Color.gray);
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
        add(j1);
        add(s1);
        add(on1);        
        
        
        j2 = new JLabel("valve NO2 name: " + NutrientSystemConstant.FillInCommand.NO2.getDescritpion());
        on2 = new JButton("NO 2 On");
        s2 = new JLabel("");
        on2.setBackground(Color.gray);

        i = 2;
        j2.setBounds(0, 300+(i)*50, 200, 50);
        on2.setBounds(400, 300+(i)*50, 100, 50);
        s2.setBounds(600, 300+(i)*50, 200, 50);    
        on2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV2_value == "ON"){
						system.close("actuator.valve",2);
						SV2_value = "OFF";
						toClose(s2,on2);
	            	}else if(SV1_value == "OFF"){
						system.open("actuator.valve",2);
						SV2_value = "ON";
						toOpen(s2,on2);
	            	}else{
	            		showError("System not connected", false);
	            		on2.setBackground(Color.gray);
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
        add(j2);
        add(s2);
        add(on2);         
        
        
        
        j3 = new JLabel("valve NO3 name: " + NutrientSystemConstant.FillInCommand.NO3.getDescritpion());
        on3 = new JButton("NO 3 On");
        s3 = new JLabel("");
        on3.setBackground(Color.gray);

        i = 3;
        j3.setBounds(0, 300+(i)*50, 200, 50);
        on3.setBounds(400, 300+(i)*50, 100, 50);
        s3.setBounds(600, 300+(i)*50, 200, 50);  
        on3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV3_value == "ON"){
						system.close("actuator.valve",3);
						SV3_value = "OFF";
						toClose(s3,on3);
	            	}else if(SV1_value == "OFF"){
						system.open("actuator.valve",3);
						SV3_value = "ON";
						toOpen(s3,on3);
	            	}else{
	            		showError("System not connected", false);
	            		on3.setBackground(Color.gray);
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
        add(j3);
        add(s3);
        add(on3);         
        
        
        
        j4 = new JLabel("valve NO4 name: " + NutrientSystemConstant.FillInCommand.NO4.getDescritpion());
        on4 = new JButton("NO 4 On");
        s4 = new JLabel("");
        on4.setBackground(Color.gray);

        i = 4;
        j4.setBounds(0, 300+(i)*50, 200, 50);
        on4.setBounds(400, 300+(i)*50, 100, 50);
        s4.setBounds(600, 300+(i)*50, 200, 50);  
        on4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV4_value == "ON"){
						system.close("actuator.valve",4);
						SV4_value = "OFF";
						toClose(s4,on4);
	            	}else if(SV4_value == "OFF"){
						system.open("actuator.valve",4);
						SV4_value = "ON";
						toOpen(s4,on4);
	            	}else{
	            		showError("System not connected", false);
	            		on4.setBackground(Color.gray);
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
        add(j4);
        add(s4);
        add(on4);         
        
        
        
        j5 = new JLabel("valve NO5 name: " + NutrientSystemConstant.FillInCommand.NO5.getDescritpion());
        on5 = new JButton("NO 5 On");
        s5 = new JLabel("");
        on5.setBackground(Color.gray);

        i = 5;
        j5.setBounds(0, 300+(i)*50, 200, 50);
        on5.setBounds(400, 300+(i)*50, 100, 50);
        s5.setBounds(600, 300+(i)*50, 200, 50);  
        on5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV5_value == "ON"){
						system.close("actuator.valve",5);
						SV5_value = "OFF";
						toClose(s5,on5);
	            	}else if(SV5_value == "OFF"){
						system.open("actuator.valve",5);
						SV5_value = "ON";
						toOpen(s5,on5);
	            	}else{
	            		showError("System not connected", false);
	            		on5.setBackground(Color.gray);
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
        add(j5);
        add(s5);
        add(on5);         
        
        
        j6 = new JLabel("valve NO6 name: " + NutrientSystemConstant.FillInCommand.NO6.getDescritpion());
        on6 = new JButton("NO 6 On");
        s6 = new JLabel("");
        on6.setBackground(Color.gray);

        i = 6;
        j6.setBounds(0, 300+(i)*50, 200, 50);
        on6.setBounds(400, 300+(i)*50, 100, 50);
        s6.setBounds(600, 300+(i)*50, 200, 50);
        on6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV6_value == "ON"){
						system.close("actuator.valve",6);
						SV6_value = "OFF";
						toClose(s6,on6);
	            	}else if(SV6_value == "OFF"){
						system.open("actuator.valve",6);
						SV6_value = "ON";
						toOpen(s6,on6);
	            	}else{
	            		showError("System not connected", false);
	            		on6.setBackground(Color.gray);
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
        add(j6);
        add(s6);
        add(on6);         
        
        

    }

    private void pumpUI() {
        pumpName = new JLabel("pump name: " + NutrientSystemConstant.Table.PUMP.getDescritpion());
        pumpOn = new JButton("pump on");
        pumpStatus = new JLabel();
        pumpOn.setBackground(Color.gray);

        pumpName.setBounds(0, 250, 200, 50);
        pumpOn.setBounds(400, 250, 100, 50);
        pumpStatus.setBounds(600, 250, 200, 50);
        pumpOn.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(pump_value == "ON"){
						system.close("actuatord.pump",1);
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
    	control_component.setBackground(Color.red);    	
    }    
    
    
    
    private void configUI() {
        start = new JButton("start");
        stop = new JButton("stop");
        config = new JButton("connfig");
        configStatus = new JLabel("config status: " );
        start.setBounds(0,200,100,50);
        stop.setBounds(100,200,100,50);
        config.setBounds(200,200,100,50);
        configStatus.setBounds(300,200,300,50);
        add(start);
        add(stop);
        add(config);
        add(configStatus);
    }

    private void modeUI() {
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(NutrientSystemConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(NutrientSystemConstant.ModeCommand.MANUAL));
        vs.add(String.valueOf(NutrientSystemConstant.ModeCommand.SEMI_AUTOMATIC));
        modeSelect = new JComboBox(vs);
        modeSelect.setBounds(0,150,100,50);
        curmode = new JLabel("");
        curmode.setBounds(100, 150, 200, 50);
        // 璁剧疆妯″紡
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                curmode.setText("current mode: " + mode);
                setMode(mode);
            }
        });
        add(modeSelect);
        add(curmode);
    }
    
    
    private void setMode(String mode) {
        if (Objects.equals(mode, NutrientSystemConstant.ModeCommand.AUTOMATIC)) {
            try {
				system.configMode("config.system.mode", NutrientSystemConstant.ModeCommand.AUTOMATIC);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
            
        } else if (Objects.equals(mode, NutrientSystemConstant.ModeCommand.MANUAL)) {
            try {
				system.configMode("config.system.mode", NutrientSystemConstant.ModeCommand.MANUAL);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        } else if (Objects.equals(mode, NutrientSystemConstant.ModeCommand.SEMI_AUTOMATIC)) {
            try {
				system.configMode("config.system.mode", NutrientSystemConstant.ModeCommand.SEMI_AUTOMATIC);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        }   
    }    
    

    private void meterUI() {
        meterName = new JLabel("meter pump name: " + NutrientSystemConstant.Table.PUMP.getDescritpion());
        meterOn =  new JButton("meter pump on");
        meterStatus = new JLabel("meter pump status: ");
        meterName.setBounds(0, 90, 300, 30);
        meterOn.setBounds(0, 120, 200, 30);
        meterStatus.setBounds(400, 120, 300, 30);
        meterOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(meter_value == "ON"){
						system.close("actuator.meter_pump",1);
						meter_value = "OFF";
						toClose(meterStatus,meterOn);
	            	}else if(meter_value == "OFF"){
						system.open("actuator.meter_pump",1);
						meter_value = "ON";
						toOpen(meterStatus,meterOn);
	            	}else{
	            		showError("System not connected", false);
	            		meterOn.setBackground(Color.gray);
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

        add(meterName);
        add(meterOn);
        add(meterStatus);
    }

    private void peUI() {
        // 鎵句笉鍒板叾浠杝ensor
        peValue = new JLabel("PE value: ");
        peValue.setBounds(0,60,200,30);
        add(peValue);
    }
    


    private void ecUI() {
        // 鎵句笉鍒板叾浠杝ensor
        ecValue = new JLabel("EC value: ");
        ecValue.setBounds(0,30,200,30);
        add(ecValue);
    }

    private void phUI() {
        phValue = new JLabel("PH value: ");
        phValue.setBounds(0,0,200,30);
        add(phValue);
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

    
    
    public static void main(String args[]) {
    	
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);        	
    	
 	    NurientSystemUI UI = new NurientSystemUI();

 	   	
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("NurientSystem");
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