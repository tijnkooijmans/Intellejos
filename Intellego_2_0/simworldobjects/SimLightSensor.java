package simworldobjects;

import interfaces.*;
import main.*;

/**
* Simulates a light sensor
*
* @author Graham Ritchie modified by Simon Zienkiewicz
*/
public class SimLightSensor extends SimSensor
{
	private SimRCX robot;
	private double xOffset, zOffset;
        private int currentValue;
        private int previousValue;
	
	/** Sets up a light sensor on the given SimRCX, and positions it at the
         * the appropriate offset from the robot's position.
         * @param loc the location of the sensor
         * @param r the SimRCX owner of this sensor
         * @param xOffset the offest from the SimRCX's x position
         * @param zOffset the offest from the SimRCX's z position
         */
	public SimLightSensor(SimRCX r, double xOffset, double zOffset, char loc)
	{
		robot=r;
		initSimSensor(r,xOffset,zOffset,4.0,4.0,4.0,"sensorLight", loc);
		this.xOffset=xOffset;
		this.zOffset=zOffset;
                currentValue=0;
                previousValue=0;
	}
	
	/**
	* Returns the current brightness of the world at this sensor's position
	* modified by: Simon Zienkiewicz
	* @return the light level as an int
	*/
	public int getValue()
	{
		BasicSimWorld world= (BasicSimWorld)robot.getWorld();
		return LightSensorColorLibrary.getValue(world.getColorUnderLightSensor(this));
	}
	
	/**
	* Returns the offset from the owning SimRCX's x coordinate
	*
	* @return the x offset
	*/
	public double getXOffset()
	{
		return xOffset;
	}
	
	/**
	* Returns the offset from the owning SimRCX's y coordinate
	*
	* @return the y offset
	*/
	public double getZOffset()
	{
		return zOffset;
	}
        
        /** Returns the previous value read by the sensor.
         * @return the previous value of the sensor
         */        
        public int getPreviousValue(){
            return this.previousValue;
        }
        
        /** Returns the present value of the sensor.
         * @return the present value of the sensor
         */        
        public int getCurrentValue(){
            BasicSimWorld world= (BasicSimWorld)robot.getWorld();
            previousValue = currentValue;
            currentValue = LightSensorColorLibrary.getValue(world.getColorUnderLightSensor(this));
            return this.currentValue;
        }
}
