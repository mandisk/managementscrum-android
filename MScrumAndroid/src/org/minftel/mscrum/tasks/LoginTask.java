package org.minftel.mscrum.tasks;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Integer, String> {
	
	private LoginActivity activity;
	private ProgressDialog progressDialog;
	
	public LoginTask(Activity activity) {
		this.activity = (LoginActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}

	@Override
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
			
			
			dos.writeInt(ScrumConstants.ACTION_LOGIN);
			dos.writeUTF(params[0]);	// Email
			dos.writeUTF(params[1]);	// Password

			// Receive from server
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
			result = dis.readUTF();
			Log.i(ScrumConstants.TAG, "Result: " + result);
			
			dis.close();
			dos.close();
			connection = null;
			
		} catch (MalformedURLException e) {
			Log.e(ScrumConstants.TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(ScrumConstants.TAG, e.getMessage());
		}
		
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		if (result != null) {
			
			this.activity.getEditor().putString(ScrumConstants.SESSION_ID, result);
			this.activity.getEditor().commit();
			
			Log.i(ScrumConstants.TAG, "SESSION ID saved. Value: " + result);
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}

}
