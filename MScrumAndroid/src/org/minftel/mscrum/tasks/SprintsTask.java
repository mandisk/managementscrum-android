package org.minftel.mscrum.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.activities.R;
import org.minftel.mscrum.activities.SprintsActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SprintsTask extends AsyncTask<String, Integer, String> {

	private SprintsActivity activity;
	private ProgressDialog progressDialog;

	public SprintsTask(Activity activity) {
		this.activity = (SprintsActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}

	public String doInBackground(String... params) {

		String result = null;

		try {

			URL urlDispatcher = new URL(ScrumConstants.BASE_URL);
			URLConnection connection = urlDispatcher.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send to server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);

			dos.writeInt(ScrumConstants.ACTION_REQUEST_LIST_SPRINTS);
			dos.writeUTF(params[0]); // ID Sprint
			

			// Receive from server
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);

			result = dis.readUTF();

			dis.close();
			dos.close();
			connection = null;

		} catch (MalformedURLException e) {
			Log.e(ScrumConstants.TAG,
					"MalformedURLException: " + e.getMessage());
		} catch (IOException e) {
			Log.e(ScrumConstants.TAG, "IOException: " + e.getMessage());
		}

		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		if (result != null) {
			
			if (result.equals(ScrumConstants.ERROR_SPRINTS)) {
				Toast.makeText(
						activity, 
						activity.getResources().getString(R.string.sprints_error), 
						Toast.LENGTH_SHORT).show();
				
				Log.i(ScrumConstants.TAG, "Error loading sprints");
				
				return;
			}
			
			try {
				
				JSONObject json = new JSONObject(result);
				JSONArray jsonProjects = json.getJSONArray("tasks");
								
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_TASKS);
				broadCastIntent.putExtra("tasks", jsonProjects.toString());
				this.activity.sendBroadcast(broadCastIntent);
				
				Log.i(ScrumConstants.TAG, "Sprint selected");
				
			} catch (JSONException e) {
				Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
			}
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage(activity.getResources().getString(R.string.dialog_loading));
		progressDialog.show();
	}

}
