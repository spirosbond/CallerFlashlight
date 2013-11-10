package com.spirosbond.callerflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by spiros on 11/11/13.
 */
public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = BootReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");
		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		callerFlashlight.setLowBat(false);
	}
}
