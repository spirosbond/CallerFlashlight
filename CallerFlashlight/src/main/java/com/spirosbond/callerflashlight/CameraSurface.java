package com.spirosbond.callerflashlight;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by spiros on 8/7/13.
 */

/**
 * A basic Camera preview class
 */
public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = CameraSurface.class.getSimpleName();
	private static SurfaceHolder mHolder;
	private static Camera mCamera;

	public CameraSurface(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
		try {
			Log.d(TAG, "surfaceCreated");
			mCamera.setPreviewDisplay(holder);

			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed");
		// empty. Take care of releasing the Camera preview in your activity.

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		Log.d(TAG, "surfaceChanged");

		if (mHolder.getSurface() == null) {
			// preview surface does not exist

		}

		//		// stop preview before making changes
		//		try {
		//			mCamera.stopPreview();
		//		} catch (Exception e){
		//			// ignore: tried to stop a non-existent preview
		//		}
		//
		//		// set preview size and make any resize, rotate or
		//		// reformatting changes here
		//
		//		// start preview with new settings
		//		try {
		//			mCamera.setPreviewDisplay(mHolder);
		//			mCamera.startPreview();
		//
		//		} catch (Exception e){
		//			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		//		}
	}
}