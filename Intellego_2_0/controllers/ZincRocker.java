import interfaces.Controller;
import interfaces.AbstractRobot;


/**
* This sample program rocks the robot, meaning it alternates between moving the
* robot forwards and backwards at the execution of a timer. Also the light sensor
* reading of LEFT SENSOR or S1 is displayed on the LCD whenever a light change is
* detected.
*
* @author  Simon Zienkiewicz
*/
public class ZincRocker extends Controller 
{
    
    private AbstractRobot zincBot;
    private boolean running;
    private int[] sensors={Controller.SENSOR_TYPE_LIGHT,Controller.SENSOR_TYPE_TOUCH,Controller.SENSOR_TYPE_LIGHT};
    private boolean on = false;
    int timer1;
    boolean var = false;
    
    /** Creates a new instance of ZincRocker */
    public ZincRocker()
    {
    }
    
    /** Initialises controller. It should be noted that this method will only
    * ever be called once, whereas run() can be called many times, so any
    * variables or data structures etc. that are meant to persist in between
    * stops and starts of the controller's thread, or that should only be
    * initialised once should be set up from within this method, not in run().
    *
    * @param r the AbstractRobot associated with this controller
    *
    */
    public void initController(AbstractRobot r) 
    {
        zincBot = r;
        
        //creates a timer with a delay of 1000 milli seconds
        timer1 = zincBot.createTimer(1000);
    }
    
    /** Returns the AbstractRobot associated with this controller
    *
    * @return the AbstractRobot
    *
    */
    public AbstractRobot getRobot() 
    {
        return zincBot;
    }
    
    /** Returns an array of the sensors used by this controller, and the type
    * of sensor required. This type must be one of the sensor type constants
    * declared above. The index of the array is used to establish which sensor
    * is being defined. e.g. if array[0] is SENSOR_TYPE_TOUCH then sensor 1
    * will be set to a touch sensor. The sensors array *must* be initialised
    * with the correct values from the outset, and must not be set in
    * initController(), or any other method. (see example Controllers for
    * working examples)
    *
    * @return the sensor array
    *
    */
    public int[] getSensors() 
    {
        return sensors;
    }
    
    /** Stops this controller's thread running, i.e. must allow run() to return
    * as quickly as possible. However it is acceptable for this method to do
    * some housekeeping before stopping the controller, e.g. save some internal
    * data structure to a file.
    *
    */
    public void halt() 
    {
        running = false;
    }
    
    /** This method is called whenever a LIGHT SENSOR detects change from its previous
    * value.
    *
    * @param sensorNumber, this is the LIGHT SENSOR number that called this method
    */
    public void lightSensorListener(int sensorNumber)
    {
        if(on){
            
            //
            //when LIGHT SENSOR, S1 detects a change this is called
            //
            if(sensorNumber == AbstractRobot.S1)
            {
                
                //display the colour reading of the sensor on the LCD
                zincBot.displayNumberLCD(zincBot.getSensor1());
                
            }
            
            //
            //when LIGHT SENSOR, S3 detects a change this is called
            //
            else if(sensorNumber == AbstractRobot.S3)
            {
            
            }
        
        }
    }
    
    /** This method is called whenever a TOUCH SENSOR detects change from its previous
    * value.
    *
    * @param sensorNumber, this is the TOUCH SENSOR number that called this method
    */
    public void touchSensorListener(int sensorNumber)
    {
        if(on)
        {
            //
            //when TOUCH SENSOR, S2 detects a change this is called
            //
        }
      
    }
    
    /**
    * Starts this controller's thread running. The 'real' controller 
    * functionality should be started from here.
    */
    public void run()
    {
        running = true;
        
        //activates all of the sensore S1, S2, S3
        zincBot.activate();
        
        //prepares the sensor events for accepting events
        on = true;
        
        go();
    }
    
    /** The true body of the code. Your code will likely go in this block.
    *
    */
    public void go()
    {
        
        //startes the created timer
        zincBot.startTimer(timer1);
    
    }
   
    
    /** Whenever a timer elapses this method is called and the timer which 
    * elapsed is passed as a parameter
    *
    * @param elapsedTimer, the timer which elapsed
    */
    public void setTimerExecution(int elapsedTimer) 
    {
       if(running)
        {
            if(elapsedTimer == timer1)
            {
                //stop the timer
                zincBot.stopTimer(timer1);
                
                //begin desired execution of the rocking
                if(var)
                {
                    //the robot moves forward
                    zincBot.forward();
                }
                
                else
                {
                    //the robot moves backwards
                    zincBot.backward();
                }

                var = !var;
                
                //start the timer once again
                zincBot.startTimer(timer1);
            }
        }
        
        else
        {
            
            //stop all the active timers
            zincBot.stopAllTimers();
        }
        
    }
    
}
