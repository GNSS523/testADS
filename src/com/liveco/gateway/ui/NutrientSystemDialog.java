package com.liveco.gateway.ui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.liveco.gateway.constant.NutrientSystemConstant;
import com.liveco.gateway.constant.SystemStructure;
import com.liveco.gateway.plc.ADSConnection;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.NurientSystem;
import com.liveco.gateway.system.SystemRepository;

import javax.swing.UIManager;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Vector;

import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JSeparator;

public class NutrientSystemDialog extends JPanel {

    private static final Logger LOG = LogManager.getLogger(NutrientSystemDialog.class);
	
	
	private NurientSystem system;
    private static SystemRepository respository;	
	
	/**
	 * Create the panel.
	 */
	
    JButton sv1_btn,sv2_btn,sv3_btn,sv4_btn,sv5_btn,sv6_btn, pump_btn, uv_led_btn;
    JLabel sv1_status_label,sv2_status_label,sv3_status_label,sv4_status_label,sv5_status_label,sv6_status_label,
    pump_status_label,uv_led_status_label;
    JLabel water_level_label, ph_label, ec_label;
    
    private String SV1_value,SV2_value,SV3_value,SV4_value,SV5_value,SV6_value;
    private String uv_led_value, pump_value,meter_value;        
    String mode_value;	
    
    JComboBox modeSelect ;
	
	
	private void initGUI(){
		setLayout(null);
				
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 182, 587, 262);
		add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Pump", null, panel_1, null);
		panel_1.setLayout(null);
		
		JPanel panel_14 = new JPanel();
		panel_14.setLayout(null);
		panel_14.setBounds(10, 24, 277, 41);
		panel_1.add(panel_14);
		
		//////////////////////////////////////
		JLabel lblSv = new JLabel("SV5:");
		lblSv.setBounds(21, 14, 46, 15);
		panel_14.add(lblSv);
		
		JLabel sv5_status_label = new JLabel("ON");
		sv5_status_label.setBounds(77, 14, 52, 15);
		panel_14.add(sv5_status_label);
		
