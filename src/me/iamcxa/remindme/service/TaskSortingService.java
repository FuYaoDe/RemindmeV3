/**
 * 
 */
package me.iamcxa.remindme.service;

import me.iamcxa.remindme.provider.GPSManager;
import me.iamcxa.remindme.provider.LocationGetter;
import me.iamcxa.remindme.provider.PriorityCalculator;
import me.iamcxa.remindme.R;
import me.iamcxa.remindme.RemindmeMainActivity;
import me.iamcxa.remindme.RemindmeVar;
import me.iamcxa.remindme.provider.GPSCallback;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

/**
 * @author cxa
 * 
 */
public class TaskSortingService extends Service {

	private static final int notifyID = 1;
	// public static double Lat;
	// public static double Lon;
	private Handler handler = null;
	// private GPSManager gpsManager = null;
	private static boolean isGpsStrat = false;
	private String msg = null;
	private static Notification noti;
	private static PriorityCalculator UpdatePriority;
	private String timePeriod;

	private static LocationGetter UpdataLocation;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		RemindmeVar.debugMsg(0, "service onDestroy");
		UpdataLocation.CloseUpdatePriority();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		RemindmeVar.mPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		UpdataLocation = new LocationGetter(getApplicationContext());
		
		UpdataLocation.UpdatePriority();
		
		setNotification();

	}

	private void setNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nNotificationManager = (NotificationManager) getSystemService(ns);
		CharSequence tickertextr = "remindme is running";
		long when = System.currentTimeMillis();
		Intent intent = new Intent(this, RemindmeMainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.remindme_logo);
		noti = new Notification.Builder(getApplicationContext())
				.setContentTitle("remindme Task shorting service")
				.setContentText("remindme is running")
				.setSmallIcon(R.drawable.remindme_logo).setLargeIcon(bm)
				.setNumber(notifyID).setSubText(msg).setWhen(when)
				.setContentIntent(contentIntent).build();
		
		//使notifcation不被刪除
		noti.flags =Notification.FLAG_NO_CLEAR;
		
		nNotificationManager.notify(notifyID, noti);
		RemindmeVar.debugMsg(0, "service started");
	}
	
	private void Stopself(){
		
		
		if (!RemindmeVar.IS_SERVICE_ON()) {
			RemindmeVar.debugMsg(0, "service Stopself");
			this.stopSelf();
		}
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		RemindmeVar.debugMsg(0, "service onStartCommand");
		
		return super.onStartCommand(intent, flags, startId);
	}


}
