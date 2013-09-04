package com.spirosbond.callerflashlight;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by spiros on 8/23/13.
 */
public class NotificationService extends AccessibilityService {


	public static final String TAG = NotificationService.class.getSimpleName();

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		Log.d(TAG, "Got event from: " + event.getPackageName());
//		Toast.makeText(getApplicationContext(), "Got event from: " + event.getPackageName(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInterrupt() {
		Log.d(TAG, "***** onInterrupt");
	}

	@Override
	public void onServiceConnected() {
		Log.d(TAG, "***** onServiceConnected");


		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		info.notificationTimeout = 100;
		info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
		setServiceInfo(info);

	}
}
