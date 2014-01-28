package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.bugsense.trace.BugSenseHandler;
import com.jirbo.adcolony.AdColony;
import com.winsontan520.wversionmanager.library.WVersionManager;

/**
 * Created by spiros on 8/4/13.
 */
public class CallerFlashlight extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static final boolean LOG = false;
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_ALTERNATIVE = 2;
	public static final int TYPE_ALTERNATIVE_2 = 3;
	private static final String packages = "com.viber.voip,com.skype.raider,com.google.android.talk,com.google.android.gm,com.facebook.katana,com.whatsapp,com.google.android.apps.plus,mikado.bizcalpro,netgenius.bizcal,com.ryosoftware.contactdatesnotifier,com.twitter.android,com.fsck.k9,com.onegravity.k10.pro2,com.google.android.apps.plus,de.gmx.mobile.android.mail,com.quoord.tapatalkHD,com.quoord.tapatalkpro.activity,com.android.deskclock,com.android.alarmclock,com.sec.android.app.clockpackage,com.facebook.orca,com.joelapenna.foursquared,com.snapchat.android,com.instagram.android,com.handcent.nextsms,kik.android,jp.naver.line.android,com.imo.android.imoim,de.shapeservices.impluslite,de.shapeservices.implusfull,com.bbm,com.gvoip,com.snrblabs.grooveip,com.google.android.apps.googlevoice,com.jb.gosms,com.moplus.gvphone,com.skymobius.vtok";
	private static final String TAG = CallerFlashlight.class.getSimpleName();
	public static Runnable commit;
	private boolean callFlash = false, msgFlash = false, callFlashTest = false, msgFlashTest = false;
	private int callFlashOnDuration = 250, callFlashOffDuration = 250, msgFlashOnDuration = 250, msgFlashOffDuration = 250, msgFlashDuration = 3;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private boolean normalMode, vibrateMode, silentMode, sleepMode, appListCheck;
	private String sleepStart;
	private String sleepStop;
	private int sleepStartHour, sleepStartMinute, sleepStopHour, sleepStopMinute;
	private int type;
	private int screenWidth, screenHeight;
	private int msgFlashType;
	private boolean bootReceiver, serviceRunning, firstTime;
	private boolean volumeButtonPressed, volumeButtonPref;
	private BroadcastReceiver mediaButtonReceiver;
	//	private boolean screenOffPref;
	private boolean screenLockedPref, lowBat, lowBatPref;

	@Override
	public void onCreate() {
		super.onCreate();
		if (LOG) Log.d(TAG, "onCreated");

		//		registerVolumeButtonReceiver();

		BugSenseHandler.initAndStartSession(CallerFlashlight.this, "2b2cf28e");
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		editor = prefs.edit();

		commit = new Runnable() {
			public void run() {
				if (LOG) Log.d(TAG, "Committing preferences");
				savePreferences();
			}
		};
		loadPreferences();

		BugSenseHandler.addCrashExtraData("driver", String.valueOf(getType()));
		BugSenseHandler.addCrashExtraData("who", this.getPackageName());
	}

	public void registerVolumeButtonReceiver() {

		try {

			//			AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
			//			int volume = am.getStreamVolume(AudioManager.STREAM_RING);
			//		mediaButtonReceiver = new MediaButtonReceiver();
			//		am.registerMediaButtonEventReceiver(new ComponentName(getPackageName(), MediaButtonReceiver.class.getName()));

			if (LOG) Log.d(TAG, "Registering Receiver");
			volumeButtonPressed = false;
			//					IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
			IntentFilter filter = new IntentFilter();
			//		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
			filter.addAction("android.media.VOLUME_CHANGED_ACTION");
			//			filter.addAction("android.media.RINGER_MODE_CHANGED");
			mediaButtonReceiver = new MediaButtonReceiver();
			registerReceiver(mediaButtonReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void unregisterVolumeButtonReceiver() {
		if (LOG) Log.d(TAG, "Unregistering Receiver");
		unregisterReceiver(mediaButtonReceiver);
	}

	public Resources getMyResources() {

		//		getResources().updateConfiguration(getResources().getConfiguration(),getResources().getDisplayMetrics());
		return getResources();
	}

	private void loadPreferences() {

		if (LOG) Log.d(TAG, "loadPreferences");

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
		sleepStartHour = prefs.getInt("sleep_start_hour", 0);
		sleepStopHour = prefs.getInt("sleep_stop_hour", 0);
		sleepStartMinute = prefs.getInt("sleep_start_minute", 0);
		sleepStopMinute = prefs.getInt("sleep_stop_minute", 0);
		type = prefs.getInt("type", 1);
		msgFlashType = prefs.getInt("sms_mode_type", 1);
		appListCheck = prefs.getBoolean("app_list_check", false);
		//		bootReceiver = prefs.getBoolean("boot_receiver", false);
		serviceRunning = prefs.getBoolean("service_running", false);
		firstTime = prefs.getBoolean("first_time", true);
		//		screenOffPref = prefs.getBoolean("screen_off", false);
		screenLockedPref = prefs.getBoolean("screen_locked", false);
		lowBatPref = prefs.getBoolean("low_battery_pref", false);
		volumeButtonPref = prefs.getBoolean("volume_button_disable", false);

	}

	private void savePreferences() {

		if (LOG) Log.d(TAG, "savePreferences");

		editor.putBoolean("callFlash", callFlash);
		editor.putBoolean("msgFlash", msgFlash);
		editor.putInt("callFlashOnDuration", callFlashOnDuration);
		editor.putInt("callFlashOffDuration", callFlashOffDuration);
		editor.putInt("msgFlashOnDuration", msgFlashOnDuration);
		editor.putInt("msgFlashOffDuration", msgFlashOffDuration);
		editor.putInt("msgFlashDuration", msgFlashDuration);
		editor.putBoolean("silent_mode", silentMode);
		editor.putBoolean("vibrate_mode", vibrateMode);
		editor.putBoolean("normal_mode", normalMode);
		editor.putBoolean("sleep_check", sleepMode);
		//		editor.putString("sleep_start", sleepStart);
		//		editor.putString("sleep_stop", sleepStop);
		//		editor.putInt("sleep_start_hour", sleepStartHour);
		//		editor.putInt("sleep_stop_hour", sleepStopHour);
		//		editor.putInt("sleep_start_minute", sleepStartMinute);
		//		editor.putInt("sleep_stop_minute", sleepStopMinute);
		editor.putInt("type", type);
		editor.putInt("sms_mode_type", msgFlashType);
		editor.putBoolean("app_list_check", appListCheck);
		editor.putBoolean("service_running", serviceRunning);
		editor.putBoolean("first_time", firstTime);
		//		editor.putBoolean("screen_off", screenOffPref);
		//		editor.putBoolean("screen_locked", screenLockedPref);
		//		editor.putBoolean("low_battery_pref",lowBatPref);
		editor.commit();

	}

	public boolean isCallFlash() {
		//		callFlash = prefs.getBoolean("callFlash", false);
		return callFlash;
	}

	public void setCallFlash(boolean callFlash) {
		this.callFlash = callFlash;
		//		editor.putBoolean("callFlash", callFlash);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "setCallFlash: " + callFlash);
	}

	public boolean isMsgFlash() {
		//		msgFlash = prefs.getBoolean("msgFlash", false);
		return msgFlash;
	}

	public void setMsgFlash(boolean msgFlash) {
		this.msgFlash = msgFlash;
		//		editor.putBoolean("msgFlash", msgFlash);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "setMsgFlash: " + msgFlash);
	}

	public int getCallFlashOnDuration() {
		//		callFlashOnDuration = prefs.getInt("callFlashOnDuration", 250);
		return callFlashOnDuration;
	}

	public void setCallFlashOnDuration(int callFlashOnDuration) {
		this.callFlashOnDuration = callFlashOnDuration;
		//		editor.putInt("callFlashOnDuration", callFlashOnDuration);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "setCallFlashOnDuration: " + callFlashOnDuration);
	}

	public int getCallFlashOffDuration() {
		//		callFlashOffDuration = prefs.getInt("callFlashOffDuration", 250);
		return callFlashOffDuration;
	}

	public void setCallFlashOffDuration(int callFlashOffDuration) {
		this.callFlashOffDuration = callFlashOffDuration;
		//		editor.putInt("callFlashOffDuration", callFlashOffDuration);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "setCallFlashOffDuration: " + callFlashOffDuration);

	}

	public int getMsgFlashOnDuration() {
		//		msgFlashOnDuration = prefs.getInt("msgFlashOnDuration", 3);
		return msgFlashOnDuration;
	}

	public void setMsgFlashOnDuration(int msgFlashOnDuration) {
		this.msgFlashOnDuration = msgFlashOnDuration;
		//		editor.putInt("msgFlashOnDuration", msgFlashOnDuration);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "setMsgFlashOnDuration: " + msgFlashOnDuration);

	}

	public int getMsgFlashOffDuration() {
		//		msgFlashOffDuration = prefs.getInt("msgFlashOffDuration", 3);
		return msgFlashOffDuration;
	}

	public void setMsgFlashOffDuration(int msgFlashOffDuration) {
		this.msgFlashOffDuration = msgFlashOffDuration;
		//		editor.putInt("msgFlashOffDuration", msgFlashOffDuration);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "setMsgFlashOffDuration: " + msgFlashOffDuration);

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		if (LOG) Log.d(TAG, s + " changed!");
		if (s.equals("normal_mode")) setNormalMode(sharedPreferences.getBoolean("normal_mode", false));
		else if (s.equals("vibrate_mode")) setVibrateMode(sharedPreferences.getBoolean("vibrate_mode", false));
		else if (s.equals("silent_mode")) setSilentMode(sharedPreferences.getBoolean("silent_mode", false));
		else if (s.equals("sleep_check")) setSleepMode(sharedPreferences.getBoolean("sleep_check", false));
		else if (s.equals("msgFlashDuration")) setMsgFlashDuration(sharedPreferences.getInt("msgFlashDuration", 3));
		/*else if (s.equals("type_list")) {
			setType(Integer.valueOf(sharedPreferences.getString("type_list", "")));

		} *//*else if (s.equals("sms_mode_list")) {
			setMsgFlashType(Integer.valueOf(sharedPreferences.getString("sms_mode_list", "")));

		}*/ /*else if (s.equals("screen_off")) {
			setScreenOffPref(sharedPreferences.getBoolean("screen_off", false));
		}*/
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
		//		editor.putInt("msgFlashDuration", msgFlashDuration);
		//commit.run();
		if (LOG) Log.d(TAG, "setMsgFlashDuration: " + msgFlashDuration);

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
				if (LOG) Log.d(TAG, "Phone in silent mode");
				if (isSilentMode()) enabled = true;
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				if (LOG) Log.d(TAG, "Phone in vibrate mode");
				if (isVibrateMode()) enabled = true;
				break;
			case AudioManager.RINGER_MODE_NORMAL:
				if (LOG) Log.d(TAG, "Phone in normal mode");
				if (isNormalMode()) enabled = true;
				break;
		}

		Time now = new Time();
		now.setToNow();

		if (LOG)
			Log.d(TAG, "time: " + now.hour + ":" + now.minute + " - " + "sleep start: " + getSleepStartHour() + ":" + getSleepStartMinute() + " - " + "sleep stop: " + getSleepStopHour() + ":" + getSleepStopMinute());

		if (isSleepMode() && enabled) {
			if (LOG) Log.d(TAG, "Checking Sleep periods");
			if (now.hour > getSleepStartHour() && now.hour < getSleepStopHour()) {
				if (LOG) Log.d(TAG, "1");
				enabled = false;
			} else if (now.hour == getSleepStartHour() && now.hour == getSleepStopHour()) {
				if (now.minute >= getSleepStartMinute() && now.minute <= getSleepStopMinute()) {
					if (LOG) Log.d(TAG, "2");
					enabled = false;
				}
			} else if (now.hour == getSleepStartHour()) {
				if (now.minute >= getSleepStartMinute()) {
					if (LOG) Log.d(TAG, "3");
					enabled = false;
				}
			} else if (now.hour == getSleepStopHour()) {
				if (now.minute <= getSleepStopMinute()) {
					if (LOG) Log.d(TAG, "4");
					enabled = false;
				}
			} else if (now.hour < getSleepStartHour() && now.hour < getSleepStopHour() && getSleepStartHour() > getSleepStopHour()) {
				if (LOG) Log.d(TAG, "5");
				enabled = false;
			} else if (now.hour > getSleepStartHour() && now.hour > getSleepStopHour() && getSleepStartHour() > getSleepStopHour()) {
				if (LOG) Log.d(TAG, "6");
				enabled = false;
			}
		}

		//		if (screenOffPref)
		//			enabled = checkIfscreenOff(enabled);


		if (screenLockedPref)
			enabled = checkIfLocked(enabled);

		if (lowBatPref)
			enabled = checkLowBat(enabled);


		if (LOG) Log.d(TAG, "enabled: " + enabled);
		return enabled;
	}

	private boolean checkLowBat(boolean enabled) {
		if (LOG) Log.d(TAG, "checkLowBat");
		if (isLowBat()) {
			if (LOG) Log.d(TAG, "Low Battery");
			enabled = false;
		}
		return enabled;
	}

	private boolean checkIfLocked(boolean enabled) {
		KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
		if (myKM.inKeyguardRestrictedInputMode()) {
			if (LOG) Log.d(TAG, "screen is locked");
		} else {
			if (LOG) Log.d(TAG, "screen is NOT locked");
			enabled = false;
		}

		return enabled;
	}

	private boolean checkIfscreenOff(boolean enabled) {
		PowerManager powermanager;
		powermanager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		if (powermanager.isScreenOn()) {
			if (LOG) Log.d(TAG, "screen is ON");
			enabled = false;
		} else {
			if (LOG) Log.d(TAG, "screen is OFF");
		}
		return enabled;

	}

	public boolean isNormalMode() {
		//		normalMode = prefs.getBoolean("normal_mode", true);
		return normalMode;
	}

	public void setNormalMode(boolean normalMode) {
		this.normalMode = normalMode;
		//		editor.putBoolean("normal_mode", normalMode);
		//		editor.commit();
		//commit.run();
	}

	public boolean isVibrateMode() {
		//		vibrateMode = prefs.getBoolean("vibrate_mode", true);
		return vibrateMode;
	}

	public void setVibrateMode(boolean vibrateMode) {
		this.vibrateMode = vibrateMode;
		//		editor.putBoolean("vibrate_mode", vibrateMode);
		//		editor.commit();
		//commit.run();
	}

	public boolean isSilentMode() {
		//		silentMode = prefs.getBoolean("silent_mode", true);
		return silentMode;
	}

	public void setSilentMode(boolean silentMode) {
		this.silentMode = silentMode;
		//		editor.putBoolean("silent_mode", silentMode);
		//		editor.commit();
		//commit.run();
	}

	public String getSleepStart() {
		//		sleepStart = prefs.getString("sleep_start", "");
		return sleepStart;
	}

	public void setSleepStart(String sleepStart) {
		this.sleepStart = sleepStart;
		//		editor.putString("sleep_start", sleepStart);
		//		editor.commit();
		//commit.run();
	}

	public String getSleepStop() {
		//		sleepStart = prefs.getString("sleep_stop", "");
		return sleepStop;
	}

	public void setSleepStop(String sleepStop) {
		this.sleepStop = sleepStop;
		//		editor.putString("sleep_stop", sleepStop);
		//		editor.commit();
		//commit.run();
	}

	public int getSleepStartHour() {
		sleepStartHour = prefs.getInt("sleep_start_hour", 0);
		return sleepStartHour;
	}

	public void setSleepStartHour(int sleepStartHour) {
		this.sleepStartHour = sleepStartHour;
		//		editor.putInt("sleep_start_hour", sleepStartHour);
		//		editor.commit();
		//commit.run();
	}

	public int getSleepStartMinute() {
		sleepStartMinute = prefs.getInt("sleep_start_minute", 0);
		return sleepStartMinute;
	}

	public void setSleepStartMinute(int sleepStartMinute) {
		this.sleepStartMinute = sleepStartMinute;
		//		editor.putInt("sleep_start_minute", sleepStartMinute);
		//		editor.commit();
		//commit.run();
	}

	public int getSleepStopHour() {
		sleepStopHour = prefs.getInt("sleep_stop_hour", 0);
		return sleepStopHour;
	}

	public void setSleepStopHour(int sleepStopHour) {
		this.sleepStopHour = sleepStopHour;
		//		editor.putInt("sleep_stop_hour", sleepStopHour);
		//		editor.commit();
		//commit.run();
	}

	public int getSleepStopMinute() {
		sleepStopMinute = prefs.getInt("sleep_stop_minute", 0);
		return sleepStopMinute;
	}

	public void setSleepStopMinute(int sleepStopMinute) {
		this.sleepStopMinute = sleepStopMinute;
		//		editor.putInt("sleep_stop_minute", sleepStopMinute);
		//		editor.commit();
		//commit.run();
	}

	public boolean isSleepMode() {
		//		sleepMode = prefs.getBoolean("sleep_check", false);
		return sleepMode;
	}

	public void setSleepMode(boolean sleepMode) {
		this.sleepMode = sleepMode;
		//		editor.putBoolean("sleep_check", sleepMode);
		//		editor.commit();
		//commit.run();
	}

	public int getType() {
		//		type = prefs.getInt("type", 1);
		if (LOG) Log.d(TAG, "type is: " + type);
		return type;
	}

	public void setType(int type) {
		this.type = type;
		//		editor.putInt("type", type);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "type set to: " + type);
	}

	public int getMsgFlashType() {
		//		msgFlashType = prefs.getInt("sms_mode_type", 1);
		if (LOG) Log.d(TAG, "sms_mode_type is: " + msgFlashType);
		return msgFlashType;
	}

	public void setMsgFlashType(int msgFlashType) {
		this.msgFlashType = msgFlashType;
		//		editor.putInt("sms_mode_type", msgFlashType);
		//		editor.commit();
		//commit.run();
		if (LOG) Log.d(TAG, "sms_mode_type set to: " + msgFlashType);
	}

	public boolean loadApp(String packageName) {
		return prefs.getBoolean(packageName, false);
	}

	public void saveApp(String packageName, boolean b) {
		editor.putBoolean(packageName, b);
		editor.commit();
	}

	public boolean isAppListCheck() {
		//		appListCheck = prefs.getBoolean("app_list_check", false);
		return appListCheck;
	}

	public void setAppListCheck(boolean appListCheck) {
		this.appListCheck = appListCheck;
		//		editor.putBoolean("app_list_check", appListCheck);
		//		editor.commit();
		//commit.run();
	}

	public boolean isBootReceiver() {
		//		bootReceiver = prefs.getBoolean("boot_receiver", false);
		return bootReceiver;
	}

	public void setBootReceiver(boolean bootReceiver) {
		this.bootReceiver = bootReceiver;
		//		editor.putBoolean("boot_receiver", bootReceiver);
		//		editor.commit();
		//commit.run();
	}

	public boolean isServiceRunning() {
		//		serviceRunning = prefs.getBoolean("service_running", false);
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
		//		editor.putBoolean("service_running", serviceRunning);
		//		editor.commit();
		//commit.run();
	}

	public boolean isInPackages(String packageName) {
		return packages.contains(packageName);
	}

	public boolean loadDonate(String type) {
		return prefs.getBoolean(type, false);
	}

	public boolean isFirstTime() {
		//		firstTime = prefs.getBoolean("first_time", true);
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
		//		editor.putBoolean("first_time", firstTime);
		//		editor.commit();
		//commit.run();
	}

	public void configureAdColony(Activity act) {
		try {
			if (CallerFlashlight.LOG) Log.d(TAG, "version Code: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
			AdColony.configure(act, "version=" + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + ",store:google", "appc0bebfc9f4a3489fb82153", "vz9bf8a5eb30ef477798b82b", /*"vz81c21390fa4e4b25aaa8ed", "vzf738e644f1394a9abcf4cf", */"vz6494ace59eb4446db403f4");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isVolumeButtonPressed() {
		return volumeButtonPressed;
	}

	public void setVolumeButtonPressed(boolean volumeButtonPressed) {
		this.volumeButtonPressed = volumeButtonPressed;
	}

	/*public boolean isScreenOffPref() {
		return screenOffPref;
	}*/

	/*public void setScreenOffPref(boolean screen_off_pref) {
		if (CallerFlashlight.LOG) Log.d(TAG, "screenOffPreff set to: " + screen_off_pref);
		this.screenOffPref = screen_off_pref;
		//		editor.putBoolean("screen_off", screen_off_pref);
		//		editor.commit();
		//commit.run();
	}*/

	public void setWindowDimensions(WindowManager windowManager) {
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		setScreenWidth(metrics.widthPixels);
		setScreenHeight(metrics.heightPixels);
	}

	public int getScreenHeight() {
		screenHeight = prefs.getInt("screen_height", 0);
		if (CallerFlashlight.LOG) Log.d(TAG, "getScreenHeight: " + screenHeight);
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
		if (CallerFlashlight.LOG) Log.d(TAG, "setScreenHeight: " + screenHeight);
		editor.putInt("screen_height", screenHeight);
		editor.commit();
	}

	public int getScreenWidth() {
		screenWidth = prefs.getInt("screen_width", 0);
		if (CallerFlashlight.LOG) Log.d(TAG, "getScreenWidth: " + screenWidth);
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
		if (CallerFlashlight.LOG) Log.d(TAG, "setScreenWidth: " + screenWidth);
		editor.putInt("screen_width", screenWidth);
		editor.commit();
	}

	public void checkForUpdates(Activity context) {
		WVersionManager versionManager = new WVersionManager(context);
		versionManager.setVersionContentUrl("https://dl.dropboxusercontent.com/u/4596106/callflash_version"); // your update content url, see the response format below
		//		versionManager.setVersionContentUrl("http://ubuntuone.com/4wlZetjy97wgKAg9PEUHSU"); // your update content url, see the response format below
		versionManager.setUpdateNowLabel(getResources().getString(R.string.update_now_label));
		versionManager.setRemindMeLaterLabel(getResources().getString(R.string.remind_me_later));
		versionManager.setIgnoreThisVersionLabel(getResources().getString(R.string.ignore));
		versionManager.setReminderTimer(10); // this mean checkVersion() will not take effect within 10 minutes
		versionManager.checkVersion();
	}

	public void promptToRate(Activity context) {
		WVersionManager versionManager = new WVersionManager(context);
		versionManager.setTitle(getResources().getString(R.string.please_rate));
		versionManager.setMessage(getResources().getString(R.string.rate_message));
		versionManager.setAskForRatePositiveLabel(getResources().getString(R.string.rate_button));
		versionManager.setAskForRateNegativeLabel(getResources().getString(R.string.remind_me_later));
		versionManager.askForRate();
	}

	public void promptAppLanding(Activity context) {
		WVersionManager versionManager = new WVersionManager(context);
		//		versionManager.setVersionContentUrl("https://dl.dropboxusercontent.com/u/4596106/callflash_version"); // your update content url, see the response format below
		//		versionManager.setVersionContentUrl("http://ubuntuone.com/4wlZetjy97wgKAg9PEUHSU"); // your update content url, see the response format below
		versionManager.setUpdateNowLabel(getResources().getString(R.string.update_now_label));
		versionManager.setRemindMeLaterLabel(getResources().getString(R.string.remind_me_later));
		versionManager.setIgnoreThisVersionLabel(getResources().getString(R.string.ignore));
		versionManager.setUpdateUrl("market://"); // this is the link will execute when update now clicked. default will go to google play based on your package name.
		versionManager.setReminderTimer(10); // this mean checkVersion() will not take effect within 10 minutes
		versionManager.checkVersion();
	}

	public boolean isScreenLockedPref() {
		return screenLockedPref;
	}

	public void setScreenLockedPref(boolean screenLockedPref) {
		this.screenLockedPref = screenLockedPref;
	}

	public boolean isLowBat() {
		lowBat = prefs.getBoolean("low_bat", false);
		return lowBat;
	}

	public void setLowBat(boolean lowBat) {
		this.lowBat = lowBat;
		editor.putBoolean("low_bat", lowBat);
		editor.commit();
	}

	public boolean isLowBatPref() {
		return lowBatPref;
	}

	public void setLowBatPref(boolean lowBatPref) {
		this.lowBatPref = lowBatPref;
	}

	public boolean isVolumeButtonPref() {
		return volumeButtonPref;
	}

	public void setVolumeButtonPref(boolean volumeButtonPref) {
		this.volumeButtonPref = volumeButtonPref;
	}

	//	public void registerShared(Donate donateActivity) {
	//		prefs.registerOnSharedPreferenceChangeListener(donateActivity);
	//	}
	//
	//	public void unregisterShared(Donate donateActivity) {
	//		prefs.unregisterOnSharedPreferenceChangeListener(donateActivity);
	//	}
}
