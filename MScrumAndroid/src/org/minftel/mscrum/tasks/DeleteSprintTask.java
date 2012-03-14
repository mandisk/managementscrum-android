package org.minftel.mscrum.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.minftel.mscrum.activities.R;
import org.minftel.mscrum.activities.SprintsActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
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
					activity.MODE_PRIVATE).getString(ScrumConstants.SESSION_ID, "");
			String url = ScrumConstants.BASE_URL + ScrumConstants.SESSION_URL + sessionId;			
			
			URL urlDispatcher = new URL(url);
			URLConnection connection = urlDispatcher.openConnection();
			
			// To send to the server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			
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
		
		return null;
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage(activity.getResources().getString(R.string.dialog_loading));
		progressDialog.show();
	}

}
