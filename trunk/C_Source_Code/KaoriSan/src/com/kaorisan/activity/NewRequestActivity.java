package com.kaorisan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.beans.AttachmentTmp;
import com.kaorisan.beans.DashBoard;
import com.kaorisan.beans.Task;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dialog.TextRequestDialog;
import com.kaorisan.manager.TaskManager;

public class NewRequestActivity extends Activity {
	EditText edtTitle;
	EditText edtContent;
	TextView btnRepLyForRequest;

	ImageView btnAttach;
	ProgressDialog showProcess = null;

	public static boolean isSubmit = false;
	public static boolean isNewRequest = false;
	
	private final String HINT_TEXT_COLOR = "#c8c8c8";
	private final int ATTACHMENT_ACTIVITY_REQUEST_CODE = 100;

	String title = "";
	String requestContent = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_request);

		edtTitle = (EditText) findViewById(R.id.edtTitle);
		edtContent = (EditText) findViewById(R.id.edtContent);
		edtContent.setHintTextColor(Color.parseColor(HINT_TEXT_COLOR));

		btnAttach = (ImageView) findViewById(R.id.btnAttach);

		if (TaskActivity.isRecommendTask) {
			if (getIntent().getExtras().getString("title") != null) {
				title = getIntent().getExtras().getString("title");
			}
			if (getIntent().getExtras().getString("requestContent") != null) {
				requestContent = getIntent().getExtras().getString("requestContent");
			}
			DebugLog.logd(requestContent);
		}
		
		isNewRequest = true;
		
		edtTitle.setText(title);	
		edtContent.setText(requestContent);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_request, menu);
		return true;
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnSubmit:
			DebugLog.logd("Clicked summit");
			// String validate = validateInputs();
			// if(validate.length() == 0){
			// this.createNewTask();
			// }

			isSubmit = false;
			if (CacheData.getInstant().getDashBoard() != null) {
				if(CacheData.getInstant().getDashBoard().getAvailableTask() > 0){
					createNewTask();
				}else{
					Utils.showToast(NewRequestActivity.this, getResources().getString(R.string.cant_create_task_msg));
				}
			} else {

				Utils.showToast(NewRequestActivity.this, getResources().getString(R.string.cant_create_task_msg));
			}
			break;

		case R.id.btnCancel:
			DebugLog.logd("Button Cancel click");
			onBackPressed();
			isNewRequest = false;
			break;
		case R.id.btnAttach:
			DebugLog.logd("Button attach click");
			// if (CacheData.getInstant().getListAttachment().isEmpty()) {
			// Intent intentAttachPhoto = new Intent(this,
			// AttachmentActivity.class);
			// intentAttachPhoto.putExtra("activity", 1);
			// startActivityForResult(intentAttachPhoto,
			// ATTACHMENT_ACTIVITY_REQUEST_CODE);
			// } else {
			// Intent intentChoosePhotoExist = new Intent(this,
			// ChoosePhotoExistActivity.class);
			// intentChoosePhotoExist.putExtra("activity", 1);
			// startActivityForResult(intentChoosePhotoExist,
			// ATTACHMENT_ACTIVITY_REQUEST_CODE);
			// }

			Intent intentChoosePhotoExist = new Intent(this, ChoosePhotoExistActivity.class);
			intentChoosePhotoExist.putExtra("activity", 1);
			startActivityForResult(intentChoosePhotoExist, ATTACHMENT_ACTIVITY_REQUEST_CODE);

			break;
		}

	}

	public void createNewTask() {
		TaskManager manager = new TaskManager();
		showProcess = new ProgressDialog(this);
		showProcess.setCancelable(false);
		showProcess.setTitle(String.format(getResources().getString(R.string.creating_new_task), edtTitle.getText()));
		showProcess.show();
		String strTitle = "";
		if (edtTitle.getText().toString().length() > 0) {
			strTitle = edtTitle.getText().toString();
		} else {
			strTitle = getResources().getString(R.string.task_title) + " " + Utils.convertMillisecondsToDate(System.currentTimeMillis());
		}
		String strRequest = edtContent.getText().toString();
		Log.i("Phong chu", strTitle);
		manager.createNewTask(CacheData.getInstant().getTokenKaorisan(),
				strTitle, strRequest,String.valueOf(TextRequestDialog.isVip), new TaskManager.OnCreateNewTaskResult() {

					@Override
					public void onCreateNewTaskMethod(boolean isSuccess,
							Task task, String message) {
						if (isSuccess) {
							isSubmit = true;
							isNewRequest = false;
							// finish();
							showProcess.dismiss();
							showProcess = null;
							Log.i("ID Create", "" + task.getId());
							CacheData.getInstant().setCurrentTask(task);
							//CacheData.getInstant().getListTask().add(0, task);
							DashBoard dashBoard = new DashBoard();
							dashBoard.setOpenTask(CacheData.getInstant().getDashBoard().getOpenTask() + 1);
							dashBoard.setAvailableTask(CacheData.getInstant().getDashBoard().getAvailableTask() - 1);
							dashBoard.setClosedTask(CacheData.getInstant().getDashBoard().getClosedTask());
							CacheData.getInstant().setDashBoard(dashBoard);
							// Intent intent = new
							// Intent(NewRequestActivity.this,
							// TaskDetailActivity.class);
							// startActivity(intent);
							if (CacheData.getInstant().getListAttachmentTmps() .size() > 0){
								Log.i("Uploading", "Uploading");
								final TaskManager manager = new TaskManager();
								manager.totalNumber = CacheData.getInstant().getListAttachmentTmps().size();

								showProcess = Utils.createProgressDialog(String.format(getResources().getString(R.string.uploading_files_msg), manager.totalNumber), NewRequestActivity.this);

								showProcess.show();
								CacheData.getInstant().setCurrentProgressDialog(showProcess);
								for (final AttachmentTmp attachment : CacheData.getInstant().getListAttachmentTmps()) {
									
									manager.uploadPhotoToTask(CacheData.getInstant().getTokenKaorisan(), attachment.getFilePath(),
											String.valueOf(CacheData.getInstant().getCurrentTask().getId()), attachment, 
											new TaskManager.OnUploadPhotoToTaskResult() {

												@Override
												public void onUploadPhotoToTaskMethod(boolean isSuccess, String message) {
													int numberFilesFinish = manager.numberOfUploadSucess + manager.numberOfUploadFail;

													String content = String.format(getResources().getString(R.string.upload_photo_message), numberFilesFinish,  
															manager.totalNumber,  manager.numberOfUploadSucess,  manager.numberOfUploadFail);
													
													Utils.makeProgressUploadDownloadToast(NewRequestActivity.this, content);
													if (isSuccess) {
														Log.i("Count", "" + CacheData.getInstant().getListAttachmentTmps().size());
													}

													if (numberFilesFinish >= manager.totalNumber) {
														DebugLog.logd("dismiss");
														showProcess.dismiss();
														showProcess = null;
														Intent intent = new Intent(NewRequestActivity.this, TaskDetailActivity.class);
														startActivity(intent);
														finish();
													}
												}
											});
								}
							} else {
								finish();
							}
						} else {
							showProcess.dismiss();
							showProcess = null;
//							Toast.makeText(getApplicationContext(),
//									"Create request failed!", Toast.LENGTH_LONG)
//									.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
							Utils.showToast(NewRequestActivity.this, getResources().getString(R.string.create_new_request_failed));							
						}
						//
						// showProcess.dismiss();
					}

				});
	}

	public String validateInputs() {
		String result = "";
		return result;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ATTACHMENT_ACTIVITY_REQUEST_CODE) {
			DebugLog.logd("Result ok " + requestCode);
			if (CacheData.getInstant().getListAttachmentTmps() == null 
					|| CacheData.getInstant().getListAttachmentTmps().isEmpty()) {
				btnAttach.setImageResource(R.drawable.attach_gray);
			} else {
				btnAttach.setImageResource(R.drawable.attach);
			}

		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		isNewRequest = false;
		if (CacheData.getInstant().getListAttachmentTmps() != null) {
			CacheData.getInstant().getListAttachmentTmps().clear();
		}
		if (CacheData.getInstant().getListAttachment() != null) {
			CacheData.getInstant().getListAttachment().clear();
		}
		finish();
	}

}
