package main;

import intellego.Intellego;
import util.*;
import interfaces.*;
import real.*;
import simworldobjects.*;
import main.*;
import NetBeansResources.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


/** Provides a dialog box for the user to save their simulation world.
 * @author Simon Zienkiewicz
 */ 
public class SaveWorldDialog extends JDialog implements ActionListener
{
    private JLabel fileL, folderL, typeL;
    private JButton OK, cancel;
    private JTextField fileT, folderT;
   
    private SimGround simg;
    private GridDisplay grid;
    private int gridSize;
    private int pixelSize=4;
    private boolean noGrid=true;
    
    private BasicSimWorld  world;
    private String path;
    private String currentWorldFile;
    private String fileName = ""; 
    private JFileChooser chooser;
    
    
    /** Creates a dialog box for the user to save their simulation world.
     * @param path the path of the file
     * @param world the SimWorld object itself
     * @param currentWorldFile the name of the current world
     */

    public void createSaveWorldPopUp(String path, SimWorld world, String currentWorldFile)
    {
        //setup variables
        this.path = path;
        this.world = (BasicSimWorld)world;
        this.currentWorldFile=currentWorldFile.substring(0,currentWorldFile.length()-6);

        //sets the properties of the popup window
        setTitle("Save Instance of this SimWorld:");
        setSize(275,155);
        setLocation(400,400);
        //this.setResizable(false);
        
        this.setDefaultLookAndFeelDecorated(false);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        
        setVisible(true);

        Container mainPanel=getContentPane();
        mainPanel.setLayout(new BorderLayout(1,1));

        Container Panell=new Container();
        Panell.setLayout(new AbsoluteLayout());

        Container Panel5=new Container();
        Panel5.setLayout(new FlowLayout());

        this.fileL = new JLabel("File Name: ");
        fileL.setForeground(Color.lightGray);
        this.folderL = new JLabel("Folder: ");
        folderL.setForeground(Color.lightGray);
        this.typeL = new JLabel(". JAVA");
        typeL.setForeground(Color.lightGray);
       
        this.fileT = new JTextField(11);
        this.folderT = new JTextField();
        this.folderT.setEditable(false);
        this.folderT.setText(this.path);
        this.folderT.setFocusable(false);
        
        //set color
        fileT.setBackground(Color.yellow);
        fileT.setForeground(Color.darkGray);
        folderT.setBackground(Color.yellow);
        folderT.setForeground(Color.darkGray);
        
        (OK=new JButton("OK")).addActionListener(this);
        (cancel=new JButton("Cancel")).addActionListener(this);
        
        //set color
        OK.setBackground(Color.lightGray);
        OK.setForeground(Color.darkGray);
        cancel.setBackground(Color.lightGray);
        cancel.setForeground(Color.darkGray);

        Panell.add(this.fileL,new AbsoluteConstraints(10,20));
        Panell.add(this.fileT,new AbsoluteConstraints(80,17));
        Panell.add(this.typeL,new AbsoluteConstraints(210,20));
        Panell.add(this.folderL,new AbsoluteConstraints(30,57));
        Panell.add(this.folderT,new AbsoluteConstraints(80,54));

        Panel5.add("South",OK);
        Panel5.add("South",cancel);

        mainPanel.setBackground(Color.darkGray);
        mainPanel.add("North",Panell);
        mainPanel.add("South",Panel5);

        this.show();
    }
    
