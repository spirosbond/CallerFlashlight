package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by spiros on 9/5/13.
 */
public class InteractiveArrayAdapter extends ArrayAdapter<Model> {

	private final List<Model> list;
	private final Activity context;
	CallerFlashlight myapp;

	public InteractiveArrayAdapter(Activity context, List<Model> list) {
		super(context, R.layout.app_row, list);
		this.context = context;
		this.list = list;
		myapp = (CallerFlashlight) context.getApplication();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.app_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.list_item_view);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.app_list_checkbox);
			viewHolder.label = (ImageView) view.findViewById(R.id.app_img);
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Model element = (Model) viewHolder.checkbox.getTag();
					if (CallerFlashlight.LOG) Log.d("Adapter", "enter");
					if (CallerFlashlight.LOG) Log.d("Adapter", element.getName() + " " + element.isSelected());
					element.setSelected(buttonView.isChecked());
					myapp.saveApp(element.getPackageName(), element.isSelected());

				}
			});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getName());
		holder.checkbox.setChecked(list.get(position).isSelected());
		holder.label.setImageDrawable(list.get(position).getLabel());
		return view;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
		protected ImageView label;
	}

}
