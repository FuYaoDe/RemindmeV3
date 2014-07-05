package me.iamcxa.remindme.editor;


public class EditorVar {

	// int
	// 提醒日期
	public static int mYear;
	public static int mMonth;
	public static int mDay;
	// 提醒時間
	public static int mHour;
	public static int mMinute;
	public static int target;
	// 顯示日期、時間對話方塊常數
	public static final int DATE_DIALOG_ID = 0;
	public static final int TIME_DIALOG_ID = 1;
	// 是否開啟提醒
	public static int on_off = 0;
	// 是否聲音警告
	public int alarm = 0;
	// String
	// 保存內容、日期與時間字串
	public static String tittle = null;
	public static String content = null;
	public static String switcher = null;
	public static String endDate = null, endTime = null;
	public static String locationName = null;
	public static String isRepeat = null;
	public static String isFixed = null;
	public static String isAllDay = null;
	public static String isHide = null;
	public static String isPW = null;
	public static String coordinate = null;
	public static String collaborator = null;
	public static String created = null;
	public static String LastTimeSearchName = "";
	public static String is_Fixed = null;
	// 是否有搜尋過地點
	public static Boolean isdidSearch = false;
	public static Boolean isDraped = false;
	// 備忘錄ID
	public static int taskId;
	// gps使用時間
	public static int GpsUseTime = 0;
	// 經緯度
	public static Double Latitude;
	public static Double Longitude;

	public static void Vars() {


	}




	public  void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
