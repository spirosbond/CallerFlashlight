package com.spirosbond.callerflashlight;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by spiros on 8/24/13.
 */
public class CallPrefs extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = CallPrefs.class.getSimpleName();
	ListPreference lp;
	CallerFlashlight callerFlashlight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callerFlashlight = (CallerFlashlight) this.getApplication();
		addPreferencesFromResource(R.xml.call_prefs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);

		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

//		lp = (ListPreference) findPreference("type_list");


	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

	}
}
