package simworldobjects;

import interfaces.*;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

/**
* Abstract class for SimWorlds, provides a base implementation of 
* the SimWorld interface which more complex worlds can extend. (e.g
* by adding lights, walls etc.)
*
* @see interfaces.SimWorld
*
* @author Graham Ritchie modified by: Simon Zienkiewicz
*/
public abstract class BasicSimWorld implements SimWorld
{
	private int ticks;
	private int ambientLight;
	private LinkedList objectList;
	private long width, length, height;
        private Color worldColor=Color.white;
	
	/**
	* Sets up the basic sim world
	*
	* @param x the largest x cooridnate in this world 
	* @param y the largest y cooridnate in this world 
	* @param z the largest z cooridnate in this world 
	*/
	public BasicSimWorld(long x, long y, long z)
	{	
		// initialise world size 
		width=x;
		height=y;
		length=z;
		
		// initialise tick counter
		ticks=0;
		
		// initialise object list
		objectList=new LinkedList();
		
		// set ambient light
		ambientLight=20;
                
                //setup walls around the world
                buildWallBoundaries();
	}
        /**
	* Sets up the basic sim world
        * Created by: Simon Zienkiewicz
	*
	* @param x the largest x cooridnate in this world 
	* @param y the largest y cooridnate in this world 
	* @param z the largest z cooridnate in this world 
        * @param c the Color of the ground of the world
	*/
	public BasicSimWorld(long x, long y, long z, Color c)
	{	
		// initialise world size 
		width=x;
		height=y;
		length=z;
                
                //initialise world color
                worldColor=c;
		
		// initialise tick counter
		ticks=0;
		
		// initialise object list
		objectList=new LinkedList();
		
		// set ambient light
		ambientLight=20;
                
	}
        	
        /**
	* Builds walls around the desired world
	*/
        private void buildWallBoundaries(){
            // add a few containing walls
            //top boundary
            SimWall wall1=new SimWall(this.length/2,0.0,6.0,0.0,this.length-2,10.0);
            addObject(wall1);

            //left
            SimWall wall2=new SimWall(6.0,0.0,this.width/2,0.0,10.0,this.length-2);
            //SimWall wall2=new SimWall(6.0,0.0,this.width/2,90.0,this.length-2,10.0);
            addObject(wall2);
            
            
            //left
            SimWall wall3=new SimWall(this.length-6.0,0.0,this.width/2,0.0,10.0,this.length-2);
            //SimWall wall3=new SimWall(this.length-6.0,0.0,this.width/2,90.0,this.length-2,10.0);
            addObject(wall3);
            
            //left
            SimWall wall4=new SimWall(this.length/2,0.0,this.width-6.0,0.0,this.length-2,10.0);
            addObject(wall4);
        
        }
        
	/**
	* Performs one update loop
	*/
	public void tick()
	{
		updateObjects();
		ticks++;
	}
	
	/**
	* Returns the number of 'ticks' since this world was started
	*
	* @return the number of ticks as a long
	*/
	public long getTime()
	{
		return ticks;
	}
	
	/**
	* Returns the light level at the specified co-ordinate. 
	*
	* @return the brightness, this will always be an int between 0 and 100
	*/
	public int getBrightness(double x, double y, double z)
	{
		double totalBrightness=0;
		
		// check through the object list for SimLights
		for(int i=0;i<objectList.size();i++)
		{
			SimObject o=(SimObject)objectList.get(i);
			
			if(o instanceof SimLight)
			{
				SimLight light=(SimLight)o;
				
				int X=Math.abs((int)(light.getXCoord()-x));
				int Z=Math.abs((int)(light.getZCoord()-z));
				
				// work out distance from point to light with pythagoras
				double distance=Math.sqrt((X*X)+(Z*Z));
				
				// work out the reduction in light coefficient
				double coeff=(((double)light.getBrightness()-(distance/8))/(double)light.getBrightness());
				
				// establish the brightness of this light at the point
				double brightness=light.getBrightness()*coeff;
				
				// add this light's brightness to the total brightness level
				totalBrightness+=brightness;
			}
		}
		
		// make sure light never falls below ambient level
		if(totalBrightness < ambientLight)
		{
			totalBrightness=ambientLight;
		}
		
		// make sure light never goes above 100
		if(totalBrightness > 100)
		{
			totalBrightness=100;
		}
		
		// add some  random 'noise' to the light
		Random r=new Random(System.currentTimeMillis());
		totalBrightness=totalBrightness+(2*r.nextFloat());
				
		// return total brightness at this point as an int
		return (int) totalBrightness;
	}
	
