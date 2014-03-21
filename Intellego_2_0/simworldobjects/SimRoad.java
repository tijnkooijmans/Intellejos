package simworldobjects;

import java.awt.*;

/**
* Models a simple wall object
*
* @author Graham Ritchie
*/
public class SimRoad extends BasicSimObject
{
    private Color color;
    /**
    * Sets up this wall
    *
    * @param x the wall's x coordinate
    * @param y the wall's y coordinate
    * @param z the wall's z coordinate
    * @param b the wall's bearing 
    * @param length the wall's length
    * @param width the wall's width
    */
    public SimRoad(double x, double y, double z, double b, double length, double width)
    {
        // initialise the SimObject
        super(0.0,length,width,"road",x,y,z,0.0,b);
        this.color = Color.green;
    }
    
    public Color getColor(){
        return this.color;
    }
    public void setColor(Color color){
        this.color = color;
    }
}
