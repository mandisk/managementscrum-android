package org.minftel.mscrum.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.minftel.mscrum.activities.AddProjectActivity;
import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class AddProjectTask extends AsyncTask<String, Integer, String>{

	private AddProjectActivity activity;
	private ProgressDialog progressDialog;
	
	public AddProjectTask(Activity activity) {
		this.activity = (AddProjectActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		
		try {
			Log.i(ScrumConstants.TAG, "Adding project");

			URL urlDispatcher = new URL(ScrumConstants.BASE_URL);
			URLConnection connection = urlDispatcher.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send to server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			
			dos.writeInt(ScrumConstants.ACTION_ADD_PROJECT);
			dos.writeUTF(params[0]);	// Name
			dos.writeUTF(params[1]);	// Description
			dos.writeUTF(params[2]);	// Initial Day
			dos.writeUTF(params[3]);	// Initial Month
			dos.writeUTF(params[4]);	// Initial Year
			dos.writeUTF(params[5]);	// End Day
			dos.writeUTF(params[6]);	// End Month
			dos.writeUTF(params[7]);	// End Year
			

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
}
