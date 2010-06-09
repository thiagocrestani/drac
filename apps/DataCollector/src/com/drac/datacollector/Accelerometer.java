package com.drac.datacollector;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.content.Context;

import java.io.*;
import java.util.Vector;
import java.text.DecimalFormat;

public class Accelerometer extends Listener
{
    public Accelerometer()
    {
	// make the directory
	File dracDir = new File(Output.getCommonDirectory());
	dracDir.mkdirs();

	// output
	this.magnitudeOut = new Output(Output.getCommonDirectory() + "mag.dat");

	// set up tolerance
	for(float x = 0.0f; x <= 2.0f + .005; x += 0.1f)
	    {
		this.tolerances.add(new ToleranceManager(x, Output.getCommonDirectory() + 
							 "magwithtol_" + x + ".dat"));
	    }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {	
    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
	// do not record anything if there are any errors
	if(sensorEvent.values.length != 3)
	    return;
	
	long time = System.currentTimeMillis() - this.startTime;
	float mag = this.getMagnitude(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);

	this.magnitudeOut.write(time, mag);
	
	for(ToleranceManager t : this.tolerances)
	    {
		t.write(time, t.getMagnitude(mag));
		t.updateStepCounter(time);
	    }
    }

    private float getMagnitude(float x, float y, float z)
    {
	return (float)Math.sqrt(x*x + y*y + z*z);
    }

    public boolean register(SensorManager sensorManager)
    {
	return sensorManager.registerListener(this,
					      sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					      SensorManager.SENSOR_DELAY_FASTEST,
					      new Handler());
    }

    public void onStop()
    {
	this.magnitudeOut.close();

	Output stepCounter = new Output(Output.getCommonDirectory() + "steps.dat");
	
	for(ToleranceManager t : this.tolerances)
	    {
		stepCounter.write(t.getTolerance() + " " + t.countSteps() + "\n");
	    }

	stepCounter.close();
    }
    
    // output
    private Output magnitudeOut;
    
    // The tolerance represents a threshold that the change in
    // acceleration must meet to be recorded. This should smooth our
    // the data.
    private Vector<ToleranceManager> tolerances = new Vector<ToleranceManager>();

    // step detection
    private StepCounter stepCounter = new StepCounter();
}