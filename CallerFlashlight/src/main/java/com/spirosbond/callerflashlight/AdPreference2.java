package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * Created by spiros on 12/31/13.
 */
public class AdPreference2 extends Preference {

	public AdPreference2(Context context, AttributeSet attrs, int defStyle) {super(context, attrs, defStyle);}

	public AdPreference2(Context context, AttributeSet attrs) {super(context, attrs);}

	public AdPreference2(Context context) {super(context);}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// this will create the linear layout defined in ads_layout.xml
		View view = super.onCreateView(parent);

		// the context is a PreferenceActivity
		Activity activity = (Activity) getContext();

		// Create the adView
		AdView adView = new AdView(activity, AdSize.BANNER, "ca-app-pub-4450409123751393/8391059261");

		((LinearLayout) view).addView(adView);

		// Load the adView with the ad request
		adView.loadAd(new AdRequest());


		return view;
	}
}