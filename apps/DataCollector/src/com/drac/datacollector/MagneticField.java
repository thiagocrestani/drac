package com.drac.datacollector;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.content.Context;

public class MagneticField extends Listener
{
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {	
    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
	if(sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
	    return;
	
	this.magn = sensorEvent.values.clone();
    }

    public boolean register(SensorManager sensorManager)
    {
	Handler handler = new Handler();

	return sensorManager.registerListener(this,
					      sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
					      SensorManager.SENSOR_DELAY_FASTEST,
					      handler);
    }

    public void onStop()
    {
    }

    public static float[] magn = null;
}