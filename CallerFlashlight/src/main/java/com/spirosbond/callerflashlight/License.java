package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by spiros on 12/31/13.
 */
public class License extends Activity {
	private static final String TAG = License.class.getSimpleName();
	ProgressBar progressBar;
	WebView licenseWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.license_layout);
		if (CallerFlashlight.LOG) Log.d(TAG, "onCreate");
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		licenseWebView = (WebView) findViewById(R.id.license_textView);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		licenseWebView.loadUrl("http://www.gnu.org/licenses/gpl-3.0.txt");
		licenseWebView.setVisibility(View.VISIBLE);
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
