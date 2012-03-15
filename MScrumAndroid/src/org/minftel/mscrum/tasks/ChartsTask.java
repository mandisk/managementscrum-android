package org.minftel.mscrum.tasks;

import org.minftel.mscrum.activities.ProjectActivity;
import org.minftel.mscrum.activities.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class ChartsTask extends AsyncTask<String, Integer, String> {

	private ProjectActivity activity;
	private ProgressDialog progressDialog;
	
	public ChartsTask(Activity activity) {
		this.activity = (ProjectActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog.setMessage(activity.getResources().getString(R.string.dialog_loading));
		progressDialog.show();
	}
	

}
