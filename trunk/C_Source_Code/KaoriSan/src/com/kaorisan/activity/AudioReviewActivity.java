package com.kaorisan.activity;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.beans.Attachment;
import com.kaorisan.beans.Task;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.manager.TaskManager;
import com.kaorisan.manager.TaskManager.OnUploadPhotoToTaskResult;

public class AudioReviewActivity extends Activity implements OnClickListener {
	
	Button btnPlay;
	Button btnReRecording;
	TextView btnCreateAVoice;
	TextView btnSubmit;
	TextView btnCreateAText;
	TextView btnCancel;

	public static boolean isSubmit = false;
	private MediaPlayer mPlayer = null;
	ProgressDialog showProcess = null;
	private String filePath = null;
	
	Resources resources = null;

	private final String EMPTY_STRING = "";
	private final String SPACE = " ";

	boolean isClickReCording = false;

	// private boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_review);

		resources = getResources();
		
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(this);
		btnCreateAVoice = (TextView) findViewById(R.id.btnCreatAVoice);
		btnCreateAVoice.setClickable(false);

		btnReRecording = (Button) findViewById(R.id.btn_Re_Record);
		btnReRecording.setOnClickListener(this);

		btnCreateAText = (TextView) findViewById(R.id.btnCreatAText);
		btnCreateAText.setClickable(false);

		btnSubmit = (TextView) findViewById(R.id.btnSubmit);
		btnCancel = (TextView) findViewById(R.id.btnCancel);

		Bundle extras = getIntent().getExtras();
		filePath = extras.getString("filePath");
		Log.i("FilePath", filePath);

		mPlayer = new MediaPlayer();
		try {
			mPlayer.reset();
			mPlayer.setDataSource(filePath);
			mPlayer.prepare();
		} catch (Exception e) {
			Log.e("Player Exception", "could'nt play audio file");
		}
	}

	// private void pauseRecording(){
	// Log.i("Record","Pause");
	// if(recorder != null){
	// recorder.release();
	// recorder = null;
	// }
	// }

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnCancel:
			DebugLog.logd("Button Cancel click");
			onBackPressed();
			break;

		case R.id.btnSubmit:
			DebugLog.logd("Button Submit click");
			if (CacheData.getInstant().getDashBoard().getAvailableTask() > 0) {
				createNewTask();
			} else {
				Utils.showToast(AudioReviewActivity.this, resources.getString(R.string.cant_create_task_msg));
			}
			// upload audio and create task
		}
	}

	public void createNewTask() {

		final TaskManager manager = new TaskManager();
		showProcess = new ProgressDialog(this);
		showProcess.setCancelable(false);
		showProcess.setTitle(resources.getString(R.string.creating_new_task));
		showProcess.show();
		CacheData.getInstant().setCurrentProgressDialog(showProcess);
		final String strTitle = getResources().getString(R.string.txt_task)	+ SPACE
				+ Utils.convertMillisecondsToDate(System.currentTimeMillis());

		manager.createNewTask(CacheData.getInstant().getTokenKaorisan(), strTitle, EMPTY_STRING,String.valueOf(false), new TaskManager.OnCreateNewTaskResult() {

					@Override
					public void onCreateNewTaskMethod(boolean isSuccess, final Task task, String message) {

						if (isSuccess) {
							isSubmit = true;
							CacheData.getInstant().getDashBoard().setAvailableTask(CacheData.getInstant().getDashBoard().getAvailableTask() - 1);
							btnSubmit.setClickable(false);
							btnCancel.setClickable(false);
							
							manager.uploadAudioToTask(CacheData.getInstant().getTokenKaorisan(), filePath, task.getId()
									+ EMPTY_STRING, new Attachment(), new OnUploadPhotoToTaskResult() {

										@Override
										public void onUploadPhotoToTaskMethod(boolean isSuccess, String message) {
											CacheData.getInstant().setCurrentTask(task);
											Intent intent = new Intent(AudioReviewActivity.this, TaskDetailActivity.class);
											// intent.putExtra("audioUrl",
											// filePath);
											// intent.putExtra("title",
											// strTitle);
											// intent.putExtra("message",
											// "Please see attachment for request instructions");

											startActivity(intent);
											finish();
											DebugLog.logd("Create task successfylly!");
											showProcess.dismiss();
										}
									});

						} else {
							Utils.showToast(AudioReviewActivity.this, resources.getString(R.string.create_request_failed_msg));
							showProcess.dismiss();
						}

						// showProcess.dismiss();
					}

				});

	}

	// private void startPlaying() {
	//
	// if (filePath != null) {
	// if (mPlayer.isPlaying()) {
	// mPlayer.pause();
	// } else {
	// mPlayer.start();
	// }
	// }
	//
	// }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnPlay:
			DebugLog.logd("Button Play click");
			if (!mPlayer.isPlaying()) {
				mPlayer.start();
				mPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						btnPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_icon, 0, 0, 0);
						btnPlay.setText(R.string.play_recording);
						mPlayer.reset();
						mPlayer.release();
						mPlayer = new MediaPlayer();
						try {
							mPlayer.setDataSource(filePath);
							mPlayer.prepare();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							
						} catch (IllegalStateException e) {
							e.printStackTrace();
							
						} catch (IOException e) {
							e.printStackTrace();
							
						}

					}
				});
				btnPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stop_icon, 0, 0, 0);
				btnPlay.setText(R.string.stop);
				
			} else {
				mPlayer.stop();
				mPlayer.reset();
				mPlayer.release();
				mPlayer = new MediaPlayer();
				
				try {
					mPlayer.setDataSource(filePath);
					mPlayer.prepare();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					
				} catch (IllegalStateException e) {
					e.printStackTrace();
					
				} catch (IOException e) {
					e.printStackTrace();
					
				}
				btnPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_icon, 0, 0, 0);
				btnPlay.setText(R.string.play_recording);
			}
			break;
			
		case R.id.btn_Re_Record:
			DebugLog.logd("Button Re-Recording click");

			if (!isClickReCording) {
				isClickReCording = true;
				try {
					if (mPlayer.isPlaying()) {
						mPlayer.pause();
						mPlayer.release();

					} else {
						mPlayer.release();
					}
				} catch (Exception e) {
					DebugLog.loge(e.getMessage());
				}

				Intent intent = new Intent(this, AudioRecordingActivity.class);
				startActivity(intent);
				finish();
			}
			break;

		}

	}

}
