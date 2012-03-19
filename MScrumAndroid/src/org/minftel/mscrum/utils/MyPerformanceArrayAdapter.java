package org.minftel.mscrum.utils;


import org.minftel.mscrum.activities.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyPerformanceArrayAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] names;
	private final int tam;

	static class ViewHolder {
		public TextView text;

	}

	public MyPerformanceArrayAdapter(Activity context, String[] names, int tamanioList) {
		super(context, R.layout.row_item, names);
		this.context = context;
		this.names = names;
		this.tam = tamanioList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.row_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.grp_child);
//			viewHolder. =  rowView
//					.findViewById(R.id.check1);
			if(position < 5){
				
			
			CheckBox cb = (CheckBox) rowView.findViewById(R.id.check1);
			cb.setChecked(true);
			}
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		String s = names[position];
		holder.text.setText(s);
		

		return rowView;
	}
}
