package com.liveco.gateway.ui;

import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.PanelLightingSystem;
import com.liveco.gateway.system.SystemRepository;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class PaneLightingSystemUI extends JPanel {

    private static final Logger LOG = LogManager.getLogger(PaneLightingSystemUI.class);

	private PanelLightingSystem system;
    private  SystemRepository respository;

    private JComboBox lightsComboBox;
    private int number;

    private JLabel plc;
    private JButton on;
    private JButton off;
    private JButton allon;
    private JButton alloff;

    JSlider slider_red = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
    JSlider slider_blue = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
    JSlider slider_green = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
    JSlider slider_uv = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
    JSlider slider_far_red = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);

    JLabel label_red = new JLabel("red");
    JLabel label_blue = new JLabel("blue");
    JLabel label_green = new JLabel("green");
    JLabel label_uv = new JLabel("uv");
    JLabel label_far_red = new JLabel("far_red");

    public PaneLightingSystemUI() {

        initGui();
    }

    public PaneLightingSystemUI(SystemRepository respository) {
        this.respository = respository;
        initGui();
        refreshSystemStatus(0);
    }

    public void addSystems(SystemRepository respository){
        this.respository = respository;
        refreshSystemStatus(0);
        systemListUI();
    }

    private void refreshSystemStatus(int index){
    	try{
	        this.system = (PanelLightingSystem) respository.findSystem(SystemStructure.PANEL_LIGHTING_SYSTEM,  index );
		}catch(java.lang.IndexOutOfBoundsException e){
			showError("System Not Found",false);
			e.printStackTrace();
		}

        this.system.test();
    	try {
			    int values[] = this.system.getLightIntensity();
			    System.out.println(this.system.getSystemId()+" init:  "+values[0]+" "+values[1]+" "+values[2]+"��"+values[3]+" "+values[4]);
        	slider_red.setValue(values[0]);
        	slider_blue.setValue(values[1]);
        	slider_green.setValue(values[2]);
        	slider_uv.setValue(values[3]);
        	slider_far_red.setValue(values[4]);
		} catch (AdsException | DeviceTypeException e) {
			showError(e.getMessage(),false);

			e.printStackTrace();
		}
    }


    private void initGui() {
        setLayout(null);
        modeUI();
        controlUI();
        systemListUI();
        setVisible(true);
    }

    private void setSlideMode(JSlider slider, JLabel label, Rectangle rec , Rectangle rec2){
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        label.setBounds(rec);
        slider.setBounds(rec2);
        add(label);
        add(slider);
    }

    private void modeUI(){
    	allon = new JButton("All ON");
    	allon.setBounds(170,350,100,50);

    	allon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ArrayList list = respository.getPanelLightingSystems();
            	for(int i=0;i < list.size();i++){

            		 PanelLightingSystem temp_system = (PanelLightingSystem) list.get(i);
                     try {
                    	 temp_system.setLightIntensity(100,100,100,100,100);
      	     				} catch (AdsException | DeviceTypeException e1) {
      	     					e1.printStackTrace();
      	     				}
            	}
            	slider_red.setValue(100);
            	slider_blue.setValue(100);
            	slider_green.setValue(100);
            	slider_uv.setValue(100);
            	slider_far_red.setValue(100);

            }
        });
    	alloff = new JButton("All OFF");
    	alloff.setBounds(260,350,100,50);

    	alloff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ArrayList list = respository.getPanelLightingSystems();
            	for(int i=0;i < list.size();i++){

            		 PanelLightingSystem temp_system = (PanelLightingSystem) list.get(i);
                     try {
                    	 temp_system.setLightIntensity(0,0,0,0,0);
        	     				} catch (AdsException | DeviceTypeException e1) {
        	     					e1.printStackTrace();
        	     				}
            	}

            	slider_red.setValue(0);
            	slider_blue.setValue(0);
            	slider_green.setValue(0);
            	slider_uv.setValue(0);
            	slider_far_red.setValue(0);
            }
        });

        add(allon);
        add(alloff);
    }

    private void controlUI() {

    	setSlideMode(slider_red,label_red,         new Rectangle(10,100,50,40), new Rectangle(70,100,400,40));
    	setSlideMode(slider_blue,label_blue,       new Rectangle(10,150,50,40), new Rectangle(70,150,400,40));
    	setSlideMode(slider_green,label_green,     new Rectangle(10,200,50,40), new Rectangle(70,200,400,40));
    	setSlideMode(slider_uv,label_uv,           new Rectangle(10,250,50,40), new Rectangle(70,250,400,40));
    	setSlideMode(slider_far_red,label_far_red, new Rectangle(10,300,50,40), new Rectangle(70,300,400,40));

        on = new JButton("Set Value");
        on.setBounds(170,10,100,50);

        on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int red = slider_red.getValue();
                int blue = slider_blue.getValue();
                int green = slider_green.getValue();
                int uv = slider_uv.getValue();
                int far_red = slider_far_red.getValue();

                System.out.println(red+"  "+blue+"  "+green+" "+uv+" "+far_red);

                try {
					system.setLightIntensity(red,blue,green,uv,far_red);
				} catch (AdsException | DeviceTypeException e1) {
					e1.printStackTrace();
				}
            }
        });
        add(on);
    }



    private void systemListUI(){
    	if(respository!=null){
        	number = respository.getPanelLightingSystems().size();

          System.out.println("init ligt panel system ui  "+ number);

	        Vector<String> vs = new Vector<String>();
	        for (int i=0;i< number;i++){
	            vs.add(""+i);  // "panel system"+
	        }

	        lightsComboBox = new JComboBox(vs);
	        lightsComboBox.setBounds(10,10,100,30);

	        lightsComboBox.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	int index = lightsComboBox.getSelectedIndex();
	                String name = "" + lightsComboBox.getItemAt(index);

	                System.out.println( name +"  "+ index  );


	                refreshSystemStatus(index);

	            }
	        });

	        add(lightsComboBox);

    	}
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

 	    PaneLightingSystemUI UI = new PaneLightingSystemUI();


 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("PaneLightingSystemUI");
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
			// TODO Auto-generated catch block
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
