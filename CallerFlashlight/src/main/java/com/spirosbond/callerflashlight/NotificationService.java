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
	private String eventFrom;
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
		eventFrom = String.valueOf(event.getPackageName());
		if (CallerFlashlight.LOG)

			Log.d(TAG, "Got event from: " + eventFrom/*+ " of type: " + AccessibilityEvent.eventTypeToString(event.getEventType()) */ + " with notification flag: " + flags);
		//		Toast.makeText(getApplicationContext(), "Got event from: " + event.getPackageName(), Toast.LENGTH_LONG).show();

		if (Flash.getRunning() < 1 && callerFlashlight.isMsgFlash() && callerFlashlight.isEnabled() && callerFlashlight.loadApp(String.valueOf(event.getPackageName())) && isValidFlag(flags)) {
			new ManageFlash().execute(callerFlashlight.getMsgFlashOnDuration(), callerFlashlight.getMsgFlashOffDuration(),
					callerFlashlight.getMsgFlashDuration());
		}
	}

	/**
	 * @param flag ***Invalids***
	 *             99:Viber during call
	 *             -10: Notification without flag
	 *             11: Skype actions during call
	 *             *0: Click a notification's button (ex. delete on Gmail)*
	 *             ?10: Hangouts during call?
	 *             ?98: Handcent SMS sent notification?
	 *             <p/>
	 *             ***Known Valids***
	 *             1: Viber incomming message
	 *             17: Gmail
	 *             *0: WhatsApp incomming message*
	 * @return true: Flag is Valid
	 * false: Flag is invalid
	 */
	private boolean isValidFlag(int flag) {
		boolean isValid;
		isValid = flag != -10 && flag != 10 && flag != 99 && flag != 0 && flag != 11;
		if (flag == 0 && eventFrom.contains("com.whatsapp")) isValid = true;
		return isValid;

	}

	@Override
	public void onInterrupt() {
		if (CallerFlashlight.LOG) Log.d(TAG, "***** onInterrupt");
		callerFlashlight.setServiceRunning(false);
	}

	@Override
	public void onServiceConnected() {
		if (CallerFlashlight.LOG) Log.d(TAG, "***** onServiceConnected");

		try {
			callerFlashlight = (CallerFlashlight) getApplication();
			callerFlashlight.setServiceRunning(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
