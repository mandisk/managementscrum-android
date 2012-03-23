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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ChartsTask extends AsyncTask<String, Integer, String> {

	private SprintsActivity activity;
	private ProgressDialog progressDialog;
	
	public ChartsTask(Activity activity) {
		this.activity = (SprintsActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		String result = null;
				
		try {
			
			Log.i(ScrumConstants.TAG, "Showing charts");
			
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
			
			dos.writeInt(ScrumConstants.ACTION_REQUEST_CHART);
			dos.writeUTF(params[0]); // ID sprint
			
			// To receive to the server			
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
			result = dis.readUTF(); 
		
			dos.close();
			dis.close();
			
			connection = null;
			
		} catch (MalformedURLException e) {
			Log.i(ScrumConstants.TAG, "MalformedURLException");
		} catch (IOException e) {
			Log.i(ScrumConstants.TAG, "IOException");
		}
		
		return result;
	}
	
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
				
				Log.i(ScrumConstants.TAG, result);
				JSONObject json = new JSONObject(result);
				JSONArray jsonTasks = json.getJSONArray("hours");
				
				Log.i(ScrumConstants.TAG, jsonTasks.toString());
				
				// Send broadcast to open ChartActivity
				if (jsonTasks.length() > 0) {
					Intent broadCastIntent = new Intent();
					broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_CHARTS);
				    broadCastIntent.putExtra("hours", jsonTasks.toString());
					this.activity.sendBroadcast(broadCastIntent);
				} else {
					Toast.makeText(activity,activity.getResources().getString(R.string.info_no_tasks),Toast.LENGTH_SHORT).show();
				}
				
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
