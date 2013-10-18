package com.spirosbond.callerflashlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by spiros on 8/19/13.
 */
public class TimePreference extends DialogPreference {

	private static final String TAG = TimePreference.class.getSimpleName();
	private int lastHour = 0;
	private int lastMinute = 0;
	private TimePicker picker = null;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private CallerFlashlight callerFlashlight;

	public TimePreference(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		callerFlashlight = (CallerFlashlight) ctxt.getApplicationContext();

		setPositiveButtonText(callerFlashlight.getResources().getString(R.string.set));
		setNegativeButtonText(callerFlashlight.getResources().getString(R.string.cancel));
		if (CallerFlashlight.LOG) Log.d(TAG, "My key: " + getKey());
		prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		editor = prefs.edit();
		setSummary();

	}

	public static int getHour(String time) {
		String[] pieces = time.split(":");

		return (Integer.parseInt(pieces[0]));
	}

	public static int getMinute(String time) {
		String[] pieces = time.split(":");

		return (Integer.parseInt(pieces[1]));
	}

	@Override
	protected View onCreateDialogView() {
		picker = new TimePicker(getContext());

		return (picker);
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);

		picker.setCurrentHour(lastHour);
		picker.setCurrentMinute(lastMinute);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			lastHour = picker.getCurrentHour();
			lastMinute = picker.getCurrentMinute();

			if (getKey().equals("sleep_start")) {
				editor.putInt("sleep_start_hour", lastHour);
				editor.putInt("sleep_start_minute", lastMinute);
			} else if (getKey().equals("sleep_stop")) {
				editor.putInt("sleep_stop_hour", lastHour);
				editor.putInt("sleep_stop_minute", lastMinute);
			}

			String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);
			time = convertTime(time);

			if (getKey().equals("sleep_start")) editor.putString("sleep_start", time);
			else if (getKey().equals("sleep_stop")) editor.putString("sleep_stop", time);
			editor.commit();

			if (CallerFlashlight.LOG) Log.d(TAG, time);
			setSummary();

			if (callChangeListener(time)) {
				persistString(time);
			}
		}
	}

	private String convertTime(String time) {
		String ret_time = "";
		String[] parts = time.split(":");
		if (parts[0].length() < 2) ret_time = ret_time.concat("0").concat(parts[0]);
		else ret_time = parts[0];
		ret_time = ret_time.concat(":");
		if (parts[1].length() < 2) ret_time = ret_time.concat("0").concat(parts[1]);
		else ret_time = ret_time.concat(parts[1]);
		return ret_time;
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return (a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		String time = null;

		if (restoreValue) {
			if (defaultValue == null) {
				time = getPersistedString("00:00");
			} else {
				time = getPersistedString(defaultValue.toString());
			}
		} else {
			time = defaultValue.toString();
		}

		lastHour = getHour(time);
		lastMinute = getMinute(time);
	}

	public void setSummary() {
		if (this.getKey().equals("sleep_start")) setSummary(prefs.getString("sleep_start", ""));
		else if (this.getKey().equals("sleep_stop")) setSummary(prefs.getString("sleep_stop", ""));
	}
}