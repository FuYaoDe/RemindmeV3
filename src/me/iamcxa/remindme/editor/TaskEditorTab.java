package me.iamcxa.remindme.editor;

import common.CommonVar;
import common.CommonVar.TaskCursor;

import me.iamcxa.remindme.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TaskEditorTab extends FragmentActivity  {


	private static EditorVar mEditorVar=EditorVar.GetInstance();
	private static SaveOrUpdate mSaveOrUpdate;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_editor_tab);
		setupViewComponent();
	}

	// This is the action bar menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ���editor_activity_actionbar.xml���e
		getMenuInflater().inflate(R.menu.editor_activity_actionbar, menu);

		// �ҥ�action bar��^�����b�Y
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("�إ߫ݿ�ƶ�");

		// actionAdd
		MenuItem actionAdd = menu.findItem(R.id.action_add);
		actionAdd.setOnMenuItemClickListener(btnClickListener);

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

	//  �Ѹ�Ʈw��l���ܼ�
	public static void init(Intent intent) {
		Bundle b = intent.getBundleExtra(TaskCursor.KEY._Bundle);
		if (b != null) {
			//�ѷ� ������TaskFieldContents/RemindmeVar.class���B, �T�O�ܼ����P���ǳ��ۦP
			mEditorVar.Task.setTaskId(b.getInt(TaskCursor.KEY._ID));
			mEditorVar.Task.setTittle(b.getString(TaskCursor.KEY.TITTLE));
			mEditorVar.Task.setContent(b.getString(TaskCursor.KEY.CONTENT));
			mEditorVar.Task.setCreated(b.getString(TaskCursor.KEY.CREATED));
			mEditorVar.Task.setDueDate(b.getString(TaskCursor.KEY.DUE_DATE));
			mEditorVar.TaskAlert.setAlertInterval(b.getString(TaskCursor.KEY.ALERT_Interval));
			mEditorVar.TaskAlert.setAlertTime(b.getString(TaskCursor.KEY.ALERT_TIME));
			mEditorVar.TaskLocation.setLocationName(b.getString(TaskCursor.KEY.LOCATION_NAME));
			mEditorVar.TaskLocation.setCoordinate(b.getString(TaskCursor.KEY.COORDINATE));
			mEditorVar.TaskType.setCategory(b.getString(TaskCursor.KEY.CATEGORY));
			mEditorVar.TaskType.setPriority(b.getInt(TaskCursor.KEY.PRIORITY));
			mEditorVar.TaskType.setTag(b.getString(TaskCursor.KEY.TAG));

			if (b.getString("dueDate") != null && b.getString("dueDate").length() > 0) {
				String[] dateStr = mEditorVar.Task.getDueDate().split("/");
				mEditorVar.TaskDate.setmYear(Integer.parseInt(dateStr[0]));
				mEditorVar.TaskDate.setmMonth(Integer.parseInt(dateStr[1]) - 1);
				mEditorVar.TaskDate.setmDay(Integer.parseInt(dateStr[2]));
			}

			if (b.getString("alertTime") != null && b.getString("alertTime").length() > 0) {
				String[] timeStr = mEditorVar.TaskAlert.getAlertTime().split(":");
				mEditorVar.TaskDate.setmHour (Integer.parseInt(timeStr[0]));
				mEditorVar.TaskDate.setmMinute(Integer.parseInt(timeStr[1]));
			}
		}
	}



	//�]�wtab
	private void setupViewComponent() {
		final ActionBar actBar = getActionBar();
		actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Fragment fragMarriSug =new TaskEditorMain();
		actBar.addTab(actBar.newTab()
				//.setText("����")
				.setIcon(getResources().getDrawable(R.drawable.tear_of_calendar))
				.setTabListener(new MyTabListener(fragMarriSug)));

		Fragment fragGame = new TaskEditorLocation();
		actBar.addTab(actBar.newTab()
				//.setText("��m")
				.setIcon(getResources().getDrawable(R.drawable.map_marker))
				.setTabListener(new MyTabListener(fragGame)));

		Fragment fragVideo = new TaskEditorMain();
		actBar.addTab(actBar.newTab()
				//.setText("����v��")
				.setIcon(getResources().getDrawable(android.R.drawable.ic_media_play))
				.setTabListener(new MyTabListener(fragVideo)));
	}




	// ���s��ť
	private MenuItem.OnMenuItemClickListener btnClickListener = new MenuItem.OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			String itemName=String.valueOf(item.getTitle());
			Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_SHORT).show();
			if (itemName.contentEquals( "action_add")){

				btnActionAdd();

			}else if (itemName.contentEquals( "action_add")) {
				//btnActionCancel();//�Ȯɨ������\��
			}


			return false;
		}

	};

	//��view������o��J��T
	private void getDataFromView(){
		mEditorVar.Task.setTittle(TaskEditorMain.getTaskTittle());	
		mEditorVar.Task.setContent(TaskEditorMain.getTaskTittle());	
		mEditorVar.Task.setDueDate(TaskEditorMain.getTaskDueDate());	


	}

	private void btnActionAdd(){
		//�ˬdtitle�O�_����
		boolean isEmpty=(TaskEditorMain.getTaskTittle().contentEquals("null"));
		if(!isEmpty){
			getDataFromView();

			String TaskField_Main=
					mEditorVar.Task.getTaskId()+","+		
							mEditorVar.Task.getTittle()+","+		
							mEditorVar.Task.getContent()+","+		
							mEditorVar.Task.getCreated()+","+		
							mEditorVar.Task.getDueDate();
			CommonVar.debugMsg(0,"TaskField_Main="+ TaskField_Main);

			String TaskField_Location=
					mEditorVar.TaskLocation.getCoordinate()+","+	
							mEditorVar.TaskLocation.getLocationName();
			CommonVar.debugMsg(0,"TaskField_Location="+ TaskField_Location);

			String TaskField_Alert=
					mEditorVar.TaskAlert .getAlertInterval()+","+	
							mEditorVar.TaskAlert.getAlertTime();
			CommonVar.debugMsg(0,"TaskField_Alert="+ TaskField_Alert);

			String TaskField_Type=
					mEditorVar.TaskType.getPriority()+","+		
							mEditorVar.TaskType.getCategory()+","+	
							mEditorVar.TaskType.getTag();			
			CommonVar.debugMsg(0,"TaskField_Type="+ TaskField_Type);	

			mSaveOrUpdate = new SaveOrUpdate(getApplicationContext());
			mSaveOrUpdate.DoTaskEditorAdding(
					TaskField_Main,
					TaskField_Location,
					TaskField_Alert,
					TaskField_Type
					);
			finish();
		}else {
			String EmptyMsg="�z�ݭn��J���ȦW�١C";
			Toast.makeText(getApplicationContext(),EmptyMsg , Toast.LENGTH_SHORT).show();
		}

	}


}