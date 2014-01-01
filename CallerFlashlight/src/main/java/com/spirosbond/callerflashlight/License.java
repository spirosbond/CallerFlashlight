package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by spiros on 12/31/13.
 */
public class License extends Activity {
	private static final String TAG = License.class.getSimpleName();
	ProgressBar progressBar;
	TextView licenseTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.license_layout);
		if (CallerFlashlight.LOG) Log.d(TAG, "onCreate");
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		licenseTextView = (TextView) findViewById(R.id.license_textView);


		new LoadLicense().execute();
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

	public class LoadLicense extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... voids) {
			if (CallerFlashlight.LOG) Log.d(TAG, "doInBackground");
			licenseTextView.setVisibility(View.GONE);
			licenseTextView.setText("License");

			licenseTextView.setVisibility(View.VISIBLE);


			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (CallerFlashlight.LOG) Log.d(TAG, "onPostExecute");
			progressBar.setVisibility(View.GONE);

		}
	}
}
