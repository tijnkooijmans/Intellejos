package main;

import interfaces.*;
import simworldobjects.*;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.geom.*;

/**
* Provides a grid display for any SimWorld
*
* @author Simon Zienkiewicz
*/
public class GridDisplay extends JPanel
{
    /** the width of the simulation world */    
    private int width;
    /** the height of the simulation world */    
    private int height;
    /** java graphics 2d object */    
    private Graphics2D g2;
    /** the horizontal and vertical spacing between grid pixels */    
    private int gridSize=25;
    /** a buffered image of the grid */    
    private BufferedImage gridBuffer;   
    /** a graphics 2d image */    
    private Graphics2D bufferG2D;
    /** a java RescaleOp object */    
    private RescaleOp op;
    /** the simulation world */    
    private BasicSimWorld simWorld;
    /** the colour of the grid pixels */    
    private Color gridColor;
    /** the ground colour of the world */    
    private Color worldColor;
    /** if the grid is visible */    
    private boolean wantGrid;
    /** the pixel-size of each grid dot */    
    private int dotSize;
       
    /** Sets up the grid display
     * @param size the horizontal and vertical spacing between grid pixels
     * @param width the width of the simulation world
     * @param height the height of the simulation world
     * @param gridColor the colour of the grid pixels
     * @param worldColor the ground colour of the world
     * @param wantGrid if the grid is visible
     */
    public GridDisplay(int size, int width, int height, Color gridColor, Color worldColor, boolean wantGrid)
    {
        this.gridSize = size;
        this.width = width;
        this.height = height;
        this.simWorld = simWorld;
        this.gridColor = gridColor;
        this.worldColor = worldColor;
        this.wantGrid = wantGrid;
        
        
        gridBuffer = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        bufferG2D = gridBuffer.createGraphics();
        op=null;

        //draw the background/grid
        this.drawImage();        
    }
    /** Updates the size of the grid pixels
     * @param x the desired size for the grid pixels
     */    
    public void updateGridDotSize(int x){
        this.dotSize = x;
        this.drawImage();
    }
    /** Updates the horizontal and vertical spacing between grid pixels
     * @param s the new horizontal and vertical spacing between grid pixels
     */    
    public void updateGridSize(int s){
        this.gridSize = s;
        this.drawImage();
    }
    
    /** Updates the ground colour of the world
     * @param c the new world ground colour
     */    
    public void updateWorldColor(Color c){
        this.worldColor=c;
        this.drawImage();
    }
    
    /** Updates the grid colour
     * @param c the new grid colour
     */    
    public void updateGridColor(Color c){
        this.gridColor = c;
        this.drawImage();
    }
    /** Gets the current state of the grid; displayed or hidden.
     * @return true if grid is activated, false otherwise
     */    
    public boolean getWantGrid(){
        return this.wantGrid;
    }
    /** Sets the current state of the grid; displayed or hidden.
     * @param s the new status, true to activate, false to hide
     */    
    public void setWantGrid(boolean s){
        this.wantGrid = s;
        this.drawImage();
    }
        
    /** Draws the grid into a java graphics object which is then saved to a buffered
     * image for fast displaying and redrawing.
     */    
    private void drawImage(){
        //draws the background
        bufferG2D.setColor(this.worldColor);
        bufferG2D.fillRect(0,0,width,height);
        
        if(wantGrid){
            //draws the grid
            bufferG2D.setColor(this.gridColor);
            for(int a=0;a*gridSize<=width;a++){
                for(int b=0; b*gridSize<=height;b++){
                    bufferG2D.fillOval((int)(a*gridSize-this.dotSize/2),(int)(b*gridSize-this.dotSize/2), this.dotSize,this.dotSize);
                }
            }
        }
   }

    /** Main repaint method
     * @param g java graphics object
     */
    public void paintComponent(Graphics g)
    {
        // paint background
        g2 = (Graphics2D)g;
        g2.drawImage(gridBuffer,op,0,0);
    }
}
	
	