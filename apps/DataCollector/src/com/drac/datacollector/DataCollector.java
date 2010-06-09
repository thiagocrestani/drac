package com.drac.datacollector;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

public class DataCollector extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	Button startServiceButton = (Button)findViewById(R.id.startServiceButton);
	startServiceButton.setOnClickListener( new View.OnClickListener() {
		public void onClick(View v)
		{
		    EditText collectorId = (EditText)findViewById(R.id.collectorId);
		    EditText intervalInSecondsText = (EditText)findViewById(R.id.intervalInSecondsText);

		    CollectorService.setId(collectorId.getText().toString());
		    try
			{
			    CollectorService.setLife(Long.parseLong(intervalInSecondsText.getText().toString()) * 1000);
			    
			    // start the service
			    Intent i = new Intent();
			    i.setClassName("com.drac.datacollector", "com.drac.datacollector.CollectorService");
			    startService(i);
			}
		    catch(NumberFormatException ex)
			{
			}
		}
	    });
    }
}
