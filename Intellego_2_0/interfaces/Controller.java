package interfaces;

/**
* Interface class for all controllers.
*
* All Controllers must extend this abstract class, and should obey the commands
* specified or system behaviour is undefined. e.g. a call to halt() *must* stop 
* this controller's thread as quickly as possible and allow run() to return.
*
* @author Graham Ritchie, modified by: Simon Zienkiewicz
*/
public abstract class Controller extends Thread
{

    /** Constant for sensor of type light.
     * Sensor type constants (see getSensors() for explanation)
     */
    public static int SENSOR_TYPE_LIGHT=1;
    /** Constant for sensor of type touch.
     * Sensor type constants (see getSensors() for explanation)
     */    
    public static int SENSOR_TYPE_TOUCH=2;

    /**
    * Initialises controller. It should be noted that this method will only
    * ever be called once, whereas run() can be called many times, so any
    * variables or data structures etc. that are meant to persist in between
    * stops and starts of the controller's thread, or that should only be 
    * initialised once should be set up from within this method, not in run().
    *
    * @param r the AbstractRobot associated with this controller
    */
    public abstract void initController(AbstractRobot r);
    
    /**
    * Returns the AbstractRobot associated with this controller
    *
    * @return the AbstractRobot
    */
    public abstract AbstractRobot getRobot();

    /**
    * Returns an array of the sensors used by this controller, and the type
    * of sensor required. This type must be one of the sensor type constants
    * declared above. The index of the array is used to establish which sensor
    * is being defined. e.g. if array[0] is SENSOR_TYPE_TOUCH then sensor 1 
    * will be set to a touch sensor. The sensors array *must* be initialised
    * with the correct values from the outset, and must not be set in 
    * initController(), or any other method. (see example Controllers for 
    * working examples)
    *
    * @return the sensor array
    */
    public abstract int[] getSensors();
    
    /**
    * Stops this controller's thread running, i.e. must allow run() to return
    * as quickly as possible. However it is acceptable for this method to do 
    * some housekeeping before stopping the controller, e.g. save some internal
    * data strcuture to a file.
    */
    public abstract void halt();

    /** This method is called whenever a LIGHT SENSOR detects change from its previous value.
     * @param sensorNumber this is the LIGHT SENSOR number that called this method
     */
    public abstract void lightSensorListener(int sensorNumber);

    /** This method is called whenever a TOUCH SENSOR detects change from its previous value.
     * @param sensorNumber this is the TOUCH SENSOR number that called this method
     */
    public abstract void touchSensorListener(int sensorNumber);
    
    /**
    * Starts this controller's thread running. The 'real' controller 
    * functionality should be started from here.
    */
    public abstract void run();
    
    /** Whenever a timer elapses this method is called and the timer which
     * elapsed is passed as a parameter
     * @param elapsedTimer the timer which elapsed
     */
    public abstract void setTimerExecution(int elapsedTimer);
    
}
