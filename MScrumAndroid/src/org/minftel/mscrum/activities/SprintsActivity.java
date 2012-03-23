package org.minftel.mscrum.activities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.minftel.mscrum.model.SprintDetail;
import org.minftel.mscrum.tasks.ChartsTask;
import org.minftel.mscrum.tasks.CloseSessionTask;
import org.minftel.mscrum.tasks.DeleteSprintTask;
import org.minftel.mscrum.tasks.SprintsTask;
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
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class SprintsActivity extends ListActivity implements
		OnGesturePerformedListener {

	private List<SprintDetail> sprintList = null;
	private GestureLibrary gestureLib;

	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	private int pos;
	private ListActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sprints);
		activity = this;
		Resources res = getResources();

		// Initializes the menu
		iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
		iconContextMenu.addItem(res, R.string.menu_delete_sprints,
				R.drawable.discard, R.id.ctx_menu_delete);
		iconContextMenu.addItem(res, R.string.menu_view_charts,
				R.drawable.stadistic, R.id.ctx_menu_view_charts);

		String[] sprintNumbers = null;
		String[] dates = null;

		// Gets the Sprints List
		String json = getIntent().getExtras().getString("sprints");

		try {
			sprintList = JSONConverter.fromJSONtoSprintList(json);
			dates = new String[sprintList.size()];
			sprintNumbers = new String[sprintList.size()];
		} catch (Exception e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		for (int i = 0; i < sprintList.size(); i++) {
			SprintDetail sprint = sprintList.get(i);
			sprintNumbers[i] = getString(R.string.sprint)
					+ sprint.getSprintNumber();
			dates[i] = getString(R.string.from)
					+ df.format(sprint.getInitialDate())
					+ getString(R.string.to) + df.format(sprint.getEndDate());
		}

		// Gesture detection
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.sprints, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		// gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, sprintNumbers,
				dates));
		getListView().setOnItemLongClickListener(itemLongClickHandler);

		iconContextMenu
				.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {

					public void onClick(int menuId) {

						SprintDetail sprintDetail = getList().get(pos);
						String idSprint = String.valueOf(sprintDetail
								.getIdSprint());

						switch (menuId) {

						case R.id.ctx_menu_delete:
							DeleteSprintTask dst = new DeleteSprintTask(
									activity);
							dst.execute(idSprint);
							break;

						case R.id.ctx_menu_view_charts:
							ChartsTask ct = new ChartsTask(activity);
							ct.execute(idSprint);
							break;
						}
					}
				});
	}

	public List<SprintDetail> getList() {
		return this.sprintList;
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
					R.string.Sprints));
		}
		return super.onCreateDialog(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_sprint, menu);
		return true;
	}

	/** Called when an item is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.LogOut:
			logOut();
			break;
		case R.id.addsprint:
			add();
		default:
			break;
		}
		return true;
	}

	/** Called when an item of the list is selected. */
	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Gets selected sprint
		SprintDetail selectedSprint = this.sprintList.get(position);

		// Converted to string to send
		String idSprint = String.valueOf(selectedSprint.getIdSprint());

		SprintsTask sprintTask = new SprintsTask(this);
		sprintTask.execute(idSprint);

	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 2.0) {
				if (prediction.name.equalsIgnoreCase("toleft")) {
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
		Intent intent = new Intent(this, AddSprintActivity.class);
		startActivity(intent);
	}
}