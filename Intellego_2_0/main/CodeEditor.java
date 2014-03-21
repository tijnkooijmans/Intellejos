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
import java.net.*;
import javax.swing.*;
import java.beans.*;

/** Provides a simple textual code editor
 * @author Graham Ritchie
 * @modifyer Simon Zienkiewicz
 */
public class CodeEditor extends JInternalFrame
{ 
    static final int xOffset = 30, yOffset = 30;
    static int openFrameCount = 0;
	
    /** the current file open in the Code Editor */    
    private File currentFile;
	
    /** The name and directory of the current open file. */    
    private String currentFileName;
    
    /** The contents of the file up to the last save point. */    
    private String oldFileContents;
    /** The current contents including changes made from the last save point of the file. */    
    private String newFileContents;
	
    /** The current file's directory */    
    private File currentDir;
	
    /** The editor pane within the current frame. */    
    private JEditorPane codePane;
    
    /** A timer used to display saving messages to the user. */    
    private Timer saveTimer;
    /** The name of the current open file */    
    private String fileString;

    /** Creates a new instance of CodeEditor */    
    public CodeEditor() 
    {
        super("Code Editor: (no file)",true,true,true,true);
        this.setFrameIcon(new ImageIcon("images/code.gif"));
	++openFrameCount;
        
        
        // create the code editor GUI and put it in the window...
		
            // add the menu bar
            setJMenuBar(createMenuBar());
		
            // create and add the editor pane
            codePane=new JEditorPane();
            codePane.setCaretColor(Color.white);
            codePane.setVisible(true);
            codePane.setEditable(true);
            codePane.setBackground(Color.darkGray);
            codePane.setForeground(Color.white);
            
            
            this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
            
            
            //setup save timer
            saveTimer = new Timer(3000,new TListener());
		
            // put it in a scroll pane
            JScrollPane codeScrollPane = new JScrollPane(codePane);
            codeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            codeScrollPane.setPreferredSize(new Dimension(400,400));
            codeScrollPane.setMinimumSize(new Dimension(20,20));
            (codeScrollPane.getVerticalScrollBar()).setBackground(Color.DARK_GRAY);
            (codeScrollPane.getHorizontalScrollBar()).setBackground(Color.DARK_GRAY);
            codeScrollPane.setBackground(Color.DARK_GRAY);
	    
            // and add this to a main content pane
            JPanel contentPane = new JPanel();
            BoxLayout box = new BoxLayout(contentPane, BoxLayout.X_AXIS);
            contentPane.setLayout(box);
            contentPane.add(codeScrollPane);
            setContentPane(contentPane);
		
            // set the window size
            setSize(400,600);

            // and set the window's location.
            setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }
    
