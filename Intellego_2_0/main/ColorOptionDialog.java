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
import javax.swing.plaf.basic.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
* Provides a dialog box to get color selection parameters from the user
* @author Simon Zienkiewicz
* 
*/ 
public class ColorOptionDialog extends JPanel implements ActionListener
{
    private JLabel colorL,rL,bL,gL, previewL, title;
    private JButton OK, cancel, tryW;
    private JTextField rT, bT, gT;
    private JTextArea preview;
    private JComboBox colorSelection;
    private Color desiredColor;
    private String[] availableColors = {"Black","Blue","Cyan","Gray","Green","Magenta","Orange","Pink","Red","White","Yellow"};
    private int index;
    private SimDisplay display;
    private SimWorld  world;
    private String objectName;
    private SimGround simg;
    private GridDisplay grid;
    private SimUI sim;
    
    /** Used to change the colours of objects located within the Simulation environment.
     * Note that one ColorOptionDialog is created for the entire Simulation Window.
     * @param sim the SIMUI requesting a ColorOptionDialog
     */
    public ColorOptionDialog(SimUI sim){
        this.sim = sim;
        this.setBackground(Color.darkGray);
    }
    
    /** Creates and displays the Popup dialog witht the Simulation environment */    
    public void createColorPopUp()
    {
        setSize(150,245);
        setLocation(400,400);
        setVisible(true);
        this.setBorder(BasicBorders.getTextFieldBorder());
        this.setLayout(new AbsoluteLayout());

        title = new JLabel("Color Editor:");
        previewL =new JLabel("Preview:");
        colorL=new JLabel("Select Color:");
        rL=new JLabel("R:");
        bL=new JLabel("B:");
        gL=new JLabel("G:");
        //set colors
        previewL.setForeground(Color.lightGray);
        colorL.setForeground(Color.lightGray);
        title.setForeground(Color.yellow);
        rL.setForeground(Color.red);
        bL.setForeground(Color.blue);
        gL.setForeground(Color.green);
                
        preview = new JTextArea(3,5);
        preview.setSize(20,20);
        preview.setBackground(Color.white);
        preview.setEditable(false);
        
        rT=new JTextField(3);
        bT=new JTextField(3);
        gT=new JTextField(3);
        rT.setEditable(false);
        bT.setEditable(false);
        gT.setEditable(false);
        //set color
        rT.setBackground(Color.yellow);
        rT.setForeground(Color.darkGray);
        bT.setBackground(Color.yellow);
        bT.setForeground(Color.darkGray);
        gT.setBackground(Color.yellow);
        gT.setForeground(Color.darkGray);
 
        colorSelection = new JComboBox(availableColors);
        colorSelection.addActionListener(this);
        colorSelection.setSelectedIndex(9);
        //set color
        colorSelection.setBackground(Color.yellow);
        colorSelection.setForeground(Color.darkGray);
  
        (OK=new JButton("OK")).addActionListener(this);
        (tryW=new JButton("Try")).addActionListener(this);
        (cancel=new JButton("Cancel")).addActionListener(this);
        //set color
        OK.setMargin(new Insets(1,1,1,1));
        tryW.setMargin(new Insets(1,1,1,1));
        cancel.setMargin(new Insets(1,1,1,1));
        
        OK.setBackground(Color.yellow);
        OK.setForeground(Color.darkGray);
        tryW.setBackground(Color.yellow);
        tryW.setForeground(Color.darkGray);
        cancel.setBackground(Color.yellow);
        cancel.setForeground(Color.darkGray);

        this.add(title,new AbsoluteConstraints(6,7));
        this.add(colorL,new AbsoluteConstraints(6,30));
        this.add(colorSelection,new AbsoluteConstraints(50,50));
        this.add(previewL,new AbsoluteConstraints(6,80));
        this.add(preview,new AbsoluteConstraints(60,100));

        this.add(rL,new AbsoluteConstraints(25,160));
        this.add(rT,new AbsoluteConstraints(15,180)); 
        this.add(gL,new AbsoluteConstraints(65,160));
        this.add(gT,new AbsoluteConstraints(55,180)); 
        this.add(bL,new AbsoluteConstraints(105,160));
        this.add(bT,new AbsoluteConstraints(95,180)); 
        
        this.add(OK,new AbsoluteConstraints(15,210)); 
        this.add(tryW,new AbsoluteConstraints(45,210)); 
        this.add(cancel,new AbsoluteConstraints(75,210)); 
      
        this.setBackground(Color.darkGray);
        this.show();
    }
    
