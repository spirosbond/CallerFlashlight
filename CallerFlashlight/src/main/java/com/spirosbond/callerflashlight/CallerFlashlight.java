package com.spirosbond.callerflashlight;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

/**
 * Created by spiros on 8/4/13.
 */
public class CallerFlashlight extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_ALTERNATIVE = 2;
	public static final int TYPE_ALTERNATIVE_2 = 3;
	private static final String packages = "com.viber.voip, com.skype.raider, com.google.android.talk, com.google.android.gm, com.facebook.katana, com.whatsapp, com.google.android.apps.plus";
	private static final String TAG = CallerFlashlight.class.getSimpleName();
	private boolean callFlash = false, msgFlash = false, callFlashTest = false, msgFlashTest = false;
	private int callFlashOnDuration = 250, callFlashOffDuration = 250, msgFlashOnDuration = 250, msgFlashOffDuration = 250, msgFlashDuration = 3;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private boolean normalMode, vibrateMode, silentMode, sleepMode, appListCheck;
	private String sleepStart;
	private String sleepStop;
	private int sleepStartHour, sleepStartMinute, sleepStopHour, sleepStopMinute;
	private int type;
	private int msgFlashType;
	private boolean bootReceiver, serviceRunning;

	@Override
	public void onCreate() {
		super.onCreate();


		Log.d(TAG, "onCreated");
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		editor = prefs.edit();
		loadPreferences();

	}

	private void loadPreferences() {

		Log.d(TAG, "loadPreferences");

		callFlash = prefs.getBoolean("callFlash", false);
		msgFlash = prefs.getBoolean("msgFlash", false);
		callFlashOnDuration = prefs.getInt("callFlashOnDuration", 250);
		callFlashOffDuration = prefs.getInt("callFlashOffDuration", 250);
		msgFlashOnDuration = prefs.getInt("msgFlashOnDuration", 250);
		msgFlashOffDuration = prefs.getInt("msgFlashOffDuration", 250);
		msgFlashDuration = prefs.getInt("msgFlashDuration", 3);
		silentMode = prefs.getBoolean("silent_mode", true);
		vibrateMode = prefs.getBoolean("vibrate_mode", true);
		normalMode = prefs.getBoolean("normal_mode", true);
		sleepMode = prefs.getBoolean("sleep_check", false);
		sleepStart = prefs.getString("sleep_start", "");
		sleepStop = prefs.getString("sleep_stop", "");
		sleepStartHour = getSleepStartHour();
		sleepStopHour = getSleepStopHour();
		sleepStartMinute = getSleepStartMinute();
		sleepStopMinute = getSleepStopMinute();
		type = prefs.getInt("type", 1);
		msgFlashType = getMsgFlashType();
		appListCheck = prefs.getBoolean("app_list_check", false);
		bootReceiver = prefs.getBoolean("boot_receiver", false);
		serviceRunning = prefs.getBoolean("service_running", false);

	}

	public boolean isCallFlash() {
		callFlash = prefs.getBoolean("callFlash", false);
		return callFlash;
	}

	public void setCallFlash(boolean callFlash) {
		this.callFlash = callFlash;
		editor.putBoolean("callFlash", callFlash);
		editor.commit();
		Log.d(TAG, "setCallFlash: " + callFlash);
	}

	public boolean isMsgFlash() {
		msgFlash = prefs.getBoolean("msgFlash", false);
		return msgFlash;
	}

	public void setMsgFlash(boolean msgFlash) {
		this.msgFlash = msgFlash;
		editor.putBoolean("msgFlash", msgFlash);
		editor.commit();
		Log.d(TAG, "setMsgFlash: " + msgFlash);
	}

	public int getCallFlashOnDuration() {
		callFlashOnDuration = prefs.getInt("callFlashOnDuration", 250);
		return callFlashOnDuration;
	}

	public void setCallFlashOnDuration(int callFlashOnDuration) {
		this.callFlashOnDuration = callFlashOnDuration;
		editor.putInt("callFlashOnDuration", callFlashOnDuration);
		editor.commit();
		Log.d(TAG, "setCallFlashOnDuration: " + callFlashOnDuration);
	}

	public int getCallFlashOffDuration() {
		callFlashOffDuration = prefs.getInt("callFlashOffDuration", 250);
		return callFlashOffDuration;
	}

	public void setCallFlashOffDuration(int callFlashOffDuration) {
		this.callFlashOffDuration = callFlashOffDuration;
		editor.putInt("callFlashOffDuration", callFlashOffDuration);
		editor.commit();
		Log.d(TAG, "setCallFlashOffDuration: " + callFlashOffDuration);

	}

	public int getMsgFlashOnDuration() {
		msgFlashDuration = prefs.getInt("msgFlashDuration", 3);
		return msgFlashOnDuration;
	}

	public void setMsgFlashOnDuration(int msgFlashOnDuration) {
		this.msgFlashOnDuration = msgFlashOnDuration;
		editor.putInt("msgFlashOnDuration", msgFlashOnDuration);
		editor.commit();
		Log.d(TAG, "setMsgFlashOnDuration: " + msgFlashOnDuration);

	}

	public int getMsgFlashOffDuration() {
		return msgFlashOffDuration;
	}

	public void setMsgFlashOffDuration(int msgFlashOffDuration) {
		this.msgFlashOffDuration = msgFlashOffDuration;
		editor.putInt("msgFlashOffDuration", msgFlashOffDuration);
		editor.commit();
		Log.d(TAG, "setMsgFlashOffDuration: " + msgFlashOffDuration);

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		Log.d(TAG, s + " changed!");
		if (s.equals("normal_mode")) setNormalMode(sharedPreferences.getBoolean("normal_mode", false));
		else if (s.equals("vibrate_mode")) setVibrateMode(sharedPreferences.getBoolean("vibrate_mode", false));
		else if (s.equals("silent_mode")) setSilentMode(sharedPreferences.getBoolean("silent_mode", false));
		else if (s.equals("sleep_check")) setSleepMode(sharedPreferences.getBoolean("sleep_check", false));
		else if (s.equals("type_list")) {
			setType(Integer.valueOf(sharedPreferences.getString("type_list", "")));

		} else if (s.equals("sms_mode_list")) {
			setMsgFlashType(Integer.valueOf(sharedPreferences.getString("sms_mode_list", "")));

		}
//		else if (s.equals("app_list_check")) {
//			setAppListCheck(sharedPreferences.getBoolean("app_list_check", false));
//			setBootReceiver(sharedPreferences.getBoolean("app_list_check", false));
//			if (isAppListCheck()) startService(new Intent(this, NotificationService.class));
//			if (!isAppListCheck()) stopService(new Intent(this, NotificationService.class));
//		}
		if (!(isNormalMode() || isSilentMode() || isSilentMode())) {
			setCallFlash(false);
			setMsgFlash(false);
		}


	}

	public int getMsgFlashDuration() {
		return msgFlashDuration;
	}

	public void setMsgFlashDuration(int msgFlashDuration) {
		this.msgFlashDuration = msgFlashDuration;
		editor.putInt("msgFlashDuration", msgFlashDuration);
		editor.commit();
		Log.d(TAG, "setMsgFlashDuration: " + msgFlashDuration);

	}

	public boolean isCallFlashTest() {
		return callFlashTest;
	}

	public void setCallFlashTest(boolean callFlashTest) {
		this.callFlashTest = callFlashTest;
	}

	public boolean isMsgFlashTest() {
		return msgFlashTest;
	}

	public void setMsgFlashTest(boolean msgFlashTest) {
		this.msgFlashTest = msgFlashTest;
	}

	public boolean isEnabled() {
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		boolean enabled = false;

		switch (am.getRingerMode()) {
			case AudioManager.RINGER_MODE_SILENT:
				Log.d(TAG, "Phone in silent mode");
				if (silentMode) enabled = true;
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				Log.d(TAG, "Phone in vibrate mode");
				if (vibrateMode) enabled = true;
				break;
			case AudioManager.RINGER_MODE_NORMAL:
				Log.d(TAG, "Phone in normal mode");
				if (normalMode) enabled = true;
				break;
		}

		Time now = new Time();
		now.setToNow();

		Log.d(TAG, "time: " + now.hour + ":" + now.minute);

		if (isSleepMode()) {
			if (now.hour > sleepStartHour && now.hour < sleepStopHour) {
				enabled = false;
			} else if (now.hour == sleepStartHour && now.hour == sleepStopHour) {
				if (now.minute >= sleepStartMinute && now.minute <= sleepStopMinute) {
					enabled = false;
				}
			} else if (now.hour == sleepStartHour) {
				if (now.minute >= sleepStartMinute) {
					enabled = false;
				}
			} else if (now.hour == sleepStopHour) {
				if (now.minute <= sleepStopMinute) {
					enabled = false;
				}
			}
		}
		Log.d(TAG, "enabled: " + enabled);
		return enabled;
	}

	public boolean isNormalMode() {
		normalMode = prefs.getBoolean("normal_mode", true);
		return normalMode;
	}

	public void setNormalMode(boolean normalMode) {
		this.normalMode = normalMode;
		editor.putBoolean("normal_mode", normalMode);
		editor.commit();
	}

	public boolean isVibrateMode() {
		vibrateMode = prefs.getBoolean("vibrate_mode", true);
		return vibrateMode;
	}

	public void setVibrateMode(boolean vibrateMode) {
		this.vibrateMode = vibrateMode;
		editor.putBoolean("vibrate_mode", vibrateMode);
		editor.commit();
	}

	public boolean isSilentMode() {
		silentMode = prefs.getBoolean("silent_mode", true);
		return silentMode;
	}

	public void setSilentMode(boolean silentMode) {
		this.silentMode = silentMode;
		editor.putBoolean("silent_mode", silentMode);
		editor.commit();
	}

	public String getSleepStart() {
		sleepStart = prefs.getString("sleep_start", "");
		return sleepStart;
	}

	public void setSleepStart(String sleepStart) {
		this.sleepStart = sleepStart;
		editor.putString("sleep_start", sleepStart);
		editor.commit();
	}

	public String getSleepStop() {
		sleepStart = prefs.getString("sleep_stop", "");
		return sleepStop;
	}

	public void setSleepStop(String sleepStop) {
		this.sleepStop = sleepStop;
		editor.putString("sleep_stop", sleepStop);
		editor.commit();
	}

	public int getSleepStartHour() {
		sleepStartHour = prefs.getInt("sleep_start_hour", 0);
		return sleepStartHour;
	}

	public void setSleepStartHour(int sleepStartHour) {
		this.sleepStartHour = sleepStartHour;
		editor.putInt("sleep_start_hour", sleepStartHour);
		editor.commit();
	}

	public int getSleepStartMinute() {
		sleepStartMinute = prefs.getInt("sleep_start_minute", 0);
		return sleepStartMinute;
	}

	public void setSleepStartMinute(int sleepStartMinute) {
		this.sleepStartMinute = sleepStartMinute;
		editor.putInt("sleep_start_minute", sleepStartMinute);
		editor.commit();
	}

	public int getSleepStopHour() {
		sleepStopHour = prefs.getInt("sleep_stop_hour", 0);
		return sleepStopHour;
	}

	public void setSleepStopHour(int sleepStopHour) {
		this.sleepStopHour = sleepStopHour;
		editor.putInt("sleep_stop_hour", sleepStopHour);
		editor.commit();
	}

	public int getSleepStopMinute() {
		sleepStopMinute = prefs.getInt("sleep_stop_minute", 0);
		return sleepStopMinute;
	}

	public void setSleepStopMinute(int sleepStopMinute) {
		this.sleepStopMinute = sleepStopMinute;
		editor.putInt("sleep_stop_minute", sleepStopMinute);
		editor.commit();
	}

	public boolean isSleepMode() {
		sleepMode = prefs.getBoolean("sleep_check", false);
		return sleepMode;
	}

	public void setSleepMode(boolean sleepMode) {
		this.sleepMode = sleepMode;
		editor.putBoolean("sleep_check", sleepMode);
		editor.commit();
	}

	public int getType() {
		type = prefs.getInt("type", 1);
		Log.d(TAG, "type is: " + type);
		return type;
	}

	public void setType(int type) {
		this.type = type;
		editor.putInt("type", type);
		editor.commit();
		Log.d(TAG, "type set to: " + type);
	}

	public int getMsgFlashType() {
		msgFlashType = prefs.getInt("sms_mode_type", 1);
		Log.d(TAG, "sms_mode_type is: " + msgFlashType);
		return msgFlashType;
	}

	public void setMsgFlashType(int msgFlashType) {
		this.msgFlashType = msgFlashType;
		editor.putInt("sms_mode_type", msgFlashType);
		editor.commit();
		Log.d(TAG, "sms_mode_type set to: " + msgFlashType);
	}

	public boolean loadApp(String packageName) {
		return prefs.getBoolean(packageName, false);
	}

	public void saveApp(String packageName, boolean b) {
		editor.putBoolean(packageName, b);
		editor.commit();
	}

	public boolean isAppListCheck() {
		appListCheck = prefs.getBoolean("app_list_check", false);
		return appListCheck;
	}

	public void setAppListCheck(boolean appListCheck) {
		this.appListCheck = appListCheck;
		editor.putBoolean("app_list_check", appListCheck);
		editor.commit();
	}

	public boolean isBootReceiver() {
		bootReceiver = prefs.getBoolean("boot_receiver", false);
		return bootReceiver;
	}

	public void setBootReceiver(boolean bootReceiver) {
		this.bootReceiver = bootReceiver;
		editor.putBoolean("boot_receiver", bootReceiver);
		editor.commit();
	}

	public boolean isServiceRunning() {
		serviceRunning = prefs.getBoolean("service_running", false);
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
		editor.putBoolean("service_running", serviceRunning);
		editor.commit();
	}

	public boolean isInPackages(String packageName) {
		return packages.contains(packageName);
	}
}
