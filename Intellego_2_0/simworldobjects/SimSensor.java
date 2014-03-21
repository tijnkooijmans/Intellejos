package simworldobjects;

import interfaces.*;

/**
* Abstract class for SimSensors.
* A SimSensor is a different sort of object to a BasicSimObject. It has single
* 'owning' SimObject (normally a SimRCX) and all of its parameters are derived 
* from this object. Many of its methods therefore do nothing - they are simply
* included here to fulfill the requirements of a SimObject.
*
* @see interfaces.SimObject
*
* @author Graham Ritchie
*/
public abstract class SimSensor implements SimObject
{
	// the SimObject which 'owns' this sensor
	private SimObject object;
	
	private double xOffset, zOffset;
	private double height, width, length;
	private String type;
        private char loc;
	
	/**
	* Initialises this SimSensor
	*/
	public void initSimSensor(SimObject object, double xOffset, double zOffset, double height, double width, double length, String type, char loc)
	{
		this.object=object;
		this.xOffset=xOffset;
		this.zOffset=zOffset;
		this.height=height;
		this.width=width;
		this.length=length;
		this.type=type;
                this.loc = loc;
	}
	
	/**
	* Returns this sensor's current value
	*
	* @return the current value of this sensor as an int
	*/
	public abstract int getValue();
	
	public void setDesiredVelocity(double v){}
	public void setActualVelocity(double v){}
	public void setXCoord(double x){}
	public void setYCoord(double y){}
	public void setZCoord(double z){}
	
	public double getDesiredVelocity()
	{
		return 0.0;
	}
	
	public double getActualVelocity()
	{
		return 0.0;
	}
	
	/**
	* Returns this sensor's X coordinate - derived from its owning SimObject
	*/
	public double getXCoord()
	{
		return 	object.getXCoord()+
				(xOffset*Math.cos(Math.toRadians(object.getActualBearingXZ())))+
				(zOffset*-Math.sin(Math.toRadians(object.getActualBearingXZ())));
	}
	
	/**
	* Returns this sensor's X coordinate
	*/
	public double getYCoord()
	{
		return 0.0;
	}
	
	/**
	* Returns this sensor's Z coordinate - derived from its owning SimObject
	*/
	public double getZCoord()
	{
		return 	object.getZCoord()+
				(zOffset*Math.cos(Math.toRadians(object.getActualBearingXZ())))+
				(xOffset*Math.sin(Math.toRadians(object.getActualBearingXZ())));
	}
	
	public void setDesiredBearingVelocityXZ(double v){}
	public void setDesiredBearingVelocityXY(double v){}
	public void setActualBearingVelocityXZ(double b){}
	public void setActualBearingVelocityXY(double b){}
	public void setActualBearingXZ(double b){}
	public void setActualBearingXY(double b){}

	public double getDesiredBearingVelocityXZ()
	{
		return 0.0;
	}
	
	public double getDesiredBearingVelocityXY()
	{
		return 0.0;
	}	
	
	public double getActualBearingVelocityXZ()
	{
		return 0.0;
	}
	
	public double getActualBearingVelocityXY()
	{
		return 0.0;
	}
	
	/**
	* Returns this sensor's XZ bearing (the same as the owning SimObject's bearing)
	*/
	public double getActualBearingXZ()
	{
		return object.getActualBearingXZ();
	}
	
	public double getActualBearingXY()
	{
		return 0.0;
	}
	
	public double getHeight()
	{
		return this.height;
	}
	
	public double getWidth()
	{
		return this.width;
	}
	
	public double getLength()
	{
		return this.length;
	}

	public String getType()
	{
		return this.type;
	}
        public char getPosition(){
            return this.loc;
        }
        public SimObject getOwner(){
            return this.object;
        }
}
