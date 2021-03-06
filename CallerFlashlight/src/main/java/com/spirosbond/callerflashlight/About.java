package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by spiros on 8/5/13.
 */
public class About extends Activity {
	TextView footer;
	CallerFlashlight callerFlashlight;
	ImageView xdaIcon, googlePlusIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		callerFlashlight = (CallerFlashlight) getApplicationContext();
		footer = (TextView) findViewById(R.id.about_footer);
		try {
			footer.setText(footer.getText() + " " + getResources().getString(R.string.version) + " " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + ".\n" + getResources().getString(R.string.allRightsReserved));
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

	public void onOpenXdaClick(View view) {
		openBrowser("http://forum.xda-developers.com/showthread.php?t=2403340");
	}

	public void onOpenGoogleplusClick(View view) {
		openBrowser("https://plus.google.com/109742763412505279388/posts");
	}

	public void onOpenCrowdinClick(View view) {
		openBrowser("http://crowdin.net/project/callflash/invite");
	}

	private void openBrowser(String url) {
		startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
	}
}
