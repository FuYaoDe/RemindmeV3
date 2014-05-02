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
	public static final String debugMsgTag = "debugmsg";

	// debug msg on/off, read from Shared Preferences XML file
	public static boolean isDebugMsgOn() {
		return CommonUtils.mPreferences.getBoolean("isDebugMsgOn", true);
	}

	// isServiceOn
	public static boolean isServiceOn() {
		return CommonUtils.mPreferences.getBoolean("isServiceOn", true);
	};

	// isServiceOn
	public static boolean isSortingOn() {
		return CommonUtils.mPreferences.getBoolean("isSortingOn", true);
	};

	// isServiceOn
	public static String getUpdatePeriod(){
		return CommonUtils.mPreferences.getString("GetPriorityPeriod","5000");
	};

	private CommonUtils() {
	}

	/***********************/
	/** debug msg section **/
	/***********************/
	public static final void debugMsg(int section, String msgs) {
		if (isDebugMsgOn()) {
			switch (section) {
			case 0:
				Log.w(debugMsgTag, " " + msgs);
				break;
			case 1:
				Log.w(debugMsgTag, "thread ID=" + msgs);
				break;
			case 999:
				Log.w(debugMsgTag, "�ɶ��p�⥢��!," + msgs);
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
		
		//���ʶZ��
		public static final double GpsTolerateErrorDistance = 1.5;

	}

	// �������O
	public static final class TaskCursor implements BaseColumns {

		private TaskCursor() {
		}

		// �d�����}�C
		public static final String[] PROJECTION = new String[] {
				KeyColumns.KEY_ID, // 0
				KeyColumns.Tittle, // 1
				KeyColumns.StartDate, // 2
				KeyColumns.EndDate,// 3
				KeyColumns.StartTime, // 4
				KeyColumns.EndTime,// 5
				KeyColumns.Is_Repeat, // 6
				KeyColumns.Is_AllDay,// 7
				KeyColumns.LocationName, // 8
				KeyColumns.Coordinate,// 9
				KeyColumns.Distance,// 10
				KeyColumns.CONTENT,// 11
				KeyColumns.CREATED,// 12
				KeyColumns.Is_Alarm_ON, // 13
				KeyColumns.Is_Hide_ON,// 14
				KeyColumns.Is_PW_ON,// 15
				KeyColumns.Password,// 16
				KeyColumns.Priority,// 17
				KeyColumns.Collaborator,// 18
				KeyColumns.CalendarID,// 19
				KeyColumns.GoogleCalSyncID,// 20
				KeyColumns.Other, // 21
				KeyColumns.Level, KeyColumns.Is_Fixed };

		// �d�����}�C
		public static final String[] PROJECTION_GPS = new String[] {
				KeyColumns.KEY_ID, // 0
				KeyColumns.LocationName, // 1
				KeyColumns.Coordinate,// 2
				KeyColumns.Distance,// 3
				KeyColumns.Priority,// 4
				KeyColumns.EndDate,// 5
				KeyColumns.EndTime,// 6
		};

		public static class KeyIndex {
			public static final int KEY_ID = 0;
			public static final int Tittle = 1;
			public static final int StartDate = 2;
			public static final int EndDate = 3;
			public static final int StartTime = 4;
			public static final int EndTime = 5;
			public static final int Is_Repeat = 6;
			public static final int Is_AllDay = 7;
			public static final int LocationName = 8;
			public static final int Coordinate = 9;
			public static final int Distance = 10;
			public static final int CONTENT = 11;
			public static final int CREATED = 12;
			public static final int Is_Alarm_ON = 13;
			public static final int Is_Hide_ON = 14;
			public static final int Is_PW_ON = 15;
			public static final int Password = 16;
			public static final int Priority = 17;
			public static final int Collaborator = 18;
			public static final int CalendarID = 19;
			public static final int GoogleCalSyncID = 20;
			public static final int Other = 21;
			public static final int Level = 22;
			public static final int Is_Fixed = 23;
		}

		// ��L���`��
		public static class KeyColumns {
			// 00 index ID
			public static final String KEY_ID = "_id";
			// 01 �ݭn�P�B�� Google calender���ID
			public static final String GoogleCalSyncID = "GoogleCalSyncID";
			// 02 �ƥ���D
			public static final String Tittle = "Tittle";
			// 03 �}�l�P�����ɶ�
			public static final String StartTime = "StartTime";
			public static final String EndTime = "EndTime";
			// 04 �}�l�P�������
			public static final String StartDate = "StartDate";
			public static final String EndDate = "EndDate";
			// 05 �}�� - �ƥ�O�_����
			public static final String Is_Repeat = "Is_Repeat";
			// 06 �}�� - �O�_�����Ѩƥ�
			public static final String Is_AllDay = "Is_AllDay";
			// 07 �ƥ�a�I�]�W�١^
			public static final String LocationName = "LocationName";
			// 08 �ƥ�y��
			public static final String Coordinate = "Coordinate";
			// 09 �ƥ�P��U�ϥΪ̦a�I���Z��
			public static final String Distance = "Distance";
			// 10 ��䥻����ID(�ƥ�)
			public static final String CalendarID = "CalendarID";
			// 11 �ƥ󤺮e�]��r�^
			public static final String CONTENT = "content";
			// 12 �إ߮ɶ�
			public static final String CREATED = "created";
			// 13 �}�� - �O�_����
			public static final String Is_Alarm_ON = "Is_Alarm_ON";
			// 14 �}�� - �ƥ�O�_����
			public static final String Is_Hide_ON = "Is_Hide_ON";
			// 15 �}�� - �O�_�[�K
			public static final String Is_PW_ON = "Is_PW_ON";
			// 16 �K�X
			public static final String Password = "password";
			// 17 �Y���v��
			public static final String Priority = "Priority";
			// 18 ��@��GMAIL
			public static final String Collaborator = "Collaborator";
			// 19 �ƥ�
			public static final String Other = "other";
			// 20 �u���h��
			public static final String Level = "Level";
			// 21 �O�_�T�w���u���v
			public static final String Is_Fixed = "Is_fixed";
			// 18 �������n���ɮ׸��|
			// public static final String AlarmSoundPath = "AlarmSoundPath";
		}
	}
}