    /** A method used to subsequantly call the ColorOptionDialog object in order to
     * change the colour properties of a Simulation environment object.
     * @param name the title of the object displayed in the Dialog
     * @param index a number associated with the type of sim. world object
     * @param d the SimDisplay object refrenced to the sim. world object
     * @param w the SimWorld in which the object is in
     * @param objectName the type of object
     * @param simg the name of the SimGround object, if referenced object is NOT of that type, NULL
     * is passed
     * @param grid the name of the GridDisplay object, if referenced object is NOT of that type, NULL
     * is passed
     */    
    public void updateColorPopUp(String name, int index, SimDisplay d, SimWorld w, String objectName, SimGround simg, GridDisplay grid){
               
        title.setText(name);
        this.index = index;
        this.display = d;
        this.world = w;
        this.objectName=objectName;
        this.simg = simg;
        this.grid = grid;
                
        if(simg != null)deselectOtherObjects();
        this.show();
     }

    /** This method deselects all the other objects within the simulation environment,
     * ensuring that only the current object is selected.
     */    
    private void deselectOtherObjects(){
        
        LinkedList newList = world.getObjectList();
        
        for(int i=0;i<newList.size();i++){
            SimObject simObject2 = (SimObject)newList.get(i);
            if(!this.simg.equals(simObject2)){
                if(simObject2 instanceof SimGround){
                    ((SimGround)simObject2).setSelected(false);
                    ((SimGround)simObject2).setOutlineColor(Color.black);
                    display.repaint();
                }
            }
        }
    }
    
    /**
    *  Action event handler - sets up color code according to user selection
    *
    *  @param e the action event
    */
    public void actionPerformed(ActionEvent e)
    {
        //System.out.println("BEFORE ACTION EVENT:"+ this.isSelected());
        if (e.getSource()==OK || e.getSource()==tryW)
        {
           switch(index){
               case 0:  ((SimpleDisplay)display).setColorLibrary(this.objectName,this.desiredColor);
                        if(this.objectName.equals("road")){
                            for(int i=0;i<world.getObjectList().size();i++)
                            {
                                SimObject o=(SimObject)world.getObjectList().get(i);
                                if(o instanceof SimRoad)((SimRoad)o).setColor(this.desiredColor);

                            }
                        }
               
                        break;
               
               case 1: ((BasicSimWorld)world).setWorldColor(this.desiredColor);
                        grid.updateWorldColor(this.desiredColor);
                        grid.repaint();
                        break;
               
               case 2:  simg.setColor(this.desiredColor);
                        if(e.getSource()==OK){
                            simg.setOutlineColor(Color.black);
                            simg.setSelected(false);
                        }
                        
                        //undo/redo purposes ***************
                        sim.pushUndoItem(sim.copyList((LinkedList)world.getObjectList()));
               
                        break;
               
               case 6:  grid.updateGridColor(this.desiredColor);
                                             
           }
           display.repaint();
           
           if(e.getSource()==OK )this.hide();

        }
        //action handler for the JComboBox of colors
        else if(e.getSource()==colorSelection){
            Color color= Color.white;
            switch(colorSelection.getSelectedIndex()){

                case 0: color = Color.black;
                        break;
                case 1: color = Color.blue;
                        break;
                case 2: color = Color.cyan;
                        break;
                case 3: color = Color.gray;
                        break;
                case 4: color = Color.green;
                        break;
                case 5: color = Color.magenta;
                        break;
                case 6: color = Color.orange;
                        break;
                case 7: color = Color.pink;
                        break;
                case 8: color = Color.red;
                        break;
                case 9: color = Color.white;
                        break;
                case 10: color = Color.yellow;
                         break;
            }
            //generates a preview of the selected color
            this.preview.setBackground(color);
            
            //gets and sets the RBG values or the selected color
            this.rT.setText(""+color.getRed());
            this.bT.setText(""+color.getBlue());
            this.gT.setText(""+color.getGreen());
            
            //sets the class variable
            this.desiredColor = color;
            
        }

        else
        {
           if(index == 2){
            simg.setSelected(false);
            simg.setOutlineColor(Color.black);
            display.repaint();
           }
           this.hide();
        }
        try{sim.setSize(sim.getWidth(),sim.getHeight());}
        catch(Exception e1){};
    }
}