package me.iamcxa.remindme.editor;

import me.iamcxa.remindme.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TaskEditorTab extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);

        setupViewComponent();
    }
    
    private void setupViewComponent() {
        final ActionBar actBar = getActionBar();
        actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
//        Fragment fragMarriSug = new RemindmeTaskEditor();
//        actBar.addTab(actBar.newTab()
//        		.setText("婚姻建議")
//        		.setIcon(getResources().getDrawable(android.R.drawable.ic_lock_idle_alarm))
//        		.setTabListener(new MyTabListener(fragMarriSug)));
//
//        Fragment fragGame = new GameFragment();
//        actBar.addTab(actBar.newTab()
//        		.setText("電腦猜拳遊戲")
//        		.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
//        		.setTabListener(new MyTabListener(fragGame)));
//
//        Fragment fragVideo = new VideoFragment();
//        actBar.addTab(actBar.newTab()
//        		.setText("播放影片")
//        		.setIcon(getResources().getDrawable(android.R.drawable.ic_media_play))
//        		.setTabListener(new MyTabListener(fragVideo)));
//
//        Fragment fragList = new ExpList();
//        actBar.addTab(actBar.newTab()
//        		.setText("ExpandableListView")
//        		.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info))
//        		.setTabListener(new MyTabListener(fragList)));
    }
}