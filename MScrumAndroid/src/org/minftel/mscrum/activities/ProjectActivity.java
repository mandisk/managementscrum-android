package org.minftel.mscrum.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.tasks.CloseSessionTask;
import org.minftel.mscrum.tasks.DeleteProjectTask;
import org.minftel.mscrum.tasks.EditUserProjectAskTask;
import org.minftel.mscrum.tasks.ProjectsTask;
import org.minftel.mscrum.tasks.UserTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;
import org.minftel.mscrum.utils.TextAdapter;

import org.minftel.mscrum.utils.IconContextMenu;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ProjectActivity extends ListActivity implements OnGesturePerformedListener{
	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	private List<ProjectDetail> projectList;
	private GestureLibrary gestureLib;
	private int pos;
	
	private ListActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project);
		activity = this;
		Resources res = getResources();
		
		// Initializae the menu
        iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
        iconContextMenu.addItem(res, R.string.menu_view_users, R.drawable.group, R.id.ctx_menu_view_users);
        iconContextMenu.addItem(res, R.string.menu_delete_project, R.drawable.discard, R.id.ctx_menu_delete);
        iconContextMenu.addItem(res, R.string.menu_edit_project, R.drawable.edit, R.id.ctx_menu_edit_project);
        iconContextMenu.addItem(res, R.string.menu_edit_user, R.drawable.add_person, R.id.ctx_menu_edit_user_in_project);
		
		// Get Project List
		String json = getIntent().getExtras().getString("projects");

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
			scrumMasters[i] = getString(R.string.scrum_master)
					+ project.getScrumMaster().getName();
		}

		// Gesture Detection
				GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
//				gestureOverlayView.setGestureColor(Color.TRANSPARENT);
				gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
				View inflate = getLayoutInflater().inflate(R.layout.project, null);
				gestureOverlayView.addView(inflate);
				gestureOverlayView.addOnGesturePerformedListener(this);
				gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
				if (!gestureLib.load()) {
					Log.w(ScrumConstants.TAG, "Gesture not loaded!");
				}
				setContentView(gestureOverlayView);
				
		// Load data in ListAdapter
		setListAdapter(new TextAdapter(this, R.layout.list_item, projectNames,
				scrumMasters));
		getListView().setOnItemLongClickListener(itemLongClickHandler);
				
		// Sets onClick Listener for context menu
        iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
        	
			public void onClick(int menuId) {
				ProjectDetail projectDetail = getList().get(pos);
	    		String idproject = String.valueOf(projectDetail.getIdProject());
				switch(menuId) {
				case R.id.ctx_menu_view_users:
					UserTask ut = new UserTask(activity);
					ut.execute(idproject);	
					break;
				case R.id.ctx_menu_delete:
					DeleteProjectTask dpt = new DeleteProjectTask(activity);
					dpt.execute(idproject);
					break;
				case R.id.ctx_menu_edit_project:
					String SmEmail = "";
					
					SharedPreferences prefs;
					prefs = getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
					prefs.getString("userEmail", SmEmail);
					
					Intent intent = new Intent(activity, EditProjectActivity.class);
					intent.putExtra("name", projectDetail.getName());
					intent.putExtra("description", projectDetail.getDescription());
					intent.putExtra("initdate", projectDetail.getInitialDate());
					intent.putExtra("enddate", projectDetail.getEndDate());
					intent.putExtra("ScrumMaster", projectDetail.getScrumMaster().getEmail());
					intent.putExtra("idProject", projectDetail.getIdProject());
					startActivity(intent);
						
					break;
				case R.id.ctx_menu_edit_user_in_project:
					EditUserProjectAskTask editprojectTask = new EditUserProjectAskTask(activity);
					editprojectTask.execute(Integer.toString(projectDetail.getIdProject()));
					break;

				}
			}
		});
	}	
		
	public List<ProjectDetail> getList(){
		return this.projectList;
	}
	
	/**
     * list item long click handler
     * used to show the context menu
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
			return iconContextMenu.createMenu(getResources().getString(R.string.Projects));
		}
		return super.onCreateDialog(id);
	}
	
	
    public void estadistica(View v){
    	Intent intent = new Intent(ProjectActivity.this,ChartActivity.class);
    	startActivity(intent);
    }

	public void onListItemClick(ListView parent, View v, int position, long id) {

		// Gets the selected project
		ProjectDetail selectedProject = this.projectList.get(position);
		String idProject = String.valueOf(selectedProject.getIdProject());

		ProjectsTask projectTask = new ProjectsTask(this);
		projectTask.execute(idProject);

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
		CloseSessionTask closeSessionTask = new CloseSessionTask(activity);
		closeSessionTask.execute();
	}
	
	public void add(){
		Intent intent = new Intent(this, AddProjectActivity.class);
		startActivity(intent);
	}
}