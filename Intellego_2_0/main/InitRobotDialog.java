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
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/** Provides a dialog box to get intial robot parameters from the user.
 * @author Graham Ritchie
 * @Modyfier Simon Zienkiewicz
 */ 
public class InitRobotDialog extends JPanel implements ActionListener
{
	private JTextField xField, zField, bField, cField;
        private JSpinner xS,zS,bS,cS;
        private JLabel xLabel, zLabel, bLabel, cLabel,cLabel2,title;
        private JButton OK, cancel;
	private SimWorld world;
	private Controller controller;
	private SimDisplay display;
        private SimUI sim;

        /** Creates a new instance of InitRobotDialog() object */
        
        public InitRobotDialog(){
        
        }
        
        /** Creates a robot object from given parameters.
         * @param w the SimWorld of the robot
         * @param c the controller of the robot (the brains)
         * @param d the SimDisplay associated with the robot
         * @param sim the Simulation environment of the robot
         */        
        public void createRobot(SimWorld w, Controller c, SimDisplay d, SimUI sim)
        {
            //setTitle("Initialise Robot:");
            setSize(220,195);
            setLocation(550,180);
            setVisible(true);
            this.setBorder(BasicBorders.getTextFieldBorder());
            this.setLayout(new AbsoluteLayout());	
            
            //set variables
            world=w;
            controller=c;
            display=d;
            this.sim = sim;
	              
            xS=new JSpinner(new SpinnerNumberModel(400,0,((BasicSimWorld)world).getWorldDimensions()[0],1));
            zS=new JSpinner(new SpinnerNumberModel(500,0,((BasicSimWorld)world).getWorldDimensions()[2],1));
            bS=new JSpinner(new SpinnerNumberModel(0,0,360,1));
            cS=new JSpinner(new SpinnerNumberModel(7726,0,8000,1));
                   
            //set colors
            xS.setBackground(Color.yellow);
            zS.setBackground(Color.yellow);
            bS.setBackground(Color.yellow);
            cS.setBackground(Color.yellow);
             
            title = new JLabel("Initialize Robot:");
            xLabel=new JLabel("Initial X value:");
            zLabel=new JLabel("Initial Z value:");
            bLabel=new JLabel("Initial bearing:");
            cLabel=new JLabel("Enter the battery");
            cLabel2=new JLabel("voltage(mV):");
            //set colors
            title.setForeground(Color.yellow);
            xLabel.setForeground(Color.lightGray);
            zLabel.setForeground(Color.lightGray);
            bLabel.setForeground(Color.lightGray);
            cLabel.setForeground(Color.lightGray);
            cLabel2.setForeground(Color.lightGray);

            (OK=new JButton("OK")).addActionListener(this);
            (cancel=new JButton("Cancel")).addActionListener(this);
             
            OK.setMargin(new Insets(1,1,1,1));
            cancel.setMargin(new Insets(1,1,1,1));
        
            //set color
            OK.setBackground(Color.yellow);
            OK.setForeground(Color.darkGray);
            cancel.setBackground(Color.yellow);
            cancel.setForeground(Color.darkGray);

            int r=15;
            this.add(title,new AbsoluteConstraints(6,7));
            this.add(xLabel,new AbsoluteConstraints(6,30));
            this.add(xS,new AbsoluteConstraints(30+r,55));
            this.add(zLabel,new AbsoluteConstraints(6,85));
            this.add(zS,new AbsoluteConstraints(30+r,110));
            this.add(bLabel,new AbsoluteConstraints(6,140));
            this.add(bS,new AbsoluteConstraints(50+r,165));
            this.add(cLabel,new AbsoluteConstraints(6,195));
            this.add(cS,new AbsoluteConstraints(30+r,237));
            this.add(cLabel2,new AbsoluteConstraints(23,212));
            
            this.add(OK,new AbsoluteConstraints(32,270));
            this.add(cancel,new AbsoluteConstraints(62,270));

            this.setBackground(Color.darkGray);
            //this.show();
        }
        
        public void callOK(){
                        
            try{
                int x=Integer.parseInt(xS.getValue().toString());
                int z=Integer.parseInt(zS.getValue().toString());
                int b=Integer.parseInt(bS.getValue().toString());
                int c=Integer.parseInt(cS.getValue().toString());

                long X=(long)x;
                long Z=(long)z;
                long B=(long)b;

                // create the robot
                SimRCX rcx=new SimRCX(world, controller, 30.0,40.0,60.0,"robot",X,0.0,Z,0.0,B,c);

                // add it to the controller
                controller.initController(rcx);

                // ... and add it to the world
                world.addObject((SimObject)rcx);

                display.repaint();
                
            }
            catch(Exception a){
                main.MainInterface.displayMessage("ERROR: Incorrect Input Type | FIX: Please try again, only entering INTEGERs");
                
            }
        
        }

        /**  Action event handler - creates a robot according to user input,
         *  having check that the input is valid.
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource()==OK)
            {
                
                try{
                    int x=Integer.parseInt(xS.getValue().toString());
                    int z=Integer.parseInt(zS.getValue().toString());
                    int b=Integer.parseInt(bS.getValue().toString());
                    int c=Integer.parseInt(cS.getValue().toString());
                    
                    long X=(long)x;
                    long Z=(long)z;
                    long B=(long)b;
                    
                    // create the robot
                    SimRCX rcx=new SimRCX(world, controller, 30.0,40.0,60.0,"robot",X,0.0,Z,0.0,B,c);

                    // add it to the controller
                    controller.initController(rcx);

                    // ... and add it to the world
                    world.addObject((SimObject)rcx);

                    display.repaint();

                    hide();
                }
                catch(Exception a){
                    main.MainInterface.displayMessage("ERROR: Incorrect Input Type | FIX: Please try again, only entering INTEGERs");
                    hide();
                }
            }
            else
            {
                hide();
            }
            
            try{sim.setSize(sim.getWidth(),sim.getHeight());}
            catch(Exception e1){};
	}
}
