package me.iamcxa.remindme.editor;

import common.MyCalendar;
import common.MyDebug;
import common.CommonVar;
import common.MyCursor.TaskCursor;

import me.iamcxa.remindme.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

public class TaskEditorMain extends Fragment {

	private static MultiAutoCompleteTextView taskTittle; 	//���ȼ��D
	private static EditText taskDueDate;					//���Ȩ����
	private static Spinner taskCategory;					//�������O
	private static Spinner taskPriority;					//�����u��
	private static ImageButton taskBtnDueDate;

	private static EditorVar mEditorVar=EditorVar.GetInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.activity_task_editor, container,false);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupViewComponent();
		setupStringArray();
		init(getActivity().getIntent());
	}

	private void setupStringArray(){
		String[] BasicStringArray =
				getResources().getStringArray(R.array.Array_Task_Editor_Date_Basic_Meaning_String);
		String[] RepeatStringArray =
				getResources().getStringArray(R.array.Array_Task_Editor_Date_Repeat_Meaning_String);
		CommonVar.TASKEDITOR_DUEDATE_BASIC_STRING_ARRAY=BasicStringArray;
		CommonVar.TASKEDITOR_DUEDATE_EXTRA_STRING_ARRAY=RepeatStringArray;
	}

	private void setupViewComponent(){
		//���ȼ��D��J��
		taskTittle =(MultiAutoCompleteTextView)getView().
				findViewById(R.id.multiAutoCompleteTextViewTittle);
		taskTittle.setHint("����");
		//taskTittle.setText(mEditorVar.Task.getTittle());

		//���ȴ�����J��
		taskDueDate =(EditText)getView().findViewById(R.id.editTextDueDate);
		taskDueDate.setHint("�����");
		//taskDueDate.setText(mEditorVar.Task.getDueDate());

		//���ȴ�����ܫ��s
		taskBtnDueDate=(ImageButton)getView().findViewById(R.id.imageButtonResetDate);
		//OnClickListener btnClcikListener;
		taskBtnDueDate.setOnClickListener(btnClcikListener);

		//�������O��ܮ�
		taskCategory=(Spinner)getActivity().findViewById(R.id.spinnerCategory);

		//���ȼ��ҿ�J��



	}
	
	//  �Ѹ�Ʈw��l���ܼ�
	public static void init(Intent intent) {
		Bundle b = intent.getBundleExtra(CommonVar.BundleName);
		if (b != null) {
			//�ѷ� ������TaskFieldContents/RemindmeVar.class���B, �T�O�ܼ����P���ǳ��ۦP
			mEditorVar.Task.setTaskId(b.getInt(TaskCursor.KEY._ID));
//			mEditorVar.Task.setTittle(b.getString(TaskCursor.KEY.TITTLE));
//			mEditorVar.Task.setContent(b.getString(TaskCursor.KEY.CONTENT));
//			mEditorVar.Task.setCreated(b.getString(TaskCursor.KEY.CREATED));
//			mEditorVar.Task.setDueDate(b.getString(TaskCursor.KEY.DUE_DATE));
//			mEditorVar.TaskAlert.setAlertInterval(b.getString(TaskCursor.KEY.ALERT_Interval));
//			mEditorVar.TaskAlert.setAlertTime(b.getString(TaskCursor.KEY.ALERT_TIME));
//			mEditorVar.TaskLocation.setLocationName(b.getString(TaskCursor.KEY.LOCATION_NAME));
//			mEditorVar.TaskLocation.setCoordinate(b.getString(TaskCursor.KEY.COORDINATE));
//			mEditorVar.TaskType.setCategory(b.getString(TaskCursor.KEY.CATEGORY));
//			mEditorVar.TaskType.setPriority(b.getInt(TaskCursor.KEY.PRIORITY));
//			mEditorVar.TaskType.setTag(b.getString(TaskCursor.KEY.TAG));
			
			TaskEditorMain.setTaskTittle(b.getString(TaskCursor.KEY.TITTLE));
			TaskEditorMain.setTaskDueDate(b.getString(TaskCursor.KEY.DUE_DATE));

			
		}
	}

	
	private  OnClickListener btnClcikListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.imageButtonResetDate:
				ShowTaskDueDateSelectMenu();
				break;

			default:
				break;
			}
		}

	};

	private TaskEditorMain ShowTaskDueDateSelectMenu() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View mview = inflater.inflate(
				R.layout.activity_task_editor_parts_dialog_duedate,
				null);

		new AlertDialog.Builder(getActivity())
		.setTitle("�п��...")
		.setView(mview)
		.setItems(R.array.Array_TaskEditor_btnTaskDueDate_String,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int which) {

				switch (which) {
				case 0:// ����
					taskDueDate.setText(
							MyCalendar.getCalendarToday(0));
					break;
				case 1:// ����
					taskDueDate.setText(
							MyCalendar.getCalendarToday(1));
					break;
				case 2:// �U�g
					taskDueDate.setText(
							MyCalendar.getCalendarToday(7));
					break;
				case 3:// �@�Ӥ뤺
					taskDueDate.setText(
							MyCalendar.getCalendarToday(30));
					break;
				case 4:// ��ܤ��

					break;
				case 5:// ����

					break;
				}
			}
		}).show();
		return this;
	}

	//-----------------TaskTittle------------------//
	// �T�O Title ��줣����
	public static String getTaskTittle() {
		String TaskTittleString = "null";
		// �p�GTaskTittle��줣���ūh��J�ϥΪ̿�J�ƭ�
		if (!(taskTittle.getText().toString().isEmpty())){
			TaskTittleString= taskTittle.getText().toString().trim();
		}
		MyDebug.MakeLog(0,"getTaskTittle:"+ TaskTittleString+",TaskTittle.len="+TaskTittleString.length());
		return TaskTittleString;
	}
	public static void setTaskTittle(String taskTittle) {
		TaskEditorMain.taskTittle.setText(taskTittle);
	}

	
	//-----------------TaskDueDate------------------//
	// �T�O  DueDate ��줣����
	public static String getTaskDueDate() {
		String rawTaskDueDateString = taskDueDate.getText().toString();
		String taskDueDateString="null";

		TaskEditorMain_DueDateCheck.checkDueDate(rawTaskDueDateString);

		
		return taskDueDateString;
	}	
	public static void setTaskDueDate(String taskDueDate) {
		TaskEditorMain.taskDueDate.setText(taskDueDate);
	}


	//-----------------TaskCategory------------------//
	public static Spinner getTaskCategory() {
		return taskCategory;
	}
	public static void setTaskCategory(Spinner taskCategory) {
		TaskEditorMain.taskCategory = taskCategory;
	}


	//-----------------TaskPriority------------------//
	public static Spinner getTaskPriority() {
		return taskPriority;
	}
	public static void setTaskPriority(Spinner taskPriority) {
		TaskEditorMain.taskPriority = taskPriority;
	}






}
