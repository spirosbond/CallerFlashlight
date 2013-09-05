package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by spiros on 9/5/13.
 */
public class AppList extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

	private static final String TAG = AppList.class.getSimpleName();
	protected InteractiveArrayAdapter adapter;
	LinearLayout progBar;
	private PackageManager packageManager;
	private ArrayList<Model> activities;
	private List<String> names;
	private CallerFlashlight callerFlashlight;
	private ListView lv;
	private Button clearAll, selectAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_list);
		callerFlashlight = (CallerFlashlight) getApplication();
		names = new ArrayList<String>();
		activities = new ArrayList<Model>();
		names = new ArrayList<String>();
		clearAll = (Button) findViewById(R.id.clear_button);
		clearAll.setOnClickListener(this);
		selectAll = (Button) findViewById(R.id.selectall_button);
		selectAll.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.appList);
//		this.adapter = new InteractiveArrayAdapter(this, activities);
		lv.setOnItemClickListener(this);
		lv.setAdapter(adapter);
		packageManager = getPackageManager();
		progBar = (LinearLayout) findViewById(R.id.channelsProgress);
		final ArrayList<Model> data = (ArrayList<Model>) getLastNonConfigurationInstance();

		// Fill the list
		if (data == null) { // List not stored
			Log.d(TAG, "null");
			packageManager = getPackageManager();
			this.adapter = new InteractiveArrayAdapter(this, activities);
			lv.setAdapter(adapter);
			UpdateData updateData = new UpdateData();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				updateData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			else updateData.execute((Void[]) null);
		} else { // List stored
			Log.d(TAG, "ok");
			activities = data;
			this.adapter = new InteractiveArrayAdapter(this, activities);
			lv.setAdapter(adapter);
			for (Model mdl : activities) {
				names.add(mdl.getPackageName());
			}
			progBar.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
		}
	}

	public void updateApps() {
		//		Log.d(TAG, "0");
		Intent localIntent = new Intent("android.intent.action.MAIN", null);
		localIntent.addCategory("android.intent.category.LAUNCHER");
		//		Log.d(TAG, "1");
//		this.adapter = new InteractiveArrayAdapter(this, activities, callerFlashlight);
		packageManager = getPackageManager();
		//		Log.d(TAG, "2");
		List<ResolveInfo> rInfo = packageManager.queryIntentActivities(localIntent, 1);
		//		Log.d(TAG, "3");
		List<ApplicationInfo> packages = new ArrayList<ApplicationInfo>();
		//		Log.d(TAG, "4");
		for (ResolveInfo info : rInfo) {
			packages.add(info.activityInfo.applicationInfo);
		}
		Model temp;
		for (ApplicationInfo packageInfo : packages) {
			//			Log.d(TAG, "Installed package :" + packageInfo.packageName);
			if (names.contains(packageInfo.packageName)) {
				continue;
			}
			names.add(packageInfo.packageName);
			temp = new Model((String) packageManager.getApplicationLabel(packageInfo));
			temp.setPackageName(packageInfo.packageName);
			Drawable pic = packageInfo.loadIcon(packageManager);
			temp.setLabel(pic);
			//			Log.d(TAG, "Installed package :" + temp.getName());
			//temp.put(IS_CHECKED, true);
			if (callerFlashlight.loadApp(packageInfo.packageName)) temp.setSelected(true);

			activities.add(temp);
			//			Log.d(TAG, "Launch Activity :" + packageManager.getLaunchIntentForPackage(packageInfo.packageName));
		}
		Collections.sort(activities, new SortByString());
		Collections.sort(activities, new SortByCheck());
		//		Log.d(TAG, "END");
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Log.d(TAG, "onItemClick");
		CheckBox temp = (CheckBox) view.findViewById(R.id.app_list_checkbox);
		if (activities.get(i).isSelected()) {
			temp.setChecked(false);
		} else temp.setChecked(true);
		adapter.notifyDataSetChanged();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, "onRetain");
		final ArrayList<Model> data = activities;
		return data;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.clear_button:
				for (Model mdl : activities) {
					mdl.setSelected(false);
					callerFlashlight.saveApp(mdl.getPackageName(), false);
				}
				adapter.notifyDataSetChanged();
				break;
			case R.id.selectall_button:
				for (Model mdl : activities) {
					mdl.setSelected(true);
					callerFlashlight.saveApp(mdl.getPackageName(), true);
				}
				adapter.notifyDataSetChanged();
				break;
		}

	}

	public class UpdateData extends AsyncTask<Void, Void, Void> {


		@Override
		protected Void doInBackground(Void... voids) {
			//            Log.d(TAG, "doInBackground");
			updateApps();
			return null;
		}

		@Override
		protected void onPreExecute() {
			//            Log.d(TAG, "onPreExecute1");
			super.onPreExecute();
			//            Log.d(TAG, "onPreExecute2");
//			buttonsLayout = (LinearLayout) findViewById(R.id.twoButtons);

			//            Log.d(TAG, "onPreExecute3");
			lv.setVisibility(View.GONE);

			progBar.setVisibility(View.VISIBLE);
			//            Log.d(TAG, "onPreExecute3");
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progBar.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);

			adapter.notifyDataSetChanged();
		}


	}

}
