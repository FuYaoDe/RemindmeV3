package me.iamcxa.remindme.cardfragment;


import common.MyCalendar;
import common.MyCursor.TaskCursor;
import common.MyDebug;
import common.CommonVar;

import it.gmariotti.cardslib.library.internal.Card;
import me.iamcxa.remindme.editor.TaskEditorTab;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class OnClickCard {
	Context context;
	Card card;
	Cursor cursor;
	MyCursorCardAdapter mMyCursorCardAdapter;

	public MyCursorCardAdapter getMyCursorCardAdapter() {
		return mMyCursorCardAdapter;
	}

	public void setMyCursorCardAdapter(MyCursorCardAdapter mMyCursorCardAdapter) {
		this.mMyCursorCardAdapter = mMyCursorCardAdapter;
	}

	public OnClickCard(Context context, Cursor cursor, Card card) {
		super();
		this.context = context;
		this.cursor = cursor;
		this.card = card;
	}

	public void readIt(String cardPosition) {
		try {

			MyDebug.MakeLog(0,
					"ReadCardonClick cursor moveToPosition cardIDfromclcikevent="
							+ cardPosition);
			String cardID = mMyCursorCardAdapter.getCardFromCursor(cursor)
					.getId();
			String cardID2=mMyCursorCardAdapter.getItem(Integer.parseInt(card.getId())).getId();		

			long cardID3=mMyCursorCardAdapter.getItemId(Integer.parseInt(cardID));



			cursor.moveToPosition(Integer.parseInt(cardPosition));

			ReadDatefromCursor();

		} catch (Exception e) {

			MyDebug.MakeLog(0, "ReadCardonClick error=" + e);
		}
	}

	private void ReadDatefromCursor() {
		int taskId = cursor.getInt(0);

		String[] DatefromCursor={
				//�D�n���e
				cursor.getString(TaskCursor.KEY_INDEX.TITTLE),				//0		
				cursor.getString(TaskCursor.KEY_INDEX.CONTENT),				//1	
				cursor.getString(TaskCursor.KEY_INDEX.CREATED),				//2
				cursor.getString(TaskCursor.KEY_INDEX.DUE_DATE),			//3
				//����
				cursor.getString(TaskCursor.KEY_INDEX.ALERT_Interval),		//4
				cursor.getString(TaskCursor.KEY_INDEX.ALERT_TIME),			//5
				//��m
				cursor.getString(TaskCursor.KEY_INDEX.LOCATION_NAME),		//6
				cursor.getString(TaskCursor.KEY_INDEX.COORDINATE),			//7
				cursor.getString(TaskCursor.KEY_INDEX.DISTANCE),			//8
				//����,���һP�u��
				cursor.getString(TaskCursor.KEY_INDEX.CATEGORY),			//9
				cursor.getString(TaskCursor.KEY_INDEX.PRIORITY),			//10
				cursor.getString(TaskCursor.KEY_INDEX.TAG),					//11
				cursor.getString(TaskCursor.KEY_INDEX.LEVEL),				//12
				//��L
				cursor.getString(TaskCursor.KEY_INDEX.COLLABORATOR),		//13
				cursor.getString(TaskCursor.KEY_INDEX.GOOGOLE_CAL_SYNC_ID),	//14
				cursor.getString(TaskCursor.KEY_INDEX.TASK_COLOR)			//15
		};

		Bundle b = new Bundle();
		//�D�n���e
		b.putInt(TaskCursor.KEY._ID,taskId);
		b.putString(TaskCursor.KEY.TITTLE,DatefromCursor[0]);
		b.putString(TaskCursor.KEY.CONTENT,DatefromCursor[1]);
		b.putString(TaskCursor.KEY.CREATED,DatefromCursor[2]);
		// duedate �ݭn�Ѹ�Ʈw���@���A�ഫ������榡 yyyy/mm/dd.
		String mDueDate="";
		MyDebug.MakeLog(0, "ReadCardonClick DatefromCursor3=" + DatefromCursor[3]);
		if(!DatefromCursor[3].contentEquals("null")){
			mDueDate=MyCalendar.getDate_From_TimeMillis(false, 
					Long.valueOf(DatefromCursor[3]));
		}//�ഫ����
		b.putString(TaskCursor.KEY.DUE_DATE,mDueDate);
		//����
		b.putString(TaskCursor.KEY.ALERT_Interval,DatefromCursor[4]);
		b.putString(TaskCursor.KEY.ALERT_TIME,DatefromCursor[5]);
		//��m
		b.putString(TaskCursor.KEY.LOCATION_NAME,DatefromCursor[6]);
		b.putString(TaskCursor.KEY.COORDINATE,DatefromCursor[7]);
		b.putString(TaskCursor.KEY.DISTANCE,DatefromCursor[8]);
		//����,���һP�u��
		b.putString(TaskCursor.KEY.CATEGORY,DatefromCursor[9]);
		b.putInt(TaskCursor.KEY.PRIORITY,Integer.valueOf(DatefromCursor[10]));
		b.putString(TaskCursor.KEY.TAG,DatefromCursor[11]);
		b.putString(TaskCursor.KEY.LEVEL,DatefromCursor[12]);
		//��L
		b.putString(TaskCursor.KEY.COLLABORATOR,DatefromCursor[13]);
		b.putString(TaskCursor.KEY.GOOGOLE_CAL_SYNC_ID,DatefromCursor[14]);
		b.putString(TaskCursor.KEY.TASK_COLOR,DatefromCursor[15]);

		// �N�Ƨѿ���T�K�[��Intent
		Intent intent = new Intent();
		intent.putExtra(CommonVar.BundleName, b);
		// �ҰʳƧѿ��ԲӸ�TActivity
		intent.setClass(context, TaskEditorTab.class);
		context.startActivity(intent);
	}
}
