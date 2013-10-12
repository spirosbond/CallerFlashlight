package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by spiros on 8/5/13.
 */
public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		ImageView logo = (ImageView) findViewById(R.id.aboutImageView);
		logo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.round));
	}
}
