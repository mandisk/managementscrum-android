package org.minftel.mscrum.receiver;

import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.activities.ProjectActivity;
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
		}
	}

}
