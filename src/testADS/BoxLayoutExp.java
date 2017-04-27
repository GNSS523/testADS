package testADS;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
 
public class BoxLayoutExp extends JFrame {
     
    public BoxLayoutExp() {
         
        setTitle("Box Layout Example");
        setSize(150, 150);
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
         
        JButton btn1 = new JButton("Button 1");
        JButton btn2 = new JButton("Button 2");
        JButton btn3 = new JButton("Button 3");
         
        JButton btn4 = new JButton("Button 4");
        getContentPane().add(btn1);
        getContentPane().add(btn2);
        btn1.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(btn3);
        getContentPane().add(btn4);
    }
    public static void main(String[] args) {
         
        BoxLayoutExp ble = new BoxLayoutExp();
        ble.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ble.setVisible(true);
         
    }
}
