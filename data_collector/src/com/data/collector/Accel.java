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
import android.os.SystemClock;
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
		long time = SystemClock.currentThreadTimeMillis() - Accel.startTime;
		if(time > Accel.intervalInSeconds*1000)
		    {
			return;
		    }

		String s = "";
		   s = "t: " + Long.toString(time) + 
		    " x: " + Float.toString(sensorEvent.values[0]) + 
		    " y: " + Float.toString(sensorEvent.values[1]) + 
		    " z: " + Float.toString(sensorEvent.values[2]);

		xout.write(Long.toString(time) + " " + Float.toString(sensorEvent.values[0]) + "\n");
		yout.write(Long.toString(time) + " " + Float.toString(sensorEvent.values[1]) + "\n");
		zout.write(Long.toString(time) + " " + Float.toString(sensorEvent.values[2]) + "\n");
			
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
	    }
	catch(IOException ex)
	    {
	    }
    }

    private SensorManager sensorMgr;
    private BufferedWriter xout;
    private BufferedWriter yout;
    private BufferedWriter zout;
    public static long intervalInSeconds = 0;
    public static long startTime;
    public static String runId = "";
}