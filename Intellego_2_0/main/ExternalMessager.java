package main;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

/** Provides a window which displays messages from external processes to the user,
 * within the main interface.
 * @author Graham Ritchie
 * @modifyer Simon Zienkiewicz
 */
public class ExternalMessager extends JInternalFrame
{
    /** The pane which communicates to the user the results of the proccesses. */    
    private JEditorPane messagePane;
    static final int xOffset = 30, yOffset = 30;
    static int openFrameCount = 0;

    /** Sets up the message display window
     * @param num the index refering to the type of external messenger created
     */
    public ExternalMessager(int num)
    {
        //super("Compile/Upload Status Window: ",true,true,true,true);
        super("",true,true,true,true);

        if(num ==0)this.setTitle("Compile Status Window:");
        else if(num ==1) this.setTitle("Upload Status Window:");
        else if(num ==3) this.setTitle("Upload Firmware Status Window:");

        openFrameCount++;

        // create and add the editor pane
        messagePane=new JEditorPane();
        messagePane.setBackground(Color.darkGray);
        messagePane.setForeground(Color.yellow);
        messagePane.setVisible(true);
        messagePane.setEditable(false);

        // put it in a scroll pane
        JScrollPane messageScrollPane = new JScrollPane(messagePane);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messageScrollPane.setPreferredSize(new Dimension(400,600));
        messageScrollPane.setMinimumSize(new Dimension(10, 10));
        (messageScrollPane.getVerticalScrollBar()).setBackground(Color.darkGray);
        (messageScrollPane.getHorizontalScrollBar()).setBackground(Color.darkGray);
        (messageScrollPane.getViewport()).setBackground(Color.darkGray);

        // and add this to a main content pane
        JPanel contentPane = new JPanel();
        BoxLayout box = new BoxLayout(contentPane, BoxLayout.X_AXIS);
        contentPane.setLayout(box);
        contentPane.add(messageScrollPane);
        setContentPane(contentPane);

    // set the window size
    setSize(500,300);

    // and set the window's location.
    setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }

    /**
    * Appends a message to the message pane of the main window
    *
    * @param text the test to be appended
    */
    public void append(String text)
    {
        messagePane.setText(messagePane.getText()+text);
    }

    /** Returns the status of the compilation of the code.
     * @return true if the compile was successful, false otherwise
     */        
    public boolean successfullCompile(){
        if(messagePane.getText().equals(""))
        {
            return true;
        }
        else
        { 
            return false;
        }


    }
}
