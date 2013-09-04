package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by spiros on 8/28/13.
 */
public class Donate extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donate);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);

		WebView wv = (WebView) findViewById(R.id.webView);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setBackgroundColor(Color.TRANSPARENT);
		String html = "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadboltads.net/show_app_ad.js?section_id=139088876'></script></body></html>";
		wv.loadData(html, "text/html", "utf-8");
	}
//
//	/* Class that prevents opening the Browser */
//	private class InsideWebViewClient extends WebViewClient {
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			view.loadUrl(url);
//			return true;
//		}
//	}
}
