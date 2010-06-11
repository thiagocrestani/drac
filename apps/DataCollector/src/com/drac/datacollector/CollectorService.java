package com.drac.datacollector;

import android.app.Service;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.hardware.SensorManager;

import java.util.Timer;
import java.util.TimerTask;

// a TimerTask to stop the service after its life has ran out
class StopService extends TimerTask
{
    public StopService(Service service)
    {
	this.service = service;
    }

    public void run()
    {
	service.stopSelf();
    }

    private Service service;
}

public class CollectorService extends Service
{
    public void onCreate()
    {
	// -- start timer
	Timer timer = new Timer();
	// schedual stopping this service
	timer.schedule(new StopService(this), CollectorService.life);

	// -- set up listeners
	sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

	// get the time at which the service starts
	this.birth = System.currentTimeMillis();

	accel.setStartTime(this.birth);
	compass.setStartTime(this.birth);
	mf.setStartTime(this.birth);

	// register sensor listeners
	accel.register(sensorManager);
	compass.register(sensorManager);
	mf.register(sensorManager);

	// -- notifications
	this.mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	showNotifcations();

	this.setForeground(true);
    }

    public IBinder onBind(Intent intent)
    {
	return null;
    }

    public void onDestroy()
    {
	this.sensorManager.unregisterListener(accel);
	this.sensorManager.unregisterListener(compass);
	this.sensorManager.unregisterListener(mf);
	accel.onStop();
	compass.onStop();
	mf.onStop();
	this.mNM.cancelAll();
    }
    
    private void showNotifcations()
    {
	Notification notification = new Notification(R.drawable.star, "Collector Service Started", System.currentTimeMillis());
     
	Intent notificationIntent = new Intent(this, DataCollector.class);
	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

	notification.setLatestEventInfo(this, "Collector Service Started", "Running for " + this.life + " milliseconds", contentIntent);
	
	this.mNM.notify(0, notification);
    }

    // -- accessor methods
    public static void setLife(long life)
    {
	CollectorService.life = life;
    }

    public static void setId(String id)
    {
	CollectorService.id = id;
    }

    public static String getId()
    {
	return CollectorService.id;
    }

    // time of birth
    private long birth = 0;
    // time in milliseconds to run
    private static long life = 0;
    // an id
    private static String id = "collector";
    
    // SensorEventListeners
    private Accelerometer accel = new Accelerometer();
    private MagneticField mf = new MagneticField();
    private Compass compass = new Compass();

    // notifications
    private NotificationManager mNM;

    private SensorManager sensorManager;
}