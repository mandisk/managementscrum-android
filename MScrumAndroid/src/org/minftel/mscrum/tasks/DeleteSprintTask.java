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
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class DeleteSprintTask extends AsyncTask<String, Integer, String>{

	private SprintsActivity activity;
	private ProgressDialog progressDialog;
	
	public DeleteSprintTask(Activity activity) {
		this.activity =  (SprintsActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		String result = null;
		
		try {
			Log.i(ScrumConstants.TAG, "Deleting a sprint");
			
			String sessionId = activity.getSharedPreferences(
					ScrumConstants.SHARED_PREFERENCES_FILE, 
					Activity.MODE_PRIVATE).getString(ScrumConstants.SESSION_ID, "");
			String url = ScrumConstants.BASE_URL + ScrumConstants.SESSION_URL + sessionId;			
			
			URL urlDispatcher = new URL(url);
			URLConnection connection = urlDispatcher.openConnection();
			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			// To send to the server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			
			dos.writeInt(ScrumConstants.ACTION_DELETE_SPRINT);
			dos.writeUTF(params[0]); // sprint ID
			
			// To receive from the server
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
			result = dis.readUTF();
			
			dos.close();
			dis.close();
			
			connection = null;
			
		} catch(MalformedURLException e) {
			Log.i(ScrumConstants.TAG, "MalformedURLException");
		} catch (IOException e) {
			Log.i(ScrumConstants.TAG, "IOException");
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
				SharedPreferences prefs = activity.getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, Activity.MODE_PRIVATE);
				prefs.edit().clear().commit();
				Intent intent = new Intent(this.activity, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        activity.startActivity(intent);
				return;
			}
			
			if (result.equals(ScrumConstants.ERROR_DELETE_SPRINT)) {
				Log.w(ScrumConstants.TAG, "Error deleting the sprint");
				return;
			}
			
			try {
				JSONObject json = new JSONObject(result);
				JSONArray jsonSprints = json.getJSONArray("sprints");
				
				// Send broadcast to open ProjectActivity
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_SPRINTS);
				broadCastIntent.putExtra("sprints", jsonSprints.toString());
				broadCastIntent.putExtra("selectedProject", activity.getProjectDetail());
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
