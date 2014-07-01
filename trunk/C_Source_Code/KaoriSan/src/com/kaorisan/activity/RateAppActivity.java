package com.kaorisan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.kaorisan.R;
import com.kaorisan.common.DebugLog;

public class RateAppActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DebugLog.logd("Rate App activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_app);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rate_app, menu);
		return true;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
			case R.id.btnLike:
				DebugLog.logd("Like click");
				break;
			case R.id.btnDislike:
				DebugLog.logd("Dislike click");
				break;
				
			default:
				finish();
				 break;
			}
	
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
