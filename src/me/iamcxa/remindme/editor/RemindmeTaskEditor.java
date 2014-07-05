<<<<<<< HEAD
/**
 * 
 */
package me.iamcxa.remindme.editor;

import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import me.iamcxa.remindme.CommonUtils;
import me.iamcxa.remindme.R;
import me.iamcxa.remindme.CommonUtils.TaskCursor;
import me.iamcxa.remindme.provider.GPSCallback;
import me.iamcxa.remindme.provider.GPSManager;
import me.iamcxa.remindme.provider.GeocodingAPI;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import me.iamcxa.remindme.provider.WorkaroundMapFragment;

/**
 * @author cxa
 * 
 */

public class RemindmeTaskEditor extends FragmentActivity implements GPSCallback {

	private static SaveOrUpdate mSaveOrUpdate;

	// �ŧiGPS�Ҳ�
	private static GPSManager gpsManager = null;

	// �ŧipick
	private static GoogleMap map;

	// ��ܤ���B�ɶ���ܤ���`��
	private static int mYear;
	private static int mMonth;
	private static int mDay;
	private static int mHour;
	private static int mMinute;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	// EditText
	private static EditText EditTextTittle;
	private static EditText SearchText;
	private static MultiAutoCompleteTextView boxTittle;
	private static MultiAutoCompleteTextView boxContent;

	private static TextView dateDesc;
	private static TextView timeDesc;
	private static TextView contentDesc;
	// Button
	private static Button Search;
	private static Button buttonPickFile;
	private static Button buttonSetDate;
	private static Button buttonSetTime;
	private static Button buttonSetLocation;
	private static Button buttonTakePhoto;

	private static ImageButton cancelLocation;
	private static ImageButton cancelDateTime;
	private static ImageButton cancelAttachment;

	// ImageButton
	private static ImageButton OK;
	// �O�_�}�Ҵ���
	private int on_off = 0;
	// String
	// �O�s���e�B����P�ɶ��r��
	private static String tittle = null;
	private static String content = null;
	private static String switcher = null;
	private static String endDate = null, endTime = null;
	private static String locationName = null;
	private static String isRepeat = null;
	private static String isFixed = null;
	private static String coordinate = null;
	private static String collaborator = null;
	private static String created = null;
	private static String LastTimeSearchName = "";
	private static String is_Fixed = null;

	// �g�n��
	private static Double Latitude;
	private static Double Longitude;
	private static ScrollView main_scrollview;

	private static CheckBox checkBoxIsFixed;

	private Handler GpsTimehandler = new Handler();
	// gps�ϥήɶ�
	private static int GpsUseTime = 0;
	// �O�_���j�M�L�a�I
	private static Boolean isdidSearch = false;
	private static Boolean isDraped = false;
	// �Ƨѿ�ID
	private int taskId;
	// �s���G�����
	private static LayoutInflater li;

