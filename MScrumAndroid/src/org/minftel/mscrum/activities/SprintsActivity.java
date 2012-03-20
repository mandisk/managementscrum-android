package org.minftel.mscrum.activities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import org.minftel.mscrum.model.SprintDetail;
import org.minftel.mscrum.tasks.DeleteSprintTask;
import org.minftel.mscrum.tasks.SprintsTask;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class SprintsActivity extends ListActivity implements OnGesturePerformedListener{

	private List<SprintDetail> sprintList = null;
	private GestureLibrary gestureLib;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sprints);

		String[] sprintNumbers = null;
		String[] dates = null;

		// Get theSprints List
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

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, sprintNumbers,
				dates));

		// Detección de gesto
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.sprints, null);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ctx_sprints, menu);
	}

	// Called when an item is selected from the context menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		SprintDetail sprintDetail = this.sprintList.get(info.position);

		switch (item.getItemId()) {
		case R.id.ctx_menu_sprints:

			// Convert to string sprint ID
			String idSprint = String.valueOf(sprintDetail.getIdSprint());
 
			DeleteSprintTask dst = new DeleteSprintTask(this);
			dst.execute(idSprint);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	/** Called when an item of the list is selected. */
	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected sprint
		SprintDetail selectedSprint = this.sprintList.get(position);

		// Toast.makeText(
		// this,
		// "Number of selected sprint : "
		// + selectedSprint.getSprintNumber() + " [ ID: "
		// + selectedSprint.getIdSprint() + " ]",
		// Toast.LENGTH_SHORT).show();

		// Converted to string to send
		String idSprint = String.valueOf(selectedSprint.getIdSprint());

		SprintsTask sprintTask = new SprintsTask(this);
		sprintTask.execute(idSprint);

	}
	
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 2.0) {
				if(prediction.name.equalsIgnoreCase("toleft")){
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
		Intent intent = new Intent(this, AddSprintActivity.class);
		startActivity(intent);
	}
}