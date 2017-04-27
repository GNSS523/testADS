package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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



public class MainUI  extends JPanel{

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem_plc;
	JMenuItem menuItem_mqtt;
	JMenuItem menuItem_exit;
	
	
	public MainUI(){
        super();
        
        BorderLayout bord = new BorderLayout();
        setLayout(bord);
        addToolBarPane();
        addTabbedPane();
		
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
	
	
	private void addToolBarPane(){
		
		JToolBar bar = new JToolBar();
        JButton button_plc = new JButton("PLC");
        JButton button_mqtt = new JButton("MQTT");

        button_plc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
            	String response = JOptionPane.showInputDialog(null,
           			 "What is your name?",
           			 "Enter your name",
           			 JOptionPane.QUESTION_MESSAGE);            	
           
            	
            }
        });
		
        button_mqtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
            	String response = JOptionPane.showInputDialog(null,
              			 "What is your name?",
              			 "Enter your name",
              			 JOptionPane.QUESTION_MESSAGE);
            	
            	
            }
        });		
        
        bar.add(button_plc);
        bar.add(button_mqtt);
        
        
        add("North",bar);
		
	}
	
	private void addTabbedPane(){
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Production area 1", panel1);
        
        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Production area 2", panel2);
        
        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("Production area 3", panel3);
        
        JComponent panel4 = makeTextPanel(
                "Experiment area");
        panel4.setPreferredSize(new Dimension(1200, 600));
        tabbedPane.addTab("Experiment area", panel4);
        
        //Add the tabbed pane to this panel.
        add("Center",tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);		
	}
	
	
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }	
    
    private void showError(String message, boolean exit) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        if (exit)
            System.exit(0);
    }    
	
    public static void main(String[] args) {
        

 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(1200, 800);
 	    main_frame.setTitle("Main System");
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
 	    	    
 	    MainUI mainui = new MainUI();
 	   // main_frame.setJMenuBar(mainui.addMenu());
 	    main_frame.add(mainui);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
          	   	main_frame.pack();
            	main_frame.setVisible(true);
            }
        });     
    }		
}
