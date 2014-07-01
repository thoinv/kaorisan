// to create new progress dialog

package com.niw.adv.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadDialog {
	private ProgressDialog dialog;
	
	public LoadDialog(Context mContext) {
		dialog=new ProgressDialog(mContext);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	}

	public ProgressDialog getDialog() {
		return dialog;
	}

	public void setDialog(ProgressDialog dialog) {
		this.dialog = dialog;
	}
	
}
