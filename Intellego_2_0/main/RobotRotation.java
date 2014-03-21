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

/** Provides a dialog box to allow the user to interact with the robot in the
 * simulation environment.
 * @author Simon Zienkiewicz
 */ 
public class RobotRotation extends JPanel implements ActionListener
{
    private JLabel bearingL,title;
    private JButton OK, cancel, tryW, remove;
    private JSpinner bearingS;
    private SimUI sim;
    private SimRCX object;
    private BasicSimWorld world=null;
        
    /** Creates an instance of RobotRotation
     * @param sim the simulator window
     */

    public RobotRotation(SimUI sim){
        this.sim = sim;
    }
    
    /** Creates the actual robot rotation dialog. */    
    public void createRobotRotation(){
        
        //sets the properties of the popup window
        setSize(210,165);
        setLocation(400,400);
       // setLocation(0,0);
        setVisible(true);
        this.setBorder(BasicBorders.getTextFieldBorder());
        this.setLayout(new AbsoluteLayout());
      
        this.title = new JLabel("Robot Bearing:");
        this.bearingL = new JLabel("XZ Rotation:");
              
        title.setForeground(Color.yellow);
        bearingL.setForeground(Color.lightGray);
        
        this.bearingS = new JSpinner(new SpinnerNumberModel(0,0,360,1));
        bearingS.setBackground(Color.yellow);
        
        
        (OK=new JButton("OK")).addActionListener(this);
        (tryW=new JButton("Try")).addActionListener(this);
        (remove=new JButton("Del")).addActionListener(this);
        remove.setMnemonic('d');
        (cancel=new JButton("Cancel")).addActionListener(this);
        
        OK.setMargin(new Insets(1,1,1,1));
        tryW.setMargin(new Insets(1,1,1,1));
        remove.setMargin(new Insets(1,1,1,1));
        cancel.setMargin(new Insets(1,1,1,1));
        
        //set color
        OK.setBackground(Color.yellow);
        OK.setForeground(Color.darkGray);
        tryW.setBackground(Color.yellow);
        tryW.setForeground(Color.darkGray);
        remove.setBackground(Color.red);
        remove.setForeground(Color.darkGray);
        cancel.setBackground(Color.yellow);
        cancel.setForeground(Color.darkGray);
        
        this.add(this.title,new AbsoluteConstraints(6,7));
        this.add(this.bearingL,new AbsoluteConstraints(6,27));
        this.add(this.bearingS,new AbsoluteConstraints(35,50));
       
        this.add(OK,new AbsoluteConstraints(5,85));
        this.add(tryW,new AbsoluteConstraints(32,85));
        this.add(remove,new AbsoluteConstraints(59,85));
        this.add(cancel,new AbsoluteConstraints(86,85));

        this.setBackground(Color.darkGray);
        
        this.show();
    }
    /** Retrieves the dialog so the user can see it.
     * @param robot the sim. world robot
     * @param world the SimWorld where the robot exists
     */    
    public void updateRotation(SimRCX robot, SimWorld world){
        this.object = robot;
        this.world = (BasicSimWorld)world;
        this.show();
    }
    
    /** Updates the rotation which the user modified for the robot.
     * @param robot the sim. world robot
     */    
    public void updateValue(SimRCX robot){
        int value = (int)robot.getActualBearingXZ();
        int div = (int)value/360;
        value = value-(360*div);
        if(value <0) value = value+360;
        bearingS.setValue(new Integer(value));
    }

    /**  Action event handler
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource()==OK || e.getSource()==tryW)
        {
            try{
                int rotation=Integer.parseInt(bearingS.getValue().toString());
                this.object.setActualBearingXZ((long)rotation);
            }
            catch(Exception e2){}
            
            if(e.getSource()==OK){
                    this.object.setSelected(false);                                      
                    this.hide();
            }
            
        }
        else if(e.getSource()==remove){
            LinkedList newList = this.world.getObjectList();
            
            for(int i=0;i<newList.size();i++){
                
                SimObject simObject = (SimObject)newList.get(i);
                               
                if(simObject instanceof SimRCX){
                    if(((SimRCX)simObject).equals(this.object)){
                        newList.remove(this.object);
                        i=(-1);
                    }
               }
                
                else if(simObject instanceof SimSensor){
                    
                    if((((SimSensor)simObject).getOwner()).equals(this.object)){
                        SimObject a = (SimObject)newList.remove(i);
                        i=(-1);
                    }
                }
            }
            
            this.world.updateObjectList(newList);
            this.hide();
        
        }
  
        else{
            this.object.setSelected(false);   
            this.hide();
        }
        
        try{sim.setSize(sim.getWidth(),sim.getHeight());}
        catch(Exception e1){};
    }
}