package com.kaorisan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;

import com.kaorisan.R;
import com.kaorisan.common.DebugLog;

public class TermsAndConditionsActivity extends Activity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_and_conditions);

		webView = (WebView) findViewById(R.id.con_term);
		webView.loadUrl("file:///android_asset/Kaorisan_TOSPrivacyPlicy.html");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.terms_and_conditions, menu);
		return true;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnSettings:
			DebugLog.logd("Button Settings click");
			onBackPressed();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
