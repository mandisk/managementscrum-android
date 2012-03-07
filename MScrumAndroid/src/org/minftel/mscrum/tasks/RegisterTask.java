package org.minftel.mscrum.tasks;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class RegisterTask extends AsyncTask<String, Integer, String> {
	
	private LoginActivity activity;
	private ProgressDialog progressDialog;
	
	public RegisterTask(Activity activity) {
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
			
			
			dos.writeInt(ScrumConstants.ACTION_REGISTER);
			dos.writeUTF(params[0]);	// Nombre
			dos.writeUTF(params[1]);	// Apellido
			dos.writeUTF(params[2]);	// Email
			dos.writeUTF(params[3]);	// Password

			// Receive from server
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
			result = dis.readUTF();
			
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
			
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}

}
