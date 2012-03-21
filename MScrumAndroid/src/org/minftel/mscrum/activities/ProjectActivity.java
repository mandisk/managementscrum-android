package org.minftel.mscrum.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.tasks.ChartsTask;
import org.minftel.mscrum.tasks.DeleteProjectTask;
import org.minftel.mscrum.tasks.EditUserProjectAskTask;
import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.tasks.UserTask;
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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class ProjectActivity extends ListActivity implements OnGesturePerformedListener{

	private List<ProjectDetail> projectList;
	private GestureLibrary gestureLib;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);

		// Context menu
		registerForContextMenu(getListView());
			
		// Get the project List
		String json = getIntent().getExtras().getString("projects");
		Log.i(ScrumConstants.TAG,json);

		try {
			projectList = JSONConverter.fromJSONtoProjecList(json);
		} catch (JSONException e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}

		int size = projectList.size();

		String[] projectNames = new String[size];
		String[] scrumMasters = new String[size];

		for (int i = 0; i < size; i++) {
			ProjectDetail project = projectList.get(i);
			projectNames[i] = project.getName();
			scrumMasters[i] = "Scrum Master: "+project.getScrumMaster().getName();
			scrumMasters[i] = getString(R.string.scrum_master)
					+ project.getScrumMaster().getName();
		}

		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, projectNames,
				scrumMasters));
		
		//Detección de gesto
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
//		gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		View inflate = getLayoutInflater().inflate(R.layout.project, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
//			finish();
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);
		
		// Context menu
		registerForContextMenu(getListView());

	}
	
    public void estadistica(View v){
    	Intent intent = new Intent(ProjectActivity.this,ChartActivity.class);
    	startActivity(intent);
    }
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ctx_projects, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		
		ProjectDetail projectDetail = this.projectList.get(info.position);
		String idproject = String.valueOf(projectDetail.getIdProject());

		switch (item.getItemId()) {
		case R.id.ctx_menu_view_users:			
			// View Users
			UserTask ut = new UserTask(this);
			ut.execute(idproject);			
			return true;
			
		case R.id.ctx_menu_delete:
			// Delete project
			DeleteProjectTask dpt = new DeleteProjectTask(this);
			dpt.execute(idproject);
			return true;	
			
		case R.id.ctx_menu_view_charts:
			// View charts
			ChartsTask ct = new ChartsTask(this);
			ct.execute(idproject);			
			return true;
			
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Get selected project
		ProjectDetail selectedProject = this.projectList.get(position);

//		Toast.makeText(
//				this,
//				"Project selected: " + selectedProject.getName() + " [ ID: "
//						+ selectedProject.getIdProject() + " ]",
//				Toast.LENGTH_SHORT).show();

		// Converted to string to send
		String idProject = String.valueOf(selectedProject.getIdProject());
		
		// Save in preferences to use with sprints
		getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit().putString("idproject", idProject).commit();
		
		String idproject = String.valueOf(selectedProject.getIdProject());
		          			
        ProjectsTask projectTask = new ProjectsTask(this);
        projectTask.execute(idproject);       

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_project, menu);
		return true;
	}

	/** Called when an item is selected. */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.LogOut:
			logOut();
			break;
		case R.id.AddProject:
			add();
		default:
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
	}
	
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			Log.i(ScrumConstants.TAG, String.valueOf(prediction.score));
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
		Intent intent = new Intent(this, AddProjectActivity.class);
		startActivity(intent);
	}
}