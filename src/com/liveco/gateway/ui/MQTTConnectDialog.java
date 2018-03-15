package com.liveco.gateway.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.liveco.gateway.mqtt.MqttAdapter;
import com.liveco.gateway.mqtt.MqttAdapterConfiguration;
import com.liveco.gateway.mqtt.MqttCommandCallback;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.system.SystemRepository;
 
// This class displays the dialog used for creating messages.
public class MQTTConnectDialog extends JDialog {
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Dialog message identifiers.
    public static final int NEW = 0;
    public static final int REPLY = 1;
    public static final int FORWARD = 2;
     
    // Message from, to and subject text fields.
    private JTextField mqttAddressTextField, mqttIDTextField;
    private JTextField subjectTextField;
     
    // Message content text area.
    private JTextArea contentTextArea;
     
    // Flag specifying whether or not dialog was cancelled.
    private boolean cancelled;   
    
	MqttAdapterConfiguration configuration;
	MqttAdapter adapter; 
	SystemRepository repository;
     
    // Constructor for dialog.
    public MQTTConnectDialog(Frame parent, int type, SystemRepository repository)
    throws Exception {
        // Call super constructor, specifying that dialog is modal.
        super(parent, true);
        
        setTitle("New Message"); 
        this.repository = repository;	   	
     
        // Handle closing events.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionCancel();
            }
        });
         
        setUI( parent);
    }
    
    private void setUI(Frame parent){
    	
    	String to="", subject="", content = "";
    	
        // Setup fields panel.
        JPanel fieldsPanel = new JPanel();
        GridBagConstraints constraints;
        GridBagLayout layout = new GridBagLayout();
        fieldsPanel.setLayout(layout);
        JLabel fromLabel = new JLabel("MQTT Address");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(5, 5, 0, 0);
        layout.setConstraints(fromLabel, constraints);
        fieldsPanel.add(fromLabel);
        mqttAddressTextField = new JTextField();
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(5, 5, 0, 0);
        layout.setConstraints(mqttAddressTextField, constraints);
        mqttAddressTextField.setText("tcp://139.59.170.74:1883");
        fieldsPanel.add(mqttAddressTextField);
        
        JLabel toLabel = new JLabel("MQTT name");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(5, 5, 0, 0);
        layout.setConstraints(toLabel, constraints);
        fieldsPanel.add(toLabel);
        mqttIDTextField = new JTextField(to);
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(5, 5, 0, 0);
        constraints.weightx = 1.0D;
        layout.setConstraints(mqttIDTextField, constraints);
        mqttIDTextField.setText("JavaSample");
        fieldsPanel.add(mqttIDTextField);
        
        JLabel subjectLabel = new JLabel("Message:");
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 0);
        layout.setConstraints(subjectLabel, constraints);
        //fieldsPanel.add(subjectLabel);
        subjectTextField = new JTextField(subject);
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(5, 5, 5, 0);
        layout.setConstraints(subjectTextField, constraints);
        //fieldsPanel.add(subjectTextField);
         
        // Setup content panel.
        JScrollPane contentPanel = new JScrollPane();
        contentTextArea = new JTextArea(content, 10, 70);
        contentPanel.setViewportView(contentTextArea);
         
        // Setup buttons panel.
        JPanel buttonsPanel = new JPanel();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionSend();
            }
        });
        buttonsPanel.add(sendButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });
        buttonsPanel.add(cancelButton);
         
        // Add panels to display.
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(fieldsPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
         
        // Size dialog to components.
        pack();
         
        // Center dialog over application.
        setLocationRelativeTo(parent);    	
    }
     
    // Validate message fields and close dialog.
    private void actionSend() {
        if (mqttAddressTextField.getText().trim().length() < 1
                || mqttIDTextField.getText().trim().length() < 1
                //|| subjectTextField.getText().trim().length() < 1
                //|| contentTextArea.getText().trim().length() < 1
                ) {
            JOptionPane.showMessageDialog(this,
                    "One or more fields is missing.",
                    "Missing Field(s)", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String netid = mqttAddressTextField.getText().trim();
        String mqttID =  mqttIDTextField.getText().trim();
        
 	    configuration = new MqttAdapterConfiguration();
 	    configuration.setMqttBroker(netid);
		configuration.setMqttClientId(mqttID);
		adapter = new MqttAdapter(configuration, new MqttCommandCallback(repository)); 
    	adapter.getConfiguration().setMqttBroker(netid);
    	adapter.getConfiguration().setMqttClientId(mqttID);
		adapter.startup(); 	    
        // Close dialog.
        //dispose();
    }
     
    // Cancel creating this message and close dialog.
    private void actionCancel() {
        cancelled = true;
         
        // Close dialog.
        dispose();
    }
     
    // Show dialog.
    public boolean display() {
        show();    
        // Return whether or not display was successful.
        return !cancelled;
    }
     
    // Get message's "NetID" field value.
    public String getMqttAddress() {
        return mqttAddressTextField.getText();
    }
     
    // Get message's "Port" field value.
    public String getMqttID() {
        return mqttIDTextField.getText();
    }
    
    // Get ADSConnection" field value.
    public MqttAdapter getAdapter() {
        return adapter;
    }    
    
    public MqttAdapterConfiguration getConfiguration(){
    	return configuration;
    }
     
    // Get message's "Subject" field value.
    public String getSubject() {
        return subjectTextField.getText();
    }
     
    // Get message's "content" field value.
    public String getContent() {
        return contentTextArea.getText();
    }
}
