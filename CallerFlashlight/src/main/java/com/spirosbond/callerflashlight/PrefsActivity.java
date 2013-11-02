package com.spirosbond.callerflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by spiros on 8/5/13.
 */
public class PrefsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

	private static final String TAG = PrefsActivity.class.getSimpleName();
	private ListPreference lp;
	private CallerFlashlight callerFlashlight;
	private CheckBoxPreference screenOfPreference;
	private boolean dismissed;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callerFlashlight = (CallerFlashlight) this.getApplication();
		addPreferencesFromResource(R.xml.prefs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		lp = (ListPreference) findPreference("type_list");
		lp.setValue(String.valueOf(callerFlashlight.getType()));
		setTypeSum(callerFlashlight.getType());

		screenOfPreference = (CheckBoxPreference) findPreference("screen_off");
		screenOfPreference.setOnPreferenceClickListener(this);
		setScreenOffSum(callerFlashlight.isScreenOffPref());

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setScreenOffSum(boolean isChecked) {
		if (isChecked) {
			screenOfPreference.setSummary(getResources().getString(R.string.screen_off_ticked_sum));
		} else {
			screenOfPreference.setSummary(getResources().getString(R.string.screen_off_sum));
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		if (CallerFlashlight.LOG) Log.d(TAG, "onSharedPreferenceChanged: " + s);
		if (s.equals("type_list")) {
			//			lp = (ListPreference) findPreference("type_list");
			setTypeSum(Integer.valueOf(sharedPreferences.getString("type_list", "")));
			callerFlashlight.setWindowDimensions(getWindowManager());

		}
	}

	public void setTypeSum(int type) {

		//		lp = (ListPreference) findPreference("type_list");
		if (CallerFlashlight.LOG) Log.d(TAG, "setTypeSum");
		//		int type = callerFlashlight.getType();
		if (type == 1) {
			if (CallerFlashlight.LOG) Log.d(TAG, "sum type 1");
			lp.setSummary(getResources().getString(R.string.type_list_1));
		} else if (type == 2) {
			if (CallerFlashlight.LOG) Log.d(TAG, "sum type 2");
			lp.setSummary(getResources().getString(R.string.type_list_2));
		} else if (type == 3) {
			if (CallerFlashlight.LOG) Log.d(TAG, "sum type 3");
			lp.setSummary(getResources().getString(R.string.type_list_3));
		}

	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		dismissed = true;
		if ("screen_off".equals(preference.getKey())) {
			if (!callerFlashlight.isScreenOffPref()) {
				if (CallerFlashlight.LOG) Log.d(TAG, "callerFlashlight.isScreenOffPref()=false");
				new AlertDialog.Builder(this)
						.setTitle(getResources().getString(R.string.warning))
						.setMessage(getResources().getString(R.string.warning_screenoff))
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(R.string.enable_anyway, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int whichButton) {
								if (CallerFlashlight.LOG) Log.d(TAG, "whichButton: " + whichButton);
								dismissed = false;
								callerFlashlight.setScreenOffPref(true);
								setScreenOffSum(true);
								screenOfPreference.setChecked(true);
							}
						})
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int whichButton) {
								if (CallerFlashlight.LOG) Log.d(TAG, "whichButton: " + whichButton);
								dismissed = false;
								callerFlashlight.setScreenOffPref(false);
								setScreenOffSum(false);
								screenOfPreference.setChecked(false);
							}
						}).setCancelable(false)/*.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialogInterface) {
						if (dismissed) {
							if (CallerFlashlight.LOG) Log.d(TAG, "onDismiss");
							screenOfPreference.setChecked(false);
							callerFlashlight.setScreenOffPref(false);
							setScreenOffSum(false);
						}
					}
				})*/.show();
				return true;
			} else {
				if (CallerFlashlight.LOG) Log.d(TAG, "callerFlashlight.isScreenOffPref()=true");
				screenOfPreference.setChecked(false);
				callerFlashlight.setScreenOffPref(false);
				setScreenOffSum(false);
				return false;

			}
		}
		return false;

	}
}
