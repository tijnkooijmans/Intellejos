package simworldobjects;

import interfaces.*;
import main.*;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.geom.*;

import java.util.*;
import josx.util.*;

/**
* Simulates an RCX for use in SimWorld
*
* @author Graham Ritchie
*/
public class SimRCX extends BasicSimObject implements AbstractRobot 
{
	public static final int STATE_STOPPED=1;
	public static final int STATE_FORWARD=2;
	public static final int STATE_BACKWARD=3;
	public static final int STATE_RIGHT=4;
	public static final int STATE_LEFT=5;
        
        private Point2D basePointQ1;
        private Point2D basePointQ2;
        private Point2D basePointQ3;
        private Point2D basePointQ4;
        
        private static final double ANGLE_OFFSET = 28.18;
        private static final double RADIUS = Math.sqrt((Math.pow(28,2)+Math.pow(15,2)));
        private boolean rotateAboutWheel=false;
        private int activeMotor = 3;
	
	private int state;
	private SimWorld world;
        private boolean selected = false;
        private boolean activation = false;
	
	private SimSensor S1;
	private SimSensor S2;
	private SimSensor S3;
	
	private Controller controller;
        private int MOTOR_POWER=1;
        private int[] sensors;
        private int BATTERY_VOLTAGE;
        private String LCD_OUTPUT="";
        
        private double lastXCoord=0;
        private double lastZCoord=0;
        private boolean changeBack = false;
        
        private double decreasingAngle = 0;
        
        private Vector timerList = new  Vector();
        private Sound soundPlayer=null;
	
	/**
	* Constructor: sets up the robot with some predefined parameters
	*
	* @param w the SimWorld where this SimRCX resides
	* @param c the Controller of this SimRCX
	*/
	public SimRCX(SimWorld w, Controller c)
	{
		// initialise the SimObject
		super(30.0,40.0,60.0,"robot",600.0,0.0,200.0,0.0,160.0);
      		
                // set this robot's world
		this.world=w;
		
		// set this robot's controler
		this.controller=c;
                
                //sets the robots battery voltage
                this.BATTERY_VOLTAGE=7900;
		
		setupRobot();
                
	}
	
	/**
	* Constructor: sets up the robot according to the parameters
	*/
	public SimRCX(SimWorld w, Controller c, double height, double width, double length, String type, double x, double y, double z, double bearingXY, double bearingXZ, int battery)
	{
		// initialise the SimObject
		super(height,width,length,type,x,y,z,bearingXY,bearingXZ);
		
		// set this robot's world
		world=w;
		
		// set this robot's controler
		this.controller=c;
                
                //sets the robots battery voltage
                this.BATTERY_VOLTAGE=battery;
                
		setupRobot();
	}
	
	/**
	* Sets up robot: adds sensors etc.
	*/
	private void setupRobot()
	{
		// set the robot's initial state to stopped
		this.state=SimRCX.STATE_STOPPED;
		
		// set up sensors as specified in controller
		sensors=controller.getSensors();
		
		if(sensors[0]==Controller.SENSOR_TYPE_LIGHT)
		{
			S1=new SimLightSensor(this,-(this.getWidth()/2)-7,-((this.getLength()/10)-10),'L');
		}
		else
		{
			S1=new SimTouchSensor(this,-this.getWidth(),-((this.getLength()/6)),'L');
		}
		
		if(sensors[1]==Controller.SENSOR_TYPE_LIGHT)
		{
			S2=new SimLightSensor(this,0.0,-(this.getLength()/2)-25,'F'); 
		}
		else
		{
			S2=new SimTouchSensor(this,0.0,-(this.getLength()/2)-15,'F');
		}
		
		if(sensors[2]==Controller.SENSOR_TYPE_LIGHT)
		{
			S3=new SimLightSensor(this,(this.getWidth()/2)+7,-((this.getLength()/10)-10),'R');
		}
		else
		{
			S3=new SimTouchSensor(this,this.getWidth(),-this.getLength()/4,'R');
		}
		
		// add sensors to the world
		world.addObject(S1);
		world.addObject(S2);
		world.addObject(S3);
                                
	}
        
        public boolean getSelected(){
            return this.selected;
        }
        public void setSelected(boolean selected){
            this.selected = selected;
        }
	
	public SimWorld getWorld()
	{
		return this.world;
	}
	
	/*******************************************************************
	                 Methods required by AbstractRobot
	*******************************************************************/
	
