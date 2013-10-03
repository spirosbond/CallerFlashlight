package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainPanel extends Activity implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

	private static final String TAG = MainPanel.class.getSimpleName();
	private CallerFlashlight callerFlashlight;
	private MainPanel mainPanel;
	private SeekBar callFlashOnBar, callFlashOffBar, msgFlashOnBar, msgFlashOffBar, msgFlashDurBar;
	private EditText callFlashOnBarValue, callFlashOffBarValue, msgFlashOnBarValue, msgFlashOffBarValue, msgFlashDurBarValue;
	private ToggleButton callFlashButton;
	private ToggleButton msgFlashButton;
	private ToggleButton callFlashTestButton;
	private ToggleButton msgFlashTestButton;
	private SeekBarChange seekBarChange = new SeekBarChange();
//	private StartAppAd startAppAd = null;

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResumed");
		callFlashButton = (ToggleButton) findViewById(R.id.callFlashToggle);
		callFlashButton.setChecked(callerFlashlight.isCallFlash());
		callFlashTestButton = (ToggleButton) findViewById(R.id.callFlashTestToggle);
		callFlashTestButton.setChecked(callerFlashlight.isCallFlashTest());
		msgFlashButton = (ToggleButton) findViewById(R.id.msgFlashToggle);
		msgFlashButton.setChecked(callerFlashlight.isMsgFlash());
		msgFlashTestButton = (ToggleButton) findViewById(R.id.msgFlashTestToggle);
		msgFlashTestButton.setChecked(callerFlashlight.isMsgFlashTest());
//		if (startAppAd == null) {
//			startAppAd = new StartAppAd(this);
//			startAppAd.load();
//		}

