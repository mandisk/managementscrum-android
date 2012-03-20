package org.minftel.mscrum.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.UserDetail;
import org.minftel.mscrum.tasks.EditUserProjectSendTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.MyPerformanceArrayAdapter;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

public class EditUserProjectActivity extends ListActivity {

	private List<UserDetail> userListNotinProject;
	private List<UserDetail> userList;
	private List<UserDetail> userListAll;
	private List<Integer> chekboxEnabled;

	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.edituserproject);
			chekboxEnabled = new ArrayList<Integer>();

			// Lista de usuarios
			String json2 = getIntent().getExtras().getString(
					"usersnotinproject");
			String json = getIntent().getExtras().getString("users");
			Log.e(ScrumConstants.TAG, "EDIT USER: " + json);
			Log.e(ScrumConstants.TAG, "EDIT USER2: " + json2);

			userList = JSONConverter.fromJSONtoUserList(json);
			userListNotinProject = JSONConverter.fromJSONtoUserList(json2);
			/*
			 * Lo hago asi porque si esta lista solo contiene un usuario te
			 * devuelve el numero de campos de la lista, y eso no estaria bien
			 */

			int tamUserList = 0;

			for (UserDetail user : userList) {
				chekboxEnabled.add(tamUserList);
				tamUserList++;
				// Estas posiciones estaran habilitadas
			}

			userListAll = userList;
			userListAll.addAll(userListNotinProject);

			Log.e(ScrumConstants.TAG, "tam: " + userList.size());
			Log.e(ScrumConstants.TAG, "tam: " + tamUserList);
			Log.e(ScrumConstants.TAG, "tam: " + userListNotinProject.size());
			Log.e(ScrumConstants.TAG, "tam: " + userListAll.size());

			String[] names = new String[userListAll.size()];
			int i = 0;
			for (UserDetail user : userListAll) {
				names[i] = user.getName();
				i++;
			}

			setListAdapter(new MyPerformanceArrayAdapter(this, names,
					tamUserList));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		ListView vista = (ListView) findViewById(android.R.id.list);

		CheckBox check = (CheckBox) ((View) vista.getChildAt(position))
				.findViewById(R.id.check1);
		check.setChecked(!check.isChecked());

		if (check.isChecked()) {
			chekboxEnabled.add(position);
		} else {
			chekboxEnabled.remove(position);
		}
	}

	public void save(View view) {
		String[] idUsers = new String[chekboxEnabled.size()];

		int i = 0;
		for (Integer position : chekboxEnabled) {
			idUsers[i] = String.valueOf(userListAll.get(position).getId());
			Log.e(ScrumConstants.TAG, "Usuario:  " + idUsers[i] + "Position: "+position);

			i++;
		}

		EditUserProjectSendTask editprojectTask = new EditUserProjectSendTask(
				this);
		editprojectTask.execute(idUsers);

	}

}