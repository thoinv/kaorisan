package com.niw.kuttypanda;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.niw.jumppypanda.R;
import com.niw.kuttypanda.base.DebugLog;

public class ShareFacebookActivity extends Activity{
	private Facebook facebook;
	private String messageToPost;
	private static final String[] PERMISSIONS = new String[] { "publish_stream" };
	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_share_facebook);
//		shareFacebook();
	}
	
	public void shareFacebook(){
		facebook = new Facebook("1418005058449444");
		restoreCredentials(facebook);
		messageToPost = "Hello Everyone.";
		if (!facebook.isSessionValid()) {
			loginAndPostToWall();
		} else {
			postToWall(messageToPost);
		}
	}
	
	public boolean saveCredentials(Facebook facebook) {
		Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	public boolean restoreCredentials(Facebook facebook) {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	public void loginAndPostToWall() {
		facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
	}

	public void postToWall(String message) {
		Bundle parameters = new Bundle();
		parameters.putString("message", message);
		parameters.putString("description", "topic share");
		try {
			facebook.request("me");
			String response = facebook.request("me/feed", parameters, "POST");
			DebugLog.loge("got response: " + response);
			if (response == null || response.equals("") || response.equals("false")) {
				showToast("Blank response.");
			} else {
				showToast("Message posted to your facebook wall!");
			}
		} catch (Exception e) {
			showToast("Failed to post to wall!");
			e.printStackTrace();
		}
	}
	
	public void onFinishActivity(){
		finish();
	}

	class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			saveCredentials(facebook);
			if (messageToPost != null) {
				postToWall(messageToPost);
			}
		}

		public void onFacebookError(FacebookError error) {
			showToast("Authentication with Facebook failed!");
		}

		public void onError(DialogError error) {
			showToast("Authentication with Facebook failed!");
		}

		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
		}
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		onFinishActivity();
	}
	
}