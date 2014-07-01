package com.kaorisan.manager;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.kaorisan.beans.User;
import com.kaorisan.common.DebugLog;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.service.NetworkService;
@SuppressLint("NewApi")
public class AuthenticationManager {
	public interface OnGetTokenFromLoginFaceBookResult{
		void onGetTokenFromLoginFaceBookMethod(boolean isSuccess,String tokenKaorisan, String message);
	}
	OnGetTokenFromLoginFaceBookResult callbackOnGetTokenFromLoginFaceBookResult = null;
	
	public interface OnLogout {
		void onLogoutMethod(boolean isSuccess, String message);
	}
	OnLogout callbackOnLogout = null;
	
	public void getTokenKaorisanToFacebook(String fbtoken, String fbexpiration,OnGetTokenFromLoginFaceBookResult onGetTokenFromLoginFaceBookResult){
		callbackOnGetTokenFromLoginFaceBookResult = onGetTokenFromLoginFaceBookResult;
		final NetworkService service = new NetworkService();
		new AsyncTask<String,String,JSONObject>(){

			@Override
			protected JSONObject doInBackground(String... params) {
				return service.getTokenKaorisanFromLoginFaceBook(params[0], params[1]);
			}
			@Override
			protected void onPostExecute(JSONObject result){
				try {
					if(result == null){
						callbackOnGetTokenFromLoginFaceBookResult.onGetTokenFromLoginFaceBookMethod(false, null,"Network is unreachable");
						Log.i("Test", "Get token failed");
					}
					else{
						if(result.has("status")){
							callbackOnGetTokenFromLoginFaceBookResult.onGetTokenFromLoginFaceBookMethod(false, null, "");
							DebugLog.logd("Get Token Fail " + result.toString());
						}
						else{
							String token = result.getString("token");
							callbackOnGetTokenFromLoginFaceBookResult.onGetTokenFromLoginFaceBookMethod(true, token,null);
							DebugLog.logd("Get Token Success " + result.toString());
						}
					}
				} catch (Exception e) {
					DebugLog.loge(e.getMessage());
				}
			}
		}.execute(fbtoken,fbexpiration);
	}
	
	public interface OnRefreshTokenGoogle{
		void onRefreshTokenGoogle(boolean isSuccess, User user, String message);
	}
	
	OnRefreshTokenGoogle callbackOnRefreshTokenGoogleResult = null;
	
	public void refreshTokenGoogle(String clientId, String clientSecret, String refreshToken, OnRefreshTokenGoogle onRefreshTokenGoogleResult ){
		callbackOnRefreshTokenGoogleResult = onRefreshTokenGoogleResult;
		final NetworkService service = new NetworkService();
		new AsyncTask<String, String, User>(){

			@Override
			protected User doInBackground(String... params) {
				User user = null;
				JSONObject jsonResult = service.refreshAccessTokenGoogle(params[0], params[1], params[2]);
				try {
					if(jsonResult != null){
						user = new User();
						if(jsonResult.has("access_token")){
							user.setGoogleToken(jsonResult.getString("access_token"));
						}
						
						if(jsonResult.has("expires_in")){
							user.setGoogleExpDate(jsonResult.getString("expires_in"));
						}
					}else{
						DebugLog.logd("Reresh access token failed!");
					}
					return user;
				} catch (Exception e) {
					DebugLog.loge("Refresh access token exception: " + e);
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(User result) {
				if(result != null){
					callbackOnRefreshTokenGoogleResult.onRefreshTokenGoogle(true, result, null);
				}else{
					callbackOnRefreshTokenGoogleResult.onRefreshTokenGoogle(false, new User(), "failed");
				}
			};
			
		}.execute(clientId, clientSecret, refreshToken);

	}
	
	public void getTokenKaorisanToGoogle(String gbtoken, String gbtokenRefresh,String expiresDate,OnGetTokenFromLoginFaceBookResult onGetTokenFromLoginFaceBookResult){
		callbackOnGetTokenFromLoginFaceBookResult = onGetTokenFromLoginFaceBookResult;
		final NetworkService service = new NetworkService();
		new AsyncTask<String,String, JSONObject>(){

			@Override
			protected JSONObject doInBackground(String... params) {
				return service.getTokenKaorisanFromLoginGoogle(params[0], params[1], params[2]);
			}
			
			protected void onPostExecute(JSONObject result){
				try {
					if(result == null){
						callbackOnGetTokenFromLoginFaceBookResult.onGetTokenFromLoginFaceBookMethod(false, null,"Network is unreachable");
						Log.i("Test", "Get token failed");
					}
					else{
						if(result.has("status")){
							callbackOnGetTokenFromLoginFaceBookResult.onGetTokenFromLoginFaceBookMethod(false, null, "");
							DebugLog.logd("Get Token Fail " + result.toString());
						}
						else{
							String token = result.getString("token");
							callbackOnGetTokenFromLoginFaceBookResult.onGetTokenFromLoginFaceBookMethod(true, token,null);
							DebugLog.logd("Get Token Success " + result.toString());
						}
					}
				} catch (Exception e) {
					DebugLog.loge(e.getMessage());
				}
			}
		}.execute(gbtoken,gbtokenRefresh,expiresDate);
	}
	
	
	public static String getAvatarFaceBook(String id){
		String address = "http://graph.facebook.com/"+id+"/picture";
	    URLConnection url;
	    URL newLocation = null;
	    try {
	        url = new URL(address).openConnection();
	        url.connect();
	        InputStream is = url.getInputStream();
	        newLocation = url.getURL();
	        Log.i("linkurl", ""+url.getURL());
	        is.close();
	        //HttpURLConnection.setFollowRedirects(true); //Do _not_ follow redirects!
	        //HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        //newLocation = connection.getHeaderField("Location");
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return newLocation.toString();
	}
	public void logout(String token, String platform, String pushToken, OnLogout onLogout){
		callbackOnLogout = onLogout;
		
		final NetworkService service = new NetworkService();

		new AsyncTask<String, String, JSONObject>() {

			@Override
			protected JSONObject doInBackground(String... params) {
				DebugLog.logd(params[0] + " " + params[1] + " " + params[2]);
				return service.logout(params[0], params[1], params[2]);
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				if (result != null) {
					if (!result.has("message")) {
						try {
							DebugLog.logd("Status: " + result.getString("status"));
							callbackOnLogout.onLogoutMethod(true, null);
						} catch (JSONException e) {
							DebugLog.loge("Post pushToken error: " + e.getMessage());
						}
						
					} else {
						callbackOnLogout.onLogoutMethod(false, null);
					}
				} else {
					DebugLog.logd("result is null");
					callbackOnLogout.onLogoutMethod(false, null);
				}

			}
		}.execute(token, platform, pushToken);
	}
	public void logOutCurrentUser() {
		DebugLog.logd("log out current User");
		CacheData.getInstant().setCurrentUser(null);
	}
}
