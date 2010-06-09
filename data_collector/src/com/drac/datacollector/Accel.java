package com.drac.datacollector;

import android.app.Activity;
import android.os.Bundle;
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
import android.app.Service;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.*;
import java.io.*;

public class Accel extends Service implements SensorEventListener
{
    public void onCreate()
    {
	//super.onCreate(savedInstanceState);
	//setContentView(R.layout.accel);

	// sleep for the specified timeout
	try
	    {
		Thread.sleep(Accel.timeout * 1000);
	    }
	catch(InterruptedException ex)
	    {
	    }

	// set the start time
	Accel.startTime = System.currentTimeMillis();

	// setup the sensor manager and register this as a listener to
	// the accelerometer
	this.sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE);

	boolean accelSupported = sensorMgr.registerListener(this,
							    this.sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							    SensorManager.SENSOR_DELAY_FASTEST,
							    new Handler());

	try
	    {
		xout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "x_" + Accel.runId + ".dat")));
		yout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "y_" + Accel.runId + ".dat")));
		zout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "z_" + Accel.runId + ".dat")));	
		magout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "mag_" + Accel.runId + ".dat")));     
		tolmagout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "tolmag_" + Accel.runId + ".dat")));
	    }
	catch(IOException ex)
	    {
	    }
    }
    
    public void onStart(Intent  intent, int flags, int startId)
    {
	//super.onStart();
	this.ID = startId;
    }

    public void onSensorChanged(android.hardware.SensorEvent sensorEvent)
    {	
	//TextView text = (TextView) findViewById(R.id.text);

	try
	    {
		long time = System.currentTimeMillis() - Accel.startTime;

		// we're done
		if(time > Accel.intervalInSeconds*1000)
		    {
			sensorMgr.unregisterListener(this);
			
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); 
			v.vibrate(2000);

			//this.finish();
			stopSelf();
			return;
		    }

		float mag = this.getMagnitude(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
		hasChanged(sensorEvent);

		String s = "";
		s = "t: " + time + 
		    " x: " + sensorEvent.values[0] + 
		    " y: " + sensorEvent.values[1] + 
		    " z: " + sensorEvent.values[2] +
		    " mag: " + mag +
		    "tol mag: " + prevMag;		
		
		xout.write(time + " " + sensorEvent.values[0] + "\n");
		yout.write(time + " " + sensorEvent.values[1] + "\n");
		zout.write(time + " " + sensorEvent.values[2] + "\n");
		magout.write(time + " " + mag + "\n");
		tolmagout.write(time + " " + prevMag + "\n");
			
		//text.setText(s);
	    }
	catch(IOException ex)
	    {
	    }
    }
    
    public float getMagnitude(float x, float y, float z)
    {
	return (float)Math.sqrt(x*x + y*y + z*z);
    }

    public void onAccuracyChanged(android.hardware.Sensor sensorEvent, int stuff)
    {
    }
    
    public void hasChanged(android.hardware.SensorEvent sensorEvent)
    {
	float mag = this.getMagnitude(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);	       
	    
    	if(Math.abs(mag - prevMag) > Accel.tolerance)
	    {
    		prevMag = mag;
	    }
    }

    public void onDestroy()
    {
    	//super.onStop();
    	try
    	    {
    		xout.close();
    		yout.close();
    		zout.close();
    		magout.close();
		tolmagout.close();
    	    }
    	catch(IOException ex)
    	    {
    	    }
    }

    public IBinder onBind(Intent  intent)
    {
	return null;
    }

    private SensorManager sensorMgr;
    private BufferedWriter xout;
    private BufferedWriter yout;
    private BufferedWriter zout;
    private BufferedWriter magout;
    private BufferedWriter tolmagout;

    public static long intervalInSeconds = 5;
    public static long startTime;
    public static long timeout = 0;
    public static String runId = "ID";
    //The tolerance of change
    public static double tolerance = 1.5;

    private float prevMag = 0;
    private int ID = 0;
}