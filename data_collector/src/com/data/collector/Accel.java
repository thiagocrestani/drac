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
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.*;
import java.io.*;

public class Accel extends Activity implements SensorEventListener
{
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.accel);
    }
    
    public void onStart()
    {
	super.onStart();

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

	TextView text = (TextView) findViewById(R.id.text);

	try
	    {
		xout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "x_" + Accel.runId + ".dat")));
		yout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "y_" + Accel.runId + ".dat")));
		zout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "z_" + Accel.runId + ".dat")));	
		avout = new BufferedWriter(new FileWriter(new File(android.os.Environment.getExternalStorageDirectory(), "av_" + Accel.runId + ".dat")));     
	    }
	catch(IOException ex)
	    {
	    }
    }

    public void onSensorChanged(android.hardware.SensorEvent sensorEvent)
    {	
	TextView text = (TextView) findViewById(R.id.text);

	try
	    {
		long time = System.currentTimeMillis() - Accel.startTime;

		// we're done
		if(time > Accel.intervalInSeconds*1000)
		    {
			try
			    {
				xout.close();
				yout.close();
				zout.close();
				avout.close();
			    }
			catch(IOException ex)
			    {
			    }

			sensorMgr.unregisterListener(this);

			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); 
			v.vibrate(3000);

			this.finish();
			return;
		    }

		float av = (sensorEvent.values[0] + sensorEvent.values[1] + sensorEvent.values[2])/(float)3;

		String s = "";
		s = "t: " + Long.toString(time) + 
		    " x: " + Float.toString(sensorEvent.values[0]) + 
		    " y: " + Float.toString(sensorEvent.values[1]) + 
		    " z: " + Float.toString(sensorEvent.values[2]) +
		    " av: " + Float.toString(av);
		
		xout.write(Long.toString(time) + " " + Float.toString(sensorEvent.values[0]) + "\n");
		yout.write(Long.toString(time) + " " + Float.toString(sensorEvent.values[1]) + "\n");
		zout.write(Long.toString(time) + " " + Float.toString(sensorEvent.values[2]) + "\n");
		avout.write(Long.toString(time) + " " + Float.toString(av) + "\n");
			
		text.setText(s);
	    }
	catch(IOException ex)
	    {
	    }
    }
    
    public void onAccuracyChanged(android.hardware.Sensor sensorEvent, int stuff)
    {
    }
    
    public void onStop()
    {
    	super.onStop();
    	try
    	    {
    		xout.close();
    		yout.close();
    		zout.close();
    		avout.close();
    	    }
    	catch(IOException ex)
    	    {
    	    }
    }

    private SensorManager sensorMgr;
    private BufferedWriter xout;
    private BufferedWriter yout;
    private BufferedWriter zout;
    private BufferedWriter avout;
    public static long intervalInSeconds = 0;
    public static long startTime;
    public static long timeout = 0;
    public static String runId = "";
}