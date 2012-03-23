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
import org.minftel.mscrum.activities.ProjectActivity;
import org.minftel.mscrum.activities.R;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class CloseSessionTask extends AsyncTask<String, Integer, String> {
	private ProjectActivity activity;
	private ProgressDialog progressDialog;

	public CloseSessionTask(Activity activity) {
		this.activity = (ProjectActivity) activity;
		this.progressDialog = new ProgressDialog(activity);
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;

		try {
			String sessionId = activity.getSharedPreferences(
					ScrumConstants.SHARED_PREFERENCES_FILE,
					Activity.MODE_PRIVATE).getString(ScrumConstants.SESSION_ID,
					"");
			String url = ScrumConstants.BASE_URL + ScrumConstants.SESSION_URL
					+ sessionId;

			URL urlDispatcher = new URL(url);
			URLConnection connection = urlDispatcher.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);

			// To send to the server
			OutputStream out = connection.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);

			dos.writeInt(ScrumConstants.ACTION_CLOSE_SESSION);
			dos.close();

			// Receive from server
			InputStream in = connection.getInputStream();
			DataInputStream dis = new DataInputStream(in);

			result = dis.readUTF();

			dis.close();
			dos.close();
			connection = null;

		} catch (MalformedURLException e) {
			Log.i(ScrumConstants.TAG,
					"MalformedURLException: " + e.getMessage());
		} catch (IOException e) {
			Log.i(ScrumConstants.TAG, "IOException: " + e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		SharedPreferences prefs = activity.getSharedPreferences(
				ScrumConstants.SHARED_PREFERENCES_FILE, Activity.MODE_PRIVATE);
		prefs.edit().clear().commit();
		Intent intent = new Intent(activity, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage(activity.getResources().getString(
				R.string.dialog_loading));
		progressDialog.show();
	}

}
