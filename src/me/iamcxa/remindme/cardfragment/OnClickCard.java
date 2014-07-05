package me.iamcxa.remindme.cardfragment;


import it.gmariotti.cardslib.library.internal.Card;
import me.iamcxa.remindme.RemindmeVar;
import me.iamcxa.remindme.RemindmeVar.TaskCursor;
import me.iamcxa.remindme.editor.TaskEditorMain;
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
			

			RemindmeVar.debugMsg(0,
					"ReadCardonClick cursor moveToPosition cardIDfromclcikevent="
							+ cardPosition);
			String cardID = mMyCursorCardAdapter.getCardFromCursor(cursor)
					.getId();
			String cardID2=mMyCursorCardAdapter.getItem(Integer.parseInt(card.getId())).getId();		
			
			long cardID3=mMyCursorCardAdapter.getItemId(Integer.parseInt(cardID));
			
			
			
		cursor.moveToPosition(Integer.parseInt(cardPosition));

		ReadDatefromCursor();

		} catch (Exception e) {

			RemindmeVar.debugMsg(0, "ReadCardonClick error=" + e);
		}

	}

	private void ReadDatefromCursor() {
		int taskId = cursor.getInt(0);;
		String tittle = cursor.getString(1);
		String endDate = cursor.getString(3);
		String endTime = cursor.getString(TaskCursor.KEY_INDEX.END_TIME);
		String isRepeat = cursor.getString(TaskCursor.KEY_INDEX.IS_REPEAT);
		String isFixed = cursor.getString(TaskCursor.KEY_INDEX.IS_FIXED);
		String locationName = cursor
				.getString(TaskCursor.KEY_INDEX.LOCATION_NAME);
		String coordinate = cursor.getString(TaskCursor.KEY_INDEX.COORDINATE);
		String collaborator = cursor
				.getString(TaskCursor.KEY_INDEX.COLLABORATOR);
		String created = cursor.getString(TaskCursor.KEY_INDEX.CREATED);
		String content = cursor.getString(TaskCursor.KEY_INDEX.CONTENT);

		Bundle b = new Bundle();
		b.putInt("taskId", taskId);
		b.putString("tittle", tittle);
		b.putString("created", created);
		b.putString("endDate", endDate);
		b.putString("endTime", endTime);
		b.putString("content", content);
		b.putString("isRepeat", isRepeat);
		b.putString("IsFixed", isFixed);
		b.putString("locationName", locationName);
		b.putString("coordinate", coordinate);
		b.putString("collaborator", collaborator);

		// 將備忘錄資訊添加到Intent
		Intent intent = new Intent();
		intent.putExtra("b", b);
		// 啟動備忘錄詳細資訊Activity
		intent.setClass(context, TaskEditorMain.class);
		context.startActivity(intent);
	}
}
