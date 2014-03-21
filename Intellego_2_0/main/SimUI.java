package main;

import simworldobjects.*;
import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;
import NetBeansResources.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.awt.geom.*;


/** Provides a user interface for a simulation
 * @author Graham Ritchie
 * @modifyer Simon Zienkiewicz
 */
public class SimUI extends JInternalFrame implements Runnable, ItemListener
{
    /** The container of this class/frame */    
    private Container mainContainer;
    /** The SimDisplay object associated with this frame */        
    private SimDisplay display=null;
    /** The main pane containing all the simulator frames */        
    private JLayeredPane mainPane;
    /** the list of controller files */        
    private LinkedList controllerList;
    /** the robot list */        
    private LinkedList robotList;
    /** the SimWorld currently opened in the simulator */        
    private SimWorld world=null;
    /** the state of the simulator */        
    private boolean running=false;
    /** the time between each cycle of the simulator */        
    private int UPDATE_TIME=10;
    /** if there is currently a SimWorld opened in the simulator */        
    private boolean openWorld = false;
    /** the desired width of the simulator screen */        
    private static int screenWidth=900;
    /** the desired height of the simulator screen */        
    private static int screenHeight=400;
    /** the panel containing the SimDisplay object */        
    private JPanel jPanel2;
    /** the panel containing the grid */        
    private JPanel jPanel3;
    /** the textbox indicating the X position of the mouse in the simulator */
    private JTextField xT;
    /** the textbox indicating the Z position of the mouse in the simulator */        
    private JTextField  zT;
    /** pane allowing for scrolling of mainContainer */        
    private JScrollPane codeScrollPane;
    /** light properties menu item */        
    private JMenuItem lightProp;
    /** the GridDisplay object for the simulator */        
    private GridDisplay gridDisplay = null;
    /** constant */        
    private static final String NO_CLASS="__NOCLASS__";
    /** the index of a selected object */        
    private int objectIndex;
    /** if a object is selected */        
    private boolean selectedObject=false;
    /** the offset of an object in the X direction to allow for proper object placement */
    private int offSetX;
    /** the offset of an object in the Y(Z) direction to allow for proper object placement */        
    private int  offSetY;
    /** the path of a worldobject */
    private String path;
    /** the file name of the current world */        
    private String  currentWorldFile;
    /** if a color properties dialog exists */        
    private boolean colorPropertiesExist = false;
    /** if a grid dialog exists */        
    private boolean gridDialogExist=false;
    /** if a color dialog exists */        
    private boolean colorDialogExist=false;
    /** if a object dialog exists */        
    private boolean objectDialogExist=false;
    /** the light properties dialog for the simulator */        
    private LightSensorPropertiesDialog lightProperties=null;
    /** the grid dialog for the simulator */        
    private GridOptionDialog newGridOption=null;
    /** the color dialog for the simulator */        
    private ColorOptionDialog colorsPop=null;
    /** the object dialog for the simulator */        
    private ObjectEditDialog objectEdit=null;
    /** the robot rotation dialog for the simulator */        
    private RobotRotation robotRotation = null;
    /** the lock checkbox for the simultor */        
    private JCheckBox lock;
    /** the textbox indicating the status of the simulation */        
    private JTextField simulationStatus;
    /** the panel containing the dialog boxes */        
    private JPanel propertyPanel;
    /** an array indicating the number of robotObjects */        
    private int[] robotObjects;
    /** the initialization of a robot dialog */        
    private InitRobotDialog d=null;
    /** redo menu item */
    private JMenuItem redo;
    /** undo menu item */        
    private JMenuItem undo;
    /** the undo stack */        
    private Stack undoStack = new Stack();
    /** the redo stack */        
    private Stack redoStack = new Stack();
    /** is the simulation on */        
    private boolean simOn = false;
    /** is the robot selected */        
    private boolean ROBOT_IS_SELECTED = false;
    /** did the user begin to rotate the robot */        
    private boolean BEGIN_ROTATION = false;
    /** initial X postition where the robot was rotated from */        
    private int INITIAL_X_ROT;
    /** initial Y(Z) postition where the robot was rotated from */        
    private int INITIAL_Y_ROT;

    /** the thread on which the simulation is running */        
    private Thread t;
    /** to exit the thread */        
    private boolean exit = true;

    /**
    * Sets up the main window for an empty simulation
    */
    public SimUI()
    {
        // window setup
        super("Simulator:",true,true,true,true);
        this.setFrameIcon(new ImageIcon("images/execute.gif"));
        setSize(screenWidth,screenHeight);
        setupWindow();
        
        // set up a default world
        selectWorld(0);

    }
	
    /** Loads a new simulation with the specified controller preloaded
     * @param file the file name of the controller (robot)
     */
    public SimUI(File file)
    {
        // window setup
        super("Simulator: ",true,true,true,true);
        setSize(screenWidth,screenHeight);     
        setupWindow();

        // set up a world
        selectWorld(1);

        Controller c=createController(file);

        if(c!=null)
        {
            // and add it to the system if it is not null
            addController(c);
        }
        else
        {
            MainInterface.displayMessage("Failed to create Controller");
            Intellego.addToLog("SimUI.addRobot(): Failed to create Controller with name "+file);
        }

    }
    
    /**
    * Opens a controller file in the current simulator window and simworld
    */
    public void openCurrentController(File file){
        
        //world is open in the simulator
        
        Controller c=createController(file);

        if(c!=null)
        {
            // and add it to the system if it is not null
            addController(c);
        }
        else
        {
            MainInterface.displayMessage("Failed to create Controller");
            Intellego.addToLog("SimUI.addRobot(): Failed to create Controller with name "+file);
        }
    
    }
	
    /**
    * Sets up the main window
    */
    public void setupWindow()
    {
        // set up main pane
        mainContainer=getContentPane();
        //mainContainer.setLayout(new BorderLayout());

        //new stuff using capture the flag game
        mainContainer.setLayout(new AbsoluteLayout());

        // put it in a scroll pane
        codeScrollPane = new JScrollPane(mainContainer);
        codeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        codeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        codeScrollPane.setPreferredSize(new Dimension(400,400));
        codeScrollPane.setMinimumSize(new Dimension(250, 100));
        codeScrollPane.setBackground(Color.darkGray);
        (codeScrollPane.getVerticalScrollBar()).setBackground(Color.darkGray);
        (codeScrollPane.getHorizontalScrollBar()).setBackground(Color.darkGray);
        (codeScrollPane.getViewport()).setBackground(Color.yellow);

        // and add this to a main content pane
        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.yellow);
        BoxLayout box = new BoxLayout(contentPane, BoxLayout.X_AXIS);
        contentPane.setLayout(box);

