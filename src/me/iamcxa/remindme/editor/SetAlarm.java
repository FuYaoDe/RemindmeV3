package me.iamcxa.remindme.editor;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SetAlarm {
	Context context;
	int mYear;
	int mMonth;
	int mDay;
	int mHour;
	int mMinute;

	public SetAlarm(Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
	}

	// 設定通知提示
	public void SetIt(boolean flag) {

		// 保存內容、日期與時間字串
		String content = null;

		final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";

		// 取得AlarmManager實例
		final AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// 實例化Intent
		Intent intent = new Intent();

		// 設定Intent action屬性
		intent.setAction(BC_ACTION);
		intent.putExtra("msg", content);

		// 實例化PendingIntent
		final PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent,
				0);

		// 取得系統時間
		final long time1 = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();

		c.set(mYear, mMonth, mDay, mHour, mMinute);
		long time2 = c.getTimeInMillis();
		if (flag && (time2 - time1) > 0) {
			am.set(AlarmManager.RTC_WAKEUP, time2, pi);
		} else {
			am.cancel(pi);
		}

	}

}
