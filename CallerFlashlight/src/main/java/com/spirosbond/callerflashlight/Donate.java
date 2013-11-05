package com.spirosbond.callerflashlight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.InterstitialAd;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyVideoAd;


/**
 * Created by spiros on 8/28/13.
 */
public class Donate extends PreferenceActivity implements Preference.OnPreferenceClickListener, AdListener, AdColonyAdListener { //

	private static final String TAG = Donate.class.getSimpleName();
	//	private static StartAppAd startAppAd;
	private static AdColonyVideoAd adColonyVideoAd;
	//	private Preference appoftheday;
	private ConnectivityManager connectivityManager;
	private InterstitialAd interstitialAdMob;
	private Donate donate = this;

	@Override
	protected void onResume() {
		super.onResume();

		//		startAppAd = new StartAppAd(this);
		//		startAppAd.loadAd();

		adColonyVideoAd = new AdColonyVideoAd();
		if (CallerFlashlight.LOG) Log.d(TAG, "onResume");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) { //
		super.onCreate(savedInstanceState);
		if (CallerFlashlight.LOG) Log.d(TAG, "onCreate");
		addPreferencesFromResource(R.xml.donateprefs);
		CallerFlashlight callerFlashlight = (CallerFlashlight) getApplication();
		//		StartAppAd.init(this, "108632531", "208372780");
		//		AppFlood.initialize(this, "Thib0u8GfGgfXsLX", "6GX8sMOv1791L521de8ea", AppFlood.AD_ALL);
		//		AdColony.configure(this, "version=1,store:google", "appc0bebfc9f4a3489fb82153", "vz9bf8a5eb30ef477798b82b", "vz81c21390fa4e4b25aaa8ed", "vzf738e644f1394a9abcf4cf");
		//		startAppAd = new StartAppAd(this);
		//		startAppAd.loadAd();
		callerFlashlight.configureAdColony(this);
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		adColonyVideoAd = new AdColonyVideoAd();

		interstitialAdMob = new InterstitialAd(this, "ca-app-pub-4450409123751393/1956296862");

		//		appoftheday = findPreference("appoftheday");
		//		startapp = findPreference("startapp");
		Preference adcolony = findPreference("adcolony");
		Preference adMob = findPreference("adMob");
		Preference paypal = findPreference("paypal");
		//		startapp.setOnPreferenceClickListener(this);
		//		appoftheday.setOnPreferenceClickListener(this);
		adcolony.setOnPreferenceClickListener(this);
		adMob.setOnPreferenceClickListener(this);
		paypal.setOnPreferenceClickListener(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle(getResources().getString(R.string.donate_dialog_title));
		builder1.setMessage(getResources().getString(R.string.donateDialog));
		builder1.setCancelable(true);
		builder1.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
		builder1.setPositiveButton(getResources().getString(R.string.donate_dialog_ok),
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
				//				AppFlood.showInterstitial(this);
			} else if ("startapp".equals(preference.getKey())) {

				//				startAppAd.showAd();
				//				startAppAd.loadAd();

			} else if ("adcolony".equals(preference.getKey())) {
				//				adColonyVideoAd = new AdColonyVideoAd();

				try {
					if (adColonyVideoAd != null)
						adColonyVideoAd.show();
					adColonyVideoAd = new AdColonyVideoAd();
				} catch (Exception e) {
					e.printStackTrace();
					BugSenseHandler.sendException(e);
				}


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


			} else if ("paypal".equals(preference.getKey())) {
				Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=Z5V7A5QNQDFPS"));
				startActivity(browse);
			}
		} else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
		}
		return true;
	}

	@Override
	public void onReceiveAd(Ad ad) {
		if (CallerFlashlight.LOG) Log.d("OK", "Received ad");
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
		if (CallerFlashlight.LOG) Log.d(TAG, "onAdColonyAdAttemptFinished");
		//		adColonyVideoAd.show();

	}

	@Override
	public void onAdColonyAdStarted(AdColonyAd adColonyAd) {
		if (CallerFlashlight.LOG) Log.d(TAG, "onAdColonyAdStarted");
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
}
