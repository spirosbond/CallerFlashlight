package com.spirosbond.callerflashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by spiros on 10/12/13.
 */
public class FirstTimeUtilisation extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

	private static final String TAG = FirstTimeUtilisation.class.getSimpleName();
	CallerFlashlight callerFlashlight;
	Button testButton;
	Button contButton;
	Spinner moduleList;
	ImageView logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreated");
		setContentView(R.layout.first_time);
		callerFlashlight = (CallerFlashlight) getApplication();
		testButton = (Button) findViewById(R.id.firstFlashTest);
		testButton.setOnClickListener(this);
		contButton = (Button) findViewById(R.id.firstcontinue);
		contButton.setOnClickListener(this);
		moduleList = (Spinner) findViewById(R.id.module_list);
		moduleList.setOnItemSelectedListener(this);
		logo = (ImageView) findViewById(R.id.logo);
		logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.round));

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResumed");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.firstFlashTest:
				//					callerFlashlight.setMsgFlashTest(false);
				new ManageFlash().execute();
				//					startActivity(new Intent(this, CameraSurface.class));

				break;
			case R.id.firstcontinue:
				setResult(RESULT_OK, new Intent());
				finish();
				break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		Log.d(TAG, "onItemClick " + i + 1);
		callerFlashlight.setType(i + 1);

	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private int flashes = 3;
		private Flash flash;

		public ManageFlash() {

			flash = new Flash(callerFlashlight);
			Flash.incRunning();
		}

		@Override
		protected String doInBackground(Integer... integers) {
			Log.d(TAG, "doInBackgroung Started");

			while (flashes > 0) {
				flash.enableFlash(callerFlashlight.getCallFlashOnDuration(), callerFlashlight.getCallFlashOffDuration());
				flashes -= 1;
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
