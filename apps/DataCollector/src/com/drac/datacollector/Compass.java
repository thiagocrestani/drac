package com.drac.datacollector;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;

public class Compass extends Listener
{
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {	
    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
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
}