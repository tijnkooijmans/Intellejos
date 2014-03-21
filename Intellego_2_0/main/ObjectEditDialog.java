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
import java.util.*;

/** Provides a dialog box to allow the user to interact with the objects in the
 * simulation environment.
 * @author Simon Zienkiewicz
 */ 
public class ObjectEditDialog extends JPanel implements ActionListener
{
    private JLabel pointL,xL,yL,zL, sizeL, widthL, lengthL, orientationL, bearingL, heightL, title;
    private JButton OK, cancel, tryW, delW,clone;
    private JSpinner xS,yS,zS,widthS,lengthS,bearingS,heightS;
    private String shapeNames[] = {"robot","light","sensorTouch","sensorLight","wall","ground"};
    private int index;
    private SimDisplay display;
    private BasicSimObject simObject;
    private BasicSimWorld simWorld;
    private int objectIndex;
    private SimUI sim;
  
    /** Displays a dialog box for the user to interact with the simulation envirionment objects.
     * @param sim the simulator window
     */

    public ObjectEditDialog(SimUI sim){
        this.sim = sim;
        this.setBackground(Color.darkGray);
    }
    
    /** Creates the actual popup dialog. */    
    public void createObjectEditorPopUp()
    {
        setSize(355,225);
        setLocation(400,400);
        setVisible(true);
        this.setBorder(BasicBorders.getTextFieldBorder());
        this.setLayout(new AbsoluteLayout());
      
        this.title = new JLabel("Object Editor:");
        this.pointL = new JLabel("Center Point:");
        this.xL = new JLabel("X:");
        this.yL = new JLabel("Y:");
        this.zL = new JLabel("Z:");
        this.sizeL = new JLabel("Dimensions:");
        this.widthL = new JLabel("Width:");
        this.heightL = new JLabel("Height:");
        this.lengthL = new JLabel("Length:");
        this.orientationL = new JLabel("Orientation:");
        this.bearingL = new JLabel("XZ Rotation:");
        //set colors
        title.setForeground(Color.yellow);
        pointL.setForeground(Color.lightGray);
        xL.setForeground(Color.yellow);
        yL.setForeground(Color.yellow);
        zL.setForeground(Color.yellow);
        sizeL.setForeground(Color.lightGray);
        widthL.setForeground(Color.yellow);
        heightL.setForeground(Color.yellow);
        lengthL.setForeground(Color.yellow);
        orientationL.setForeground(Color.lightGray);
        bearingL.setForeground(Color.yellow);
        
        this.xS = new JSpinner(new SpinnerNumberModel(0,0,1000,1));
        this.xS.setBackground(Color.yellow);
        this.yS = new JSpinner(new SpinnerNumberModel(0,0,1000,1));
        this.yS.setBackground(Color.yellow);
        this.yS.setEnabled(false);
        this.zS = new JSpinner(new SpinnerNumberModel(0,0,1000,1));
        this.zS.setBackground(Color.yellow);
                       
        this.widthS = new JSpinner(new SpinnerNumberModel(10,10,500,1));
        this.heightS = new JSpinner(new SpinnerNumberModel(0,0,500,1));
        this.heightS.setEnabled(false);
        this.lengthS = new JSpinner(new SpinnerNumberModel(10,10,500,1));
        //set colors
        widthS.setBackground(Color.yellow);
        heightS.setBackground(Color.yellow);
        lengthS.setBackground(Color.yellow);
        
        this.bearingS = new JSpinner(new SpinnerNumberModel(0,0,360,1));
        //this.bearingS.setEnabled(false);
        bearingS.setBackground(Color.yellow);
                        
        (OK=new JButton("OK")).addActionListener(this);
        (tryW=new JButton("Try")).addActionListener(this);
        (delW=new JButton("Del")).addActionListener(this);
        delW.setMnemonic('d');
                
        (cancel=new JButton("Cancel")).addActionListener(this); 
        (clone=new JButton()).addActionListener(this); 
        
        ImageIcon copyI = new ImageIcon("images\\copy.gif");
        Image image3 = (copyI.getImage().getScaledInstance(18,18,0));
        clone.setIcon(new ImageIcon(image3));
        clone.setMnemonic('c');
                
        clone.setToolTipText("clone current object");
        delW.setToolTipText("delete current object");
        
        OK.setMargin(new Insets(1,1,1,1));
        tryW.setMargin(new Insets(1,1,1,1));
        delW.setMargin(new Insets(1,1,1,1));
        cancel.setMargin(new Insets(1,1,1,1));
        clone.setMargin(new Insets(1,1,1,1));
        
        OK.setBackground(Color.yellow);
        OK.setForeground(Color.darkGray);
        tryW.setBackground(Color.yellow);
        tryW.setForeground(Color.darkGray);
        delW.setBackground(Color.red);
        delW.setForeground(Color.darkGray);
        cancel.setBackground(Color.yellow);
        cancel.setForeground(Color.darkGray);
        clone.setBackground(Color.red);
        clone.setForeground(Color.darkGray);
                
        this.add(this.title,new AbsoluteConstraints(6,7));
        this.add(this.pointL,new AbsoluteConstraints(6,30));
        
        this.add(this.xL,new AbsoluteConstraints(35,57));
        this.add(this.xS,new AbsoluteConstraints(50,53));
        this.add(this.yL,new AbsoluteConstraints(35,87));
        this.add(this.yS,new AbsoluteConstraints(50,83));
        this.add(this.zL,new AbsoluteConstraints(35,117));
        this.add(this.zS,new AbsoluteConstraints(50,113));
        
        this.add(this.sizeL,new AbsoluteConstraints(6,143)); 
        this.add(this.widthL,new AbsoluteConstraints(20,170)); 
        this.add(this.widthS,new AbsoluteConstraints(70,166));
        this.add(this.heightL,new AbsoluteConstraints(20,200));
        this.add(this.heightS,new AbsoluteConstraints(70,196));
        this.add(this.lengthL,new AbsoluteConstraints(20,230));
        this.add(this.lengthS,new AbsoluteConstraints(70,226));
        
        this.add(orientationL,new AbsoluteConstraints(6,256));
        this.add(this.bearingL,new AbsoluteConstraints(20,282));
        this.add(this.bearingS,new AbsoluteConstraints(70,310));
                
        this.add(OK,new AbsoluteConstraints(5,345));
        this.add(tryW,new AbsoluteConstraints(32,345));
        this.add(delW,new AbsoluteConstraints(59,345));
        this.add(cancel,new AbsoluteConstraints(86,345));
        this.add(clone,new AbsoluteConstraints(105,5));
       
        this.setBackground(Color.DARK_GRAY);
     
        this.show();
    }
    /** Updates the object after the user makes changes in the dialog window.
     * @param simObject the object that was modified
     * @param d the SimDisplay object associated with the modified object
     * @param objectIndex the index of the object within the object list
     * @param world the SimWorld which contains the object
     */    
    public void updateObjectPopUp(SimObject simObject, SimDisplay d, int objectIndex, SimWorld world){
         //setup variables
        this.display = d;
        this.simObject = (BasicSimObject)simObject;
        this.simWorld = (BasicSimWorld)world;
        this.objectIndex = objectIndex;
        
        this.title.setText("Editing: " + ((this.simObject.getType()).substring(0,1)).toUpperCase()+(this.simObject.getType()).substring(1)+" "+ objectIndex);
        
        ((SpinnerNumberModel)(xS.getModel())).setValue(new Integer((int)this.simObject.getXCoord()));
        ((SpinnerNumberModel)(xS.getModel())).setMaximum(new Integer(simWorld.getWorldDimensions()[0]));     
        
        ((SpinnerNumberModel)(yS.getModel())).setValue(new Integer((int)this.simObject.getYCoord()));
        
        ((SpinnerNumberModel)(zS.getModel())).setValue(new Integer((int)this.simObject.getZCoord()));
        ((SpinnerNumberModel)(zS.getModel())).setMaximum(new Integer(simWorld.getWorldDimensions()[2]));     
        
        ((SpinnerNumberModel)(heightS.getModel())).setValue(new Integer((int)this.simObject.getHeight()));
        ((SpinnerNumberModel)(lengthS.getModel())).setValue(new Integer((int)this.simObject.getLength()));
        ((SpinnerNumberModel)(lengthS.getModel())).setMaximum(new Integer(simWorld.getWorldDimensions()[2]/2));
        ((SpinnerNumberModel)(widthS.getModel())).setValue(new Integer((int)this.simObject.getWidth()));
        ((SpinnerNumberModel)(widthS.getModel())).setMaximum(new Integer(simWorld.getWorldDimensions()[0]/2));     
        
        ((SpinnerNumberModel)(bearingS.getModel())).setValue(new Integer((int)this.simObject.getActualBearingXZ()));
        
        deselectOtherObjects();
        this.show();
     }

