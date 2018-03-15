package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.constant.AirConditionerConstant.FanCommand;
import com.liveco.gateway.constant.AirConditionerConstant.WorkCommand;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.AirConditioner;
import com.liveco.gateway.system.CO2System;
import com.liveco.gateway.system.SystemRepository;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.JLabel;

public class AirConditionerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private AirConditioner system;
    private SystemRepository respository;
    private int index;	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AirConditionerDialog dialog = new AirConditionerDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public AirConditionerDialog() {
		initGUI();
	}

	public AirConditionerDialog(SystemRepository respository,int index) {
		initGUI();
        this.index = index;
		refreshSystemStatus(0); 		
	}
	
    public void addSystems(SystemRepository respository,int index){
        this.respository = respository;
        this.index = index;
        refreshSystemStatus(index);
    }
    
	public AirConditionerDialog(Frame parent) {
		super(parent, true);
	    		
		initGUI();
	}

	public AirConditionerDialog(Frame parent,SystemRepository respository,int index) {
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
	        system = (AirConditioner) respository.findSystem(SystemStructure.AIR_CONDITIONING_SYSTEM,  index );
	        System.out.println("------------------------------------------------"+SystemStructure.AIR_CONDITIONING_SYSTEM+"    "+  index);
		}catch(java.lang.IndexOutOfBoundsException e){
					
			e.printStackTrace();
		}        
        
        system.test();
        /*
        try {

	        System.out.println(" air conditioner system mode    "+ system.getWorkMode() +"    " + system.getFanMode()   );
		} catch (AdsException e) {
			e.printStackTrace();
		} catch (DeviceTypeException e) {
			e.printStackTrace();
		}
		*/
     
    }     
	

	JRadioButton working_close_btn,working_cold_btn,working_dry_btn,working_dehumidity_btn,working_heat_btn;
	JRadioButton fan_close_btn,fan_low_btn,fan_medium_btn,fan_high_btn,fan_auto_btn;
	FanModeActionListener fanListener;
	WorkingModeActionListener workingListener;
	
	/**
	 * Create the dialog.
	 */
	private void initGUI(){ 
		
		setTitle("Air Conditoner System");
		setBounds(100, 100, 450, 393);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		 this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("windows closing ");
            }
        });			
		
		setBounds(100, 100, 631, 375);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblWorkingMode = new JLabel("Working Mode");
		lblWorkingMode.setBounds(20, 11, 80, 24);
		
		ButtonGroup group = new ButtonGroup();
		 working_close_btn = new JRadioButton("Close");
		working_close_btn.setBounds(20, 42, 109, 23);
		 working_cold_btn = new JRadioButton("Cold");
		working_cold_btn.setBounds(131, 42, 109, 23);
		 working_dry_btn = new JRadioButton("Dry");
		working_dry_btn.setBounds(242, 42, 109, 23);
		 working_dehumidity_btn = new JRadioButton("Dehumidity");
		working_dehumidity_btn.setBounds(353, 42, 109, 23);
		 working_heat_btn = new JRadioButton("Heat");
		working_heat_btn.setBounds(464, 42, 109, 23);
		
		 workingListener = new WorkingModeActionListener();
		
		working_close_btn.addActionListener(workingListener);
		working_cold_btn.addActionListener(workingListener);
		working_dry_btn.addActionListener(workingListener);
		working_dehumidity_btn.addActionListener(workingListener);
		working_heat_btn.addActionListener(workingListener);
		
		group.add(working_close_btn);
		group.add(working_cold_btn);
		group.add(working_dry_btn);
		group.add(working_dehumidity_btn);
		group.add(working_heat_btn);
		contentPanel.add(working_close_btn);
		contentPanel.add(working_cold_btn);
		contentPanel.add(working_dry_btn);
		contentPanel.add(working_dehumidity_btn);
		contentPanel.add(working_heat_btn);
		contentPanel.add(lblWorkingMode);		
		
		///////////////////////////////////////////////////
		
		ButtonGroup group2 = new ButtonGroup();
		
		JLabel label = new JLabel("Fan Mode");
		label.setBounds(20, 71, 80, 24);		
		 fan_close_btn = new JRadioButton("Close");
		fan_close_btn.setBounds(20, 102, 109, 23);
		
		 fan_low_btn = new JRadioButton("Low speed");
		fan_low_btn.setBounds(131, 102, 109, 23);
		
		 fan_medium_btn = new JRadioButton("Medium speed");
		fan_medium_btn.setBounds(242, 102, 109, 23);
		
		 fan_high_btn = new JRadioButton("High speed");
		fan_high_btn.setBounds(353, 102, 109, 23);
		
		 fan_auto_btn = new JRadioButton("Automatic");
		fan_auto_btn.setBounds(464, 102, 109, 23);
		
		 fanListener = new FanModeActionListener();

		fan_close_btn.addActionListener(fanListener);
		fan_low_btn.addActionListener(fanListener);
		fan_medium_btn.addActionListener(fanListener);
		fan_high_btn.addActionListener(fanListener);
		fan_auto_btn.addActionListener(fanListener);		
		group2.add(fan_close_btn);
		group2.add(fan_low_btn);
		group2.add(fan_medium_btn);
		group2.add(fan_high_btn);
		group2.add(fan_auto_btn);
		contentPanel.add(fan_close_btn);
		contentPanel.add(fan_low_btn);
		contentPanel.add(fan_medium_btn);
		contentPanel.add(fan_high_btn);
		contentPanel.add(fan_auto_btn);
		

		

		contentPanel.add(label);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	class WorkingModeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.out.println("working mode");
        	
        	JRadioButton btn = (JRadioButton)e.getSource();
        	try {        	
	        	if(btn == working_close_btn) {
					system.setWorkMode("command.set.control.mode",WorkCommand.OFF);
	        	}else if(btn ==working_cold_btn ) {
					system.setWorkMode("command.set.control.mode",WorkCommand.COLD);
	        	}else if(btn == working_dry_btn) {
					system.setWorkMode("command.set.control.mode",WorkCommand.DRY);
	        	}else if(btn == working_dehumidity_btn) {
					system.setWorkMode("command.set.control.mode",WorkCommand.DEHUMIDITY);
	        	}else if(btn == working_heat_btn) {
					system.setWorkMode("command.set.control.mode",WorkCommand.HOT);	        		
	        	}
			} catch (AdsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }	
	
	class FanModeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	System.out.println("fan mode"); 
        	
        	
        	JRadioButton btn = (JRadioButton)e.getSource();
        	try {
	        	if(btn == fan_close_btn) {
	    			system.setFanMode("command.set.fan.mode",FanCommand.OFF);	
	        	}else if(btn == fan_low_btn) {
	    			system.setFanMode("command.set.fan.mode",FanCommand.LOW);
	        	}else if(btn == fan_medium_btn) {
	    			system.setFanMode("command.set.fan.mode",FanCommand.MIDDLE);	
	        	}else if(btn == fan_high_btn) {
	    			system.setFanMode("command.set.fan.mode",FanCommand.HIGH);	
	        	}else if(btn == fan_auto_btn) {
	    			system.setFanMode("command.set.fan.mode",FanCommand.AUTO);		        		
	        	}       	
			} catch (AdsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }	
}
