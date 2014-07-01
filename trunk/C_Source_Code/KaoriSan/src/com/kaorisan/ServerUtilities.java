	package com.kaorisan;

import static com.kaorisan.CommonUtilities.TAG;
import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.kaorisan.common.DebugLog;
import com.kaorisan.manager.TaskManager;

public final class ServerUtilities {

	/**
	 * Register this account/device pair within the server.
	 * 
	 */
	public static void register(final Context context, String token,
			String platform, String regId) {
		Log.i(TAG, "registering device (regId = " + regId + ")");

		TaskManager taskManager = new TaskManager();

		taskManager.postPushToken(token, platform, regId, new TaskManager.OnPostPushToken() {

					@Override
					public void onPostPushTokenMethod(boolean isSuccess, String message) {

						if (isSuccess) {

							DebugLog.logd(context.getString(R.string.server_registered));

							GCMRegistrar.setRegisteredOnServer(context, true);
							String messageServerRegistered = context.getString(R.string.server_registered);

							DebugLog.logd(messageServerRegistered);
						} else {
							DebugLog.logd("register not successfully!");
						}
					}
				});

	}

	public static void unregister(final Context context, final String regId) {
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		
		GCMRegistrar.setRegisteredOnServer(context, false);
		String message = context.getString(R.string.server_unregistered);
		DebugLog.logd( message);
		
	}

	
}
