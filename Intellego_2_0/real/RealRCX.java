package real;

import interfaces.*;

import josx.platform.rcx.*;
import java.util.*;
import josx.util.*;

/**
* This class implements the simple commands provided by AbstractRobot with
* real lejos commands, allowing the simple commands to be run on the 
* real RCX.
*
* @author Graham Ritchie
*/
public class RealRCX extends Thread implements AbstractRobot, SensorConstants
{
        private int[] sensors;
	public static int MOTOR_POWER;
        private Controller controller1;
        private Vector timerList = new  Vector();
        private Vector lightList = new Vector();
        private Vector toughList = new Vector();
                
	public void run(){}
	
	/**
	* Initialises necessary variables, and sets up sensors, currently fixed to:
	*
	* @param controller the Controller associated with this RCX 
	*/
	public RealRCX(Controller controller)
	{
		MOTOR_POWER=2;
		Motor.A.setPower(MOTOR_POWER);
		Motor.B.setPower(MOTOR_POWER);
		Motor.C.setPower(MOTOR_POWER);
		
		// set up sensors as specified in controller
		sensors=controller.getSensors();
                controller1 = controller;
		
		if(sensors[0]==Controller.SENSOR_TYPE_LIGHT)
		{
			Sensor.S1.setTypeAndMode (SensorConstants.SENSOR_TYPE_LIGHT, SensorConstants.SENSOR_MODE_PCT);
			//Sensor.S1.activate();
                        Sensor.S1.addSensorListener(new LListener(controller1,1));
		}
		else if(sensors[0]==Controller.SENSOR_TYPE_TOUCH)
		{
			Sensor.S1.setTypeAndMode (SensorConstants.SENSOR_TYPE_TOUCH, SensorConstants.SENSOR_MODE_PCT);
			//Sensor.S1.activate();
                        Sensor.S1.addSensorListener(new SListener(controller1,1));
		}
		
		if(sensors[1]==Controller.SENSOR_TYPE_LIGHT)
		{
			Sensor.S2.setTypeAndMode (SensorConstants.SENSOR_TYPE_LIGHT, SensorConstants.SENSOR_MODE_PCT);
			//Sensor.S2.activate();
                        Sensor.S2.addSensorListener(new LListener(controller1,2));
		}
		else if(sensors[1]==Controller.SENSOR_TYPE_TOUCH)
		{
			Sensor.S2.setTypeAndMode (SensorConstants.SENSOR_TYPE_TOUCH, SensorConstants.SENSOR_MODE_PCT);
			//Sensor.S2.activate();
                        Sensor.S2.addSensorListener(new SListener(controller1,2));
		}
		
		if(sensors[2]==Controller.SENSOR_TYPE_LIGHT)
		{
			Sensor.S3.setTypeAndMode (SensorConstants.SENSOR_TYPE_LIGHT, SensorConstants.SENSOR_MODE_PCT);
			//Sensor.S3.activate();
                        Sensor.S3.addSensorListener(new LListener(controller1,3));
		}
		else if(sensors[2]==Controller.SENSOR_TYPE_TOUCH)
		{
			Sensor.S3.setTypeAndMode (SensorConstants.SENSOR_TYPE_TOUCH, SensorConstants.SENSOR_MODE_PCT);
			//Sensor.S3.activate();
                        Sensor.S3.addSensorListener(new SListener(controller1,3));
		}
                
                
	}
        
	/*******************************************************************
	                Methods required by AbstractRobot
	*******************************************************************/
	
        public void activate(){
            Sensor.S1.activate();
            Sensor.S2.activate();
            Sensor.S3.activate();
        }
	/**
	* Sets the robot moving forwards, this will continue until some other 
	* method is called to stop it.
	*/
	public void forward()
	{
		Motor.A.stop();
		Motor.C.stop();

                Motor.A.backward();
		Motor.C.backward();
	}
	
