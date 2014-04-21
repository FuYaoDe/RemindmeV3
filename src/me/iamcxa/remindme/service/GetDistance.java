/**
 * 
 */
package me.iamcxa.remindme.service;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.iamcxa.remindme.CommonUtils;
import me.iamcxa.remindme.R;
import me.iamcxa.remindme.CommonUtils.RemindmeTaskCursor;
import me.iamcxa.remindme.provider.DistanceProvider;
import me.iamcxa.remindme.provider.TaskDBProvider;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author cxa
 * 
 */
public class GetDistance{

	private static double Lat;
	private static double Lon;
	public static String[] projection = RemindmeTaskCursor.PROJECTION_GPS;
	public static String selection = RemindmeTaskCursor.KeyColumns.Coordinates
			+ " <> \"\" AND " + RemindmeTaskCursor.KeyColumns.Coordinates
			+ " NOT LIKE \"null%\"";
	public static String sortOrder = RemindmeTaskCursor._ID;
	public static String[] selectionArgs;
	// private ArrayList<String> coordinatesList = new ArrayList<String>();
	// private ArrayList<String> distanceList = new ArrayList<String>();
	// private ArrayList<String> newdistanceList = new ArrayList<String>();
	// private ArrayList<String> idList = new ArrayList<String>();
	// private ArrayList<String> WeightsList = new ArrayList<String>();

	private int newPriorityWeight;
	private String endDate, endTime;
	private Context context;
    private static TaskDBProvider load;
	
	GetDistance(Context context){
		this.context=context;
		load = new TaskDBProvider();
	}
	
	public Cursor loadData() {

		Cursor Data = load.query( CommonUtils.CONTENT_URI, projection,
				selection, selectionArgs, sortOrder);
		return Data;
		
	}

	public void ProcessData(Cursor data) {

		CommonUtils.debugMsg(0, "GetDistance onLoadFinished");
		int i = 0;
		double Distance;
		long dayLeft;
		data.moveToFirst();
		for (i = 0; i < data.getCount(); i++) {
			CommonUtils.debugMsg(0, "GetDistance data.move@" + i);
			// idList.add("" +
			// data.getInt(RemindmeTaskCursor.IndexColumns.KEY_ID));
			// WeightsList.add("" + data.getInt(4));

			CommonUtils.debugMsg(0, "GetDistance LatLon1=" + data.getString(2)
					+ " / LatLon2=" + Lat + "," + Lon);
			Distance = DistanceProvider.Distance(data.getString(2), Lat, Lon);
			endDate = data.getString(5);
			endTime = data.getString(6);
			dayLeft = CommonUtils.getDaysLeft(endDate + " " + endTime, 1);

			CommonUtils.debugMsg(0, "GetDistance dayLeft org=" + dayLeft);
			CommonUtils.debugMsg(0, "GetDistance endDate=" + endDate);
			CommonUtils.debugMsg(0, "GetDistance endTime=" + endTime);
			CommonUtils.debugMsg(0, "GetDistance Distance=" + Distance);
			saveOrUpdate(
					Integer.valueOf(data.getInt(0)),
					getNewWeight(i, Integer.valueOf(data.getInt(4)), Distance,
							dayLeft), Distance);
			if (!data.isLast())
				data.moveToNext();

		}
	}

	// if(10km> �Z�� >3km) �v���C�� 1km+150.�C
	// if(4km> �Z�� >1km) �v���C�� 1km+250
	// if(2km> �Z�� >1km) �v���C�� 1km+400�C
	// if(0.5km> �Z�� >0.1km) �v���C�� 1km+800
	// if(48> �ɶ� >23) �v���C�֤@�p�� +200
	// if(24> �ɶ� >11) �v���C�֤@�p�� +250
	// if(12> �ɶ� >6) �v���C�֤@�p�� +300
	// if(7> �ɶ� >3) �v���C�֤@�p�� +350
	// if(4> �ɶ� >2) �v���C�֤@�p�� +550
	// if(3> �ɶ� >0) �v���C�֤@�p�� +1000

	public int getNewWeight(int i, int oldWeights, Double Distance, long dayLeft) {
		int newPriorityWeight = oldWeights;
		dayLeft = (dayLeft) * 24;
		//Distance *= 10;
		CommonUtils.debugMsg(0, "GetDistance dayLeft=" + dayLeft);
		// �ɶ�
//		if ((dayLeft>168)) {
//			newPriorityWeight *= 0.25;
//		} else if ((168 > dayLeft) && (dayLeft > 119)) {
//			newPriorityWeight *= 0.5;
//		} else if ((120 > dayLeft) && (dayLeft > 47)) {
//			newPriorityWeight -= 50;
//		} else if ((48 > dayLeft) && (dayLeft > 23)) {
//			newPriorityWeight -= 20;
//		} else if ((24 > dayLeft) && (dayLeft > 11)) {
//			newPriorityWeight += 5;
//		} else if ((12 > dayLeft) && (dayLeft > 6)) {
//			newPriorityWeight += 30;
//		}

		// �Z��
		if ((Distance >= 24)) {
			newPriorityWeight *= 0.78;
		} else if ((24 > Distance) && (Distance > 4)) {
			newPriorityWeight *= 0.92;
		} else if ((5 > Distance) && (Distance > 1.9)) {
			newPriorityWeight *= 0.95;
		} else if ((2 > Distance) && (Distance > 0.4)) {
			newPriorityWeight *= 1.01;
		} else if ((0.5 > Distance) && (Distance > 0.1)) {
			newPriorityWeight *= 1.15;
		}
//		if ((Distance >= 15)) {
//			newPriorityWeight *= 0.88;
//		} else {
//			newPriorityWeight *= 1.15;
//	}
//		
		//newPriorityWeight = newPriorityWeight + oldWeights;
//		
	//	oldWeights+=newPriorityWeight ;
		CommonUtils
				.debugMsg(0, "GetDistance getNewWeight=" + newPriorityWeight);
		return newPriorityWeight+1;   //+1�����k0
	}

	// �x�s�έק�Ƨѿ���T
	public boolean saveOrUpdate(int ID, int newPriorityWeight, Double Distance) {
		try {
			ContentValues values = new ContentValues();

			DecimalFormat df = new DecimalFormat("##.00");
			Distance = Double.parseDouble(df.format(Distance));

			values.clear();

			values.put(RemindmeTaskCursor.KeyColumns.Distance,
					((Distance)));
			values.put(RemindmeTaskCursor.KeyColumns.PriorityWeight,
					newPriorityWeight);

			// �ק�
			Uri uri = ContentUris.withAppendedId(CommonUtils.CONTENT_URI, ID);
			load.update(uri, values, null, null);
			// Toast.makeText(this, "�ƶ���s���\�I" + curDate.toString(),
			// Toast.LENGTH_SHORT).show();

			CommonUtils.debugMsg(0, "GetDistance newPriorityWeight��s���\�I");
			return true;
		} catch (Exception e) {
			Toast.makeText(context, "GetDistance �x�s�X���I",
					Toast.LENGTH_SHORT).show();
			return false;
		}

	}
	
	public void SetLatLng(double Lat,Double Lon){
		this.Lat=Lat;
		this.Lon=Lon;
	}
}
