package org.minftel.mscrum.activities;

import java.util.ArrayList;
import java.util.List;

import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.tasks.DeleteTaskTask;
import org.minftel.mscrum.tasks.ModifyTaskAsk;
import org.minftel.mscrum.tasks.ModifyTaskSendTask;
import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class TaskActivity extends ListActivity implements OnGesturePerformedListener{

	private List<TaskDetail> taskList;
	private GestureLibrary gestureLib;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);

		//String[] countries = getResources().getStringArray(R.array.sprintsList);
		String[] taskNames = null;
		String[] taskTimes = null;

		String json = getIntent().getExtras().getString("tasks");
		Log.e(ScrumConstants.TAG, " " + json);

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
			taskTimes[i] = getString(R.string.user) + userName
					+ getString(R.string.time) + task.getTime();
		}
		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, taskNames,
				taskTimes));

		// Detecci�n de gesto
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.task, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
//		gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			// finish();
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);

		// Context Menu
		registerForContextMenu(getListView());

	}

	// Menu que sale al dejar pulsado una tarea
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

	// Menu que sale al pulsar tecla menu
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
			logOut();
			break;
		case R.id.AddTask:
			add();

		default:
			break;
		}
		return true;
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected task
		TaskDetail selectedTask = this.taskList.get(position);
		String userEmail = null;
		
//		Intent intent = new Intent(this, EditTaskActivity.class);
//		intent.putExtra("state", selectedTask.getState());
//		intent.putExtra("time", selectedTask.getTime());
//
//		if (selectedTask.getUser() == null) {
//
//			// If it doesn't have user, we edit with the current user.
//			SharedPreferences prefs;
//			prefs = getSharedPreferences(
//					ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
//			
//			prefs.getString("email", userEmail);
//			intent.putExtra("user", userEmail);
//			
//		} else {
//			intent.putExtra("user", selectedTask.getUser().getEmail());
//		}
//
//		intent.putExtra("description", selectedTask.getDescription());
//
//		startActivity(intent);

		 ModifyTaskAsk modifyTask = new ModifyTaskAsk(this);
		 modifyTask.execute(Integer.toString(selectedTask.getIdTask()));       

	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 2.0) {
				if(prediction.name.equalsIgnoreCase("toRight")){
					onBackPressed();
				}
				if(prediction.name.equalsIgnoreCase("logout")){
					logOut();
				}
				if(prediction.name.equalsIgnoreCase("add")){
					add();
				}
			}
		}
	}
	public void logOut() {

		SharedPreferences prefs = getSharedPreferences(
				ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
		prefs.edit().clear().commit();
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void add(){
		Intent intent = new Intent(this, AddTask.class);
		startActivity(intent);
	}
}
