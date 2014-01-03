package com.spirosbond.callerflashlight;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.os.AsyncTask;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by spiros on 8/23/13.
 */
public class NotificationService extends AccessibilityService {


	private static final String TAG = NotificationService.class.getSimpleName();
	private CallerFlashlight callerFlashlight;
	private Notification notification;
	private int flags = -10;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		notification = (Notification) event.getParcelableData();
		flags = -10;
		try {
			flags = notification.flags;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (CallerFlashlight.LOG)
			if (callerFlashlight.LOG)
				Log.d(TAG, "Got event from: " + String.valueOf(event.getPackageName()) /*+ " of type: " + AccessibilityEvent.eventTypeToString(event.getEventType()) */ + " with notification flag: " + flags);
		//		Toast.makeText(getApplicationContext(), "Got event from: " + event.getPackageName(), Toast.LENGTH_LONG).show();

		if (Flash.getRunning() < 1 && callerFlashlight.isMsgFlash() && callerFlashlight.isEnabled() && callerFlashlight.loadApp(String.valueOf(event.getPackageName())) && isValidFlag(flags)) {
			new ManageFlash().execute(callerFlashlight.getMsgFlashOnDuration(), callerFlashlight.getMsgFlashOffDuration(),
					callerFlashlight.getMsgFlashDuration());
		}
	}

	/**
	 * @param flags 99:Viber during call
	 *              -10: Long press a button
	 *              11: Skype actions during call
	 * @return
	 */
	private boolean isValidFlag(int flags) {
		return flags != -10 && flags != 10 && flags != 99 && flags != 11;
	}

	@Override
	public void onInterrupt() {
		if (CallerFlashlight.LOG) Log.d(TAG, "***** onInterrupt");
		callerFlashlight.setServiceRunning(false);
	}

	@Override
	public void onServiceConnected() {
		if (CallerFlashlight.LOG) Log.d(TAG, "***** onServiceConnected");

		callerFlashlight = (CallerFlashlight) getApplication();
		callerFlashlight.setServiceRunning(true);

		//				AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		//		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		//				info.notificationTimeout = 10000;

		//		info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
		//		info.feedbackType = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		//				setServiceInfo(info);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (CallerFlashlight.LOG) Log.d(TAG, "***** onDestroyed");
		callerFlashlight.setServiceRunning(false);
	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private Flash flash = new Flash(callerFlashlight);

		public ManageFlash() {
			Flash.incRunning();
		}
		//
		//		@Override
		//		protected void onPreExecute() {
		//			super.onPreExecute();
		//			flash = new Flash(callerFlashlight);
		//		}

		@Override
		protected String doInBackground(Integer... integers) {
			if (CallerFlashlight.LOG) Log.d(TAG, "doInBackgroung Started");
			long start = System.currentTimeMillis();
			int tries = 3;
			if (callerFlashlight.getMsgFlashType() == 1) {
				int durMillis = integers[2] * 1000;
				while (System.currentTimeMillis() - start <= durMillis && tries > 0) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					if (!Flash.gotCam) {
						if (CallerFlashlight.LOG) Log.d(TAG, "Flash failed, retrying..." + tries);
						tries = tries - 1;
					}
				}
			} else if (callerFlashlight.getMsgFlashType() == 2) {
				int times = 0;
				int repeats = integers[2];
				while (times < repeats && tries > 0) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));

					if (!Flash.gotCam) {
						if (CallerFlashlight.LOG) Log.d(TAG, "Flash failed, retrying..." + tries);
						tries = tries - 1;
					} else {
						times = times + 1;
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (CallerFlashlight.LOG) Log.d(TAG, "onPostExecute Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (CallerFlashlight.LOG) Log.d(TAG, "onCancelled Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}


	}
}
