/**
 * 
 */
package common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author cxa
 * 
 */
public class CommonMethod {

	// SharedPreferences preferences;
	public static SharedPreferences mPreferences;

	// debug msg TAG
	public static final String DEBUG_MSG_TAG= "debugmsg";

	// debug msg on/off, read from Shared Preferences XML file
	public static boolean IS_DEBUG_MSG_ON() {
		return CommonMethod.mPreferences.getBoolean("isDebugMsgOn", true);
	}

	// isServiceOn
	public static boolean IS_SERVICE_ON() {
		return CommonMethod.mPreferences.getBoolean("isServiceOn", true);
	};

	// isServiceOn
	public static boolean IS_SORTING_ON() {
		return CommonMethod.mPreferences.getBoolean("isSortingOn", true);
	};

	// isServiceOn
	public static String getUpdatePeriod() {
		return CommonMethod.mPreferences.getString("GetPriorityPeriod", "5000");
	};

	private CommonMethod() {
	}

	/***********************/
	/** debug msg section **/
	/***********************/
	public static final void debugMsg(int section, String msgs) {
		if (IS_DEBUG_MSG_ON()) {
			switch (section) {
			case 0:
				Log.w(DEBUG_MSG_TAG, " " + msgs);
				break;
			case 1:
				Log.w(DEBUG_MSG_TAG, "thread ID=" + msgs);
				break;
			case 999:
				Log.w(DEBUG_MSG_TAG, "時間計算失敗!," + msgs);
				break;
			default:
				break;
			}
		}

	}

	/***********************/
	/** getDaysLeft **/
	/***********************/
	public static long getDaysLeft(String TaskDate, int Option) {

		// 定義時間格式
		// java.text.SimpleDateFormat sdf = new
		SimpleDateFormat sdf = null;
		if (Option == 1) {
			sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.TAIWAN);
		} else if (Option == 2) {
			sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN);
		}

		// 取得現在時間
		Date now = new Date();
		String nowDate = sdf.format(now);
		debugMsg(0, "now:" + nowDate + ", task:" + TaskDate);
		try {
			// 取得事件時間與現在時間
			Date dt1 = sdf.parse(nowDate);
			//Date dt2 = sdf.parse(TaskDate);

			// 取得兩個時間的Unix時間
			Long ut1 = dt1.getTime();
			//Long ut2 = dt2.getTime();

			Long timeP = Long.valueOf(TaskDate) - ut1;// 毫秒差
			// 相減獲得兩個時間差距的毫秒
			// Long sec = timeP / 1000;// 秒差
			// Long min = timeP / 1000 * 60;// 分差
			// Long hr = timeP / 1000 * 60 * 60;// 時差
			Long day = timeP / (1000 * 60 * 60 * 24);// 日差
			debugMsg(0, "Get days left Sucessed! " + day);
			return day;
		} catch (Exception e) {
			// TODO: handle exception
			debugMsg(999, e.toString());
			return -1;
		}

	}

	public static long getNextFewDays(int Days) {
		long NextFewDays=System.currentTimeMillis()+( 60 * 60 * 24 * 1000 * Days);//N天的毫秒數;
		return NextFewDays;

	}

	public static long getToday() {
		long curDate=System.currentTimeMillis();
		return curDate;
	}



	public static String getCalendarToday(int ExtraDays) {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, ExtraDays);
		int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
		String month = String.valueOf(today.get(Calendar.MONTH)+1);
		int year=today.get(Calendar.YEAR);
		if (String.valueOf(month).length()==1){
			month="0"+month;
		}

		return year+"/"+month+"/"+dayOfMonth;
	}

	public static class GpsSetting {
		// GPS超時關閉改用wifi
		public static final int TIMEOUT_SEC = 5;
		// Gps狀態
		public static boolean GpsStatus = false;

		// 移動距離
		public static final double GpsTolerateErrorDistance = 1.5;

	}

	// 內部類別
	public static final class TaskCursor implements BaseColumns {

		private TaskCursor() {
		}

		// 查詢欄位陣列
		public static final String[] PROJECTION = new String[] { 
			KEY._ID ,
			//主要內容
			KEY.TITTLE ,
			KEY.CONTENT ,
			KEY.CREATED ,
			KEY.DUE_DATE ,
			//提醒
			KEY.ALERT_Interval ,
			KEY.ALERT_TIME ,
			//位置
			KEY.LOCATION_NAME ,
			KEY.COORDINATE ,
			KEY.DISTANCE ,
			//分類,標籤與優先
			KEY.CATEGORY ,
			KEY.PRIORITY ,
			KEY.TAG ,
			KEY.LEVEL ,
			//其他
			KEY.COLLABORATOR ,
			KEY.GOOGOLE_CAL_SYNC_ID ,
			KEY.TASK_COLOR ,};

		// 查詢欄位陣列
		public static final String[] PROJECTION_GPS = new String[] {
			KEY._ID, // 0
			KEY.LOCATION_NAME, // 1
			KEY.COORDINATE,// 2
			KEY.DISTANCE,// 3
			KEY.PRIORITY,// 4
			KEY.DUE_DATE,// 5
			KEY.ALERT_TIME,// 6
			KEY.CREATED,// 6
		};

		public static class KEY_INDEX {
			public static final int KEY_ID = 0;
			//主要內容
			public static final int TITTLE = 1;
			public static final int CONTENT = 2;
			public static final int CREATED = 3;
			public static final int DUE_DATE = 4;
			//提醒
			public static final int ALERT_Interval = 5;
			public static final int ALERT_TIME = 6;
			//位置
			public static final int LOCATION_NAME = 7;
			public static final int COORDINATE = 8;
			public static final int DISTANCE = 9;
			//分類,標籤與優先
			public static final int CATEGORY = 10;
			public static final int PRIORITY = 11;
			public static final int TAG = 12;
			public static final int LEVEL = 13;
			//其他
			public static final int COLLABORATOR = 14;
			public static final int GOOGOLE_CAL_SYNC_ID = 15;
			public static final int TASK_COLOR = 16;
		}

		// 其他欄位常數
		public static class KEY {

			public static final String _Bundle = "Bundle"; 

			public static final String _ID = "_id";
			//主要內容
			public static final String TITTLE = "TITTLE";
			public static final String CONTENT = "CONTENT";
			public static final String CREATED = "CREATED";
			public static final String DUE_DATE = "DUE_DATE";
			//提醒
			public static final String ALERT_Interval = "ALERT_Interval";
			public static final String ALERT_TIME = "ALERT_TIME";
			//位置
			public static final String LOCATION_NAME = "LOCATION_NAME";
			public static final String COORDINATE = "COORDINATE";
			public static final String DISTANCE = "DISTANCE";
			//分類,標籤與優先
			public static final String CATEGORY = "CATEGORY";
			public static final String PRIORITY = "PRIORITY";
			public static final String TAG = "TAG";
			public static final String LEVEL = "LEVEL";
			//其他
			public static final String COLLABORATOR = "COLLABORATOR";
			public static final String GOOGOLE_CAL_SYNC_ID = "GOOGOLE_CAL_SYNC_ID";
			public static final String TASK_COLOR = "TASK_COLOR";

		}
	}
}