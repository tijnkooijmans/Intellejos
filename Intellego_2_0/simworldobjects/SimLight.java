package simworldobjects;

/**
* Models a simple light source
*
* @author Graham Ritchie
*/
public class SimLight extends BasicSimObject
{
	// central brightness of this light
	private int brightness;
	
	/**
	* Sets up this light
	*
	* @param brightness the intensity of the light - must be between 1 and 100
	* @param x the light's x coordinate
	* @param y the light's y coordinate
	* @param z the light's z coordinate
	*/
	public SimLight(int brightness, double x, double y, double z)
	{
		// initialise the SimObject
		super(30.0,30.0,30.0,"light",x,y,z,0.0,0.0);
		
		this.brightness=brightness;
	}

	/**
	* Returns the brightness of this light, this will always be a value between 0 and 100
	*
	* @return the brightness
	*/
	public int getBrightness()
	{
		return brightness;
	}
}
