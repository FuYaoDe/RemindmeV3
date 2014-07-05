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

import me.iamcxa.remindme.R;
import me.iamcxa.remindme.RemindmeVar;
import me.iamcxa.remindme.provider.GPSCallback;
import me.iamcxa.remindme.provider.GPSManager;
import me.iamcxa.remindme.provider.GeocodingAPI;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import me.iamcxa.remindme.provider.WorkaroundMapFragment;

/**
 * @author cxa
 * 
 */

public class TaskEditorMain extends FragmentActivity implements GPSCallback {

	private static SaveOrUpdate mSaveOrUpdate;

	// 宣告GPS模組
	private static GPSManager gpsManager = null;

	// 宣告pick
	private static GoogleMap map;

	// 備忘錄訊息列表
	private ListView listView;


	private static EditText EditTextTittle;
	private static EditText SearchText;
	private static EditText datePicker, timePicker, contentBox, locationBox;

	// 日期顯示TextView
	private static TextView dateTittle;
	private static TextView dateDesc, locationDesc;
	// 時間顯示TextView
	private static TextView timeTittle;
	private static TextView timeDesc;
	// 提醒內容TextView
	private static TextView contentTittle;
	private static TextView contentDesc;
	private static TextView locationTittle;

	private static Button Search;

	private static ImageButton OK;

	private static ScrollView main_scrollview;

	private static CheckBox checkBoxIsFixed;

	private Handler GpsTimehandler = new Handler();


	// 多選框
	private CheckedTextView ctv1, ctv2;
	// 存取佈局實例
	private static LayoutInflater li;

	// 初始化方法
	private void init(Intent intent) {
		Bundle b = intent.getBundleExtra("b");
		if (b != null) {
			EditorVar.taskId = b.getInt("taskId");
			EditorVar.tittle = b.getString("tittle");
			EditorVar.created = b.getString("created");
			EditorVar.endDate = b.getString("endDate");
			EditorVar.endTime = b.getString("endTime");
			EditorVar.content = b.getString("content");
			EditorVar.isRepeat = b.getString("isRepeat");
			EditorVar.isFixed = b.getString("isFixed");
			EditorVar.locationName = b.getString("locationName");
			EditorVar.coordinate = b.getString("coordinate");
			EditorVar.collaborator = b.getString("collaborator");

			if (EditorVar.endDate != null && EditorVar.endDate.length() > 0) {
				String[] strs = EditorVar.endDate.split("/");
				EditorVar.mYear = Integer.parseInt(strs[0]);
				EditorVar.mMonth = Integer.parseInt(strs[1]) - 1;
				EditorVar.mDay = Integer.parseInt(strs[2]);
			}

			if (EditorVar.endTime != null && EditorVar.endTime.length() > 0) {
				String[] strs = EditorVar.endTime.split(":");
				EditorVar.mHour = Integer.parseInt(strs[0]);
				EditorVar.mMinute = Integer.parseInt(strs[1]);
			}

			EditTextTittle.setText(EditorVar.tittle);

		}

		Toast.makeText(getApplicationContext(),
				EditorVar.taskId + "," +
						EditorVar.content + "," +
						EditorVar.endDate + "," +
						EditorVar.endTime,
						Toast.LENGTH_LONG).show();

	}

	public View vv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor);
		checkBoxIsFixed = (CheckBox) findViewById(R.id.checkBoxIsFixed);
		SearchText = (EditText) findViewById(R.id.SearchText);
		Search = (Button) findViewById(R.id.Search);
		OK = (ImageButton) findViewById(R.id.OK);
		//Search.setOnClickListener(SearchPlace);
		//OK.setOnClickListener(SearchPlace);
		//gpsManager = new GPSManager();
		//gpsManager.startGpsListening(getApplicationContext());
		//gpsManager.setGPSCallback(RemindmeTaskEditor.this);
		RemindmeVar.GpsSetting.GpsStatus = true;
		EditorVar.GpsUseTime = 0;
		GpsTimehandler.post(GpsTime);

		// map = ((MapFragment) getFragmentManager()
		// .findFragmentById(R.id.map)).getMap();
		//map = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		main_scrollview = (ScrollView) findViewById(R.id.main_scrollview);

//		((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(
//				R.id.map))
//				.setListener(new WorkaroundMapFragment.OnTouchListener() {
//					@Override
//					public void onTouch() {
//						main_scrollview
//						.requestDisallowInterceptTouchEvent(true);
//					}
//				});
		//map.setMyLocationEnabled(true);
		//map.clear();
