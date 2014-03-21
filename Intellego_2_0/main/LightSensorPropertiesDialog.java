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
import javax.swing.filechooser.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;

/** Provides a dialog box for the user to define colour value which the robot can
 * read within the simulation environment.
 * @author Simon Zienkiewicz
 */ 
public class LightSensorPropertiesDialog extends JFrame implements ActionListener, ItemListener, ChangeListener, WindowFocusListener
{
    private JLabel checkL, colorL, rangeL, errorL;
    private JLabel[] tA = new JLabel[11];
    private JLabel[] pA = new JLabel[11];
    private JLabel[] mA = new JLabel[11];
    private JButton OK, cancel, save, open, clear;
    private JTextArea[] previewA = new JTextArea[11];
    private JTextField[] nameA = new JTextField[11];
    private JCheckBox c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11;
    private JCheckBox[] checkA = new JCheckBox[11];
    private JSpinner[] lrSpinner = new JSpinner[11];
    private JSpinner[] urSpinner = new JSpinner[11];
    private JSpinner[] leSpinner = new JSpinner[11];
    private JSpinner[] ueSpinner = new JSpinner[11];
    private Container[][] containerArray = new Container[11][7];
    private int row=11; 
    private int column=7;
    private Color desiredColor;
    private Color[] colorA={Color.black,Color.blue,Color.cyan,Color.gray,Color.green,Color.magenta,Color.orange,Color.pink,Color.red,Color.white,Color.yellow};
    private String[] availableColors = {"Black","Blue","Cyan","Gray","Green","Magenta","Orange","Pink","Red","White","Yellow"};
    private int index;
    private final int b=30;
    private static boolean[] desiredColors= new boolean[11];;
    private static int[] lowerRange= new int[11];
    private static int[] upperRange= new int[11];
    private static int[] lowerError= new int[11];
    private static int[] upperError= new int[11];
    private LightSensorColorLibrary lib=null;
    
   