    //Modified by: Simon Zienkiewicz
    /** Creates a menubar for the Code Editor
     * @return the created menu bar
     */    
    private JMenuBar createMenuBar()
    {
	JMenuBar menuBar = new JMenuBar();
		
        // create the file menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setBackground(Color.darkGray);
        fileMenu.setForeground(Color.lightGray);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
		
        // create the file menu items
        ImageIcon newI = new ImageIcon("images/new.gif");
        Image image1 = (newI.getImage().getScaledInstance(18,18,0));
        ImageIcon newIc = new ImageIcon(image1);
        
        JMenuItem newFile = new JMenuItem(" New",newIc);
        newFile.setBackground(Color.darkGray);
        newFile.setForeground(Color.lightGray);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
        
        ImageIcon openI = new ImageIcon("images/open.gif");
        Image image2 = (openI.getImage().getScaledInstance(18,18,0));
        ImageIcon openIc = new ImageIcon(image2);
        
        JMenuItem open = new JMenuItem(" Open",openIc);
        open.setBackground(Color.darkGray);
        open.setForeground(Color.lightGray);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
        
        ImageIcon saveI = new ImageIcon("images/save.gif");
        Image image3 = (saveI.getImage().getScaledInstance(18,18,0));
        ImageIcon saveIc = new ImageIcon(image3);
        
        JMenuItem save=new JMenuItem(" Save",saveIc);
        save.setBackground(Color.darkGray);
        save.setForeground(Color.lightGray);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        
        JMenuItem saveAs=new JMenuItem("        Save As");
        saveAs.setBackground(Color.darkGray);
        saveAs.setForeground(Color.lightGray);
                
        JMenuItem close = new JMenuItem("        Exit");
        close.setBackground(Color.darkGray);
        close.setForeground(Color.lightGray);
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
		
        // create the action listeners
        newFile.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                createNewFile();
            }
        });
		
        open.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                openFile(); 
            }
        });
		
        save.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                saveFile();
            }
        });
		
        saveAs.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                saveFileAs();
            }
        });
		
        close.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                closeFile();
            }
        });
		
        // add the menu items to the menu
        fileMenu.addSeparator();
        fileMenu.add(newFile);
        fileMenu.addSeparator();
        fileMenu.add(open);
        fileMenu.addSeparator();
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.addSeparator();
        fileMenu.add(close);
      		
        // create the lejos menu
        JMenu lejosMenu = new JMenu("leJOS");
        lejosMenu.setMnemonic(KeyEvent.VK_J);
        lejosMenu.setBackground(Color.darkGray);
        lejosMenu.setForeground(Color.lightGray);
	
        // create the lejos menu items
        ImageIcon rcxI = new ImageIcon("images/RCX.jpg");
        Image image7 = (rcxI.getImage().getScaledInstance(20,26,0));
        ImageIcon rcxIc = new ImageIcon(image7);
        
        JMenuItem compile = new JMenuItem("   Compile",rcxIc);
        compile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
        
        ImageIcon linkI = new ImageIcon("images/irtower.gif");
        Image image5 = (linkI.getImage().getScaledInstance(26,26,0));
        ImageIcon linkIc = new ImageIcon(image5);
        
        JMenuItem download = new JMenuItem("Link & Upload",linkIc);
        download.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0));
        JMenuItem firmwaredl = new JMenuItem("Upload Lejos Firmware");
        firmwaredl.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,0));
        //set colors
        compile.setBackground(Color.darkGray);
        compile.setForeground(Color.lightGray);
        download.setBackground(Color.darkGray);
        download.setForeground(Color.lightGray);
        firmwaredl.setBackground(Color.darkGray);
        firmwaredl.setForeground(Color.lightGray);
        		
        // create the action listeners
		
        compile.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                lejosCompileFile();
            }
        });
		
        download.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                downloadFile();
            }
        });
                
        firmwaredl.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                downloadFirmware();
            }
        });
		
        // add the menu items to the menu
        lejosMenu.add(compile);
        lejosMenu.addSeparator();
        lejosMenu.add(download);
        lejosMenu.addSeparator();
        lejosMenu.add(firmwaredl);
      
   
        // create the sim menu
        JMenu simMenu = new JMenu("Simulator");
        simMenu.setMnemonic(KeyEvent.VK_I);
        simMenu.setBackground(Color.darkGray);
        simMenu.setForeground(Color.lightGray);
		
        // create the lejos menu items
        
        ImageIcon manI = new ImageIcon("images/hips.gif");
        Image image6 = (manI.getImage().getScaledInstance(26,26,0));
        ImageIcon manIc = new ImageIcon(image6);
        
        JMenuItem javaCompile = new JMenuItem(" Compile",manIc);
        javaCompile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,0));
        JMenuItem openInSim = new JMenuItem("Open controller in simulator");
        openInSim.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8,0));
	
        //set colors
        javaCompile.setBackground(Color.darkGray);
        javaCompile.setForeground(Color.lightGray);
        openInSim.setBackground(Color.darkGray);
        openInSim.setForeground(Color.lightGray);
        
        // create the action listeners
	openInSim.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                openFileInSim();
            }
        });
		
        javaCompile.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                javaCompileFile();
            }
        });
		
        // add the menu items to the menu
        simMenu.add(javaCompile);
        simMenu.addSeparator();
        simMenu.add(openInSim);
		
        // add the menus to the menu bar

        menuBar.setLayout(new AbsoluteLayout());
        menuBar.setBackground(Color.DARK_GRAY);
        menuBar.add(fileMenu,new AbsoluteConstraints(3,0));
        menuBar.add(lejosMenu,new AbsoluteConstraints(38,0));
        menuBar.add(simMenu,new AbsoluteConstraints(89,0));
                
        //strictly used to extend the menu bar to proper height
        JButton sizeButton = new JButton("hi");
        sizeButton.setSize(new Dimension(20,10));
        sizeButton.setMargin(new Insets(0,0,0,0));
        sizeButton.setVisible(false);
        menuBar.add(sizeButton,new AbsoluteConstraints(263,0));
 	
        // return the final menu bar
        return menuBar;
    }
	
    /** Creates a new editor pane which hosts a new file. */
    public void createNewFile()
    {
	MainInterface.createCodeEditorFrame();
    }
   
    /** Opens a file which currently exists */
    private void openFile()
    {
        JFileChooser chooser=new JFileChooser(new File(System.getProperties().getProperty("user.dir"),"controllers"));
	
         for(int a=0;a<chooser.getComponentCount();a++){
            Container a1 =(Container)chooser.getComponent(a);
                if(a1 instanceof JTextField ||a1 instanceof JComboBox || a1 instanceof JButton ){
                    a1.setBackground(Color.darkGray);
                    a1.setForeground(Color.lightGray);
                 }
            for(int b=0;b<((Container)chooser.getComponent(a)).getComponentCount();b++){
                Container b1 =(Container)((Container)chooser.getComponent(a)).getComponent(b);
                if(b1 instanceof JTextField ||b1 instanceof JComboBox || b1 instanceof JButton ){
                    b1.setBackground(Color.darkGray);
                    b1.setForeground(Color.lightGray);
                }
                 for(int c=0;c<((Container)((Container)chooser.getComponent(a)).getComponent(b)).getComponentCount();c++){
                    Container c1 =(Container)((Container)((Container)chooser.getComponent(a)).getComponent(b)).getComponent(c);
                    if(c1 instanceof JTextField ||c1 instanceof JComboBox || c1 instanceof JButton ){
                        c1.setBackground(Color.darkGray);
                        c1.setForeground(Color.lightGray);
                     }
                }
            }
        }
    
	// add a filename filter for java files
	String[] extensions={".java"};
	chooser.addChoosableFileFilter(new FileChooserFilter(extensions,"Java Controller Files"));
        chooser.showOpenDialog(this);
        
        File file=chooser.getSelectedFile();
        
        if(file == null){
            //do nothing
        }
        else
        {
            //try to open the file
                
            try 
            {
                URL filePath=chooser.getSelectedFile().toURL();

                // set this file as the page in the codePane
                codePane.setPage(filePath);

                // set file as current file
                currentFile=file;

                // set current directory
                currentDir=chooser.getCurrentDirectory();
                codePane.updateUI();
                codePane.setVisible(true);

                // and change the title
                fileString = file.toString();
                super.setTitle("Code Editor:  "+fileString);

                this.oldFileContents = codePane.getText();
            }
            catch (Exception e)
            {
                MainInterface.displayMessage("Cannot open file");
                Intellego.addToLog("CodeEditor.openFile(): Failed to open file: "+e);
            }
        }
        
    }
	
    /**
    * Saves the current file
    */
    public void saveFile()
    {
        if (currentFile!=null)
	{
            // save file
            try
            {
                FileWriter fw=new FileWriter(currentFile);
		fw.write(codePane.getText());
		fw.close();
                super.setTitle("Code Editor:  ....SAVING.... ");
                this.saveTimer.start();
                
                this.oldFileContents = this.codePane.getText();
            }
            
            catch (Exception e)
            {
                MainInterface.displayMessage("Cannot save file");
		Intellego.addToLog("CodeEditor.saveFile(): Save to file failed: "+e);
            }
        }
	else
	{
            // no current file, so it must be new, so save as
            saveFileAs();
        }
    }
	
    /** Saves the file under a specified name. */    
    private void saveFileAs()
    {
        JFileChooser chooser=new JFileChooser(new File(System.getProperties().getProperty("user.dir"),"controllers"));
        
    	 //set color
        for(int a=0;a<chooser.getComponentCount();a++){
            Container a1 =(Container)chooser.getComponent(a);
                if(a1 instanceof JTextField ||a1 instanceof JComboBox || a1 instanceof JButton ){
                    a1.setBackground(Color.darkGray);
                    a1.setForeground(Color.lightGray);
                 }
            for(int b=0;b<((Container)chooser.getComponent(a)).getComponentCount();b++){
                Container b1 =(Container)((Container)chooser.getComponent(a)).getComponent(b);
                if(b1 instanceof JTextField ||b1 instanceof JComboBox || b1 instanceof JButton ){
                    b1.setBackground(Color.darkGray);
                    b1.setForeground(Color.lightGray);
                }
                 for(int c=0;c<((Container)((Container)chooser.getComponent(a)).getComponent(b)).getComponentCount();c++){
                    Container c1 =(Container)((Container)((Container)chooser.getComponent(a)).getComponent(b)).getComponent(c);
                    if(c1 instanceof JTextField ||c1 instanceof JComboBox || c1 instanceof JButton ){
                        c1.setBackground(Color.darkGray);
                        c1.setForeground(Color.lightGray);
                     }
                }
            }
        }
        
        int returnValue=chooser.showSaveDialog(this);
        
	if(returnValue==JFileChooser.APPROVE_OPTION)
	{
            // save file as
            try
            {
                FileWriter fw=new FileWriter(chooser.getSelectedFile().getPath()+".java");
		fw.write(codePane.getText());
		fw.close();
                super.setTitle("Code Editor:  "+chooser.getSelectedFile().getPath()+".java");
                // set file as current file
                currentFile=new File(chooser.getSelectedFile().getPath()+".java");
			
                // set current directory
                currentDir=chooser.getCurrentDirectory();
            }
            catch (Exception e)
            {
            	MainInterface.displayMessage("Cannot save file");
		Intellego.addToLog("CodeEditor.saveFile(): Save to file failed: "+e);
            }
	}
	
        else
	{	
            // cancel, do nothing atm
        }
    }
    
    /**
    * Closes the current file
    */
    private void closeFile()
    {
        if(oldFileContents != null && this.codePane.getText() != null)
        {
            if(!this.oldFileContents.equals(this.codePane.getText())){
               
                VerificationPopUp check = new VerificationPopUp();
                check.createPopUpWindow("Save Updated Changes",370,95,"Do you want to Save your last changes?",1,this);
            }

            else{
                // save the file
                saveFile();

                // and get rid of it
                currentFile=null;
                codePane.setVisible(false);

                // and change the title
                super.setTitle("Code Editor:  (no file)");
                this.dispose();
            }
        }
        
        else if(oldFileContents == null && this.codePane.getText().trim().length() != 0)
        {
            VerificationPopUp check = new VerificationPopUp();
            check.createPopUpWindow("Save Updated Changes",370,95,"Do you want to Save your last changes?",1,this);
        }
        
        else
        {
            // and get rid of it
            currentFile=null;
            codePane.setVisible(false);

            // and change the title
            super.setTitle("Code Editor:  (no file)");
            this.dispose();
        }
    }
	
    /**
    * Compiles the current file with lejosc
    */
    private void lejosCompileFile()
    {   
        super.setTitle("Code Editor:  ....LEJOS COMPILE.... ");
        this.saveTimer.start();
                
        if(currentFile!=null)externalCommand("lejosc "+currentFile.toString());
	else MainInterface.displayMessage("Cannot compile an empty file");
    }
	
    /**
    * Compiles the current file with javac
    */
    private void javaCompileFile()
    {   
        //save the file before compiling
        //saveFile();
        
        super.setTitle("Code Editor:  ....JAVA COMPILE.... ");
        this.saveTimer.start();
        
        if(currentFile!=null) externalCommand("javac "+currentFile.toString());
 	else MainInterface.displayMessage("Cannot compile an empty file");
    }
    
    /**
    * Downloads the firmware onto the RCX
    * Modified by: Simon Zienkiewicz
    */
    private void downloadFirmware(){
       VerificationPopUp check = new VerificationPopUp();
       check.createPopUpWindow("LEJOS Firmware Check:",370,95,"Are you sure that you want to upload the LEJOS firmware?",0,this);
       check.dispose();
    }
	
    /**
    * Links and downloads the current file
    */
    private void downloadFile()
    {
        if(currentFile!=null)
	{
            ControllerDL c=new ControllerDL(currentFile,currentDir);
        }
	else
	{
            MainInterface.displayMessage("You need to open a controller first");
	}
    }
	
    /** Opens the current Controller file in a Simulator Window. */    
    public void openFileInSim()
    {
        if(currentFile!=null)
	{
            String fileName = (currentFile.getAbsolutePath()).substring(0,(currentFile.getAbsolutePath()).length() - 5) +".class";
            MainInterface.createSimulatorFrame(new File(fileName));
   	}
	else
	{
            MainInterface.displayMessage("You need to open a controller first");
	}
    }

    /** Processes all external calls, i.e calls to lejos & lejosc
     * @param cmd the command wanting execution
     */
    private void externalCommand(String cmd)
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
                ExternalMessager output=MainInterface.createExternalMessagerFrame(0);
                
                while ((len = ees.read(buffer)) != -1)
		{
                    String eo = new String (buffer, 0, len);
                    output.append(eo);            
                }
                external.waitFor();
                
                if(output.successfullCompile()){
                
                    output.append("\n"+"\n"+"   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
                    output.append("\n"+"\n"+"     |  COMPILING SUCCESSFUL: NO ERRORS FOUND  |");
                    output.append("\n"+"\n"+"   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
                }
                
                else{
                    
                    output.append("\n"+"\n"+"   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
                    output.append("\n"+"\n"+"     |  COMPILING FAILED: ERRORS FOUND   |");
                    output.append("\n"+"\n"+"   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
                
                }
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
    
    /** Sets the title of the frame to the file name. */    
    public void setTitletoFileName(){
        super.setTitle("Code Editor: "+fileString);
    }
       
    /** Used to display saving message when the user saves his/her file. */    
    private class TListener implements ActionListener {
        /** Invoked when an action occurs.
         * @param e the event that occured
         */
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == saveTimer){
                setTitletoFileName();
                saveTimer.stop();
            }
        }
        
    }
}