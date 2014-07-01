/**
 * 
 */
package com.kaorisan.supportActivity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kaorisan.R;
import com.kaorisan.common.DebugLog;

public class LogoutConfirm extends Dialog implements android.view.View.OnClickListener{

	protected Activity behind;
	protected Button yesButton;
	protected Button noButton;
	protected Boolean tapToClose = false;
	OnLogoutYesClicked callback = null;
	
	public interface OnLogoutYesClicked {
		public void onLogOutClicked(Activity behind);
	}
	public LogoutConfirm(Activity behindActivity, OnLogoutYesClicked logoutClicked) {
		super(behindActivity);
		this.behind = behindActivity;
		callback = logoutClicked;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pop_up_logout);

		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		yesButton = (Button) findViewById(R.id.btn_logout_yes);
		noButton = (Button) findViewById(R.id.btn_logout_no);
		yesButton.setOnClickListener(this);
		noButton.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	@Override
	public void onClick(View v) {
		DebugLog.logd(" logout confirmation view clicked");
		this.onStop();
		switch (v.getId()) {
		// NIW_FUNC_121_2
		case R.id.btn_logout_no:
			this.dismiss();
			break;
			
		case R.id.btn_logout_yes:
			this.dismiss();
			if (callback != null) {
				callback.onLogOutClicked(behind);
			}
			
			break;
		
		}
		
	} 

	
}
