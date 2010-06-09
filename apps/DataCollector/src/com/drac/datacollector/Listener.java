package com.drac.datacollector;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public abstract class Listener implements SensorEventListener
{
    abstract public boolean register(SensorManager sensorManager);
    public void setStartTime(long time)
    {
	this.startTime = time;
    }
    abstract public void onStop();

    protected long startTime;
}