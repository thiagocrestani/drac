package com.data.collector;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.*;
import java.io.*;

public class DataCollector extends Activity implements SensorEventListener
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	TextView text = (TextView) findViewById(R.id.text);
	text.setText("hi");
    }

    public void onStart()
    {
	super.onStart();
	this.sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE);

	boolean accelSupported = sensorMgr.registerListener(this,
							    this.sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							    SensorManager.SENSOR_DELAY_UI,
							    new Handler());

	TextView text = (TextView) findViewById(R.id.text);
	if(accelSupported)
	    text.setText("true");
	else
	    text.setText("false");
    }

    public void onSensorChanged(android.hardware.SensorEvent sensorEvent)
    {	
	FileOutputStream file = null;
	TextView text = (TextView) findViewById(R.id.text);

	try
	    {
		file = openFileOutput("data", Context.MODE_WORLD_READABLE);
		String s = "";
		for(int i = 0; i < sensorEvent.values.length; ++i)
		    {
			s += " " + Float.toString(sensorEvent.values[i]);
			file.write("Test".getBytes());
		    }
		text.setText(s);
		file.close();
	    }
	catch(IOException ex)
	    {
	    }
    }
    
    public void onAccuracyChanged(android.hardware.Sensor sensorEvent, int stuff)
    {
    }

    private SensorManager sensorMgr;
}
