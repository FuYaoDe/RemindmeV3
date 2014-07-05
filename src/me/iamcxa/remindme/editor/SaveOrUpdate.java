/**
 * 
 */
package me.iamcxa.remindme.editor;

import java.util.Date;

import me.iamcxa.remindme.RemindmeVar;
import me.iamcxa.remindme.RemindmeVar.TaskCursor;

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

	public SaveOrUpdate(Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		mSetAlarm = new SetAlarm(context);
	}

	public boolean DoTaskEditorAdding(int taskId, String tittle,
			String endDate, String endTime, String content, String location,
			String coordinate, String priority, String is_Fixed, String level) {


		ContentValues values = new ContentValues();

		values.clear();

		// �s�J���D
		values.put(TaskCursor.KEY.TITTLE, tittle);

		// save checkbox options
		values.put(TaskCursor.KEY.IS_FIXED, is_Fixed);

		// save ���/�ɶ�
		values.put(TaskCursor.KEY.END_DATE, endDate);
		values.put(TaskCursor.KEY.END_TIME, endTime);

		// save contents
		values.put(TaskCursor.KEY.CONTENT, content);

		// save location parts
		values.put(TaskCursor.KEY.LOCATION_NAME, location);
		values.put(TaskCursor.KEY.COORDINATE, coordinate);

		// save priority parts
		values.put(TaskCursor.KEY.PRIORITY, priority);
		values.put(TaskCursor.KEY.LEVEL, level);


			return SaveOrUpdateIt(values, taskId);
	
	}

	/*****************/
	//
	// �ֳt��J��o��
	//
	// ��ĳ�ؤ@�Ӱ}�C String[] args����
	// ��arg[0]����ƥi��|�����K��DoQuickAdding
	/****************/

	public boolean DoQuickAdding(int taskId, String tittle, String endDate,
			String endTime, String content, String location, String coordinate,
			String priority, String is_Fixed, String level, String is_Alarm_On) {


		ContentValues values = new ContentValues();

		values.clear();

		// �s�J���D
		values.put(TaskCursor.KEY.TITTLE, tittle);

		// save checkbox options
		values.put(TaskCursor.KEY.IS_FIXED, is_Fixed);

		// save ���/�ɶ�
		values.put(TaskCursor.KEY.END_DATE, endDate);
		values.put(TaskCursor.KEY.END_TIME, endTime);

		// save contents
		values.put(TaskCursor.KEY.CONTENT, content);

		// save location parts
		values.put(TaskCursor.KEY.LOCATION_NAME, location);
		values.put(TaskCursor.KEY.COORDINATE, coordinate);

		// save priority parts
		values.put(TaskCursor.KEY.PRIORITY, priority);
		values.put(TaskCursor.KEY.LEVEL, level);

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
			Date curDate = new Date(System.currentTimeMillis());
			values.put(TaskCursor.KEY.CREATED, String.valueOf(curDate));
			Uri uri = RemindmeVar.CONTENT_URI;
			context.getContentResolver().insert(uri, values);
			Toast.makeText(context, "�s�ƶ��w�g�x�s", Toast.LENGTH_SHORT).show();
			mSetAlarm.SetIt(true);
			return true;
		} catch (Exception e) {
			Toast.makeText(context, "�x�s�X���I", Toast.LENGTH_SHORT).show();
			RemindmeVar.debugMsg(0, "SaveOrUpdate SaveIt error=" + e);
			return false;
		}
	}

	private boolean UpdateIt(ContentValues values, int taskId) {
		try {
			Uri uri = ContentUris.withAppendedId(RemindmeVar.CONTENT_URI,
					taskId);
			context.getContentResolver().update(uri, values, null, null);
			Toast.makeText(context, "�ƶ���s���\�I", Toast.LENGTH_SHORT).show();
			mSetAlarm.SetIt(true);
			return true;
		} catch (Exception e) {
			Toast.makeText(context, "�x�s�X���I", Toast.LENGTH_SHORT).show();
			RemindmeVar.debugMsg(0, "SaveOrUpdate SaveIt error=" + e);
			return false;
		}
	}

}
