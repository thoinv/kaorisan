package com.kaorisan.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.beans.Attachment;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.manager.TaskManager;

public class ReplyActivity extends Activity {
	
	TextView txtTitle;
	EditText edtContent;
	TextView btnReply;
	TextView txtName;
	ImageView imageAttachment;
	ProgressDialog showProcess = null;
	
	private static int RESULT_CODE_BACK_TASKACTIVITY_DETAIL = 1;
	private static int REQUEST_CODE_REPLY = 2;
	public static boolean isReply = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply);
		if(CacheData.getInstant().getCurrentTask() != null){
			txtTitle = (TextView) findViewById(R.id.txtTitle);
			txtTitle.setText(CacheData.getInstant().getCurrentTask().getTitle());
			
		}
		edtContent = (EditText) findViewById(R.id.edtContent);
		imageAttachment = (ImageView) findViewById(R.id.imgAttachmentForScreenRequest);
		imageAttachment.setImageResource(R.drawable.attach_gray);
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnBack:
			DebugLog.logd("Button back click");
			onBackPressed();
			break;
			
		case R.id.btnCancel:
			DebugLog.logd("Button cancel click");
			onBackPressed();
			break;
			
		case R.id.btnSubmit:
			String txtBody = edtContent.getText().toString();
			Log.i("Body", txtBody);
			if(!Utils.isNullOrEmpty(txtBody)){
				Log.i("Test", "Content is required!");
				showProcess = Utils.createProgressDialog(getResources().getString(R.string.creating_new_reply), ReplyActivity.this);
				showProcess.show();
				TaskManager manager = new TaskManager();
				manager.creatNewRepLy(CacheData.getInstant().getTokenKaorisan(),txtBody,String.valueOf(CacheData.getInstant().getCurrentTask().getId()),
						new TaskManager.OnCreateNewRepLyResult() {
					
					@Override
					public void onCreateNewRepLyMethod(boolean isSuccess, String message) {
						if(isSuccess){
							showProcess.dismiss();
							setResult(RESULT_CODE_BACK_TASKACTIVITY_DETAIL);
							finish();
							isReply = true;
						}
						else{
							showProcess.dismiss();
							Utils.showToast(ReplyActivity.this, getResources().getString(R.string.create_new_request_failed));

						}
					}
				});
			}
			else{
				Utils.showToast(ReplyActivity.this, getResources().getString(R.string.content_required_msg));
			}
			break;
			
		case R.id.btnRepLyForRequest:
//			DebugLog.logd("Button reply click");
//			Intent intent = new Intent(ReplyActivity.this,
//					AttachmentActivity.class);
//			startActivity(intent);
			break;

		case R.id.btnRate:
//			DebugLog.logd("Button rate click");
//			Intent intentRateActitvity = new Intent(ReplyActivity.this, RateActivity.class);
//			startActivity(intentRateActitvity);
//			break;
			DebugLog.logd("Button rate click");
			if (CacheData.getInstant().getCurrentTask() != null) {
				if (CacheData.getInstant().getCurrentTask().isRated() != true) {
					Utils.showToast(ReplyActivity.this, getResources().getString(R.string.task_had_rated));
					return;
					
				} else if (CacheData.getInstant().getCurrentTask().getWorkflowState().equals("unassigned")) {
					Utils.showToast(ReplyActivity.this, getResources().getString(R.string.task_unassign_msg));
					return;
					
				} else {
					Intent intentRateActitvity = new Intent(ReplyActivity.this, RateActivity.class);
					startActivity(intentRateActitvity);
				}

			}
			break;
		case R.id.imgAttachmentForScreenRequest:
				Log.i("Listattachment","List not null");
				Intent intentAttackment = new Intent(ReplyActivity.this,ChoosePhotoExistActivity.class);
				intentAttackment.putExtra("activity",3);
				startActivityForResult(intentAttackment, REQUEST_CODE_REPLY);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ArrayList<Attachment> listAttachments = CacheData.getInstant().getListAttachment();
		
		if (listAttachments != null) {
			for (int i = listAttachments.size() - 1; i >= 0; i--) {
				if (listAttachments.get(i).getTmp() == 1) {
					listAttachments.remove(i);
				} else {
					break;
				}
			}
		}
		if (CacheData.getInstant().getListAttachmentTmps() != null) {
			CacheData.getInstant().getListAttachmentTmps().clear();
		}
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_CODE_REPLY) {
			if (CacheData.getInstant().getListAttachment() == null) {
				imageAttachment.setImageResource(R.drawable.attach_gray);
			} else {
				if (CacheData.getInstant().getListAttachment().isEmpty()) {
					imageAttachment.setImageResource(R.drawable.attach_gray);
				} else {
					imageAttachment.setImageResource(R.drawable.attach);
				}
			}
		}
	}
	
	

}
