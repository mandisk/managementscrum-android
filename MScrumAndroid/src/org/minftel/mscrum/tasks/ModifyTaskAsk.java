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
import org.minftel.mscrum.activities.TaskActivity;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class ModifyTaskAsk extends AsyncTask<String, Integer, String>{

	private	TaskActivity activity;
	private ProgressDialog progressDialog;
	private TaskDetail task;
	
	public ModifyTaskAsk(Activity activity, TaskDetail task) {
		this.activity = (TaskActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
		this.task = task;
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
			
			dos.writeInt(ScrumConstants.ACTION_MODIFY_TASK_ASK);
			dos.writeUTF(params[0]);	// id task
			
			

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
			
			if (result.equals(ScrumConstants.ERROR_EDITING_TASK_ASK)) {
				Log.w(ScrumConstants.TAG, "Error editing the task ");
				return;
			}
			
			try {
				JSONObject json = new JSONObject(result);
				JSONArray jsonUserInProject = json.getJSONArray("users");
				
				
				// Send broadcast to open EditProjectActivity
				Intent broadCastIntent = new Intent();
				broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_EDIT_TASK);
				broadCastIntent.putExtra("users", jsonUserInProject.toString());
				broadCastIntent.putExtra("task", task);
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