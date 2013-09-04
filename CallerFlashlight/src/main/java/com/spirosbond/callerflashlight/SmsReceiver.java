package com.spirosbond.callerflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by spiros on 8/4/13.
 */
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = SmsReceiver.class.getSimpleName();
	CallerFlashlight callerFlashlight;

	@Override
	public void onReceive(Context context, Intent intent) {
		callerFlashlight = (CallerFlashlight) context.getApplicationContext();
		if (callerFlashlight.isMsgFlash() && callerFlashlight.isEnabled()) {
			new ManageFlash().execute(callerFlashlight.getMsgFlashOnDuration(), callerFlashlight.getMsgFlashOffDuration(),
							callerFlashlight.getMsgFlashDuration());
		}
	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private Flash flash = new Flash(callerFlashlight);

		public ManageFlash() {
			Flash.incRunning();
		}

		@Override
		protected String doInBackground(Integer... integers) {
			Log.d(TAG, "doInBackgroung Started");
			long start = System.currentTimeMillis();

			if (callerFlashlight.getMsgFlashType() == 1) {
				int durMillis = integers[2] * 1000;
				while (System.currentTimeMillis() - start <= durMillis) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
				}
			} else if (callerFlashlight.getMsgFlashType() == 2) {
				int times = 0;
				int repeats = integers[2];
				while (times < repeats) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					times = times + 1;
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			Log.d(TAG, "onPostExecute Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Log.d(TAG, "onCancelled Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}


	}
}


