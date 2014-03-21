/*
 * IntroScene.java
 *
 * Created on August 8, 2003, 3:09 PM
 */

package intellego;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;
import main.*;
import NetBeansResources.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

/** Loads the introscene while the application is being complied
 * @author Simon Zienkiewicz
 */
public class IntroScene extends JDialog implements ActionListener {
    
    private Timer exitTimer;
    
    /** Creates a new instance of IntroScene */
    public IntroScene() {
                
        //place JDialog in the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(screenSize.width/2-200,screenSize.height/2-150, 400, 300);
        
        this.setResizable(false);
                
        Container mainPanel=getContentPane();
        mainPanel.setLayout(new BorderLayout(1,1));
        
        mainPanel.add(new GetImage());
        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
                   
        this.show();
        exitTimer = new Timer(8000,this);
        exitTimer.start();
       
    }
    
    /** Execution method for timer event.
     * @param e executed event
     */    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exitTimer){
            this.dispose();
            System.exit(0);
        }
    }
    
    /** The main class.
     * @param args arguements
     */    
    public static void main(String[] args){
        IntroScene intro = new IntroScene();
    }
}
