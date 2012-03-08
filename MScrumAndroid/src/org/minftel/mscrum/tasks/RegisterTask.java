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
import org.minftel.mscrum.activities.R;
import org.minftel.mscrum.activities.Register;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

public class RegisterTask extends AsyncTask<String, Integer, String> {
	
	private	Register activity;
	private ProgressDialog progressDialog;
	
	public RegisterTask(Activity activity) {
		this.activity = (Register) activity;
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
			
			
//			dos.writeInt(ScrumConstants.ACTION_REGISTER);
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
			// Send broadcast to open ProjectActivity
			//--------
			//Para un segundo sprint podriamos pasar nombre y contraseña registradas.
			//--------
			Intent broadCastIntent = new Intent();
			broadCastIntent.setAction(ScrumConstants.BROADCAST_GO_LOGIN);
			this.activity.sendBroadcast(broadCastIntent);
			
		}else{
			Toast.makeText(this.activity, "User already register", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}


}
