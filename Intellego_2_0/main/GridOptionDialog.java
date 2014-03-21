package main;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;
import simworldobjects.*;
import main.*;
import NetBeansResources.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.*;
import java.io.*;

/** Provides a dialog box to allow the user to set the their perferred setting for
 * the grid display option, within the simulation environment.
 * @author Simon Zienkiewicz
 */ 
public class GridOptionDialog extends JPanel implements ActionListener
{
    private JLabel gridDetailL, pixelL, title;
    private JButton OK, cancel, tryW;
    private JComboBox dimensionSelection, pixelD;
    private Color desiredColor;
    private String[] availableDimensions = {"NONE","25x25","50x50","100x100",};
    private String[] availablePixelSizes = {"1","2","4","6"};
    private SimDisplay display;
    private SimWorld  world;
    private String objectName;
    private SimGround simg;
    private GridDisplay grid;
    private int gridSize;
    private int pixelSize=4;
    private boolean noGrid=true;
    private SimUI simulator;
        
    /** Creates a GridOptionDialog object. */

    public GridOptionDialog(){
    
    }
    
    /** Creates and displays a grid popup menu.
     * @param name the title of the popup menu
     * @param grid the GridDisplay object associated with the popup menu
     * @param display the SimDisplay object associated with the popup menu
     * @param simulator the SimUI, or simulation environment to which this grid will be applied to
     */    
    public void createGridPopUp(String name, GridDisplay grid, SimDisplay display, SimUI simulator)
    {
        //setup variables
        this.grid = grid;
        this.display = display;
        this.simulator = simulator;

        //sets the properties of the popup window
        //setTitle(name);
        setSize(210,165);
        setLocation(400,400);
        setVisible(true);
        this.setBorder(BasicBorders.getTextFieldBorder());
        this.setLayout(new AbsoluteLayout());
      
        this.title = new JLabel(name);
        this.gridDetailL = new JLabel("Grid Spacing:");
        gridDetailL.setForeground(Color.lightGray);
        this.pixelL = new JLabel("Pixel Diameter:");
        pixelL.setForeground(Color.lightGray);
        title.setForeground(Color.yellow);
        
        this.dimensionSelection = new JComboBox(this.availableDimensions);
        this.dimensionSelection.setSelectedIndex(0);
        this.dimensionSelection.addActionListener(this);
        this.pixelD = new JComboBox(this.availablePixelSizes);
        this.pixelD.setSelectedIndex(2);
        this.pixelD.addActionListener(this);
        //set color
        dimensionSelection.setBackground(Color.yellow);
        dimensionSelection.setForeground(Color.darkGray);
        pixelD.setBackground(Color.yellow);
        pixelD.setForeground(Color.darkGray);
        
        (OK=new JButton("OK")).addActionListener(this);
        (tryW=new JButton("Try")).addActionListener(this);
        (cancel=new JButton("Cancel")).addActionListener(this);
        OK.setMargin(new Insets(1,1,1,1));
        tryW.setMargin(new Insets(1,1,1,1));
        cancel.setMargin(new Insets(1,1,1,1));
        
        //set color
        OK.setBackground(Color.yellow);
        OK.setForeground(Color.darkGray);
        tryW.setBackground(Color.yellow);
        tryW.setForeground(Color.darkGray);
        cancel.setBackground(Color.yellow);
        cancel.setForeground(Color.darkGray);

        
        this.add(this.title,new AbsoluteConstraints(6,7));
        this.add(this.gridDetailL,new AbsoluteConstraints(6,27));
        this.add(this.dimensionSelection,new AbsoluteConstraints(35,50));
        this.add(this.pixelL,new AbsoluteConstraints(6,90));
        this.add(this.pixelD,new AbsoluteConstraints(95,87));
        
        this.add(OK,new AbsoluteConstraints(15,125)); 
        this.add(tryW,new AbsoluteConstraints(45,125)); 
        this.add(cancel,new AbsoluteConstraints(75,125)); 

        this.setBackground(Color.darkGray);
        
        this.show();
    }

    /**
    *  Action event handler - sets up color code according to user selection
    *
    *  @param e the action event
    */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource()==OK || e.getSource()==tryW)
        {
            this.simulator.setGrid(!noGrid);
           
            if(!noGrid){
                grid.setWantGrid(true);
                grid.updateGridSize(this.gridSize);
                grid.updateGridDotSize(this.pixelSize);
            }
            display.repaint();
            if(e.getSource()==OK)this.hide();
        }
        //action handler for the JComboBox of colors
        else if(e.getSource()==dimensionSelection){
            switch(this.dimensionSelection.getSelectedIndex()){
                case 0:     this.noGrid = true;
                            break;
                case 1:     this.gridSize = 25;
                            this.noGrid = false;
                            break;
                case 2:     this.gridSize = 50;
                            this.noGrid = false;
                            break;
                case 3:     this.gridSize = 100;
                            this.noGrid = false;
                            break;
            }
        }
        
        else if(e.getSource()==pixelD){
            switch(this.pixelD.getSelectedIndex()){
                case 0:     this.pixelSize = 1;
                            break;
                case 1:     this.pixelSize = 2;
                            break;
                case 2:     this.pixelSize = 4;
                            break;
                case 3:     this.pixelSize = 6;
                            break;
            }
       }
  
       else this.hide();
        
       try{simulator.setSize(simulator.getWidth(),simulator.getHeight());}
       catch(Exception e1){};
    }
}