package org.minftel.mscrum.receiver;

import org.minftel.mscrum.activities.ChartsActivity;
import org.minftel.mscrum.activities.EditUserProjectActivity;
import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.activities.ProjectActivity;
import org.minftel.mscrum.activities.SprintsActivity;
import org.minftel.mscrum.activities.TaskActivity;
import org.minftel.mscrum.activities.UserActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScrumReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Go to ProjectActivity
		if (intent.getAction().equals(ScrumConstants.BROADCAST_GO_PROJECTS)) {
			// Create Intent to start activity
			Intent projectIntent = new Intent(context, ProjectActivity.class);
			projectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			String json = intent.getStringExtra("projects");
			projectIntent.putExtra("projects", json);
			
			// ProjectActivity starts
			context.startActivity(projectIntent);
			
		}else if(intent.getAction().equals(ScrumConstants.BROADCAST_GO_LOGIN)){
			// Create Intent to start activity
			Intent projectIntent = new Intent(context, LoginActivity.class);
			projectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(projectIntent);
		
		}else if(intent.getAction().equals(ScrumConstants.BROADCAST_GO_SPRINTS)){
			//Create Intent to start activity SprintsActivity
			Intent sprintIntent = new Intent(context, SprintsActivity.class);
			sprintIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			String json = intent.getStringExtra("sprints");
			sprintIntent.putExtra("sprints", json);
			
			context.startActivity(sprintIntent);
		
		}else if(intent.getAction().equals(ScrumConstants.BROADCAST_GO_TASKS)){
			
			//Create Intent to start activity TasksActivity
			Intent taskIntent = new Intent(context, TaskActivity.class);
			taskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			String json = intent.getStringExtra("tasks");
			taskIntent.putExtra("tasks", json);
			
			context.startActivity(taskIntent);
		
		}else if(intent.getAction().equals(ScrumConstants.BROADCAST_GO_USERS)){
			
			//Create Intent to start activity UserActivity
			Log.i(ScrumConstants.TAG, "Launch users activity...");
			
			Intent userIntent = new Intent(context, UserActivity.class);
			userIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			String json = intent.getStringExtra("users");
			userIntent.putExtra("users", json);
			
			context.startActivity(userIntent);
		
		} else if (intent.getAction().equals(ScrumConstants.BROADCAST_GO_CHARTS)){
			
			//Create Intent to start the activity ChartsActivity
			Log.i(ScrumConstants.TAG, "Launch Charts Activity");
			
			Intent chartsIntent = new Intent(context, ChartsActivity.class);
			chartsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
		} else if (intent.getAction().equals(ScrumConstants.BROADCAST_GO_EDIT_USER_PROJECT)) {
			
			//Create Intent to start the activity EditProjectActivity
			Log.i(ScrumConstants.TAG, "Launch Charts Activity");
			
			Intent editProjectIntent = new Intent(context, EditUserProjectActivity.class);
			editProjectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			String jsonUsers = intent.getStringExtra("users");
			String jsonUsersNotInProject = intent.getStringExtra("usersnotinproject");
			
			editProjectIntent.putExtra("users", jsonUsers);
			editProjectIntent.putExtra("usersnotinproject", jsonUsersNotInProject);
			
			context.startActivity(editProjectIntent);
		} 
			
		
	}

}