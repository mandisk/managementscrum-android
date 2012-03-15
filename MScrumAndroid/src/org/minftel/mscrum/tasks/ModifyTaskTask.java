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
import org.minftel.mscrum.activities.AddTask;
import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class ModifyTaskTask extends AsyncTask<String, Integer, String>{

	private AddTask activity;
	private ProgressDialog progressDialog;
	
	public ModifyTaskTask(Activity activity) {
		this.activity = (AddTask) activity;
		this.progressDialog = new ProgressDialog(activity);
	}
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		
		try {
			Log.i(ScrumConstants.TAG, "Modifing task");

			URL urlDispatcher = new URL(ScrumConstants.BASE_URL);
			URLConnection connection = urlDispatcher.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send to server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			
			dos.writeInt(ScrumConstants.ACTION_MODIFY_TASK);
			dos.writeUTF(params[0]);	// state Task
			dos.writeUTF(params[1]);	// user task
			dos.writeUTF(params[2]); 	//project task
			dos.writeUTF(params[3]);	//sprint task
			dos.writeUTF(params[4]);	//time task
			dos.writeUTF(params[5]);	//description task
			
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
			try {
				
				JSONObject json = new JSONObject(result);
				JSONArray jsonTasks = json.getJSONArray("tasks");
								
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_TASKS);
				broadCastIntent.putExtra("tasks", jsonTasks.toString());
				this.activity.sendBroadcast(broadCastIntent);
				
				Log.i(ScrumConstants.TAG, "TASK selected");
				
			} catch (JSONException e) {
				Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
			}
			
		}
	}

}
