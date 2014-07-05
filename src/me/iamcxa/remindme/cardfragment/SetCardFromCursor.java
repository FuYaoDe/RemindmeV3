package me.iamcxa.remindme.cardfragment;

import me.iamcxa.remindme.R;
import me.iamcxa.remindme.RemindmeVar;
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
		// 準備常數
		// card.setId(cursor.getString(0));
		card.setId(String.valueOf(cursor.getPosition()));
		

		RemindmeVar.debugMsg(0, "prepare data from cursor...");
		boolean Extrainfo = cursor
				.isNull(RemindmeVar.TaskCursor.KEY_INDEX.OTHER);
		int CID = cursor.getInt(RemindmeVar.TaskCursor.KEY_INDEX.KEY_ID);
		String dintence = cursor
				.getString(RemindmeVar.TaskCursor.KEY_INDEX.DISTANCE);
		String created = cursor
				.getString(RemindmeVar.TaskCursor.KEY_INDEX.CREATED);
		String endTime = "";// cursor.getString(5);
		String endDate = cursor
				.getString(RemindmeVar.TaskCursor.KEY_INDEX.END_DATE);
		String LocationName = cursor
				.getString(RemindmeVar.TaskCursor.KEY_INDEX.LOCATION_NAME);
		String extraInfo = cursor
				.getString(RemindmeVar.TaskCursor.KEY_INDEX.OTHER);
		String PriorityWeight = cursor
				.getString(RemindmeVar.TaskCursor.KEY_INDEX.PRIORITY);
		long dayLeft = RemindmeVar.getDaysLeft(endDate, 2);
		// int dayLeft = Integer.parseInt("" + dayLeftLong);

		// give a ID.
		RemindmeVar.debugMsg(0, "set ID...Card id=" + CID);
		//card.setId("" + CID);

		// 卡片標題 - first line
		RemindmeVar.debugMsg(0, CID + " set Tittle...");
		card.mainHeader = cursor
				.getString(RemindmeVar.TaskCursor.KEY_INDEX.TITTLE);

		// 時間日期 - sec line
		RemindmeVar.debugMsg(0, CID + " set Date/Time...");
		RemindmeVar.debugMsg(0, CID + " dayleft=" + dayLeft);
		if ((180 > dayLeft) && (dayLeft > 14)) {
			card.DateTime = "再 " + (int) Math.floor(dayLeft) / 30 + " 個月 - "
					+ endDate + " - " + endTime;
		} else if ((14 > dayLeft) && (dayLeft > 0)) {
			card.DateTime = "再 " + dayLeft + " 天 - " + endDate + " - "
					+ endTime;
		} else if ((2 > dayLeft) && (dayLeft > 0)) {
			card.DateTime = "再 " + (int) Math.floor(dayLeft * 24) + "小時後 - "
					+ endDate + " - " + endTime;
		} else if (dayLeft == 0) {
			card.DateTime = "今天 - " + endDate + " - " + endTime;
		} else {
			card.DateTime = endDate + " - " + endTime;
		}

		// 小圖標顯示 - 判斷是否存有地點資訊
		RemindmeVar.debugMsg(0, "Location=\"" + LocationName + "\"");
		if (LocationName != null) {
			if ((LocationName.length()) > 1) {
				card.resourceIdThumb = R.drawable.map_marker;
			} else {
				card.resourceIdThumb = R.drawable.tear_of_calendar;
				card.LocationName = null;
			}
		}

		// 距離與地點資訊
		RemindmeVar.debugMsg(0, "dintence=" + dintence);
		if (dintence == null) {
			card.LocationName = LocationName;
		} else {
//			if (Double.valueOf(dintence) < 1) {
//				card.LocationName = LocationName + " - 距離 "
//						+ Double.valueOf(dintence) * 1000 + " 公尺";
//			} else {
				card.LocationName = LocationName + " - 距離 " + dintence + " 公里";

//			}
		}

		// 可展開額外資訊欄位
		RemindmeVar.debugMsg(0, "isExtrainfo=" + Extrainfo);
		// card.Notifications = "dbId="
		// + cursor.getString(0)
		// + ",w="
		// +
		cursor.getString(RemindmeVar.TaskCursor.KEY_INDEX.PRIORITY);
		if (!Extrainfo) {
			card.resourceIdThumb = R.drawable.outline_star_act;
			// 額外資訊提示 - 第四行

		}
		card.Notifications = cursor.getString(0);

		// 依照權重給予卡片顏色
		if (cursor.getInt(RemindmeVar.TaskCursor.KEY_INDEX.PRIORITY) > 6000) {
			card.setBackgroundResourceId(R.drawable.demo_card_selector_color5);
		} else if (cursor.getInt(RemindmeVar.TaskCursor.KEY_INDEX.PRIORITY) > 3000) {
			card.setBackgroundResourceId(R.drawable.demo_card_selector_color3);
		}
	}

}