        //
        propertyPanel = new JPanel();
        propertyPanel.setLayout(new AbsoluteLayout());
        propertyPanel.setBackground(Color.darkGray);
        propertyPanel.setSize(170,10000);
        propertyPanel.setPreferredSize(new Dimension(170,750));
        propertyPanel.setMaximumSize(new Dimension(170,750));
        propertyPanel.setMinimumSize(new Dimension(170,750));

        JScrollPane propertyScrollPane = new JScrollPane(propertyPanel);
        propertyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        propertyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        propertyScrollPane.setPreferredSize(new Dimension(180,1000));
        propertyScrollPane.setMinimumSize(new Dimension(180, 1000));
        propertyScrollPane.setMaximumSize(new Dimension(180, 1000));
        propertyScrollPane.setBackground(Color.darkGray);
        (propertyScrollPane.getVerticalScrollBar()).setBackground(Color.darkGray);
        (propertyScrollPane.getViewport()).setBackground(Color.yellow);

        contentPane.add(propertyScrollPane);
        contentPane.add(codeScrollPane);

        setContentPane(contentPane);
        this.setMinimumSize(new Dimension(850, 400));

        // create and set the menu bar
        JMenuBar mb=createMenuBar();
        setJMenuBar(mb);

        // set up required lists
        controllerList=new LinkedList();
        robotList=new LinkedList();
    }	
	
    /**
    * Creates the menu bar for the main window, and adds action listeners 
    * to the menus.
    *
    * @return the menu bar
    */
    private JMenuBar createMenuBar()
    {
        //deligates the appropriate function for the colors menu

        // menu bar
        JMenuBar mb=new JMenuBar();

        // menus
        JMenu controlMenu=new JMenu("Controls");
        controlMenu.setMnemonic(KeyEvent.VK_C);
        JMenu optionsMenu=new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_N);

        //set colors
        controlMenu.setBackground(Color.darkGray);
        controlMenu.setForeground(Color.lightGray);
        optionsMenu.setBackground(Color.darkGray);
        optionsMenu.setForeground(Color.lightGray);

        // control menu items
        JMenuItem pickWorld=new JMenuItem("Open SimWorld");
        pickWorld.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
        JMenuItem saveSimWorld = new JMenuItem("Save SimWorld Instance");
        saveSimWorld.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        JMenuItem addNewRobot=new JMenuItem("Add Robot");
        addNewRobot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
        JMenuItem startSim=new JMenuItem("Start Simulation");
        startSim.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.ALT_MASK));
        JMenuItem stopSim=new JMenuItem("Stop Simulation");
        stopSim.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.ALT_MASK));
        //set colors
        pickWorld.setBackground(Color.darkGray);
        saveSimWorld.setForeground(Color.lightGray);
        saveSimWorld.setBackground(Color.darkGray);
        pickWorld.setForeground(Color.lightGray);
        addNewRobot.setBackground(Color.darkGray);
        addNewRobot.setForeground(Color.lightGray);
        startSim.setBackground(Color.darkGray);
        startSim.setForeground(Color.green);
        stopSim.setBackground(Color.darkGray);
        stopSim.setForeground(Color.red.darker());

        // grid sub menu'
        JMenuItem gridC = new JMenuItem("Colour");
        gridC.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.ALT_MASK));
        JMenuItem gridAt = new JMenuItem("Attributes");
        gridAt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,ActionEvent.ALT_MASK));
        //set colors
        gridC.setBackground(Color.darkGray);
        gridC.setForeground(Color.lightGray);
        gridAt.setBackground(Color.darkGray);
        gridAt.setForeground(Color.lightGray);

        //create sub-submenu
        JMenuItem worldBG = new JMenuItem("SimWorld Background");
        worldBG.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,ActionEvent.ALT_MASK));
        JMenuItem walls = new JMenuItem("Walls");
        walls.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,ActionEvent.ALT_MASK));
        JMenuItem road = new JMenuItem("Road");
        road.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,ActionEvent.ALT_MASK));

        JMenuItem robot = new JMenuItem("Robot");
        robot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5,ActionEvent.ALT_MASK));
        JMenuItem lightS = new JMenuItem("Light Sensors");
        lightS.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6,ActionEvent.ALT_MASK));
        JMenuItem touchS = new JMenuItem("Touch Sensors");
        touchS.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7,ActionEvent.ALT_MASK));

        //set colors
        walls.setBackground(Color.darkGray);
        walls.setForeground(Color.lightGray);
        robot.setBackground(Color.darkGray);
        robot.setForeground(Color.lightGray);
        lightS.setBackground(Color.darkGray);
        lightS.setForeground(Color.lightGray);
        touchS.setBackground(Color.darkGray);
        touchS.setForeground(Color.lightGray);
        worldBG.setBackground(Color.darkGray);
        worldBG.setForeground(Color.lightGray);
        road.setBackground(Color.darkGray);
        road.setForeground(Color.lightGray);

        //create sub-submenu for Add Objects
        JMenuItem wall = new JMenuItem("Wall");
        JMenuItem path = new JMenuItem("Ground/Intersection");
        //set colors
        wall.setBackground(Color.darkGray);
        wall.setForeground(Color.lightGray);
        path.setBackground(Color.darkGray);
        path.setForeground(Color.lightGray);

        // add action listeners
        pickWorld.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                //colorsPop=null;
                selectWorld(1);
            }
        });

        saveSimWorld.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                saveWorld();

            }
        });

        addNewRobot.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                addRobot();
            }
        });

        startSim.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(!simOn){                  
                    startSimulation();
                    simOn=true;
                }
            }
        });

        stopSim.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(simOn){
                    stopSimulation();
                    simOn = false;
                }
            }
        });

        //grid activation menu item action listeners
        gridC.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(gridDisplay != null) popUpColorOptions(6, "Set Grid Colour:",null,gridDisplay);
            }
        });


        gridAt.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(gridDisplay != null) popUpGridOptions("Grid Setup:");
            }
        });


        //submenu direction of color methods
        walls.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                popUpColorOptions(0, "Colour: Wall",null,null);
            }
        });
        touchS.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                popUpColorOptions(3, "Colour: TouchSensor",null,null);
            }
        });
        lightS.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                popUpColorOptions(4, "Colour: LightSensor",null,null);
            }
        });
        robot.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                popUpColorOptions(5, "Colour: Robot",null,null);
            }
        });
        road.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                popUpColorOptions(7, "Colour: Road",null,null);
            }
        });
        worldBG.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                if(gridDisplay != null) popUpColorOptions(1,"Colour: SimWorld",null,gridDisplay);
            }
        });

        //submenu direction of add objects methods
        wall.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                addObjectToWorld("wall");
            }
        });
        path.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                addObjectToWorld("path");
            }
        });

        // add items to control menu
        controlMenu.add(pickWorld);
        controlMenu.add(saveSimWorld);
        controlMenu.addSeparator();
        controlMenu.add(addNewRobot);
        controlMenu.addSeparator();
        controlMenu.add(startSim);
        controlMenu.add(stopSim);

        //create a submenu
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,ActionEvent.CTRL_MASK));
        undo.setEnabled(false);
        redo.setEnabled(false);

        //create a submenu 
        JMenu submenu = new JMenu("Grid");
        JMenu submenuColors = new JMenu("Colour Code");
        JMenu submenuAddParts = new JMenu("Build Objects");
        lightProp = new JMenuItem("Light Sensor Prop.");
        lightProp.setEnabled(false);
        lightProp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));

        //set colors
        undo.setBackground(Color.darkGray);
        undo.setForeground(Color.red.darker());
        redo.setBackground(Color.darkGray);
        redo.setForeground(Color.yellow);   

        submenu.setBackground(Color.darkGray);
        submenu.setForeground(Color.lightGray);
        submenuColors.setBackground(Color.darkGray);
        submenuColors.setForeground(Color.lightGray);
        submenuAddParts.setBackground(Color.darkGray);
        submenuAddParts.setForeground(Color.lightGray);
        lightProp.setBackground(Color.darkGray);
        lightProp.setForeground(Color.yellow);

        undo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
               undoRedoSet("undo");
            }
        });

        redo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                undoRedoSet("redo");
            }
        });

        lightProp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                setLightSensorProperties();
            }
        });

        //submenu for grid options
        submenu.add(gridC);
        submenu.addSeparator();
        submenu.add(gridAt);

        //submenuColor options
        submenuColors.add(worldBG);
        submenuColors.addSeparator();
        submenuColors.add(walls);
        submenuColors.add(road);
        submenuColors.addSeparator();
        submenuColors.add(robot);
        submenuColors.add(lightS);
        submenuColors.add(touchS);

        //submenuAddParts options
        submenuAddParts.add(wall);
        submenuAddParts.addSeparator();
        submenuAddParts.add(path);

        //undo/redo options
        optionsMenu.add(undo);
        optionsMenu.add(redo);
        optionsMenu.addSeparator();

        optionsMenu.add(submenu);
        optionsMenu.addSeparator();
        optionsMenu.add(submenuColors);
        optionsMenu.addSeparator();
        optionsMenu.add(submenuAddParts);
        optionsMenu.addSeparator();
        optionsMenu.add(lightProp);

        //mouse position
        JLabel xL=new JLabel("X: ");
        xL.setForeground(Color.yellow);
        JLabel zL=new JLabel("Z: ");
        zL.setForeground(Color.yellow);
        JLabel titleL=new JLabel("  |   Mouse Position: ");
        titleL.setForeground(Color.yellow.darker());
        JLabel blank = new JLabel("  |  ");
        blank.setForeground(Color.yellow.darker());
        JLabel blank2 = new JLabel("  |  ");
        JLabel blank3 = new JLabel("  |  ");
        blank2.setForeground(Color.yellow.darker());
        blank3.setForeground(Color.yellow.darker());
        JLabel buildTools = new JLabel("Building Tools: ");
        buildTools.setForeground(Color.yellow.darker());

        xT=new JTextField(3);
        xT.setEditable(false);
        xT.setBackground(Color.yellow);
        xT.setForeground(Color.darkGray.darker());

        zT=new JTextField(3);
        zT.setEditable(false);
        zT.setBackground(Color.yellow);
        zT.setForeground(Color.darkGray.darker());

        // add menus to menu bar
        mb.setLayout(new AbsoluteLayout());
        mb.setBackground(Color.DARK_GRAY);
        mb.add(controlMenu,new AbsoluteConstraints(3,0));
        mb.add(optionsMenu,new AbsoluteConstraints(68,0));
        mb.add(titleL,new AbsoluteConstraints(128,2));
        mb.add(xL,new AbsoluteConstraints(248,2));
        mb.add(xT,new AbsoluteConstraints(263,0));
        mb.add(zL,new AbsoluteConstraints(303,2));
        mb.add(zT,new AbsoluteConstraints(318,0));
        mb.add(blank,new AbsoluteConstraints(355,2));
        mb.add(buildTools,new AbsoluteConstraints(372,2));

        //button handler
        Icon iconBrick = new ImageIcon("images\\brickIcon.png");
        JButton brickButton = new JButton(iconBrick);
        brickButton.setBackground(Color.darkGray);
        brickButton.setSize(new Dimension(20,10));
        brickButton.setMargin(new Insets(1,1,1,1));
        brickButton.setToolTipText("build wall");
        brickButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                addObjectToWorld("wall");
            }
        });

        Icon iconPath = new ImageIcon("images\\pathIcon.png");
        JButton pathButton = new JButton(iconPath);
        pathButton.setBackground(Color.darkGray);
        pathButton.setSize(new Dimension(20,10));
        pathButton.setMargin(new Insets(1,1,1,1));
        pathButton.setToolTipText("build ground/intersection");
        pathButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                addObjectToWorld("path");
            }
        });

        (lock = new JCheckBox()).addItemListener(this);
        JLabel lockL = new JLabel("Lock:");
        lockL.setForeground(Color.yellow.darker());
        lock.setBackground(Color.yellow);
        lock.setMargin(new Insets(0,0,0,0));

        JLabel simStat = new JLabel("  Simulation Status:");
        simStat.setForeground(Color.yellow.darker());
        this.simulationStatus = new JTextField(3);
        this.simulationStatus.setForeground(Color.gray.darker());
        this.simulationStatus.setEditable(false);
        this.simulationStatus.setFont(new Font("Arial", Font.BOLD, 8));
        this.simulation_is_OFF();

        mb.add(brickButton,new AbsoluteConstraints(465,0));
        mb.add(pathButton,new AbsoluteConstraints(499,0));
        mb.add(blank2,new AbsoluteConstraints(537,2));
        mb.add(lockL,new AbsoluteConstraints(555,2));
        mb.add(lock,new AbsoluteConstraints(595,2));
        mb.add(blank3,new AbsoluteConstraints(615,2));
        mb.add(simStat,new AbsoluteConstraints(625,2));
        mb.add(this.simulationStatus,new AbsoluteConstraints(745,3));

        return mb;
    }
        
    /** Internal method informing the user the simulation is OFF or not running. */        
    private void simulation_is_OFF(){
        this.simulationStatus.setBackground(Color.RED);
        this.simulationStatus.setText(" OFF");
    }

    /** Internal method informing the user the simulation is ON or running. */        
    private void simulation_is_ON(){
        this.simulationStatus.setBackground(Color.GREEN);
        this.simulationStatus.setText(" ON");

    }
	
    /**
    * Main loop: updates the world and repaints the screen 
    */
    public void run()
    {
        if(world==null || display==null)
        {
            MainInterface.displayMessage("You must select a SimWorld and a SimDisplay first");
        }
        else
        {
            while (exit)
            {
                if(running){

                    // update SimWorld
                    world.tick();

                    // repaint the display screen
                    display.repaint();   

                    // sleep for a bit
                    try{Thread.sleep(UPDATE_TIME);}catch(Exception e){}
                }

            }

        }
    }

	/** Allows the user to pick a simworld for their simulation, and loads it
         * @param index indicates whether default world load or user selected world loading
         */
    public void selectWorld(int index)
    {
        // create simworld by getting the class name from the user
        try{
            
            File file;
            
            //default
            if(index ==0)
            {
                file = new File("simworlds/defaultSe101.class");
                currentWorldFile = file.getName();
                path = ((file.getAbsolutePath()).substring(0,(((file.getAbsolutePath()).length()) - ((file.getName()).length()))));
            }
            
            //user selection
            else
            {
                file = getClassName("Pick a SimWorld class file:","simworlds");
            }
            
            
            
            String className=file.getName();
            className=className.substring(0,file.getName().length() - 6);


            // if there is already a world open, stop the current sim and get rid of it.
            if(world!=null)
            {
                stopSimulation();

                objectEdit.hide();
                newGridOption.hide();
                colorsPop.hide();
                robotRotation.hide();

                world=null;
                mainContainer.removeAll();

                jPanel2=null;
                display=null;
                lightProperties = null;
                newGridOption = null;
                objectEdit = null;
                colorsPop = null;
                robotRotation = null;

                colorPropertiesExist = false;
                gridDialogExist=false;
                colorDialogExist=false;
                objectDialogExist=false;

                if(d != null)d.setVisible(false);

            }

            // try to create a simworld from this class
            SimWorld s=createSimWorld(className);

            // check simworld creation was successful
            if(s!=null)
            {

                // set s as the world for this simulation
                world=s;

                // and create a new display with this simworld
                display=new SimpleDisplay(world,this);
                jPanel2=display;
                jPanel2.setLayout(new AbsoluteLayout());

                //Mouse events for the jPanel2 display frame
                jPanel2.addMouseListener(new java.awt.event.MouseAdapter() 
                {
                    public void mouseClicked(java.awt.event.MouseEvent evt) 
                    {
                        if(!lock.isSelected())jPanel2MouseClicked(evt);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) 
                    {
                        if(!lock.isSelected())jPanel2MousePressed(evt);
                    }
                    public void mouseReleased(java.awt.event.MouseEvent evt) 
                    {
                        if(!lock.isSelected())jPanel2MouseReleased(evt);
                    }

                });

                jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() 
                {
                    public void mouseDragged(java.awt.event.MouseEvent evt) 
                    {
                        if(!lock.isSelected())jPanel2MouseDragged(evt);
                    }
                    public void mouseMoved(java.awt.event.MouseEvent evt) 
                    {
                        jPanel2MouseMoved(evt);
                    }
                });

                this.gridDisplay = new GridDisplay(25,((BasicSimWorld)world).getWorldDimensions()[0],((BasicSimWorld)world).getWorldDimensions()[2], Color.black, ((BasicSimWorld)world).getWorldColor(),false);
                jPanel3 =  this.gridDisplay;
                jPanel3.setLayout(new AbsoluteLayout());

               // add the simdisplay and griddisplay to the main pane
                mainContainer.add(jPanel2, new AbsoluteConstraints(0, 0, ((BasicSimWorld)world).getWorldDimensions()[0], ((BasicSimWorld)world).getWorldDimensions()[2]));
                mainContainer.add(jPanel3, new AbsoluteConstraints(0, 0, ((BasicSimWorld)world).getWorldDimensions()[0], ((BasicSimWorld)world).getWorldDimensions()[2]));

                //create a light properties dialog
                lightProperties = new LightSensorPropertiesDialog();
                newGridOption = new GridOptionDialog();
                objectEdit = new ObjectEditDialog(this);
                colorsPop = new ColorOptionDialog(this);
                robotRotation = new RobotRotation(this);
                robotRotation.createRobotRotation();

                newGridOption.setVisible(false);
                colorsPop.setVisible(false);
                objectEdit.setVisible(false);
                robotRotation.setVisible(false);

                JTextField propTitle = new JTextField(" Property Settings: ");
                propTitle.setEditable(false);
                propTitle.setBackground(Color.darkGray);
                propTitle.setForeground(Color.yellow);

                JTextField divider2 = new JTextField(" ");
                divider2.setEditable(false);
                divider2.setBackground(Color.darkGray);

                mainContainer.add(divider2,new AbsoluteConstraints(((BasicSimWorld)world).getWorldDimensions()[0],0,15,((BasicSimWorld)world).getWorldDimensions()[2]));

                propertyPanel.add(propTitle,new AbsoluteConstraints(10,10,140,20));
                propertyPanel.add(colorsPop,new AbsoluteConstraints(10,40,140,240));
                propertyPanel.add(objectEdit,new AbsoluteConstraints(10,40,140,375));
                propertyPanel.add(newGridOption,new AbsoluteConstraints(10,425,140,155));
                //propertyPanel.add(robotRotation,new AbsoluteConstraints(10,590,140,115));
                propertyPanel.add(robotRotation,new AbsoluteConstraints(10,40,140,115));

                //set the display panel to not be transparent
                this.setGrid(false);

                // set size to force display to repaint
                setSize(this.getWidth(),this.getHeight());

                // initialize the stack
                this.undoStack.clear();
                this.redoStack.clear();
                this.pushUndoItem(copyList((LinkedList)world.getObjectList()));

                //allows the grid to be draw
                this.openWorld = true;
                super.setTitle("Simulator: "+this.path+ this.currentWorldFile);



            }
            else // simworld creation failed, so just bail out
            {
                //MainInterface.displayMessage("Failed to create SimWorld");
                Intellego.addToLog("SimUI.selectWorld(): Failed to create SimWorld with name "+className);
            }
        }

        catch(Exception e){
            //MainInterface.displayMessage("Failed to create SimWorld");
            Intellego.addToLog("SimUI.selectWorld(): Failed to create SimWorld");
        }
    }
    
        
    /** Handles the clicking of objects
    * @param evt Mouse event.
    */
    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) 
    {
        int x= evt.getX();
        int y= evt.getY();
        int b=9999;
        Point2D mouseClicked = new Point(x,y);

        LinkedList objectList = ((BasicSimWorld)world).getObjectList();

        for(int a=0;a<objectList.size();a++){
            SimObject sim = (SimObject)(objectList.get(a));
            if(sim instanceof SimGround || sim instanceof SimWall || sim instanceof SimRCX){
                Shape newShape = (new Rectangle2D.Double((sim.getXCoord()-sim.getWidth()/2),(sim.getZCoord()-sim.getLength()/2),sim.getWidth(),sim.getLength()));
                Shape newShape2 = rotateShape(newShape,sim.getActualBearingXZ(),sim.getXCoord(),sim.getZCoord());
                if(newShape2.contains(mouseClicked))b=a; 
            }
        }

        if(b!=9999){
            SimObject sim = (SimObject)(objectList.get(b)); 
            if((sim instanceof SimGround && evt.getButton() == evt.BUTTON3) || (sim instanceof SimGround && evt.getButton() == evt.BUTTON1)){
                SimGround ground = (SimGround)sim;
                deselectOtherObjects();
                robotRotation.hide();
                setSize(this.getWidth(),this.getHeight());
                ground.setOutlineColor(Color.yellow);
                ground.setSelected(true);
                display.repaint();
                if(evt.getButton() == evt.BUTTON3) this.popUpColorOptions(2, "Colour: Ground "+b, ground,null);
                else if(evt.getButton() == evt.BUTTON1) {
                    objectEdit.setVisible(true);
                    colorsPop.setVisible(false);
                    if(!this.objectDialogExist){
                        objectEdit.createObjectEditorPopUp();
                        this.objectDialogExist = true;
                    }
                    objectEdit.updateObjectPopUp(ground,display,b,world);
                }
            }
            else if(sim instanceof SimWall){
                if(evt.getButton() == evt.BUTTON1) {
                    SimWall wall = (SimWall)sim;
                    deselectOtherObjects();
                    robotRotation.hide();
                    setSize(this.getWidth(),this.getHeight());
                    wall.setOutlineColor(Color.yellow);
                    wall.setSelected(true);
                    display.repaint();
                    objectEdit.setVisible(true);
                    colorsPop.setVisible(false);
                    if(!this.objectDialogExist){
                        objectEdit.createObjectEditorPopUp();
                        this.objectDialogExist = true;
                    }
                    objectEdit.updateObjectPopUp(wall,display,b,world);
                }
            }
            else if(sim instanceof SimRCX){
               if(evt.getButton() == evt.BUTTON1){
                    SimRCX robot = (SimRCX)sim;
                    deselectOtherObjects();
                    robot.setSelected(true);
                    display.repaint();
                    this.robotRotation.updateRotation(robot,world);
                    ROBOT_IS_SELECTED = true;
               }
           }

        }
        else{
            deselectOtherObjects();
            objectEdit.hide();
            colorsPop.hide();
            robotRotation.hide();
            setSize(this.getWidth(),this.getHeight());
        }

    }
     
    /** Initializes the item desiring dragging
    * @param evt Mouse event.
    */
    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) 
    { 
    
        int x= evt.getX();
        int y= evt.getY();
        int b=9999;
        Point2D mouseClicked = new Point(x,y);

        LinkedList objectList = ((BasicSimWorld)world).getObjectList();

        for(int a=0;a<objectList.size();a++){

            SimObject sim = (SimObject)(objectList.get(a));

            if(sim instanceof SimGround || sim instanceof SimWall || sim instanceof SimRCX){

                Shape newShape = (new Rectangle2D.Double((sim.getXCoord()-sim.getWidth()/2),(sim.getZCoord()-sim.getLength()/2),sim.getWidth(),sim.getLength()));
                Shape newShape2 = rotateShape(newShape,sim.getActualBearingXZ(),sim.getXCoord(),sim.getZCoord());
                if(newShape2.contains(mouseClicked))b=a;
            }
        }

        if(evt.getButton() == evt.BUTTON1){  
            if(b!=9999){
                this.robotObjects=null;
                SimObject sim = (SimObject)(objectList.get(b));
                if(sim instanceof SimGround) {
                    SimGround ground = (SimGround)sim;
                    deselectOtherObjects();
                    ground.setOutlineColor(Color.yellow);
                    ground.setSelected(true);
                }
                else if(sim instanceof SimWall){ 
                    SimWall wall = (SimWall)sim;
                    deselectOtherObjects();
                    wall.setOutlineColor(Color.yellow);
                    wall.setSelected(true);
                }
                else if(sim instanceof SimRCX){ 
                    SimRCX robot = (SimRCX)sim;
                    deselectOtherObjects();
                    robot.setSelected(true);
                }

                display.repaint();

                //stores the status denoting an object is to be dragged
                selectedObject=true;

                //stores the index of the to be dragged item for the Linked List of Objects
                objectIndex=b;

                //allow the user to grab and drag an item from any point on the object
                offSetX=(int)sim.getXCoord()-evt.getX();
                offSetY=(int)sim.getZCoord()-evt.getY();
             }
        }

        else if(evt.getButton() == evt.BUTTON3 && this.ROBOT_IS_SELECTED){
            if(b!=9999){
                this.robotObjects=null;
                SimObject sim = (SimObject)(objectList.get(b));
                if(sim instanceof SimRCX){
                    display.repaint();
                    BEGIN_ROTATION = true;
                    objectIndex=b;
                    offSetX=(int)sim.getXCoord()-evt.getX();
                    offSetY=(int)sim.getZCoord()-evt.getY();

                    INITIAL_X_ROT = (int)evt.getX();
                    INITIAL_Y_ROT = (int)evt.getY();
                    
                }
            }

        }
    }
    
    /** Controls the dragged item
     * @param evt Mouse event.
    */    
    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) 
    {
        if(selectedObject){
            
            BasicSimObject moveMe = (BasicSimObject)world.getObjectList().get(objectIndex);
            if(evt.getX() >= 0 && evt.getX() <= ((BasicSimWorld)world).getWorldDimensions()[0]) moveMe.setXCoord(evt.getX()+offSetX);
            if(evt.getY() >= 0 && evt.getY() <= ((BasicSimWorld)world).getWorldDimensions()[2])moveMe.setZCoord(evt.getY()+offSetY);
            display.repaint();
           
        }
        
        else if(BEGIN_ROTATION){
            try
            {
                int VALUE = (int)(((SimRCX)(world.getObjectList().get(objectIndex))).getActualBearingXZ())+(int)(evt.getX() - this.INITIAL_X_ROT);
                
                ((SimRCX)(world.getObjectList().get(objectIndex))).setActualBearingXZ(VALUE);
                robotRotation.updateValue(((SimRCX)(world.getObjectList().get(objectIndex))));
                display.repaint();
            }
            catch(Exception e2){}
        
        }
        
        //mouse coordinate display
         if(evt.getX() >= 0 && evt.getX() <= ((BasicSimWorld)world).getWorldDimensions()[0]) this.xT.setText(""+evt.getX());
         if(evt.getY() >= 0 && evt.getY() <= ((BasicSimWorld)world).getWorldDimensions()[2])this.zT.setText(""+evt.getY()); 
    }
    
    /** Positions the dragged item in the desired location
     * @param evt Mouse event.
     */    
    private void jPanel2MouseReleased(java.awt.event.MouseEvent evt) {
         if(selectedObject){
            
             //undo/redo purposes *****
            this.pushUndoItem(copyList((LinkedList)world.getObjectList()));
             
            BasicSimObject moveMe = (BasicSimObject)world.getObjectList().get(objectIndex);

            if(moveMe instanceof SimGround) {
                SimGround ground = (SimGround)moveMe;
                ground.setOutlineColor(Color.black);
                ground.setSelected(false);
            }
            else if(moveMe instanceof SimWall){ 
                SimWall wall = (SimWall)moveMe;
                wall.setOutlineColor(Color.black);
                wall.setSelected(false);
            }
            else if(moveMe instanceof SimRCX){ 
                SimRCX robot = (SimRCX)moveMe;
                robot.setSelected(false);
                this.robotRotation.hide();
            }
                    
               
            display.repaint();
            selectedObject = false;
            deselectOtherObjects();
            objectEdit.hide();
            colorsPop.hide();
            robotRotation.hide();
            setSize(this.getWidth(),this.getHeight());
            
        }
         
    }
   
    /** Positions the dragged item in the desired location
    * @param evt Mouse event.
    */    
    private void jPanel2MouseMoved(java.awt.event.MouseEvent evt) {
         if(evt.getX() >= 0 && evt.getX() <= ((BasicSimWorld)world).getWorldDimensions()[0]) this.xT.setText(""+evt.getX());
         if(evt.getY() >= 0 && evt.getY() <= ((BasicSimWorld)world).getWorldDimensions()[2]) this.zT.setText(""+evt.getY());    
    }
    
    /** Rotates a shape so that selection of a rotated shape may occur.
     * @param shape the name of the shape
     * @param angle the angle that it has been rotated
     * @param X the X position
     * @param Z the X position
     * @return the new oriented shape
     */    
    private Shape rotateShape(Shape shape, double angle, double X, double Z)
    {
        // convert the angle to radians
        double theta=Math.toRadians(angle);

        // create a new affine transform rotator
        AffineTransform  atx = AffineTransform.getRotateInstance(theta,X,Z); 

        // create a rotated version of the shape
        shape = atx.createTransformedShape(shape);

        // return the shape
        return shape;
    }

    /** Dynamically loads a simworld class
     * @param name the name of the simworld as a string
     * @return the simworld, or null if unsuccessful
     */
    public SimWorld createSimWorld(String name)
    {
        SimWorld s=null;

        try
        {
            // try to create the class
            Class simworldClass=Class.forName(name);

            try
            {
                // try to cast the class to a SimWorld, this will fail if the class
                // is not a valid Intellego SimWorld
                s=(SimWorld)simworldClass.newInstance();
            }
            catch (Exception e)
            {
                MainInterface.displayMessage("Error: this class is not a valid Intellego SimWorld");
            }
        }

        catch (Exception e)
        {
            // failed to create the class, so return null
            //MainInterface.displayMessage("Error creating class");
            Intellego.addToLog("SimUI.createSimWorld(): Error creating class: "+name+": "+e);
            return null;
        }

        // return the simworld
        return s;
    }
	
    /**
    * Allows the user to pick a robot class, and loads it into this simulation
    */
    public void addRobot()
    {
        if(world==null || display==null)
        {
            MainInterface.displayMessage("You must select a SimWorld and a SimDisplay first");
        }
        else
        {
            try{this.stopSimulation();}
            catch(Exception e2){}
            try{
                // get the name of the class from the user
                File file = getClassName("Pick a Controller class file:","controllers");
                String className = file.getName();
                className=className.substring(0,file.getName().length() - 6);
                
                // check user actually selected a class name
                if (className.equals(NO_CLASS))
                {
                     // do nothing
                }
                else
                {
                    Controller c=createController(file);

                    if(c!=null)
                    {
                        // and add it to the system if it is not null
                        addController(c);
                    }
                    else
                    {
                        MainInterface.displayMessage("Failed to create Controller");
                        Intellego.addToLog("SimUI.addRobot(): Failed to create Controller with name "+className);
                    }
                }
            }
            catch(Exception e){}
        }
    }
	
    /**
    * Gets the class name of the required class from the user 
    * using a JFileChooser
    * @param title the title to display in the FileChooser window
    * @param base_dir the directory to start looking in
    * @return the classname as string
    */
    public File getClassName(String title,String base_dir)
    {
        //String className=" ";
        File file = null;
        // pop up a file chooser dialog
        JFileChooser chooser=new JFileChooser(new File(System.getProperties().getProperty("user.dir"),base_dir));

        // add a filename filter for class files
        String[] extensions={".class"};
        chooser.addChoosableFileFilter(new FileChooserFilter(extensions,"Compiled Class File"));

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

        int returnVal=chooser.showOpenDialog(this);

        try
        {
            if (returnVal==JFileChooser.APPROVE_OPTION) // user has selected a file
            {
                // get the user's selected file
                file=chooser.getSelectedFile();


                //sets the path only for worldobjects
                if(base_dir.equals("simworlds")){    
                    path = ((file.getPath()).substring(0,(((file.getPath()).length()) - ((file.getName()).length()))));
                    currentWorldFile = file.getName();
                }

                // return the file
                return (file);
            }

            else // user selected cancel or some other button we are going to ignore
            {
                return null;//NO_CLASS;
            }

        }
        catch(Exception e)
        {
            return null;
        }
    }
	
    /** Dynamically loads a controller class
     * Modified by: Simon Zienkiewicz
     * @return the controller, or null if unsuccessful
     * @param file the name of the file
     */
    public Controller createController(File file)
    {
        Controller c=null;
        Class controllerClass=null;
        String name = file.getName();
        name=name.substring(0,file.getName().length() - 6);

        try
        {   // try to create the class
            AdaptiveClassLoader extra = new AdaptiveClassLoader(new Vector());
            controllerClass = extra.loadClass(file,false);

            try
            {
                // try to cast the class to a Controller, this will fail if the class
                // is not a valid Intellego Controller
                c=(Controller)controllerClass.newInstance();
                Intellego.addToLog("SimUI.createController(): class "+name+": CREATED SUCCESSFULLY");
            }
            catch (Exception e)
            {
                MainInterface.displayMessage("Error: class is not a valid Intellego Controller class");
                Intellego.addToLog("SimUI.createController(): Attempt to create Intellego Controller "+name+" failed. Not a valid Controller.");
            }
        }
        catch (Exception e)
        {
            // failed to create the class, so return null
            MainInterface.displayMessage("Error creating class");
            Intellego.addToLog("SimUI.createController(): Error creating class "+name+": "+e);
            return null;
        }

        // return the controller
        return c;
    }
	
    /**
    * Adds a controller to this simulation
    * @param c the controller to be added
    */
    public void addController(Controller c)
    {
        // check if controller creation was successsful
        if(c!=null)
        {   Intellego.addToLog("SimUI.addController: C is not null");
            // ... if so then add this controller to the list
            controllerList.add(c);

            // and get robot details
            d=new InitRobotDialog();
                        
            //possible future improvements could use hard coded simulation
            //values such as battery voltage
            //this is beyond the scope of the current version
            //d.setVisible(true);

            // create the robot
            d.createRobot(world,c,display,this);
            d.callOK();
            d.hide();
            
            colorsPop.hide();
            objectEdit.hide();

            propertyPanel.add(d,new AbsoluteConstraints(10,40,140,300));
            setSize(this.getWidth(),this.getHeight());
            //initialize light sensor readings if the robot possesses any light sensors
            this.setLightSensorProperties();
            lightProp.setEnabled(true);

            // and repaint the display
            repaint();
        }
        else // failed to create controller
        {
            MainInterface.displayMessage("Failed to add controller");
            Intellego.addToLog("SimUI.addController: Failed to add controller");
        }
    }

    /**
    * Starts this simulation (by starting a new thread for this instance)
    */
    public void startSimulation()
    {
        // check there is a simworld and a simdisplay open
        if (world==null || display==null)
        {
            // if not display an error message
            MainInterface.displayMessage("You must pick a SimWorld and a SimDisplay first");
        }
        else
        {

           // set this sim running in a new thread
            running=true;
            t =new Thread(this);
            exit = true;
            t.start();

            this.simulation_is_ON();

            super.setTitle("Simulation State: PLAYING");

            Thread clean = new Thread(new GarbageCleanUp());
            clean.setPriority(Thread.MAX_PRIORITY);
            clean.start();

            // start up all the controllers
            for (int i=0;i<controllerList.size();i++)
            {
                Controller c=(Controller)controllerList.get(i);
                Thread u=new Thread(c);
                u.setPriority(Thread.MIN_PRIORITY);
                u.start();
                Intellego.addToLog("SimUI.run(): Started controller");
            }

        }
    }
        
    /** A class that runs a thread in order to initiate the garbage collector and recycle memory. */    
    private class GarbageCleanUp extends Thread{

        /** A method that runs a thread in order to initiate the garbage collector and
         * recycle memory.
         */        
        public GarbageCleanUp(){
            System.gc();
        }
    }
	
    /**
    * Stops this simulation
    */
    public void stopSimulation()
    {
        // check there is a simworld and a simdisplay open
        if (world==null || display==null)
        {
            // if not display an error message
            MainInterface.displayMessage("No simulation to stop");
        }
        else
        {
            this.simulation_is_OFF();
            exit = false;
            running = false;
            super.setTitle("Simulator: "+this.path+ this.currentWorldFile);

        }
    }
                      
    /** Creates a dialog window from where the user can alter simulation colors
     * @param index the index of the object
     * @param name the name of the object
     * @param simg the name if the object is an instance of SimGround, otherwise NULL
     * @param grid the name if the object is an instance of SimGrid, otherwise NULL
     */       
    private void popUpColorOptions(int index, String name, SimGround simg, GridDisplay grid){
        if(openWorld){
            colorsPop.setVisible(true);
            objectEdit.setVisible(false);
            if(!colorDialogExist){
                colorsPop.createColorPopUp();
                colorDialogExist = true;
            }
            if(index != 2)deselectOtherObjects();
            
            switch(index){
                case 0: colorsPop.updateColorPopUp(name, index,display,world,"wall",simg, grid);
                        break;
                case 1: colorsPop.updateColorPopUp(name, index,display,world,null,simg, grid);
                        break;
                case 2: colorsPop.updateColorPopUp(name, index,display,world,null,simg, grid);
                        break;
                case 3: colorsPop.updateColorPopUp(name, 0,display,world,"sensorTouch",simg, grid);
                        break;
                case 4: colorsPop.updateColorPopUp(name, 0,display,world,"sensorLight",simg, grid);
                        break;
                case 5: colorsPop.updateColorPopUp(name, 0,display,world,"robot",simg, grid);
                         break;
                case 6: colorsPop.updateColorPopUp(name, index,display,world,null,simg, grid);
                         break;
                case 7: colorsPop.updateColorPopUp(name, 0,display,world,"road",simg, grid);
                         break;
             }
        }
    }
    
    /** Deselects all the objects except the selected object. */    
    private void deselectOtherObjects(){
        
        LinkedList newList = ((BasicSimWorld)world).getObjectList();
        
        for(int i=0;i<newList.size();i++){
            SimObject simObject2 = (SimObject)newList.get(i);
            if(simObject2 instanceof SimGround){
                ((SimGround)simObject2).setSelected(false);
                ((SimGround)simObject2).setOutlineColor(Color.black);
                display.repaint();
            }
            else if(simObject2 instanceof SimWall){
                ((SimWall)simObject2).setSelected(false);
                ((SimWall)simObject2).setOutlineColor(Color.black);
                display.repaint();
            }
            else if(simObject2 instanceof SimRCX){
                ((SimRCX)simObject2).setSelected(false);
                this.robotRotation.hide();
                 display.repaint();   
            }
        }
        
        ROBOT_IS_SELECTED = false;
        BEGIN_ROTATION = false;
        display.repaint();
        
    }
    
    /** Adds a SimGround or SimWall object to the world
     * @param type the type of object
     */     
    private void addObjectToWorld(String type){
        if(!lock.isSelected()){
            if(openWorld){
                if(type.equals("wall")){
                    SimWall newWall = new SimWall(150.0,0.0,50.0,0.0,200.0,30.0);
                    ((BasicSimWorld)world).addObjecttoFront(newWall);

                }
                else{
                    Random randomColor = new Random();
                    double colorIndex = randomColor.nextDouble();
                    Color groundColor=Color.blue;
                    if(colorIndex >= 0.5) groundColor=Color.black;

                    SimGround newGround = new SimGround(450.0,0.0,150.0,0.0,(int)(50+(50*colorIndex)*2),(int)(50+(50*colorIndex)*2),groundColor);
                    ((BasicSimWorld)world).addObjecttoFront(newGround);
                }
               display.repaint();
               
               //undo/redo purposes *****
               this.pushUndoItem(copyList((LinkedList)world.getObjectList()));
            }
        }
       
      }
     
    /** Creates a dialog window from where the user can alter grid Options
     * @param title the title of the grid options dialog
     */  
    private void popUpGridOptions(String title){
        if(openWorld){
        newGridOption.setVisible(true);
            if(!gridDialogExist){
                newGridOption.createGridPopUp(title, gridDisplay, display, this);
                gridDialogExist=true;
            }
            else newGridOption.show();
        }
    }
    
    /**
    * Allows the saving of a designed world
    */  
    private void saveWorld(){
        if(world != null){
            SaveWorldDialog saveWorld = new SaveWorldDialog();
            saveWorld.createSaveWorldPopUp(path,world,this.currentWorldFile);
        }
        else{
            MainInterface.displayMessage("Cannot save an empty world");
            Intellego.addToLog("SimUI.saveWorld(): Save to file failed: ");
        }
    }
    
    /** Determines if the grid is turned on or off
     * @return true if gird is on, false otherwise
     */  
    public boolean isGridOn(){
        return jPanel3.isVisible();
    }
    
    /** Sets the grid on if true, off if false
     * @param a true to set grid on, false otherwise
     */  
    public void setGrid(boolean a){
        if(a){
            jPanel2.setOpaque(false);
            display.repaint();
            jPanel3.setVisible(true);
            
        }
        else{ 
            jPanel3.setVisible(false);  
            jPanel2.setOpaque(true);
            display.repaint();
            
        }
    }
    
    /**
    * Sets the Light Sensor Proptertises
    */  
    private void setLightSensorProperties(){
        
        if(!colorPropertiesExist){
            lightProperties.createLightSensorPropertiesPopUp();
            colorPropertiesExist=true;
            
        }
        else{
            lightProperties.call();
        }
    
    }
    
    /** Executes is an item has changed state.
     * @param e event
     */    
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if(source == lock){
            if(lock.isSelected()){
                deselectOtherObjects();
                objectEdit.hide();
                colorsPop.hide();
                setSize(this.getWidth(),this.getHeight());
            }
        }
    }
    
    /** Puts objects in the undo, redo stack accordingly.
     * @param a the indicator, either "undo" or "redo"
     */    
    private void undoRedoSet(String a){
    
        if(a.equals("undo")){
            
            enableUndo(2);
            LinkedList b = (LinkedList) this.undoStack.pop();
            
            this.redoStack.push(b);
            ((BasicSimWorld)world).updateObjectList(copyList((LinkedList)this.undoStack.peek()));
            this.redo.setEnabled(true);
       }
        
        else{
    
            if(redoStack.size() >=1){
                //push onto undo stack
                LinkedList b = (LinkedList) this.redoStack.pop();
                this.undoStack.push(b);
                //update the screen
                ((BasicSimWorld)world).updateObjectList(copyList(b));
                
                if(redoStack.isEmpty())this.redo.setEnabled(false);
                }
            enableUndo(1);
        }
        
        this.objectEdit.hide();
        setSize(this.getWidth(),this.getHeight());
        display.repaint();
    
    }
    
    /** Enables the undo stack if its possible.
     * @param i the test to see if the undo stack can be enabled
     */    
    private void enableUndo(int i){
    
        if(this.undoStack.size()<= i) this.undo.setEnabled(false);
        else this.undo.setEnabled(true);
        
    }
    
    /** Pushes an item into the undo stack.
     * @param a the linklist which contains the item
     */    
    public void pushUndoItem(LinkedList a){
        
        //check for clone
        try{ 
            if(a.size() == ((LinkedList)this.undoStack.peek()).size())

                for(int i=0;i<a.size();i++){
                    
                    BasicSimObject c = (BasicSimObject)a.get(i);
                    BasicSimObject d = (BasicSimObject)((LinkedList)this.undoStack.peek()).get(i);

                    if(c instanceof SimWall){
                        if(c.getXCoord() == d.getXCoord() && c.getYCoord() == d.getYCoord() && c.getZCoord() == d.getZCoord()
                                && c.getActualBearingXZ() == d.getActualBearingXZ() && c.getLength() == d.getLength() && c.getWidth() == c.getWidth())
                        {
                            //do nothing since they are the same   
                        }
                        
                        else{
                            this.undoStack.push(a);
                            break;
                        }
                    }
                    else if(c instanceof SimGround){
                        
                        SimGround e = (SimGround)c;
                        SimGround f = (SimGround)d;
                        
                        if(e.getXCoord() == f.getXCoord() && e.getYCoord() == f.getYCoord() && e.getZCoord() == f.getZCoord()
                                && e.getActualBearingXZ() == f.getActualBearingXZ() && e.getLength() == f.getLength() && e.getWidth() == f.getWidth() && e.getColor().getRGB() == f.getColor().getRGB())
                        {
                            //do nothing since they are the same  
                            
                        }
                        
                        else{
                            this.undoStack.push(a);
                            break;
                        }
                    }
                }
            else this.undoStack.push(a);
          
        }
        catch(Exception e){this.undoStack.push(a);}
        
        this.redoStack.clear();
        this.redo.setEnabled(false);
        enableUndo(1);
    }
    
    /** Copies the entire contents of one linked list and returns a new linked list of
     * the same size and contents.
     * @param a the linklist requiring duplication (ORIGINAL)
     * @return a new linklist mirroring the original (COPY)
     */    
    public LinkedList copyList(LinkedList a){
        
        LinkedList b = new LinkedList();
        for(int i=0;i<a.size();i++){
            
            SimObject c = (SimObject)a.get(i);
            
            if(c instanceof SimWall){
                b.add(new SimWall(c.getXCoord(),c.getYCoord(),c.getZCoord(),c.getActualBearingXZ(),c.getLength(),c.getWidth()));
            }
            
            else if(c instanceof SimGround){
                b.add(new SimGround(c.getXCoord(),c.getYCoord(),c.getZCoord(),c.getActualBearingXZ(),c.getLength(),c.getWidth(),((SimGround)c).getColor()));
            }
            
            else b.add(c);
            
        }
        return b;
    }
     
}