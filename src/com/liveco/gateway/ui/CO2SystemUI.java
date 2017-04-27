package com.liveco.gateway.ui;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.CO2System;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class CO2SystemUI extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3403675278480479931L;
	
	private CO2System system;
    private JPanel jp;
    
    private JLabel modeName;
    private JComboBox modeSelect;
    
    private JLabel valveName;
    private JLabel valvePLC;
    private JButton valveOn;
    private JButton valveOff;
    
    private JLabel CO2SensorName;
    private JLabel CO2SensorValue;
    
    private JLabel minText;
    private JLabel maxText;
    private JTextField minValue;
    private JTextField maxValue;

    public CO2SystemUI(CO2System system) {
        super();
        this.system = system;

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
           initGui();
        }
        catch (Exception e) {
          e.printStackTrace();
        }        
       
    }

    private void initGui() throws Exception {
    	
 	    setLayout(null); 	   

    	
        modeUI();
        CO2ValveControlUI();
        CO2SensorUI();
        CO2ValveSettingUI();
    }

    private void modeUI() {
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.MANUAL));
        modeSelect = new JComboBox(vs);
        // 设置模式
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
        valvePLC = new JLabel("Valve PLC: " + String.valueOf(CO2SystemConstant.Table.VALVE.getOffset()));
        valvePLC.setBounds(500, 200, 300, 25);
        valveOn = new JButton("Valve On");
        valveOff = new JButton("Vave Off");
        
        valveName.setBounds( 10,  60, 200, 50);
        valveOn.setBounds(   100, 70, 100, 25);
        valveOff.setBounds(  200, 70, 100, 25);
        
        valveOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOff.setEnabled(true);
                valveOn.setEnabled(false);
                
                try {
					system.open("actuator.valve",1);
				} catch (AdsException e1) {
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				}               
            }
        });
        valveOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOn.setEnabled(true);
                valveOff.setEnabled(false);

                try {
					system.close("actuator.valve",1);
				} catch (AdsException e1) {
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				}   
            }
        });
        add(valveName);
        //getContentPane().add(valvePLC);
        add(valveOn);
        add(valveOff);
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
        // minLimt 和 maxLimit 的值一样
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
                valveOff.setEnabled(false);
                minValue.setEnabled(false);
                maxValue.setEnabled(false);
                system.configMode("config.system.mode", CO2SystemConstant.ModeCommand.AUTOMATIC);
                
            } else if (Objects.equals(m, CO2SystemConstant.ModeCommand.MANUAL.getDescritpion())) {
                valveOn.setEnabled(true);
                valveOff.setEnabled(true);
                minValue.setEnabled(true);
                maxValue.setEnabled(true);
                
                system.configMode("config.system.mode", CO2SystemConstant.ModeCommand.MANUAL);

            }    		
    	}else{
    		
    		
    		
    	}
    }
    
    
    public static void main(String[] args) {
        // write your code here
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG); 
 		
 	   	//ADSConnection ads = new ADSConnection();
 	   	//ads.openPort(false,"5.42.203.215.1.1",851);	
 		 	   	
 	   	CO2System system_CO2 = new CO2System(null, 0, "af");
 	    CO2SystemUI CO2_ui = new CO2SystemUI(system_CO2);
 	   	
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
                System.exit(1);
            }
        });
 	     	    
 	    main_frame.getContentPane().add(CO2_ui);
	    
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
