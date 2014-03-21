package main;

import interfaces.*;
import simworldobjects.*;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.geom.*;

/** Provides a simple display for any SimWorld. In general is responsible for
 * drawing and redrawing all the objects used to display the simulation.
 * @author Graham Ritchie
 * @Modifyer Simon Zienkiewicz
 */
public class SimpleDisplay extends SimDisplay
{
    private SimWorld world;
    private Graphics2D g2;
    private BasicStroke stroke;
    private Color color;
    private JTextField lcd = new JTextField();
    private java.awt.image.VolatileImage volImage;
    private Color objectColorArray[] = {Color.yellow,Color.yellow,Color.black,Color.blue,Color.gray,Color.green};
    private String shapeNames[] = {"robot","light","sensorTouch","sensorLight","wall","ground","road"};
    private SimUI simulator;

    /** Sets up this display
     * @param simulator the simulator associated with the SimDisplay
     * @param s the SimWorld to be displayed
     */

    public SimpleDisplay(SimWorld s, SimUI simulator)
    {
            // set world
            world=s;
            this.repaint();
            //stroke=new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL);
            this.add(lcd);
            lcd.setVisible(false);
            this.simulator = simulator;
            
    }
	
    /** Main repaint method, paints all the objects in the simulator
     * @param g java graphics object
     */
    public void paintComponent(Graphics g)
    {
        //System.out.println("SimpleDisply: repaint");
        // paint background
        super.paintComponent(g);
        //sets the bounds of the image to that of the size of the world
        this.setBounds(0,0,((BasicSimWorld)world).getWorldDimensions()[0], ((BasicSimWorld)world).getWorldDimensions()[2]);
        g.setColor(Color.lightGray);
        g.drawRect(0,0,((BasicSimWorld)world).getWorldDimensions()[0]-1, ((BasicSimWorld)world).getWorldDimensions()[2]-1);
        
        //if grid is off then the background will be painted by simdisplay
        if(!simulator.isGridOn())this.setBackground(world.getWorldColor());

        // cast g to a Graphics2D instance
        g2=(Graphics2D)g;

        // get the world's object list
        LinkedList list=world.getObjectList();

        // draw a shape for each object

        for (int i=0;i<list.size();i++)
        {
            // get each object in the list in turn
            SimObject o=(SimObject)list.get(i);
            
            stroke=new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL);
            
            // create a shape for this object
            Shape s=getShape(o);

            // rotate shape to correct bearing
            s=rotateShape(s,o.getActualBearingXZ(),o.getXCoord(),o.getZCoord());

            // draw shape on the screen
            if(o instanceof SimGround){              
                g2.setPaint(((SimGround)o).getOutlineColor());
                if(((SimGround)o).getSelected()) stroke=new BasicStroke(10.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
            }
            else if(o instanceof SimWall){
                g2.setPaint(((SimWall)o).getOutlineColor());
                if(((SimWall)o).getSelected()) stroke=new BasicStroke(10.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
            }
            else if(o instanceof SimRCX){
                if(((SimRCX)o).getSelected()){
                    g2.setPaint(Color.yellow);
                    g2.drawOval((int)(o.getXCoord()-70),(int)(o.getZCoord()-70),140,140);
                    g2.drawOval((int)(o.getXCoord()-65),(int)(o.getZCoord()-65),130,130);
                    g2.drawLine((int)(o.getXCoord()-45),(int)(o.getZCoord()-45), (int)(o.getXCoord()+45), (int)(o.getZCoord()+45));
                    g2.drawLine((int)(o.getXCoord()+45),(int)(o.getZCoord()-45), (int)(o.getXCoord()-45), (int)(o.getZCoord()+45));
                }
                g2.setPaint(Color.black);
            }
            else g2.setPaint(Color.black);
                
            Shape outline=stroke.createStrokedShape(s);
            g2.draw(outline);
            g2.setPaint(getColorLibrary(o.getType().toString(),o));
            g2.fill(s);

            //in order to mark a selcted ground piece
            if(o instanceof SimGround || o instanceof SimWall){
                if(o instanceof SimGround){
                  if(((SimGround)o).getSelected()) {
                      g2.setPaint(((SimGround)o).getOutlineColor());
                      Line2D.Double line1 = new Line2D.Double((int)(o.getXCoord()-o.getWidth()/2),(int)(o.getZCoord()-o.getLength()/2), (int)(o.getXCoord()+o.getWidth()/2), (int)(o.getZCoord()+o.getLength()/2));
                      Line2D.Double line2 = new Line2D.Double((int)(o.getXCoord()+o.getWidth()/2), (int)(o.getZCoord()-o.getLength()/2),(int)(o.getXCoord()-o.getWidth()/2), (int)(o.getZCoord()+o.getLength()/2));
                      g2.draw(rotateShape(line1, o.getActualBearingXZ(),o.getXCoord(),o.getZCoord()));
                      g2.draw(rotateShape(line2, o.getActualBearingXZ(),o.getXCoord(),o.getZCoord()));
                  }
                }
                //in order to mark a selected wall piece
                else if(o instanceof SimWall){
                  if(((SimWall)o).getSelected()){
                      g2.setPaint(((SimWall)o).getOutlineColor());
                      Line2D.Double line1 = new Line2D.Double((int)(o.getXCoord()-o.getWidth()/2),(int)(o.getZCoord()-o.getLength()/2), (int)(o.getXCoord()+o.getWidth()/2), (int)(o.getZCoord()+o.getLength()/2));
                      Line2D.Double line2 = new Line2D.Double((int)(o.getXCoord()+o.getWidth()/2), (int)(o.getZCoord()-o.getLength()/2),(int)(o.getXCoord()-o.getWidth()/2), (int)(o.getZCoord()+o.getLength()/2));
                      g2.draw(rotateShape(line1, o.getActualBearingXZ(),o.getXCoord(),o.getZCoord()));
                      g2.draw(rotateShape(line2, o.getActualBearingXZ(),o.getXCoord(),o.getZCoord()));
                  }
                  
                }
                 
            }

            //for the LCD purposes
            if(o instanceof SimRCX){

                createLCDDisplay((o.getXCoord()-(o.getWidth()/2)), (o.getZCoord()-(o.getLength()/2)));
                //write the LCD output
                volImage.createGraphics().drawString(((SimRCX)o).getLCDOutput(),3,16);

                java.awt.image.BufferedImage buffImage = volImage.getSnapshot();
                double theta=Math.toRadians(o.getActualBearingXZ());

                // create a new affine transform rotator
                AffineTransform  atx = AffineTransform.getRotateInstance(theta,o.getXCoord()-lcd.getLocation().getX(),o.getZCoord()-lcd.getLocation().getY()); 

                java.awt.image.AffineTransformOp transformSetup = new AffineTransformOp(atx,AffineTransformOp.TYPE_BILINEAR);

                g2.drawImage(buffImage,transformSetup,(int)(o.getXCoord()-(o.getWidth()/2))+1, (int)(o.getZCoord()-(o.getLength()/2))+10);

            }
        }
    }
	
    /** Deligates an appropriate color for a specified object in the simulations.
     * @param type the string name of the SimObject
     * @param o the SimObject requiring repaint
     * @return the corresponding Color
     */
    private Color getColorLibrary(String type, SimObject o){
        int index=1;

        if(type.equals(shapeNames[0]))index = 0;
        else if(type.equals(shapeNames[1])) index=1;
        else if(type.equals(shapeNames[2])) index=2;
        else if(type.equals(shapeNames[3])) index=3;
        else if(type.equals(shapeNames[4])) index=4;
        else if(type.equals(shapeNames[5])) return((SimGround)o).getColor();
        else if(type.equals(shapeNames[6])) index=5;

        return this.objectColorArray[index];
    }

    /** Sets the color library, sets a color for a specific shape.
     * @param type shape type
     * @param color the desired color for that shape
     */    
    protected void setColorLibrary(String type, Color color){
        int index = 1;

        if(type.equals(shapeNames[0]))index = 0;
        else if(type.equals(shapeNames[1])) index=1;
        else if(type.equals(shapeNames[2])) index=2;
        else if(type.equals(shapeNames[3])) index=3;
        else if(type.equals(shapeNames[4])) index=4;
        else if(type.equals(shapeNames[6])) index=5;
        
        this.objectColorArray[index]=color;
        this.repaint();
    }

    /**
    * Checks if there is an associated shape for a SimObject. If so it is 
    * returned. If not then a standard shape of the SimObject's size is returned.
    *
    * @param o the SimObject
    * @return the appropriate java.awt.Shape
    */
    public Shape getShape(SimObject o)
    {
            // create a new Shape
            Shape s;

            // check the SimObject's type
            if(o.getType().equalsIgnoreCase("robot"))
            {
                    // SimObject is a robot
                    s=createRobotShape(o.getXCoord(),o.getZCoord(),o.getWidth(),o.getLength());
            }
            else if(o.getType().equalsIgnoreCase("light"))
            {
                    // SimObject is a light
                    s=createLightShape(o.getXCoord(),o.getZCoord(),o.getWidth(),o.getLength());
            }
            else if(o.getType().equalsIgnoreCase("sensorTouch")||o.getType().equalsIgnoreCase("sensorLight"))
            {
                    // SimObject is a sensor
                    s=createSensorShape(o.getXCoord(),o.getZCoord(),o.getWidth(),o.getLength());
            }
            else if (o.getType().equalsIgnoreCase("wall")||o.getType().equalsIgnoreCase("ground")||o.getType().equalsIgnoreCase("road"))
            {
                    //SimObject is a wall or ground
                    s=createWallShape(o.getXCoord(),o.getZCoord(),o.getWidth(),o.getLength());
            }
            else
            {
                    // unknown SimObject, so create standard shape
                    s=createStandardShape(o.getXCoord(),o.getZCoord(),o.getWidth(),o.getLength());
            }

            // return the shape
            return s;
    }

    /**
    * Rotates a java.awt.Shape around a certain point a certain angle 
    *
    * @param shape the shape to be rotated
    * @param angle the angle by which the shape is to be rotated
    * @param X the x co-ordinate about which to rotate
    * @param Z the z co-ordinate about which to rotate
    * @return the rotated shape, as a new java.awt.Shape
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

    /**
    * Creates a robot shape
    *
    * @param x the x coordinate of the robot
    * @param z the z coordinate of the robot
    * @param width the width of the robot
    * @param length the length of the robot
    *
    * @return the robot shape
    */
    private Shape createRobotShape(double x, double z, double width, double length)
    {
        // size of the 'wheels'
        double a=10.0;
        double b=20.0;

        // establish top left corner of robot
        double X=(x-(width/2));
        double Z=(z-(length/2));

        // make up the shape
        Rectangle2D.Double body=new Rectangle2D.Double(X,Z,width,length);

        Rectangle2D.Double wheel1=new Rectangle2D.Double(X-a-3,Z+5,a,b);
        Rectangle2D.Double wheel2=new Rectangle2D.Double(X+width+3,Z+5,a,b);
        Rectangle2D.Double wheel3=new Rectangle2D.Double(X-a+7+(width/2),Z+63,6,b/2);
               
        Area shape=new Area(body);

        shape.add(new Area(wheel1));
        shape.add(new Area(wheel2));
        shape.add(new Area(wheel3));

        // return the complete shape
        return (Shape) shape;
    }

    /** Creates an LCD display on the RCX Brick
     * @param x the X position of the LCD display
     * @param z the Z position of the LCD display
     */
    private void createLCDDisplay(double x, double z){
        lcd.setBounds((int)(x+1),(int)(z+10), 38, 20);
        volImage=lcd.createVolatileImage(38,20);
        volImage.createGraphics().setColor(Color.black);
        volImage.createGraphics().drawRect(1,1,36,17);

    }

    /** Creates a light shape of the appropriate dimesion.
     * @param x the X position
     * @param z the Z position
     * @param width the width
     * @param length the length
     * @return an elipse shape with the set parameters
     */
    private Shape createLightShape(double x, double z, double width, double length)
    {
        // establish top left corner of object
        double X=(x-(width/2));
        double Z=(z-(length/2));

        return new Ellipse2D.Double(X,Z,width,length);
    }

    /** Creates a sensor shape of the appropriate dimension.
     * @param x the X position
     * @param z the Z position
     * @param width the width
     * @param length the length
     * @return a rectangle shape with the set parameters
     */
    private Shape createSensorShape(double x, double z, double width, double length)
    {
        // establish top left corner of object
        double X=(x-(width/2));
        double Z=(z-(length/2));

        return new Rectangle2D.Double(X,Z,width,length);
    }

    /** Creates a wall shape of the appropriate dimension
     * @param x the X position
     * @param z the Z position
     * @param width the width
     * @param length the length
     * @return a rectangle shape with the set parameters
     */
    private Shape createWallShape(double x, double z, double width, double length)
    {
        // establish top left corner of object
        double X=(x-(width/2));
        double Z=(z-(length/2));

        return new Rectangle2D.Double(X,Z,width,length);
    }

    /** Creates a standard shape, currently a rectangle
     * @param x the X position
     * @param z the Z position
     * @param width the width
     * @param length the length
     * @return a rectangle shape with the set parameters
     */
    private Shape createStandardShape(double x, double z, double width, double length)
    {
        // establish top left corner of object
        double X=(x-(width/2));
        double Z=(z-(length/2));

        return new Rectangle2D.Double(x,z,width,length);
    }
}
