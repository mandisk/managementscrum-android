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
import org.minftel.mscrum.activities.AddSprintActivity;
import org.minftel.mscrum.activities.R;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class AddSprintTask extends AsyncTask<String, Integer, String> {

	private AddSprintActivity activity;
	private ProgressDialog progressDialog;
	
	public AddSprintTask(Activity activity) {
		this.activity = (AddSprintActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}	
	
	@Override
	protected String doInBackground(String... params) {

		String result = null;

		try {

			Log.i(ScrumConstants.TAG, "Adding a new Sprint");

			String sessionId = activity.getSharedPreferences(
					ScrumConstants.SHARED_PREFERENCES_FILE, 
					activity.MODE_PRIVATE).getString(ScrumConstants.SESSION_ID, "");
			String url = ScrumConstants.BASE_URL + ScrumConstants.SESSION_URL + sessionId;
			
			URL urlDispatcer = new URL(url);
			URLConnection connection = urlDispatcer.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);

			// To send to the server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);

			dos.writeInt(ScrumConstants.ACTION_ADD_SPRINT);
			dos.writeUTF(params[0]); // Sprint Number
			dos.writeUTF(params[1]); // Initial Day
			dos.writeUTF(params[2]); // Initial Month
			dos.writeUTF(params[3]); // Initial Year
			dos.writeUTF(params[4]); // End Day
			dos.writeUTF(params[5]); // End Month
			dos.writeUTF(params[6]); // End Year
			dos.writeUTF(params[7]); // associated project ID

			// To receive from the dispatcher
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);

			result = dis.readUTF();

			dis.close();
			dos.close();
			
			connection = null;

		} catch (MalformedURLException e) {
			Log.e(ScrumConstants.TAG, "Malformed URL exception");
		} catch (IOException e) {
			Log.e(ScrumConstants.TAG, "IO exception");
		}

		return result;
	}	
	
	@Override
	protected void onPostExecute(String result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		if (result != null) {
			
			if (result.equals(ScrumConstants.SESSION_EXPIRED)) {
				Log.w(ScrumConstants.TAG, "Session expired");
				return;
			}
			
			if (result.equals(ScrumConstants.ERROR_ADD_PROJECT)) {
				Log.w(ScrumConstants.TAG, "Error adding the sprint");
				return;
			}
			
			try {
				JSONObject json = new JSONObject(result);
				JSONArray jsonProjects = json.getJSONArray("sprints");
				
				// Send broadcast to open ProjectActivity
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_SPRINTS);
				broadCastIntent.putExtra("sprints", jsonProjects.toString());
				this.activity.sendBroadcast(broadCastIntent);
				
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
