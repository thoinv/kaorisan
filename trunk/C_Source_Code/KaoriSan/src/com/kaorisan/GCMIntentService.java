	package com.kaorisan;

import static com.kaorisan.CommonUtilities.SENDER_ID;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.kaorisan.activity.TaskDetailActivity;
import com.kaorisan.beans.Task;
import com.kaorisan.beans.User;
import com.kaorisan.common.DebugLog;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dataLayer.SQLiteDatabaseAdapter;
import com.kaorisan.dataLayer.UserDao;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String PLATFORM = "android";
	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(SENDER_ID);
		SQLiteDatabaseAdapter.setContext(this);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		if(!registrationId.isEmpty()){
			CacheData.getInstant().getCurrentUser().setPushToken(registrationId);
		}
		DebugLog.logd("Your device registred with GCM");
		// Log.d("NAME", TaskDetailActivity.name);
		ServerUtilities.register(context, CacheData.getInstant().getTokenKaorisan(), PLATFORM, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		DebugLog.logd(getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		
		if(CacheData.getInstant().getCurrentUser() == null && UserDao.getKaorisanToken() != null){
			if(!UserDao.getKaorisanToken().isEmpty()){
				CacheData.getInstant().setTokenKaorisan(UserDao.getKaorisanToken());
			
				User user = new User();
				
				String fullName = UserDao.getName();
				String avatar = UserDao.getAvatar();
				
				if(fullName != null){
					user.setFullName(fullName);
				}
				
				if(avatar != null){
					user.setAvatar(avatar);
				}
				user.setPushable("1");
				
				CacheData.getInstant().setCurrentUser(user);
			}else{
				DebugLog.logd("");
			}
		}
		String message = intent.getExtras().getString("body");
		int taskId = Integer.parseInt(intent.getExtras().getString("chore"));
		
		DebugLog.logd("id: " +  taskId);
		DebugLog.logd(message);

		generateNotification(context, message, taskId);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		DebugLog.logd(message);
		// notifies user
		generateNotification(context, message, CacheData.getInstant().getCurrentTask().getId());
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		DebugLog.logd(getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		DebugLog.logd(getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message, int taskId) {
		if(CacheData.getInstant().getCurrentUser() != null){
			DebugLog.logd("Pushable in SQLite: " + UserDao.getPushable());
			
			if(UserDao.getPushable() != "0"){
				int icon = R.drawable.logo_notification;
				long when = System.currentTimeMillis();
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(icon, message, when);
				
				String title = context.getString(R.string.app_name);
				Intent notificationIntent = new Intent(context, TaskDetailActivity.class);
				
				Task currentTask = new Task();
				currentTask.setId(taskId);
				CacheData.getInstant().setCurrentTask(currentTask);
				
				UserDao.setCurrentTaskPushId(taskId);
				
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
				notification.setLatestEventInfo(context, title, message, intent);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				
				// Play default notification sound
				notification.defaults |= Notification.DEFAULT_SOUND;
			
				// notification.sound = Uri.parse("android.resource://" +
				// context.getPackageName() + "your_sound_file_name.mp3");
			
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notificationManager.notify(0, notification);
			}else{
				DebugLog.logd("Account is logout!");
			}
		}else{
			DebugLog.logd("currentUser is null!");
		}
		
	}

}
