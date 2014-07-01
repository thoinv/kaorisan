package com.kaorisan.activity;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.kaorisan.R;

public class LoginFragment extends Fragment {
	//private static final String URL_PREFIX = "https://graph.facebook.com/me/?access_token=";
	private static final String TAG = LoginFragment.class.getSimpleName();

	private UiLifecycleHelper uiHelper;
	
	private final List<String> permissions;
	
	public LoginFragment() {
		permissions = Arrays.asList("email","user_birthday","user_photos");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_login, container, false);

		LoginButton authButton = (LoginButton) view.findViewById(R.id.btn_login_facebook);
		authButton.setFragment(this);
		authButton.setReadPermissions(permissions);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		// For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		session = Session.getActiveSession();
		if (session.isOpened()) {
			/*
			CacheData.getInstant().setUrlFaceBook(URL_PREFIX+session.getAccessToken());
			Log.i("Urltessttttttttt",URL_PREFIX+session.getAccessToken());
			AuthenticationManager manager = new AuthenticationManager();
			manager.getUserInfoFaceBook(new AuthenticationManager.OnPaserInfoFaceBookResult() {
				
				@Override
				public void onPaserInfoFaceBookResult(boolean isSuccess,
						final CacheUser cacheUser, String message) {
					if(isSuccess){
						String urlAvatar = "http://graph.facebook.com/"+cacheUser.getId()+"/picture";
						Utils.loadBitmap(urlAvatar, new Utils.OnBitmapResult() {
							
							@Override
							public void onBitmapResult(boolean isSuccess, String message, Bitmap bitmap) {
								if(isSuccess){
									if(bitmap != null){
										cacheUser.setAvatar(bitmap);
										Log.i("Testbitmap","Bitmap khong null");
									}
								}
							}
						});
						CacheData.getInstant().setCacheUser(cacheUser);
						Log.i("Test du lieu",CacheData.getInstant().getCacheUser().getEmail() + CacheData.getInstant().getCacheUser().getAvatar());
						Intent intent = new Intent(getActivity(),TaskActivity.class);
						startActivity(intent);
					}
				}
			});
			*/
			
			Request request = Request.newMeRequest(session,new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if( user != null){
						Log.i("Email",(String)user.getProperty("email"));
						Log.i("Email",user.getId());
						Log.i("Email",user.getBirthday());
						Log.i("Email",user.getFirstName());
						Log.i("Email",user.getLastName());
						Log.i("Email",user.getUsername());
					}
					
				}
			});
			Request.executeBatchAsync(request);
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
}
