package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by spiros on 8/5/13.
 */
public class HowTo extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_how_to);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
