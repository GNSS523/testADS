package com.liveco.gateway.ui;

import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.constant.NutrientSystemConstant;
import com.liveco.gateway.system.NurientSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Created by halsn on 17-4-2.
 */
public class NurientSystemUI extends JPanel{
    private NurientSystem system;
    // PH传感器
    private JLabel phName;
    private JLabel phPLC;
    private JLabel phValue;
    // EC传感器
    private JLabel ecName;
    private JLabel ecPLC;
    private JLabel ecValue;
    // PE流量计
    private JLabel peName;
    private JLabel pePLC;
    private JLabel peValue;
    // meter水阀
    private JLabel meterName;
    private JLabel meterPLC;
    private JButton meterOn;
    private JButton meterOff;
    private JLabel meterStatus;
    // mode
    private JComboBox mode;
    private JLabel curmode;
    // 控制状态
    private JButton start;
    private JButton stop;
    private JButton config;
    private JLabel configStatus;
    // 一个小水泵
    private JLabel pumpName;
    private JLabel pumpPLC;
    private JButton pumpOn;
    private JButton pumpOff;
    private JLabel pumpStatus;
    // 6个循环水阀
    private Vector<JLabel> valveName;
    private Vector<JLabel> valvePLC;
    private Vector<JButton> valveOn;
    private Vector<JButton> valveOff;
    private Vector<JLabel> valveStstus;
    // 1个LEd杀菌灯
    private JLabel ledName;
    private JLabel ledPLC;
    private JButton ledOn;
    private JButton ledOff;
    private JLabel ledStatus;

    
    public NurientSystemUI(NurientSystem system) {
        this.system = system;
        initGui();
    }

    private void initGui() {
        setLayout(null);
        phUI();
        ecUI();
        peUI();
        meterUI();
        modeUI();
        configUI();
        pumpUI();
        valveUI();
        ledUI();
        setVisible(true);
    }    
    
