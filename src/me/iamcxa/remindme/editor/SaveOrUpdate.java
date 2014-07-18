/**
 * 
 */
package me.iamcxa.remindme.editor;

import common.MyCalendar;
import common.MyCursor.TaskCursor;
import common.MyDebug;
import common.CommonVar;

import it.gmariotti.cardslib.library.internal.Card;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

/**
 * @author cxa
 * 
 */
public class SaveOrUpdate {
	SetAlarm mSetAlarm;
	Context context;
	Card card;
	Cursor cursor;
	int taskId;

	public SaveOrUpdate(Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		mSetAlarm = new SetAlarm(context);
	}

	public boolean DoTaskEditorAdding(
			String TaskField_Main,
			String TaskField_Location,
			String TaskField_Alert,
			String TaskField_Type,
			String TaskField_Other) {

		ContentValues values = new ContentValues();
		values.clear();

		// 存入任務標題
		String[] Split_TaskField_Main = TaskField_Main.split(",");
		//	String TaskField_Main=
		//		mEditorVar.Task.getTaskId()+","+			0
		//		mEditorVar.Task.getTittle()+","+			1
		//		mEditorVar.Task.getContent()+","+			2
		//		mEditorVar.Task.getCreated()+","+			3
		//		mEditorVar.Task.getDueDate();				4
		taskId=Integer.valueOf(Split_TaskField_Main[0]);
		values.put(TaskCursor.KEY.TITTLE, Split_TaskField_Main[1]);
		values.put(TaskCursor.KEY.CONTENT, Split_TaskField_Main[2]);
		values.put(TaskCursor.KEY.CREATED, Split_TaskField_Main[3]);
		// 確保DueDate欄位是數字
		if(!(Split_TaskField_Main[4].contains("null"))){
			long mDueDate=MyCalendar.getTimeMillis_From_Date((Split_TaskField_Main[4]));
			MyDebug.MakeLog(0, "mDueDate="+mDueDate);
			values.put(TaskCursor.KEY.DUE_DATE,String.valueOf(mDueDate));
		}else {
			values.put(TaskCursor.KEY.DUE_DATE,String.valueOf(Split_TaskField_Main[4]));
		}

		// 存入任務位址
		String[] Split_TaskField_Location = TaskField_Location.split(",");
		//	String TaskField_Location=
		//		mEditorVar.TaskLocation.getCoordinate()+","+	0
		//		mEditorVar.TaskLocation.getLocationName();		1
		values.put(TaskCursor.KEY.COORDINATE, Split_TaskField_Location[0]);
		values.put(TaskCursor.KEY.LOCATION_NAME, Split_TaskField_Location[1]);

		// 存入任務通知
		String[] Split_TaskField_Alert = TaskField_Alert.split(",");
		//	String TaskField_Alert=
		//		mEditorVar.TaskAlert .getALERT_Interval()+","+	0
		//		mEditorVar.TaskAlert.getAlertTime();		1
		values.put(TaskCursor.KEY.ALERT_Interval, Split_TaskField_Alert[0]);
		values.put(TaskCursor.KEY.ALERT_TIME, Split_TaskField_Alert[1]);

		//存入任務類型
		String[] Split_TaskField_Type = TaskField_Type.split(",");
		//	String TaskField_Type=
		//		mEditorVar.TaskType.getPriority()+","+		0
		//		mEditorVar.TaskType.getCategory()+","+		1	
		//		mEditorVar.TaskType.getTag()+","+			2
		//		mEditorVar.TaskType.getLevel();				3
		values.put(TaskCursor.KEY.PRIORITY, Split_TaskField_Type[0]);
		values.put(TaskCursor.KEY.CATEGORY, Split_TaskField_Type[1]);
		values.put(TaskCursor.KEY.TAG, Split_TaskField_Type[2]);
		values.put(TaskCursor.KEY.LEVEL, Split_TaskField_Type[3]);

		//存入任務類型
		String[] Split_TaskField_Other = TaskField_Other.split(",");
		//	String TaskField_Other=
		//		mEditorVar.TaskOther.getCollaborator()+","+			0
		//		mEditorVar.TaskOther.getGoogle_cal_sync_id()+","+	1
		//		mEditorVar.TaskOther.getTask_color();				2
		values.put(TaskCursor.KEY.COLLABORATOR, Split_TaskField_Other[0]);
		values.put(TaskCursor.KEY.GOOGOLE_CAL_SYNC_ID, Split_TaskField_Other[1]);
		values.put(TaskCursor.KEY.TASK_COLOR, Split_TaskField_Other[2]);

		return SaveOrUpdateIt(values, taskId);
	}

	/*****************/
	//
	// 快速輸入改這裡
	//
	// 建議建一個陣列 String[] args塞資料
	// 用arg[0]取資料可能會比較方便用DoQuickAdding
	// 2014.07.18 此部份應廢除 
	/****************/

	public boolean DoQuickAdding(int taskId, String tittle, String endDate,

			String endTime, String content, String location, String coordinate,
			String priority, String is_Fixed, String level, String is_Alarm_On) {


		ContentValues values = new ContentValues();
		//
		//		values.clear();
		//
		//		// 存入標題
		//		values.put(TaskCursor.KEY.TITTLE, tittle);
		//
		//		// save checkbox options
		//		values.put(TaskCursor.KEY.IS_FIXED, is_Fixed);
		//
		//		// save 日期/時間
		//		values.put(TaskCursor.KEY.DUE_DATE, endDate);
		//		values.put(TaskCursor.KEY.ALERT_TIME, endTime);
		//
		//		// save contents
		//		values.put(TaskCursor.KEY.CONTENT, content);
		//
		//		// save location parts
		//		values.put(TaskCursor.KEY.LOCATION_NAME, location);
		//		values.put(TaskCursor.KEY.COORDINATE, coordinate);
		//
		//		// save priority parts
		//		values.put(TaskCursor.KEY.PRIORITY, priority);
		//		values.put(TaskCursor.KEY.LEVEL, level);

		return SaveOrUpdateIt(values, taskId);

	}


	
	private boolean SaveOrUpdateIt(ContentValues values, int taskId) {
		if (taskId != 0) {
			return UpdateIt(values, taskId);
		} else {
			return SaveIt(values);
		}		
	}

	private boolean SaveIt(ContentValues values) {
		try {			
			values.put(TaskCursor.KEY.CREATED, String.valueOf(MyCalendar.getToday()));
			Uri uri = CommonVar.CONTENT_URI;
			context.getContentResolver().insert(uri, values);
			Toast.makeText(context, "新事項已經儲存", Toast.LENGTH_SHORT).show();
			mSetAlarm.SetIt(true);
			return true;
		} catch (Exception e) {
			Toast.makeText(context, "儲存出錯！", Toast.LENGTH_SHORT).show();
			MyDebug.MakeLog(0, "SaveOrUpdate SaveIt error=" + e);
			return false;
		}
	}

	private boolean UpdateIt(ContentValues values, int taskId) {
		try {
			Uri uri = ContentUris.withAppendedId(CommonVar.CONTENT_URI,
					taskId);
			context.getContentResolver().update(uri, values, null, null);
			Toast.makeText(context, "事項更新成功！", Toast.LENGTH_SHORT).show();
			mSetAlarm.SetIt(true);
			return true;
		} catch (Exception e) {
			Toast.makeText(context, "儲存出錯！", Toast.LENGTH_SHORT).show();
			MyDebug.MakeLog(0, "SaveOrUpdate UpdateIt error=" + e);
			return false;
		}
	}

}
