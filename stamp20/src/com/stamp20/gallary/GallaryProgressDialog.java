package com.stamp20.gallary;

import com.stamp20.app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GallaryProgressDialog extends ProgressDialog {
	
	public GallaryProgressDialog(Context context){
		super(context);
	}
	
	public GallaryProgressDialog(Context context, int theme){
		super(context, theme);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallary_progress_dialog);
	}
	
	public static GallaryProgressDialog show(Context context, String message, boolean indeterminate, boolean cancelable, DialogInterface.OnCancelListener cancelListener){
		GallaryProgressDialog dialog = new GallaryProgressDialog(context, R.style.CustomProgressDialog);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		if(message != null && !message.isEmpty() ){
			TextView tv = (TextView)(dialog.findViewById(R.id.progresstext));
			tv.setText(message);
			tv.setVisibility(View.VISIBLE);
		}
		if(indeterminate)
			dialog.show();
		return dialog;
	}
}