	/**
	* Checks whether there is an obstacle in the specified co-ordinate
	*
	* @param x the x coordinate
	* @param y the y coordinate
	* @param z the z coordinate
	*
	* @return true or false accordingly
	*/
	public boolean hasObstacle(double x, double y, double z)
	{
		// examine each SimObject in turn
		for (int i=0;i<objectList.size();i++)
		{
			SimObject o=(SimObject)objectList.get(i);
			
			// create a shape the same size and bearing as o
			Shape s=createShape(o.getXCoord(),o.getZCoord(),o.getWidth(),o.getLength(),o.getActualBearingXZ());
			
			// if o's shape contains the coords return true (ignoring sensors)
			if(s.contains(x,z) && !(o instanceof SimSensor) && !(o instanceof SimGround) && !(o instanceof SimRoad))
			{
				// has obstacle
				return true;
			}
		}
		// if we've got here then there is no obstacle, so return false
		return false;
	}
	
	/**
	* Rotates a Shape around a certain point a certain angle 
	*
	* @param shape the shape to be rotated
	* @param angle the angle by which the shape is to be rotated
	* @param X the x co-ordinate about which to rotate
	* @param Z the z co-ordinate about which to rotate
	*
	* @return the rotated shape, as a new Shape
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
	* Creates a rectangle with the given properties
	*
	* @param x the x coordinate of the rectangle 
	* @param z the z coordinate of the rectangle 
	* @param width the width of the rectangle 
	* @param width the length of the rectangle 
	* @param angle the bearing of the rectangle 
	*
	* @return the rectangle
	*/
	private Shape createShape(double x, double z, double width, double length, double angle)
	{
		// establish top left corner of object
		double X=(x-(width/2));
		double Z=(z-(length/2));
		
		// create shape
		Shape s=new Rectangle2D.Double(X,Z,width,length);
		
		// rotate it to the correct bearing
		s=rotateShape(s,angle,x,z);
		
		return s;
	}
	
	/**
	* Checks if SimObject o is colliding with SimObject p.
	*
	* @param o the first SimObject
	* @param p the second SimObject
	*
	* @return true or false accordingly
	*/
	public boolean colliding(SimObject o, SimObject p)
	{
		// create a rectangle the same size and bearing as o
		Shape shapeO=createShape(o.getXCoord(),o.getZCoord(),o.getWidth(),o.getLength(),o.getActualBearingXZ());
		
		// create a rectangle the same size and bearing as p
		Shape shapeP=createShape(p.getXCoord(),p.getZCoord(),p.getWidth(),p.getLength(),p.getActualBearingXZ());
		
		// 'cast' shapeP to a Rectangle2D so intersects() will work
		Rectangle2D rectP=shapeP.getBounds2D();
		
		// check if o's shape intersects with p's shape
		if (shapeO.intersects(rectP))
		{
			// objects are colliding
			return true;
		}
		else
		{
			// objects aren't colliding
			return false;
		}
	}
	
	/**
	* Adds an object to the back of this SimWorld
	*
	* @param o the SimObject to be added
	*/
	public void addObject(SimObject s)
	{
		// add this object to the back of the list list
		objectList.add(s);
	}
        