	public View vv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);

		setComponents();
		setGPS();
		// setMAP();

		// ���oIntent
		final Intent intent = getIntent();
		// �]�wUri
		if (intent.getData() == null) {
			intent.setData(CommonUtils.CONTENT_URI);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��l�ƦC��
		init(getIntent());
	}

	private void setGPS() {
		gpsManager = new GPSManager();
		gpsManager.startGpsListening(getApplicationContext());
		gpsManager.setGPSCallback(RemindmeTaskEditor.this);
		CommonUtils.GpsSetting.GpsStatus = true;
		GpsUseTime = 0;
		GpsTimehandler.post(GpsTime);
	}

	private void setMAP() {
		// map = ((MapFragment) getFragmentManager()
		// .findFragmentById(R.id.map)).getMap();
		map = ((WorkaroundMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		main_scrollview = (ScrollView) findViewById(R.id.main_scrollview);

		((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.map))
				.setListener(new WorkaroundMapFragment.OnTouchListener() {
					@Override
					public void onTouch() {
						main_scrollview
								.requestDisallowInterceptTouchEvent(true);
					}
				});
		map.setMyLocationEnabled(true);
		map.clear();
		LatLng nowLoacation;
		if (gpsManager.LastLocation() != null) {
			nowLoacation = new LatLng(gpsManager.LastLocation().getLatitude(),
					gpsManager.LastLocation().getLongitude());
			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
					map.getMaxZoomLevel() - 4)));
		} else {
			nowLoacation = new LatLng(23.6978, 120.961);
			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
					map.getMinZoomLevel() + 7)));
		}
		map.addMarker(new MarkerOptions().title("��e��m").draggable(true)
				.position(nowLoacation));

		map.setOnCameraChangeListener(listener);
	}

	private void setComponents() {
		// ���oCalendar���
		final Calendar c = Calendar.getInstance();

		// ���o�ثe����B�ɶ�
		mYear = c.get(Calendar.YEAR);
		mMonth = (c.get(Calendar.MONTH));
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		setEditorComponent();
		setLocationPicker();

	}

	// �s�边�D�e������
	private void setEditorComponent() {
		// ��J���
		boxTittle = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextViewTittle);
		boxContent = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextViewContent);

		// �϶��G �ɶ����
		buttonSetDate = (Button) findViewById(R.id.buttonSetDate);
		buttonSetDate.setOnClickListener(btnActionEditorButton);
		buttonSetTime = (Button) findViewById(R.id.buttonSetTime);
		buttonSetTime.setOnClickListener(btnActionEditorButton);
		cancelDateTime = (ImageButton) findViewById(R.id.imageButtonCancelDateTime);

		// �϶��G �a�I
		buttonSetLocation = (Button) findViewById(R.id.buttonSetLocation);
		buttonSetLocation.setOnClickListener(btnActionEditorButton);
		cancelLocation = (ImageButton) findViewById(R.id.imageButtonCancelLocation);

		// �϶��G ����
		buttonPickFile = (Button) findViewById(R.id.buttonPickFile);
		buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
		cancelAttachment = (ImageButton) findViewById(R.id.imageButtonCancelAttachment);

	}

	// �a�I��ܾ�
	private void setLocationPicker() {

		checkBoxIsFixed = (CheckBox) findViewById(R.id.checkBoxIsFixed);
		SearchText = (EditText) findViewById(R.id.SearchText);

		Search = (Button) findViewById(R.id.Search);
		// Search.setOnClickListener(SearchPlace);

		OK = (ImageButton) findViewById(R.id.OK);
		// OK.setOnlickListener(SearchPlace);
	}

	private Button.OnClickListener btnActionEditorButton = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonSetDate:
				// �]�w�������
				showDialog(DATE_DIALOG_ID);
				break;
			case R.id.buttonSetTime:
				// �]�w�����ɶ�
				showDialog(TIME_DIALOG_ID);
				break;
			case R.id.buttonSetLocation:
				// �]�w�����ɶ�
				showDialog1(null, null, TIME_DIALOG_ID);
				break;
			}
		}
	};

	// ��l�Ƥ�k
	private void init(Intent intent) {
		Bundle b = intent.getBundleExtra("b");
		if (b != null) {
			taskId = b.getInt("taskId");
			tittle = b.getString("tittle");
			created = b.getString("created");
			endDate = b.getString("endDate");
			endTime = b.getString("endTime");
			content = b.getString("content");
			isRepeat = b.getString("isRepeat");
			isFixed = b.getString("isFixed");
			locationName = b.getString("locationName");
			coordinate = b.getString("coordinate");
			collaborator = b.getString("collaborator");

			if (endDate != null && endDate.length() > 0) {
				String[] strs = endDate.split("/");
				mYear = Integer.parseInt(strs[0]);
				mMonth = Integer.parseInt(strs[1]) - 1;
				mDay = Integer.parseInt(strs[2]);
			}

			if (endTime != null && endTime.length() > 0) {
				String[] strs = endTime.split(":");
				mHour = Integer.parseInt(strs[0]);
				mMinute = Integer.parseInt(strs[1]);
			}

			EditTextTittle.setText(tittle);

		}

		Toast.makeText(getApplicationContext(),
				taskId + "," + content + "," + endDate + "," + endTime,
				Toast.LENGTH_LONG).show();
	}

	// ��ܹ�ܤ��
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		// ��ܤ����ܤ��
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth - 1, mDay);
			// ��ܮɶ���ܤ��
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		}
		return null;
	}

	// �]�w�q������
	private void setAlarm(boolean flag) {
		final String BC_ACTION = "com.amaker.ch17.TaskReceiver";
		// ���oAlarmManager���
		final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// ��Ҥ�Intent
		Intent intent = new Intent();
		// �]�wIntent action�ݩ�
		intent.setAction(BC_ACTION);
		intent.putExtra("msg", content);
		// ��Ҥ�PendingIntent
		final PendingIntent pi = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		// ���o�t�ήɶ�
		final long time1 = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.set(mYear, mMonth, mDay, mHour, mMinute);
		long time2 = c.getTimeInMillis();
		if (flag && (time2 - time1) > 0 && on_off == 1) {
			am.set(AlarmManager.RTC_WAKEUP, time2, pi);
		} else {
			am.cancel(pi);
		}
	}

	/*
	 * �]�w���ܤ����ܤ��
	 */
	private void showDialog1(String msg, String tittle, int target) {
		View v = li.inflate(
				R.layout.activity_task_editor_parts_dialog_location, null);
		final TextView editTextTittle = (TextView) v.findViewById(R.id.name);
		final EditText editTextbox = (EditText) v.findViewById(R.id.editTexbox);
		editTextTittle.setText(tittle + target);

		switch (target) {
		case 2:
			switcher = content;
			break;
		default:
			break;
		}

		editTextbox.setText(switcher);

		new AlertDialog.Builder(this).setView(v).setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						content = editTextbox.getText().toString();

						contentDesc.setText(switcher);
						// locationDesc.setText(switcher);
					}
				}).show();

	}

	// �ɶ���ܹ�ܤ��
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			if (String.valueOf(mMinute).length() == 1) {
				buttonSetTime.setText(mHour + ":0" + mMinute);
			}else{
			buttonSetTime.setText(mHour + ":" + mMinute);
			}
		}
	};

	// �����ܹ�ܤ��
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			buttonSetDate.setText(mYear + "/" + (mMonth + 1) + "/" + mDay);
		}
	};

	// �x�s�έק�Ƨѿ���T
	@Override
	protected void onPause() {
		super.onPause();
		locationName = null;
		content = null;
	};

	// This is the action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ���editor_activity_actionbar.xml���e
		getMenuInflater().inflate(R.menu.editor_activity_actionbar, menu);

		// �ҥ�actionbar��^�����b�Y
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("�إ߫ݿ�ƶ�");

		// actionAdd
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		actionAdd.setOnMenuItemClickListener(btnActionAddClick);

		// actionCancel
		MenuItem actionCancel = menu.findItem(R.id.action_cancel);
		actionCancel.setOnMenuItemClickListener(btnActionCancelClick);

		return true;
	}

	// actionbar�b�Y��^�����ʧ@
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 
	 */
	private MenuItem.OnMenuItemClickListener btnActionAddClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// Toast.makeText(getApplicationContext(),
			// dateDesc.getText()+"2"+timeDesc.getText(),
			// Toast.LENGTH_SHORT).show();
			// if (dateDesc.getText().equals("") &&
			// timeDesc.getText().equals("")
			// && contentDesc.getText().equals("")
			// && SearchText.getText().toString().equals("")) {
			// String[] StringArray = EditTextTittle.getText().toString()
			// .split(" ");
			// try {
			// int i = Integer.parseInt(StringArray[0]);
			// // System.out.println(i);
			// } catch (Exception e) {
			// EditTextTittle.setText("3 " + StringArray[0]);
			// }
			// // String[] QuickTitle =
			// // QuickInput.QuickInput(EditTextTittle.getText().toString());
			// // for (int a=0 ;a<QuickTitle.length;a++) {
			// // if(QuickTitle[a]!=null){
			// // switch (a) {
			// // case 1:
			// // String[] Time =QuickInput.TimeQuickInput(QuickTitle[1]);
			// // try {
			// // mHour = Integer.parseInt(Time[0]);
			// // mMinute = Integer.parseInt(Time[1]);
			// // timeDesc.setText(mHour + ":" + mMinute);
			// //
			// // } catch (Exception e) {
			// // Toast.makeText(getApplicationContext(), e.toString(),
			// // Toast.LENGTH_SHORT).show();
			// // }
			// // break;
			// // case 2:
			// // SearchText.setText(QuickTitle[2]);
			// // break;
			// // case 3:
			// // EditTextTittle.setText(QuickTitle[3]);
			// // break;
			// // case 4:
			// // contentDesc.setText(QuickTitle[4]);
			// // break;
			// // default:
			// // break;
			// // }
			// // }
			// // }
			// }

			if (!isdidSearch && !SearchText.getText().toString().equals("")) {
				// SearchPlace();
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(), SearchText.getText()
								.toString());
				if (LoacationAddress.GeocodingApiLatLngGet() != null) {
					Longitude = LoacationAddress.GeocodingApiLatLngGet().longitude;
					Latitude = LoacationAddress.GeocodingApiLatLngGet().latitude;
				}
			}
			if (isDraped && !SearchText.getText().toString().equals("")) {
				Longitude = map.getCameraPosition().target.longitude;
				Latitude = map.getCameraPosition().target.latitude;
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(), Latitude + "," + Longitude);
				if (LoacationAddress.GeocodingApiAddressGet() != null) {
					SearchText.setText(LoacationAddress
							.GeocodingApiAddressGet());
				}
			}

			if (checkBoxIsFixed != null) {
				is_Fixed = String.valueOf(checkBoxIsFixed.isChecked());
				endDate = dateDesc.getText().toString();
				// endTime = timeDesc.getText().toString();
				// content = contentDesc.getText().toString();
				tittle = EditTextTittle.getText().toString();
				coordinate = Latitude + "," + Longitude;
				locationName = SearchText.getText().toString();
			}

			mSaveOrUpdate = new SaveOrUpdate(getApplicationContext());
			mSaveOrUpdate.DoTaskEditorAdding(taskId, tittle, endDate, endTime,
					content, locationName, coordinate, "1", is_Fixed, "1");
			finish();
			return true;
		}

	};

	/*
	 * 
	 */
	private MenuItem.OnMenuItemClickListener btnActionCancelClick = new MenuItem.OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub

			// Intent EventEditor = new Intent();
			// EventEditor.setClass(getApplication(),RemindmeTaskEditorActivity.class);
			// EventEditor.setClass(getApplication(), IconRequest.class);
			// startActivity(EventEditor);

			// saveOrUpdate();
			finish();

			return false;
		}

	};

	// GPS��m���ɷ|��s��m
	@Override
	public void onGPSUpdate(Location location) {
		// TODO Auto-generated method stub
		Double Longitude = location.getLongitude();
		// �n��
		Double Latitude = location.getLatitude();

		// textView1.setText("�g�n��:"+Latitude+","+Longitude);
		// ����g�n�׫ᰨ�W����
		// Toast.makeText(getApplicationContext(), "����GPS"+location,
		// Toast.LENGTH_LONG).show();

		if (CommonUtils.GpsSetting.GpsStatus) {
			CommonUtils.GpsSetting.GpsStatus = false;
			gpsManager.stopListening();
			gpsManager.setGPSCallback(null);
			gpsManager = null;
		} else {
			CommonUtils.GpsSetting.GpsStatus = false;
		}
		LatLng nowLoacation = new LatLng(Latitude, Longitude);

		map.setMyLocationEnabled(true);

		map.clear();

		map.animateCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
				map.getMaxZoomLevel() - 4)));

		map.addMarker(new MarkerOptions().title("�ثe��m").position(nowLoacation));

		// GeocodingAPI LoacationAddress = new
		// GeocodingAPI(getApplicationContext(),Latitude+","+Longitude);
		// textView2.setText(textView2.getText()+" "+LoacationAddress.GeocodingApiAddressGet());
	}

	private Button.OnClickListener SearchPlace = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// �ŧiGPSManager
			switch (v.getId()) {
			case R.id.OK:
				// textView2.setText(textView2.getText()+"\n"+LoacationAddress.GeocodingApiAddressGet());
				// //����a�}
				// textView2.setText(textView2.getText()+"\n"+LoacationAddress.GeocodingApiLatLngGet());
				// //����g�n��
				if (!isdidSearch
						|| !SearchText.getText().toString()
								.equals(LastTimeSearchName)) {
					SearchPlace();
					isdidSearch = true;
					LastTimeSearchName = SearchText.getText().toString();
				}
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(),
						map.getCameraPosition().target.latitude + ","
								+ map.getCameraPosition().target.longitude);
				if (LoacationAddress.GeocodingApiLatLngGet() != null) {
					Longitude = LoacationAddress.GeocodingApiLatLngGet().longitude;
					Latitude = LoacationAddress.GeocodingApiLatLngGet().latitude;
				}
				// locationDesc = LoacationAddress.GeocodingApiAddressGet();
				// Toast.makeText(getApplicationContext(),
				// "����g�n��"+map.getCameraPosition().target.latitude+","+map.getCameraPosition().target.longitude+"\n�a�}:"+locationName,
				// Toast.LENGTH_SHORT).show();

				break;
			case R.id.Search:
				// textView2.setText(map.getMyLocation().toString());
				// //�i�κ������GPS��m
				if (!SearchText.getText().toString().equals(LastTimeSearchName)) {
					SearchPlace();
					isdidSearch = true;
					LastTimeSearchName = SearchText.getText().toString();
				}
				break;

			default:
				break;
			}
		}
	};

	// �a�ϲ��ʮɧ�s���w��m
	private GoogleMap.OnCameraChangeListener listener = new GoogleMap.OnCameraChangeListener() {

		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
			map.clear();
			LatLng now = new LatLng(position.target.latitude,
					position.target.longitude);
			map.addMarker(new MarkerOptions().title("�ت��a").position(now));
			if (isdidSearch)
				isDraped = true;
		}
	};

	private Runnable GpsTime = new Runnable() {
		@Override
		public void run() {
			GpsUseTime++;
			// Timeout Sec, �W�LTIMEOUT�]�w�ɶ���,�����]�wFLAG�ϱogetCurrentLocation���
			// lastlocation.
			if (GpsUseTime > CommonUtils.GpsSetting.TIMEOUT_SEC) {
				if (CommonUtils.GpsSetting.GpsStatus) {
					gpsManager.stopListening();
					gpsManager.startNetWorkListening(getApplicationContext());
					CommonUtils.GpsSetting.GpsStatus = true;
					// Toast.makeText(getApplicationContext(), "����GPS",
					// Toast.LENGTH_LONG).show();
				}
			} else {
				GpsTimehandler.postDelayed(this, 1000);
			}
		}
	};

	private void SearchPlace() {
		if (!SearchText.getText().toString().equals("")) {
			GeocodingAPI LoacationAddress2 = null;
			LatLng SearchLocation = null;
			LoacationAddress2 = new GeocodingAPI(getApplicationContext(),
					SearchText.getText().toString());
			// textView2.setText("");
			// locationName=LoacationAddress2.GeocodingApiAddressGet();
			// textView2.setText(textView2.getText()+"\n"+Address);
			SearchLocation = LoacationAddress2.GeocodingApiLatLngGet();
			// textView2.setText(textView2.getText()+"\n"+SearchLocation);
			if (SearchLocation != null) {
				map.animateCamera((CameraUpdateFactory.newLatLngZoom(
						SearchLocation, map.getMaxZoomLevel() - 4)));
				map.addMarker(new MarkerOptions().title("�j�M����m")
						.snippet(locationName).position(SearchLocation));
			} else {
				Toast.makeText(getApplicationContext(), "�d�L�a�I�@,���ӵ��ոլ�",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}

// * CLASS JUST FOR THE CUSTOM ALERT DIALOG
class CustomAlertDialog extends AlertDialog {
	public CustomAlertDialog(Context context) {
		super(context);
	}
}
=======
/**
 * 
 */
package me.iamcxa.remindme.editor;

import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import me.iamcxa.remindme.CommonUtils;
import me.iamcxa.remindme.R;
import me.iamcxa.remindme.CommonUtils.TaskCursor;
import me.iamcxa.remindme.provider.GPSCallback;
import me.iamcxa.remindme.provider.GPSManager;
import me.iamcxa.remindme.provider.GeocodingAPI;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import me.iamcxa.remindme.provider.WorkaroundMapFragment;

/**
 * @author cxa
 * 
 */

public class RemindmeTaskEditor extends FragmentActivity implements GPSCallback {

	/************************************** �ܱ`�ư϶��}�l *******************************************/
	private static SaveOrUpdate mSaveOrUpdate;

	// �ŧiGPS�Ҳ�
	private static GPSManager gpsManager = null;

	// �ŧipick
	private static GoogleMap map;

	// ��ܤ���B�ɶ���ܤ���`��
	private static int mYear;
	private static int mMonth;
	private static int mDay;
	private static int mHour;
	private static int mMinute;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	// EditText
	private static EditText EditTextTittle;
	private static EditText SearchText;
	private static MultiAutoCompleteTextView boxTittle;
	private static MultiAutoCompleteTextView boxContent;

	private static TextView dateDesc;
	private static TextView timeDesc;
	private static TextView contentDesc;
	
	// Button
	private static Button Search;
	private static Button buttonPickFile;
	private static Button buttonSetDate;
	private static Button buttonSetTime;
	private static Button buttonSetLocation;
	private static Button buttonTakePhoto;

	private static ImageButton cancelLocation;
	private static ImageButton cancelDateTime;
	private static ImageButton cancelAttachment;

	// ImageButton
	private static ImageButton OK;
	// �O�_�}�Ҵ���
	private int on_off = 0;
	// String
	// �O�s���e�B����P�ɶ��r��
	private static String tittle = null;
	private static String content = null;
	private static String switcher = null;
	private static String endDate = null, endTime = null;
	private static String locationName = null;
	private static String isRepeat = null;
	private static String isFixed = null;
	private static String coordinate = null;
	private static String collaborator = null;
	private static String created = null;
	private static String LastTimeSearchName = "";
	private static String is_Fixed = null;

	// �g�n��
	private static Double Latitude;
	private static Double Longitude;
	private static ScrollView main_scrollview;

	private static CheckBox checkBoxIsFixed;

	private Handler GpsTimehandler = new Handler();
	// gps�ϥήɶ�
	private static int GpsUseTime = 0;
	// �O�_���j�M�L�a�I
	private static Boolean isdidSearch = false;
	private static Boolean isDraped = false;
	// �Ƨѿ�ID
	private int taskId;
	/************************************** �ܱ`�ư϶����� *******************************************/

	/************************************* Activity�g���}�l ******************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);

		setComponents();
		setGPS();
		// setMAP();

		// ���oIntent
		final Intent intent = getIntent();
		// �]�wUri
		if (intent.getData() == null) {
			intent.setData(CommonUtils.CONTENT_URI);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��l�ƦC��
		init(getIntent());
	}

	// �x�s�έק�Ƨѿ���T
	@Override
	protected void onPause() {
		super.onPause();
		locationName = null;
		content = null;
	};

	/************************************* Activity�g������ ******************************************/

	/************************************* �]�w�e������}�l ******************************************/
	private void setComponents() {
		// ���oCalendar���
		final Calendar c = Calendar.getInstance();

		// ���o�ثe����B�ɶ�
		mYear = c.get(Calendar.YEAR);
		mMonth = (c.get(Calendar.MONTH));
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		setEditorComponent();
		setLocationPicker();

	}

	// �s�边�D�e������
	private void setEditorComponent() {
		// ��J���
		boxTittle = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextViewTittle);
		boxContent = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextViewContent);

		// �϶��G �ɶ����
		buttonSetDate = (Button) findViewById(R.id.buttonSetDate);
		buttonSetDate.setOnClickListener(btnActionEditorButton);
		buttonSetTime = (Button) findViewById(R.id.buttonSetTime);
		buttonSetTime.setOnClickListener(btnActionEditorButton);
		cancelDateTime = (ImageButton) findViewById(R.id.imageButtonCancelDateTime);

		// �϶��G �a�I
		buttonSetLocation = (Button) findViewById(R.id.buttonSetLocation);
		buttonSetLocation.setOnClickListener(btnActionEditorButton);
		cancelLocation = (ImageButton) findViewById(R.id.imageButtonCancelLocation);

		// �϶��G ����
		buttonPickFile = (Button) findViewById(R.id.buttonPickFile);
		buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
		cancelAttachment = (ImageButton) findViewById(R.id.imageButtonCancelAttachment);

	}

	// �a�I��ܾ�
	private void setLocationPicker() {

		checkBoxIsFixed = (CheckBox) findViewById(R.id.checkBoxIsFixed);
		SearchText = (EditText) findViewById(R.id.SearchText);

		Search = (Button) findViewById(R.id.Search);
		// Search.setOnClickListener(SearchPlace);

		OK = (ImageButton) findViewById(R.id.OK);
		// OK.setOnlickListener(SearchPlace);
	}
	/************************************* �]�w�e�����󵲧� ******************************************/

	/************************************** �Ƶ{���϶��}�l ******************************************/
	private void setGPS() {
		gpsManager = new GPSManager();
		gpsManager.startGpsListening(getApplicationContext());
		gpsManager.setGPSCallback(RemindmeTaskEditor.this);
		CommonUtils.GpsSetting.GpsStatus = true;
		GpsUseTime = 0;
		GpsTimehandler.post(GpsTime);
	}

	private void setMAP() {
		// map = ((MapFragment) getFragmentManager()
		// .findFragmentById(R.id.map)).getMap();
		map = ((WorkaroundMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		main_scrollview = (ScrollView) findViewById(R.id.main_scrollview);

		((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(
				R.id.map))
				.setListener(new WorkaroundMapFragment.OnTouchListener() {
					@Override
					public void onTouch() {
						main_scrollview
								.requestDisallowInterceptTouchEvent(true);
					}
				});
		map.setMyLocationEnabled(true);
		map.clear();
		LatLng nowLoacation;
		if (gpsManager.LastLocation() != null) {
			nowLoacation = new LatLng(gpsManager.LastLocation().getLatitude(),
					gpsManager.LastLocation().getLongitude());
			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
					map.getMaxZoomLevel() - 4)));
		} else {
			nowLoacation = new LatLng(23.6978, 120.961);
			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
					map.getMinZoomLevel() + 7)));
		}
		map.addMarker(new MarkerOptions().title("��e��m").draggable(true)
				.position(nowLoacation));

		map.setOnCameraChangeListener(listener);
	}

	/************************************** �Ƶ{���϶����� ******************************************/

	private Button.OnClickListener btnActionEditorButton = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonSetDate:
				// �]�w�������
				showDialog(DATE_DIALOG_ID);
				break;
			case R.id.buttonSetTime:
				// �]�w�����ɶ�
				showDialog(TIME_DIALOG_ID);
				break;
			case R.id.buttonSetLocation:
				// �]�w�����ɶ�
				showDialogLocationPicker();
				break;
			}
		}
	};

	// ��l�Ƥ�k
	private void init(Intent intent) {
		Bundle b = intent.getBundleExtra("b");
		if (b != null) {
			taskId = b.getInt("taskId");
			tittle = b.getString("tittle");
			created = b.getString("created");
			endDate = b.getString("endDate");
			endTime = b.getString("endTime");
			content = b.getString("content");
			isRepeat = b.getString("isRepeat");
			isFixed = b.getString("isFixed");
			locationName = b.getString("locationName");
			coordinate = b.getString("coordinate");
			collaborator = b.getString("collaborator");

			if (endDate != null && endDate.length() > 0) {
				String[] strs = endDate.split("/");
				mYear = Integer.parseInt(strs[0]);
				mMonth = Integer.parseInt(strs[1]) - 1;
				mDay = Integer.parseInt(strs[2]);
			}

			if (endTime != null && endTime.length() > 0) {
				String[] strs = endTime.split(":");
				mHour = Integer.parseInt(strs[0]);
				mMinute = Integer.parseInt(strs[1]);
			}

			EditTextTittle.setText(tittle);

		}

		Toast.makeText(getApplicationContext(),
				taskId + "," + content + "," + endDate + "," + endTime,
				Toast.LENGTH_LONG).show();
	}

	// �]�w�q������
	private void setAlarm(boolean flag) {
		// ���oAlarmManager���
		final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// ��Ҥ�Intent
		Intent intent = new Intent();
		// �]�wIntent action�ݩ�
		intent.setAction(CommonUtils.BC_ACTION);
		intent.putExtra("msg", content);
		// ��Ҥ�PendingIntent
		final PendingIntent pi = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		// ���o�t�ήɶ�
		final long time1 = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.set(mYear, mMonth, mDay, mHour, mMinute);
		long time2 = c.getTimeInMillis();
		if (flag && (time2 - time1) > 0 && on_off == 1) {
			am.set(AlarmManager.RTC_WAKEUP, time2, pi);
		} else {
			am.cancel(pi);
		}
	}

	/************************************** ��ܤ���}�l *******************************************/
	// ��ܹ�ܤ��
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		// ��ܤ����ܤ��
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth - 1, mDay);
			// ��ܮɶ���ܤ��
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		}
		return null;
	}

	// �ɶ���ܹ�ܤ��
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			if (String.valueOf(mMinute).length() == 1) {
				buttonSetTime.setText(mHour + ":0" + mMinute);
			} else {
				buttonSetTime.setText(mHour + ":" + mMinute);
			}
		}
	};

	// �����ܹ�ܤ��
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			buttonSetDate.setText(mYear + "/" + (mMonth + 1) + "/" + mDay);
		}
	};
	/************************************** ��ܤ������ *******************************************/

	/************************************** ActionBar�}�l ******************************************/
	// This is the action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ���editor_activity_actionbar.xml���e
		getMenuInflater().inflate(R.menu.editor_activity_actionbar, menu);

		// �ҥ�actionbar��^�����b�Y
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("�إ߫ݿ�ƶ�");

		// actionAdd
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		actionAdd.setOnMenuItemClickListener(btnActionAddClick);

		// actionCancel
		MenuItem actionCancel = menu.findItem(R.id.action_cancel);
		actionCancel.setOnMenuItemClickListener(btnActionCancelClick);

		return true;
	}

	// actionbar�b�Y��^�����ʧ@
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// ���s��ť��:btnActionAddClick
	private MenuItem.OnMenuItemClickListener btnActionAddClick = new MenuItem.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// Toast.makeText(getApplicationContext(),
			// dateDesc.getText()+"2"+timeDesc.getText(),
			// Toast.LENGTH_SHORT).show();
			// if (dateDesc.getText().equals("") &&
			// timeDesc.getText().equals("")
			// && contentDesc.getText().equals("")
			// && SearchText.getText().toString().equals("")) {
			// String[] StringArray = EditTextTittle.getText().toString()
			// .split(" ");
			// try {
			// int i = Integer.parseInt(StringArray[0]);
			// // System.out.println(i);
			// } catch (Exception e) {
			// EditTextTittle.setText("3 " + StringArray[0]);
			// }
			// // String[] QuickTitle =
			// // QuickInput.QuickInput(EditTextTittle.getText().toString());
			// // for (int a=0 ;a<QuickTitle.length;a++) {
			// // if(QuickTitle[a]!=null){
			// // switch (a) {
			// // case 1:
			// // String[] Time =QuickInput.TimeQuickInput(QuickTitle[1]);
			// // try {
			// // mHour = Integer.parseInt(Time[0]);
			// // mMinute = Integer.parseInt(Time[1]);
			// // timeDesc.setText(mHour + ":" + mMinute);
			// //
			// // } catch (Exception e) {
			// // Toast.makeText(getApplicationContext(), e.toString(),
			// // Toast.LENGTH_SHORT).show();
			// // }
			// // break;
			// // case 2:
			// // SearchText.setText(QuickTitle[2]);
			// // break;
			// // case 3:
			// // EditTextTittle.setText(QuickTitle[3]);
			// // break;
			// // case 4:
			// // contentDesc.setText(QuickTitle[4]);
			// // break;
			// // default:
			// // break;
			// // }
			// // }
			// // }
			// }

			if (!isdidSearch && !SearchText.getText().toString().equals("")) {
				// SearchPlace();
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(), SearchText.getText()
								.toString());
				if (LoacationAddress.GeocodingApiLatLngGet() != null) {
					Longitude = LoacationAddress.GeocodingApiLatLngGet().longitude;
					Latitude = LoacationAddress.GeocodingApiLatLngGet().latitude;
				}
			}
			if (isDraped && !SearchText.getText().toString().equals("")) {
				Longitude = map.getCameraPosition().target.longitude;
				Latitude = map.getCameraPosition().target.latitude;
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(), Latitude + "," + Longitude);
				if (LoacationAddress.GeocodingApiAddressGet() != null) {
					SearchText.setText(LoacationAddress
							.GeocodingApiAddressGet());
				}
			}

			if (checkBoxIsFixed != null) {
				is_Fixed = String.valueOf(checkBoxIsFixed.isChecked());
				endDate = dateDesc.getText().toString();
				// endTime = timeDesc.getText().toString();
				// content = contentDesc.getText().toString();
				tittle = EditTextTittle.getText().toString();
				coordinate = Latitude + "," + Longitude;
				locationName = SearchText.getText().toString();
			}

			mSaveOrUpdate = new SaveOrUpdate(getApplicationContext());
			mSaveOrUpdate.DoTaskEditorAdding(taskId, tittle, endDate, endTime,
					content, locationName, coordinate, "1", is_Fixed, "1");
			finish();
			return true;
		}

	};

	// ���s��ť��:btnActionCancelClick
	private MenuItem.OnMenuItemClickListener btnActionCancelClick = new MenuItem.OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub

			// Intent EventEditor = new Intent();
			// EventEditor.setClass(getApplication(),RemindmeTaskEditorActivity.class);
			// EventEditor.setClass(getApplication(), IconRequest.class);
			// startActivity(EventEditor);

			// saveOrUpdate();
			finish();

			return false;
		}

	};
	/************************************** ActionBar���� ******************************************/

	/************************************** �a�I��ܾ��}�l ******************************************/
	// GPS��m���ɷ|��s��m
	@Override
	public void onGPSUpdate(Location location) {
		// TODO Auto-generated method stub
		Double Longitude = location.getLongitude();
		// �n��
		Double Latitude = location.getLatitude();

		// textView1.setText("�g�n��:"+Latitude+","+Longitude);
		// ����g�n�׫ᰨ�W����
		// Toast.makeText(getApplicationContext(), "����GPS"+location,
		// Toast.LENGTH_LONG).show();

		if (CommonUtils.GpsSetting.GpsStatus) {
			CommonUtils.GpsSetting.GpsStatus = false;
			gpsManager.stopListening();
			gpsManager.setGPSCallback(null);
			gpsManager = null;
		} else {
			CommonUtils.GpsSetting.GpsStatus = false;
		}
		LatLng nowLoacation = new LatLng(Latitude, Longitude);

		// map.setMyLocationEnabled(true);

		map.clear();

		map.animateCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
				map.getMaxZoomLevel() - 4)));

		map.addMarker(new MarkerOptions().title("�ثe��m").position(nowLoacation));

		// GeocodingAPI LoacationAddress = new
		// GeocodingAPI(getApplicationContext(),Latitude+","+Longitude);
		// textView2.setText(textView2.getText()+" "+LoacationAddress.GeocodingApiAddressGet());
	}

	// ���s���U��ť��
	private Button.OnClickListener SearchPlace = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// �ŧiGPSManager
			switch (v.getId()) {
			case R.id.OK:
				// textView2.setText(textView2.getText()+"\n"+LoacationAddress.GeocodingApiAddressGet());
				// //����a�}
				// textView2.setText(textView2.getText()+"\n"+LoacationAddress.GeocodingApiLatLngGet());
				// //����g�n��
				if (!isdidSearch
						|| !SearchText.getText().toString()
								.equals(LastTimeSearchName)) {
					SearchPlace();
					isdidSearch = true;
					LastTimeSearchName = SearchText.getText().toString();
				}
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(),
						map.getCameraPosition().target.latitude + ","
								+ map.getCameraPosition().target.longitude);
				if (LoacationAddress.GeocodingApiLatLngGet() != null) {
					Longitude = LoacationAddress.GeocodingApiLatLngGet().longitude;
					Latitude = LoacationAddress.GeocodingApiLatLngGet().latitude;
				}
				// locationDesc = LoacationAddress.GeocodingApiAddressGet();
				// Toast.makeText(getApplicationContext(),
				// "����g�n��"+map.getCameraPosition().target.latitude+","+map.getCameraPosition().target.longitude+"\n�a�}:"+locationName,
				// Toast.LENGTH_SHORT).show();

				break;
			case R.id.Search:
				// textView2.setText(map.getMyLocation().toString());
				// //�i�κ������GPS��m
				if (!SearchText.getText().toString().equals(LastTimeSearchName)) {
					SearchPlace();
					isdidSearch = true;
					LastTimeSearchName = SearchText.getText().toString();
				}
				break;

			default:
				break;
			}
		}
	};

	// �a�ϲ��ʮɧ�s���w��m
	private GoogleMap.OnCameraChangeListener listener = new GoogleMap.OnCameraChangeListener() {

		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
			map.clear();
			LatLng now = new LatLng(position.target.latitude,
					position.target.longitude);
			map.addMarker(new MarkerOptions().title("�ت��a").position(now));
			if (isdidSearch)
				isDraped = true;
		}
	};

	// gps�ƥ�
	private Runnable GpsTime = new Runnable() {
		@Override
		public void run() {
			GpsUseTime++;
			// Timeout Sec, �W�LTIMEOUT�]�w�ɶ���,�����]�wFLAG�ϱogetCurrentLocation���
			// lastlocation.
			if (GpsUseTime > CommonUtils.GpsSetting.TIMEOUT_SEC) {
				if (CommonUtils.GpsSetting.GpsStatus) {
					gpsManager.stopListening();
					gpsManager.startNetWorkListening(getApplicationContext());
					CommonUtils.GpsSetting.GpsStatus = true;
					// Toast.makeText(getApplicationContext(), "����GPS",
					// Toast.LENGTH_LONG).show();
				}
			} else {
				GpsTimehandler.postDelayed(this, 1000);
			}
		}
	};

	// �j�M�a�I
	private void SearchPlace() {
		if (!SearchText.getText().toString().equals("")) {
			GeocodingAPI LoacationAddress2 = null;
			LatLng SearchLocation = null;
			LoacationAddress2 = new GeocodingAPI(getApplicationContext(),
					SearchText.getText().toString());
			// textView2.setText("");
			// locationName=LoacationAddress2.GeocodingApiAddressGet();
			// textView2.setText(textView2.getText()+"\n"+Address);
			SearchLocation = LoacationAddress2.GeocodingApiLatLngGet();
			// textView2.setText(textView2.getText()+"\n"+SearchLocation);
			if (SearchLocation != null) {
				map.animateCamera((CameraUpdateFactory.newLatLngZoom(
						SearchLocation, map.getMaxZoomLevel() - 4)));
				map.addMarker(new MarkerOptions().title("�j�M����m")
						.snippet(locationName).position(SearchLocation));
			} else {
				Toast.makeText(getApplicationContext(), "�d�L�a�I�@,���ӵ��ոլ�",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// �a�Ϲ�ܤ��
	private void showDialogLocationPicker() {
		// �s���G�����
		LayoutInflater li = getLayoutInflater();

		View v = li.inflate(
				R.layout.activity_task_editor_parts_dialog_location, null);

		new AlertDialog.Builder(this).setView(v).setMessage("jj")
				.setCancelable(false)
				.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				}).show();

	}
	/************************************** �a�I��ܾ����� ******************************************/
}

// * CLASS JUST FOR THE CUSTOM ALERT DIALOG
class CustomAlertDialog extends AlertDialog {
	public CustomAlertDialog(Context context) {
		super(context);
	}
}
>>>>>>> 90f650a3bd13712ec20e73f59bc083ca452f4916
