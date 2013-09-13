package com.spirosbond.callerflashlight;

import android.app.Service;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by spiros on 8/7/13.
 */
public class Flash {

	private final static String TAG = Flash.class.getSimpleName();
	private static Camera cam;
	private static int running;
	//	private static SurfaceView preview;
	private static CallerFlashlight cf;
	private static SurfaceHolder mHolder;
	private static Camera.Parameters pon, poff;
	private static CameraSurface preview;
	private static LinearLayout orientationChanger;
	private static WindowManager wm;
	private static boolean gotCam = false;


	public Flash(CallerFlashlight cf) {
		this.cf = cf;
		if (!gotCam) {
			try {
				cam = Camera.open();
				Log.d(TAG, "Constructor: Camera opened successfully");
				gotCam = true;

			} catch (NullPointerException e) {
				e.printStackTrace();
				gotCam = false;
				return;
			} catch (RuntimeException e) {
				Log.d(TAG, "Constructor: Unable to open the camera. Is already Opened??");
				gotCam = false;
				return;

			}
		}

		loadParameters();
	}

	public static int getRunning() {
		return running;
	}

	public static void incRunning() {

		Flash.running = Flash.running + 1;
		Log.d(TAG, "running: " + running);
	}

	public static void decRunning() {

		Flash.running = Flash.running - 1;
		Log.d(TAG, "running: " + running);
	}

	public static void releaseCam() {
		Log.d(TAG, "releaseCam");
		if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE) {
			orientationChanger.setVisibility(View.GONE);
		}
		cam.stopPreview();
		cam.release();
		gotCam = false;
	}

	private void loadParameters() {
		if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE) {

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, 1);
			layoutParams.setMargins(1000, 1000, 1000, 1000);

//		preview.setLayoutParams(layoutParams);
//			FrameLayout frame = new FrameLayout(cf.getApplicationContext());

			orientationChanger = new LinearLayout(cf.getApplicationContext());

			WindowManager.LayoutParams orientationLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0,
							PixelFormat.RGBA_8888);

			wm = (WindowManager) cf.getApplicationContext().getSystemService(Service.WINDOW_SERVICE);

			FrameLayout frame = new FrameLayout(cf.getApplicationContext());
			frame.setLayoutParams(layoutParams);
			preview = new CameraSurface(cf.getApplicationContext(), cam);
			preview.setLayoutParams(layoutParams);

			frame.addView(preview);

			orientationChanger.addView(frame);
			orientationChanger.setVisibility(View.VISIBLE);

			wm.addView(orientationChanger, orientationLayout);

		}
		if (cf.getType() == CallerFlashlight.TYPE_NORMAL || cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE) {
			pon = cam.getParameters();
			poff = cam.getParameters();
			pon.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			poff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		} else if (cf.getType() == CallerFlashlight.TYPE_ALTERNATIVE_2) {
			pon = cam.getParameters();
			poff = cam.getParameters();
			pon.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			poff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}
	}

	public void enableFlash(long onMillis, long offMillis) {
		Log.d(TAG, "enableFlash. ON: " + onMillis + " OFF: " + offMillis + " gotCam= " + gotCam);

		if (!gotCam) {
			try {
				cam = Camera.open();
				Log.d(TAG, "enableFlash: Camera opened successfully");
				gotCam = true;

			} catch (NullPointerException e) {
				e.printStackTrace();
				gotCam = false;
				try {
					Thread.sleep(onMillis + offMillis);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return;
			} catch (RuntimeException e) {
				Log.d(TAG, "enableFlash: Unable to open the camera. Is already Opened??");
				gotCam = false;
				try {
					Thread.sleep(onMillis + offMillis);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return;

			}
			loadParameters();
		}

		cam.setParameters(pon);

		try {
			Thread.sleep(onMillis);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		disableFlash();
		try {
			Thread.sleep(offMillis);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

	public void disableFlash() {
		Log.d(TAG, "disableFlash");

		cam.setParameters(poff);

	}


}