        /**
	* Adds an object to the front of any Robot object/part in the list
        * Added by: Simon Zienkiewicz
	*
	* @param o the SimObject to be added
	*/
	public void addObjecttoFront(SimObject s)
	{
	    int b=this.objectList.size();
            for(int i=0;i<this.objectList.size();i++){
                SimObject sim = (SimObject)objectList.get(i);
                if(sim instanceof SimSensor || sim instanceof SimRCX){
                    b=i;
                    break;
                }
            }
            this.objectList.add(b,s);
        }
        
	/**
	* Returns this SimWorld's object list
	*
	* @return the object list as a LinkedList
	*/
	public LinkedList getObjectList()
	{
		return objectList;
	}
	
	/**
	* Updates all the SimObjects in this world by one step
	*/
	public void updateObjects()
	{
            // flag to determine whether any object has collided with any other
            boolean collided;

            for (int i=0;i<objectList.size();i++)
            {
                // examine each object in turn
                SimObject o=(SimObject)objectList.get(i);

                // reset collided flag
                collided=false;

                // check for collisions:

                // ignore sensors and ground/paths
                if(o instanceof SimRCX || o instanceof SimLightSensor)
                {
                    // see if it is colliding with any other object in the world
                    for (int j=0;j<objectList.size();j++)
                    {
                        // examine each object in turn
                        SimObject p=(SimObject)objectList.get(j);

                        // ignore sensors and ground/paths
                        if(p instanceof SimWall)
                        {
                            // ignore comparisons with self (no other object should ever have the same
                            // x and z coords (if this works!)
                            if(!(o.getXCoord()==p.getXCoord() && o.getZCoord()==p.getZCoord()))
                            {
                                if(colliding(o,p))
                                {	
                                        //System.out.println("collision: "+p.getType()+" with "+o.getType());
                                        collided=true;
                                }
                            }
                        }
                    }
                }

                if(!collided)
                {
                    // determine what the object wants to do, and do it

                    if(o.getDesiredVelocity()>0)
                    {
                        moveForward(o);
                    }
                    else if(o.getDesiredVelocity()<0)
                    {
                        moveBackward(o);
                    }

                    if(o.getDesiredBearingVelocityXZ()>0)
                    {
                        moveRight(o);
                    }
                    else if(o.getDesiredBearingVelocityXZ()<0)
                    {
                        moveLeft(o);
                    }
                }
                else // objects are currently collided so ...
                {
                    if(o instanceof SimRCX){
                        SimRCX robot = (SimRCX)o;
                        robot.stopMoving();
                    
                    }
                    else if(o instanceof SimLightSensor){
                        SimRCX robot = (SimRCX)((SimSensor)o).getOwner();
                        robot.stopMoving();
                    }
                }
                //check for light or touch events:
                if(o instanceof SimSensor){
                    SimSensor sensor = (SimSensor)o;
                    int index=0;
                    
                    switch(sensor.getPosition()){
                        case 'L':   index=1; break;
                        case 'F':   index=2; break;
                        case 'R':   index=3; break;
                    }
                    
                    //check for touch events
                    if(o instanceof SimTouchSensor){
                        SimTouchSensor touch = (SimTouchSensor)o;
                        if(touch.getCurrentValue() != touch.getPreviousValue()){
                            ((SimRCX)touch.getOwner()).touchEvent(index);
                        }
                    }

                    //check for light events
                    else if(o instanceof SimLightSensor){
                        
                        SimLightSensor light = (SimLightSensor)o;
                        
                        if(light.getCurrentValue() != light.getPreviousValue()){
                            ((SimRCX)light.getOwner()).lightEvent(index);
                        }
                    }
                }
                
            }
               
        }	
	
	/**
	* Moves the object forward by the desired velocity according to its current bearing
	* Modified by: Simon Zienkiewicz
        *
	* @param o the SimObject to be moved
	*/
	private void moveForward(SimObject o)
	{  
            if(o.getXCoord() >= 0 && o.getZCoord() >=0 && o.getXCoord() <= this.getWorldDimensions()[0] && o.getZCoord() <= this.getWorldDimensions()[2]){ 
                o.setActualVelocity(o.getDesiredVelocity());
                o.setXCoord(o.getXCoord()+ o.getDesiredVelocity()*(Math.sin(Math.toRadians(o.getActualBearingXZ()))));
                o.setZCoord(o.getZCoord() - o.getDesiredVelocity()*(Math.cos(Math.toRadians(o.getActualBearingXZ()))));
            }
	}
	
