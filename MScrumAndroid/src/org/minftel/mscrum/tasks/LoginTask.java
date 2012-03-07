package org.minftel.mscrum.tasks;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.minftel.mscrum.activities.LoginActivity;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.UserDetail;
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
			
			if (result.equals(ScrumConstants.ERROR_LOGIN)) {
				Toast.makeText(this.activity, "Wrong user or password", Toast.LENGTH_SHORT).show();
				return;
			}
			
			try {
				JSONObject json = new JSONObject(result);
				JSONArray jsonProjects = json.getJSONArray("projects");
				
				List<ProjectDetail> projects = new ArrayList<ProjectDetail>();
				
				for (int i = 0; i < jsonProjects.length(); i++) {
					JSONObject project = jsonProjects.getJSONObject(i);
					
					ProjectDetail projectDetail = new ProjectDetail();
					projectDetail.setIdProject(project.getInt("id"));
					projectDetail.setName(project.getString("name"));
					projectDetail.setDescription(project.getString("description"));
					projectDetail.setInitialDate(new Date(project.getLong("initialdate")));
					projectDetail.setEndDate(new Date(project.getLong("enddate")));
					
					JSONObject scrumMaster = project.getJSONObject("scrummaster");
					
					UserDetail userDetail = new UserDetail();
					userDetail.setName(scrumMaster.getString("name"));
					userDetail.setSurname(scrumMaster.getString("surname"));
					userDetail.setEmail(scrumMaster.getString("email"));
					userDetail.setPhone(scrumMaster.getString("phone"));
					
					projectDetail.setScrumMaster(userDetail);
					
					projects.add(projectDetail);
				}
				
				Log.i(ScrumConstants.TAG, "Session id: " + json.getString("session"));
				Log.i(ScrumConstants.TAG, "Projects: " + projects.toString());
				
				this.activity.getEditor().putString(ScrumConstants.SESSION_ID, json.getString("session"));
				this.activity.getEditor().commit();
				
				Toast.makeText(this.activity, "User logged", Toast.LENGTH_SHORT).show();
				
			} catch (JSONException e) {
				Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
			}
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}

}
