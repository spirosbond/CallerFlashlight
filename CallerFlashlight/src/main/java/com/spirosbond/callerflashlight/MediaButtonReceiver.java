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
	private static int userVolume, newVolume;
	private static boolean lowVolume;

	public MediaButtonReceiver(int volume) {
		userVolume = volume;

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {

			//			AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
			//			userVolume = am.getStreamVolume(AudioManager.STREAM_RING);

			newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
			Log.d(TAG, "onReceived: " + intent.getAction() + " newVolume: " + newVolume + " userVolume: " + userVolume);
			if (userVolume != newVolume) {
				lowVolume = true;
				return;
			}
			//			KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			//			KeyEvent event = (KeyEvent)intent.getExtras().get("android.media.RINGER_MODE_SILENT");
			//			if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
			//				Log.d(TAG,"KeyEvent.KEYCODE_VOLUME_DOWN");
			//			}
			//			if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
			//				Log.d(TAG,"KeyEvent.KEYCODE_VOLUME_UP");
			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		CallerFlashlight callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		//		callerFlashlight.setVolumeButtonPressed(true);
	}
}
