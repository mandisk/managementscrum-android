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
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class AddProjectTask extends AsyncTask<String, Integer, String>{

	private Activity activity;
	private ProgressDialog progressDialog;
	
	public AddProjectTask(Activity activity) {
		this.activity = activity;
		this.progressDialog = new ProgressDialog(activity);
	}
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		
		try {
			
			String sessionId = activity.getSharedPreferences(
					ScrumConstants.SHARED_PREFERENCES_FILE, 
					Activity.MODE_PRIVATE).getString(ScrumConstants.SESSION_ID, "");
			String url = ScrumConstants.BASE_URL + ScrumConstants.SESSION_URL + sessionId;
			
			URL urlDispatcher = new URL(url);
			URLConnection connection = urlDispatcher.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send to server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			
			int action = params[0].equals("0") ? ScrumConstants.ACTION_ADD_PROJECT : ScrumConstants.ACTION_EDIT_PROJECT;
			
			dos.writeInt(action);
			dos.writeUTF(params[1]);	// Name
			dos.writeUTF(params[2]);	// Description
			dos.writeUTF(params[3]);	// Initial Day
			dos.writeUTF(params[4]);	// Initial Month
			dos.writeUTF(params[5]);	// Initial Year
			dos.writeUTF(params[6]);	// End Day
			dos.writeUTF(params[7]);	// End Month
			dos.writeUTF(params[8]);	// End Year
			if (action == ScrumConstants.ACTION_EDIT_PROJECT) {
				dos.writeUTF(params[10]);// Project ID 
			}
			

			// Receive from server
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
			result = dis.readUTF();
			
			dis.close();
			dos.close();
			connection = null;
			
		}catch (MalformedURLException e) {
			Log.e(ScrumConstants.TAG, "MalformedURLException: " + e.getMessage());
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
			
			if (result.equals(ScrumConstants.SESSION_EXPIRED)) {
				Log.w(ScrumConstants.TAG, "Session expired");
				SharedPreferences prefs = activity.getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, Activity.MODE_PRIVATE);
				prefs.edit().clear().commit();
				Intent intent = new Intent(this.activity, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        activity.startActivity(intent);
				return;
			}
			
			if (result.equals(ScrumConstants.ERROR_ADD_PROJECT)) {
				Log.w(ScrumConstants.TAG, "Add project error");
				return;
			}
			
			if (result.equals(ScrumConstants.ERROR_EDIT_PROJECT)) {
				Log.w(ScrumConstants.TAG, "Edit project error");
				return;
			}
			
			try {
				JSONObject json = new JSONObject(result);
				JSONArray jsonProjects = json.getJSONArray("projects");
				
				// Send broadcast to open ProjectActivity
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_PROJECTS);
				broadCastIntent.putExtra("projects", jsonProjects.toString());
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