	/**
	* Sets the robot moving forwards, this will continue until some other 
	* method is called to stop it.
	*/
	public void forward()
	{
            
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_FORWARD;
            this.setDesiredVelocity(MOTOR_POWER);
	}
	
	/**
	* Makes the robot move forwards for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void forward(int time)
	{
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_FORWARD;
            this.setDesiredVelocity(MOTOR_POWER);
            try{Thread.sleep(time);}catch(Exception e){}
            stopMoving();
	}
	
	/**
	* Sets the robot moving backwards, this will continue until some other 
	* method is called to stop it.
	*/
	public void backward()
	{
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_BACKWARD;
            this.setDesiredVelocity(-(MOTOR_POWER));
	}
	
	/**
	* Makes the robot move backwards for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void backward(int time)
	{
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_BACKWARD;
            this.setDesiredVelocity(-(MOTOR_POWER));
            try{Thread.sleep(time);}catch(Exception e){}
            stopMoving();
	}
	
	/**
	* Sets the robot spinning right, this will continue until some other 
	* method is called to stop it.
	*/
	public void right()
	{
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_RIGHT;
            this.setDesiredBearingVelocityXZ(MOTOR_POWER);
	}
	
	/**
	* Spins the robot right for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void right(int time)
	{
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_RIGHT;
            this.setDesiredBearingVelocityXZ(MOTOR_POWER);
            try{Thread.sleep(time);}catch(Exception e){}
            stopMoving();
	
	}
	
	/**
	* Sets the robot spinning left, this will continue until some other 
	* method is called to stop it.
	*/
	public void left()
	{
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_LEFT;
            this.setDesiredBearingVelocityXZ(-(MOTOR_POWER));
	}
	
	/**
	* Spins the robot left for the given amount of time
	*
	* @param time the time in milliseconds
	*/
	public void left(int time)
	{
            this.rotateAboutWheel = false;
            stopMoving();
            this.state=SimRCX.STATE_LEFT;
            this.setDesiredBearingVelocityXZ(-(MOTOR_POWER));
            try{Thread.sleep(time);}catch(Exception e){}
            stopMoving();
	}
        
