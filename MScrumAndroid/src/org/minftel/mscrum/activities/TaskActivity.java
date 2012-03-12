package org.minftel.mscrum.activities;

import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TaskActivity extends ListActivity {

	private List<TaskDetail> taskList;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);
		Context ctx = getApplicationContext();

		// PRUEBA
				registerForContextMenu(getListView());
		// FIN PRUEBA
				
		// Get Project List
		String json = getIntent().getExtras().getString("task");
		
		try {
			taskList = JSONConverter.fromJSONtoTaskList(json);
		} catch (JSONException e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}
		
		int size = taskList.size();
		
		String[] taskNames = new String[size];
		String[] taskTimes = new String[size];
		
		for (int i = 0; i < size; i++) {
			TaskDetail task = taskList.get(i);
			taskNames[i] = task.getName();
			taskTimes[i] = "Time: "+task.getTime();
		}

		setListAdapter(new TextAdapter(ctx, R.layout.list_item, taskNames,taskTimes));

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_task, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.addTask:
			// View task description
			return true;
		case R.id.deleteTask:
			// Delete Project
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected project
		TaskDetail selectedTask = this.taskList.get(position);

		Toast.makeText(this,
				"Project selected: " +  selectedTask.getDescription() + 
				" [ ID: " + selectedTask.getIdTask() + " ]" +  "Time: " + selectedTask.getTime(),
				Toast.LENGTH_SHORT).show();
	

	}

	
}
