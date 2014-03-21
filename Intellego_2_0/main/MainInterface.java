package main;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;
import NetBeansResources.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

/** The main GUI.
 * @author Graham Ritchie
 * @modifyer Simon Zienkiewcz
 */
public class MainInterface extends JFrame
{
    /** main desktop pane */    
    private static JDesktopPane mainPane;
    /** the simulator frame */    
    private static SimUI frame = null;
    /** the help dialog frame */    
    private HelpDialog helpDialog=null;
    /** the about dialog */    
    private About about = null;
    /**
    * Sets up the JDesktopPane
    */
    public MainInterface()
    {
        super("Intellego 2.0");

        // Make the main window indented 50 pixels from each edge of the screen.
        int inset = 50;
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset*2, screenSize.height-inset*2);

        //load icon image from a file
        try{ 
                BufferedImage simIcon= ImageIO.read(new File("images\\lego.png"));
                this.setIconImage(simIcon);
        }
        catch(Exception e){}

        // Quit the whole program when the main window closes.
        addWindowListener(new WindowAdapter() 
        {
            public void windowClosing(WindowEvent e) 
            {
                Intellego.addToLog("MainInterface.init(): System quitting");
                System.exit(0);
            }
        });

        // set up the main pane
        mainPane=new JDesktopPane();

        setContentPane(mainPane);
        mainPane.setBackground(Color.darkGray);
        setJMenuBar(createMenuBar());
        
        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

    }

    /**
    * Creates a new code editor window
    */
    public static void createCodeEditorFrame() 
    {
        
        CodeEditor frame = new CodeEditor();
        frame.setVisible(true);
        mainPane.add(frame);
        
        try 
        {
            frame.setSelected(true);
        }
        catch (Exception e) 
        {
            Intellego.addToLog("MainInterface.createFrame(): Failed to create internal code editor frame properly: "+e);
        }
    }

    /**
    * Creates a new simulator window
    */
    public void createSimulatorFrame() 
    {
        if(frame == null || !frame.isVisible()){
            frame = new SimUI();
            frame.setVisible(true);
            mainPane.add(frame);
            try 
            {
                frame.setSelected(true);
            }
            catch (Exception e) 
            {
                Intellego.addToLog("MainInterface.createFrame(): Failed to create internal simulator frame properly: "+e);
            }
        }
        else displayMessage("ERROR: more than one active SIMULATOR WINDOW | ONLY ONE is allowed");
    }
    
     /** Creates a new simulator window with the specified controller preloaded
     * @param file the controller file wanting to be opened in the simulator window
     */
    public static void createSimulatorFrame(File file)//String className) 
    {
        // prompt the user to open a new simworld and then open 
        // the controller in that simworld
        if(frame == null || !frame.isShowing()){
            frame = new SimUI(file);
            frame.setVisible(true);
            mainPane.add(frame);
            try 
            {
                frame.setSelected(true);
            }
            catch (Exception e) 
            {
                Intellego.addToLog("MainInterface.createFrame(): Failed to create internal simulator frame properly: "+e);
            }
        }
        
        // open the controller fine in the simworld that is 
        // opened in the simulator window
        else 
        {
            
            frame.openCurrentController(file);
            
            
            
        }
        //displayMessage("ERROR: more than one active SIMULATOR WINDOW | ONLY ONE is allowed");
    }

    /** Creates a new window to display messages from external processes
     * @return the ExternalMessager frame
     * @param num the index, indicating which type of external messenger is being used
     */
    public static ExternalMessager createExternalMessagerFrame(int num) 
    {
        ExternalMessager frame = new ExternalMessager(num);
        frame.setVisible(true);
        mainPane.add(frame);
        try 
        {
            frame.setSelected(true);
        }
        catch (Exception e) 
        {
            Intellego.addToLog("MainInterface.createFrame(): Failed to create internal frame properly: "+e);
        }
        return frame;
    }

    /**
    * Creates the menu bar for the main desktop pane
    *
    * @return the menu bar
    */
    private JMenuBar createMenuBar() 
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem codeEditor = new JMenuItem(" Code Editor",new ImageIcon("images/code.gif"));
        codeEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.ALT_MASK));
        JMenuItem simulator = new JMenuItem(" Simulator",new ImageIcon("images/execute.gif"));
        simulator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.ALT_MASK));
        
        JMenuItem helpI = new JMenuItem(" Help",new ImageIcon("images/help.gif"));
        helpI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
        JMenuItem aboutI = new JMenuItem("About Intellego 2.0");
                        
        //set colors
        toolsMenu.setBackground(Color.darkGray);
        toolsMenu.setForeground(Color.lightGray);
        help.setBackground(Color.darkGray);
        help.setForeground(Color.lightGray);
        helpI.setBackground(Color.darkGray);
        helpI.setForeground(Color.lightGray);
        aboutI.setBackground(Color.darkGray);
        aboutI.setForeground(Color.lightGray);
        codeEditor.setBackground(Color.darkGray);
        codeEditor.setForeground(Color.red.darker());
        simulator.setBackground(Color.darkGray);
        simulator.setForeground(Color.yellow);

        helpI.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(helpDialog == null){
                    //create the help JFrame
                    helpDialog = new HelpDialog();
                }
                
                else{
                    helpDialog.setVisible(true);
                }
            }
        });
        aboutI.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(about == null){
                    //create the about JFrame
                    about = new About();
                }
                
                else{
                    about.setVisible(true);
                }
            }
        });
        codeEditor.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                createCodeEditorFrame();
            }
        });

        simulator.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                createSimulatorFrame();
            }
        });

        toolsMenu.add(codeEditor);
        toolsMenu.addSeparator();
        toolsMenu.add(simulator);
        
        help.add(helpI);
        help.addSeparator();
        help.add(aboutI);
        
        menuBar.setLayout(new AbsoluteLayout());
        menuBar.setBackground(Color.DARK_GRAY);
        menuBar.add(toolsMenu,new AbsoluteConstraints(3,0));
        menuBar.add(help,new AbsoluteConstraints(50,0));
        
        //strictly used to extend the menu bar to proper height
        JButton sizeButton = new JButton("hi");
        sizeButton.setSize(new Dimension(20,10));
        sizeButton.setMargin(new Insets(0,0,0,0));
        sizeButton.setVisible(false);
        menuBar.add(sizeButton,new AbsoluteConstraints(263,0));    
        
        return menuBar;
    }

    /**
    * Displays messages to the user in a dialog box
    *
    * @param message the message to be displayed to the user
    */
    public static void displayMessage(String message)
    {
            // pop up a dialog box with the message
            IntellegoDialog dialog=new IntellegoDialog(message);

    }
}
