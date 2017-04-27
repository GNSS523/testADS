package com.liveco.gateway.ui;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.constant.PanelLightingConstant;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.PanelLightingSystem;

import javax.swing.*;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class PaneLightingSystemUI extends JPanel {
    private PanelLightingSystem system;
    private String curmode;
    
    
    private JComboBox panelLights;
    private int number;
    
    
    private JLabel plc;
    private JButton on;
    private JButton off;
    
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

    public PaneLightingSystemUI(PanelLightingSystem system, int number) {
        this.system = system;
        this.number = number;
        initGui();

    }

    private void initGui() {
        setLayout(null);
        modeUI();
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
    
    private void modeUI() {
    	
    	setSlideMode(slider_red,label_red,         new Rectangle(10,50,50,40), new Rectangle(70,50,400,40));
    	setSlideMode(slider_blue,label_blue,       new Rectangle(10,100,50,40), new Rectangle(70,100,400,40));
    	setSlideMode(slider_green,label_green,     new Rectangle(10,150,50,40), new Rectangle(70,150,400,40));
    	setSlideMode(slider_uv,label_uv,           new Rectangle(10,200,50,40), new Rectangle(70,200,400,40));
    	setSlideMode(slider_far_red,label_far_red, new Rectangle(10,250,50,40), new Rectangle(70,250,400,40));
    	
        Vector<String> vs = new Vector<String>();
        for (int i=0;i< number;i++){
            vs.add("panel system"+i);        	
        }
        
        panelLights = new JComboBox(vs);
        panelLights.setBounds(10,10,150,70);
             
        // 设置模式
        panelLights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String m = "" + panelLights.getItemAt(panelLights.getSelectedIndex());
                
                System.out.println( m +"  "+ panelLights.getSelectedIndex()  );
                

            }
        });
        
        
        on = new JButton("Set Value");
        on.setBounds(170,10,80,50);

        on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                on.setEnabled(false);
                System.out.println(curmode);

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

        add(panelLights);
        add(on);
        
        
    }

    private void setMode(String m) {
        curmode = m;
        if (m == PanelLightingConstant.Table.BLUE.getDescritpion()) {
            plc.setText(String.valueOf(PanelLightingConstant.Table.BLUE.getOffset()));
        } else if (m == PanelLightingConstant.Table.RED.getDescritpion()) {
            plc.setText(String.valueOf(PanelLightingConstant.Table.RED.getOffset()));
        } else if (m == PanelLightingConstant.Table.GREEN.getDescritpion()) {
            plc.setText(String.valueOf(PanelLightingConstant.Table.GREEN.getOffset()));
        } else if (m == PanelLightingConstant.Table.FAR_RED.getDescritpion()) {
            plc.setText(String.valueOf(PanelLightingConstant.Table.FAR_RED.getOffset()));
        } else if (m == PanelLightingConstant.Table.GREEN.getDescritpion()) {
            plc.setText(String.valueOf(PanelLightingConstant.Table.GREEN.getOffset()));
        }
    }
    
    
    public static void main(String[] args) {
        // write your code here
        PanelLightingSystem s = new PanelLightingSystem(null, 0, "af");

    
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
                System.exit(1);
            }
        });
 	     	    
 	    main_frame.add(new PaneLightingSystemUI(s,18));
 	    
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
