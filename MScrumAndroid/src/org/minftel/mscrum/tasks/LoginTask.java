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
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Integer, String> {
	
	private LoginActivity activity;
	private ProgressDialog progressDialog;
	String email = null;
	public LoginTask(Activity activity) {
		this.activity = (LoginActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}

	@Override
	public String doInBackground(String... params) {
		
		String result = null;
		
		try {
			Log.i(ScrumConstants.TAG, "Logging user");

			URL urlDispatcher = new URL(ScrumConstants.BASE_URL);
			URLConnection connection = urlDispatcher.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send to server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			
			
			dos.writeInt(ScrumConstants.ACTION_LOGIN);
			email = params[0];
			dos.writeUTF(params[0]);	// Email
			dos.writeUTF(params[1]);	// Password

			// Receive from server
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
			result = dis.readUTF();
			
			dis.close();
			dos.close();
			connection = null;
			
		} catch (MalformedURLException e) {
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
			
			if (result.equals(ScrumConstants.ERROR_LOGIN)) {
				Toast.makeText(
						activity, 
						activity.getResources().getString(R.string.login_error), 
						Toast.LENGTH_SHORT).show();
				
				Log.i(ScrumConstants.TAG, "Error login");
				
				return;
			}
			
			try {
				JSONObject json = new JSONObject(result);
				JSONArray jsonProjects = json.getJSONArray("projects");
				
				// Save SESSION ID in SharedPreferences
				this.activity.getEditor().putString(ScrumConstants.SESSION_ID, json.getString("session"));
				this.activity.getEditor().commit();
				
//				Toast.makeText(this.activity, "User logged", Toast.LENGTH_SHORT).show();
				
				// Send broadcast to open ProjectActivity
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_PROJECTS);
				broadCastIntent.putExtra("projects", jsonProjects.toString());
				
				this.activity.getSharedPreferences(ScrumConstants.SHARED_PREFERENCES_FILE, Activity.MODE_PRIVATE).edit().putString("email", email);
				this.activity.sendBroadcast(broadCastIntent);
				
				Log.i(ScrumConstants.TAG, "User logged");
				
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
