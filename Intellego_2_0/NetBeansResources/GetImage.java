/*
 * FormBackground.java
 *
 * Created on November 13, 2002, 7:05 PM
 */

package NetBeansResources;

import javax.swing.JPanel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

/** Gets the intro scene to the simulator */
public class GetImage extends JPanel 
{
    /** Stores the image. */    
    private BufferedImage map;
    /** Stores the rescale operation. */    
    private RescaleOp op;
    
    /** Creates an image from a picture */    
    public GetImage() 
    {
        try{ map= ImageIO.read(new File("images\\intro.gif"));}
        catch(Exception e){}
        
        op =null;
    }
    
    /** Creates an image from a picture
     * @param name the file name and path of the picture
     */    
    public GetImage(String name){
        try{ map= ImageIO.read(new File(name));}
        catch(Exception e){}
        
        op =null;
    }
    
    /** Paints the image
     * @param g java graphics object.
     */    
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(map, op, 0,0);
    }
}

