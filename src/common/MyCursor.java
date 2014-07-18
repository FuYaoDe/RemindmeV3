package common;

import android.provider.BaseColumns;

public class MyCursor {
	
	private MyCursor(){}
	
	// �������O
		public static final class TaskCursor implements BaseColumns {

			private TaskCursor() {
			}

			// �d�����}�C
			public static final String[] PROJECTION = new String[] { 
				KEY._ID ,
				//�D�n���e
				KEY.TITTLE ,
				KEY.CONTENT ,
				KEY.CREATED ,
				KEY.DUE_DATE ,
				//����
				KEY.ALERT_Interval ,
				KEY.ALERT_TIME ,
				//��m
				KEY.LOCATION_NAME ,
				KEY.COORDINATE ,
				KEY.DISTANCE ,
				//����,���һP�u��
				KEY.CATEGORY ,
				KEY.PRIORITY ,
				KEY.TAG ,
				KEY.LEVEL ,
				//��L
				KEY.COLLABORATOR ,
				KEY.GOOGOLE_CAL_SYNC_ID ,
				KEY.TASK_COLOR ,};

			// �d�����}�C
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
				//�D�n���e
				public static final int TITTLE = 1;
				public static final int CONTENT = 2;
				public static final int CREATED = 3;
				public static final int DUE_DATE = 4;
				//����
				public static final int ALERT_Interval = 5;
				public static final int ALERT_TIME = 6;
				//��m
				public static final int LOCATION_NAME = 7;
				public static final int COORDINATE = 8;
				public static final int DISTANCE = 9;
				//����,���һP�u��
				public static final int CATEGORY = 10;
				public static final int PRIORITY = 11;
				public static final int TAG = 12;
				public static final int LEVEL = 13;
				//��L
				public static final int COLLABORATOR = 14;
				public static final int GOOGOLE_CAL_SYNC_ID = 15;
				public static final int TASK_COLOR = 16;
			}

			// ��L���`��
			public static class KEY {


				public static final String _ID = "_id";
				//�D�n���e
				public static final String TITTLE = "TITTLE";
				public static final String CONTENT = "CONTENT";
				public static final String CREATED = "CREATED";
				public static final String DUE_DATE = "DUE_DATE";
				//����
				public static final String ALERT_Interval = "ALERT_Interval";
				public static final String ALERT_TIME = "ALERT_TIME";
				//��m
				public static final String LOCATION_NAME = "LOCATION_NAME";
				public static final String COORDINATE = "COORDINATE";
				public static final String DISTANCE = "DISTANCE";
				//����,���һP�u��
				public static final String CATEGORY = "CATEGORY";
				public static final String PRIORITY = "PRIORITY";
				public static final String TAG = "TAG";
				public static final String LEVEL = "LEVEL";
				//��L
				public static final String COLLABORATOR = "COLLABORATOR";
				public static final String GOOGOLE_CAL_SYNC_ID = "GOOGOLE_CAL_SYNC_ID";
				public static final String TASK_COLOR = "TASK_COLOR";

			}
		}
}
