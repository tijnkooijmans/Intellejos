package main;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Providea a general purpose dialog box to display messages from the
* system to the user.
*
* @author Graham Ritchie
* @modifyer: Simon Zienkiewicz
*/
class IntellegoDialog extends JDialog implements ActionListener, WindowFocusListener, WindowListener
{
    JLabel message;
    JButton OK;
    static int openFrameCount=5;
    static final int xOffset = 30, yOffset = 30;
    private boolean exitWin = false;

    /**
    * Creates and displays the dialog box
    *
    * @param text the message to be displyed to the user
    */
    public IntellegoDialog(String text)
    {
        openFrameCount++;
        setSize(600,100);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);

        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

        Container c=getContentPane();
        c.setLayout(new BorderLayout(1,1));

        Container top=new Container();
        top.setLayout(new FlowLayout());

        Container bottom=new Container();
        bottom.setLayout(new FlowLayout());

        (OK=new JButton("OK")).addActionListener(this);
        message=new JLabel(text);
        message.setForeground(Color.lightGray);
        OK.setBackground(Color.lightGray);
        OK.setForeground(Color.DARK_GRAY);

        top.add("Center",message);
        bottom.add("Center",OK);

        c.setBackground(Color.darkGray);
        c.add("North",top);
        c.add("South",bottom);
                
        this.addWindowFocusListener(this);
        this.addWindowListener(this);
                
        show();
    }

    /**
    * Disposes with the dialog box when the user clicks on OK
    *
    * @param e the action event
    */
    public void actionPerformed(ActionEvent e)
    {
            exitWin = true;
            dispose();
            openFrameCount--;
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
