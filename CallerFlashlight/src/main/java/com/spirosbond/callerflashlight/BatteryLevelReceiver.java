package com.spirosbond.callerflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by spiros on 11/7/13.
 */
public class BatteryLevelReceiver extends BroadcastReceiver {
	private static final String TAG = BatteryLevelReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		//		String level = BatteryManager.EXTRA_LEVEL;
		if (CallerFlashlight.LOG) Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
		if (CallerFlashlight.LOG) Log.d(TAG, "intent action: " + intent.getAction());
		//		if (CallerFlashlight.LOG) Log.d(TAG, "battery level:" + level);
		if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) callerFlashlight.setLowBat(true);
		else callerFlashlight.setLowBat(false);

	}
}
