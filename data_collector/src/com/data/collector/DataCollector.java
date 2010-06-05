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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.*;
import java.io.*;
import android.os.SystemClock;

public class DataCollector extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	Button startButton = (Button)findViewById(R.id.startButton);
	
	startButton.setOnClickListener(new View.OnClickListener() 
	    {
		public void onClick(View v) 
		{					 
		    try
			{
			    EditText intervalInSecondsText = (EditText)findViewById(R.id.intervalInSecondsText);
			    EditText runId = (EditText)findViewById(R.id.runId);
		
			    Accel.intervalInSeconds = Long.parseLong(intervalInSecondsText.getText().toString());
			    Accel.runId = runId.getText().toString();

			    Intent i = new Intent();
			    i.setClassName("com.drac.datacollector", "com.drac.datacollector.Accel");
			    startActivity(i);
			    Accel.startTime = SystemClock.currentThreadTimeMillis();
			}
		    catch(NumberFormatException ex)
			{
			}
		}
	    });
    }
}
