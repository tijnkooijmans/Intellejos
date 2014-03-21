package interfaces;

/**
* Interface class for both real and simulated robots.
* Defines all the input and output methods a controller
* can call.
*
* @author Graham Ritchie && MODIFIED BY: Simon Zienkiewicz
*/
public interface AbstractRobot
{
        /** constant for single motor to move backwards */    
        public static final int BACKWARD = 0;
        
        /** constant for single motor to move forwards */        
        public static final int FORWARD = 1;  
        
        /** constant for MOTOR A */        
        public static final int MOTOR_A = 0; 
        
        /** constant for MOTOR C */        
        public static final int MOTOR_C = 1; 
        
        /** constant for sensor 1 */       
        public static final int S1 = 1; 
        
        /** constant for sensor 2 */        
        public static final int S2 = 2; 
        
        /** sconstant for sensor 3 */        
        public static final int S3 = 3; 
        
        
	/******************************************
	   Methods for moving the robot - output
	******************************************/
	
	/**
	* Sets the robot moving forwards, this will continue until some other 
	* method is called to stop it
	*/
	public abstract void forward();
         
         /**
	* Makes the robot move forwards for the given amount of time 
	* @param time the time in milliseconds
    	*/       
        public abstract void forward(int time);
       
	/**
	* Sets the robot moving backwards, this will continue until some other 
	* method is called to stop it 
	*/
	public abstract void backward();
	
	/**
	* Makes the robot move backwards for the given amount of time
	*
	* @param time the time in milliseconds
        */       
	public abstract void backward(int time);
	
	/**
	* Sets the robot spinning right on a dime, this will continue until some other 
	* method is called to stop it.
	*/
	public abstract void right();
	
	/**
	* Spins the robot right on a dime for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public abstract void right(int time);
	
	/**
	* Sets the robot spinning left on a dime, this will continue until some other 
	* method is called to stop it.
	*/
	public abstract void left();
	
	/**
	* Spins the robot left on a dime for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public abstract void left(int time);
	
        /** Sets a single Motor moving, this will continue until some other
         * method is called to stop it
         * Created by: Simon Zienkiewicz
         * @param direction the direction number (AbstractRobot.BACKWARDS or AbstractRobot.FORWARDS
         * @param motor the motor number (AbstractRobot.MOTOR_A or AbstractRobot.MOTOR_C);
         */
        public abstract void singleMotor(int motor, int direction);
       
        /** Sets a single Motor moving for the given amount of time
         * Created by: Simon Zienkiewicz
         * @param motor the motor number (AbstractRobot.MOTOR_A or AbstractRobot.MOTOR_C);
         * @param direction the direction number (AbstractRobot.BACKWARDS or AbstractRobot.FORWARDS
         * @param time the time in milliseconds
         */
        public abstract void singleMotor(int motor, int direction, int time);
    
        /**
	* Stops all motors immediately
	*/
	public abstract void stopMoving();
	
        
	/*************************************************
	  Methods for reading the robot's sensors - input 
	**************************************************/
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public abstract int getSensor1();
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public abstract int getSensor2();
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public abstract int getSensor3();
        
        /**
	* Gets an array of boolean values for the touch sensors
        * Created by: Simon Zienkiewicz
        *
        * @return boolean values of the status of the sensors
	*/
        public abstract boolean[] getTouchSensorStatus();
        
        
        /*************************************************
	  Methods for handling LCD output 
	**************************************************/
        
        
        /**
	* Display numbers on the LCD
	*
	* Created by: Simon Zienkiewicz
        * @param num the number to display
	*/
        public abstract void displayNumberLCD(int num);
                
        /**
	* Clears the display on the LCD
	*
	* Modified by: Simon Zienkiewicz
      	*/
        public abstract void clearLCD();
        
        /** Display text on the LCD
         * Created by: Simon Zienkiewicz
         * @param word string to display
         */
        public abstract void displayTextLCD(String word);
        
        
        /*************************************************
	  Methods for handling ROBOT properties 
	**************************************************/
        
        
        /** Sets the power of the motors for the robot
         * Created by: Simon Zienkiewicz
         * @param power the desired power
         */
        public abstract void setMotorPower(int power);
        
        /**
	* Gets the power of the motors for the robot
        * Created by: Simon Zienkiewicz
        *
        * @return the power of the motor 0-7
	*/
        public abstract int getMotorPower();
                      
        /**
	* Gets the voltage of the RCX battery in MilliVolts
        * Created by: Simon Zienkiewicz
        *
        * @return int voltage in the battery of the RCX
	*/
        public abstract int getVoltage();
        
        /**
	* Activates the light and touch sensors
        * Created by: Simon Zienkiewicz
        *
	*/
        public abstract void activate();
        
        /** Plays a sound
         * Created by: Simon Zienkiewicz
         * @param frequency the sound frequency
         * @param duration the duration of the sound in milliseconds
         */
        public abstract void playTune(int frequency, int duration);
                
        /**
	* Makes the robot beep
	*/
	public abstract void beep();
        
        /*************************************************
	  Methods for handling timer/timing events 
	**************************************************/
        
        /** Creates a timer
         * Created by: Simon Zienkiewicz
         * @param time the delay for the timer
         * @return int the timer number
         */
        public abstract int createTimer(int time);
        
        /** Starts the requested timer
         * Created by: Simon Zienkiewicz
         * @param num the timer number
         */
        public abstract void startTimer(int num);
         
        /** Stops the requested timer
         * Created by: Simon Zienkiewicz
         * @param num the timer number
         */
        public void stopTimer(int num);
        
        /**
	* Stops all timers
        * Created by: Simon Zienkiewicz
        *
        */
        public void stopAllTimers();
        
        /** Gets the delay from the requested timer
         * Created by: Simon Zienkiewicz
         * @param num the timer number
         * @return int the timer delay
         */       
        public int getDelay(int num);
        
        /** Sets the delay for the requested timer
         * Created by: Simon Zienkiewicz
         * @param num the timer number
         * @param time the new delay time
         */       
        public void setDelay(int num, int time);
        
        /** Pauses the simulation
         * Created by: Simon Zienkiewicz
         * @param num the number of milliseconds
         */
        public abstract void pause(int num);
        
        
      	        
} 
