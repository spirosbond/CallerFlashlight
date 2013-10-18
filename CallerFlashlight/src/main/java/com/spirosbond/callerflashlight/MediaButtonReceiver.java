package com.spirosbond.callerflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by spiros on 10/18/13.
 */
public class MediaButtonReceiver extends BroadcastReceiver {

	private static final String TAG = MediaButtonReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (CallerFlashlight.LOG) Log.d(TAG, "onReceived");
		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		callerFlashlight.setVolumeButtonPressed(true);
	}
}
