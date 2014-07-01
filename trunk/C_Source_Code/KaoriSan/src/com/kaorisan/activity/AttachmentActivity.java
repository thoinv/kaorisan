package com.kaorisan.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.kaorisan.R;
import com.kaorisan.beans.Attachment;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dialog.TakeImageTypeDialog;

public class AttachmentActivity extends Activity {
	
//	private static int REQUEST_CODE_CAMERA = 1;
	public Uri fileUri;
	
	TakeImageTypeDialog takeImageTypeDialog;
	
	private static int fromActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attachment);
		
		takeImageTypeDialog = new TakeImageTypeDialog(AttachmentActivity.this);
		takeImageTypeDialog.setCanceledOnTouchOutside(true);
		takeImageTypeDialog.show();
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			fromActivity = extras.getInt("activity");
		}
	}

	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnCancel:
			DebugLog.logd("Button Cancel click");
			onBackPressed();
			break;

		case R.id.btnAdd:
			DebugLog.logd("Button Add Click");
			takeImageTypeDialog = new TakeImageTypeDialog(
					this);
			takeImageTypeDialog.setCanceledOnTouchOutside(true);
			takeImageTypeDialog.show();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
			Log.i("test",""+ takeImageTypeDialog.fileUri.toString());
			String filePath = takeImageTypeDialog.fileUri.toString();
			
			if(resultCode == Activity.RESULT_OK){
				Utils.galleryAddPhoto(takeImageTypeDialog.fileUri, getApplicationContext());
				Attachment attach = new Attachment();
				attach.setFilePath(filePath.substring(6));
				attach.setFileName(filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length()));
				CacheData.getInstant().getListAttachment().add(attach);
				
				Intent intentChooseExit = new Intent(AttachmentActivity.this, ChoosePhotoExistActivity.class);
				intentChooseExit.putExtra("activity", fromActivity);
				startActivity(intentChooseExit);
				takeImageTypeDialog.dismiss();
				takeImageTypeDialog = null;
				finish();
			}
			break;
			
		case 2:
			if(data != null){
				String path = getRealPathFromURI(data.getData());
				Log.i("File", path);
				Attachment attachment = new Attachment();
				attachment.setFileName(path.substring(path.lastIndexOf('/') + 1, path.length()));
				attachment.setFilePath(path);
				CacheData.getInstant().getListAttachment().add(attachment);
				
				Intent intent = new Intent(AttachmentActivity.this, ChoosePhotoExistActivity.class);
				intent.putExtra("activity", fromActivity);
				startActivity(intent);
				takeImageTypeDialog.dismiss();
				takeImageTypeDialog = null;
				finish();
			}
			break;
		}
	}
	
	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public Uri getFileUri() {
		return fileUri;
	}

	public void setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
	}

	@Override
	protected void onStop() {
		super.onPause();
		if(takeImageTypeDialog != null){
			if(takeImageTypeDialog.isShowing()){
				takeImageTypeDialog.cancel();
			}
		}
	}
	
	
}
