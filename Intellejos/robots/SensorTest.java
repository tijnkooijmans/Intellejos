import lejos.platform.rcx.*;

public class SensorTest implements SensorListener, SensorConstants
{
	public void stateChanged (Sensor aSource, int aOldValue, int aNewValue)
	{
		System.out.println(aSource.getId() + ": " + aNewValue);
	}

	public SensorTest()
	{
		Sensor.S1.setTypeAndMode(SENSOR_TYPE_LIGHT, SENSOR_MODE_BOOL);
		Sensor.S2.setTypeAndMode(SENSOR_TYPE_LIGHT, SENSOR_MODE_BOOL);
		Sensor.S3.setTypeAndMode(SENSOR_TYPE_LIGHT, SENSOR_MODE_BOOL);
		Sensor.S1.activate();
		Sensor.S2.activate();
		Sensor.S3.activate();
		Sensor.S1.addSensorListener(this);
		Sensor.S2.addSensorListener(this);
		Sensor.S3.addSensorListener(this);
		forward();
	}

	public synchronized void backUpAndTurn(int sensorId)
	{
		backward();
		try{Thread.sleep(1000);} catch(Exception e){}
		if (sensorId == 1 || sensorId == 2)
		{
			turnRight();
			try{Thread.sleep(2000);} catch(Exception e){}
			forward();
		}
		else
		{
			turnLeft();
			try{Thread.sleep(2000);} catch(Exception e){}
			forward();
		}
	}

	public void forward()
	{
		Motor.A.forward();
		Motor.B.forward();
	}

	public void backward()
	{
		Motor.A.backward();
		Motor.B.backward();
	}

	public void turnRight()
	{
		Motor.A.forward();
		Motor.B.backward();
	}

	public void turnLeft()
	{
		Motor.B.forward();
		Motor.A.backward();
	}

	public static void main(String[] args)
	{
		SensorTest st = new SensorTest();
	}

}
