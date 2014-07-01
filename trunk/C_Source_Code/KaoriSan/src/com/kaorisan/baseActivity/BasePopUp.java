/**
 * 
 */
package com.kaorisan.baseActivity;

import java.util.Timer;
import java.util.TimerTask;

import com.kaorisan.R;
import com.kaorisan.common.DebugLog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

// NIW_SCREEN_121 BASED

/**
 * @author Macbook
 * 
 */
public class BasePopUp extends Dialog implements
		android.view.View.OnClickListener {
	
	protected String message = null;
	protected Boolean isShowCancelButton = true;
	protected Activity behind;
	protected Button closeButton;
	protected int livingDuration = 3000;
	protected Timer timer;
	protected Boolean tapToClose = false;

	public BasePopUp(Activity behindActivity) {
		super(behindActivity);
		this.behind = behindActivity;
	}

	private void overSetTimer() {
		// NIW_FUNC_121_1
		if (livingDuration > 0) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					dismissDialogLocal();
				}
			}, livingDuration);
		}
	}

	protected void dismissDialogLocal() {
		this.dismiss();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	protected void setLivingDuration(int duration) {
		// override default time
		livingDuration = duration;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base_popup);
		
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		this.overSetTimer();
		
		// NIW_FUNC_121_1
		if (message != null) {
			TextView textView = (TextView)findViewById(R.id.base_popup_message);
			textView.setText(Html.fromHtml(this.message + "<br/>"));
		}
		if (isShowCancelButton == false) {
			closeButton = (Button) findViewById(R.id.btn_x);
			closeButton.setVisibility(View.INVISIBLE);
		} else {
			// NIW_FUNC_121_2
			closeButton = (Button) findViewById(R.id.btn_x);
			closeButton.setOnClickListener(this);
		}
		
		closeButton = (Button) findViewById(R.id.btn_close);
		closeButton.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		if (timer != null) {
			timer.cancel();
		}
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		DebugLog.logd("Kaorisan " + "view clicked");
		this.onStop();
		switch (v.getId()) {
		// NIW_FUNC_121_2
		case R.id.btn_x:
			DebugLog.logd("Kaorisan" + "button X clicked");
			dismissDialogLocal();
			break;
		case R.id.btn_close:
			dismissDialogLocal();
			break;
		default:
			if (tapToClose == true) {
				this.dismissDialogLocal();
			}
			break;
		}
	}

}
