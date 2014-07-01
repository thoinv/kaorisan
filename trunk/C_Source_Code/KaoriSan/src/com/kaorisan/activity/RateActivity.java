package com.kaorisan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.kaorisan.R;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.manager.TaskManager;

public class RateActivity extends Activity {

	private boolean isClickUp = false;

	private boolean isClickDown = false;

	private String strUp = "up";

	private String strDown = "down";

	TextView btnLike;
	TextView btnDislike;

	ProgressDialog showProcess = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate);

		btnLike = (TextView) findViewById(R.id.btnLike);
		btnDislike = (TextView) findViewById(R.id.btnDislike);
		if(CacheData.getInstant().getCurrentTask() != null){
			if(!CacheData.getInstant().getCurrentTask().isRated()){
				btnLike.setBackgroundResource(R.drawable.like_button_gray);
				btnDislike.setBackgroundResource(R.drawable.dislike_button_gray);
			}
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rate, menu);
		return true;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnLike:
			if(CacheData.getInstant().getCurrentTask().isRated() != false){
				if (!isClickUp) {
					rateTask(strUp);
				}
			}else{
				Utils.showToast(RateActivity.this, getResources().getString(R.string.haved_rate_msg));
			}
			// btnLike.setBackgroundResource(R.drawable.like_button_gray);
			// btnDislike.setBackgroundResource(R.drawable.dislike_button);
			break;
		case R.id.btnDislike:
			if(CacheData.getInstant().getCurrentTask().isRated() == false){
				if (!isClickDown) {
					rateTask(strDown);
				}
			}else{
				Utils.showToast(RateActivity.this, getResources().getString(R.string.haved_rate_msg));
			}
			// btnDislike.setBackgroundResource(R.drawable.dislike_button_gray);
			// btnLike.setBackgroundResource(R.drawable.like_button);
			break;
		}
	}

	private void rateTask(final String rate) {
		TaskManager manager = new TaskManager();
		showProcess = new ProgressDialog(this);
		showProcess.setCancelable(false);
		showProcess.setTitle(getResources().getString(R.string.please_waiting_msg));
		showProcess.show();
		
		manager.rateTask(CacheData.getInstant().getTokenKaorisan(),	String.valueOf(CacheData.getInstant().getCurrentTask().getId()),
				rate, new TaskManager.OnGetRateTaskResult() {

					@Override
					public void onGetRateTaskMethod(boolean isSuccess, String message) {
						if (isSuccess) {
							if (rate.equals(strUp)) {
								isClickUp = true;
								isClickDown = false;

							} else if (rate.equals(strDown)) {
								isClickDown = true;
								isClickUp = false;	
							}
							CacheData.getInstant().getCurrentTask().setRated(true);
							btnLike.setBackgroundResource(R.drawable.like_button_gray);
							btnDislike.setBackgroundResource(R.drawable.dislike_button_gray);

							btnLike.setClickable(false);
							btnDislike.setClickable(false);

							Utils.showToast(RateActivity.this, getResources().getString(R.string.rate_successfully));
							if(CacheData.getInstant().getCurrentTask() != null){
								CacheData.getInstant().getCurrentTask().setRated(false);
							}
							finish();
						} else {
							Utils.showToast(RateActivity.this, getResources().getString(R.string.rate_failed));
						}

						showProcess.dismiss();
					}
				});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
