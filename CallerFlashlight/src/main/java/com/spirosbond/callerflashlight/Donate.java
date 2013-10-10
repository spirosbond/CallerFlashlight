package com.spirosbond.callerflashlight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.appflood.AppFlood;
import com.startapp.android.publish.StartAppAd;

/**
 * Created by spiros on 8/28/13.
 */
public class Donate extends PreferenceActivity implements Preference.OnPreferenceClickListener { //

	private static final String TAG = Donate.class.getSimpleName();
	private CallerFlashlight myapp;
	//	private SwitchPreference appflood2, appflood;
	private Preference appoftheday;
	private ConnectivityManager connectivityManager;
	private Preference startapp;
	private StartAppAd startAppAd = null;

	@Override
	protected void onResume() {
		super.onResume();
		if (startAppAd == null) {
			startAppAd = new StartAppAd(this);
			startAppAd.loadAd();
		}
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) { //
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		addPreferencesFromResource(R.xml.donateprefs);
		StartAppAd.init(this, "108632531", "208372780");
		AppFlood.initialize(this, "Thib0u8GfGgfXsLX", "6GX8sMOv1791L521de8ea", AppFlood.AD_ALL);
		startAppAd = new StartAppAd(this);
		startAppAd.loadAd();
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		appoftheday = findPreference("appoftheday");
		startapp = findPreference("startapp");
		startapp.setOnPreferenceClickListener(this);
		appoftheday.setOnPreferenceClickListener(this);
		myapp = (CallerFlashlight) getApplication();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
//		appflood = (SwitchPreference) findPreference("appflood");
//		appflood2 = (SwitchPreference) findPreference("appflood2");
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle("Thank you for donating!");
		builder1.setMessage(getResources().getString(R.string.donateDialog));
		builder1.setCancelable(true);
		builder1.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
		builder1.setPositiveButton("Got it!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
//		if (!myapp.loadDonate("appflood")) appflood.setSummary(R.string.summaryDonate1);
//		else appflood.setSummary(R.string.summaryDontDonate1);
//		if (!myapp.loadDonate("appflood2")) appflood2.setSummary(R.string.summaryDonate2);
//		else appflood2.setSummary(R.string.summaryDontDonate2);
//		myapp.registerShared(this);
	}

//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Respond to the action bar's Up/Home button
//		switch (item.getItemId()) {
//			case android.R.id.home:
//				setResult(RESULT_OK, new Intent());
//				myapp.unregisterShared(this);
//				finish();
//				return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//
//	@Override
//	public void onBackPressed() {
//		setResult(RESULT_OK, new Intent());
//		myapp.unregisterShared(this);
//		finish();
//	}

//	@Override
//	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//		Log.d("MPIKA", "" + key);
//		if (key.equals("appflood")) {
//			if (sharedPreferences.getBoolean("appflood", false)) {
//				Log.d("MPIKA", "true");
//				appflood.setSummary(R.string.summaryDontDonate1);
//			} else {
//				Log.d("MPIKA", "false");
//				appflood.setSummary(R.string.summaryDonate1);
//			}
//		}
//		if (key.equals("appflood2")) {
//			if (sharedPreferences.getBoolean("appflood2", false)) {
//				Log.d("MPIKA", "true");
//				appflood2.setSummary(R.string.summaryDontDonate2);
//			} else {
//				Log.d("MPIKA", "false");
//				appflood2.setSummary(R.string.summaryDonate2);
//			}
//		}
//
//	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (connectivityManager.getActiveNetworkInfo() != null) {
			if (preference.getKey().equals("appoftheday")) {
				AppFlood.showInterstitial(this);
			}
			if (preference.getKey().equals("startapp")) {

				startAppAd.showAd();
				startAppAd.loadAd();

			}
			return true;
		} else {
			Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
		}
		return true;
	}
}