	/**
	* Moves the object backward one step according to its current bearing
	*
	* @param o the SimObject to be moved
	*/
	private void moveBackward(SimObject o)
	{   
            if(o.getXCoord() >= 0 && o.getZCoord() >=0 && o.getXCoord() <= this.getWorldDimensions()[0] && o.getZCoord() <= this.getWorldDimensions()[2]){ 
                o.setActualVelocity(o.getDesiredVelocity());
                o.setXCoord(o.getXCoord() + o.getDesiredVelocity()*(Math.sin(Math.toRadians(o.getActualBearingXZ()))));
                o.setZCoord(o.getZCoord() - o.getDesiredVelocity()*(Math.cos(Math.toRadians(o.getActualBearingXZ()))));
            }
	}
	
	/**
	* Turns the object one step right (clockwise)
	*
	* @param o the SimObject to be moved
	*/
	private void moveRight(SimObject o)
	{
            o.setActualBearingVelocityXZ(o.getDesiredBearingVelocityXY());
            o.setActualBearingXZ((o.getActualBearingXZ()+o.getDesiredBearingVelocityXZ()));
            
            if(o instanceof SimRCX){
                //angle is updated
                ((SimRCX)o).pivotRotation();
            }
	}
	
	/**
	* Turns the object one step left (anticlockwise)
	*
	* @param o the SimObject to be moved
	*/
	private void moveLeft(SimObject o)
	{
            if(o instanceof SimWall)
            o.setActualBearingVelocityXZ(o.getDesiredBearingVelocityXY());
            o.setActualBearingXZ((o.getActualBearingXZ()+o.getDesiredBearingVelocityXZ()));
            
            if(o instanceof SimRCX){
                //angle is updated
                ((SimRCX)o).pivotRotation();
            }
	}
        
        /**
	* Gets the color under the position of the light sensor
	*
	* @param lightSensor the light sensor object
	*/
        public Color getColorUnderLightSensor(SimLightSensor lightSensor){
           // check through the object list for SimLights
            
            int b=9999;
            for(int i=0;i<objectList.size();i++)
            {
                SimObject o=(SimObject)objectList.get(i);

                if(o instanceof SimGround)
                {
                    SimGround ground=(SimGround)o;
                    //check if the light sensor and the ground are colliding
                    if(this.colliding(lightSensor, ground))b=i;
                }
                if(o instanceof SimRoad)
                {
                    SimRoad road=(SimRoad)o;
                    //check if the light sensor and the ground are colliding
                    if(this.colliding(lightSensor, road))b=i;
                }
            }
            //find the most upper ground object
            if(b!=9999) {
                SimObject o=(SimObject)objectList.get(b);
                if(o instanceof SimGround) return ((SimGround)(objectList.get(b))).getColor();
                else return ((SimRoad)(objectList.get(b))).getColor();              
            }
            else return worldColor;
        }
        
        /**
	* Gets the ground color of the world
	* Created by: Simon Zienkiewicz
        *
	* @param lightSensor the light sensor object
	*/
        public Color getWorldColor(){
            return worldColor;
        }
         
        /**
	* Sets the ground color of the world
	* Created by: Simon Zienkiewicz
        *
	* @param lightSensor the light sensor object
	*/
        public void setWorldColor(Color color){
            worldColor = color;
        }
        
        /**
	* Gets the dimensions of the world
	* Created by: Simon Zienkiewicz
        *
	* @return the dimensions of the world
	*/
        public int[] getWorldDimensions(){
            int[] dimensions = {(int)this.width,(int)this.height,(int)this.length};
            return dimensions;
        }
        
        public void updateObjectList(LinkedList newList){
            objectList=newList;
        }
}