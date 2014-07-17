package me.iamcxa.remindme.editor;

import me.iamcxa.remindme.R;
import me.iamcxa.remindme.RemindmeVar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

public class TaskEditorMain extends Fragment {

	private static MultiAutoCompleteTextView taskTittle; //���ȼ��D
	private static EditText taskDueDate;//���Ȩ����
	private static Spinner taskCategory;//�������O
	private static Spinner taskPriority;//�����u��

	private EditorVar mEditorVar ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_task_editor, container, false);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		setupViewComponent();
	}


	private void setupViewComponent(){
		//���ȼ��D��J��
		taskTittle =(MultiAutoCompleteTextView)getView().
				findViewById(R.id.multiAutoCompleteTextViewTittle);
		taskTittle.setHint("����");

		//���Ȥ����J��
		taskDueDate =(EditText)getView().findViewById(R.id.editTextDueDate);
		taskDueDate.setHint("�����");

		//�������O��ܮ�
		taskCategory=(Spinner)getActivity().findViewById(R.id.spinnerCategory);



		//���ȼ��ҿ�J��





	}


	public static String getTaskDueDate() {
		String taskDueDateString = "null";
		if (!(taskDueDate.getText().toString().isEmpty())){
			taskDueDateString=taskDueDate.getText().toString().trim();
		}
		
		RemindmeVar.debugMsg(0,"getTaskDueDate:"+ taskDueDateString.isEmpty());
		return taskDueDateString;
	}
	public static String getTaskTittle() {
		String TaskTittleString = "null";
		if (!(taskTittle.getText().toString().isEmpty())){
			TaskTittleString= taskTittle.getText().toString().trim();
		}
		RemindmeVar.debugMsg(0,"getTaskTittle:"+ TaskTittleString+",TaskTittle.len="+TaskTittleString.length());
		return TaskTittleString;
	}

}
