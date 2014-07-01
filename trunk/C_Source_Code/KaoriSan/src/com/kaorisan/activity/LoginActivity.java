package com.kaorisan.activity;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Utility;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.kaorisan.R;
import com.kaorisan.ServerUtilities;
import com.kaorisan.beans.User;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.NetworkUtil;
import com.kaorisan.common.Utils;
import com.kaorisan.common.Utils.Social;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dataLayer.SQLiteDatabaseAdapter;
import com.kaorisan.dataLayer.UserDao;
import com.kaorisan.manager.AuthenticationManager;

@SuppressWarnings("deprecation")
public class LoginActivity extends FragmentActivity implements
ConnectionCallbacks, OnConnectionFailedListener,
		OnAccessRevokedListener {
	Handler mHandler = new Handler();

	private Button btnLoginFaceBook;
	
	public static Activity loginActivity = null;

	private SessionListener mSessionListener = new SessionListener();

	private static final String TAG = "SignInTestActivity";

	public static int isLogout = 0;
	
	private Button btnLoginGoogle;

//	private static final String SCHEME = "package";

//	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";

//	private static final String APP_PKG_NAME_22 = "pkg";

//	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";

//	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	
	// Single sign on with facebook app => set this value to >=0, otherwise set
	// < 0
	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;

	private static final int OUR_REQUEST_CODE = 2000;

	public static Utils.Social currentSocial = Social.FACEBOOK;

	public static PlusClient mPlusClient;

	private boolean mResolveOnFail;

	private ConnectionResult mConnectionResult = null;

	ProgressDialog showProcess = null;
	
	private boolean isClickButtonGoogle = false;
	
	// build APP_ID here and APP_ID in string.xml
	public static final String APP_ID = "237471026413628";

	protected static final int GOOGLE_PLAY_SEVICES_MISSING_REQUEST_CODE = 1200;

	Resources resource;

	String[] permissions = {};

	private FbAPIsAuthListener authenListener = new FbAPIsAuthListener();;
	private FbAPIsLogoutListener logoutListener = new FbAPIsLogoutListener();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginActivity = this;
		SQLiteDatabaseAdapter.setContext(this);
		
		checkGooglePlayServices();
		
		mPlusClient = new PlusClient.Builder(this, this, this)
		.setVisibleActivities("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity").setScopes(Scopes.PLUS_LOGIN, Scopes.PLUS_PROFILE).build();

		btnLoginGoogle = (Button) findViewById(R.id.btn_login_google);
		initGoogleButton();

		genKeyHash();
		resource = getResources();

		DebugLog.logd("On create");

		// Create the Facebook Object using the app id.
		if (Utility.mFacebook == null) {
			Utility.mFacebook = new Facebook(APP_ID);
		}
		// Instantiate the asynrunner object for asynchronous api calls.
		if (Utility.mAsyncRunner == null) {
			Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
		}

		SessionStore.restore(Utility.mFacebook, this);

		SessionEvents.addAuthListener(authenListener);
		SessionEvents.addLogoutListener(logoutListener);

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		btnLoginFaceBook = (Button) findViewById(R.id.btn_login_facebook);
		initFacebookButton();

		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);

		if (Utility.mFacebook.isSessionValid() && isLogout == 0 && currentSocial == Social.FACEBOOK) {
			DebugLog.logd("On facebook Create");
			if (Utils.checkInternetConnect(this)) {
				requestGetUserData();
			}
		}

		if (currentSocial == Social.GOOGLE && isLogout == 0) {
			DebugLog.logd("On Google Create");
			mPlusClient.connect();
		}
		checkLogout();

	}

	private void checkGooglePlayServices() {
		// TODO Auto-generated method stub
		int googlePlayServiceResultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
			
			switch (googlePlayServiceResultCode) {			
			case ConnectionResult.SERVICE_MISSING:
				DebugLog.logd("missing");
				  Intent intent = new Intent(Intent.ACTION_VIEW);
				  intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms"));
				  startActivity(intent);
				break;	
			
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			case ConnectionResult.SERVICE_DISABLED:
				DebugLog.logd("update require");
				isClickButtonGoogle = false;
		        showGooglePlayServicesAvailabilityErrorDialog(googlePlayServiceResultCode);
			
				break;
	
			}
	}

	private void checkLogout() {

		if (isLogout == 1 && NetworkUtil.NETWORK_STATE != NetworkUtil.TYPE_NOT_CONNECTED) {
			if (currentSocial == Social.FACEBOOK) {
				if (Utility.mFacebook.isSessionValid()) {
					SessionEvents.onLogoutBegin();
					AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
					
					try {
						if (showProcess != null) {
							if (showProcess.isShowing()) {
								showProcess.dismiss();
							}
							showProcess = null;
						}

						showProcess = new ProgressDialog(this);
						showProcess.setCancelable(false);
						showProcess.setTitle(getResources().getString(R.string.txt_logout));

						showProcess.show();
					} catch (Exception ex) {
						DebugLog.logd("checkLogout ??? Do not know what happen with show process, " +
								"maybe the facebook already hace show process");
					}
					asyncRunner.logout(this, new LogoutRequestListener());
				}
			
				if(TaskActivity.taskActivity != null){
					TaskActivity.taskActivity.finish();
				}
			} else if (currentSocial == Social.GOOGLE) {
				DebugLog.logd("On google logout");
				mPlusClient.disconnect();
				
			}
			
			if(CacheData.getInstant().getTokenKaorisan() != null){
				if(!CacheData.getInstant().getTokenKaorisan().isEmpty()){
					DebugLog.logd("Delete token kaorisan in SQLite");
//					UserDao.deleteUser(CacheData.getInstant().getTokenKaorisan());
					UserDao.setCurrentUser("", "0", "", "");
				}
			}
		}
		
		if(CacheData.getInstant().getCurrentUser() != null){
			if(CacheData.getInstant().getCurrentUser().getPushToken() != null && isLogout == 1){
				if (GCMRegistrar.isRegisteredOnServer(TaskActivity.taskActivity)) {
		            ServerUtilities.unregister(TaskActivity.taskActivity, CacheData.getInstant().getCurrentUser().getPushToken());
		            Log.i(TAG, "Unregister server");
		        } else {
		            // This callback results from the call to unregister made on
		            // ServerUtilities when the registration to the server failed.
		            Log.i(TAG, "Ignoring unregister callback");
		        }
			}
		}
		
		Utils.resetCacheData();
		isLogout = 0;
	}

	private void initFacebookButton() {
		btnLoginFaceBook.setText(R.string.login_btn_login_facebook);
		btnLoginFaceBook.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				
				if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS){
					if (Utils.checkInternetConnect(getApplicationContext())) {
						onFacebookClickLogin();
					} else {
						Utils.showToast(LoginActivity.this, getResources().getString(R.string.not_connect_internet_msg));
					}
				}else{
					DebugLog.logd("Google play service is invailable!");
					checkGooglePlayServices();
				}
			}
		});
	}

	@SuppressLint("InlinedApi")
	private void genKeyHash() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo("com.kaorisan", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (currentSocial == Social.FACEBOOK) {
				DebugLog.logd("onActivityResult session.isOpened()");
				Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
			} else {
				onActivityResultForGoogle(requestCode, resultCode);
				Log.i("Cancal", "Cancel");
			}
		}
	}

	// Function Login FaceBook
	private void onFacebookClickLogin() {
		currentSocial = Social.FACEBOOK;
		if (Utility.mFacebook.isSessionValid()) {
			DebugLog.logd("On click login");
			requestGetUserData();
		} else {
			Utility.mFacebook.authorize(this, permissions, AUTHORIZE_ACTIVITY_RESULT_CODE, new LoginDialogListener());
		}
	}

	// Function Login FaceBook
	private final class LoginDialogListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		@Override
		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class LogoutRequestListener extends BaseRequestListener {
		@Override
		public void onComplete(String response, final Object state) {
			/*
			 * callback should be run in the original thread, not the background
			 * thread
			 */
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});

			try {
				if (showProcess != null) {
					if (showProcess.isShowing()) {
						showProcess.dismiss();
					}
				}
			}

			catch (Exception e) {
				DebugLog.logd("LogoutRequestListener ??? Do not know what happen with show process, maybe the facebook already hace show process");
			}
		}
	}

	public class FbAPIsAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {
			DebugLog.logd("On Auth Suceed");
		}

		@Override
		public void onAuthFail(String error) {
		}
	}

	/*
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 */
	public class FbAPIsLogoutListener implements LogoutListener {
		@Override
		public void onLogoutBegin() {
		}

		@Override
		public void onLogoutFinish() {
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		@Override
		public void onAuthSucceed() {
			SessionStore.save(Utility.mFacebook, LoginActivity.this);
			requestGetUserData();
		}

		@Override
		public void onAuthFail(String error) {
		}

		@Override
		public void onLogoutBegin() {
		}

		@Override
		public void onLogoutFinish() {
			SessionStore.clear(LoginActivity.this);
		}
	}

	public class UserRequestListener extends BaseRequestListener {
		@Override
		public void onComplete(final String response, final Object state) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);
				final String picURL = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");

				String email = "";
				try {
					email = jsonObject.getString("email");
				} catch (Exception Ex) {
					DebugLog.logd("Can not get email???");
				}
				
				final String fullName = jsonObject.getString("name");
				final User cacheUser = new User();
				
				cacheUser.setId(Utility.userUID);
				cacheUser.setEmail(email);
				cacheUser.setFullName(fullName);
				cacheUser.setAvatar(picURL);
				CacheData.getInstant().setCurrentUser(cacheUser);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						AuthenticationManager manager = new AuthenticationManager();
						// Looper.prepare();
						manager.getTokenKaorisanToFacebook(Utility.mFacebook.getAccessToken(), String.valueOf(Utility.mFacebook.getAccessExpires()),
								new AuthenticationManager.OnGetTokenFromLoginFaceBookResult() {

									@Override
									public void onGetTokenFromLoginFaceBookMethod(boolean isSuccess, String tokenKaorisan, String message) {
										DebugLog.logd("Is success: " + isSuccess);
						
										if (isSuccess) {
										
											setCurrentUserToDatabase(tokenKaorisan, "1",  CacheData.getInstant().getCurrentUser().getAvatar(), CacheData.getInstant().getCurrentUser().getFullName());
											
											try {
												if (showProcess != null) {
													if (showProcess.isShowing()) {
														showProcess.dismiss();
													}
												}
											} catch (Exception ex) {
												DebugLog.logd("onGetTokenFromLoginFaceBookMethod ??? Do not know what happen with show process, " +
														"maybe the facebook already hace show process");
											}

											CacheData.getInstant().setTokenKaorisan(tokenKaorisan);
											DebugLog.logd("TokenKaorisan Test: " + CacheData.getInstant().getTokenKaorisan());
											DebugLog.logd("onActivityResult cacheUser : " + cacheUser);
											Intent taskIntent = new Intent(LoginActivity.this, TaskActivity.class);	
											startActivity(taskIntent);
//											finish();
											
										} else {
											Utils.showToast(LoginActivity.this, getResources().getString(R.string.cant_find_account_msg));
											try {
												if (showProcess != null) {
													if (showProcess.isShowing()) {
														showProcess.dismiss();
													}
												}
												
											} catch (Exception ex) {
												DebugLog.logd("onGetTokenFromLoginFaceBookMethod ??? Do not know what happen with show process, " +
														"maybe the facebook already hace show process");
											}
											SessionEvents.onLogoutBegin();
											AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
											asyncRunner.logout(LoginActivity.this, new LogoutRequestListener());
										}

									}

								
								});

					}
				});
				// Looper.loop();
				// if (Build.VERSION.SDK_INT <= 11){
				// Log.i("Vao day", "Vao day nhe");
				// Looper.myLooper().quit();
				// }
			} catch (JSONException e) {
				e.printStackTrace();
				try {
					if (showProcess != null) {
						if (showProcess.isShowing()) {
							showProcess.dismiss();
						}
					}
				} catch (Exception ex) {
					DebugLog.logd("Catch on onComplete get userdata");
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Utils.resetCacheData();
		
		Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        
		finish();
	}

	private void requestGetUserData() {
		DebugLog.logd("Test login facebook");
		try {
			if (showProcess != null) {
				if (showProcess.isShowing()) {
					showProcess.dismiss();
				}
				showProcess = null;
			}

			showProcess = new ProgressDialog(this);
			showProcess.setCancelable(false);
			showProcess.setTitle(getResources().getString(R.string.loading_status));
			CacheData.getInstant().setCurrentProgressDialog(showProcess);
			showProcess.show();
		} catch (Exception ex) {
			DebugLog.logd("Have authen before");
			return;
		}
		Bundle params = new Bundle();
		params.putString("fields", "name, picture, email");
		Utility.mAsyncRunner.request("me", params, new UserRequestListener());
	}

	@Override
	protected void onStop() {
		super.onStop();
		DebugLog.logd("On stop");
		try {
			if (showProcess != null) {
				if (showProcess.isShowing()) {
					showProcess.dismiss();
				}
			}
		} catch (Exception e) {
			DebugLog.logd("onStop ??? Do not know what happen with show process, maybe the facebook already hace show process");
		}


	}
	private void setCurrentUserToDatabase(String tokenKaorisan, String pushable, String avatar, String fullName) {
		if(tokenKaorisan != null && avatar != null && fullName != null){
			if(!UserDao.isExist(tokenKaorisan)){
				if(CacheData.getInstant().getCurrentUser() != null){
					UserDao.setCurrentUser(tokenKaorisan, pushable, avatar, fullName);
				}
			}
		}else{
			DebugLog.logd("User not exist!");
		}
	}
	
	@Override
	protected void onDestroy() {

		super.onDestroy();
		mPlusClient.disconnect();
		DebugLog.logd("Destroy");
		SessionEvents.removeAuthListener(authenListener);
		SessionEvents.removeLogoutListener(logoutListener);
		SessionEvents.removeAuthListener(mSessionListener);
		SessionEvents.removeLogoutListener(mSessionListener);
	}

	// Function Login Google
	private void initGoogleButton() {
		// Connect google our sign in, sign out and disconnect buttons
		btnLoginGoogle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				currentSocial = Social.GOOGLE;
				switch (view.getId()) {
				case R.id.btn_login_google:
					DebugLog.logd("on button login google clicked");
					if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS){
						if(!isClickButtonGoogle){
							
							Log.i("Click", "Click Google");
							isClickButtonGoogle = true;
							
							DebugLog.logd("success");
							if (!mPlusClient.isConnected()) {
								mResolveOnFail = true;
								if (mConnectionResult != null) {
										startGoogleResolution();
								} else {
									mPlusClient.connect();
								}
							} else {
								mPlusClient.connect();
							}
						}
					}else{
						DebugLog.logd("Google play service available!");
						checkGooglePlayServices();
					}
					break;
				}
			}
		});
	}
	
	void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
	    runOnUiThread(new Runnable() {
		    public void run() {
		    final Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, LoginActivity.this, GOOGLE_PLAY_SEVICES_MISSING_REQUEST_CODE);
		        if (dialog == null) {
		                Log.e("Google play services", "couldn't get GooglePlayServicesUtil.getErrorDialog");
		                Utils.showToast(LoginActivity.this, "Incompatible version of Google Play Services");
		            }
		            dialog.show();
		       }
		    });
	}