//		LatLng nowLoacation;
//		if (gpsManager.LastLocation() != null) {
//			nowLoacation = new LatLng(gpsManager.LastLocation().getLatitude(),
//					gpsManager.LastLocation().getLongitude());
//			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
//					map.getMaxZoomLevel() - 4)));
//		} else {
//			nowLoacation = new LatLng(23.6978, 120.961);
//			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
//					map.getMinZoomLevel() + 7)));
//		}
//		map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
//				.position(nowLoacation));
//
//		map.setOnCameraChangeListener(listener);
		// 取得Intent
		final Intent intent = getIntent();
		// 設定Uri
		if (intent.getData() == null) {
			intent.setData(RemindmeVar.CONTENT_URI);
		}

		/*
		 * //String[] ops={"旅行","交易","購物","提醒","特急"};
		 * 
		 * // spinner spinnerTag=(Spinner)findViewById(R.id.spinnerTag);
		 * ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this,
		 * android.R.layout.simple_spinner_item,ops);
		 * spinnerTag.setAdapter(spinnerAdapter); spinnerTag.setPrompt("選擇類型");
		 */

		// 標題輸入欄位
		EditTextTittle = (EditText) findViewById(R.id.editTextTittle);
		// EditTextTittle.setHint("您能輸入\"123 9. 星巴克 裝文青 \"快速設定");
		// EditTextTittle.setTextSize(textsize(5));
		// EditTextTittle.setHintTextColor(R.color.background_window);

		// 取得Calendar實例
		final Calendar c = Calendar.getInstance();

		// 取得目前日期、時間
		EditorVar.mYear = c.get(Calendar.YEAR);
		EditorVar.mMonth = (c.get(Calendar.MONTH));
		EditorVar.mDay = c.get(Calendar.DAY_OF_MONTH);
		EditorVar.mHour = c.get(Calendar.HOUR_OF_DAY);
		EditorVar.mMinute = c.get(Calendar.MINUTE);

		// 取得ListView
		listView = (ListView) (findViewById(R.id.listView1));
		// 實例化LayoutInflater
		li = getLayoutInflater();
		// 設定ListView Adapter
		try {
			listView.setAdapter(new ViewAdapter());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 可多選
		//listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

		// 回應列表單擊事件
		//listView.setOnItemClickListener(ViewAdapterClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 初始化列表
		init(getIntent());
	}

	// ListView Adatper，該類別實作列表的每一項透過自定視圖實現
	static class ViewAdapter extends BaseAdapter {
		// 列表內容
		String[] strs = { "截止日", "提醒時間", "備註" };

		// 取得列表數量
		// @Override
		@Override
		public int getCount() {
			return strs.length;
		}

		// 取得列表項目
		// @Override
		@Override
		public Object getItem(int position) {
			return position;
		}

		// 返回列表ID
		// @Override
		@Override
		public long getItemId(int position) {
			return position;
		}

		// 取得目前列表項目視圖
		// @Override
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 自定示圖layout
			View textView = li.inflate(
					R.layout.activity_task_editor_parts_textview, null);
			// View editView =
			// li.inflate(R.layout.activity_event_editor_parts_editview, null);
			switch (position) {
			// 是否開啟該筆備忘錄
			/*
			 * case 0: ctv1 = (CheckedTextView) li .inflate(
			 * android.R.layout.simple_list_item_multiple_choice, null);
			 * ctv1.setText(strs[position]); if (on_off == 0) {
			 * ctv1.setChecked(false); } else { ctv1.setChecked(true); } return
			 * ctv1;
			 */
			// 提醒日期
			case 0:
				// datePicker=(EditText)v.findViewById(R.id.editTextbox);
				// datePicker.setHint("輕觸以選擇日期");
				// datePicker.setHintTextColor(R.color.background_window);
				// datePicker.setText(mYear + "/" + mMonth + "/" + mDay);

				dateTittle = (TextView) textView.findViewById(R.id.name);
				dateDesc = (TextView) textView.findViewById(R.id.desc);
				dateTittle.setText(strs[position]);
				//dateDesc.setText(mYear + "/" + mMonth + 1 + "/" + mDay);
				return textView;
				// 提醒時間
			case 1:
				// timePicker=(EditText)v.findViewById(R.id.editTextbox);
				// timePicker.setHint("輕觸以選擇時間");
				// timePicker.setHintTextColor(R.color.background_window);
				// timePicker.setText(mHour + ":" + mMinute);
				timeTittle = (TextView) textView.findViewById(R.id.name);
				timeDesc = (TextView) textView.findViewById(R.id.desc);
				timeTittle.setText(strs[position]);
				//timeDesc.setText(mHour + ":" + mMinute);
				return textView;
				// 提醒內容
			case 2:
				// contentBox=(EditText)
				// editView.findViewById(R.id.editTextbox);
				// contentBox.setHint("輕觸以輸入內容");
				// contentBox.setHintTextColor(R.color.background_window);
				// contentBox.setText(content);

				contentTittle = (TextView) textView.findViewById(R.id.name);
				contentDesc = (TextView) textView.findViewById(R.id.desc);
				contentTittle.setText(strs[position]);
				contentDesc.setText(EditorVar.content);

				contentDesc.setTextColor(Color.GRAY);
				return textView;
				// 地點選擇與輸入
				// 是否開啟提醒
				/*
				 * case 5: ctv2 = (CheckedTextView) li .inflate(
				 * android.R.layout.simple_list_item_multiple_choice, null);
				 * ctv2.setText(strs[position]);
				 * 
				 * if (alarm == 0) { ctv2.setChecked(false); } else {
				 * ctv2.setChecked(true); } return ctv2;
				 */
			default:
				break;
			}

			return null;
		}
	}

	private OnItemClickListener ViewAdapterClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int position, long id) {

			switch (position) {
			// 設定是否開啟提醒
			/*
			 * case 0:
			 * 
			 * ctv1 = (CheckedTextView) v; if (ctv1.isChecked()) { on_off = 0; }
			 * else { on_off = 1; }
			 * 
			 * break;
			 */
			// 設定提醒日期
			case 0:
				showDialog(EditorVar.DATE_DIALOG_ID);
				break;
				// 設定提醒時間
			case 1:
				showDialog(EditorVar.TIME_DIALOG_ID);
				break;
				// 設定提醒內容
			case 2:
				showDialog1("請輸入內容：", "內容", position);
				break;

				// 設定是否開啟語音提醒
			default:
				break;
			}
		}

	};

	// 顯示對話方塊
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		// 顯示日期對話方塊
		case EditorVar.DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					mDateSetListener, EditorVar.mYear,
					EditorVar.mMonth - 1, EditorVar.mDay);
			// 顯示時間對話方塊
		case EditorVar.TIME_DIALOG_ID:
			return new TimePickerDialog(this, 
					mTimeSetListener, EditorVar.mHour, EditorVar.mMinute,
					false);
		}
		return null;
	}

	// 設定通知提示
	private void setAlarm(boolean flag) {
		final String BC_ACTION = "com.amaker.ch17.TaskReceiver";
		// 取得AlarmManager實例
		final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// 實例化Intent
		Intent intent = new Intent();
		// 設定Intent action屬性
		intent.setAction(BC_ACTION);
		intent.putExtra("msg", EditorVar.content);
		// 實例化PendingIntent
		final PendingIntent pi = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		// 取得系統時間
		final long time1 = System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		c.set(EditorVar.mYear, EditorVar.mMonth, EditorVar.mDay, EditorVar.mHour, EditorVar.mMinute);
		long time2 = c.getTimeInMillis();
		if (flag && (time2 - time1) > 0 && EditorVar.on_off == 1) {
			am.set(AlarmManager.RTC_WAKEUP, time2, pi);
		} else {
			am.cancel(pi);
		}
	}

	/*
	 * 設定提示日期對話方塊
	 */
	private void showDialog1(String msg, String tittle, int target) {
		View v = li.inflate(R.layout.activity_task_editor_parts_textedit, null);
		final TextView editTextTittle = (TextView) v.findViewById(R.id.name);
		final EditText editTextbox = (EditText) v.findViewById(R.id.editTexbox);
		editTextTittle.setText(tittle + target);

		switch (target) {
		case 2:
			EditorVar.switcher = EditorVar.content;
			break;
		default:
			break;
		}

		editTextbox.setText(EditorVar.switcher);

		new AlertDialog.Builder(this).setView(v).setMessage(msg)
		.setCancelable(false)
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				EditorVar.content = editTextbox.getText().toString();

				contentDesc.setText(EditorVar.switcher);
				// locationDesc.setText(switcher);
			}
		}).show();

	}

	// 時間選擇對話方塊
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			EditorVar.mHour = hourOfDay;
			EditorVar.mMinute = minute;
			timeDesc.setText(EditorVar.mHour + ":" + EditorVar.mMinute);
		}
	};
	// 日期選擇對話方塊
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			EditorVar.mYear = year;
			EditorVar.mMonth = monthOfYear;
			EditorVar.mDay = dayOfMonth;
			dateDesc.setText(EditorVar.mYear + "/" + (EditorVar.mMonth + 1) + "/" + EditorVar.mDay);
		}
	};

	// 儲存或修改備忘錄資訊
	@Override
	protected void onPause() {
		super.onPause();
		EditorVar.locationName = null;
		EditorVar.content = null;
	};

	// This is the action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 抓取editor_activity_actionbar.xml內容
		getMenuInflater().inflate(R.menu.editor_activity_actionbar, menu);

		// 啟用actionbar返回首頁箭頭
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("建立待辦事項");

		// actionAdd
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		actionAdd.setOnMenuItemClickListener(btnActionAddClick);

		// actionCancel
		MenuItem actionCancel = menu.findItem(R.id.action_cancel);
		actionCancel.setOnMenuItemClickListener(btnActionCancelClick);

		return true;
	}

	// actionbar箭頭返回首頁動作
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

			if (!EditorVar.isdidSearch && !SearchText.getText().toString().equals("")) {
				// SearchPlace();
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(), SearchText.getText()
						.toString());
				if (LoacationAddress.GeocodingApiLatLngGet() != null) {
					EditorVar.Longitude = LoacationAddress.GeocodingApiLatLngGet().longitude;
					EditorVar.Latitude = LoacationAddress.GeocodingApiLatLngGet().latitude;
				}
			}
			if (EditorVar.isDraped && !SearchText.getText().toString().equals("")) {
				EditorVar.Longitude = map.getCameraPosition().target.longitude;
				EditorVar.Latitude = map.getCameraPosition().target.latitude;
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(), EditorVar.Latitude + "," + EditorVar.Longitude);
				if (LoacationAddress.GeocodingApiAddressGet() != null) {
					SearchText.setText(LoacationAddress
							.GeocodingApiAddressGet());
				}
			}


			//			// 存入標題
			//			values.put(TaskCursor.KeyColumns.Tittle, EditTextTittle.getText()
			//					.toString());
			//			// 存入日期
			//			values.put(TaskCursor.KeyColumns.StartDate, curDate.toString());
			//			values.put(TaskCursor.KeyColumns.EndDate, dateDesc.getText()
			//					.toString());
			//			// save the selected value of time
			//			values.put(TaskCursor.KeyColumns.StartTime, curDate.toString());
			//			values.put(TaskCursor.KeyColumns.EndTime, timeDesc.getText()
			//					.toString());
			//			// save contents
			//			values.put(TaskCursor.KeyColumns.CONTENT, contentDesc.getText()
			//					.toString());
			//			// save the name string of location
			//			values.put(TaskCursor.KeyColumns.LocationName, SearchText.getText()
			//					.toString());
			//			values.put(TaskCursor.KeyColumns.Coordinate, Latitude + ","
			//					+ Longitude);
			//			values.put(TaskCursor.KeyColumns.Priority, 1000);

			if (checkBoxIsFixed != null) {
				EditorVar.is_Fixed = String.valueOf(checkBoxIsFixed.isChecked());
				EditorVar.endDate = dateDesc.getText().toString();
				//endTime = timeDesc.getText().toString();
				//content = contentDesc.getText().toString();
				EditorVar.tittle = EditTextTittle.getText().toString();
				EditorVar.coordinate = EditorVar.Latitude + "," + EditorVar.Longitude;
				EditorVar.locationName=SearchText.getText()
						.toString();
			}

			mSaveOrUpdate = new SaveOrUpdate(getApplicationContext());
			mSaveOrUpdate.DoTaskEditorAdding(EditorVar.taskId, EditorVar.tittle, EditorVar.endDate, EditorVar.endTime,
					EditorVar.content, EditorVar.locationName, EditorVar.coordinate, "1", EditorVar.is_Fixed, "1");
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

	// GPS位置抓到時會更新位置
	@Override
	public void onGPSUpdate(Location location) {
		// TODO Auto-generated method stub
		Double Longitude = location.getLongitude();
		// 緯度
		Double Latitude = location.getLatitude();

		// textView1.setText("經緯度:"+Latitude+","+Longitude);
		// 拿到經緯度後馬上關閉
		// Toast.makeText(getApplicationContext(), "關閉GPS"+location,
		// Toast.LENGTH_LONG).show();

		if (RemindmeVar.GpsSetting.GpsStatus) {
			RemindmeVar.GpsSetting.GpsStatus = false;
			gpsManager.stopListening();
			gpsManager.setGPSCallback(null);
			gpsManager = null;
		} else {
			RemindmeVar.GpsSetting.GpsStatus = false;
		}
		LatLng nowLoacation = new LatLng(Latitude, Longitude);

		map.setMyLocationEnabled(true);

		map.clear();

		map.animateCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
				map.getMaxZoomLevel() - 4)));

		map.addMarker(new MarkerOptions().title("目前位置").position(nowLoacation));

		// GeocodingAPI LoacationAddress = new
		// GeocodingAPI(getApplicationContext(),Latitude+","+Longitude);
		// textView2.setText(textView2.getText()+" "+LoacationAddress.GeocodingApiAddressGet());
	}

	private Button.OnClickListener SearchPlace = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 宣告GPSManager
			switch (v.getId()) {
			case R.id.OK:
				// textView2.setText(textView2.getText()+"\n"+LoacationAddress.GeocodingApiAddressGet());
				// //獲取地址
				// textView2.setText(textView2.getText()+"\n"+LoacationAddress.GeocodingApiLatLngGet());
				// //獲取經緯度
				if (!EditorVar.isdidSearch
						|| !SearchText.getText().toString()
						.equals(EditorVar.LastTimeSearchName)) {
					SearchPlace();
					EditorVar.isdidSearch = true;
					EditorVar.LastTimeSearchName = SearchText.getText().toString();
				}
				GeocodingAPI LoacationAddress = new GeocodingAPI(
						getApplicationContext(),
						map.getCameraPosition().target.latitude + ","
								+ map.getCameraPosition().target.longitude);
				if (LoacationAddress.GeocodingApiLatLngGet() != null) {
					EditorVar.Longitude = LoacationAddress.GeocodingApiLatLngGet().longitude;
					EditorVar.Latitude = LoacationAddress.GeocodingApiLatLngGet().latitude;
				}
				// locationDesc = LoacationAddress.GeocodingApiAddressGet();
				// Toast.makeText(getApplicationContext(),
				// "獲取經緯度"+map.getCameraPosition().target.latitude+","+map.getCameraPosition().target.longitude+"\n地址:"+locationName,
				// Toast.LENGTH_SHORT).show();

				break;
			case R.id.Search:
				// textView2.setText(map.getMyLocation().toString());
				// //可用網路抓到GPS位置
				if (!SearchText.getText().toString().equals(EditorVar.LastTimeSearchName)) {
					SearchPlace();
					EditorVar.isdidSearch = true;
					EditorVar.LastTimeSearchName = SearchText.getText().toString();
				}
				break;

			default:
				break;
			}
		}
	};

	// 地圖移動時更新指針位置
	private GoogleMap.OnCameraChangeListener listener = new GoogleMap.OnCameraChangeListener() {

		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
			map.clear();
			LatLng now = new LatLng(position.target.latitude,
					position.target.longitude);
			map.addMarker(new MarkerOptions().title("目的地").position(now));
			if (EditorVar.isdidSearch)
				EditorVar.isDraped = true;
		}
	};

	private Runnable GpsTime = new Runnable() {
		@Override
		public void run() {
			EditorVar.GpsUseTime++;
			// Timeout Sec, 超過TIMEOUT設定時間後,直接設定FLAG使得getCurrentLocation抓取
			// lastlocation.
			if (EditorVar.GpsUseTime > RemindmeVar.GpsSetting.TIMEOUT_SEC) {
				if (RemindmeVar.GpsSetting.GpsStatus) {
					gpsManager.stopListening();
					gpsManager.startNetWorkListening(getApplicationContext());
					RemindmeVar.GpsSetting.GpsStatus = true;
					// Toast.makeText(getApplicationContext(), "關閉GPS",
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
				map.addMarker(new MarkerOptions().title("搜尋的位置")
						.snippet(EditorVar.locationName).position(SearchLocation));
			} else {
				Toast.makeText(getApplicationContext(), "查無地點哦,換個詞試試看",
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
