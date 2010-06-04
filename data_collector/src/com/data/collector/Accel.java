package com.data.collector;

import static android.hardware.SensorManager.DATA_X;
import static android.hardware.SensorManager.DATA_Y;
import static android.hardware.SensorManager.DATA_Z;
import static android.hardware.SensorManager.SENSOR_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_UI;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_LOW;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;
import static android.hardware.SensorManager.SENSOR_STATUS_UNRELIABLE;
import android.app.Activity;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Accel extends Activity implements SensorEventListener
{
    public void Accel()
    {
	boolean accelSupported = sensorMgr.registerListener(this,
							    this.sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							    SENSOR_DELAY_UI,
							    new Handler());
    }

    public void onSensorChanged(android.hardware.SensorEvent sensorEvent)
    {

    }
    
    public void onAccuracyChanged(android.hardware.Sensor sensor, int stuff)
    {
    }
    
    SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
}