    private void ledUI() {
        // where is led???
        ledName = new JLabel("led name: " + NutrientSystemConstant.Table.PUMP.getDescritpion());
        ledPLC = new JLabel("led PLC: " + String.valueOf(NutrientSystemConstant.Table.PUMP.getOffset()));
        ledOn = new JButton("led on");
        ledOff = new JButton("led off");
        ledStatus = new JLabel();
        ledName.setBounds(0, 650, 200, 50);
        ledPLC.setBounds(200,650,200,50);
        ledOn.setBounds(400, 650, 100, 50);
        ledOff.setBounds(500, 650, 100, 50);
        ledStatus.setBounds(600, 650, 200, 50);
        ledOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ledOn.setEnabled(false);
                ledOff.setEnabled(true);
                // TODO:获取系统状态
                ledStatus.setText("ON");
            }
        });
        ledOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ledOff.setEnabled(false);
                ledOn.setEnabled(true);
                // TODO:获取系统状态
                ledStatus.setText("Off");
            }
        });
        add(ledName);
        add(ledPLC);
        add(ledOn);
        add(ledOff);
        add(ledStatus);
    }

    private void valveUI() {
        valveName = new Vector<JLabel>();
        valvePLC = new Vector<JLabel>();
        valveOn = new Vector<JButton>();
        valveOff = new Vector<JButton>();
        valveStstus = new Vector<JLabel>();
        JLabel j1 = new JLabel("valve NO1 name: " + NutrientSystemConstant.FillInCommand.NO1.getDescritpion());
        JLabel j2 = new JLabel("valve NO2 name: " + NutrientSystemConstant.FillInCommand.NO2.getDescritpion());
        JLabel j3 = new JLabel("valve NO3 name: " + NutrientSystemConstant.FillInCommand.NO3.getDescritpion());
        JLabel j4 = new JLabel("valve NO4 name: " + NutrientSystemConstant.FillInCommand.NO4.getDescritpion());
        JLabel j5 = new JLabel("valve NO5 name: " + NutrientSystemConstant.FillInCommand.NO5.getDescritpion());
        JLabel j6 = new JLabel("valve NO6 name: " + NutrientSystemConstant.FillInCommand.NO6.getDescritpion());
        valveName.add(j1);
        valveName.add(j2);
        valveName.add(j3);
        valveName.add(j4);
        valveName.add(j5);
        valveName.add(j6);
        JLabel p1 = new JLabel("valve NO1 PLC: " + String.valueOf(NutrientSystemConstant.FillInCommand.NO1.getValue()));
        JLabel p2 = new JLabel("valve NO2 PLC: " + String.valueOf(NutrientSystemConstant.FillInCommand.NO2.getValue()));
        JLabel p3 = new JLabel("valve NO3 PLC: " + String.valueOf(NutrientSystemConstant.FillInCommand.NO3.getValue()));
        JLabel p4 = new JLabel("valve NO4 PLC: " + String.valueOf(NutrientSystemConstant.FillInCommand.NO4.getValue()));
        JLabel p5 = new JLabel("valve NO5 PLC: " + String.valueOf(NutrientSystemConstant.FillInCommand.NO5.getValue()));
        JLabel p6 = new JLabel("valve NO6 PLC: " + String.valueOf(NutrientSystemConstant.FillInCommand.NO6.getValue()));
        valvePLC.add(p1);
        valvePLC.add(p2);
        valvePLC.add(p3);
        valvePLC.add(p4);
        valvePLC.add(p5);
        valvePLC.add(p6);
        JButton on1 = new JButton("NO 1 On");
        JButton on2 = new JButton("NO 2 On");
        JButton on3 = new JButton("NO 3 On");
        JButton on4 = new JButton("NO 4 On");
        JButton on5 = new JButton("NO 5 On");
        JButton on6 = new JButton("NO 6 On");
        JButton off1 = new JButton("NO 1 Off");
        JButton off2 = new JButton("NO 2 Off");
        JButton off3 = new JButton("NO 3 Off");
        JButton off4 = new JButton("NO 4 Off");
        JButton off5 = new JButton("NO 5 Off");
        JButton off6 = new JButton("NO 6 Off");
        valveOn.add(on1);
        valveOn.add(on2);
        valveOn.add(on3);
        valveOn.add(on4);
        valveOn.add(on5);
        valveOn.add(on6);
        valveOff.add(off1);
        valveOff.add(off2);
        valveOff.add(off3);
        valveOff.add(off4);
        valveOff.add(off5);
        valveOff.add(off6);
        JLabel s1 = new JLabel("");
        JLabel s2 = new JLabel("");
        JLabel s3 = new JLabel("");
        JLabel s4 = new JLabel("");
        JLabel s5 = new JLabel("");
        JLabel s6 = new JLabel("");
        valveStstus.add(s1);
        valveStstus.add(s2);
        valveStstus.add(s3);
        valveStstus.add(s4);
        valveStstus.add(s5);
        valveStstus.add(s6);
        for(int i = 0;i < 6;i++) {
            int finalI = i;
            valveOn.get(i).addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   valveOn.get(finalI).setEnabled(false);
                   valveOff.get(finalI).setEnabled(true);
                   valveStstus.get(finalI).setText("valve NO"+ (finalI + 1) +" On");
                   // TODO:system command
               }
            });
            valveOff.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    valveOn.get(finalI).setEnabled(true);
                    valveOff.get(finalI).setEnabled(false);
                    valveStstus.get(finalI).setText("valve NO"+ (finalI + 1) +" Off");
                    // TODO:system command
                }
            });
            valveName.get(finalI).setBounds(0, 300+(i)*50, 200, 50);
            valvePLC.get(finalI).setBounds(200, 300+(i)*50, 200, 50);
            valveOn.get(finalI).setBounds(400, 300+(i)*50, 100, 50);
            valveOff.get(finalI).setBounds(500, 300+(i)*50, 100, 50);
            valveStstus.get(finalI).setBounds(600, 300+(i)*50, 200, 50);
            add(valveName.get(finalI));
            add(valvePLC.get(finalI));
            add(valveOn.get(finalI));
            add(valveOff.get(finalI));
            add(valveStstus.get(finalI));
        }
    }

    private void pumpUI() {
        pumpName = new JLabel("pump name: " + NutrientSystemConstant.Table.PUMP.getDescritpion());
        pumpPLC = new JLabel("pump PLC: " + String.valueOf(NutrientSystemConstant.Table.PUMP.getOffset()));
        pumpOn = new JButton("pump on");
        pumpOff = new JButton("pump off");
        pumpStatus = new JLabel();
        pumpName.setBounds(0, 250, 200, 50);
        pumpPLC.setBounds(200,250,200,50);
        pumpOn.setBounds(400, 250, 100, 50);
        pumpOff.setBounds(500, 250, 100, 50);
        pumpStatus.setBounds(600, 250, 200, 50);
        pumpOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pumpOn.setEnabled(false);
                pumpOff.setEnabled(true);
                // TODO:获取系统状态
                pumpStatus.setText("ON");
            }
        });
        pumpOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pumpOff.setEnabled(false);
                pumpOn.setEnabled(true);
                // TODO:获取系统状态
                pumpStatus.setText("Off");
            }
        });
        add(pumpName);
        add(pumpPLC);
        add(pumpOn);
        add(pumpOff);
        add(pumpStatus);
    }

    private void configUI() {
        start = new JButton("start");
        stop = new JButton("stop");
        config = new JButton("connfig");
        configStatus = new JLabel("config status: " + NutrientSystemConstant.Table.CONFIG_MODE.getDescritpion());
        start.setBounds(0,200,100,50);
        stop.setBounds(100,200,100,50);
        config.setBounds(200,200,100,50);
        configStatus.setBounds(300,200,300,50);
        add(start);
        add(stop);
        add(config);
        add(configStatus);
    }

    private void modeUI() {
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(HydroponicsConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(HydroponicsConstant.ModeCommand.MANUAL));
        vs.add(String.valueOf(HydroponicsConstant.ModeCommand.SEMI_AUTOMATIC));
        mode = new JComboBox(vs);
        mode.setBounds(0,150,100,50);
        curmode = new JLabel("");
        curmode.setBounds(100, 150, 200, 50);
        // 设置模式
        mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String m = "" + mode.getItemAt(mode.getSelectedIndex());
                curmode.setText("current mode: " + m);
                // TODO:对应系统指令
            }
        });
        add(mode);
        add(curmode);
    }

    private void meterUI() {
        meterName = new JLabel("meter pump name: " + NutrientSystemConstant.Table.PUMP.getDescritpion());
        meterPLC = new JLabel("meter pump PLC: " + String.valueOf(NutrientSystemConstant.Table.PUMP.getOffset()));
        meterOn =  new JButton("meter pump on");
        meterOff = new JButton("meter pump off");
        meterStatus = new JLabel("meter pump status: ");
        meterName.setBounds(0, 90, 300, 30);
        meterPLC.setBounds(300, 90, 200, 30);
        meterOn.setBounds(0, 120, 200, 30);
        meterOff.setBounds(200, 120, 200, 30);
        meterStatus.setBounds(400, 120, 300, 30);
        meterOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                meterOn.setEnabled(false);
                meterOff.setEnabled(true);
                meterStatus.setText("meter pump status: ON");
                // TODO:对应系统指令
            }
        });
        meterOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                meterOn.setEnabled(true);
                meterOff.setEnabled(false);
                meterStatus.setText("meter pump status: OFF");
                // TODO:对应系统指令
            }
        });
        add(meterName);
        add(meterPLC);
        add(meterOn);
        add(meterOff);
        add(meterStatus);
    }

    private void peUI() {
        // 找不到其他sensor
        peName = new JLabel("PE name: " + NutrientSystemConstant.Table.LEVEL_SENSOR.getDescritpion());
        pePLC = new JLabel("PE PLC: " + String.valueOf(NutrientSystemConstant.Table.LEVEL_SENSOR.getOffset()));
        peValue = new JLabel("PE value: " + String.valueOf(NutrientSystemConstant.Table.LEVEL_SENSOR.getNumber()));
        peName.setBounds(0, 60, 400, 30);
        pePLC.setBounds(500,60,200,30);
        peValue.setBounds(700,60,200,30);
        add(peName);
        add(pePLC);
        add(peValue);
    }

    private void ecUI() {
        // 找不到其他sensor
        ecName = new JLabel("EC name: " + NutrientSystemConstant.Table.LEVEL_SENSOR.getDescritpion());
        ecPLC = new JLabel("EC PLC: " + String.valueOf(NutrientSystemConstant.Table.LEVEL_SENSOR.getOffset()));
        ecValue = new JLabel("EC value: " + String.valueOf(NutrientSystemConstant.Table.LEVEL_SENSOR.getNumber()));
        ecName.setBounds(0, 30, 400, 30);
        ecPLC.setBounds(500,30,200,30);
        ecValue.setBounds(700,30,200,30);
        add(ecName);
        add(ecPLC);
        add(ecValue);
    }

    private void phUI() {
        phName = new JLabel("PH name: " + String.valueOf(NutrientSystemConstant.Table.LEVEL_SENSOR.getDescritpion()));
        phPLC = new JLabel("PH PLC: " + String.valueOf(NutrientSystemConstant.Table.LEVEL_SENSOR.getOffset()));
        phValue = new JLabel("PH value: " + String.valueOf(NutrientSystemConstant.Table.LEVEL_SENSOR.getNumber()));
        phName.setBounds(0,0,400,30);
        phPLC.setBounds(500,0,200,30);
        phValue.setBounds(700,0,200,30);
        add(phName);
        add(phPLC);
        add(phValue);
    }




    
    
    public static void main(String args[]) {
        NurientSystem s = new NurientSystem(null, 0, "af");

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
                System.exit(1);
            }
        });
 	     	    
 	    main_frame.add(new NurientSystemUI(s));
	    
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