package com.liveco.gateway.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.mqtt.MqttAdapter;
import com.liveco.gateway.mqtt.MqttAdapterConfiguration;
import com.liveco.gateway.mqtt.MqttCommandCallback;
import com.liveco.gateway.system.SystemRepository;


public class MqttSystemUI  extends JFrame{

	
	SystemRepository system = new SystemRepository();
	
	MqttAdapterConfiguration configuration = new MqttAdapterConfiguration();
	MqttAdapter adapter; 
	
	MqttSystemUI(){
		configuration.setMqttBroker("tcp://139.59.170.74:1883");
		configuration.setMqttClientId("JavaSample");
		adapter = new MqttAdapter(configuration, new MqttCommandCallback(system));
		
		initGui();
	}
	
	
    private JButton mqttConnect;
	
	

    private void initGui() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        getContentPane().setLayout(null);
        modeUI();
        setVisible(true);
    }    
 
    private void modeUI() {

    	mqttConnect = new JButton("MQTT Connect");
    	mqttConnect.setBounds(100, 300, 100, 50);
    	mqttConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        		adapter.startup();
            }
        });    	
    	
        getContentPane().add(mqttConnect);
    }  
    
    public static void main(String[] args) {
        // write your code here   	
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);   
    	new MqttSystemUI();

    }
    
}
