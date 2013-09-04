package com.spirosbond.callerflashlight;


import android.app.Dialog;
import android.content.Context;

/**
 * Created by spiros on 8/5/13.
 */
public class AboutDialog extends Dialog {

	public AboutDialog(Context context) {
		super(context);
		setContentView(R.layout.activity_about);
	}
//
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		// Get the layout inflater
//		LayoutInflater inflater = getActivity().getLayoutInflater();
//
//		// Inflate and set the layout for the dialog
//		// Pass null as the parent view because its going in the dialog layout
//		builder.setView(inflater.inflate(R.layout.activity_about, null));
//
//		return builder.create();
//	}

}