	/**
	* Makes the robot move forwards for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void forward(int time)
	{
		Motor.A.stop();
		Motor.C.stop();
                
		Motor.A.backward();
		Motor.C.backward();
		try{sleep(time);}catch(Exception e){}
		Motor.A.stop();
		Motor.C.stop();
	}
	
	/**
	* Sets the robot moving backwards, this will continue until some other 
	* method is called to stop it.
	*/
	public void backward()
	{
            Motor.A.stop();
	    Motor.C.stop();

            Motor.A.forward();
            Motor.C.forward();
	}
	
	/**
	* Makes the robot move backwards for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void backward(int time)
	{
            Motor.A.stop();
            Motor.C.stop();
            
            Motor.A.forward();
            Motor.C.forward();
            try{sleep(time);}catch(Exception e){}
            Motor.A.stop();
            Motor.C.stop();
	}
	
	/**
	* Sets the robot spinning right, this will continue until some other 
	* method is called to stop it.
	*/
	public void right()
	{
		Motor.A.stop();
		Motor.C.stop();
                
		Motor.A.backward();
		Motor.C.forward();
	}
	
	/**
	* Spins the robot right for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void right(int time)
	{
            Motor.A.stop();
            Motor.C.stop();
            
            Motor.A.backward();
            Motor.C.forward();
            try{sleep(time);}catch(Exception e){}
            Motor.A.stop();
            Motor.C.stop();
	}
	
	/**
	* Sets the robot spinning left, this will continue until some other 
	* method is called to stop it.
	*/
	public void left()
	{
            Motor.A.stop();
            Motor.C.stop();
            
            Motor.A.forward();
            Motor.C.backward();
	}
	
	/**
	* Spins the robot left for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void left(int time)
	{
            Motor.A.stop();
            Motor.C.stop();
            
            Motor.A.forward();
            Motor.C.backward();
            
            try{sleep(time);}catch(Exception e){}
            Motor.A.stop();
            Motor.C.stop();
	}
        
        /**
	* Sets a single Motor moving, this will continue until some other 
	* method is called to stop it
        *
	* @param motor the motor number (AbstractRobot.MOTOR_A or AbstractRobot.MOTOR_C);
        * @param direction, the direction number (AbstractRobot.BACKWARDS or AbstractRobot.FORWARDS
	*/
        public void singleMotor(int motor, int direction) {
            Motor.A.stop();
            Motor.C.stop();
            
            if(motor == this.MOTOR_A){
                if(direction == this.FORWARD) Motor.A.backward();
                else if(direction == this.BACKWARD) Motor.A.forward();
            }
            else if(motor == this.MOTOR_C){
                if(direction == this.FORWARD) Motor.C.backward();
                else if(direction == this.BACKWARD) Motor.C.forward();
            }
        }
       
         /**
	* Sets a single Motor moving for the given amount of time
	*
	* @param motor the motor number (AbstractRobot.MOTOR_A or AbstractRobot.MOTOR_C);
        * @param direction, the direction number (AbstractRobot.BACKWARDS or AbstractRobot.FORWARDS
        * @param time the time in milliseconds 
	*/
        public void singleMotor(int motor, int direction, int time) {
            this.singleMotor(motor,direction);
            try{sleep(time);}catch(Exception e){}
            Motor.A.stop();
            Motor.C.stop();
        } 
	
	/**
	* Stops all motors immediately
	*/
	public void stopMoving()
	{
            Motor.A.stop();
            Motor.C.stop();
	}
	
	/**
	* Makes the robot beep
	*/
	public void beep()
	{
            Sound.beep();
	}
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public int getSensor1()
	{
		return Sensor.S1.readValue();
	}
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public int getSensor2()
	{
		return Sensor.S2.readValue();
	}
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public int getSensor3()
	{
		return Sensor.S3.readValue();
	}
        
        /** Display numbers on the LCD
         *
         *Modified by: Simon Zienkiewicz
         *
         */
        public void displayNumberLCD(int num) {
            this.clearLCD();
            LCD.showNumber(num);
            LCD.refresh();
        }
        
        /** Clears the display on the LCD
         *
         * Modified by: Simon Zienkiewicz
         *
         */
        public void clearLCD() {
            LCD.clear();
        }        
        
