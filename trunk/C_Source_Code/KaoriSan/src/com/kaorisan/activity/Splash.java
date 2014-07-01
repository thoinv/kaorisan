package com.kaorisan.activity;

import com.kaorisan.R;
import com.kaorisan.beans.User;
import com.kaorisan.common.DebugLog;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dataLayer.SQLiteDatabaseAdapter;
import com.kaorisan.dataLayer.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity {
	private long ms = 0;
	private long splashTime = 2000;
	private boolean splashActive = true;
	private boolean paused = false;
	private static Splash instant = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instant = this;
		
		SQLiteDatabaseAdapter.setContext(this);
		
		// Hides the titlebar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash);

		Thread mythread = new Thread() {
			public void run() {
				try {
					while (splashActive && ms < splashTime) {
						if (!paused)
							ms = ms + 100;
						sleep(100);
					}
				} catch (Exception e) {
				} finally {
					if(UserDao.getKaorisanToken() != null){
						
						try {
							String kaorisanToken = UserDao.getKaorisanToken();
							DebugLog.logd("Kaorisan Token in SQLite: " + kaorisanToken);
							
							if(!kaorisanToken.isEmpty()){
								CacheData.getInstant().setTokenKaorisan(UserDao.getKaorisanToken());
								
								String fullName = UserDao.getName();
								String avatar = UserDao.getAvatar();								
								User user = new User();
								if(fullName != null){
									DebugLog.logd("FullName in SQLite: " + fullName);
									user.setFullName(fullName);
								}
								
								if(avatar != null){
									DebugLog.logd("Avatar in SQLite: " + avatar);
									user.setAvatar(avatar);
								}
								
								CacheData.getInstant().setCurrentUser(user);
								
								Intent intent = new Intent(Splash.this, TaskActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
								startActivity(intent);
							}else{
								Intent intent = new Intent(Splash.this, LoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
								startActivity(intent);
							}
						} catch (Exception e2) {
							e2.printStackTrace();
							Intent intent = new Intent(Splash.this, LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
							startActivity(intent);
						}
						
					}
					
					finish();
				}
			}
		};
		mythread.start();
		
	}

	public static Splash getInstant() {
		return instant;
	}

}