//		callFlashCreate();
//		msgFlashCreate();
	}

	@Override
	public void onBackPressed() {
//
//		}
//		AppFlood.initialize(this, "Thib0u8GfGgfXsLX", "6GX8sMOv1791L521de8ea", AppFlood.AD_ALL);

//		AppFlood.showFullScreen(this);

		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPaused");
		super.onPause();
		callerFlashlight.setCallFlashTest(false);
		callerFlashlight.setMsgFlashTest(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreated");

		setContentView(R.layout.activity_main);
		this.callerFlashlight = (CallerFlashlight) this.getApplication();
		this.mainPanel = this;
//		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Button callPrefs = (Button) findViewById(R.id.CallPref);
		callPrefs.setOnClickListener(this);
		Button msgPrefs = (Button) findViewById(R.id.MsgPref);
		msgPrefs.setOnClickListener(this);

		callFlashCreate();
		msgFlashCreate();


//		callerFlashlight.setPreview((SurfaceView) findViewById(R.id.surfaceView));
//		startActivityForResult( new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
//		startService(new Intent(this, NotificationService.class));


	}

	private void msgFlashCreate() {
		msgFlashButton = (ToggleButton) findViewById(R.id.msgFlashToggle);
		msgFlashButton.setChecked(callerFlashlight.isMsgFlash());
		msgFlashButton.setOnClickListener(this);

		msgFlashTestButton = (ToggleButton) findViewById(R.id.msgFlashTestToggle);
		msgFlashTestButton.setChecked(callerFlashlight.isMsgFlashTest());
		msgFlashTestButton.setOnClickListener(this);

		msgFlashOnBar = (SeekBar) findViewById(R.id.flashOnDurationBarMsg);
		msgFlashOnBar.setProgress(callerFlashlight.getMsgFlashOnDuration());
		msgFlashOnBar.setMax(1000);

		msgFlashOffBar = (SeekBar) findViewById(R.id.flashOffDurationBarMsg);
		msgFlashOffBar.setProgress(callerFlashlight.getMsgFlashOffDuration());
		msgFlashOffBar.setMax(1000);

//		msgFlashDurBar = (SeekBar) findViewById(R.id.flashDurationBarMsg);
//		msgFlashDurBar.setProgress(callerFlashlight.getMsgFlashDuration());
//		msgFlashDurBar.setMax(10);

		msgFlashOnBarValue = (EditText) findViewById(R.id.flashOnDurationValueMsg);
		msgFlashOnBarValue.setText(String.valueOf(msgFlashOnBar.getProgress()));
		msgFlashOnBarValue.addTextChangedListener(this);
		msgFlashOnBarValue.setOnFocusChangeListener(this);

		msgFlashOffBarValue = (EditText) findViewById(R.id.flashOffDurationValueMsg);
		msgFlashOffBarValue.setText(String.valueOf(msgFlashOffBar.getProgress()));
		msgFlashOffBarValue.addTextChangedListener(this);
		msgFlashOffBarValue.setOnFocusChangeListener(this);


//		msgFlashDurBarValue = (EditText) findViewById(R.id.flashDurationValueMsg);
//		msgFlashDurBarValue.setText(String.valueOf(msgFlashDurBar.getProgress()));
//		msgFlashDurBarValue.addTextChangedListener(this);

		msgFlashOnBar.setOnSeekBarChangeListener(seekBarChange);

		msgFlashOffBar.setOnSeekBarChangeListener(seekBarChange);

//		msgFlashDurBar.setOnSeekBarChangeListener(seekBarChange);
	}

	private void callFlashCreate() {
		callFlashButton = (ToggleButton) findViewById(R.id.callFlashToggle);
		callFlashButton.setChecked(callerFlashlight.isCallFlash());
		callFlashButton.setOnClickListener(this);

		callFlashTestButton = (ToggleButton) findViewById(R.id.callFlashTestToggle);
		callFlashTestButton.setChecked(callerFlashlight.isCallFlashTest());
		callFlashTestButton.setOnClickListener(this);

		callFlashOnBar = (SeekBar) findViewById(R.id.flashOnDurationBar);
		callFlashOnBar.setProgress(callerFlashlight.getCallFlashOnDuration());
		callFlashOnBar.setMax(1000);

		callFlashOffBar = (SeekBar) findViewById(R.id.flashOffDurationBar);
		callFlashOffBar.setProgress(callerFlashlight.getCallFlashOffDuration());
		callFlashOffBar.setMax(1000);

		callFlashOnBarValue = (EditText) findViewById(R.id.flashOnDurationValue);
		callFlashOnBarValue.setText(String.valueOf(callFlashOnBar.getProgress()));
		callFlashOnBarValue.addTextChangedListener(this);
		callFlashOnBarValue.setOnFocusChangeListener(this);

		callFlashOffBarValue = (EditText) findViewById(R.id.flashOffDurationValue);
		callFlashOffBarValue.setText(String.valueOf(callFlashOffBar.getProgress()));
		callFlashOffBarValue.addTextChangedListener(this);
		callFlashOffBarValue.setOnFocusChangeListener(this);

		callFlashOnBar.setOnSeekBarChangeListener(seekBarChange);

		callFlashOffBar.setOnSeekBarChangeListener(seekBarChange);
	}

	@Override
	public void onClick(View view) {

		ToggleButton tb = null;
//		try {
//			tb = (ToggleButton) view;
//		} catch (ClassCastException e) {
//			tb = (Button) view;
//		}
//		Log.d(TAG, "onClicked: " + tb.isChecked());

		switch (view.getId()) {
			case R.id.callFlashToggle:
				if (!(callerFlashlight.isNormalMode() || callerFlashlight.isSilentMode() || callerFlashlight.isVibrateMode())) {
					callFlashButton.setChecked(false);
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.enable_toast), Toast.LENGTH_SHORT).show();
					break;
				}
				callerFlashlight.setCallFlash(callFlashButton.isChecked());

				break;
			case R.id.msgFlashToggle:
				if (!(callerFlashlight.isNormalMode() || callerFlashlight.isSilentMode() || callerFlashlight.isVibrateMode())) {
					msgFlashButton.setChecked(false);
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.enable_toast), Toast.LENGTH_SHORT).show();
					break;
				}
				callerFlashlight.setMsgFlash(msgFlashButton.isChecked());
				break;
			case R.id.callFlashTestToggle:
				callerFlashlight.setCallFlashTest(callFlashTestButton.isChecked());
				if (callFlashTestButton.isChecked()) {
					msgFlashTestButton.setChecked(false);
					callerFlashlight.setMsgFlashTest(false);
					new ManageFlash(callFlashTestButton).execute();
//					startActivity(new Intent(this, CameraSurface.class));
				}//else if (!msgFlashTestButton.isChecked()){
				//	Flash.releaseCam();
				//}
				break;
			case R.id.msgFlashTestToggle:
				callerFlashlight.setMsgFlashTest(msgFlashTestButton.isChecked());
				if (msgFlashTestButton.isChecked()) {
					callFlashTestButton.setChecked(false);
					callerFlashlight.setCallFlashTest(false);

					new ManageFlash(msgFlashTestButton).execute();
//					startActivity(new Intent(this, CameraSurface.class));
				}//else if(!callFlashTestButton.isChecked()){
				//	Flash.releaseCam();
				//}
				break;
			case R.id.CallPref:
				startActivity(new Intent(this, CallPrefs.class));
				break;
			case R.id.MsgPref:
				startActivity(new Intent(this, MsgPrefs.class));
				break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_panel, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//return super.onOptionsItemSelected(item);
		switch (item.getItemId()) {     // figure out what was pressed
			case R.id.howTo:
				startActivity(new Intent(this, HowTo.class)); //
				break;
			case R.id.about:
				startActivity(new Intent(this, About.class));   // start the About Dialog
				break;
			case R.id.donate:
				startActivity(new Intent(this, Donate.class));   // start the PrefsActivity
				break;
			case R.id.preferences:
				startActivity(new Intent(this, PrefsActivity.class));
				break;
		}

		return true;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//		Log.d(TAG, "beforeTextChanged with char: " + charSequence);
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//		Log.d(TAG, "onTextChanged with char: " + charSequence);
//		if (charSequence.length() == 0) {
////			Log.d(TAG, "Empty editText. Set it to 0");
//			((EditText) getCurrentFocus()).setText(String.valueOf(0));
//		}

	}

	@Override
	public void afterTextChanged(Editable editable) {
		EditText editText;
		int value;
		int editId;

		try {
			editText = (EditText) getCurrentFocus();
			if (editable.length() == 0) {
				value = 0;
//				editText.setText(String.valueOf(value));
			} else {
				value = Integer.valueOf(String.valueOf(editable));
			}
			editId = editText.getId();
//			Log.d(TAG, "Value: " + value);
		} catch (NumberFormatException e) {
			return;
		} catch (NullPointerException e) {
			return;
		}


		if (value > 1000) {
			value = 1000;
			editText.setText(String.valueOf(value));
		}

//		Log.d(TAG, "EditID: " + editId);

		switch (editId) {
			case -1:
				break;
			case R.id.flashOnDurationValue:
//				if(value<50){
//					value=50;
//					editText.setText(String.valueOf(value));
//				}
				callerFlashlight.setCallFlashOnDuration(value);
				callFlashOnBar.setOnSeekBarChangeListener(null);
				callFlashOnBar.setProgress(value);
				callFlashOnBar.setOnSeekBarChangeListener(seekBarChange);

				break;
			case R.id.flashOffDurationValue:
//				if(value<50){
//					value=50;
//					editText.setText(String.valueOf(value));
//				}
				callerFlashlight.setCallFlashOffDuration(value);
				callFlashOffBar.setOnSeekBarChangeListener(null);
				callFlashOffBar.setProgress(value);
				callFlashOffBar.setOnSeekBarChangeListener(seekBarChange);

				break;
			case R.id.flashOnDurationValueMsg:
//				if(value<50){
//					value=50;
//					editText.setText(String.valueOf(value));
//				}
				callerFlashlight.setMsgFlashOnDuration(value);
				msgFlashOnBar.setOnSeekBarChangeListener(null);
				msgFlashOnBar.setProgress(value);
				msgFlashOnBar.setOnSeekBarChangeListener(seekBarChange);

				break;
			case R.id.flashOffDurationValueMsg:
//				if(value<50){
//					value=50;
//					editText.setText(String.valueOf(value));
//				}
				callerFlashlight.setMsgFlashOffDuration(value);
				msgFlashOffBar.setOnSeekBarChangeListener(null);
				msgFlashOffBar.setProgress(value);
				msgFlashOffBar.setOnSeekBarChangeListener(seekBarChange);

				break;
//			case R.id.flashDurationValueMsg:
//				if (value > 10) {
//					value = 10;
//					editText.setText(String.valueOf(value));
//				}
////				else if(value<1){
////					value=1;
////					editText.setText(String.valueOf(value));
////				}
//				callerFlashlight.setMsgFlashDuration(value);
//				msgFlashDurBar.setOnSeekBarChangeListener(null);
//				msgFlashDurBar.setProgress(value);
//				msgFlashDurBar.setOnSeekBarChangeListener(seekBarChange);
//
//				break;
		}


	}

	@Override
	public void onFocusChange(View view, boolean b) {
		EditText temp = (EditText) view;
		switch (view.getId()) {
			case R.id.flashOnDurationValue:
				if (!b && callerFlashlight.getCallFlashOnDuration() == 0) {
					temp.setText("50");
					callerFlashlight.setCallFlashOnDuration(50);
					break;
				}

			case R.id.flashOffDurationValue:
				if (!b && callerFlashlight.getCallFlashOffDuration() == 0) {
					temp.setText("50");
					callerFlashlight.setCallFlashOffDuration(50);
					break;
				}

			case R.id.flashOnDurationValueMsg:
				if (!b && callerFlashlight.getMsgFlashOnDuration() == 0) {
					temp.setText("50");
					callerFlashlight.setMsgFlashOnDuration(50);
					break;
				}

			case R.id.flashOffDurationValueMsg:
				if (!b && callerFlashlight.getMsgFlashOffDuration() == 0) {
					temp.setText("50");
					callerFlashlight.setMsgFlashOffDuration(50);
					break;
				}
		}
	}

	private class SeekBarChange implements SeekBar.OnSeekBarChangeListener {

		public SeekBarChange() {
			super();
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
			switch (seekBar.getId()) {
				case R.id.flashOnDurationBar:
					progress = progress / 10;
					progress = progress * 10;
					if (progress < 50) progress = 50;

					callFlashOnBarValue.removeTextChangedListener(mainPanel);
					callFlashOnBarValue.setText(String.valueOf(progress));
					callFlashOnBarValue.addTextChangedListener(mainPanel);
					callerFlashlight.setCallFlashOnDuration(progress);
					break;
				case R.id.flashOffDurationBar:
					progress = progress / 10;
					progress = progress * 10;
					if (progress < 50) progress = 50;

					callFlashOffBarValue.removeTextChangedListener(mainPanel);
					callFlashOffBarValue.setText(String.valueOf(progress));
					callFlashOffBarValue.addTextChangedListener(mainPanel);
					callerFlashlight.setCallFlashOffDuration(progress);
					break;
				case R.id.flashOnDurationBarMsg:
					progress = progress / 10;
					progress = progress * 10;
					if (progress < 50) progress = 50;

					msgFlashOnBarValue.removeTextChangedListener(mainPanel);
					msgFlashOnBarValue.setText(String.valueOf(progress));
					msgFlashOnBarValue.addTextChangedListener(mainPanel);
					callerFlashlight.setMsgFlashOnDuration(progress);
					break;
				case R.id.flashOffDurationBarMsg:
					progress = progress / 10;
					progress = progress * 10;
					if (progress < 50) progress = 50;
					msgFlashOffBarValue.removeTextChangedListener(mainPanel);
					msgFlashOffBarValue.setText(String.valueOf(progress));
					msgFlashOffBarValue.addTextChangedListener(mainPanel);
					callerFlashlight.setMsgFlashOffDuration(progress);
					break;
//				case R.id.flashDurationBarMsg:
//					progress = progress / 1;
//					progress = progress * 1;
//					if (progress < 1) progress = 1;
//					msgFlashDurBarValue.removeTextChangedListener(mainPanel);
//					msgFlashDurBarValue.setText(String.valueOf(progress));
//					msgFlashDurBarValue.addTextChangedListener(mainPanel);
//					callerFlashlight.setMsgFlashDuration(progress);
//					break;


			}


		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private ToggleButton button;
		private Flash flash;

		public ManageFlash(ToggleButton button) {
			this.button = button;
			flash = new Flash(callerFlashlight);
			Flash.incRunning();
		}

		@Override
		protected String doInBackground(Integer... integers) {
			Log.d(TAG, "doInBackgroung Started");


			switch (button.getId()) {
				case R.id.callFlashTestToggle:
					while (callerFlashlight.isCallFlashTest()) {
						flash.enableFlash(callerFlashlight.getCallFlashOnDuration(), callerFlashlight.getCallFlashOffDuration());
					}
					break;
				case R.id.msgFlashTestToggle:
					while (callerFlashlight.isMsgFlashTest()) {
						flash.enableFlash(callerFlashlight.getMsgFlashOnDuration(), callerFlashlight.getMsgFlashOffDuration());
					}
					break;
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
