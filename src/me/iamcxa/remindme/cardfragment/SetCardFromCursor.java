package me.iamcxa.remindme.cardfragment;

import me.iamcxa.remindme.CommonUtils;
import me.iamcxa.remindme.R;
import me.iamcxa.remindme.cardfragment.MyCursorCardAdapter.MyCursorCard;
import android.content.Context;
import android.database.Cursor;

public class SetCardFromCursor {

	@SuppressWarnings("unused")
	private Context context;
	private Cursor cursor;
	private MyCursorCard card;

	public SetCardFromCursor(Context context, Cursor cursor, MyCursorCard card) {
		this.context = context;
		this.cursor = cursor;
		this.card = card;
	}

	public void setIt() {
		// �ǳƱ`��
		// card.setId(cursor.getString(0));
		card.setId(String.valueOf(cursor.getPosition()));
		

		CommonUtils.debugMsg(0, "prepare data from cursor...");
		boolean Extrainfo = cursor
				.isNull(CommonUtils.TaskCursor.KEY_INDEX.OTHER);
		int CID = cursor.getInt(CommonUtils.TaskCursor.KEY_INDEX.KEY_ID);
		String dintence = cursor
				.getString(CommonUtils.TaskCursor.KEY_INDEX.DISTANCE);
		String created = cursor
				.getString(CommonUtils.TaskCursor.KEY_INDEX.CREATED);
		String endTime = "";// cursor.getString(5);
		String endDate = cursor
				.getString(CommonUtils.TaskCursor.KEY_INDEX.END_DATE);
		String LocationName = cursor
				.getString(CommonUtils.TaskCursor.KEY_INDEX.LOCATION_NAME);
		String extraInfo = cursor
				.getString(CommonUtils.TaskCursor.KEY_INDEX.OTHER);
		String PriorityWeight = cursor
				.getString(CommonUtils.TaskCursor.KEY_INDEX.PRIORITY);
		long dayLeft = CommonUtils.getDaysLeft(endDate, 2);
		// int dayLeft = Integer.parseInt("" + dayLeftLong);

		// give a ID.
		CommonUtils.debugMsg(0, "set ID...Card id=" + CID);
		//card.setId("" + CID);

		// �d�����D - first line
		CommonUtils.debugMsg(0, CID + " set Tittle...");
		card.mainHeader = cursor
				.getString(CommonUtils.TaskCursor.KEY_INDEX.TITTLE);

		// �ɶ���� - sec line
		CommonUtils.debugMsg(0, CID + " set Date/Time...");
		CommonUtils.debugMsg(0, CID + " dayleft=" + dayLeft);
		if ((180 > dayLeft) && (dayLeft > 14)) {
			card.DateTime = "�A " + (int) Math.floor(dayLeft) / 30 + " �Ӥ� - "
					+ endDate + " - " + endTime;
		} else if ((14 > dayLeft) && (dayLeft > 0)) {
			card.DateTime = "�A " + dayLeft + " �� - " + endDate + " - "
					+ endTime;
		} else if ((2 > dayLeft) && (dayLeft > 0)) {
			card.DateTime = "�A " + (int) Math.floor(dayLeft * 24) + "�p�ɫ� - "
					+ endDate + " - " + endTime;
		} else if (dayLeft == 0) {
			card.DateTime = "���� - " + endDate + " - " + endTime;
		} else {
			card.DateTime = endDate + " - " + endTime;
		}

		// �p�ϼ���� - �P�_�O�_�s���a�I��T
		CommonUtils.debugMsg(0, "Location=\"" + LocationName + "\"");
		if (LocationName != null) {
			if ((LocationName.length()) > 1) {
				card.resourceIdThumb = R.drawable.map_marker;
			} else {
				card.resourceIdThumb = R.drawable.tear_of_calendar;
				card.LocationName = null;
			}
		}

		// �Z���P�a�I��T
		CommonUtils.debugMsg(0, "dintence=" + dintence);
		if (dintence == null) {
			card.LocationName = LocationName;
		} else {
//			if (Double.valueOf(dintence) < 1) {
//				card.LocationName = LocationName + " - �Z�� "
//						+ Double.valueOf(dintence) * 1000 + " ����";
//			} else {
				card.LocationName = LocationName + " - �Z�� " + dintence + " ����";

//			}
		}

		// �i�i�}�B�~��T���
		CommonUtils.debugMsg(0, "isExtrainfo=" + Extrainfo);
		// card.Notifications = "dbId="
		// + cursor.getString(0)
		// + ",w="
		// +
		cursor.getString(CommonUtils.TaskCursor.KEY_INDEX.PRIORITY);
		if (!Extrainfo) {
			card.resourceIdThumb = R.drawable.outline_star_act;
			// �B�~��T���� - �ĥ|��

		}
		card.Notifications = cursor.getString(0);

		// �̷��v�������d���C��
		if (cursor.getInt(CommonUtils.TaskCursor.KEY_INDEX.PRIORITY) > 6000) {
			card.setBackgroundResourceId(R.drawable.demo_card_selector_color5);
		} else if (cursor.getInt(CommonUtils.TaskCursor.KEY_INDEX.PRIORITY) > 3000) {
			card.setBackgroundResourceId(R.drawable.demo_card_selector_color3);
		}
	}

}
