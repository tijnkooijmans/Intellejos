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

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.ToolTipManager;
import java.net.URL;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.*;
import java.util.*;

/** Provides a dialog for the user to view help files in html format.
 * (A kind of browser for the files)
 * @author Simon Zienkiewicz
 */ 
public class HelpDialog extends JFrame implements HyperlinkListener, ActionListener,TreeSelectionListener{
    
    private JEditorPane htmlPane;
    private URL helpURL;
    private JButton back; 
    private JButton forward;
    private Stack backStack = new Stack();
    private Stack forwardStack = new Stack();
    private final JTree tree;
    private LinkedList hyperlinkList = new LinkedList();
    
    /** Creates an instance of HelpDialog() */    
    public HelpDialog(){
        
        setTitle("Intellego 2.0 Help");
        setSize(920,620);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width/2)-460,(screenSize.height/2)-310);
        
        //set the frame boarder
        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
                
        setVisible(true);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        //this.setResizable(false);
        
         //load icon image from a file
        try{ 
                BufferedImage simIcon= ImageIO.read(new File("images\\lego.png"));
                this.setIconImage(simIcon);
        }
        catch(Exception e){}
        
        HelpInfo one = new HelpInfo("Intellego 2.0 Help","intellegoHelp.htm");
        this.hyperlinkList.add(one);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(one);
        createNodes(top);

        //create forward and backwards buttons
        forward = new JButton(new ImageIcon("images\\forward2.gif"));
        back = new JButton(new ImageIcon("images\\back2.gif"));
        forward.addActionListener(this);
        back.addActionListener(this);
        forward.setToolTipText("go forwards");
        back.setToolTipText("go backwards");
        forward.setMargin(new Insets(1,1,1,1));
        back.setMargin(new Insets(1,1,1,1));
         
        forward.setBackground(Color.yellow);
        forward.setForeground(Color.darkGray);
        back.setBackground(Color.yellow);
        back.setForeground(Color.darkGray);
        forward.setEnabled(false);
        back.setEnabled(false);
        
        
        //Create a tree that allows one selection at a time.
        
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setBackground(Color.darkGray);
        tree.setForeground(Color.yellow);
        tree.setCellRenderer(new MyRenderer());
        tree.addTreeSelectionListener(this);
        JScrollPane treePane = new JScrollPane(tree);
                
        //Create the scroll pane and add the tree to it. 
        JPanel treePanel = new JPanel();
        treePanel.setBackground(Color.darkGray);
        treePanel.setBorder(BasicBorders.getTextFieldBorder());
        
        treePanel.setLayout(new AbsoluteLayout());
        
        JTextField title = new JTextField(" Help Options: ");
        title.setEditable(false);
        title.setBackground(Color.darkGray);
        title.setForeground(Color.yellow);
        
        JTextField backL = new JTextField("  Back");
        JTextField forwardL = new JTextField("  Forward");
        backL.setBackground(Color.darkGray);
        backL.setForeground(Color.yellow);
        backL.setEditable(false);
        forwardL.setBackground(Color.darkGray);
        forwardL.setForeground(Color.yellow);
        forwardL.setEditable(false);
        
        treePanel.add(title,new AbsoluteConstraints(15,20,180,20));
        treePanel.add(back,new AbsoluteConstraints(30,60,70,60));
        treePanel.add(forward,new AbsoluteConstraints(105,60,70,60));
        
        treePanel.add(backL,new AbsoluteConstraints(30,120,70,20));
        treePanel.add(forwardL,new AbsoluteConstraints(105,120,70,20));
        
        treePanel.add(treePane, new AbsoluteConstraints(15,160,300,400));//180,250));
        JScrollPane treeView = new JScrollPane(treePanel);
        treeView.setMinimumSize(new Dimension(330,300));
        treeView.setMaximumSize(new Dimension(360,300));
        tree.setBorder(BasicBorders.getTextFieldBorder());
                
         //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        htmlPane.addHyperlinkListener(this);
        JScrollPane htmlView = new JScrollPane(htmlPane);
        htmlView.setMinimumSize(new Dimension(300,300));
        
         //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treeView,htmlView);
        splitPane.setMinimumSize(new Dimension(520, 300));
        splitPane.setDividerLocation(330);
        splitPane.setOneTouchExpandable(true);
       
        //initialize it
        HelpInfo helpFile = new HelpInfo("none","gettingStarted.html");
        displayURL(helpFile);//.getURL());
        
        
        //Add the split pane to this frame.
        getContentPane().add(splitPane, BorderLayout.CENTER); 
        
        tree.setSelectionPath(tree.getNextMatch("Getting Started",1,javax.swing.text.Position.Bias.Forward));
        tree.expandPath(tree.getNextMatch("Getting Started",1,javax.swing.text.Position.Bias.Forward));
                
        tree.expandPath(tree.getNextMatch("Detailed Help",1,javax.swing.text.Position.Bias.Forward));
                        
        this.show();
        
    }
         
    /** Creates nodes, headings which will contain subheadings.
     * @param top the top node
     */    
    private void createNodes(DefaultMutableTreeNode top) {
        
        DefaultMutableTreeNode gettingStarted = null;
        DefaultMutableTreeNode detailedHelp = null;

        //GETTING STARTED
        gettingStarted = new DefaultMutableTreeNode(new HelpInfo("Getting Started","gettingStarted.html"));
        top.add(gettingStarted);
        
        DefaultMutableTreeNode codeEditor = null;
        DefaultMutableTreeNode simulator = null;
        HelpInfo one = null;
        
        //getting started: GETTING STARTED
        one = new HelpInfo("The Code Editor","codeEditor.htm");
        this.hyperlinkList.add(one);
        codeEditor = new DefaultMutableTreeNode(one);
        gettingStarted.add(codeEditor);
        
            //GETTING STARTED->the CODE EDITOR
            one = new HelpInfo("Creating New Controller File","creatingController.htm");
            this.hyperlinkList.add(one);
            codeEditor.add(new DefaultMutableTreeNode(one));

            one = new HelpInfo("Editing Existing Controller File","editingController.htm");
            this.hyperlinkList.add(one);
            codeEditor.add(new DefaultMutableTreeNode(one));

            one = new HelpInfo("Uploading Controller File","uploadingController.htm");
            this.hyperlinkList.add(one);
            codeEditor.add(new DefaultMutableTreeNode(one));

            one = new HelpInfo("Uploading Firmware","uploadFirmware.htm");
            this.hyperlinkList.add(one);
            codeEditor.add(new DefaultMutableTreeNode(one));
        
        //getting started: SIMULATOR
        one = new HelpInfo("The Simulator","simulator.htm");
        this.hyperlinkList.add(one);
        simulator = new DefaultMutableTreeNode(one);
        gettingStarted.add(simulator);
        
            //GETTING STARTED->the SIMULATOR
            one = new HelpInfo("Creating New SimWorld File","createSimWorld.htm");
            this.hyperlinkList.add(one);
            simulator.add(new DefaultMutableTreeNode(one));
            
            one = new HelpInfo("Running Simulation","runningSimulation.htm");
            this.hyperlinkList.add(one);
            simulator.add(new DefaultMutableTreeNode(one));
        
        //detailed help
        detailedHelp = new DefaultMutableTreeNode(new HelpInfo("Detailed Help","detailedHelp.htm"));
        top.add(detailedHelp);
        
        //important notices
        detailedHelp.add(new DefaultMutableTreeNode(new HelpInfo("Important Notices", "important.html")));
        
        //SIMULATOR TOOLS
        DefaultMutableTreeNode tools = null;
        DefaultMutableTreeNode createSimWorldObjects = null;
        DefaultMutableTreeNode wallO = null;
        DefaultMutableTreeNode groundO = null;
        DefaultMutableTreeNode theSimRobot = null;
        DefaultMutableTreeNode lightProperties = null;
        DefaultMutableTreeNode theGrid = null;
                
        one = new HelpInfo("Simulator Tools", "tools.html");
        this.hyperlinkList.add(one);
        tools = new DefaultMutableTreeNode(one);
        detailedHelp.add(tools);
        
            //CREATING SIM WORLD OBJECTS
            one = new HelpInfo("Creating SimWorld Objects", "createSimWorldObjects.htm");
            this.hyperlinkList.add(one);
            createSimWorldObjects = new DefaultMutableTreeNode(one);
            tools.add(createSimWorldObjects);
                
                //WALL OBJECTS
                one = new HelpInfo("Wall Objects", "wallObject.htm");
                this.hyperlinkList.add(one);
                wallO = new DefaultMutableTreeNode(one);
                createSimWorldObjects.add(wallO);
                
                    //Creating/Moving Wall Objects
                    one = new HelpInfo("Creating/Moving Wall Objects","creatingWallObjects.htm");
                    this.hyperlinkList.add(one);
                    wallO.add(new DefaultMutableTreeNode(one));

                    //Editing Wall Objects
                    one = new HelpInfo("Editing Wall Objects","editingObjects.htm");
                    this.hyperlinkList.add(one);
                    wallO.add(new DefaultMutableTreeNode(one));
                
            
                //GROUND/INTERSECTION OBJECTS
                one = new HelpInfo("Ground/Intersection Objects", "groundObject.htm");
                this.hyperlinkList.add(one);
                groundO = new DefaultMutableTreeNode(one);
                createSimWorldObjects.add(groundO);
                
                    //Creating/Moving Ground Objects
                    one = new HelpInfo("Creating/Moving Ground Objects","creatingGroundObjects.htm");
                    this.hyperlinkList.add(one);
                    groundO.add(new DefaultMutableTreeNode(one));

                    //Editing Ground/Intersection Objects
                    one = new HelpInfo("Editing Ground/Intersection Objects","editingObjects.htm");
                    this.hyperlinkList.add(one);
                    groundO.add(new DefaultMutableTreeNode(one));
                    
                    //Assigning Colours to Ground Objects
                    one = new HelpInfo("Assigning Colours to Ground Objects","colourGroundObject.htm");
                    this.hyperlinkList.add(one);
                    groundO.add(new DefaultMutableTreeNode(one));
            
            //LIGHT PROPERTIES
            one = new HelpInfo("Light Properties", "lightProperties.htm");
            this.hyperlinkList.add(one);
            lightProperties = new DefaultMutableTreeNode(one);
            tools.add(lightProperties);
        
                //Opening LightSensor Prop Dialog
                one = new HelpInfo("Opening LightSensor Prop Dialog","openlightProperties.htm");
                this.hyperlinkList.add(one);
                lightProperties.add(new DefaultMutableTreeNode(one));

                //Mapping Colours to Numbers
                one = new HelpInfo("Mapping Colours to Numbers","maplightProperties.htm");
                this.hyperlinkList.add(one);
                lightProperties.add(new DefaultMutableTreeNode(one));
                
                //Saving/Opening LightSensor Colour Values
                one = new HelpInfo("Saving/Opening LightSensor Colour Values","opensaveLight.htm");
                this.hyperlinkList.add(one);
                lightProperties.add(new DefaultMutableTreeNode(one));
                
                
            //THE SIMULATED ROBOT
            one = new HelpInfo("The Simulated Robot", "robot.htm");
            this.hyperlinkList.add(one);
            theSimRobot = new DefaultMutableTreeNode(one);
            tools.add(theSimRobot);
        
                //Initializing the Robot
                one = new HelpInfo("Initializing the Robot","initRobot.htm");
                this.hyperlinkList.add(one);
                theSimRobot.add(new DefaultMutableTreeNode(one));

                //Moving the Robot in the SimWorld
                one = new HelpInfo("Moving the Robot in the SimWorld","moveRobot.htm");
                this.hyperlinkList.add(one);
                theSimRobot.add(new DefaultMutableTreeNode(one));
                
                //Rotating the Robot in the SimWorld
                one = new HelpInfo("Rotating the Robot in the SimWorld","rototateRobot.htm");
                this.hyperlinkList.add(one);
                theSimRobot.add(new DefaultMutableTreeNode(one));
           
            //THE GRID
            one = new HelpInfo("Using the Grid", "grid.htm");
            this.hyperlinkList.add(one);
            theGrid = new DefaultMutableTreeNode(one);
            tools.add(theGrid);
        
                //Initializing the Robot
                one = new HelpInfo("Using the Grid Setup Editor","openGrid.htm");
                this.hyperlinkList.add(one);
                theGrid.add(new DefaultMutableTreeNode(one));

                //Moving the Robot in the SimWorld
                one = new HelpInfo("Assinging Color to the Grid","colourGrid.htm");
                this.hyperlinkList.add(one);
                theGrid.add(new DefaultMutableTreeNode(one));
              
            //ASSIGNING COLOURS IN SIM WORLD
            one = new HelpInfo("Assinging Colours in SimWorld","assignColours.html");
            this.hyperlinkList.add(one);
            tools.add(new DefaultMutableTreeNode(one));
             
            //USING UNDO/REDO
            one = new HelpInfo("Using Undo/Redo","undoRedo.htm"); 
            this.hyperlinkList.add(one);
            tools.add(new DefaultMutableTreeNode(one));
            
            //USING THE LOCK TOOL
            one = new HelpInfo("Using the Lock Tool","lockFunction.htm");
            this.hyperlinkList.add(one);
            tools.add(new DefaultMutableTreeNode(one));

         //CONTROLLER FILE SPECIFICATIONS
        one = new HelpInfo("Controller File Specifications", "controllerSpecs.html");
        this.hyperlinkList.add(one);
        DefaultMutableTreeNode specs = new DefaultMutableTreeNode(one);
        detailedHelp.add(specs);

            //The Controller Interface
            one = new HelpInfo("The Controller Interface","controllerInterface.htm");
            this.hyperlinkList.add(one);
            specs.add(new DefaultMutableTreeNode(one));
             
            //AbstractRobot Functions
            one = new HelpInfo("AbstractRobot Functions","abstractRobotFunctions.htm"); 
            this.hyperlinkList.add(one);
            specs.add(new DefaultMutableTreeNode(one));
            
            //Sample Controller File: ZincRocker
            one = new HelpInfo("Sample Controller File: ZincRocker","zincRocker.htm");
            this.hyperlinkList.add(one);
            specs.add(new DefaultMutableTreeNode(one));
        
        
        //shortcut/hotkeys
        detailedHelp.add(new DefaultMutableTreeNode(new HelpInfo("Shortcuts/Hotkeys", "shortcut.html")));
        
        //known bugs
        detailedHelp.add(new DefaultMutableTreeNode(new HelpInfo("Known Bugs/Errors", "knownBugs.htm")));
        
    }
    
    /** Display the HTML file
     * @param url the path and name of the HTLM file wanting display
     */    
    private void displayURL(HelpDialog.HelpInfo url)
    {
        try {
            
            try
            {
                if(!(url.getURL()).equals(((HelpDialog.HelpInfo)this.backStack.peek()).getURL())) this.backStack.push(url);
            }
                
            catch(Exception e2)
            {
                this.backStack.push(url);
            }
            
            if(this.backStack.size() < 2) this.back.setEnabled(false);
            else this.back.setEnabled(true);
            htmlPane.setPage(url.getURL());

        }
        
        catch (IOException e) 
        {
            System.err.println("Attempted to read a bad URL: " + url);
        }
        
    }
    
    /** Displays the HTLM once a hyperlink is clicked within the page.
     * @param e the triggered hyperlink event
     */    
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType().toString().equals("ACTIVATED")){
            
            try
            {
                HelpInfo a = null;
                
                for(int i=0;i<hyperlinkList.size();i++){
                    
                    a = (HelpInfo)this.hyperlinkList.get(i);
                    String b = (a.getURL()).substring((a.getURL()).indexOf("\\")+1);
                    String c = e.getDescription();

                    if(b.equals(c))
                    {
                        break;
                    }
                    
                    else
                    {
                        if((i+1) == hyperlinkList.size()){
                            a = new HelpInfo("none",e.getDescription());
                        }
                        //do nothing
                    }
                }
                                
                this.forwardStack.clear();
                this.forward.setEnabled(false);
                displayURL(a);
            }
            catch(Exception e2){
                //do nothing
            }
        }
    
    }
    
    /** Performs the 'back' and 'forward' button commands, resulting from the passed
     * event.
     * @param e the event occuring from either the push of the 'back' or 'forward' buttons
     */    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == back){
            if(this.backStack.size() >= 2){
                this.forwardStack.push(this.backStack.pop());
                displayURL((HelpDialog.HelpInfo)this.backStack.peek());
                this.forward.setEnabled(true);
                if(this.backStack.size() ==1)tree.setSelectionPath(tree.getNextMatch("Getting Started",1,javax.swing.text.Position.Bias.Forward));
            }
            else{
                this.back.setEnabled(false);                
            }
        }
        else if(e.getSource() == forward){
                
            if(this.forwardStack.size() >= 1){
                Object b = this.forwardStack.pop();
                this.backStack.push(b);
                displayURL((HelpDialog.HelpInfo)b);
                if(forwardStack.isEmpty())this.forward.setEnabled(false);   
            }    
        }
    }
    
    /** ...
     * @param e tree selection event
     */    
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        
        if (node == null) return;

        Object nodeInfo = node.getUserObject();
       
        HelpInfo helpFile = (HelpInfo)nodeInfo;
        displayURL(helpFile);
        
    }
    
    /** Class used to get url information pertaining to nodes. */    
    private class HelpInfo{
        
        /** name of the help file */        
        private String helpName;
        /** url of the help file. */        
        public String helpURL=null;
        /** the super path of the directory where the help files are stored */        
        public String path = "file:HelpFiles\\";
        
        /** Creates a HelpInfo object
         * @param name the title of the help file
         * @param file the file name of the help file (*.html)
         */        
        public HelpInfo(String name, String file) {
            
            helpName = name;
            
            try 
            {
                helpURL = path+file;
            }
            
            catch (Exception e)
            {
                helpURL = null;
            }
        }
        
        /** Gets the complete path+file name of the helpfile.
         * @return the complete path+file name of the helpfile
         */        
        public String getURL(){
            return helpURL;
        }
        
        /** Gets the title of the help file.
         * @return the title of the help file
         */        
        public String toString() {
            return helpName;
        }
    }
     
    //enables color adjusments on the tree cells
    /** Enables color adjusments on the tree cells */    
    private class MyRenderer extends DefaultTreeCellRenderer {
        ImageIcon tutorialIcon;

        /** Creates a new instance of MyRenderer() object */        
        public MyRenderer() {
            
        }

        /** */        
        public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,hasFocus);
            this.setForeground(Color.yellow);
            this.setBackgroundNonSelectionColor(Color.darkGray);
            this.setBackgroundSelectionColor(Color.darkGray.darker());
            return this;
        }
     }
      
}