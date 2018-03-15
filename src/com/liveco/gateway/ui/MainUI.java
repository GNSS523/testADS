package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.system.SystemRepository;

public class MainUI extends JFrame{

	private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(MainUI.class);
	
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem_plc;
	JMenuItem menuItem_mqtt;
	JMenuItem menuItem_exit;
	
	SystemRepository repository;
	PLCConnectDialog plc_dialog;
	MQTTConnectDialog mqtt_dialog;
	
	public MainUI(SystemRepository repository){
        super();
        
        this.repository = repository;
                
        initGui();
        
        connectPLC();	    
	}
	
	public void initGui(){
        BorderLayout bord = new BorderLayout();
        setLayout(bord);
        addToolBarPane();
        addTabbedPane();
        
 	    setSize(800, 600);
 	    setTitle("NurientSystem");
 	    setLocationRelativeTo(null);
 	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 	    setResizable(true);
 	    addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                showExit();
                //System.exit(1);
            }
        });   
 	    
 	   LOG.debug("what the fucks");
 	   //addMenu();		
	}
	
	public JMenuBar addMenu(){

		menuBar = new JMenuBar();
		menu = new JMenu("A Menu");
		menuBar.add(menu);

		//a group of JMenuItems
		menuItem_plc = new JMenuItem("Connect PLC");		                         
		menu.add(menuItem_plc);
		
		menuItem_plc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
            	JOptionPane.showMessageDialog(null, "I am happy.");            	
            }
        });


		menuItem_mqtt = new JMenuItem("Connect MQTT");		                         
		menu.add(menuItem_mqtt);		
		
		menuItem_mqtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
            	String response = JOptionPane.showInputDialog(null,
            			 "What is your name?",
            			 "Enter your name",
            			 JOptionPane.QUESTION_MESSAGE);            	
            }
        });

		
		menuItem_exit = new JMenuItem("Exit");
		menu.add(menuItem_exit);
		
		menuItem_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
                JOptionPane.showConfirmDialog(null,
                        "I really like my book",
                        "Question (application-modal dialog)",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);            	
            }
        });


		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);

		return menuBar;
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
	
	
	private void addToolBarPane(){
		
		JToolBar bar = new JToolBar();
        JButton button_plc = new JButton("PLC");
        JButton button_mqtt = new JButton("MQTT");

        button_plc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	/*
            	String response = JOptionPane.showInputDialog(null,
           			 "What is your PLC netID?",
           			 "Enter your netID",
           			 JOptionPane.QUESTION_MESSAGE);            	
           
            	System.out.println(response);
            	*/
            	connectPLC();            	
            	System.out.println("connectPLC  result  "  +repository.getSystemStatus());          	
            }
        });
		
        button_mqtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
            	String response = JOptionPane.showInputDialog(null,
              			 "What is your mqtt address?",
              			 "Enter your mqtt address",
              			 JOptionPane.QUESTION_MESSAGE);
            	
            	System.out.println(response);
            	*/
            	connectMQTT();
            	System.out.println("connectMQTT  result");
            	
            }
        });		
        
        bar.add(button_plc);
        bar.add(button_mqtt);
        
        
        add("North",bar);
		
	}
	
	private void addTabbedPane(){
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JComponent panel1 = new PaneLightingSystemUI();
        tabbedPane.addTab("Production area 1", panel1);
        
        JComponent panel2 = new JPanel();
        JPanel shelf = new ShelfLightingSystemUI();
        JPanel CO2 = new CO2SystemUI();
        //panel2.setLayout(null);
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER,2,2));
        panel2.add(shelf);
        panel2.add(new ShelfLightingSystemUI());
        panel2.add(new ShelfLightingSystemUI());
        //panel2.add(CO2);
        tabbedPane.addTab("Production area 2", panel2);
        
        JComponent panel3 =  new FogponicsSystem_UI();
        tabbedPane.addTab("Production area 3", panel3);
        
        JComponent panel4 = new HydroponicsSystem_UI();
        //panel4.setLayout(new FlowLayout(FlowLayout.CENTER,2,2));
        //panel4.setLayout(new GridLayout(0, 3, 0, 0));
        //panel4.setLayout(new BorderLayout());
        tabbedPane.addTab("Experiment area", panel4);
        
        tabbedPane.setPreferredSize(new Dimension(1200,600));
        
        add("Center",tabbedPane);
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);		
	}
	
	
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(4, 1));
        panel.add(filler);
        return panel;
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
				this.repository.getADSConnection().closePort();
			} catch (AdsException e) {
				e.printStackTrace();
			}
			System.exit(0);
			
		}else if(option == JOptionPane.CANCEL_OPTION){
			
		}
    	
    	
    }      
	
    public static void main(String[] args) {
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);         	    	    
 	    MainUI mainui = new MainUI(new SystemRepository());
 	   // main_frame
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	mainui.pack();
            	mainui.setVisible(true);
            }
        });     
    }		
}
