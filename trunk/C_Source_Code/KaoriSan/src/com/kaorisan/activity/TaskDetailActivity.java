package com.kaorisan.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.adapter.ReplyAdapter;
import com.kaorisan.beans.Reply;
import com.kaorisan.beans.Task;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dataLayer.SQLiteDatabaseAdapter;
import com.kaorisan.dataLayer.UserDao;
import com.kaorisan.manager.TaskManager;

public class TaskDetailActivity extends Activity {

	TextView txtTitle;
	TextView txtContent;
	TextView btnReply;
	TextView txtTime;
	TextView txtName;
	TextView txtMessage;

	Button btnPlayAudio;

	ImageView imgAttachmentForScreenRequest;

	TextView txtItemTextOfExpandListView;

	TextView txtSender;

	Resources resource = null;;

	private ListView listReply;

	private ReplyAdapter replyAdapter = null;

	ProgressDialog showProcess = null;

	private LinearLayout audioRequestLayout;

	private final int TASK_DETAIL_REQUEST_CODE = 1000;

	private final String DEFAULT_NAME_SENDER = "You";

	private static int REQUEST_CODE_TO_REPLYACTIVITY = 1;

	//private MediaPlayer mPlayer = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(UserDao.getCurrentTaskPushId() != 0){
			Task currentTask = new Task();
			currentTask.setId(UserDao.getCurrentTaskPushId());
			CacheData.getInstant().setCurrentTask(currentTask);
			UserDao.setCurrentTaskPushId(0);
		}
		
		if (CacheData.getInstant().getCurrentUser() != null) {
			setContentView(R.layout.activity_task_detail);
			
			SQLiteDatabaseAdapter.setContext(this);
			
			resource = getApplicationContext().getResources();

			ImageView attachImageButton = (ImageView) findViewById(R.id.imgAttachmentForScreenRequest);
			attachImageButton.setImageResource(R.drawable.attach_gray);

			listReply = (ListView) findViewById(R.id.listReply);

			LayoutInflater layoutInflater = LayoutInflater.from(this);
			LinearLayout headerListViewLayout = (LinearLayout) layoutInflater.inflate(R.layout.reply_header_item, null, false);

			audioRequestLayout = (LinearLayout) headerListViewLayout.findViewById(R.id.audioRequestLayout);

			btnPlayAudio = (Button) headerListViewLayout.findViewById(R.id.btnPlayAudio);
			btnPlayAudio.setText(R.string.play);

			txtMessage = (TextView) headerListViewLayout.findViewById(R.id.txtMessage);

			txtContent = (TextView) headerListViewLayout.findViewById(R.id.txtRequestContent);
			
			txtTime = (TextView) headerListViewLayout.findViewById(R.id.txtTime);

			listReply.addFooterView(headerListViewLayout);

			txtSender = (TextView) findViewById(R.id.txtNameSender);
			txtSender.setText(DEFAULT_NAME_SENDER);
			
			txtMessage.setText(resource.getString(R.string.request_success_message));

			//mPlayer = new MediaPlayer();
			
			imgAttachmentForScreenRequest = (ImageView) findViewById(R.id.imgAttachmentForScreenRequest);

			txtItemTextOfExpandListView = (TextView) findViewById(R.id.list_item_text_child);

			txtTitle = (TextView) findViewById(R.id.txtTitle);
//			if (CacheData.getInstant().getCurrentTask().getTitle() != null) {
//				txtTitle.setText(CacheData.getInstant().getCurrentTask().getTitle());
//			}
			if(CacheData.getInstant().getCurrentTask() != null){
				bindData();
			}else{
				if(LoginActivity.loginActivity !=null){
					finish();
				}else{
					Intent intentLogin = new Intent(this, LoginActivity.class);
					startActivity(intentLogin);
					finish();
				}
			}

		} else {
			if(LoginActivity.loginActivity != null){
				finish();
			}else{
				Intent intentLogin = new Intent(this, LoginActivity.class);
				startActivity(intentLogin);
				finish();
			}
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void bindData() {
		DebugLog.logd("bind data");
		TaskManager manager = new TaskManager();
		showProcess = new ProgressDialog(this);
		showProcess.setCancelable(false);
		showProcess.setTitle("Loading...");

		showProcess.show();
		CacheData.getInstant().setCurrentProgressDialog(showProcess);
		manager.getTaskDetail(String.valueOf(CacheData.getInstant().getCurrentTask().getId()),
				CacheData.getInstant().getTokenKaorisan(), new TaskManager.OnGetRequestResult() {

					@Override
					public void onGetRequestResultMethod(boolean isSuccess, final Task task, String message) {
						if (isSuccess) {

							CacheData.getInstant().setCurrentTask(task);
							if (task.getListAudios() != null) {
								if (task.getListAudios().isEmpty()) {
									btnPlayAudio.setVisibility(View.INVISIBLE);
									txtMessage.setVisibility(View.INVISIBLE);
									audioRequestLayout.setVisibility(View.INVISIBLE);
									
								} else {
									btnPlayAudio.setVisibility(View.VISIBLE);
									txtMessage.setVisibility(View.VISIBLE);
									audioRequestLayout.setVisibility(View.VISIBLE);
//									new Thread(new Runnable() {
//
//										@Override
//										public void run() {
//											try {
//												mPlayer.reset();
//												mPlayer.setDataSource(Utils.replaceHttpsToHttp(task.getListAudios().get(0).getFilePath()));
//												mPlayer.prepare();
//												
//											} catch (Exception e) {
//												Log.e("Player Exception", "could'nt play audio file");
//											}
//										}
//									}).start();
									

								}

								if (CacheData.getInstant().getListAttachment() != null) {
									if (!CacheData.getInstant().getListAttachment().isEmpty()) {

										imgAttachmentForScreenRequest.setImageResource(R.drawable.attach);
									} else {
										imgAttachmentForScreenRequest.setImageResource(R.drawable.attach_gray);
									}
								} else {
									imgAttachmentForScreenRequest.setImageResource(R.drawable.attach_gray);
								}
							}

							List<Reply> replies = task.getListReply();
							Collections.reverse(replies);
							replyAdapter = new ReplyAdapter(TaskDetailActivity.this, TASK_DETAIL_REQUEST_CODE, replies);

							DebugLog.logd(task.getCreatedAt());
							txtTitle.setText(task.getTitle());
							txtTime.setText(Utils.getRelativeTime(getApplicationContext(), Long.valueOf(task.getCreatedAt())));

							txtContent.setText(task.getRequest());

							DebugLog.logd(task.getRequest());

							listReply.setAdapter(replyAdapter);

							DebugLog.logd("Get Task Detail success");

						} else {
							DebugLog.logd("Get Task Detail Faild");
						}

						Utils.dismissCurrentProgressDialog();
						showProcess = null;
					}
				});
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {

		case R.id.btnPlayAudio:
			DebugLog.logd("Button Play Audio click");
			Intent intentAudio = new Intent();
			intentAudio.setAction(Intent.ACTION_VIEW);
			intentAudio.setDataAndType(Uri.parse(Utils.replaceHttpsToHttp(CacheData.getInstant().getCurrentTask().getListAudios().get(0).getFilePath())), "audio/*");
			startActivity(intentAudio);
//			try {
//				final String audioUrl = Utils.replaceHttpsToHttp(CacheData.getInstant().getCurrentTask().getListAudios().get(0).getFilePath());
//
//				if (!mPlayer.isPlaying()) {
//
//					mPlayer.start();
//					mPlayer.setOnCompletionListener(new OnCompletionListener() {
//
//						@Override
//						public void onCompletion(MediaPlayer mp) {
//
//							btnPlayAudio
//									.setBackgroundResource(R.drawable.audio_play);
//							btnPlayAudio.setText(R.string.play);
////							mPlayer.reset();
////							mPlayer.release();
////							mPlayer = new MediaPlayer();
////							try {
////								mPlayer.setDataSource(audioUrl);
////								mPlayer.prepare();
////							} catch (IllegalArgumentException e) {
////								e.printStackTrace();
////
////							} catch (IllegalStateException e) {
////								e.printStackTrace();
////
////							} catch (IOException e) {
////								e.printStackTrace();
////							}
//
//							mPlayer.stop();
//						}
//					});
//					btnPlayAudio.setBackgroundResource(R.drawable.audio_stop);
//					btnPlayAudio.setText(R.string.stop);
//				}
//				
//				if (mPlayer.isPlaying()) {
//					mPlayer.stop();
//					mPlayer.reset();
//					mPlayer.release();
//					mPlayer = new MediaPlayer();
//					try {
//						mPlayer.setDataSource(audioUrl);
//						mPlayer.prepare();
//					} catch (IllegalArgumentException e) {
//						e.printStackTrace();
//						
//					} catch (IllegalStateException e) {
//						e.printStackTrace();
//
//					} catch (IOException e) {
//						e.printStackTrace();
//						
//					}
//					btnPlayAudio.setBackgroundResource(R.drawable.audio_play);
//					btnPlayAudio.setText(R.string.play);
//				}
//			} catch (Exception e) {
//				Utils.showToast(TaskDetailActivity.this,"Audio not found!");
//			}

			break;
		case R.id.btnBack:
			DebugLog.logd("Button back click");
			onBackPressed();
			finish();
			break;
		case R.id.btnCancel:
			DebugLog.logd("Button cancel click");
			onBackPressed();
			break;

		case R.id.btnRepLyForRequest:
			DebugLog.logd("Button reply click");
			Intent intent = new Intent(TaskDetailActivity.this,
					ReplyActivity.class);
			startActivityForResult(intent, REQUEST_CODE_TO_REPLYACTIVITY);
			break;

		case R.id.btnRate:
			DebugLog.logd("Button rate click");
			if (CacheData.getInstant().getCurrentTask() != null) {
				if (CacheData.getInstant().getCurrentTask().isRated() != true) {
					Utils.showToast(TaskDetailActivity.this, resource.getString(R.string.task_had_rated));
					return;
					
				} else if (CacheData.getInstant().getCurrentTask().getWorkflowState().equals("unassigned")) {
					Utils.showToast(TaskDetailActivity.this, resource.getString(R.string.task_unassign_msg));
					return;
					
				} else {
					Intent intentRateActitvity = new Intent(TaskDetailActivity.this, RateActivity.class);
					startActivity(intentRateActitvity);
				}

			}
			break;
		case R.id.imgAttachmentForScreenRequest:
			Intent intentAttackment = new Intent(TaskDetailActivity.this, ChoosePhotoExistActivity.class);
			intentAttackment.putExtra("activity", 2);
			startActivityForResult(intentAttackment, TASK_DETAIL_REQUEST_CODE);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(TaskActivity.taskActivity == null){
			Intent intent = new Intent(this, TaskActivity.class);
			startActivity(intent);
		}
		
		if (CacheData.getInstant().getListAttachment() != null) {
			CacheData.getInstant().getListAttachment().clear();
		}
		finish();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (ChoosePhotoExistActivity.isDone) {
			bindData();
		}
		ChoosePhotoExistActivity.isDone = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_TO_REPLYACTIVITY && resultCode == 1) {
			finish();
		}

		if (requestCode == TASK_DETAIL_REQUEST_CODE) {

			if (CacheData.getInstant().getListAttachment() == null) {
				imgAttachmentForScreenRequest.setImageResource(R.drawable.attach_gray);
			} else {
				if (CacheData.getInstant().getListAttachment().isEmpty()) {
					imgAttachmentForScreenRequest.setImageResource(R.drawable.attach_gray);
				} else {
					imgAttachmentForScreenRequest.setImageResource(R.drawable.attach);
				}
			}
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (showProcess != null) {
			Utils.dismissCurrentProgressDialog();
		}
	}

}
