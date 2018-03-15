package com.liveco.gateway.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.mqtt.MqttAdapter;
import com.liveco.gateway.mqtt.MqttAdapterConfiguration;
import com.liveco.gateway.mqtt.MqttCommandCallback;
import com.liveco.gateway.system.CO2System;
import com.liveco.gateway.system.SystemRepository;


public class MqttSystemUI  extends JPanel{
	
    private static final Logger LOG = LogManager.getLogger(MqttSystemUI.class);

	private static final long serialVersionUID = 1L;
	MqttAdapterConfiguration configuration;
	MqttAdapter adapter; 
	
	MqttSystemUI(MqttAdapter adapter){
		this.adapter = adapter;
		initGui();
	}
	
	
    private JButton mqttConnect;
    private JTextField client_id = new JTextField();    
    private JTextField mqtt_address = new JTextField();
    static JLabel mqtt_status = new JLabel();

	
    private void initGui() {

        setLayout(null);
        modeUI();
    }    
 
    private void modeUI() {
    	
    	mqtt_address.setText(adapter.getConfiguration().getMqttBroker());
    	client_id.setText(adapter.getConfiguration().getMqttClientId());

    	
    	mqttConnect = new JButton("MQTT Connect");
    	mqttConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	adapter.getConfiguration().setMqttBroker(mqtt_address.getText());
            	adapter.getConfiguration().setMqttClientId(client_id.getText());
        		adapter.startup();
            }
        });    	
 
    	client_id.setBounds(10, 20, 200, 40);
    	mqtt_address.setBounds(250,20, 200, 40);
    	mqttConnect.setBounds(10, 70, 200, 50);
    	mqtt_status.setBounds(240, 70, 200, 50);
    	
        add(mqttConnect);
        add(mqtt_address);
        add(client_id);
        add(mqtt_status);

    }  
    
    public static void main(String[] args) {
        // write your code here   	
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);   

 		SystemRepository system = new SystemRepository(); 	   	
 	    MqttAdapterConfiguration configuration = new MqttAdapterConfiguration();
 	    configuration.setMqttBroker("tcp://139.59.170.74:1883");
		configuration.setMqttClientId("JavaSample");
		MqttAdapter adapter = new MqttAdapter(configuration, new MqttCommandCallback(system)); 	   	
	 	MqttSystemUI mqttUI = new MqttSystemUI(adapter);
 	   	
 	   	
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("MQTT unit testing");
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
 	    
 	    main_frame.getContentPane().add(mqttUI);
	    
 	   
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
