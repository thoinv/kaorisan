package com.kaorisan.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.kaorisan.R;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;

public class AudioRecordingActivity extends Activity {
	Button btnStopRecording;
	ProgressDialog progressDialog = null;
	TextView btnCreateAVoice;

	private MediaRecorder recorder = null;
	
	Resources resources = null;

	private MediaPlayer mPlayer = null;
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorderKaorisan";
	private static final String HEX_OF_BUTTON_CREATE_A_VOICE_REQUEST = "#ec4289";
	private String filePath = null;
	// private boolean isPlaying = false;

	private String AUDIO_FILE_NAME = "KaoriSanAudio";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_recording);
		
		resources = getResources();
		
		progressDialog = new ProgressDialog(this);

		btnCreateAVoice = (TextView) findViewById(R.id.btnCreatAVoice);
		btnCreateAVoice.setTextColor(Color.parseColor(HEX_OF_BUTTON_CREATE_A_VOICE_REQUEST));
		btnCreateAVoice.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.pink_button_voice_request, 0, 0);
		btnCreateAVoice.setText(resources.getString(R.string.stop_voice_recording));
		recorder = new MediaRecorder();
		startRecording();
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnCreatAVoice:
			DebugLog.logd("Button Stop click");
			stopRecording();
			progressDialog.setTitle(resources.getString(R.string.saving_msg));
			progressDialog.setCancelable(false);
			progressDialog.show();
			new Thread() {
				public void run() {
					
					try {
						sleep(3000);
					} catch (Exception e) {
						Log.e("tag", e.getMessage());
					}

					Intent intentAudioReview = new Intent(AudioRecordingActivity.this, AudioReviewActivity.class);
					intentAudioReview.putExtra("filePath", filePath);
					startActivity(intentAudioReview);
					finish();
					progressDialog.dismiss();
				}
			}.start();

			break;
		case R.id.btn_Re_Record:
			DebugLog.logd("Button Record click");
			break;

		case R.id.btnCancel:
			DebugLog.logd("Button Cancel clicked");
			stopRecording();
			finish();
			break;
		case R.id.btnSubmit:
			stopRecording();
			Intent intent = new Intent(AudioRecordingActivity.this, TaskDetailActivity.class);
			startActivity(intent);
			finish();
		default:
			break;
		}
	}

	private void startRecording() {
		Log.i("Play", "Play");
		try {
			startRecord();
		} catch (Exception e) {
			new Thread() {
				public void run() {
					try {
						sleep(3000);
					} catch (Exception e2) {
						Log.e("tag", e2.getMessage());
					}
					if (recorder != null) {
						recorder.release();
						recorder = new MediaRecorder();
					}
					try {
						startRecord();
					} catch (Exception e2) {
						finish();
					}
				}
			}.start();
		}
	}

	private void startRecord() {
		try {
			String tmpFile = getFilename();
			this.filePath = tmpFile;
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			if (Build.VERSION.SDK_INT >= 10) {
				recorder.setAudioSamplingRate(44100);
				recorder.setAudioEncodingBitRate(96000);
				MediaRecorder.getAudioSourceMax();
				recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			} else {
				recorder.setAudioSamplingRate(8000);
				recorder.setAudioEncodingBitRate(12200);
				MediaRecorder.getAudioSourceMax();
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			}
			recorder.setOutputFile(tmpFile);
			recorder.prepare();
			recorder.start();
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}
	}

	private String getFilename() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			String filepath = Environment.getExternalStorageDirectory().getPath();
			File file = new File(filepath, AUDIO_RECORDER_FOLDER);
			if (!file.exists()) {
				file.mkdirs();
			}
			return (file.getAbsolutePath() + "/" + AUDIO_FILE_NAME + ".m4a");
		} else {
			Utils.showToast(AudioRecordingActivity.this, resources.getString(R.string.need_external_storage_msg));
			finish();
		}
		return null;
	}

	private void stopRecording() {

		if (recorder != null) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
		}
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
	}

	// private void pauseRecording(){
	// Log.i("Record","Pause");
	// if(recorder != null){
	// recorder.release();
	// recorder = null;
	// }
	// }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		stopRecording();
		finish();
	}

}
