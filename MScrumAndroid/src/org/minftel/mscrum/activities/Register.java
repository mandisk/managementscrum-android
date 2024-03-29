package org.minftel.mscrum.activities;

import java.util.ArrayList;

import org.minftel.mscrum.tasks.RegisterTask;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnGesturePerformedListener {
	private GestureLibrary gestureLib;

	private static final int CONTACT_PICKER_RESULT = 0;
	private EditText nombre;
	private EditText email;
	private EditText apellido;
	private EditText password;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// No title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		Log.w(ScrumConstants.TAG, "id nombre: "+nombre);
		// Gesture Detection
		GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		View inflate = getLayoutInflater().inflate(R.layout.register, null);
		gestureOverlayView.addView(inflate);
		gestureOverlayView.addOnGesturePerformedListener(this);
		// gestureOverlayView.setGestureColor(Color.TRANSPARENT);
		gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
		gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!gestureLib.load()) {
			Log.w(ScrumConstants.TAG, "Gesture not loaded!");
		}
		setContentView(gestureOverlayView);
		nombre = (EditText) findViewById(R.id.nombreRegister);
		apellido = (EditText) findViewById(R.id.apellidoRegister);
		email = (EditText) findViewById(R.id.emailRegister);
		password = (EditText) findViewById(R.id.passRegister);
		

	}

	public void importDate(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);

		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String correo = null;

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case CONTACT_PICKER_RESULT:

				Uri result = data.getData();
				String id = result.getLastPathSegment();
				Cursor c = managedQuery(result, null, null, null, null);

				// Gets the contact name
				c.moveToFirst();
				String name = c
						.getString(c
								.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

				c = null;

				// Gests the contact email
				c = getContentResolver().query(Email.CONTENT_URI, null,
						Email.CONTACT_ID + "=?", new String[] { id }, null);

				int emaildx = c.getColumnIndex(Email.DATA);

				if (c.moveToFirst()) {
					correo = c.getString(emaildx);
				}
				Log.i(ScrumConstants.TAG, "nombre contacto--> "+name);
			
				nombre.setText(name);
				email.setText(correo);

				if (c != null) {
					c.close();
				}

				break;
			}
		}
	}

	public void registerData(View view) {

		String n = nombre.getText().toString().trim();
		String sn = apellido.getText().toString().trim();
		String correo = email.getText().toString().trim();
		String pass = password.getText().toString().trim();

		if (n == null || n.equals("") || sn == null || sn.equals("")
				|| correo == null || correo.equals("") || pass == null
				|| pass.equals("")) {
			Toast.makeText(this,
					getResources().getString(R.string.login_empty_fields),
					Toast.LENGTH_SHORT).show();
		} else {
			if (!correo.contains("@")) {
				Toast.makeText(this,
						getResources().getString(R.string.EmailError),
						Toast.LENGTH_SHORT).show();
			} else {
				RegisterTask Rtask = new RegisterTask(this);
				Rtask.execute(n, sn, correo, pass);
			}

		}
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 2.0) {
				if (prediction.name.equalsIgnoreCase("toRight")) {
					onBackPressed();
				}
			}
		}
	}
}
