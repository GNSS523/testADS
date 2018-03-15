package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.system.ShelfLightingSystem;
import com.liveco.gateway.system.SystemRepository;
import javax.swing.JList;

public class ShelfLightingPanel extends JPanel {
    private static final Logger LOG = LogManager.getLogger(ShelfLightingPanel.class);
	
	
	private ShelfLightingSystem left_system, right_system;
    private static SystemRepository respository;
    
    private int left_index, right_index;
    
    
    private JButton allon;
    private JButton alloff;
    JList left_shelf_list;
    JList right_shelf_list;
    
    private ArrayList<JButton> left_list_ui;
    private ArrayList<JButton> right_list_ui;
    LedActionListener ledListener;
    
    
    public void initGUI(){
		setLayout(null);
		
		allon = new JButton("All On");
		allon.setBounds(42, 10, 93, 23);
		add(allon);
    	allon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	try {
            		left_system.controlAllOn();
            		right_system.controlAllOn();
				} catch (AdsException e) {
    				showError(e.getErrMessage(), false);
					e.printStackTrace();
				}
            	
                for (int i= 0; i < left_system.getLightNumber(); i++) {
                	
                 }            	
           	
            }
        });
		
		alloff = new JButton("All Off");
		alloff.setBounds(232, 10, 93, 23);
		add(alloff);
    	alloff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	try {
            		left_system.controlAllOff();
            		right_system.controlAllOff();
				} catch (AdsException e) {
    				showError(e.getErrMessage(), false);					
					e.printStackTrace();
				}
            	
                for (int i= 0; i < left_system.getLightNumber(); i++) {
                	//checkBoxes[0][i].setSelected(false);
                 } 

            }
        });		
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 42, 430, 248);
		add(panel);
		
		 left_shelf_list = new JList();
		panel.add(left_shelf_list);
		
		 right_shelf_list = new JList();
		panel.add(right_shelf_list);    
		
		
    }
    
	/**
	 * Create the panel.
	 */
	public ShelfLightingPanel() {
		initGUI();

	}   
    
    public ShelfLightingPanel(SystemRepository respository, int left_index, int right_index) {
    	
    	initGUI();
    	
        this.respository = respository;
        this.left_index = left_index;
        this.right_index = right_index;
        refreshSystemStatus(left_index, right_index);
        
    } 
    
    
    public void addSystems(SystemRepository respository, int left_index, int right_index){
        this.respository = respository;
        this.left_index = left_index;
        this.right_index = right_index;        
        refreshSystemStatus(left_index, right_index);

        
    }
    
    public void refreshSystemStatus( int left_index, int right_index){
		
    	try{
    		left_system = (ShelfLightingSystem) respository.findSystem(SystemStructure.SHELF_LIGHTING_SYSTEM,   left_index);
    		right_system = (ShelfLightingSystem) respository.findSystem(SystemStructure.SHELF_LIGHTING_SYSTEM,   right_index);
    	}catch(java.lang.IndexOutOfBoundsException e){
    		showError("System Not Found",false);
    		e.printStackTrace();
    	}
    	
        this.left_system.test();
        this.right_system.test();
        
        reset();
        
        LOG.debug("shelf lighting size : "+ respository.getShelfLightingSystems().size()  );
        System.out.println("left "+this.left_system.getLightNumber()+" right:"+this.right_system.getLightNumber());
        System.out.println(this.left_system.getLightingStatus()+"   "+this.right_system.getLightingStatus());
        byte [] left_status = left_system.getLightingStatus();
        byte [] right_status = right_system.getLightingStatus();
        left_list_ui = new ArrayList();
        right_list_ui  = new ArrayList();
        ledListener = new LedActionListener();
        
        
        
        /*
        for(int i=0;i<this.left_system.getLightNumber();i++){
        	JButton btn = new JButton();
        	this.add(btn);
        	btn.addActionListener(ledListener);
        	left_list_ui.add(btn);
        	if(left_status[i] == 0){
        		btn.setBackground(Color.red);
        	}else if(left_status[i]==1){
        		btn.setBackground(Color.green);        		
        	}
        }

        for(int i=0;i<this.right_system.getLightNumber();i++){
        	JButton btn = new JButton();
        	this.add(btn);
        	btn.addActionListener(ledListener);
        	right_list_ui.add(btn); 
        	if(right_status[i] == 0){
        		btn.setBackground(Color.red);        		
        	}else if(right_status[i]==1){
        		btn.setBackground(Color.green);        		
        	}        	
        }      
        */
        updateShelfUI(left_status,left_list_ui, 0);
        updateShelfUI(right_status,right_list_ui, 1);

    }

    void updateShelfListUI( byte []status, JList list_ui, int num){
    	list_ui.removeAll();
        for(int i=0;i<status.length;i++){

        	if(status[i] == 0){
        		//list_ui.add("On");       		
        	}else if(status[i]==1){
        		   		
        	}        	
        }    	
    }    
    
    void updateShelfUI( byte []status, ArrayList list_ui, int num){
        for(int i=0;i<status.length;i++){
        	JButton btn = new JButton();
        	this.add(btn);
        	btn.addActionListener(ledListener);
        	btn.setName(num+"_"+(int)status[i]);
        	right_list_ui.add(btn); 
        	if(status[i] == 0){
        		btn.setBackground(Color.red);        		
        	}else if(status[i]==1){
        		btn.setBackground(Color.green);        		
        	}        	
        }    	
    }
    
    void reset(){
    	if(left_list_ui !=null) {
            for(int i=0;i<left_list_ui.size();i++){
            	JButton btn = left_list_ui.get(i);
            	btn.removeActionListener(ledListener);
            	left_list_ui.remove(btn);
            }    		
    	}
    	if(right_list_ui !=null) {
            for(int i=0;i<right_list_ui.size();i++){
            	JButton btn = right_list_ui.get(i);
            	btn.removeActionListener(ledListener);
            	right_list_ui.remove(btn);       	
            }
    	}	

        left_list_ui = null;
        right_list_ui  = null;
        ledListener = null;
    }
    
    
      class LedActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
        	
        	JButton btn = (JButton)event.getSource();
        	String name = btn.getName();
        	String arr[] = name.split("_");
        	int shelf_num = Integer.parseInt(arr[0]), index_num = Integer.parseInt(arr[1]);
        	ShelfLightingSystem system = null;
        	
        	if(shelf_num == 0){
        		system = left_system;
        	}else{
        		system = right_system;
        	}
        	
        	try {
        		system.open(index_num);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);					
				e.printStackTrace();
			}
        	
        }
    }
    
    
    private void showError(String message, boolean exit) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        if (exit)
            System.exit(0);        
    }     
}
