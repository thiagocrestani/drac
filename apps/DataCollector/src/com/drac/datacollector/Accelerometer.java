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
	this.xOut = new Output(Output.getCommonDirectory() + "x.dat");
	this.yOut = new Output(Output.getCommonDirectory() + "y.dat");
	this.zOut = new Output(Output.getCommonDirectory() + "z.dat");
	this.angleOut = new Output(Output.getCommonDirectory() + "angle.dat");

	this.tolerances.add(new ToleranceManager(0, Output.getCommonDirectory() + 
						 "magwithtol_" + 0 + ".dat"));
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {	
    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
	if(sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
	    return;

	long time = System.currentTimeMillis() - this.startTime;

	float[] R = new float[16];
	float[] I = new float[16];
		
	if(Accelerometer.accel != null && MagneticField.magn != null)
	    {
		if(!SensorManager.getRotationMatrix(R, I, Accelerometer.accel, MagneticField.magn))
		    return;

		float x = 0;
		float y = 0;
		float z = 0;
		
		x = R[0]*sensorEvent.values[0] + R[1]*sensorEvent.values[1] + R[2]*sensorEvent.values[2];
		y = R[4]*sensorEvent.values[0] + R[5]*sensorEvent.values[1] + R[6]*sensorEvent.values[2];
		z = R[8]*sensorEvent.values[0] + R[9]*sensorEvent.values[1] + R[10]*sensorEvent.values[2];

		this.xOut.write(time, x);
		this.yOut.write(time, y);
		this.zOut.write(time, z);

		float mag = (float)Math.sqrt(x*x + y*y);
		if(mag > 1.3)
		    this.angleOut.write((long)(Math.atan(y/x)*180.0f/Math.PI), mag);
		
		for(ToleranceManager t : this.tolerances)
		    {
			t.write(time, t.getMagnitude(z));
			t.updateStepCounter(time);
		    }
	    }

	this.accel = sensorEvent.values.clone();
    }

    private float getMagnitude(float x, float y, float z)
    {
	return (float)Math.sqrt(x*x + y*y + z*z);
    }

    public boolean register(SensorManager sensorManager)
    {
	Handler handler = new Handler();
	return sensorManager.registerListener(this,
					      sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					      SensorManager.SENSOR_DELAY_FASTEST,
					      handler);
    }

    public void onStop()
    {
	this.xOut.close();
	this.yOut.close();
	this.zOut.close();

	Output stepCounter = new Output(Output.getCommonDirectory() + "steps.dat");
	
	for(ToleranceManager t : this.tolerances)
	    {
		stepCounter.write(t.getTolerance() + " " + t.countSteps() + "\n");
	    }

	stepCounter.close();
    }
    
    // output
    private Output xOut;
    private Output yOut;
    private Output zOut;

    private Output angleOut;
    
    // The tolerance represents a threshold that the change in
    // acceleration must meet to be recorded. This should smooth our
    // the data.
    private Vector<ToleranceManager> tolerances = new Vector<ToleranceManager>();

    // step detection
    private StepCounter stepCounter = new StepCounter();
    
    public static float[] accel = null;
}