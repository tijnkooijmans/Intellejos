package interfaces;

import java.util.*;
import java.awt.*;

/**
* Interface class for all SimWorlds.
*
* @author Graham Ritchie
*/
public interface SimWorld
{
	/**
	* Performs one update loop
	*/
	public void tick();
	
	/**
	* Returns the number of 'ticks' since this world was started
	*
	* @return the number of ticks as a long
	*/
	public long getTime();

	/** Returns the light level at the specified co-ordinate.
         * @return the brightness this will always be an int between 0 and 100
         * @param x the x coordinate of position searched
         * @param y the y coordinate of position searched
         * @param z the z coordinate of position searched
         */
	public int getBrightness(double x, double y, double z);

	/** Checks whether there is an obstacle in the specified co-ordinate
         * @return true or false accordingly
         * @param x the x coordinate of position searched
         * @param y the y coordinate of position searched
         * @param z the z coordinate of position searched
         */
	public boolean hasObstacle(double x, double y, double z);

	/**
	* Returns this SimWorld's object list
	*
	* @return the object list as a LinkedList
	*/
	public LinkedList getObjectList();
	
	/**
	* Adds an object to this SimWorld
	*
	* @param o the SimObject to be added
	*/
	public void addObject(SimObject o);
        
        /** Returns the background color of this SimWorld
         * Created by: Simon Zienkiewicz
         * @return Colour the background colour of the current world
         */
	public Color getWorldColor();
}
