package org.minftel.mscrum.activities;

import java.util.List;

import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.SprintDetail;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.tasks.AddTaskTask;
import org.minftel.mscrum.tasks.DeleteTaskTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TaskActivity extends ListActivity {

	private static final int REQUEST_CODE = 1;
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
		case R.id.addTask:
			Intent intent = new Intent(this, AddTask.class);
			startActivity(intent);

			return true;
		case R.id.deleteTask:
			// Delete task
			DeleteTaskTask daleteTaskTask = new DeleteTaskTask(this);
			daleteTaskTask.execute(Integer.toString(taskdetail.getIdTask()));

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_task, menu);
		return true;
	}

	/** Called when an item is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.LogOut:
			Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
			SharedPreferences prefs;
			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			prefs.edit().putString("userEmail", "").putString("pass", "")
					.commit();
			break;
		case R.id.AddTask:
			
			Intent intent = new Intent(this, AddTask.class);
			startActivity(intent);

		default:
			break;
		}
		return true;
	}


	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected task
		TaskDetail selectedTask = this.taskList.get(position);

		Intent intent = new Intent(this, EditTaskActivity.class);
		intent.putExtra("state", selectedTask.getState());
		intent.putExtra("time", selectedTask.getTime());
		intent.putExtra("user", selectedTask.getUser().getName());
//		intent.putExtra("sprint", selectedTask.getSprint().getSprintNumber());
		intent.putExtra("description", selectedTask.getDescription());
		
		startActivity(intent);

	}

}
