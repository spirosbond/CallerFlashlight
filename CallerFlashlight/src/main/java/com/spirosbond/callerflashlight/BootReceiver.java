package com.spirosbond.callerflashlight;

/**
 * Created by spiros on 9/5/13.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = BootReceiver.class.getSimpleName();
	CallerFlashlight callerFlashlight;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d(TAG, "onBootReceived");
		callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		if (callerFlashlight.isBootReceiver()) {
			if (callerFlashlight.isServiceRunning()) {
				Log.d(TAG, "Restarting Service");
				context.startService(new Intent(context, NotificationService.class));
			}
		}
	}
}
