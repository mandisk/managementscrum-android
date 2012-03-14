package org.minftel.mscrum.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.minftel.mscrum.activities.TaskActivity;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class DeleteTaskTask extends AsyncTask<String, Integer, String>{

	private TaskActivity activity;
	private ProgressDialog progressDialog;
	
	public DeleteTaskTask(Activity activity) {
		this.activity = (TaskActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		
		try {
			Log.i(ScrumConstants.TAG, "Deleting project");

			URL urlDispatcher = new URL(ScrumConstants.BASE_URL);
			URLConnection connection = urlDispatcher.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send to server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			
			dos.writeInt(ScrumConstants.ACTION_DELETE_TASK);
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
}
