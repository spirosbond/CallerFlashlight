package com.spirosbond.callerflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

/**
 * Created by spiros on 10/18/13.
 */
public class MediaButtonReceiver extends BroadcastReceiver {

	private static final String TAG = MediaButtonReceiver.class.getSimpleName();
	private static int userVolume, newVolume, times;
	private static boolean lowVolume;
	private static Time now;

	public MediaButtonReceiver() {
		times = 0;
		//		userVolume = volume;
		//		now = new Time();
		//		now.setToNow();

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		try {

			//			AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
			//			userVolume = am.getStreamVolume(AudioManager.STREAM_RING);

			//			newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
			if (CallerFlashlight.LOG) Log.d(TAG, "onReceived: " + intent.getAction());
			times += 1;
			//			if (userVolume != newVolume) {
			//				lowVolume = true;
			//				return;
			//			}
			//						KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			//						KeyEvent event = (KeyEvent)intent.getExtras().get("android.media.RINGER_MODE_SILENT");
			//						if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
			//							Log.d(TAG,"KeyEvent.KEYCODE_VOLUME_DOWN");
			//						}
			//						if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
			//							Log.d(TAG,"KeyEvent.KEYCODE_VOLUME_UP");
			//						}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//		Time time = new Time();
		//		time.setToNow();
		//
		//		if (time.second - now.second > 2 || time.minute!=now.minute) {
		//		AudioManager audioManager;
		//		audioManager = (AudioManager) callerFlashlight.getSystemService(Context.AUDIO_SERVICE);
		//		if (CallerFlashlight.LOG) Log.d(TAG, "ringermode: " + audioManager.getRingerMode());
		if (CallerFlashlight.LOG) Log.d(TAG, "setVolumeButtonPressed, times: " + times);
		if (times > 2 && callerFlashlight.isVolumeButtonPref())
			callerFlashlight.setVolumeButtonPressed(true);
		//		}
	}
}
