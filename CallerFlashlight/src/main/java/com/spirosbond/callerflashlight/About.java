package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by spiros on 8/5/13.
 */
public class About extends Activity {
	TextView footer;
	CallerFlashlight callerFlashlight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		callerFlashlight = (CallerFlashlight) getApplicationContext();
		footer = (TextView) findViewById(R.id.about_footer);
		try {
			footer.setText(footer.getText() + " version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + ".\n" + getResources().getString(R.string.allRightsReserved));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (callerFlashlight.isLowBat()) {
			footer.setTextColor(Color.RED);
		} else {
			footer.setTextColor(Color.GREEN);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		ImageView logo = (ImageView) findViewById(R.id.aboutImageView);
		logo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.round));
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
