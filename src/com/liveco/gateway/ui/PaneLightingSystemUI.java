package com.liveco.gateway.ui;

import com.liveco.gateway.constant.PanelLightingConstant;
import com.liveco.gateway.system.PanelLightingSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 */
public class PaneLightingSystemUI extends JFrame {
    private PanelLightingSystem s;
    private String curmode;
    private JComboBox mode;
    private JLabel plc;
    private JButton on;
    private JButton off;
    public static void main(String[] args) {
        // write your code here
        PanelLightingSystem s = new PanelLightingSystem(null, 0, "af");
        new PaneLightingSystemUI(s);
    }

    public PaneLightingSystemUI(PanelLightingSystem s) {
        super("PaneLIghting测试");
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
        setVisible(true);
    }

    private void modeUI() {
        Vector<String> vs = new Vector<String>();
        vs.add(PanelLightingConstant.Table.BLUE.getDescritpion());
        vs.add(PanelLightingConstant.Table.FAR_RED.getDescritpion());
        vs.add(PanelLightingConstant.Table.RED.getDescritpion());
        vs.add(PanelLightingConstant.Table.UV.getDescritpion());
        vs.add(PanelLightingConstant.Table.GREEN.getDescritpion());
        mode = new JComboBox(vs);
        mode.setBounds(100,100,300,50);
        on = new JButton("ON");
        off = new JButton("OFF");
        on.setBounds(100, 350, 100, 50);
        off.setBounds(200, 350, 100, 50);
        plc =  new JLabel("");
        plc.setBounds(100, 200, 100, 50);
        // 设置模式
        mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String m = "" + mode.getItemAt(mode.getSelectedIndex());
                //s.setMode(mode, CO2SystemConstant.ModeCommand.get(mode));
                // TODO:对应系统指令
                setMode(m);
            }
        });
        on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                on.setEnabled(false);
                off.setEnabled(true);
                // TODO:根据mode发送相应Led指令
                System.out.println(curmode);
            }
        });
        off.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                on.setEnabled(true);
                off.setEnabled(false);
                // TODO:根据mode发送相应Led指令
            }
        });
        getContentPane().add(mode);
        getContentPane().add(on);
        getContentPane().add(off);
        getContentPane().add(plc);
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
}
