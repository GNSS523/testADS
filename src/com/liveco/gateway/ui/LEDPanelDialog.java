package com.liveco.gateway.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;


import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.PanelLightingSystem;
import com.liveco.gateway.system.SystemRepository;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class LEDPanelDialog extends JDialog {
    private static final Logger LOG = LogManager.getLogger(LEDPanelDialog.class);

	private final JPanel contentPanel = new JPanel();

	private PanelLightingSystem system;
    private  SystemRepository respository;

    private JButton okButton;
    private JButton allonBtn;
    private JButton alloffBtn;

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

    JList led_list;
    DefaultListModel led_list_model;
    int index;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);
		try {
	 	   	ADSConnection ads = new ADSConnection();
	 	   	ads.openPort(true,"5.42.203.215.1.1",851);
			LEDPanelDialog dialog = new LEDPanelDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.addSystems(new SystemRepository(ads));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void initGUI(){
		setBounds(100, 100, 520, 459);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Led Control Setting", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_5.setBounds(219, 97, 275, 283);
		contentPanel.add(panel_5);
		panel_5.setLayout(null);

		JPanel red_panel = new JPanel();
		red_panel.setBounds(10, 31, 253, 26);
		panel_5.add(red_panel);
		red_panel.setLayout(null);

		JLabel red = new JLabel("red");
		red.setBounds(0, 6, 30, 15);
		red.setToolTipText("red color");
		red.setForeground(Color.DARK_GRAY);
		red_panel.add(red);

		slider_red = new JSlider();
		slider_red.setBounds(42, 0, 183, 26);
		red_panel.add(slider_red);

		label_red = new JLabel("0");
		label_red.setBounds(225, 6, 16, 15);
		label_red.setHorizontalAlignment(SwingConstants.TRAILING);
		red_panel.add(label_red);

		JPanel blue_panel = new JPanel();
		blue_panel.setBounds(10, 67, 253, 36);
		panel_5.add(blue_panel);
		blue_panel.setLayout(null);

		JLabel lblBlue = new JLabel("blue");
		lblBlue.setBounds(0, 11, 33, 15);
		lblBlue.setToolTipText("blue color");
		lblBlue.setForeground(Color.DARK_GRAY);
		blue_panel.add(lblBlue);

		slider_blue = new JSlider();
		slider_blue.setBounds(43, 5, 182, 26);
		blue_panel.add(slider_blue);

		label_blue = new JLabel("0");
		label_blue.setBounds(225, 11, 18, 15);
		label_blue.setHorizontalAlignment(SwingConstants.TRAILING);
		blue_panel.add(label_blue);

		JPanel green_panel = new JPanel();
		green_panel.setBounds(10, 113, 253, 36);
		panel_5.add(green_panel);
		green_panel.setLayout(null);

		JLabel lblGreen = new JLabel("green");
		lblGreen.setBounds(0, 11, 43, 15);
		lblGreen.setToolTipText("red color");
		lblGreen.setForeground(Color.DARK_GRAY);
		green_panel.add(lblGreen);

		slider_green = new JSlider();
		slider_green.setBounds(40, 5, 185, 26);
		green_panel.add(slider_green);

		label_green = new JLabel("0");
		label_green.setBounds(225, 11, 18, 15);
		label_green.setHorizontalAlignment(SwingConstants.TRAILING);
		green_panel.add(label_green);

		JPanel far_red_panel = new JPanel();
		far_red_panel.setBounds(10, 152, 253, 36);
		panel_5.add(far_red_panel);
		far_red_panel.setLayout(null);

		JLabel lblFarRed = new JLabel("far red");
		lblFarRed.setBounds(0, 11, 42, 15);
		lblFarRed.setToolTipText("red color");
		lblFarRed.setForeground(Color.DARK_GRAY);
		far_red_panel.add(lblFarRed);

		slider_far_red = new JSlider();
		slider_far_red.setBounds(42, 5, 183, 26);
		far_red_panel.add(slider_far_red);

		label_far_red = new JLabel("0");
		label_far_red.setBounds(225, 11, 18, 15);
		label_far_red.setHorizontalAlignment(SwingConstants.TRAILING);
		far_red_panel.add(label_far_red);

		JPanel uv_panel = new JPanel();
		uv_panel.setBounds(10, 198, 253, 36);
		panel_5.add(uv_panel);
		uv_panel.setLayout(null);

		JLabel lblUv = new JLabel("uv");
		lblUv.setBounds(10, 5, 34, 15);
		lblUv.setToolTipText("uv color");
		lblUv.setForeground(Color.DARK_GRAY);
		uv_panel.add(lblUv);

		slider_uv = new JSlider();
		slider_uv.setBounds(44, 5, 181, 26);
		uv_panel.add(slider_uv);

		label_uv = new JLabel("0");
		label_uv.setBounds(225, 11, 18, 15);
		label_uv.setHorizontalAlignment(SwingConstants.TRAILING);
		uv_panel.add(label_uv);
		{
			okButton = new JButton("Set");
			okButton.setBounds(141, 248, 100, 23);
			panel_5.add(okButton);

			okButton.addActionListener(new ActionListener() {
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

			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Group Control Setting", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_6.setBounds(10, 10, 484, 67);
		contentPanel.add(panel_6);
		panel_6.setLayout(null);

		 allonBtn = new JButton("All Spectrum ON");
		allonBtn.setActionCommand("OK");
		allonBtn.setBounds(167, 32, 139, 23);
		panel_6.add(allonBtn);

		 alloffBtn = new JButton("All Spectrum OFF");
		alloffBtn.setActionCommand("Cancel");
		alloffBtn.setBounds(316, 32, 139, 23);
		panel_6.add(alloffBtn);

		allonBtn.addActionListener(new ActionListener() {
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

		alloffBtn.addActionListener(new ActionListener() {
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



		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "LED lights", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(10, 97, 199, 283);
		contentPanel.add(panel_7);
		panel_7.setLayout(null);

		led_list_model = new DefaultListModel();


		led_list = new JList(led_list_model);
		led_list.setBounds(10, 22, 179, 251);

    JScrollPane sp = sp = new JScrollPane(led_list);
    sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		led_list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println(e.getFirstIndex()+"  "+e.getLastIndex());

			    index = e.getLastIndex();
			    LOG.debug("   "+"   "+index);
				refreshSystemStatus(index);
			}

		});
		panel_7.add(led_list);

        // Size dialog to components.

	}

	/**
	 * Create the dialog.
	 */
	public LEDPanelDialog() {
		initGUI();
	}

    public LEDPanelDialog(SystemRepository respository) {
        this.respository = respository;
        initGUI();
        index = 0;
        refreshSystemStatus(index);
    }

    public void addSystems(SystemRepository respository){
        this.respository = respository;
        index = 0;
        refreshSystemStatus(index);
    }

    private void refreshSystemStatus(int index){
    	try{
	        this.system = (PanelLightingSystem) respository.findSystem(SystemStructure.PANEL_LIGHTING_SYSTEM,  index );

        	int number = respository.getPanelLightingSystems().size();

	        for (int i=0;i< number;i++){
	        	led_list_model.addElement(""+i);  // "panel system"+
	        }

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

    private void setSlideMode(JSlider slider, JLabel label, Rectangle rec , Rectangle rec2){
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        label.setBounds(rec);
        slider.setBounds(rec2);
        getContentPane().add(label);
        getContentPane().add(slider);
    }
}
