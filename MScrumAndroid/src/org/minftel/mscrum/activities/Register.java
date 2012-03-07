package org.minftel.mscrum.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.EditText;

public class Register extends Activity {

	private static final int CONTACT_PICKER_RESULT = 0;
	private EditText nombre;
	private EditText email;
	//private EditText apellido;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		nombre = (EditText) findViewById(R.id.nombre);
		//apellido = (EditText) findViewById(R.id.apellido);
		email = (EditText) findViewById(R.id.email);

	}

	public void importDate(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);

		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				
				Uri result = data.getData();  
				 String id = result.getLastPathSegment();  
				 Cursor c = managedQuery(result, null, null, null, null);
				 
				//Recupera el nombre del contacto
                 c.moveToFirst();
                 String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
               
               
                 //Recupera email de contacto
                 c = getContentResolver().query(Email.CONTENT_URI,  
                         null, Email.CONTACT_ID + "=?", new String[] { id },  
                         null);  
   
                 int emaildx = c.getColumnIndex(Email.DATA);  
                 String correo = c.getString(emaildx);  
               
                 //String lastName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));

                 nombre.setText(name); 
                 email.setText(correo);
                // apellido.setText(lastName);
                 
			break;
			}
		}
	}
}