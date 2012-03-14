package org.minftel.mscrum.activities;

import java.util.List;

import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.tasks.DeleteTaskTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class TaskActivity extends ListActivity {

	private List<TaskDetail> taskList;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);

		String[] taskNames = null;
		String[] taskTimes = null;

		registerForContextMenu(getListView());

		String json = getIntent().getExtras().getString("tasks");

		try {
			taskList = JSONConverter.fromJSONtoTaskList(json);
			taskTimes = new String[taskList.size()];
			taskNames = new String[taskList.size()];
		} catch (Exception e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}

		for (int i = 0; i < taskList.size(); i++) {
            TaskDetail task = taskList.get(i);
            taskNames[i] = "" + task.getDescription();
            String userName = "N/A";
            if (task.getUser() != null)
                    userName = task.getUser().getName();
            taskTimes[i] = getString(R.string.user)  + userName + getString(R.string.time) + task.getTime();
		}
		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, taskNames,
				taskTimes));

	}

	//Menu que sale al dejar pulsado  una tarea
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ctx_task, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		TaskDetail taskdetail = this.taskList.get(info.position);

		switch (item.getItemId()) {
		case R.id.deleteTask:
			// Delete task
			DeleteTaskTask daleteTaskTask = new DeleteTaskTask(this);
			daleteTaskTask.execute(Integer.toString(taskdetail.getIdTask()));

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	//Menu que sale al pulsar tecla menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_task, menu);
		return true;
	}

	/** Called when an item is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.LogOut:
			SharedPreferences prefs = getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
			prefs.edit().clear().commit();
			intent = new Intent(this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
			break;
		case R.id.AddTask:
			intent = new Intent(this, AddTask.class);
			startActivity(intent);

		default:
			break;
		}
		return true;
	}


	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected task
		TaskDetail selectedTask = this.taskList.get(position);
		String user="";
		
		Intent intent = new Intent(this, EditTaskActivity.class);
		intent.putExtra("state", selectedTask.getState());
		intent.putExtra("time", selectedTask.getTime());
		
		if(selectedTask.getUser().getName()==null){
			
			SharedPreferences prefs;
			prefs = getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
			prefs.getString("userEmail", user);
			intent.putExtra("user", user);
		}else
		{
			intent.putExtra("user", selectedTask.getUser().getName());
		}
		
//		intent.putExtra("sprint", selectedTask.getSprint().getSprintNumber());
		intent.putExtra("description", selectedTask.getDescription());
		
		startActivity(intent);

	}

}
