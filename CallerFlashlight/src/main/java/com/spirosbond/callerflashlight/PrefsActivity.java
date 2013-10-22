package com.spirosbond.callerflashlight;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by spiros on 8/5/13.
 */
public class PrefsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = PrefsActivity.class.getSimpleName();
	ListPreference lp;
	CallerFlashlight callerFlashlight;
	CheckBoxPreference screenOfPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callerFlashlight = (CallerFlashlight) this.getApplication();
		addPreferencesFromResource(R.xml.prefs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		lp = (ListPreference) findPreference("type_list");
		lp.setValue(String.valueOf(callerFlashlight.getType()));
		setTypeSum();

		screenOfPreference = (CheckBoxPreference) findPreference("screen_off");
		setScreenOffSum();

	}

	private void setScreenOffSum() {
		if (screenOfPreference.isChecked()) {
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
			setTypeSum();

		} else if (s.equals("screen_off")) {
			setScreenOffSum();
		}
	}

	public void setTypeSum() {

		lp = (ListPreference) findPreference("type_list");
		if (CallerFlashlight.LOG) Log.d(TAG, "setTypeSum");
		int type = callerFlashlight.getType();
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
}
