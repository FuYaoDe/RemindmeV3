package me.iamcxa.remindme.editor;


public class EditorVar {


	// ��ܤ���B�ɶ���ܤ���`��
	public final int DATE_DIALOG_ID = 0;
	public final int TIME_DIALOG_ID = 1;
	
	// �O�_�}�Ҵ���
	public int on_off = 0;
	// �O�_�n��ĵ�i
	public int alarm = 0;
	// String
	// �O�s���e�B����P�ɶ��r��
	public String tittle = null;
	public String content = null;
	public String switcher = null;
	public String endDate = null, endTime = null;
	public String locationName = null;
	public String isRepeat = null;
	public String isFixed = null;
	public String isAllDay = null;
	public String isHide = null;
	public String isPW = null;
	public String coordinate = null;
	public String collaborator = null;
	public String created = null;
	public String LastTimeSearchName = "";
	public String is_Fixed = null;
	// �Ƨѿ�ID
	public int taskId;
	
	
	public static EditorVar EditorVarInstance = new EditorVar();
	public DateVar Date = new DateVar();
	public LocationVar Location = new LocationVar();

	private EditorVar(){}

	public static EditorVar GetInstance(){
		return EditorVarInstance;
	}


}


class LocationVar {
	// gps�ϥήɶ�
	public int GpsUseTime = 0;
	// �g�n��
	public Double Latitude;
	public Double Longitude;

	// �O�_���j�M�L�a�I
	public Boolean isdidSearch = false;
	public Boolean isDraped = false;
}


class DateVar {

	// ���
	private int mYear;
	private int mMonth;
	private int mDay;
	// �ɶ�
	private int mHour;
	private int mMinute;
	private int target;

	public int getmYear() {
		return mYear;
	}
	public void setmYear(int mYear) {
		this.mYear = mYear;
	}
	public int getmMonth() {
		return mMonth;
	}
	public void setmMonth(int mMonth) {
		this.mMonth = mMonth;
	}
	public int getmDay() {
		return mDay;
	}
	public void setmDay(int mDay) {
		this.mDay = mDay;
	}
	public int getmHour() {
		return mHour;
	}
	public void setmHour(int mHour) {
		this.mHour = mHour;
	}
	public int getmMinute() {
		return mMinute;
	}
	public void setmMinute(int mMinute) {
		this.mMinute = mMinute;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}

}