package org.minftel.mscrum.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SprintsActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] countries = getResources().getStringArray(
				R.array.sprintsList);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				countries));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						"Has selected " + ((TextView) view).getText(),
						Toast.LENGTH_SHORT).show();

//				Intent i = new Intent(TasksActivity.this, ShowTasksActivity.class);
//				i.putExtra("Value1", "This value one for ActivityTwo ");
//				i.putExtra("Value2", "This value two ActivityTwo");
//				startActivity(i);
			}
		});
	}

}