        /**
	* Sets a single Motor moving, this will continue until some other 
	* method is called to stop it
        *
	* @param motor the motor number (AbstractRobot.MOTOR_A or AbstractRobot.MOTOR_C);
        * @param direction, the direction number (AbstractRobot.BACKWARDS or AbstractRobot.FORWARDS
	*/
        public void singleMotor(int motor, int direction) {
            
            this.rotateAboutWheel = false;
            stopMoving();
            
            this.activeMotor = motor;
            this.makeQuadrantBasePts(this.determineQuadrant());
            this.rotateAboutWheel = true;
            
            switch(motor){
                case MOTOR_A:
                    //set the rotation velocity
                    switch(direction){
                        case FORWARD:
                            this.setDesiredBearingVelocityXZ(MOTOR_POWER);
                            break;

                        case BACKWARD:
                            this.setDesiredBearingVelocityXZ(-(MOTOR_POWER));
                            break;
                    }
                    break;
                    
                case MOTOR_C:
                    switch(direction){
                        case FORWARD:
                            this.setDesiredBearingVelocityXZ(-(MOTOR_POWER));
                            break;

                        case BACKWARD:
                            this.setDesiredBearingVelocityXZ(MOTOR_POWER);
                            break;
                    }
                    break;
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
            try{Thread.sleep(time);}catch(Exception e){}
            stopMoving();
        } 
        
        private int determineQuadrant(){
            double angle = this.getActualBearingXZ()-this.ANGLE_OFFSET;
            int motor = this.activeMotor;
                        
            //reduce the angle to a value where 0 <= angle <= 360
            while(angle > 0) angle = angle-360.0;
            while(angle < 0) angle = angle+360.0;
                        
            //angle is 0 <= angle <= 360
            if(motor == this.MOTOR_C){
                angle = angle +180+(2*this.ANGLE_OFFSET);
                while(angle > 0) angle = angle-360.0;
                while(angle < 0) angle = angle+360.0;
                
                this.decreasingAngle = angle;
                
            }
            
            if(angle >= 0.0 && angle < 90.0) return 1;
            else if(angle >= 90.0 && angle < 180.0) return 2;
            else if(angle >= 180.0 && angle < 270.0) return 3;
            else if(angle >= 270.0 && angle < 360.0) return 4;
            
            return 911;
       
        }
        
        private double getSmallAngle(double angle){
            
            while(angle > 0) angle = angle-90.0;
            while(angle < 0) angle = angle+90.0;
            
             //angle is 0 <= angle <= 90
            return angle;
        
        }
        private void makeQuadrantBasePts(int quadNum){
            
            int motor = this.activeMotor;
            double angle = this.getSmallAngle(this.getActualBearingXZ()-this.ANGLE_OFFSET);
            
            if(motor ==  MOTOR_C){
                angle = this.getSmallAngle(this.decreasingAngle);
            }
                        
            switch(quadNum){
                case 1: 

                    this.basePointQ1 = new Point2D.Double(this.getXCoord()-this.formula(1,angle),this.getZCoord()+this.formula(0,angle));

                    this.basePointQ2 = new Point2D.Double(this.basePointQ1.getX()+this.RADIUS,this.basePointQ1.getY()-this.RADIUS);
                    this.basePointQ3 = new Point2D.Double(this.basePointQ1.getX()+(this.RADIUS*2),this.basePointQ1.getY());
                    this.basePointQ4 = new Point2D.Double(this.basePointQ1.getX()+this.RADIUS,this.basePointQ1.getY()+this.RADIUS);
                    break;

                case 2:
                    this.basePointQ2 = new Point2D.Double(this.getXCoord()-this.formula(0,angle),this.getZCoord()-this.formula(1,angle));

                    this.basePointQ1 = new Point2D.Double(this.basePointQ2.getX()-this.RADIUS,this.basePointQ2.getY()+this.RADIUS);
                    this.basePointQ3 = new Point2D.Double(this.basePointQ1.getX()+(this.RADIUS*2),this.basePointQ1.getY());
                    this.basePointQ4 = new Point2D.Double(this.basePointQ1.getX()+this.RADIUS,this.basePointQ1.getY()+this.RADIUS);
                    break;

                case 3:
                    this.basePointQ3 = new Point2D.Double(this.getXCoord()+this.formula(1,angle),this.getZCoord()-this.formula(0,angle));

                    this.basePointQ1 = new Point2D.Double(this.basePointQ3.getX()-(this.RADIUS*2),this.basePointQ3.getY());
                    this.basePointQ2 = new Point2D.Double(this.basePointQ1.getX()+this.RADIUS,this.basePointQ1.getY()-this.RADIUS);
                    this.basePointQ4 = new Point2D.Double(this.basePointQ1.getX()+this.RADIUS,this.basePointQ1.getY()+this.RADIUS);
                    break;

                case 4:
                    this.basePointQ4 = new Point2D.Double(this.getXCoord()+this.formula(0,angle),this.getZCoord()+this.formula(1,angle));

                    this.basePointQ1 = new Point2D.Double(this.basePointQ4.getX()-this.RADIUS,this.basePointQ4.getY()-this.RADIUS);
                    this.basePointQ2 = new Point2D.Double(this.basePointQ1.getX()+this.RADIUS,this.basePointQ1.getY()-this.RADIUS);
                    this.basePointQ3 = new Point2D.Double(this.basePointQ1.getX()+(this.RADIUS*2),this.basePointQ1.getY());
                    break;
                
            }
                 
        }
        
        private double formula(int a, double angle){
            
            angle = (angle/180)*Math.PI;
            
            switch(a){
                case 0:
                    return (Math.sin(angle)*this.RADIUS);
                    
                case 1:
                    return (this.RADIUS*(1-Math.cos(angle)));
                    
            }
            return 911;
        
        }
        
        public void pivotRotation(){
            if(this.rotateAboutWheel){
                this.moveSimRCXCenter(this.getSmallAngle(this.getActualBearingXZ()-this.ANGLE_OFFSET),this.determineQuadrant());
            }
        }
        
        private void moveSimRCXCenter(double angle,int quadNum){
            
            if(this.activeMotor== this.MOTOR_C){
               angle = this.getSmallAngle(decreasingAngle);
            }
            
            switch(quadNum){
                case 1:
                    this.setXCoord(this.basePointQ1.getX()+this.formula(1,angle));
                    this.setZCoord(this.basePointQ1.getY()-this.formula(0, angle));
                    break;

                case 2:
                    this.setXCoord(this.basePointQ2.getX()+this.formula(0,angle));
                    this.setZCoord(this.basePointQ2.getY()+this.formula(1, angle));
                    break;

                case 3:
                    this.setXCoord(this.basePointQ3.getX()-this.formula(1,angle));
                    this.setZCoord(this.basePointQ3.getY()+this.formula(0, angle));
                    break;
                case 4:
                    this.setXCoord(this.basePointQ4.getX()-this.formula(0,angle));
                    this.setZCoord(this.basePointQ4.getY()-this.formula(1, angle));
                    break;
                    
            }
        }     
           
	/**
	* Stops all motors immediately
	*/
	public void stopMoving()
	{
		this.state=SimRCX.STATE_STOPPED;
		this.setDesiredVelocity(0);
		this.setDesiredBearingVelocityXZ(0);
	}
	
	/**
	* Makes the robot beep
	*/
	public void beep()
	{
		Toolkit.getDefaultToolkit().beep(); 
                
	}
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public int getSensor1()
	{
		return S1.getValue();
	}
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public int getSensor2()
	{
		return S2.getValue();
	}
	
	/**
	* Get the current reading of this sensor
	*
	* @return the current value
	*/
	public int getSensor3()
	{
		return S3.getValue();
	}
        
        /** Clears the display on the LCD
         *
         * Modified by: Simon Zienkiewicz
         *
         */
        public void clearLCD() {
            this.LCD_OUTPUT=" ";
        }
        
        /** Display numbers on the LCD
         *
         * Modified by: Simon Zienkiewicz
         *
         */
        public void displayNumberLCD(int num) {
            this.LCD_OUTPUT=num+"";
        }
        
        /**
	* Display text on the LCD
	*
	* Modified by: Simon Zienkiewicz
	*/
        public void displayTextLCD(String word) {
            String actualword;
            if(word.length() <=5){
                actualword = word;
            }
            else{
                actualword = word.substring(0,5);
            }
            
            this.LCD_OUTPUT=actualword;
        }
        public String getLCDOutput(){
            return this.LCD_OUTPUT;
        }
        
        /** 
         * Sets the power of the motors for the robot
         * Modified by: Simon Zienkiewicz
         *
         */
        public void setMotorPower(int power) {
            /*
            if(power >= 7){
                MOTOR_POWER=8;
            }
            else if(power<=0){
                MOTOR_POWER=1;
            }
            else{
                MOTOR_POWER=power;
            }*/
            MOTOR_POWER=7;
        }
        
        /**
	* Gets the power of the motors for the robot
        * Modified by: Simon Zienkiewicz
        *
        * @return the power of the motor 0-7
	*/
        public int getMotorPower() {
            return (MOTOR_POWER-1);
        }
                
        /**
	* Gets an array of boolean values for the touch sensors
        * Modified by: Simon Zienkiewicz
        *
        * @return boolean values of the status of the sensors
	*/
        public boolean[] getTouchSensorStatus() {
            boolean[] touchStatus = new boolean[3];
            SimSensor[] sensorList = {S1,S2,S3};
            
            for(int a=0;a<3;a++){
                if(sensors[a]==Controller.SENSOR_TYPE_TOUCH){
                    touchStatus[a]= ((SimTouchSensor)sensorList[a]).getBooleanValue();
                }
                else{
                    touchStatus[a]=false;
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
            return this.BATTERY_VOLTAGE;
        }        
               
        public int createTimer(int time) {
            josx.util.Timer timer = new josx.util.Timer(time, new TListener(controller,timerList.size()));
            timerList.addElement(timer);
            return timerList.indexOf(timer);
        }
        
        public void startTimer(int num) {
            try{((josx.util.Timer)timerList.elementAt(num)).start();} catch(Exception e){}
        }
        
        public void stopTimer(int num) {
            try{((josx.util.Timer)timerList.elementAt(num)).stop();} catch(Exception e){}
        }
        
        public void stopAllTimers() {
            
            for(int i=0;i<this.timerList.size();i++){
                try{((josx.util.Timer)timerList.elementAt(i)).stop();} catch(Exception e){}
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
        
        public void activate() {
            activation = true;
        }
        
        //Event triggers
        public void lightEvent(int index){
            if(activation) controller.lightSensorListener(index);
        }
        
        public void touchEvent(int index){
            if(activation) {
                stopMoving();
                for(int i=0;i<this.timerList.size();i++){
                   ((josx.util.Timer)timerList.elementAt(i)).stop();
                }
                
                controller.touchSensorListener(index);        
            }
        }
        
        public void playTune(int frequency, int duration) {
            Sound.playTune(frequency, duration);
        }
        
        private class TListener implements TimerListener {
        /** Reverses the direction of the robot.  Called when the timer times
        * out.
        */
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
        
}
