/**
 * 
 */
package me.iamcxa.remindme;

import java.text.SimpleDateFormat;
import java.util.Date;

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
public class CommonUtils {


	// ���v�`��
	public static final String AUTHORITY = "me.iamcxa.remindme";

	// URI�`��
	public static final String TASKLIST = "remindmetasklist";

	// �s��Uri
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TASKLIST);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.iamcxa"
			+ "." + TASKLIST;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.iamcxa"
			+ "." + TASKLIST;

	// �w�]�ƧǱ`��
	public static final String DEFAULT_SORT_ORDER = "created DESC";

	// �s��������
	public static final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";

	// SharedPreferences preferences;
	public static SharedPreferences mPreferences;

	// debug msg TAG
	public static final String DEBUG_MSG_TAG= "debugmsg";

	// debug msg on/off, read from Shared Preferences XML file
	public static boolean IS_DEBUG_MSG_ON() {
		return CommonUtils.mPreferences.getBoolean("isDebugMsgOn", true);
	}

	// IS_SERVICE_ON - �I���A�ȶ}��
	public static boolean IS_SERVICE_ON() {
		return CommonUtils.mPreferences.getBoolean("isServiceOn", true);
	};

	// IS_SORTING_ON - ���z�u���}��
	public static boolean IS_SORTING_ON() {
		return CommonUtils.mPreferences.getBoolean("isSortingOn", true);
	};

	// getUpdatePeriod - ��s���j
	public static String getUpdatePeriod() {
		return CommonUtils.mPreferences.getString("GetPriorityPeriod", "5000");
	};
	
	private CommonUtils() {
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
				Log.w(DEBUG_MSG_TAG, "�ɶ��p�⥢��!," + msgs);
				break;
			default:
				break;
			}
		}

	}

	/***********************/
	/** getDaysLeft **/
	/***********************/
	@SuppressLint("SimpleDateFormat")
	public static long getDaysLeft(String TaskDate, int Option) {

		// �w�q�ɶ��榡
		// java.text.SimpleDateFormat sdf = new
		SimpleDateFormat sdf = null;
		if (Option == 1) {
			sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		} else if (Option == 2) {
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		}

		// ���o�{�b�ɶ�
		Date now = new Date();
		String nowDate = sdf.format(now);
		debugMsg(0, "now:" + nowDate + ", task:" + TaskDate);
		try {
			// ���o�ƥ�ɶ��P�{�b�ɶ�
			Date dt1 = sdf.parse(nowDate);
			Date dt2 = sdf.parse(TaskDate);

			// ���o��Ӯɶ���Unix�ɶ�
			Long ut1 = dt1.getTime();
			Long ut2 = dt2.getTime();

			Long timeP = ut2 - ut1;// �@��t
			// �۴���o��Ӯɶ��t�Z���@��
			// Long sec = timeP / 1000;// ��t
			// Long min = timeP / 1000 * 60;// ���t
			// Long hr = timeP / 1000 * 60 * 60;// �ɮt
			Long day = timeP / (1000 * 60 * 60 * 24);// ��t
			debugMsg(0, "Get days left Sucessed! " + day);
			return day;
		} catch (Exception e) {
			// TODO: handle exception
			debugMsg(999, e.toString());
			return -1;
		}

	}

	public static class GpsSetting {
		// GPS�W���������wifi
		public static final int TIMEOUT_SEC = 5;
		// Gps���A
		public static boolean GpsStatus = false;

		// ���ʶZ��
		public static final double GpsTolerateErrorDistance = 1.5;

	}

	// �������O
	public static final class TaskCursor implements BaseColumns {

		private TaskCursor() {
		}

		// �d�����}�C
		public static final String[] PROJECTION = new String[] { KEY.KEY_ID, // 0
				KEY.TITTLE, // 1
				KEY.END_DATE,// 3
				KEY.END_TIME,// 5
				KEY.IS_REPERT, // 6
				KEY.LOCATION_NAME, // 8
				KEY.COORDINATE,// 9
				KEY.DISTANCE,// 10
				KEY.CONTENT,// 11
				KEY.CREATED,// 12
				KEY.PRIORITY,// 17
				KEY.COLLABORATOR,// 18
				KEY.CAL_ID,// 19
				KEY.GOOGOLE_CAL_SYNC_ID,// 20
				KEY.OTHER, // 21
				KEY.LEVEL, KEY.IS_FIXED };

		// �d�����}�C
		public static final String[] PROJECTION_GPS = new String[] {
				KEY.KEY_ID, // 0
				KEY.LOCATION_NAME, // 1
				KEY.COORDINATE,// 2
				KEY.DISTANCE,// 3
				KEY.PRIORITY,// 4
				KEY.END_DATE,// 5
				KEY.END_TIME,// 6
				KEY.CREATED,// 6
		};

		public static class KEY_INDEX {
			public static final int KEY_ID = 0;
			public static final int TITTLE = 1;
			public static final int CREATED = 2;
			public static final int END_DATE = 3;
			public static final int END_TIME = 4;
			public static final int CONTENT = 5;
			public static final int LOCATION_NAME = 6;
			public static final int COORDINATE = 7;
			public static final int DISTANCE = 8;
			public static final int LEVEL = 9;
			public static final int PRIORITY = 10;
			public static final int COLLABORATOR = 11;
			public static final int CAL_ID = 12;
			public static final int GOOGOLE_CAL_SYNC_ID = 13;
			public static final int OTHER = 14;
			public static final int IS_FIXED = 15;
			public static final int IS_REPEAT = 16;
		}

		// ��L���`��
		public static class KEY {
			public static final String KEY_ID = "_id";
			public static final String TITTLE = "TITTLE";
			public static final String CREATED = "CREATED";
			public static final String END_TIME = "END_TIME";
			public static final String END_DATE = "END_DATE";
			public static final String CONTENT = "CONTENT";
			public static final String LOCATION_NAME = "LOCATION_NAME";
			public static final String COORDINATE = "COORDINATE";
			public static final String DISTANCE = "DISTANCE";
			public static final String LEVEL = "LEVEL";
			public static final String PRIORITY = "PRIORITY";
			public static final String COLLABORATOR = "COLLABORATOR";
			public static final String CAL_ID = "CAL_ID";
			public static final String GOOGOLE_CAL_SYNC_ID = "GOOGOLE_CAL_SYNC_ID";
			public static final String OTHER = "OTHER";
			public static final String IS_FIXED = "IS_FIXED";;
			public static final String IS_REPERT = "IS_REPERT";
		}
	}
}
