package com.liveco.gateway.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
 
public class ToolBar extends JFrame {
 
    public ToolBar() {
        super("ToolBar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button1 = new JButton("ABC");
        JButton button2 = new JButton("ACC");
        JButton button3 = new JButton("DDD");
        JToolBar bar = new JToolBar();
        bar.add(button1);
        bar.add(button2);
        bar.add(button3);
        JTextArea edit = new JTextArea(8,40);
        JScrollPane scroll = new JScrollPane(edit);
        JPanel pane = new JPanel();
        BorderLayout bord = new BorderLayout();
        pane.setLayout(bord);
        pane.add("North", bar);
        pane.add("Center", scroll);
 
        setContentPane(pane);
    }
 
    public static void main(String[] arguments) {
        ToolBar frame = new ToolBar();
        frame.pack();
        frame.setVisible(true);
    }
}