    /** Creates and displays a LightSensorProperties popup. */    
    public void createLightSensorPropertiesPopUp()
    {
        
        //sets the properties of the popup window
        setTitle("LightSensor Properties:");
        setSize(390+b,550);
        setLocation(400,180);
        
        this.addWindowFocusListener(this);
        
        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        
        setVisible(true);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setResizable(false);
        
        //load icon image from a file
        try{ 
                BufferedImage simIcon= ImageIO.read(new File("images\\lego.png"));
                this.setIconImage(simIcon);
        }
        catch(Exception e){}
        
        Container mainPanel=getContentPane();
        mainPanel.setLayout(new BorderLayout(1,1));

        Container Panell=new Container();
        Panell.setLayout(new AbsoluteLayout());

        Container Panel5=new Container();
        Panel5.setLayout(new FlowLayout());
        
        this.checkL = new JLabel("Check:");
        this.colorL = new JLabel("Color:");
        this.rangeL = new JLabel("Range:");
        this.errorL = new JLabel("Error:");
        //set color
        checkL.setForeground(Color.lightGray);
        colorL.setForeground(Color.lightGray);
        rangeL.setForeground(Color.lightGray);
        errorL.setForeground(Color.lightGray);
        
        for(int i=0;i<tA.length;i++){tA[i] = new JLabel("~"); tA[i].setForeground(Color.yellow); tA[i].setVisible(false);}
        
        for(int i=0;i<mA.length;i++){mA[i] = new JLabel("-"); mA[i].setForeground(Color.yellow);mA[i].setVisible(false);}
        
        for(int i=0;i<pA.length;i++){pA[i] = new JLabel("+"); pA[i].setForeground(Color.yellow);pA[i].setVisible(false);}
        
        for(int i=0;i<previewA.length;i++)
        {
            previewA[i] = new JTextArea(1,2); 
            previewA[i].setBackground(this.colorA[i]);
            previewA[i].setFocusable(false);
        }
        
        for(int i=0;i< nameA.length;i++){
            nameA[i] = new JTextField(5);
            nameA[i].setText(this.availableColors[i]);
            nameA[i].setForeground(Color.darkGray);
            nameA[i].setBackground(Color.yellow);
            nameA[i].setEditable(false);
        }
         
        for(int i=0;i< nameA.length;i++){
            (checkA[i] = new JCheckBox((i+1)+").")).addItemListener(this);
            checkA[i].setForeground(Color.darkGray);
            checkA[i].setBackground(Color.yellow);
        }
       
        for(int i=0;i< lrSpinner.length;i++){
            (lrSpinner[i] = new JSpinner(new SpinnerNumberModel(20,0,60,1))).addChangeListener(this);;
            lrSpinner[i].setForeground(this.colorA[i]);
            lrSpinner[i].setBackground(this.colorA[i]);
            lrSpinner[i].setVisible(false);
         }
        
        for(int i=0;i< urSpinner.length;i++){
            (urSpinner[i] = new JSpinner(new SpinnerNumberModel(40,(Integer.parseInt(lrSpinner[i].getValue().toString())+1),60,1))).addChangeListener(this);
            urSpinner[i].setForeground(this.colorA[i]);
            urSpinner[i].setBackground(this.colorA[i]);
            urSpinner[i].setVisible(false);
        }
       
        for(int i=0;i< leSpinner.length;i++){
            leSpinner[i] = new JSpinner(new SpinnerNumberModel(0,0,5,1));
            leSpinner[i].setForeground(this.colorA[i]);
            leSpinner[i].setBackground(this.colorA[i]);
            leSpinner[i].setVisible(false);
        }
         for(int i=0;i< ueSpinner.length;i++){
            ueSpinner[i] = new JSpinner(new SpinnerNumberModel(0,0,5,1));
            ueSpinner[i].setForeground(this.colorA[i]);
            ueSpinner[i].setBackground(this.colorA[i]);
            ueSpinner[i].setVisible(false);
        }
        
        //array containing all the hiddable components
        for(int r=0;r<row;r++){
            for(int c=0;c<column;c++){
                switch(c){
                    case 0:     containerArray[r][c]=lrSpinner[r];
                                break;
                    case 1:     containerArray[r][c]=tA[r];
                                break;
                    case 2:     containerArray[r][c]=urSpinner[r];
                                break;
                    case 3:     containerArray[r][c]=pA[r];
                                break;
                    case 4:     containerArray[r][c]=leSpinner[r];
                                break;
                    case 5:     containerArray[r][c]=mA[r];
                                break;
                    case 6:     containerArray[r][c]=ueSpinner[r];
                                break;
                }
               
            }
        }
         
        (OK=new JButton("OK")).addActionListener(this);
        (open=new JButton("Open")).addActionListener(this);
        (save=new JButton("Save")).addActionListener(this);
        (clear=new JButton("Clear")).addActionListener(this);
        (cancel=new JButton("Cancel")).addActionListener(this);
        
        //set color
        OK.setBackground(Color.lightGray);
        OK.setForeground(Color.darkGray);
        open.setBackground(Color.lightGray);
        open.setForeground(Color.darkGray);
        save.setBackground(Color.lightGray);
        save.setForeground(Color.darkGray);
        clear.setBackground(Color.lightGray);
        clear.setForeground(Color.darkGray);
        cancel.setBackground(Color.lightGray);
        cancel.setForeground(Color.darkGray);
        cancel.setEnabled(false);

        //labels placed in panel
        Panell.add(this.checkL,new AbsoluteConstraints(20,10));
        Panell.add(this.colorL,new AbsoluteConstraints(95,10));
        Panell.add(this.rangeL,new AbsoluteConstraints(187+b,10));
        Panell.add(this.errorL,new AbsoluteConstraints(310+b,10));

        for(int a=0;a<checkA.length;a++){
            int num = (int)(40*a);
            Panell.add(this.checkA[a],new AbsoluteConstraints(20,40+num));
            
            Panell.add(this.previewA[a],new AbsoluteConstraints(80,44+num));
            
            Panell.add(this.nameA[a],new AbsoluteConstraints(80+b,42+num));
            Panell.add(this.lrSpinner[a],new AbsoluteConstraints(155+b,40+num));
            Panell.add(this.tA[a],new AbsoluteConstraints(202+b,43+num));
            Panell.add(this.urSpinner[a],new AbsoluteConstraints(213+b,40+num));

            Panell.add(this.mA[a],new AbsoluteConstraints(278+b,41+num));
            Panell.add(this.leSpinner[a],new AbsoluteConstraints(286+b,40+num));
            Panell.add(this.pA[a],new AbsoluteConstraints(328+b,43+num));
            Panell.add(this.ueSpinner[a],new AbsoluteConstraints(336+b,40+num));
        }
        
        Panel5.add("South",OK);
        Panel5.add("South",open);
        Panel5.add("South",save);
        Panel5.add("South",clear);
        Panel5.add("South",cancel);

        mainPanel.setBackground(Color.darkGray);
        mainPanel.add("North",Panell);
        mainPanel.add("South",Panel5);
        
        if(lib != null) setupProperties();
        
        //load default file
        try{this.loadDefaultLightProperties();}
        catch(Exception e){};
        
        
        this.show();
    }
    