		JButton sv5_btn = new JButton("ON");
		sv5_btn.setBounds(139, 10, 93, 23);
		panel_14.add(sv5_btn);
		
		
		sv5_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV5_value == "ON"){
						system.close("actuator.valve",5);
						SV5_value = "OFF";
						closingState(sv5_status_label,sv5_btn);
	            	}else if(SV5_value == "OFF"){
						system.open("actuator.valve",5);
						SV5_value = "ON";
						openingState(sv5_status_label,sv5_btn);
	            	}else{
	            		showError("System not connected", false);
	            		sv5_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         }); 		
		//////////////////////////////////////////
		
		JPanel panel_15 = new JPanel();
		panel_15.setLayout(null);
		panel_15.setBounds(10, 75, 277, 41);
		panel_1.add(panel_15);
		
		////////////////////////////////////////////
		JLabel lblSv_1 = new JLabel("SV6:");
		lblSv_1.setBounds(21, 14, 45, 15);
		panel_15.add(lblSv_1);
		
		 sv6_status_label = new JLabel("ON");
		sv6_status_label.setBounds(76, 14, 52, 15);
		panel_15.add(sv6_status_label);
		
		 sv6_btn = new JButton("ON");
		sv6_btn.setBounds(138, 10, 93, 23);
		panel_15.add(sv6_btn);
		sv6_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV6_value == "ON"){
						system.close("actuator.valve",6);
						SV6_value = "OFF";
						closingState(sv6_status_label,sv6_btn);
	            	}else if(SV6_value == "OFF"){
						system.open("actuator.valve",6);
						SV6_value = "ON";
						openingState(sv6_status_label,sv6_btn);
	            	}else{
	            		showError("System not connected", false);
	            		sv6_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         }); 		
		////////////////////////////////////////////
		
		JPanel panel_16 = new JPanel();
		panel_16.setLayout(null);
		panel_16.setBounds(297, 24, 277, 41);
		panel_1.add(panel_16);
		
		JLabel lblNg = new JLabel("NG1:");
		lblNg.setBounds(21, 14, 46, 15);
		panel_16.add(lblNg);
		
		 uv_led_status_label = new JLabel("ON");
		uv_led_status_label.setBounds(77, 14, 52, 15);
		panel_16.add(uv_led_status_label);
		
		 uv_led_btn = new JButton("ON");
		uv_led_btn.setBounds(139, 10, 93, 23);
		panel_16.add(uv_led_btn);
		
		uv_led_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(uv_led_value == "ON"){
						system.close("actuator.uv_led",1);
						uv_led_value = "OFF";
						closingState(uv_led_status_label,uv_led_btn);
	            	}else if(uv_led_value == "OFF"){
						system.open("actuator.uv_led",1);
						uv_led_value = "ON";
						openingState(uv_led_status_label,uv_led_btn);
	            	}else{
	            		showError("System not connected", false);
	            		sv6_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         }); 		
		
		//////////////////////////////////////////////
		
		JPanel panel_17 = new JPanel();
		panel_17.setLayout(null);
		panel_17.setBounds(297, 75, 277, 41);
		panel_1.add(panel_17);
		
		JLabel lblPump = new JLabel("Pump:");
		lblPump.setBounds(21, 14, 43, 15);
		panel_17.add(lblPump);
		
		 pump_status_label = new JLabel("ON");
		pump_status_label.setBounds(74, 14, 52, 15);
		panel_17.add(pump_status_label);
		
		 pump_btn = new JButton("ON");
		pump_btn.setBounds(136, 10, 93, 23);
		panel_17.add(pump_btn);
		
		pump_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(pump_value == "ON"){
						system.close("actuator.pump",1);
						pump_value = "OFF";
						closingState(pump_status_label,pump_btn);
	            	}else if(pump_value == "OFF"){
						system.open("actuator.pump",1);
						pump_value = "ON";
						openingState(pump_status_label,pump_btn);
	            	}else{
	            		showError("System not connected", false);
	            		pump_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         }); 		
		///////////////////////////////////////////////////////////
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 134, 564, 2);
		panel_1.add(separator);
		
		JPanel mixing_panel = new JPanel();
		tabbedPane.addTab("Mixing", null, mixing_panel, null);
		mixing_panel.setLayout(null);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(3, 5, 550, 218);
		panel_6.setLayout(null);
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Led Control Setting", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mixing_panel.add(panel_6);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBounds(10, 21, 253, 36);
		panel_6.add(panel_7);
		
		JLabel lblReceipe = new JLabel("receipe");
		lblReceipe.setToolTipText("red color");
		lblReceipe.setForeground(Color.DARK_GRAY);
		lblReceipe.setBounds(0, 6, 42, 15);
		panel_7.add(lblReceipe);
		
		JSlider receipe_slide1 = new JSlider();
		receipe_slide1.setBounds(40, 6, 183, 26);
		panel_7.add(receipe_slide1);
		
		JLabel receipe_status_label1 = new JLabel("0");
		receipe_status_label1.setHorizontalAlignment(SwingConstants.TRAILING);
		receipe_status_label1.setBounds(225, 6, 28, 15);
		panel_7.add(receipe_status_label1);
		
		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBounds(10, 67, 253, 36);
		panel_6.add(panel_8);
		
		JLabel label_4 = new JLabel("blue");
		label_4.setToolTipText("blue color");
		label_4.setForeground(Color.DARK_GRAY);
		label_4.setBounds(0, 11, 33, 15);
		panel_8.add(label_4);
		
		JSlider receipe_slide2 = new JSlider();
		receipe_slide2.setBounds(43, 5, 182, 26);
		panel_8.add(receipe_slide2);
		
		JLabel receipe_status_label2 = new JLabel("0");
		receipe_status_label2.setHorizontalAlignment(SwingConstants.TRAILING);
		receipe_status_label2.setBounds(225, 11, 28, 15);
		panel_8.add(receipe_status_label2);
		
		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBounds(10, 113, 253, 36);
		panel_6.add(panel_9);
		
		JLabel label_6 = new JLabel("green");
		label_6.setToolTipText("red color");
		label_6.setForeground(Color.DARK_GRAY);
		label_6.setBounds(0, 11, 43, 15);
		panel_9.add(label_6);
		
		JSlider receipe_slide3 = new JSlider();
		receipe_slide3.setBounds(40, 5, 185, 26);
		panel_9.add(receipe_slide3);
		
		JLabel receipe_status_label3 = new JLabel("0");
		receipe_status_label3.setHorizontalAlignment(SwingConstants.TRAILING);
		receipe_status_label3.setBounds(225, 11, 28, 15);
		panel_9.add(receipe_status_label3);
		
		JPanel panel_10 = new JPanel();
		panel_10.setLayout(null);
		panel_10.setBounds(10, 152, 253, 36);
		panel_6.add(panel_10);
		
		JLabel label_8 = new JLabel("far red");
		label_8.setToolTipText("red color");
		label_8.setForeground(Color.DARK_GRAY);
		label_8.setBounds(0, 11, 42, 15);
		panel_10.add(label_8);
		
		JSlider receipe_slide4 = new JSlider();
		receipe_slide4.setBounds(42, 5, 183, 26);
		panel_10.add(receipe_slide4);
		
		JLabel receipe_status_label4 = new JLabel("0");
		receipe_status_label4.setHorizontalAlignment(SwingConstants.TRAILING);
		receipe_status_label4.setBounds(225, 11, 28, 15);
		panel_10.add(receipe_status_label4);
		
		JPanel panel_11 = new JPanel();
		panel_11.setLayout(null);
		panel_11.setBounds(273, 21, 253, 36);
		panel_6.add(panel_11);
		
		JLabel label_10 = new JLabel("uv");
		label_10.setToolTipText("uv color");
		label_10.setForeground(Color.DARK_GRAY);
		label_10.setBounds(10, 5, 34, 15);
		panel_11.add(label_10);
		
		JSlider receipe_slide5 = new JSlider();
		receipe_slide5.setBounds(44, 5, 181, 26);
		panel_11.add(receipe_slide5);
		
		JLabel receipe_status_label5 = new JLabel("0");
		receipe_status_label5.setHorizontalAlignment(SwingConstants.TRAILING);
		receipe_status_label5.setBounds(225, 11, 28, 15);
		panel_11.add(receipe_status_label5);
		
		JPanel panel_12 = new JPanel();
		panel_12.setLayout(null);
		panel_12.setBounds(273, 67, 253, 36);
		panel_6.add(panel_12);
		
		JLabel label_12 = new JLabel("uv");
		label_12.setToolTipText("uv color");
		label_12.setForeground(Color.DARK_GRAY);
		label_12.setBounds(10, 5, 34, 15);
		panel_12.add(label_12);
		
		JSlider receipe_slide6 = new JSlider();
		receipe_slide6.setBounds(44, 5, 181, 26);
		panel_12.add(receipe_slide6);
		
		JLabel receipe_status_label6 = new JLabel("0");
		receipe_status_label6.setHorizontalAlignment(SwingConstants.TRAILING);
		receipe_status_label6.setBounds(225, 11, 28, 15);
		panel_12.add(receipe_status_label6);
		
		JPanel panel_13 = new JPanel();
		panel_13.setLayout(null);
		panel_13.setBounds(273, 113, 253, 36);
		panel_6.add(panel_13);
		
		JLabel label_14 = new JLabel("uv");
		label_14.setToolTipText("uv color");
		label_14.setForeground(Color.DARK_GRAY);
		label_14.setBounds(10, 5, 34, 15);
		panel_13.add(label_14);
		
		JSlider receipe_slide8 = new JSlider();
		receipe_slide8.setBounds(44, 5, 181, 26);
		panel_13.add(receipe_slide8);
		
		JLabel receipe_status_label8 = new JLabel("0");
		receipe_status_label8.setHorizontalAlignment(SwingConstants.TRAILING);
		receipe_status_label8.setBounds(225, 11, 28, 15);
		panel_13.add(receipe_status_label8);
		
		JButton fillRecipeBtn = new JButton("give recipe");
		fillRecipeBtn.setBounds(345, 165, 93, 23);
		panel_6.add(fillRecipeBtn);
		
		JPanel send_panel = new JPanel();
		tabbedPane.addTab("Test", null, send_panel, null);
		send_panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(29, 31, 277, 41);
		send_panel.add(panel_2);
		
		/////////////////////////////////////
		JLabel label_1 = new JLabel("SV2:");
		label_1.setBounds(21, 14, 46, 15);
		panel_2.add(label_1);
		
		sv2_status_label = new JLabel("ON");
		sv2_status_label.setBounds(77, 14, 52, 15);
		panel_2.add(sv2_status_label);
		
		sv2_btn = new JButton("ON");
		sv2_btn.setBounds(139, 10, 93, 23);
		panel_2.add(sv2_btn);
		
		sv2_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV2_value == "ON"){
						system.close("actuator.valve",2);
						SV2_value = "OFF";
						closingState(sv2_status_label,sv2_btn);
	            	}else if(SV2_value == "OFF"){
						system.open("actuator.valve",2);
						SV2_value = "ON";
						openingState(sv2_status_label,sv2_btn);
	            	}else{
	            		showError("System not connected", false);
	            		sv2_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         });		
		
		////////////////////////////////////////
		JPanel panel_18 = new JPanel();
		panel_18.setLayout(null);
		panel_18.setBounds(29, 82, 277, 41);
		send_panel.add(panel_18);
		
		sv3_status_label = new JLabel("SV3:");
		sv3_status_label.setBounds(21, 14, 45, 15);
		panel_18.add(sv3_status_label);
		
		JLabel label_5 = new JLabel("ON");
		label_5.setBounds(76, 14, 52, 15);
		panel_18.add(label_5);
		
		sv3_btn = new JButton("ON");
		sv3_btn.setBounds(138, 10, 93, 23);
		panel_18.add(sv3_btn);
		
		sv3_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV3_value == "ON"){
						system.close("actuator.valve",3);
						SV3_value = "OFF";
						closingState(sv3_status_label,sv3_btn);
	            	}else if(SV3_value == "OFF"){
						system.open("actuator.valve",3);
						SV3_value = "ON";
						openingState(sv3_status_label,sv3_btn);
	            	}else{
	            		showError("System not connected", false);
	            		sv3_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         });		
		////////////////////////////////////////
		JPanel panel_19 = new JPanel();
		panel_19.setLayout(null);
		panel_19.setBounds(29, 133, 277, 41);
		send_panel.add(panel_19);
		
		JLabel label_7 = new JLabel("SV4:");
		label_7.setBounds(21, 14, 45, 15);
		panel_19.add(label_7);
		
		sv4_status_label = new JLabel("ON");
		sv4_status_label.setBounds(76, 14, 52, 15);
		panel_19.add(sv4_status_label);
		
		sv4_btn = new JButton("ON");
		sv4_btn.setBounds(138, 10, 93, 23);
		panel_19.add(sv4_btn);
		
		sv4_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV4_value == "ON"){
						system.close("actuator.valve",4);
						SV4_value = "OFF";
						closingState(sv4_status_label,sv4_btn);
	            	}else if(SV4_value == "OFF"){
						system.open("actuator.valve",4);
						SV4_value = "ON";
						openingState(sv4_status_label,sv4_btn);
	            	}else{
	            		showError("System not connected", false);
	            		sv4_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         });		
		////////////////////////////////////////
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 16, 614, 39);
		add(panel_3);
		panel_3.setLayout(null);
		
		
		
		//////////////////////////////////////////////////
		JLabel lblMode = new JLabel("Mode:");
		lblMode.setBounds(10, 10, 72, 15);
		panel_3.add(lblMode);

		
		
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(NutrientSystemConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(NutrientSystemConstant.ModeCommand.MANUAL));
        vs.add(String.valueOf(NutrientSystemConstant.ModeCommand.SEMI_AUTOMATIC));		
		modeSelect = new JComboBox(vs);
		modeSelect.setBounds(78, 7, 119, 22);
		panel_3.add(modeSelect);
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                setMode(mode);
            }
        });		
		
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(10, 84, 204, 41);
		add(panel_4);
		panel_4.setLayout(null);
		
		
		///////////////////////////////////
		JLabel label = new JLabel("SV1:");
		label.setBounds(10, 14, 45, 15);
		panel_4.add(label);
		
		sv1_status_label = new JLabel("ON");
		sv1_status_label.setBounds(64, 14, 36, 15);
		panel_4.add(sv1_status_label);
		
		sv1_btn = new JButton("ON");
		sv1_btn.setBounds(100, 10, 72, 23);
		panel_4.add(sv1_btn);
		
		sv1_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {             	
	            	if(SV1_value == "ON"){
						system.close("actuator.valve",1);
						SV1_value = "OFF";
						closingState(sv1_status_label,sv1_btn);
	            	}else if(SV1_value == "OFF"){
						system.open("actuator.valve",1);
						SV1_value = "ON";
						openingState(sv1_status_label,sv1_btn);
	            	}else{
	            		showError("System not connected", false);
	            		sv1_btn.setBackground(Color.gray);
	            	}
	            	
				} catch (AdsException  e1) {
					showError(e1.getErrMessage(), false);
					e1.printStackTrace();
				} catch (DeviceTypeException e1) {
					e1.printStackTrace();
				} catch(NullPointerException e1){
					showError("System not connected", false);
				}             	            	
            }
         });		
		
		
		////////////////////////////////////
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(142, 131, 137, 41);
		add(panel_5);
		panel_5.setLayout(null);
		
		JLabel aaa = new JLabel("Water level:");
		aaa.setBounds(10, 16, 80, 15);
		panel_5.add(aaa);
		
		
		
		 water_level_label = new JLabel("ON");
		water_level_label.setBounds(88, 16, 53, 15);
		panel_5.add(water_level_label);		
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(10, 131, 122, 41);
		add(panel);
		
		JLabel lblPh = new JLabel("PH:");
		lblPh.setBounds(32, 16, 35, 15);
		panel.add(lblPh);
		
		 ph_label = new JLabel("ON");
		ph_label.setBounds(66, 16, 53, 15);
		panel.add(ph_label);
		
		JPanel EC = new JPanel();
		EC.setLayout(null);
		EC.setBounds(289, 131, 116, 41);
		add(EC);
		
		JLabel lblEc = new JLabel("EC:");
		lblEc.setBounds(24, 16, 42, 15);
		EC.add(lblEc);
		
		 ec_label = new JLabel("ON");
		ec_label.setBounds(66, 16, 53, 15);
		EC.add(ec_label);
	}
	
	public NutrientSystemDialog() {
		initGUI();

	}
	
    public NutrientSystemDialog(SystemRepository respository) {
        this.respository = respository;
        
        initGUI();
        refreshSystemStatus(0);
    } 
    
    public void addSystems(SystemRepository respository){
        this.respository = respository;
        initGUI();
        refreshSystemStatus(0);             
    }
    
    private void refreshSystemStatus(int index){
    	
    	try{		
	        system = (NurientSystem) respository.findSystem(SystemStructure.NUTRIENT_SYSTEM,   0);
		}catch(java.lang.IndexOutOfBoundsException e){
			showError("System Not Found",false);
			e.printStackTrace();
		}
    	
        this.system.test();
        
        //respository.getPHNotification("G_HMI.stConfig.rPHActualValue");
        //respository.getECNotification("G_HMI.stConfig.rECActualValue");
        
        // refresh the system
    	system.refreshSystemStatus();
		
		mode_value = system.getSysteModeValue();
		modeSelect.setSelectedItem(mode_value);
		
		//valve1_value = system.getInletValveValue();
		//if(valve1_value == "ON") toClose(valveStatus1,valveOn1); else toOpen(valveStatus1,valveOn1);
		
		SV1_value = system.getValveValue(1);
		SV2_value = system.getValveValue(2);
		SV3_value = system.getValveValue(3);
		SV4_value = system.getValveValue(4);
		SV5_value = system.getValveValue(5);
		SV6_value = system.getValveValue(6);
		
		
		modeSelect.setSelectedItem(mode_value);
		
		pump_value = system.getPumpValue();
		if(pump_value == "ON") closingState(pump_status_label,pump_btn);  else openingState(pump_status_label,pump_btn);        
		uv_led_value = system.getUVLEDValue();
		if(uv_led_value == "ON") closingState(uv_led_status_label,uv_led_btn);  else openingState(uv_led_status_label,uv_led_btn);        
		meter_value = system.getMeterValue();
		//if(meter_value == "ON") toClose(meterStatus,meterOn);  else toOpen(meterStatus,meterOn); 
		
		System.out.println(mode_value+"   ");
		System.out.println(mode_value+"   "+pump_value+"  "+uv_led_value+"    "+meter_value);
		System.out.println(SV1_value+"   "+SV2_value+"  "+SV3_value+"    "+SV4_value+" "+SV5_value+"  "+SV6_value);
		
		
		
    } 
    
    
    private void setMode(String mode) {
        if (Objects.equals(mode, NutrientSystemConstant.ModeCommand.AUTOMATIC)) {
            try {
				system.configMode("config.system.mode", NutrientSystemConstant.ModeCommand.AUTOMATIC);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
            
        } else if (Objects.equals(mode, NutrientSystemConstant.ModeCommand.MANUAL)) {
            try {
				system.configMode("config.system.mode", NutrientSystemConstant.ModeCommand.MANUAL);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        } else if (Objects.equals(mode, NutrientSystemConstant.ModeCommand.SEMI_AUTOMATIC)) {
            try {
				system.configMode("config.system.mode", NutrientSystemConstant.ModeCommand.SEMI_AUTOMATIC);
			} catch (AdsException e) {
				showError(e.getErrMessage(), false);
				e.printStackTrace();
			}
        }   
    }    
    
    private void closingState(JLabel status_component, JButton control_component){
    	status_component.setText("OFF");
    	control_component.setText("ON");
    	status_component.setBackground(Color.white);    	
    }
    
    private void openingState(JLabel status_component, JButton control_component){
    	status_component.setText("ON");
    	control_component.setText("OFF");
    	status_component.setBackground(Color.green);
    }

    private void errorState(JLabel status_component, JButton control_component){
    	status_component.setText("ON");
    	control_component.setText("OFF");
    	status_component.setBackground(Color.red);
    }    
    
    private void setEnabled(){
       	
    }
    
    private void setDisabled(){
    	
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
    
    
    
    public static void main(String args[]) {
    	
 	   	Logger logger = Logger.getLogger("com.liveco.gateway");
 	   	logger.setLevel(Level.DEBUG);        	
    	
 	   NutrientSystemDialog UI = new NutrientSystemDialog();

 	   	
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("NurientSystem");
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

 	    main_frame.getContentPane().add(UI);
 	    
 	    
 	    SystemRepository repository = null;
		try {
	 	   	ADSConnection ads = new ADSConnection();
	 	   	ads.openPort(true,"5.42.203.215.1.1",851);
			UI.addSystems(new SystemRepository(ads));

		} catch (AdsException e1) {
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
