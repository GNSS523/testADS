package com.liveco.gateway.ui;

import com.liveco.gateway.constant.PanelLightingConstant;
import com.liveco.gateway.plc.AdsException;
import com.liveco.gateway.plc.DeviceTypeException;
import com.liveco.gateway.system.PanelLightingSystem;
import com.liveco.gateway.system.ShelfLightingSystem;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Created by halsn on 17-4-1.
 * http://stackoverflow.com/questions/15732194/how-to-use-jcheckbox-array-in-jpanel-array
 */
public class ShelfLightingSystemUI extends JPanel {
    private ShelfLightingSystem system;
    private String curmode;
    private JButton on;
    private JLabel range;

    private static final int CHECK_BOX_ROWS = 6;
    private static final int CHECK_BOX_COLS = 6;
    private static final int GAP = -5;
    private JCheckBox[][] checkBoxes = new JCheckBox[CHECK_BOX_ROWS][CHECK_BOX_COLS];
    private int gridIndex;
    private ItemListener itemListener = new MyCheckBoxListener();
    private int row;
    private int col;    

    public ShelfLightingSystemUI(ShelfLightingSystem system) {
        this.system = system;
        initGui();
                
        
    }

    private void initGui() {
        setSize(800, 600);
        setLayout(null);
        modeUI();
        setVisible(true);
    }

    private void modeUI() {

        this.row = row;
        this.col = col;
        gridIndex = row + col + 1;
        setBorder(BorderFactory.createTitledBorder(String.valueOf(gridIndex)));
        setLayout(new GridLayout(CHECK_BOX_ROWS, CHECK_BOX_COLS, GAP, GAP));

        for (int cbRow = 0; cbRow < checkBoxes.length; cbRow++) {
           for (int cbCol = 0; cbCol < checkBoxes[cbRow].length; cbCol++) {
              JCheckBox checkBox = new JCheckBox();
              checkBox.addItemListener(itemListener);
              add(checkBox);
              checkBoxes[cbRow][cbCol] = checkBox;
           }
        }   	
    	
    }

    private class MyCheckBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent itemEvt) {
           JCheckBox source = (JCheckBox) itemEvt.getSource();
           boolean selected = source.isSelected();
           int cbRow = -1;
           int cbCol = -1;
           for (int r = 0; r < checkBoxes.length; r++) {
              for (int c = 0; c < checkBoxes[r].length; c++) {
                 if (source.equals(checkBoxes[r][c])) {
                    cbRow = r;
                    cbCol = c;
                 }
              }
           }

           String text = String.format("Grid %d, selected: %b, at [%d, %d]", 
                 (row + col + 1), selected, cbCol, cbRow); // corrected row/col order
           System.out.println(text);
           
           if(selected){
        	   
	           try {
	        	   system.open(0);
				} catch (AdsException | DeviceTypeException e) {
					e.printStackTrace();
				}
	           
           }else{
        	   
               try {
            	   system.close(0);
    			} catch (AdsException | DeviceTypeException e) {
    				e.printStackTrace();
    			}  
               
           }
           
          
           
        }



     }
    
    
    public static void main(String[] args) {
        // write your code here
        ShelfLightingSystem s = new ShelfLightingSystem(null, 0, "af");
        
        
 	    JFrame main_frame = new JFrame();
 	    main_frame.setSize(800, 600);
 	    main_frame.setTitle("CO2 system");
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
 	     	    
 	    main_frame.add(new ShelfLightingSystemUI(s));
 	    main_frame.setLayout(new GridLayout(3, 3, GAP, GAP));
 	    /*
 	      for (int row = 0; row < checkBoxGrid.length; row++) {
 	         for (int col = 0; col < checkBoxGrid[row].length; col++) {
 	            checkBoxGrid[row][col] = new CheckBoxGrid(row, col);
 	            add(checkBoxGrid[row][col]);
 	         }
 	      } 	    
 	    */
 	    

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