package org.minftel.mscrum.receiver;

import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.activities.ProjectActivity;
import org.minftel.mscrum.activities.SprintsActivity;
import org.minftel.mscrum.activities.TasksActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScrumReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Go to ProjectActivity
		if (intent.getAction().equals(ScrumConstants.BROADCAST_GO_PROJECTS)) {
			// Create Intent to start activity
			Intent projectIntent = new Intent(context, ProjectActivity.class);
			projectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			String json = intent.getStringExtra("projects");
			projectIntent.putExtra("projects", json);
			
			// ProjectActivity starts
			context.startActivity(projectIntent);
			
		}else if(intent.getAction().equals(ScrumConstants.BROADCAST_GO_LOGIN)){
			// Create Intent to start activity
			Intent projectIntent = new Intent(context, LoginActivity.class);
			projectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(projectIntent);
		
		}else if(intent.getAction().equals(ScrumConstants.BROADCAST_GO_SPRINTS)){
			//Create Intent to start activity SprintsActivity
			Intent sprintIntent = new Intent(context, SprintsActivity.class);
			sprintIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(sprintIntent);
		
		}else if(intent.getAction().equals(ScrumConstants.BROADCAST_GO_TASKS)){
			//Create Intent to start activity TasksActivity
			Intent taskIntent = new Intent(context, TasksActivity.class);
			taskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(taskIntent);
		}
		
		
	}

}