    /** Loads the defaul light values for any simulation world. */    
    private void loadDefaultLightProperties(){
    
        File oneF = new File(System.getProperties().getProperty("user.dir"),"LightSenesorReadings");
        File twoF = new File(oneF.getAbsolutePath()+"/DEFAULT.light");
        
        char[] stringA = new char[220];
        // open selected file
        try
        {
            FileReader fw=new FileReader(twoF);
            fw.read(stringA); 
            fw.close();
        }
        
        catch(Exception e)
        {
            MainInterface.displayMessage("Cannot open LightProperties file");
            Intellego.addToLog("LightSensorPropertiesDialog.openFromTextfile(): Opening of file failed: "+e);
        }
        
        String fileData = new String(stringA);
        fileData = fileData.trim();
        
        StringTokenizer breakup = new StringTokenizer(fileData," ");
        
        while(breakup.hasMoreTokens()){
            
            String data = breakup.nextToken();
            
            if(data.equals("true")){
                int index = Integer.parseInt(breakup.nextToken());
                this.checkA[index].setSelected(true);
                ((SpinnerNumberModel)(lrSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
                ((SpinnerNumberModel)(urSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
                ((SpinnerNumberModel)(leSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
                ((SpinnerNumberModel)(ueSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
           }
           
            else if(data.equals("false")){
                int index = Integer.parseInt(breakup.nextToken());
                this.checkA[index].setSelected(false);
           }
       }
     }
     
    /** Makes sure that the minimum colour value does not exceed the maximum colour
     * value when the values are selected by the user.
     * @param e event
     */    
    public void stateChanged(ChangeEvent e){
    
         Object source = e.getSource();
      
         for(int i=0;i< urSpinner.length;i++){
            if(source == urSpinner[i]){
                ((SpinnerNumberModel)(lrSpinner[i].getModel())).setMaximum(new Integer(Integer.parseInt(urSpinner[i].getValue().toString())-1));     
                ((SpinnerNumberModel)(urSpinner[i].getModel())).setMinimum(new Integer(Integer.parseInt(lrSpinner[i].getValue().toString())+1));     
            }
         }
        
         //makes sure that every lower value spinner can not exceed the upper value spinner
         for(int i=0;i< lowerRange.length;i++){
            if(source == lrSpinner[i]){
                ((SpinnerNumberModel)(urSpinner[i].getModel())).setMinimum(new Integer(Integer.parseInt(lrSpinner[i].getValue().toString())+1));     
                ((SpinnerNumberModel)(lrSpinner[i].getModel())).setMaximum(new Integer(Integer.parseInt(urSpinner[i].getValue().toString())-1));     
            }
         }
   }
    
    /** Makes sure that once a colour is selected via the check box, that appropriate
     * spinners are displayed to allow the user to set the values
     * @param e the event
     */    
    public void itemStateChanged(ItemEvent e) {
    
        Object source = e.getItemSelectable();
        
        int row=0;
        boolean isSelected=false;
                
        for(int i=0;i<checkA.length;i++){
            if(source == checkA[i]){
                isSelected =checkA[i].isSelected();
                row=i;
            }
        }
       
        for(int i=0;i<this.column;i++){
            containerArray[row][i].setVisible(isSelected);
        }
    }
        
    /** Sets up the dialog properties. */    
    private void setupProperties(){
         //fill checkboxes
        try{
            for(int i=0;i<checkA.length;i++){
                checkA[i].setSelected(lib.getColorInfo()[i]);
                for(int a=0;a<this.column;a++) containerArray[i][a].setVisible(checkA[i].isSelected());
            }

            //fill lower range values
            for(int i=0;i<this.lrSpinner.length;i++){
                if(checkA[i].isSelected()) ((SpinnerNumberModel)(lrSpinner[i].getModel())).setValue(new Integer(lib.getLowerRangeInfo()[i]));
            }

            //fill upper range values
            for(int i=0;i<this.urSpinner.length;i++){
                if(checkA[i].isSelected()) ((SpinnerNumberModel)(urSpinner[i].getModel())).setValue(new Integer(lib.getUpperRangeInfo()[i]));
            }

            //fill lower error values
            for(int i=0;i<this.leSpinner.length;i++){
                if(checkA[i].isSelected()) ((SpinnerNumberModel)(leSpinner[i].getModel())).setValue(new Integer(lib.getLowerErrorInfo()[i]));
            }
            //fill upper error values
            for(int i=0;i<this.ueSpinner.length;i++){
                if(checkA[i].isSelected()) ((SpinnerNumberModel)(ueSpinner[i].getModel())).setValue(new Integer(lib.getUpperErrorInfo()[i]));
            }
        }
        catch(Exception e){
            //do nothing
        }
   }
    
    /** Initialises the values in each array, from the changes the user made in the
     * dialog.
     */    
    private void initializeArrays(){
    
        //fill desired color array
        for(int i=0;i<desiredColors.length;i++) this.desiredColors[i] =checkA[i].isSelected();
              
        //fill lower range array
        for(int i=0;i<this.lowerRange.length;i++){
            if(checkA[i].isSelected()) lowerRange[i]= Integer.parseInt(lrSpinner[i].getValue().toString());
        }
        
        //fill upper range array
        for(int i=0;i<this.upperRange.length;i++){
            if(checkA[i].isSelected()) upperRange[i]= Integer.parseInt(urSpinner[i].getValue().toString());
        }
        //fill lower error array
        for(int i=0;i<this.lowerError.length;i++){
            if(checkA[i].isSelected()) lowerError[i]= Integer.parseInt(leSpinner[i].getValue().toString());
        }
        //fill upper error array
        for(int i=0;i<this.upperError.length;i++){
            if(checkA[i].isSelected()) upperError[i]= Integer.parseInt(ueSpinner[i].getValue().toString());
        }
   }
    
    /** Shows the dialog to the user. */    
    public void call(){
        this.cancel.setEnabled(true);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.show();
    
    }
    
    /** Saves the current dialog settings to a text file. */    
    private void saveToTextfile(){ 
        // create the log file
		
        JFileChooser chooser=new JFileChooser(new File(System.getProperties().getProperty("user.dir"),"LightSenesorReadings"));
    	        
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
        
        String[] extensions={".light"};
	chooser.addChoosableFileFilter(new FileChooserFilter(extensions,"LightSensor Reading Files"));
        int returnValue=chooser.showSaveDialog(this);
        
	if(returnValue==JFileChooser.APPROVE_OPTION)
	{
            // save file as
            try
            {
                FileWriter fw=new FileWriter(chooser.getSelectedFile().getPath()+".light");
		for(int i=0; i<this.checkA.length;i++){
                    if(checkA[i].isSelected()){
                        fw.write(checkA[i].isSelected()+" "+i+" ");
                        fw.write(lrSpinner[i].getValue().toString()+ " ");
                        fw.write(urSpinner[i].getValue().toString()+ " ");
                        fw.write(leSpinner[i].getValue().toString()+ " ");
                        fw.write(ueSpinner[i].getValue().toString()+ " ");
                    }
                    else{
                        fw.write(checkA[i].isSelected()+" "+i+" ");
                    }
                }
   		fw.close();
                
            }
            catch (Exception e)
            {
            	MainInterface.displayMessage("Cannot save LightProperties file");
		Intellego.addToLog("LightSensorPropertiesDialog.saveToTextfile(): Save to file failed: "+e);
            }
	}
    }
    
    /** Takes a text file and sets the settings within the dialog to that of the text
     * file.
     */    
    private void openFromTextfile(){ 
        JFileChooser chooser=new JFileChooser(new File(System.getProperties().getProperty("user.dir"),"LightSenesorReadings"));
    	
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
        // add a filename filter for light files
	String[] extensions={".light"};
	chooser.addChoosableFileFilter(new FileChooserFilter(extensions,"LightSensor Reading Files"));
        chooser.showOpenDialog(this);
        
        char[] stringA = new char[220];
        // open selected file
        try
        {
            FileReader fw=new FileReader(chooser.getSelectedFile());
            fw.read(stringA); 
            fw.close();
        }
        
        catch (Exception e)
        {
            MainInterface.displayMessage("Cannot open LightProperties file");
            Intellego.addToLog("LightSensorPropertiesDialog.openFromTextfile(): Opening of file failed: "+e);
        }
        
        String fileData = new String(stringA);
        fileData = fileData.trim();
        
        StringTokenizer breakup = new StringTokenizer(fileData," ");
        
        while(breakup.hasMoreTokens()){
            
            String data = breakup.nextToken();
            
            if(data.equals("true")){
                int index = Integer.parseInt(breakup.nextToken());
                this.checkA[index].setSelected(true);
                ((SpinnerNumberModel)(lrSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
                ((SpinnerNumberModel)(urSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
                ((SpinnerNumberModel)(leSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
                ((SpinnerNumberModel)(ueSpinner[index].getModel())).setValue(new Integer(breakup.nextToken()));
           }
           
            else if(data.equals("false")){
                int index = Integer.parseInt(breakup.nextToken());
                this.checkA[index].setSelected(false);
           }
       }
   }
    
    
    /**
    *  Action event handler - sets up color code according to user selection
    *
    *  @param e the action event
    */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource()==OK){
            
            initializeArrays();
            if(lib == null){
                lib = new LightSensorColorLibrary(this.desiredColors,this.lowerRange,this.upperRange,this.lowerError,this.upperError);
            }
            else{
                lib.updateDesiredColors(this.desiredColors,this.lowerRange,this.upperRange,this.lowerError,this.upperError);
            }
                    
            this.hide();
        }
        
        else if(e.getSource()==open){
            openFromTextfile();
        }
        else if(e.getSource()==save){
            saveToTextfile();
        }
        
        else if(e.getSource()==clear){
            for(int i=0;i<this.checkA.length;i++){
                checkA[i].setSelected(false);
            }
        }
        
        else
        {
            this.setupProperties();
            this.hide();
        }
    }
         
    public void windowGainedFocus(WindowEvent e) 
    {
        OK.requestFocusInWindow(); 
    }
    
    public void windowLostFocus(WindowEvent e){}
    
}