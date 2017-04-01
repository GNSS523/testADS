package com.liveco.gateway.ui;

import com.liveco.gateway.constant.FogponicsConstant;
import com.liveco.gateway.constant.HydroponicsConstant;
import com.liveco.gateway.system.FogponicsSystem;
import com.liveco.gateway.system.HydroponicsSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class FogponicsSystemUI extends JFrame {
    private FogponicsSystem s;
    // 模式
    private JComboBox modeSelect;
    // 两个水阀
    // 1
    private JLabel valveName1;
    private JLabel valvePLC1;
    private JButton valveOn1;
    private JButton valveOff1;
    private JLabel valveStatus1;
    // 2
    private JLabel valveName2;
    private JLabel valvePLC2;
    private JButton valveOn2;
    private JButton valveOff2;
    private JLabel valveStatus2;
    // 水泵
    private JLabel pumpName;
    private JLabel pumpPLC;
    private JButton pumpOn;
    private JButton pumpOff;
    private JLabel pumpStatus;
    // 水位传感器
    private JLabel sensorName;
    private JLabel sensorPLC;
    private JLabel sensorValue;
    // 水泵运行时间
    private JLabel runTime;
    private JLabel stopTime;
    private JTextField runTimeInput;
    private JTextField stopTimeInput;
    // 申请加水
    private JButton addWater;
    private JLabel addExecute;
    // 超声波雾化器
    private JLabel fogvalveName;
    private JLabel fogvalvePLC;
    private JButton fogvalveOn;
    private JButton fogvalveOff;

    // 雾化器运行时间
    private JLabel fogrunTime;
    private JLabel fogstopTime;
    private JTextField fogrunTimeInput;
    private JTextField fogstopTimeInput;

    public static void main(String[] args) {
        // write your code here
        FogponicsSystem s = new FogponicsSystem(null, 0, "af");
        new FogponicsSystemUI(s);
    }

    public FogponicsSystemUI(FogponicsSystem s) {
        super("Fogponics测试");
        this.s = s;
        initGui();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(1);
            }
        });
    }

    private void valveUI() {
        valveName1 = new JLabel("Valve 1 name: " + FogponicsConstant.Table.VALVE_IN.getDescritpion());
        valvePLC1 = new JLabel("Valve 1 PLC: " + FogponicsConstant.Table.VALVE_IN.getOffset());
        valveOn1 = new JButton("Valve 1 ON");
        valveOff1  = new JButton("Valve 1 OFF");
        valveStatus1 =  new JLabel("Valve 1 status: ");

        valveName2 = new JLabel("Valva 2 name: " + FogponicsConstant.Table.VALVE_OUT.getDescritpion());
        valvePLC2 = new JLabel("Valve 2 PLC: " + FogponicsConstant.Table.VALVE_OUT.getOffset());
        valveOn2 = new JButton("Valve 2 ON");
        valveOff2  = new JButton("Valve 2 OFF");
        valveStatus2 =  new JLabel("Valve 2 status: ");

        valveName1.setBounds(100, 200, 300, 30);
        valvePLC1.setBounds(400, 200, 300, 30);
        valveOn1.setBounds(100, 230, 150, 30);
        valveOff1.setBounds(300, 230, 150, 30);
        valveStatus1.setBounds(100, 260, 150, 30);
        valveOn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOn1.setEnabled(false);
                valveOff1.setEnabled(true);
                valveStatus1.setText("Valve 1 status: ON");
                // TODO: 对应系统设置
            }
        });
        valveOff1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOn1.setEnabled(true);
                valveOff1.setEnabled(false);
                valveStatus1.setText("Valve 1 status: OFF");
                // TODO: 对应系统设置
            }
        });

        valveName2.setBounds(100, 290, 300, 30);
        valvePLC2.setBounds(400, 290, 300, 30);
        valveOn2.setBounds(100, 320, 150, 30);
        valveOff2.setBounds(300,320, 150, 30);
        valveStatus2.setBounds(100, 350, 150, 30);

        valveOn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOn2.setEnabled(false);
                valveOff2.setEnabled(true);
                valveStatus2.setText("Valve 2 status: ON");
                // TODO: 对应系统设置
            }
        });
        valveOff2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOn2.setEnabled(true);
                valveOff2.setEnabled(false);
                valveStatus2.setText("Valve 1 status: OFF");
                // TODO: 对应系统设置
            }
        });

        getContentPane().add(valveName1);
        getContentPane().add(valvePLC1);
        getContentPane().add(valveOn1);
        getContentPane().add(valveOff1);
        getContentPane().add(valveStatus1);

        getContentPane().add(valveName2);
        getContentPane().add(valvePLC2);
        getContentPane().add(valveOn2);
        getContentPane().add(valveOff2);
        getContentPane().add(valveStatus2);
    }

    private void pumpUI() {
        pumpName = new JLabel("Pump 1 name: " + FogponicsConstant.Table.PUMP.getDescritpion());
        pumpPLC = new JLabel("Pump PLC: " + FogponicsConstant.Table.PUMP.getOffset());
        pumpOn = new JButton("Pump ON");
        pumpOff  = new JButton("Pump OFF");
        pumpStatus =  new JLabel("Pump status: ");

        pumpOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pumpOn.setEnabled(false);
                pumpOff.setEnabled(true);
                pumpStatus.setText("Pump status: ON");
            }
        });
        pumpOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pumpOff.setEnabled(false);
                pumpOn.setEnabled(true);
                pumpStatus.setText("Pump status: OFF");
            }
        });
        pumpName.setBounds(100, 380, 300, 30);
        pumpPLC.setBounds(400, 380, 300, 30);
        pumpOn.setBounds(100, 420, 150, 30);
        pumpOff.setBounds(300,420, 150, 30);
        pumpStatus.setBounds(100, 460, 150, 30);
        getContentPane().add(pumpName);
        getContentPane().add(pumpPLC);
        getContentPane().add(pumpOn);
        getContentPane().add(pumpOff);
        getContentPane().add(pumpStatus);
    }

    private void sensorUI() {
        sensorName = new JLabel("Sensor name: " + FogponicsConstant.Table.LEVEL_SENSOR.getDescritpion());
        sensorPLC = new JLabel("Sensor PLC: " + FogponicsConstant.Table.LEVEL_SENSOR.getOffset());
        sensorValue = new JLabel("Sensor value: " + FogponicsConstant.WaterLevelState.H);
        sensorName.setBounds(20, 500, 400, 30);
        sensorPLC.setBounds(400, 500, 200, 30);
        sensorValue.setBounds(600, 500, 200, 30);
        getContentPane().add(sensorName);
        getContentPane().add(sensorPLC);
        getContentPane().add(sensorValue);
    }

    private void timeUI() {
        // 获取值的方法是哪个？
        runTime = new JLabel("running time value: " + FogponicsConstant.Table.CONFIG_PUMP_RUN_TIME.getNumber());
        stopTime = new JLabel("stop time value: " + FogponicsConstant.Table.CONFIG_PUMP_STOP_TIME.getNumber());
        runTimeInput = new JTextField();
        stopTimeInput = new JTextField();
        runTime.setBounds(100, 550, 300, 30);
        runTimeInput.setBounds(400, 550, 300, 30);
        stopTime.setBounds(100, 580, 300, 30);
        stopTimeInput.setBounds(400, 580, 300, 30);
        runTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runTime.setText("running time value: " + runTimeInput.getText());
                // TODO: 对应的 system 设置
            }
        });
        stopTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTime.setText("stop time value: " + stopTimeInput.getText());
                // TODO: 对应的 system 设置
            }
        });
        getContentPane().add(runTime);
        getContentPane().add(runTimeInput);
        getContentPane().add(stopTime);
        getContentPane().add(stopTimeInput);
    }

    private void addWaterUI() {
        addWater = new JButton("add water");
        addWater.setBounds(100, 630, 200, 30);
        addExecute = new JLabel("0");
        addExecute.setBounds(100, 660, 100, 30);
        addWater.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 对应系统指令
            }
        });
        getContentPane().add(addWater);
        getContentPane().add(addExecute);
    }

    private void fogvalveUI() {
        fogvalveName = new JLabel("Fog Valve name: " + HydroponicsConstant.Table.VALVE_IN.getDescritpion());
        fogvalvePLC = new JLabel("Fog Valve PLC: " + HydroponicsConstant.Table.VALVE_IN.getOffset());
        fogvalveOn = new JButton("Fog Valve ON");
        fogvalveOff  = new JButton("Fog Valve OFF");

        fogvalveName.setBounds(500, 100, 300, 30);
        fogvalvePLC.setBounds(800, 100, 300, 30);
        fogvalveOn.setBounds(500, 130, 150, 30);
        fogvalveOff.setBounds(800, 130, 150, 30);
        fogvalveOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fogvalveOn.setEnabled(false);
                fogvalveOff.setEnabled(true);
                // TODO: 对应系统设置
            }
        });
        fogvalveOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fogvalveOff.setEnabled(false);
                fogvalveOn.setEnabled(true);
                // TODO: 对应系统设置
            }
        });
        getContentPane().add(fogvalveName);
        getContentPane().add(fogvalvePLC);
        getContentPane().add(fogvalveOn);
        getContentPane().add(fogvalveOff);
    }

    private void fogtimeUI() {
        fogrunTime = new JLabel("fog running time value: " + FogponicsConstant.Table.CONFIG_PUMP_RUN_TIME.getNumber());
        fogstopTime = new JLabel("fog stop time value: " + FogponicsConstant.Table.CONFIG_PUMP_STOP_TIME.getNumber());
        fogrunTimeInput = new JTextField();
        fogstopTimeInput = new JTextField();
        fogrunTime.setBounds(600, 200, 300, 30);
        fogrunTimeInput.setBounds(900, 200, 300, 30);
        fogstopTime.setBounds(600, 230, 300, 30);
        fogstopTimeInput.setBounds(900, 230, 300, 30);
        fogrunTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fogrunTime.setText("fog running time value: " + fogrunTimeInput.getText());
                // TODO: 对应的 system 设置
            }
        });
        fogstopTimeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fogstopTime.setText("fog stop time value: " + fogstopTimeInput.getText());
                // TODO: 对应的 system 设置
            }
        });
        getContentPane().add(fogrunTime);
        getContentPane().add(fogrunTimeInput);
        getContentPane().add(fogstopTime);
        getContentPane().add(fogstopTimeInput);
    }

    private void initGui() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        getContentPane().setLayout(null);
        modeUI();
        valveUI();
        pumpUI();
        sensorUI();
        timeUI();
        addWaterUI();
        fogvalveUI();
        fogtimeUI();
        setVisible(true);
    }

    private void modeUI() {
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(FogponicsConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(FogponicsConstant.ModeCommand.MANUAL));
        vs.add(String.valueOf(FogponicsConstant.ModeCommand.SEMI_AUTOMATIC));
        vs.add(String.valueOf(FogponicsConstant.ModeCommand.CIRCULATE));
        vs.add(String.valueOf(FogponicsConstant.ModeCommand.OUTLET));
        vs.add(String.valueOf(FogponicsConstant.ModeCommand.INLET));
        modeSelect = new JComboBox(vs);
        modeSelect.setBounds(100,100,100,50);
        // 设置模式
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                setMode(mode);
                // TODO:对应系统指令
            }
        });
        getContentPane().add(modeSelect);
    }

    private void setMode(String mode) {

    }
}
