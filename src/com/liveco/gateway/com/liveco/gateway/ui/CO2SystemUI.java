package com.liveco.gateway.com.liveco.gateway.ui;

import com.liveco.gateway.constant.CO2SystemConstant;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.system.CO2System;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class CO2SystemUI extends JFrame{
    private CO2System s;
    private JPanel jp;

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
        Vector<String> vs = new Vector<String>();
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.AUTOMATIC));
        vs.add(String.valueOf(CO2SystemConstant.ModeCommand.MANUAL));
        JComboBox modeSelect = new JComboBox(vs);
        modeSelect.setBounds(100,100,100,50);
        // 设置模式
        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = "" + modeSelect.getItemAt(modeSelect.getSelectedIndex());
                //这里不知道用哪个函数
                try {
                    s.setMode(mode, CO2SystemConstant.ModeCommand.get(mode));
                } catch (AdsException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JLabel valveName = new JLabel("Valve Name: " + CO2SystemConstant.Table.VALVE.getDescritpion());
        JLabel valvePLC = new JLabel("Valve PLC: " + String.valueOf(CO2SystemConstant.Table.VALVE.getOffset()));
        valveName.setBounds(100, 200, 300, 30);
        valvePLC.setBounds(500, 200, 300, 30);
        JButton valveOn = new JButton("Valve On");
        JButton valveOff = new JButton("Vave Off");
        valveOn.setBounds(100, 300, 100, 50);
        valveOff.setBounds(300, 300, 100, 50);
        JLabel CO2SensorName = new JLabel("Sensor Name: " + CO2SystemConstant.Table.CO2.getDescritpion());
        JLabel CO2SensorValue = new JLabel("Sensor Value: " + String.valueOf(CO2SystemConstant.Table.CO2.getNumber()));
        CO2SensorName.setBounds(100, 400, 300, 30);
        CO2SensorValue.setBounds(500, 400, 300, 30);
        byte minLimit = CO2SystemConstant.Table.CO2_LOWER_LIMIT.getNumber();
        byte maxLimit = CO2SystemConstant.Table.CO2_HIGHER_LIMIT.getNumber();
        // minLimt 和 maxLimit 的值一样
        maxLimit += 10;
        System.out.println(minLimit);
        System.out.println(maxLimit);
        JSlider LimitSlider = new JSlider(JSlider.HORIZONTAL,minLimit,maxLimit,minLimit);
        JLabel minText = new JLabel("min: " + String.valueOf(minLimit));
        JLabel maxText = new JLabel("max: " + String.valueOf(maxLimit));
        JLabel sliderValue = new JLabel("value of slider: " + String.valueOf(minLimit));
        LimitSlider.setBounds(100, 500, 300, 30);
        minText.setBounds(50, 500, 50, 30);
        maxText.setBounds(450, 500, 100, 30);
        sliderValue.setBounds(100, 550, 300, 30);
        LimitSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int foo = LimitSlider.getValue();
                sliderValue.setText("value of slider: " + String.valueOf(foo));
            }
        });
        getContentPane().add(modeSelect);
        getContentPane().add(valveName);
        getContentPane().add(valvePLC);
        getContentPane().add(valveOn);
        getContentPane().add(valveOff);
        getContentPane().add(CO2SensorName);
        getContentPane().add(CO2SensorValue);
        getContentPane().add(LimitSlider);
        getContentPane().add(minText);
        getContentPane().add(maxText);
        getContentPane().add(sliderValue);
        setVisible(true);
    }
}