//	public void showInstalledAppDetails(Context context, String packageName) {
//	    Intent intent = new Intent();
//	    final int apiLevel = Build.VERSION.SDK_INT;
//	    if (apiLevel >= 9) { // above 2.3
//	        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//	        Uri uri = Uri.fromParts(SCHEME, packageName, null);
//	        intent.setData(uri);
//	    } else { // below 2.3
//	        final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
//	        intent.setAction(Intent.ACTION_VIEW);
//	        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//	        intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
//	        intent.putExtra(appPkgName, packageName);
//	    }
//	    context.startActivity(intent);
//	}
	
	// Function Login Google
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		DebugLog.loge("Connection Failed");
		isClickButtonGoogle = false;
		// Function Login Google
		if (result.hasResolution()) {
			mConnectionResult = result;
			if (mResolveOnFail) {
				startGoogleResolution();
			}
		}

		mConnectionResult = result;
	}

	// Function Login Google
	// Because kaori san now do not allow to login google, disable code
	@Override
	public void onConnected(Bundle bundle) {

		Log.v(TAG, "Connected. Yay!");
		mResolveOnFail = false;
		AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>() {
			@Override
			protected String doInBackground(Object... params) {
				
				String scope = "oauth2:" + Scopes.PLUS_LOGIN;
				try {
					String tokenGoolge = GoogleAuthUtil.getToken(LoginActivity.this,
							mPlusClient.getAccountName(), scope);
					DebugLog.logd("token google: " + tokenGoolge);
					return tokenGoolge;
				} catch (UserRecoverableAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GoogleAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(final String tokenGoolge) {
				if(tokenGoolge != null){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							try {
								User cacheUser = new User();
								cacheUser.setGoogleToken(tokenGoolge);
								cacheUser.setGoogleExpDate(3600 + "");
								cacheUser.setEmail(mPlusClient.getAccountName());
								Log.i("Email", mPlusClient.getAccountName());
								cacheUser.setFullName(mPlusClient.getCurrentPerson().getDisplayName());
								Log.i("Display name", mPlusClient.getCurrentPerson().getDisplayName());
								cacheUser.setAvatar(mPlusClient.getCurrentPerson().getImage().getUrl());
								Log.i("Url", mPlusClient.getCurrentPerson().getImage().getUrl());
								CacheData.getInstant().setCurrentUser(cacheUser);
								
								getTokenKaorisanToGoogle(CacheData.getInstant()
										.getCurrentUser().getGoogleToken(),"","", true);


							} catch (Exception exception) {
								DebugLog.loge("Exception: " + exception);
								//return;
								mPlusClient.clearDefaultAccount();
								mPlusClient.disconnect();
							}
							
						}
					});
				}
				isClickButtonGoogle = false;
			}
		};
		task.execute();
	}

	// Function Login Google
	@Override
	public void onDisconnected() {
		// Bye!
		isClickButtonGoogle = false;
		Log.v(TAG, "Disconnected. Bye!");
	}

	// Function Login Google
	@Override
	public void onAccessRevoked(ConnectionResult status) {
		if (mPlusClient.isConnected()) {
			mPlusClient.disconnect();
		}
		mPlusClient.connect();
		findViewById(R.id.btn_login_google).setVisibility(View.VISIBLE);
	}

	// Function Login Google
	private void startGoogleResolution() {
		try {
			mResolveOnFail = false;
			mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
		} catch (SendIntentException e) {
			if (!mPlusClient.isConnected()) {
				mPlusClient.connect();
			}
		}
	}

	// Function Login Google
	private void onActivityResultForGoogle(int requestCode, int resultCode) {
		if (requestCode == OUR_REQUEST_CODE && resultCode == RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}

	private void getTokenKaorisanToGoogle(String accessToken, String refreshToken, String expDate, boolean isShow) {
		AuthenticationManager manager = new AuthenticationManager();
		if (isShow == true) {
			showProcess = new ProgressDialog(LoginActivity.this);
			showProcess.setCancelable(false);
			showProcess.setTitle(getResources().getString(R.string.loading_status));
			showProcess.show();
		}
		manager.getTokenKaorisanToGoogle(accessToken, refreshToken, expDate, 
				new AuthenticationManager.OnGetTokenFromLoginFaceBookResult() {

					@Override
					public void onGetTokenFromLoginFaceBookMethod(boolean isSuccess, String tokenKaorisan, String message) {
						if (showProcess != null) {
							showProcess.dismiss();
							showProcess = null;
						}
						
						if (isSuccess) {
							if(!UserDao.isExist(tokenKaorisan)){
								setCurrentUserToDatabase(tokenKaorisan, "1",  CacheData.getInstant().getCurrentUser().getAvatar(), CacheData.getInstant().getCurrentUser().getFullName());
							}
							CacheData.getInstant().setTokenKaorisan(tokenKaorisan);
							Intent intent = new Intent(LoginActivity.this, TaskActivity.class);
							startActivity(intent);
							Log.i("TokenKaorisanLogin google", tokenKaorisan);
//							finish();
						}else{
							Utils.showToast(LoginActivity.this,getResources().getString(R.string.cant_find_account_msg));
							mPlusClient.clearDefaultAccount();
							mPlusClient.disconnect();
							Log.i("Show message","");
//							popup = new PopUpMessage(LoginActivity.this,
//									getResources().getString(R.string.cant_find_account_msg),
//									true, 3000, false);
//							if(!popup.isShowing()){
//								popup.show();
//							}else{
//								popup.dismiss();
//							}

						}
						isClickButtonGoogle = false;
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("OnResume", "OnResume");
		isClickButtonGoogle = false;
	}

}
