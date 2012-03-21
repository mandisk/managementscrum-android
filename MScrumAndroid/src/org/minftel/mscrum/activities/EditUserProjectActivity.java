package org.minftel.mscrum.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.minftel.mscrum.model.UserDetail;
import org.minftel.mscrum.tasks.EditUserProjectSendTask;
import org.minftel.mscrum.utils.JSONConverter;
import org.minftel.mscrum.utils.ScrumConstants;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

public class EditUserProjectActivity extends Activity {

	private CheckListAdapter adapter;
	private ListView list;
	private CheckBox check;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edituserproject);

		list = (ListView) findViewById(R.id.editUserProjectList);
		check = (CheckBox) findViewById(R.id.editUserProjectSelectAll);
		
		loadData();
	}
	
	private void loadData() {
		Bundle extras = getIntent().getExtras();
		String jsonUserNotInProjectList = extras.getString("usersnotinproject"); 
		String jsonUserInProjectList = extras.getString("users");
		
		List<UserDetail> usersInProject = null;
		List<UserDetail> usersNotInProject = null;
		try {
			usersInProject = JSONConverter.fromJSONtoUserList(jsonUserInProjectList);
			usersNotInProject = JSONConverter.fromJSONtoUserList(jsonUserNotInProjectList);
		} catch (JSONException e) {
			Log.e(ScrumConstants.TAG, "JSONException: " + e.getMessage());
		}

		adapter = new CheckListAdapter(usersInProject.size() + usersNotInProject.size());
		int size = usersInProject.size();
		for (int i = 0; i < size; i++) {
			UserDetail user = usersInProject.get(i);
			adapter.addItem(user);
			adapter.set(i, true);
		}
		
		for (UserDetail userDetail: usersNotInProject) {
			adapter.addItem(userDetail);
		}
		
		list.setAdapter(adapter);
	}
	
	public void select(View view) {
		boolean checked = check.isChecked();
		for (int i = 0; i < adapter.getItemsLength(); i++) {
			adapter.set(i, checked);
		}
		
		int pos = list.getFirstVisiblePosition();
		list.setAdapter(adapter);
		list.setSelection(pos);
		
		changeCheckBoxText(checked);
	}
	
	private void changeCheckBoxText(boolean checked) {
		if (checked)
			check.setText(getResources().getString(R.string.editUserProjectUnselectAll));
		else
			check.setText(getResources().getString(R.string.editUserProjectSelectAll));
	}

	public void save(View view) {
		List<UserDetail> userList = adapter.getItemsChecked();
		int size = userList.size();
		
		String[] idUsers = new String[size];
		for(int i = 0; i < size; i++) {
			idUsers[i] = String.valueOf(userList.get(i).getId());
		}
		
		EditUserProjectSendTask editUserProjectSendTask = new EditUserProjectSendTask(this);
		editUserProjectSendTask.execute(idUsers);
	}
	
	// Static classes to chaeckBox list
	private class CheckListAdapter extends BaseAdapter {
		private ArrayList<UserDetail> items = new ArrayList<UserDetail>();
		private LayoutInflater inflater;
		private boolean[] itemSelection;

		public CheckListAdapter(int size) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.itemSelection = new boolean[size];
		}

		public void addItem(final UserDetail userDetail) {
			items.add(userDetail);
			notifyDataSetChanged();
		}

		public int getCount() {
			return items.size();
		}

		public String getItem(int position) {
			return items.get(position).toString();
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = inflater.inflate(R.layout.row_item, null);
			final ViewHolder holder = new ViewHolder();
			holder.chkItem = (CheckBox) convertView
					.findViewById(R.id.checkItem);
			holder.chkItem
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							itemSelection[position] = holder.chkItem
									.isChecked();
							if (check.isChecked() && !isChecked) {
								changeCheckBoxText(isChecked);
								check.setChecked(isChecked);
							}
						}
					});

			holder.chkItem.setChecked(itemSelection[position]);
			convertView.setTag(holder);
			holder.chkItem.setText(getItem(position));
			return convertView;
		}

		public int getItemsLength() {
			if (itemSelection == null)
				return 0;
			return itemSelection.length;
		}

		public void set(int i, boolean b) {
			itemSelection[i] = b;
		}

		public List<UserDetail> getItemsChecked() {
			List<UserDetail> userList = new ArrayList<UserDetail>();
			int size = itemSelection.length;
			for (int i = 0; i < size; i++) {
				if (itemSelection[i]) {
					userList.add(items.get(i));
				}
			}
			
			return userList;
		}
	}

	public static class ViewHolder {
		public CheckBox chkItem;
	}
}