        /**
	* Display text on the LCD
	*
	* Modified by: Simon Zienkiewicz
	*/
        public void displayTextLCD(String word) {
            String newWord = word;
            this.clearLCD();
            TextLCD.print(newWord);
        } 
        
        /**
	* Sets the power of the motors for the robot
        * Modified by: Simon Zienkiewicz
	*/
        public void setMotorPower(int power){
            
            if(power >= 7){
                MOTOR_POWER=7;
            }
            else if(power<=0){
                MOTOR_POWER=0;
            }
            else{
                MOTOR_POWER=power;
            }
            
            Motor.A.setPower(MOTOR_POWER);
            Motor.B.setPower(MOTOR_POWER);
            Motor.C.setPower(MOTOR_POWER);
        }
        
        /**
	* Gets the power of the motors for the robot
        * Modified by: Simon Zienkiewicz
        *
        * @return the power of the motor 0-7
	*/
        public int getMotorPower() {
            return Motor.C.getPower();
        }
                
        /**
	* Gets an array of boolean values for the touch sensors
        * Modified by: Simon Zienkiewicz
        *
        * @return boolean values of the status of the sensors
	*/
        public boolean[] getTouchSensorStatus() {
            boolean[] touchStatus = new boolean[3];
            
            for(int a=0;a<3;a++){
                if(sensors[a]==Controller.SENSOR_TYPE_TOUCH){
                    touchStatus[a]= Sensor.S2.readBooleanValue();
                }
            }
            return touchStatus;
        }
        
        /**
	* Gets the voltage of the RCX battery in MilliVolts
        * Modified by: Simon Zienkiewicz
        *
        * @return int voltage in the battery of the RCX
	*/
        public int getVoltage() {
            return Battery.getVoltageMilliVolt();
        }
        
        public int createTimer(int time) {
            josx.util.Timer timer = new josx.util.Timer(time, new TListener(controller1,timerList.size()));
            timerList.addElement(timer);
            return timerList.indexOf(timer);
        }
        
        public void startTimer(int num) {
            try{((josx.util.Timer)timerList.elementAt(num)).start();} catch(Exception e){}
        }
        
        public void stopTimer(int num) {
            try{((josx.util.Timer)timerList.elementAt(num)).stop();} catch(Exception e){}
        }
        
        public void stopAllTimers(){
             for(int i=0;i<this.timerList.size();i++){
                ((josx.util.Timer)timerList.elementAt(i)).stop();
            }
        }
        
        public int getDelay(int num) {
            try{return((josx.util.Timer)timerList.elementAt(num)).getDelay();} catch(Exception e){}
            return 911;
        }
        
        public void setDelay(int num, int time) {
            try{((josx.util.Timer)timerList.elementAt(num)).setDelay(time);} catch(Exception e){}
        }
         
        public void pause(int num) {
            try{Thread.sleep(num);}
            catch(Exception e){};
        }
        
        /**
	* Plays a sound
        * Created by: Simon Zienkiewicz
        *
        * @param frequency, the sound frequency
        * @param duration, the duration of the sound in milliseconds
	*/
        public void playTune(int frequency, int duration) {
            josx.platform.rcx.Sound.playTone(frequency, duration);
         }
        
        //time listener
        private class TListener implements TimerListener {
  
            private int index;
            private Controller controller;
            
            public TListener(Controller c, int num){
                controller = c;
                index = num;
            
            }
            public void timedOut() {
                controller.setTimerExecution(index);
            }
        }
        
        //sensor listener
        private class SListener implements SensorListener {
            
            private int index;
            private Controller controller;
            
            public SListener(Controller c,int i){
                controller = c;
                index = i;
            }
            public void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {
                controller.touchSensorListener(index);
            }
        }
        
        private class LListener implements SensorListener {
            
            private int index;
            private Controller controller;
            
            public LListener(Controller c,int i){
                controller = c;
                index = i;
            }
            public void stateChanged(Sensor aSource, int aOldValue, int aNewValue) {
                controller.lightSensorListener(index);
            }
        }
        
        
}