    /** Deselects all other objects. */    
    private void deselectOtherObjects(){
        
        LinkedList newList = this.simWorld.getObjectList();
        
        for(int i=0;i<newList.size();i++){
            SimObject simObject2 = (SimObject)newList.get(i);
            if(!this.simObject.equals(simObject2)){
                if(simObject2 instanceof SimGround){
                    ((SimGround)simObject2).setSelected(false);
                    ((SimGround)simObject2).setOutlineColor(Color.black);
                    display.repaint();
                }
                else if(simObject2 instanceof SimWall){
                    ((SimWall)simObject2).setSelected(false);
                    ((SimWall)simObject2).setOutlineColor(Color.black);
                    display.repaint();
                }
            }
        }
    }
    /** Adds a new object to the simulation world
     * @param type the type of object, either wall or ground
     */    
     private void addObjectToWorld(String type){
            if(type.equals("wall")){
                SimWall newWall = new SimWall(this.simObject.getXCoord(),simObject.getYCoord(),simObject.getZCoord(),simObject.getActualBearingXZ(),simObject.getWidth(),simObject.getLength());
                simWorld.addObjecttoFront(newWall);
                
            }
            else{
                SimGround newGround = new SimGround(this.simObject.getXCoord(),simObject.getYCoord(),simObject.getZCoord(),simObject.getActualBearingXZ(),simObject.getWidth(),simObject.getLength(),((SimGround)simObject).getColor());
                simWorld.addObjecttoFront(newGround);
            }
           display.repaint();
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
            try{
                int x=Integer.parseInt(xS.getValue().toString());
                int z=Integer.parseInt(zS.getValue().toString());
                int width=Integer.parseInt(widthS.getValue().toString());
                int length=Integer.parseInt(lengthS.getValue().toString());
                int rotation=Integer.parseInt(bearingS.getValue().toString());

                this.simObject.setXCoord((long)x);
                this.simObject.setZCoord((long)z);
                this.simObject.updateWidth((long)width);
                this.simObject.updateLength((long)length);
                this.simObject.setActualBearingXZ((long)rotation);
                                
                display.repaint();
    
                //only dispose if ok was clicked
                if(e.getSource()==OK){
                    if(this.simObject instanceof SimGround){
                        ((SimGround)this.simObject).setSelected(false);
                        ((SimGround)this.simObject).setOutlineColor(Color.black);
                    }
                    else{
                        ((SimWall)this.simObject).setSelected(false);
                        ((SimWall)this.simObject).setOutlineColor(Color.black);
                    }
                                        
                    this.hide();
                }
                
                //undo/redo purposes ********************************************************************************
                sim.pushUndoItem(sim.copyList((LinkedList)simWorld.getObjectList()));
         
            }
            catch(Exception a){
                main.MainInterface.displayMessage("ERROR: Incorrect Input Type | FIX: Please try again, only entering INTEGERs");
                if(this.simObject instanceof SimGround){
                        ((SimGround)this.simObject).setSelected(false);
                        ((SimGround)this.simObject).setOutlineColor(Color.black);
                }
                else{
                    ((SimWall)this.simObject).setSelected(false);
                    ((SimWall)this.simObject).setOutlineColor(Color.black);
                }

               this.hide();
            }
            
        }
        else if(e.getSource()==delW){
           
            LinkedList newList = this.simWorld.getObjectList();
            newList.remove(this.objectIndex);
            this.simWorld.updateObjectList(newList);
            display.repaint();
            this.hide();
            
            //undo/redo purposes ********************************************************************************
            sim.pushUndoItem(sim.copyList((LinkedList)simWorld.getObjectList()));
       
        }
        else if(e.getSource()==clone){
            if(simObject instanceof SimWall) addObjectToWorld("wall");
            else addObjectToWorld("ground");
            
            //undo/redo purposes ********************************************************************************
            sim.pushUndoItem(sim.copyList((LinkedList)simWorld.getObjectList()));
        
        }
        else
        {
            if(this.simObject instanceof SimGround){
                ((SimGround)this.simObject).setSelected(false);
                ((SimGround)this.simObject).setOutlineColor(Color.black);
            }
            else{
                ((SimWall)this.simObject).setSelected(false);
                ((SimWall)this.simObject).setOutlineColor(Color.black);
            }
            
            display.repaint();
            this.hide(); 
        }
        try{sim.setSize(sim.getWidth(),sim.getHeight());}
        catch(Exception e1){};
    }
}