    /** Creates a instance of the current world and saves the world to a file. */    
    private void createInstanceWorld(){
               
        File newWorldJava = new File(path+fileName+".java");
        FileWriter out = null;
        Calendar rightNow = Calendar.getInstance();
        
        try{
            out = new FileWriter(newWorldJava);
            try{
                //start writing
                out.write("\n");
                out.write("\n //Date created: "+rightNow.getTime().toString());
                out.write("// This file was generated at you request by Intelleg.");
                out.write("\n");
                out.write("\n import simworldobjects.*;");
                out.write("\n import java.awt.*;");
                out.write("\n");
                out.write("\n public class "+fileName+" extends BasicSimWorld {");
                out.write("\n");
                out.write("\n    /** Creates a new instance of"+fileName+" */");
                out.write("\n    public "+fileName+"() {");
                out.write("\n         super("+world.getWorldDimensions()[0] + ","+world.getWorldDimensions()[1]+","+world.getWorldDimensions()[2]+ ", new Color("+world.getWorldColor().getRGB()+"));");
                out.write("\n");
                //add all the walls and ground objects to the file
                LinkedList objectList = world.getObjectList();

                for(int a=0;a<objectList.size();a++){

                SimObject sim = (SimObject)(objectList.get(a));

                    if(sim instanceof SimGround){
                        SimGround ground = (SimGround)sim;
                        out.write("\n         SimGround "+(ground.getType()+a)+"= new SimGround("+ground.getXCoord()+","+ground.getYCoord()+","+ground.getZCoord()+","+ground.getActualBearingXZ()+","+ground.getWidth()+","+ground.getLength()+", new Color("+ground.getColor().getRGB()+"));");
                        out.write("\n         addObject("+(ground.getType()+a)+");");
                        out.write("\n");
                    }
                    else if(sim instanceof SimWall){
                        SimWall wall = (SimWall)sim;
                        out.write("\n         SimWall "+(wall.getType()+a)+"= new SimWall("+wall.getXCoord()+","+wall.getYCoord()+","+wall.getZCoord()+","+wall.getActualBearingXZ()+","+wall.getWidth()+","+wall.getLength()+");");
                        out.write("\n         addObject("+(wall.getType()+a)+");");
                        out.write("\n");
                        
                    }
                    else if(sim instanceof SimRoad){
                        SimRoad road = (SimRoad)sim;
                        out.write("\n         SimRoad "+(road.getType()+a)+"= new SimRoad("+road.getXCoord()+","+road.getYCoord()+","+road.getZCoord()+","+road.getActualBearingXZ()+","+road.getWidth()+","+road.getLength()+");");
                        out.write("\n         addObject("+(road.getType()+a)+");");
                        out.write("\n");
                        
                    }
                }
                out.write("\n     }");
                out.write("\n }");
                
                //close the file
                out.close();
                
                //compile the file using the java compiler
                externalCommand("javac "+newWorldJava.getPath());
            }
            catch(Exception ea){
                MainInterface.displayMessage("Cannot write to the desired filename.");
		Intellego.addToLog("SaveWorldDialog.createInstanceWorld(): Writing to file failed: "+ea);
            }
        }
        catch(Exception e){
            MainInterface.displayMessage("Cannot recognize the entered file name");
            Intellego.addToLog("SaveWorldDialog.createInstanceWorld(): Creating file failed: "+e);
        }
    }

     /** Processes all external calls, specifically to compile the currently saved java file
      * in order to all immediate use
      * @param cmd the processed cmd
      */
    private void externalCommand(String cmd)
    {
        int len;
        byte buffer[] = new byte[1000];
	Intellego.addToLog("SaveWorldDialog.externalCommand(): Processing External Command: "+cmd);
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
             	Intellego.addToLog("SaveWorldDialog.externalCommand(): error: "+e.getMessage());
             }
	}
	catch (Exception e) 
	{
            Intellego.addToLog("SaveWorldDialog.externalCommand(): error: "+e.getMessage());
        }
    }
    
    /**  Action event handler
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e)
    {   boolean flag = false;
        if (e.getSource()==OK)
        {   
            fileName = (this.fileT.getText()).trim();
            
            //check if there are multiple spaces in the name
            for(int a=0;a<fileName.length();a++){
                char blank = ' ';
                if(fileName.charAt(a) == blank){
                    flag=true;
                    break;
                }
            }
            chooser=new JFileChooser(new File(System.getProperties().getProperty("user.dir"),"simworlds"));
	    chooser.setVisible(false);            
            
            File[] arrayF = (chooser.getCurrentDirectory().listFiles());
            String lowerCaseFile = fileName.toLowerCase();          
            
            for(int i=0;i<arrayF.length;i++){
                try{
                    String lowerCaseCurr = (arrayF[i].getName()).substring(0,(arrayF[i].getName()).indexOf('.')).toLowerCase();
                    if(lowerCaseFile.equals(lowerCaseCurr)){
                        flag = true;
                        System.out.println("Flag is TRUE!!");
                        break;
                    }
                }
                catch(Exception e2){//do nothing
                }
            }
                        
            if(flag || fileName.equals("")){   
                MainInterface.displayMessage("ERROR: invalid file name");
                this.fileT.setText("");
            }
            else{ 
                createInstanceWorld();
                this.dispose();
            }
        }
        else{this.dispose();}
    }
}