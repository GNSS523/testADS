package com.liveco.gateway.ui;

import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.system.ShelfLightingSystem;
import com.liveco.gateway.system.SystemRepository;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 * http://stackoverflow.com/questions/15732194/how-to-use-jcheckbox-array-in-jpanel-array
 */
public class ShelfLightingSystemUI extends JPanel {

    private static final Logger LOG = LogManager.getLogger(ShelfLightingSystemUI.class);
	
	
	private ShelfLightingSystem system;
    private static SystemRepository respository;

    private JComboBox lightsComboBox;
    private int system_number;    
    
    private JButton allon;
    private JButton alloff;
    
    
   
    private int light_number = 0;
    private JCheckBox[][] checkBoxes = new JCheckBox[1][6];
    
    private static final int GAP = 0;
    private ItemListener itemListener = new MyCheckBoxListener();

    
    public ShelfLightingSystemUI() {
        initGui();
            
    }    

    public ShelfLightingSystemUI(ShelfLightingSystem system) {
        this.system = system;
        initGui();
            
    }
    
    public ShelfLightingSystemUI(SystemRepository respository) {
        this.respository = respository;
        
        //refreshSystemStatus(0);
        initGui();
    } 
    
    public void addSystems(SystemRepository respository){
        this.respository = respository;
        refreshSystemStatus(0);
        JPanel systemPanel = systemListUI();
        head.add(systemPanel, BorderLayout.WEST);
        JPanel unitPanel = unitControlUI();
        add(unitPanel, BorderLayout.SOUTH );
    }
    
    private void refreshSystemStatus(int index){
		
    	try{
    		system = (ShelfLightingSystem) respository.findSystem(SystemStructure.SHELF_LIGHTING_SYSTEM,   index);
    	}catch(java.lang.IndexOutOfBoundsException e){
    		showError("System Not Found",false);
    		e.printStackTrace();
    	}
    	
        this.system.test();
        
        LOG.debug("shelf lighting size : "+ respository.getShelfLightingSystems().size()  );
        
        light_number = system.getLightNumber();
        
        LOG.debug("each shelf lighting has  : "+ light_number +" lights"  );

    }     
    JPanel head;
    private void initGui() {
        setLayout(new BorderLayout());
                
        JPanel systemPanel = systemListUI();
        JPanel unitPanel = unitControlUI();
        JPanel groupPanel = groupControlUI();
        
         head= new JPanel();
        head.setLayout(new BorderLayout());
        head.add(systemPanel, BorderLayout.WEST);
        head.add(groupPanel, BorderLayout.EAST);        
        
        add(head, BorderLayout.NORTH );
        add(unitPanel, BorderLayout.SOUTH );
        
        setVisible(true);
    }

    private JPanel unitControlUI() {
   	
        // Setup settings panel.
        JPanel panel = new JPanel();
        
        panel.setBorder(BorderFactory.createTitledBorder("Control Settings"));
       // panel. setLayout(new GridLayout(light_number, 1, GAP, GAP));

        for (int cbCol = 0; cbCol < light_number; cbCol++) {
              JCheckBox checkBox = new JCheckBox();
              checkBox.addItemListener(itemListener);
              panel.add(checkBox);
              checkBoxes[0][cbCol] = checkBox;
        }
       
        if(system!=null)  refreshUnitStatus();
        
        return panel;
    }
    
    private void refreshUnitStatus(){
    	byte status_list[] = system.getLightingStatus();    	
    	System.out.println( checkBoxes.length +"  "+ checkBoxes[0].length + checkBoxes[0][0].isSelected() );    	
		for(int i=0; i< light_number;i++){
			if(status_list[i]==0)
				checkBoxes[0][i].setSelected(false);
			else 
				checkBoxes[0][i].setSelected(true);			
		}    	
    }
    
    private JPanel groupControlUI(){
    	
    	JPanel panel = new JPanel();
    	
    	allon = new JButton("All ON");
    	//allon.setBounds(170,650,100,50);

    	allon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	try {
					system.controlAllOn();
				} catch (AdsException e) {
    				showError(e.getErrMessage(), false);
					e.printStackTrace();
				}
            	
                for (int i= 0; i < system.getLightNumber(); i++) {
                	checkBoxes[0][i].setSelected(true);
                 }            	
           	
            }
        });
    	alloff = new JButton("All OFF");
    	//alloff.setBounds(260,650,100,50);

    	alloff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	try {
					system.controlAllOff();
				} catch (AdsException e) {
    				showError(e.getErrMessage(), false);					
					e.printStackTrace();
				}
            	
                for (int i= 0; i < system.getLightNumber(); i++) {
                	checkBoxes[0][i].setSelected(false);
                 } 

            }
        });

    	panel.add(allon);
    	panel.add(alloff); 
    	return panel;
    }    

    private class MyCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent itemEvt) {
           JCheckBox source = (JCheckBox) itemEvt.getSource();
           boolean selected = source.isSelected();
           int cbRow = -1;
           int cbCol = -1;
           for (int r = 0; r < checkBoxes.length; r++) {
              for (int c = 0; c < checkBoxes[r].length; c++) {
                 if (source.equals(checkBoxes[r][c])) {
                    cbRow = r;
                    cbCol = c;
                 }
              }
           }

           int index = cbCol;
           
           if(selected){
        	   
	           try {
	        	   system.open(index);
	        	   LOG.debug("index "+index+"    open");
				} catch (AdsException e) {
    				showError(e.getErrMessage(), false);					
					e.printStackTrace();
				}
	           
           }else{
        	   
               try {
            	   system.close(index);
            	   LOG.debug("index "+index+"    close");            	   
    			} catch (AdsException e) {
    				showError(e.getErrMessage(), false);
    				e.printStackTrace();
    			}     
           }  
        }
     }
    
    private JPanel systemListUI(){
    	
    	JPanel panel = new JPanel();
    	
    	if(respository!=null){   
    		LOG.debug("add repository");
        	system_number = respository.getShelfLightingSystems().size();
        	
            Vector<String> vs = new Vector<String>();
            for (int i=0;i< system_number;i++){
                vs.add(""+i);  // "panel system"+    
                LOG.debug("add repository  "+i);
            }
            
            lightsComboBox = new JComboBox(vs);
            lightsComboBox.setBounds(10,10,100,30);
                         
            lightsComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	int index = lightsComboBox.getSelectedIndex();
                    String name = "" + lightsComboBox.getItemAt(index);
                    
                    
                    
                    refreshSystemStatus(index);
                    refreshUnitStatus();
                }
            });
            panel.add(lightsComboBox);    		
    	}
  
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
				respository.getADSConnection().closePort();
			} catch (AdsException e) {
				e.printStackTrace();
			}finally{
				System.exit(0);
			}
		    
			
		}else if(option == JOptionPane.CANCEL_OPTION){
			
		}
    	
    	
    }   
    
    public static void main(String[] args) {
    	
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG); 
 	   	

 	    ShelfLightingSystemUI UI = new ShelfLightingSystemUI(); 	    
        
        
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(400, 300);
 	    main_frame.setTitle("CO2 system");
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
 	    main_frame.setLayout(new GridLayout(3, 3, GAP, GAP));

 	    
		try {
	 	   	ADSConnection ads = new ADSConnection();
	 	   	ads.openPort(true,"5.42.203.215.1.1",851);			
			
			UI.addSystems(new SystemRepository(ads));
		} catch (AdsException e1) {
			LOG.debug("FUCK YOU HERE");
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