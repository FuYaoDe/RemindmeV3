package me.iamcxa.remindme.provider;

import me.iamcxa.remindme.CommonUtils;
import android.content.Context;
import android.location.Location;
import android.os.Handler;

public class LocationGetter implements GPSCallback {
	public double Lat;
	public double Lon;
	public double Speed;
	public GPSManager gpsManager = null;
	public static boolean isGpsStrat = false;
	private Context context;
	private Handler handler = null;
	private static PriorityCalculator UpdatePriority;
	private String updatePeriod;
	private boolean UseOnceTime;
	private static String timePeriod;
	private static boolean isSortingOn;

	@Override
	public void onGPSUpdate(Location location) {
		// TODO Auto-generated method stub
		Lat = location.getLatitude();
		Lon = location.getLongitude();
		Speed = location.getSpeed();
		CommonUtils.debugMsg(0, "LocationProvider onGPSUpdate");
	}

	public LocationGetter(Context context) {
		this.context = context;
		gpsManager = new GPSManager();
	}

	public void stopListening() {
		gpsManager.stopListening();
		gpsManager.setGPSCallback(null);
		isGpsStrat = false;
		CommonUtils.debugMsg(0, "LocationProvider stopListening");
	}

	public void startNetWorkListening(GPSCallback gpsCallBack) {
		gpsManager.startNetWorkListening(context);
		gpsManager.setGPSCallback(gpsCallBack);
		isGpsStrat = true;
		CommonUtils.debugMsg(0, "LocationProvider startNetWorkListening");
	}

	public void startGpsListening(GPSCallback gpsCallBack) {
		gpsManager.startGpsListening(context);
		gpsManager.setGPSCallback(gpsCallBack);
		isGpsStrat = true;
		CommonUtils.debugMsg(0, "LocationProvider startNetWorkListening");
	}

	public boolean isLocationGet() {
		if (Lon != 0 && Lat != 0) {
			return true;
		} else {
			return false;
		}
	}

	public void UpdatePriority() {
		handler = new Handler();
		UpdatePriority = new PriorityCalculator(context);

		updatePeriod = CommonUtils.getUpdatePeriod();

		handler.postDelayed(GpsTime, Long.parseLong(updatePeriod));
		UseOnceTime = false;
	}

	public void UpdateOncePriority() {
		handler = new Handler();
		UpdatePriority = new PriorityCalculator(context);
		handler.post(GpsTime);
		UseOnceTime = true;
	}

	public void CloseUpdatePriority() {
		handler.removeCallbacks(GpsTime);
	}

	public static boolean getIsSortingOn() {
		return isSortingOn;
	}

	public static void setIsSortingOn(boolean isSortingOn) {
		LocationGetter.isSortingOn = isSortingOn;
	}

	public static String getTimePeriod() {
		return timePeriod;
	}

	public static void setTimePeriod(String timePeriod) {
		LocationGetter.timePeriod = timePeriod;
	}

	public  double CompareLastDistance(double Lat,double Lon){
		return DistanceCalculator.haversine(
				gpsManager.LastLocation().getLatitude(),
				gpsManager.LastLocation().getLongitude(),
				Lat, 
				Lon);
	}
	private Runnable GpsTime = new Runnable() {
		@Override
		public void run() {

			CommonUtils.debugMsg(0, "service GpsTime start");

			setIsSortingOn(CommonUtils.isSortingOn());			

			CommonUtils.debugMsg(0, "service preferance isSortingOn="+getIsSortingOn());

			if (getIsSortingOn()) {

				if (isLocationGet()) {
					stopListening();
					CommonUtils.debugMsg(0, "����W���Z��:"+CompareLastDistance(Lat,Lon));
					if(CompareLastDistance(Lat,Lon)>CommonUtils.GpsSetting.GpsTolerateErrorDistance){
						CommonUtils.debugMsg(0, "��s�v��");
						UpdatePriority.SetLatLng(Lat, Lon);
						UpdatePriority.ProcessData(UpdatePriority.loadData());
					}
					if (UseOnceTime) {
						CloseUpdatePriority();
					} else {
						updatePeriod = CommonUtils.mPreferences.getString(
								"GetPriorityPeriod", "5000");
						handler.postDelayed(this, Long.parseLong(updatePeriod));
					}

				} else {
					if (isGpsStrat) {
						handler.postDelayed(this, 1000);
						CommonUtils.debugMsg(0, "�w�g�}��GPS���O�٨S������:" + Lat + ","
								+ Lon);
					} else {
						startNetWorkListening(LocationGetter.this);
						handler.postDelayed(this, 1000);
						// make log
						CommonUtils.debugMsg(0, "�}��GPS:" + Lat + "," + Lon);
					}
				}

			}else{

				CommonUtils.debugMsg(0, "service GpsTime stop because isSortingOn=False");

			}
		}
	};

}
