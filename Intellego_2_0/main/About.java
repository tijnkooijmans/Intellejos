package main;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;
import simworldobjects.*;
import main.*;
import NetBeansResources.*;
import javax.swing.plaf.basic.BasicBorders.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import java.awt.event.ItemListener.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

/**
* Provides a dialog box to get color selection parameters from the user
* @author Simon Zienkiewicz
* 
*/ 
public class About extends JFrame
{
       
    /** Displays a dialog box to get initial robot parameters, and creates and
     * creates a new SimRCX wth these values.
     *
     */

    public About()
    {
        //sets the properties of the popup window
        setTitle("Intellego 2.0 About");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(screenSize.width/2-200,screenSize.height/2-150, 410, 360);
        this.setResizable(false);
                
        Container mainPanel=getContentPane();
        mainPanel.setLayout(new BorderLayout(1,1));
        
        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        
        setVisible(true);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                
        //load icon image from a file
        try{ 
                BufferedImage simIcon= ImageIO.read(new File("images\\lego.png"));
                //this.setIconImage(simIcon);
        }
        catch(Exception e){}
         
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.darkGray);
        
        tabbedPane.addTab("About",new GetImage());
        tabbedPane.setSelectedIndex(0);
        tabbedPane.addTab("Details",new GetImage("images\\details.gif"));
        mainPanel.add(tabbedPane);
        mainPanel.setBackground(Color.darkGray);
           
        this.show();
    }
}