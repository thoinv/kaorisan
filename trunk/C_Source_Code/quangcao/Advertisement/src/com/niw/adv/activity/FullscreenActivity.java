package com.niw.adv.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adv.activity.R;
import com.niw.adv.helper.NetworkManager;

@SuppressLint("InlinedApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FullscreenActivity extends Activity {

	private WebView wv;
	private WebSettings setting;
	private RelativeLayout mContainer;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set Fullscreeen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// layout container
		mContainer=(RelativeLayout)findViewById(R.id.fullscreen_layout);
		
		// check network state
		NetworkManager nw = new NetworkManager(this);
		if (nw.NetworkState()) {
			// webview options
			wv = (WebView) findViewById(R.id.wv);
			setting = wv.getSettings();
			setting.setAppCacheEnabled(true);
			setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			setting.setJavaScriptEnabled(true);
			setting.setLoadsImagesAutomatically(true);
			setting.setUseWideViewPort(true);
			setting.setLoadWithOverviewMode(true);
			setting.setBuiltInZoomControls(true);
			setting.setSupportZoom(true);
			wv.setKeepScreenOn(true);

			// get url
			Intent intent = getIntent();
			String link = intent.getStringExtra(DetailActivity.LINK_DETAIL);
			if (link != null)
				wv.loadUrl(link);
			else
				Log.e(getPackageName(), "Fullsc activity Link bundle is null");
		} else {
			Toast toast = Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	@Override
	public void onBackPressed() {
		onDestroy();
		super.onBackPressed();
	}
	
	protected void onDestroy() {
		mContainer.removeView(wv);
		wv.removeAllViews();
		wv.destroy();
		super.onDestroy();
	};
}
