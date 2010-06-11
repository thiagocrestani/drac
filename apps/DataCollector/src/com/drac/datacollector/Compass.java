package com.drac.datacollector;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;

import java.util.Vector;

public class Compass extends Listener
{
    public Compass()
    {
	// set up tolerance
	// this.tolerances.add(new ToleranceManager(0, Output.getCommonDirectory() + 
	// 					 "azwithtol_" + 0 + ".dat"));
	// for(float x = 30.0f; x <= 90.0f + .005; x += 5f)
	//     {
	// 	this.tolerances.add(new ToleranceManager(x, Output.getCommonDirectory() + 
	// 						 "azwithtol_" + x + ".dat"));
	//     }
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {	
    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
	// do not record anything if there are any errors
	if(sensorEvent.values.length != 3)
	    return;
	
	float azimuth = sensorEvent.values[0];
	float pitch = sensorEvent.values[1];
	float roll = sensorEvent.values[2];

	long time = System.currentTimeMillis() - this.startTime;

	// for(ToleranceManager t : this.tolerances)
	//     {
	// 	t.write(time, t.getMagnitude(azimuth));
	//     }
    }

    public boolean register(SensorManager sensorManager)
    {
	return sensorManager.registerListener(this,
					      sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					      SensorManager.SENSOR_DELAY_FASTEST,
					      new Handler());
    }

    public void onStop()
    {
    }

    private Vector<ToleranceManager> tolerances = new Vector<ToleranceManager>();
}