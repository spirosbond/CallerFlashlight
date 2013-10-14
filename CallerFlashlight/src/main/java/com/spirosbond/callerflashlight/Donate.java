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
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.doubleclick.DfpInterstitialAd;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyVideoAd;
import com.startapp.android.publish.StartAppAd;


/**
 * Created by spiros on 8/28/13.
 */
public class Donate extends PreferenceActivity implements Preference.OnPreferenceClickListener, AdListener, AdColonyAdListener { //

	private static final String TAG = Donate.class.getSimpleName();
	private static StartAppAd startAppAd;
	private static AdColonyVideoAd adColonyVideoAd;
	private CallerFlashlight myapp;
	private Preference appoftheday;
	private ConnectivityManager connectivityManager;
	private Preference startapp;
	private Preference adcolony;
	private Preference adMob;
	private DfpInterstitialAd interstitialAdMob;
	private Donate donate = this;

	@Override
	protected void onResume() {
		super.onResume();

		startAppAd = new StartAppAd(this);
		startAppAd.loadAd();

		adColonyVideoAd = new AdColonyVideoAd();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) { //
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		addPreferencesFromResource(R.xml.donateprefs);
		StartAppAd.init(this, "108632531", "208372780");
		AppFlood.initialize(this, "Thib0u8GfGgfXsLX", "6GX8sMOv1791L521de8ea", AppFlood.AD_ALL);
		//		AdColony.configure(this, "version=1,store:google", "appc0bebfc9f4a3489fb82153", "vz9bf8a5eb30ef477798b82b", "vz81c21390fa4e4b25aaa8ed", "vzf738e644f1394a9abcf4cf");
		startAppAd = new StartAppAd(this);
		startAppAd.loadAd();
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		adColonyVideoAd = new AdColonyVideoAd();

		interstitialAdMob = new DfpInterstitialAd(this, "ca-app-pub-4450409123751393/1956296862");

		appoftheday = findPreference("appoftheday");
		startapp = findPreference("startapp");
		adcolony = findPreference("adcolony");
		adMob = findPreference("adMob");
		startapp.setOnPreferenceClickListener(this);
		appoftheday.setOnPreferenceClickListener(this);
		adcolony.setOnPreferenceClickListener(this);
		adMob.setOnPreferenceClickListener(this);
		myapp = (CallerFlashlight) getApplication();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

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

	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (connectivityManager.getActiveNetworkInfo() != null) {
			if ("appoftheday".equals(preference.getKey())) {
				AppFlood.showInterstitial(this);
			} else if ("startapp".equals(preference.getKey())) {

				startAppAd.showAd();
				startAppAd.loadAd();

			} else if ("adcolony".equals(preference.getKey())) {
				//				adColonyVideoAd = new AdColonyVideoAd();

				adColonyVideoAd.show();
				adColonyVideoAd = new AdColonyVideoAd();


				//        adColonyVideoAd.withListener(this);
				//				adColonyVideoAd.show();
				//				adColonyVideoAd = new AdColonyVideoAd();

			} else if ("adMob".equals(preference.getKey())) {


				interstitialAdMob.loadAd(new AdRequest());
				interstitialAdMob.setAdListener(donate);

				//				donate.runOnUiThread(new Runnable() {
				//					public void run() {
				//						interstitialAdMob.loadAd(new AdRequest());
				//						interstitialAdMob.setAdListener(donate);
				//					}
				//				});


			}
		} else {
			Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
		}
		return true;
	}

	@Override
	public void onReceiveAd(Ad ad) {
		Log.d("OK", "Received ad");
		if (ad == interstitialAdMob) {
			interstitialAdMob.show();
		}

	}

	@Override
	public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode errorCode) {

	}

	@Override
	public void onPresentScreen(Ad ad) {

	}

	@Override
	public void onDismissScreen(Ad ad) {

	}

	@Override
	public void onLeaveApplication(Ad ad) {

	}

	@Override
	public void onAdColonyAdAttemptFinished(AdColonyAd adColonyAd) {
		Log.d(TAG, "onAdColonyAdAttemptFinished");
		adColonyVideoAd.show();

	}

	@Override
	public void onAdColonyAdStarted(AdColonyAd adColonyAd) {
		Log.d(TAG, "onAdColonyAdStarted");
	}
}