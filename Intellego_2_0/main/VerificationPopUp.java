package main;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;
import simworldobjects.*;
import main.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


/**
* Provides a dialog box to ask the user whether or not they want to download the lejos firmware
*
* @author Simon Zienkiewicz
* 
*/ 
public class VerificationPopUp extends JDialog implements ActionListener, WindowFocusListener, WindowListener
{
    private JLabel xLabel;
    private JButton OK, cancel;
    private CodeEditor window;
    private int type;
    private boolean exitWin = false;
    
    
    /** Displays a dialog box to make the user verify that they want to upload the
     * firmware onto the real robot.
     * @param title the title of the diaglog
     * @param length the lenth of the dialog box
     * @param width the width of the dialog box
     * @param message the message in the dialog
     * @param t the type of dialog
     * @param win the CodeEditor sending the request
     */
    public void createPopUpWindow(String title, int length, int width, String message, int t, CodeEditor win)
    {
        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
                
        window = win;
        type = t;
        setTitle(title);
        setSize(length,width);
        setLocation(400,400);
        setVisible(true);
        this.setResizable(false);
                
        Container Panel1=new Container();
        Panel1.setLayout(new FlowLayout());     

        Container Panel2=new Container();
        Panel2.setLayout(new FlowLayout());  

        xLabel=new JLabel(message);
        xLabel.setForeground(Color.lightGray);

        (OK=new JButton("OK")).addActionListener(this);
        (cancel=new JButton("Cancel")).addActionListener(this);
        this.addWindowFocusListener(this);
        this.addWindowListener(this);
        
        //set color
        OK.setBackground(Color.lightGray);
        OK.setForeground(Color.darkGray);
        cancel.setBackground(Color.lightGray);
        cancel.setForeground(Color.darkGray);

        Panel2.add("South",OK);
        Panel2.add("South",cancel);

        Panel1.add("North",xLabel);
        Panel1.add("South",Panel2);
        
        this.getContentPane().setBackground(Color.darkGray);
        this.getContentPane().add(Panel1);
        this.show();
        this.setLocationRelativeTo(window);
        
    }

    /**
    *  Action event handler - downloads the lejos firmware to the robot
    *
    *  @param e the action event
    */
    public void actionPerformed(ActionEvent e)
    {
        
        if (e.getSource()==OK)
        {
            switch(type){
            
                case 0:  externalCommand("lejosfirmdl",2);
                         
                case 1:  exitWin = true;
                         this.window.saveFile();
                         this.window.setVisible(false);
		
                        // and change the title
                        this.window.setTitle("Code Editor:  (no file)");
                        this.window.dispose();
                
                
                dispose();
                                
            
            }
           
        }      
        else{
            if(type ==1){
                this.window.setVisible(false);
		// and change the title
                this.window.setTitle("Code Editor:  (no file)");
                exitWin = true;
                this.window.dispose();
            }
            
            exitWin = true;
            dispose();}
    }
            
    /** Processes all external calls, i.e calls to lejos & lejosc
     * @param cmd the command
     * @param num the type of external command
     */    
    private void externalCommand(String cmd, int num)
    {
        int len;
        byte buffer[] = new byte[1000];
        Intellego.addToLog("CodeEditor.externalCommand(): Processing External Command: "+cmd);
	try
	{
            Process external=Runtime.getRuntime().exec(cmd);
            InputStream ees = external.getErrorStream();
            try 
            {
                ExternalMessager output=MainInterface.createExternalMessagerFrame(num);
                while ((len = ees.read(buffer)) != -1)
		{
                    String eo = new String (buffer, 0, len);
                    output.append(eo);            
                }
                external.waitFor();
             }             
             catch (Exception e) 
             {
             	Intellego.addToLog("CodeEditor.externalCommand(): error: "+e.getMessage());
             }
	}
	catch (Exception e) 
	{
            Intellego.addToLog("CodeEditor.externalCommand(): error: "+e.getMessage());
            
        }   
    }
     
    public void windowGainedFocus(WindowEvent e) {
    }
    
    public void windowLostFocus(WindowEvent e) {
        if(exitWin == false){
            //sound
            this.getToolkit().beep();
            this.show();
        }
        
    }
    
    public void windowActivated(WindowEvent e) {
    }
    
    public void windowClosed(WindowEvent e) {
    }
    
    public void windowClosing(WindowEvent e) {
         exitWin = true;
         dispose();
    }
    
    public void windowDeactivated(WindowEvent e) {
    }
    
    public void windowDeiconified(WindowEvent e) {
    }
    
    public void windowIconified(WindowEvent e) {
    }
    
    public void windowOpened(WindowEvent e) {
    }
    
}
