package com.spirosbond.callerflashlight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
	int accessibilityEnabled = 0;
	Preference moreFlashCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callerFlashlight = (CallerFlashlight) this.getApplication();
		addPreferencesFromResource(R.xml.sms_prefs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		appList = findPreference("app_list");
		appList.setOnPreferenceClickListener(this);
		moreFlashCheck = findPreference("app_list_check");
		moreFlashCheck.setOnPreferenceClickListener(this);

		setModeSum();

		try {
			accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
			if (CallerFlashlight.LOG) Log.d(TAG, "ACCESSIBILITY: " + accessibilityEnabled);
			if (accessibilityEnabled == 0) {
				appList.setEnabled(false);
				moreFlashCheck.setSummary(getResources().getString(R.string.more_flash_warning_sum));
			} else {
				appList.setEnabled(true);
				moreFlashCheck.setSummary(getResources().getString(R.string.app_list_check_sum));
			}
		} catch (Settings.SettingNotFoundException e) {
			if (CallerFlashlight.LOG) Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
			appList.setEnabled(false);

		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		appList = findPreference("app_list");
		moreFlashCheck = findPreference("app_list_check");

		try {
			accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
			if (CallerFlashlight.LOG) Log.d(TAG, "ACCESSIBILITY: " + accessibilityEnabled);
			if (accessibilityEnabled == 0) {
				appList.setEnabled(false);
				moreFlashCheck.setSummary(getResources().getString(R.string.more_flash_warning_sum));
			} else {
				appList.setEnabled(true);
				moreFlashCheck.setSummary(getResources().getString(R.string.app_list_check_sum));
			}
		} catch (Settings.SettingNotFoundException e) {
			if (CallerFlashlight.LOG) Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
			appList.setEnabled(false);

		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		if (CallerFlashlight.LOG) Log.d(TAG, "onSharedPreferenceChanged: " + s);
		if (s.equals("sms_mode_list")) {
			//			lp = (ListPreference) findPreference("type_list");
			setModeSum();

		}
	}

	private void setModeSum() {

		lp = (ListPreference) findPreference("sms_mode_list");
		sbp = (SeekBarPreference) findPreference("msgFlashDuration");
		if (CallerFlashlight.LOG) Log.d(TAG, "setSMSModeSum");
		int type = callerFlashlight.getMsgFlashType();
		if (type == 1) {
			if (CallerFlashlight.LOG) Log.d(TAG, "sum type 1");
			lp.setSummary(getResources().getString(R.string.sms_mode_list_1));
			sbp.setmUnitsRight(" seconds");
			sbp.updateView(getListView());
		} else if (type == 2) {
			if (CallerFlashlight.LOG) Log.d(TAG, "sum type 2");
			lp.setSummary(getResources().getString(R.string.sms_mode_list_2));
			sbp.setmUnitsRight(" times");
//			sbp.updateView(getListView());
		}

	}

	@Override
	public boolean onPreferenceClick(Preference preference) {

		if (CallerFlashlight.LOG) Log.d(TAG, "preference clicked: " + preference.getKey());
		if (preference.getKey().equals("app_list")) {
			startActivity(new Intent(this, AppList.class));
		} else if (preference.getKey().equals("app_list_check")) {
//			Toast.makeText(this, "Please enable Notification Service at Accessibility Settings", Toast.LENGTH_LONG).show();
			startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
		}

		return false;
	}


}


