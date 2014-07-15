package me.iamcxa.remindme.editor;


public class EditorVar {

	// ��ܤ���B�ɶ���ܤ���`��
	public final int DATE_DIALOG_ID = 0;
	public final int TIME_DIALOG_ID = 1;

	public static EditorVar EditorVarInstance = new EditorVar();
	public DateVar Date = new DateVar();
	public LocationVar Location = new LocationVar();
	public EditorFields Editor = new EditorFields();

	private EditorVar(){}

	public static EditorVar GetInstance(){
		return EditorVarInstance;
	}

}

class EditorFields {
	// �O�_�}�Ҵ���
	public int on_off = 0;
	// �O�_�n��ĵ�i
	public int alarm = 0;
	// String
	// �Ƨѿ�ID
	public int taskId;
	
	//���ȼ��D/�Ƶ�
	public String tittle = null;
	public String content = null;
	
	//���Ȩ����/�إߤ�
	public String dueDate = null;
	public String created = null;
	
	//���Ȧa�I�W��/�y��
	public String locationName = null;
	public String coordinate = null;

	//���Ȩ������/�����g�g��
	public String alertTime = null;
	public String alertCycle = null;
	
}


class LocationVar {
	// gps�ϥήɶ�
	public int GpsUseTime = 0;
	// �g�n��
	public Double Latitude;
	public Double Longitude;

	// �O�_���j�M�L�a�I
	public Boolean isdidSearch = false;
	public Boolean isDropped = false;
	
	//---------------Getter/Setter-----------------//
	public int getGpsUseTime() {
		return GpsUseTime;
	}
	public void setGpsUseTime(int gpsUseTime) {
		GpsUseTime = gpsUseTime;
	}
	public Double getLatitude() {
		return Latitude;
	}
	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}
	public Double getLongitude() {
		return Longitude;
	}
	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}
	public Boolean getIsdidSearch() {
		return isdidSearch;
	}
	public void setIsdidSearch(Boolean isdidSearch) {
		this.isdidSearch = isdidSearch;
	}


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
	
	//---------------Getter/Setter-----------------//
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