package com.liveco.gateway.ui;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.system.CO2System;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class CO2SystemUI extends JFrame{
    private CO2System s;
    private JPanel jp;
    private JComboBox modeSelect;
    private JLabel valveName;
    private JLabel valvePLC;
    private JButton valveOn;
    private JButton valveOff;
    private JLabel CO2SensorName;
    private JLabel CO2SensorValue;
    private JLabel minText;
    private JLabel maxText;
    private JTextField minValue;
    private JTextField maxValue;

    public static void main(String[] args) {
        // write your code here
        CO2System s = new CO2System(null, 0, "af");
        new CO2SystemUI(s);
    }

    public CO2SystemUI(CO2System s) {
        super("CO2测试");
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

    private void initGui() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        getContentPane().setLayout(null);
        modeUI();
        CO2ValveControlUI();
        CO2SensorUI();
        CO2ValveSettingUI();
        setVisible(true);
    }

    private void modeUI() {
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.MANUAL));
        modeSelect = new JComboBox(vs);
        modeSelect.setBounds(100,100,100,50);
        // 设置模式
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                //CO2System 这里不知道用哪个函数
                //s.setMode(mode, CO2SystemConstant.ModeCommand.get(mode));
                setMode(mode);
            }
        });
        getContentPane().add(modeSelect);
    }

    private void CO2ValveControlUI() {
        valveName = new JLabel("Valve Name: " + CO2SystemConstant.Table.VALVE.getDescritpion());
        valvePLC = new JLabel("Valve PLC: " + String.valueOf(CO2SystemConstant.Table.VALVE.getOffset()));
        valveName.setBounds(100, 200, 300, 30);
        valvePLC.setBounds(500, 200, 300, 30);
        valveOn = new JButton("Valve On");
        valveOff = new JButton("Vave Off");
        valveOn.setBounds(100, 300, 100, 50);
        valveOff.setBounds(300, 300, 100, 50);
        valveOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOff.setEnabled(true);
                valveOn.setEnabled(false);
                // CO2System 打开valve
                // 找不到那个函数
                // s.setMode(CO2SystemConstant.Table.VALVE.);
            }
        });
        valveOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valveOn.setEnabled(true);
                valveOff.setEnabled(false);
                // CO2System 关闭valve
            }
        });
        getContentPane().add(valveName);
        getContentPane().add(valvePLC);
        getContentPane().add(valveOn);
        getContentPane().add(valveOff);
    }

    private void CO2SensorUI() {
        CO2SensorName = new JLabel("Sensor Name: " + CO2SystemConstant.Table.CO2.getDescritpion());
        CO2SensorValue = new JLabel("Sensor Value: " + String.valueOf(CO2SystemConstant.Table.CO2.getNumber()));
        CO2SensorName.setBounds(100, 400, 300, 30);
        CO2SensorValue.setBounds(500, 400, 300, 30);
        getContentPane().add(CO2SensorName);
        getContentPane().add(CO2SensorValue);
    }

    private void CO2ValveSettingUI() {
        byte minLimit = CO2SystemConstant.Table.CO2_LOWER_LIMIT.getNumber();
        byte maxLimit = CO2SystemConstant.Table.CO2_HIGHER_LIMIT.getNumber();
        // minLimt 和 maxLimit 的值一样
        maxLimit += 10;
        minText = new JLabel("min: " + String.valueOf(minLimit));
        maxText = new JLabel("max: " + String.valueOf(maxLimit));
        minValue = new JTextField();
        maxValue = new JTextField();
        minText.setBounds(50, 500, 100, 30);
        maxText.setBounds(450, 500, 100, 30);
        minValue.setBounds(150, 500, 50, 30);
        maxValue.setBounds(560, 500, 50, 30);
        minValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minText.setText("min: " + minValue.getText());
            }
        });
        maxValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxText.setText("max: " + maxValue.getText());
            }
        });
        getContentPane().add(minText);
        getContentPane().add(maxText);
        getContentPane().add(minValue);
        getContentPane().add(maxValue);
    }

    private void setMode(String m) {
        if (Objects.equals(m, CO2SystemConstant.ModeCommand.AUTOMATIC.getDescritpion())) {
            valveOn.setEnabled(false);
            valveOff.setEnabled(false);
            minValue.setEnabled(false);
            maxValue.setEnabled(false);
        } else if (Objects.equals(m, CO2SystemConstant.ModeCommand.MANUAL.getDescritpion())) {
            valveOn.setEnabled(true);
            valveOff.setEnabled(true);
            minValue.setEnabled(true);
            maxValue.setEnabled(true);
        }
    }
}
