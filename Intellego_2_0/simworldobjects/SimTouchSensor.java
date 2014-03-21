package simworldobjects;

import interfaces.*;

/**
* Simulates a light sensor
*
* @author Graham Ritchie
*/
public class SimTouchSensor extends SimSensor
{
	private SimRCX robot;
	private double xOffset, zOffset;
        private boolean currentValue;
        private boolean previousValue;
	
	/**
	* Sets up a light sensor on the given SimRCX, and positions it at the
	* the appropriate offset from the robot's position.
	*
	* @param r the SimRCX owner of this sensor
	* @param xOffset the offest from the SimRCX's x position
	* @param zOffset the offest from the SimRCX's z position
	*/
	public SimTouchSensor(SimRCX r, double xOffset, double zOffset, char loc)
	{
		robot=r;
		initSimSensor(r,xOffset,zOffset,10.0,10.0,10.0,"sensorTouch",loc);
		this.xOffset=xOffset;
		this.zOffset=zOffset;
                this.currentValue = false;
                this.previousValue = false;
	}
	
	/**
	* Returns the current value of this sensor, a 1 if it is touching something
	* or 0 if it is not.
	*
	* @return 1 or 0 accordingly
	*/
	public int getValue()
	{
		SimWorld world=robot.getWorld();
		
		if (world.hasObstacle(this.getXCoord(),this.getYCoord(),this.getZCoord()))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
        
        /**
	* Returns the current boolean value of this sensor, a 'true' if it is touching something
	* or 'false' if it is not.
	*
	* @return 'true' or 'false' accordingly
	*/
        public boolean getBooleanValue(){
            BasicSimWorld world= (BasicSimWorld)robot.getWorld();
           
            return world.hasObstacle(this.getXCoord(),this.getYCoord(),this.getZCoord());
           
        }
	
	public double getXOffset()
	{
		return xOffset;
	}
	
	public double getZOffset()
	{
		return zOffset;
	}
        
        public boolean getPreviousValue(){
            return this.previousValue;
        }
        
        public boolean getCurrentValue(){
            BasicSimWorld world= (BasicSimWorld)robot.getWorld();
            previousValue = currentValue;
            currentValue = world.hasObstacle(this.getXCoord(),this.getYCoord(),this.getZCoord());
            return this.currentValue;
        }
}
