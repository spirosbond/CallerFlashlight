package com.spirosbond.callerflashlight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by spiros on 8/24/13.
 */
public class MsgPrefs extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

	private static final String TAG = MsgPrefs.class.getSimpleName();
	ListPreference lp;
	CallerFlashlight callerFlashlight;
	SeekBarPreference sbp;
	Preference appList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callerFlashlight = (CallerFlashlight) this.getApplication();
		addPreferencesFromResource(R.xml.sms_prefs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		appList = findPreference("app_list");
		appList.setOnPreferenceClickListener(this);

		setModeSum();


	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		Log.d(TAG, "onSharedPreferenceChanged: " + s);
		if (s.equals("sms_mode_type")) {
//			lp = (ListPreference) findPreference("type_list");
			setModeSum();

		}
	}

	private void setModeSum() {

		lp = (ListPreference) findPreference("sms_mode_list");
		sbp = (SeekBarPreference) findPreference("msgFlashDuration");
		Log.d(TAG, "setSMSModeSum");
		int type = callerFlashlight.getMsgFlashType();
		if (type == 1) {
			Log.d(TAG, "sum type 1");
			lp.setSummary(getResources().getString(R.string.sms_mode_list_1));
			sbp.setmUnitsRight(" seconds");
			sbp.updateView(getListView());
		} else if (type == 2) {
			Log.d(TAG, "sum type 2");
			lp.setSummary(getResources().getString(R.string.sms_mode_list_2));
			sbp.setmUnitsRight(" times");
			sbp.updateView(getListView());
		}

	}

	@Override
	public boolean onPreferenceClick(Preference preference) {

		if (preference.getKey().equals("app_list")) {
			startActivity(new Intent(this, AppList.class));
		}

		return false;
	}
}


