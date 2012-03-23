package org.minftel.mscrum.activities;

import java.util.ArrayList;
import java.util.List;

import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.tasks.CloseSessionTask;
import org.minftel.mscrum.tasks.DeleteTaskTask;
import org.minftel.mscrum.tasks.ModifyTaskAsk;
import org.minftel.mscrum.utils.IconContextMenu;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class TaskActivity extends ListActivity implements
		OnGesturePerformedListener {

	private List<TaskDetail> taskList;
	private GestureLibrary gestureLib;

	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	private int pos;
	private ListActivity activity;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);

		activity = this;
		Resources res = getResources();
		
		// Initialize the menu
		iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
		iconContextMenu.addItem(res, R.string.menu_delete_task,
				R.drawable.discard, R.id.ctx_menu_delete);

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

		// Gesture Detection
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.task, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		// gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);

		// Context Menu
		registerForContextMenu(getListView());
		
		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, taskNames,
				taskTimes));
		getListView().setOnItemLongClickListener(itemLongClickHandler);
		
		// Sets onClick listener for context menu
		iconContextMenu
				.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {

					public void onClick(int menuId) {
						TaskDetail taskDetail = getList().get(pos);
						String idTask = String.valueOf(taskDetail.getIdTask());
						switch (menuId) {
						case R.id.ctx_menu_delete:
							DeleteTaskTask dst = new DeleteTaskTask(
									activity);
							dst.execute(idTask);
							break;
						}
					}
				});

	}

	public List<TaskDetail> getList() {
		return this.taskList;
	}

	/**
	 * list item long click handler used to show the context menu
	 */
	private OnItemLongClickListener itemLongClickHandler = new OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			pos = position;
			showDialog(CONTEXT_MENU_ID);
			return true;
		}
	};

	/**
	 * create context menu
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == CONTEXT_MENU_ID) {
			return iconContextMenu.createMenu(getResources().getString(
					R.string.Tasks));
		}
		return super.onCreateDialog(id);
	}

	// Options Menu
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

		// Gets the selected task
		TaskDetail selectedTask = this.taskList.get(position);

		ModifyTaskAsk modifyTask = new ModifyTaskAsk(this,selectedTask);
		modifyTask.execute(Integer.toString(selectedTask.getIdTask()));

	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 2.0) {
				if (prediction.name.equalsIgnoreCase("toRight")) {
					onBackPressed();
				}
				if (prediction.name.equalsIgnoreCase("logout")) {
					logOut();
				}
				if (prediction.name.equalsIgnoreCase("add")) {
					add();
				}
			}
		}
	}

	public void logOut() {
		CloseSessionTask closeSessionTask = new CloseSessionTask(activity);
		closeSessionTask.execute();
	}

	public void add() {
		Intent intent = new Intent(this, AddTask.class);
		startActivity(intent);
	}
}
