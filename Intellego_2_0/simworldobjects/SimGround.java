package simworldobjects;

import java.awt.*;
/**
* Models a simple ground object
*
* @author Simon Zienkiewicz
*/
public class SimGround extends BasicSimObject
{
        private Color color;
        private Color outlineColor= Color.black;
        private boolean isSelected = false;
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
	public SimGround(double x, double y, double z, double b, double length, double width, Color c)
	{
		// initialise the SimObject
		super(0.0,length,width,"ground",x,y,z,0.0,b);
                this.color = c;
	}
        
        public Color getColor(){
            return this.color;
        }
        public void setColor(Color color){
            this.color = color;
        }
        public Color getOutlineColor(){
            return this.outlineColor;
        }
        public void setOutlineColor(Color color){
            this.outlineColor = color;
        }
        public boolean getSelected(){
            return isSelected;
        }
      
        public void setSelected(boolean a){
            isSelected = a;
        }
}
