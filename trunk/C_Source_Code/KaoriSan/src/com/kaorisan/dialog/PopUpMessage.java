package com.kaorisan.dialog;

import com.kaorisan.baseActivity.BasePopUp;

import android.app.Activity;
import android.os.Bundle;

// NIW_SCREEN_121
public class PopUpMessage extends BasePopUp {
	public PopUpMessage(Activity behindActivity, 
			String htmlMessage, 
			Boolean showCancelButton,
			int livingTimeMilisecond,
			Boolean tapToClose) {
		super(behindActivity);
		this.message = htmlMessage;
		isShowCancelButton = showCancelButton;
		livingDuration = livingTimeMilisecond;
		this.tapToClose = tapToClose;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}