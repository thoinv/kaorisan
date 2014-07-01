package com.kaorisan.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.adapter.AttachmentAdapter;
import com.kaorisan.beans.Attachment;
import com.kaorisan.beans.AttachmentTmp;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;
import com.kaorisan.dataLayer.CacheData;
import com.kaorisan.dialog.TakeImageTypeDialog;
import com.kaorisan.lazyload.ImageLoader;
import com.kaorisan.manager.TaskManager;

public class ChoosePhotoExistActivity extends Activity implements
		DialogInterface.OnClickListener{

	private AttachmentAdapter attachMentAdapter;
	private ListView listview;
	private int fromActivity;

	ImageLoader imageLoader;

	ProgressDialog showProcess = null;

	TakeImageTypeDialog takeImageTypeDialog;

	private AlertDialog.Builder confirmDelete;

	TextView btnSubmit;
	TextView btnCancel;
	TextView txtTemp;
	// TextView btnDelete;
	LinearLayout attachmentWrapLayout;
	// private final String PREFIX_PHOTO = "photo";
	// private final String DOT = ".";
	// private int sizeListAttachment;
	int sizeListAttachmentTmp = 0;
	public static boolean isDone = false;
	
	private static int isDelete = 1;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_photo_exist);

		imageLoader = new ImageLoader(this);
		this.confirmDelete = new AlertDialog.Builder(this);
		this.confirmDelete.setMessage(R.string.label_confirm_delete_attachment);
		this.confirmDelete.setCancelable(false);
		confirmDelete.setNegativeButton(R.string.txt_no, this);
		attachmentWrapLayout = (LinearLayout) findViewById(R.id.attachmentWrapLayout);

		txtTemp = (TextView) findViewById(R.id.txtTemp);
		listview = (ListView) findViewById(R.id.listAttachment);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			fromActivity = extras.getInt("activity");
			Log.i("ChooseActivity", "" + fromActivity);
		}
		// btnDelete = (TextView) findViewById(R.id.btnDelete);
		// btnDelete.setVisibility(View.INVISIBLE);
		// if(fromActivity == 1){
		// btnDelete.setVisibility(View.VISIBLE);
		// Log.i("ChooseActivityTest", "" + fromActivity);
		// }else{
		// btnDelete.setVisibility(View.INVISIBLE);
		// }
		btnSubmit = (TextView) findViewById(R.id.btnSubmit);
		btnSubmit.setText(getResources().getString(R.string.txt_done));

		btnSubmit.setVisibility(View.INVISIBLE);

		loadListView();

		// listview.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Attachment attachment = (Attachment) parent
		// .getItemAtPosition(position);
		// try {
		// Intent intent = new Intent();
		// if (attachment.getType().equals("image")) {
		// if (attachment.isLocal() == true) {
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.parse("file://" +
		// attachment.getFilePath()), "image/*");
		//
		// } else {
		// intent.setClass(ChoosePhotoExistActivity.this,
		// KaorisanPictureDetailActivity.class);
		// intent.putExtra("url",
		// Utils.replaceHttpsToHttp(attachment.getFilePath()));
		// }
		//
		// } else {
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.parse(Utils.replaceHttpsToHttp(attachment.getFilePath())),
		// "audio/*");
		// }
		// startActivity(intent);
		//
		// } catch (Exception e) {
		// DebugLog.loge("StartIntent" + e.getMessage());
		// }
		// }
		// });
	}

	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// Log.i("Restart", "Vao day 0");
	// if (NewRequestActivity.isNewRequest == false) {
	// if (CacheData.getInstant().getListAttachmentTmps() != null) {
	// if (CacheData.getInstant().getListAttachmentTmps().isEmpty()) {
	// btnSubmit.setVisibility(View.INVISIBLE);
	// } else {
	// btnSubmit.setVisibility(View.VISIBLE);
	// }
	// } else {
	// btnSubmit.setVisibility(View.INVISIBLE);
	// }
	// Log.i("Restart", "Vao day 1");
	// } else {
	// btnSubmit.setVisibility(View.INVISIBLE);
	// if(fromActivity == 1){
	// Log.i("Restart", "Vao day 2");
	// btnDelete.setVisibility(View.VISIBLE);
	// }
	// }
	// }

	private void loadListView() {
		if (fromActivity == 1) {

			btnSubmit.setVisibility(View.INVISIBLE);

			btnCancel = (TextView) findViewById(R.id.btnCancel);
//			if (!NewRequestActivity.isNewRequest) {
//				btnCancel
//						.setText(getResources().getString(R.string.txt_cancel));
//			} else {
//				btnCancel.setText(getResources().getString(R.string.txt_done));
//			}

			if (CacheData.getInstant().getListAttachment().isEmpty()) {
				Log.i("Log3", "Log3");
				takeImageTypeDialog = new TakeImageTypeDialog(
						ChoosePhotoExistActivity.this);
				takeImageTypeDialog.setCanceledOnTouchOutside(true);
				takeImageTypeDialog.show();
			} else {
				listview.setVisibility(View.VISIBLE);
				txtTemp.setVisibility(View.INVISIBLE);
			}
			attachMentAdapter = new AttachmentAdapter(
					ChoosePhotoExistActivity.this,R.layout.attachment_item,
					CacheData.getInstant().getListAttachment(),
					new AttachmentAdapter.OnDeleteAndView() {

						@Override
						public void onDeleteItemAndView(Attachment attachment,
								int type) {
							deleteAndViewAttachment(attachment,type);
						}
					});
			listview.setAdapter(attachMentAdapter);

		} else if (fromActivity == 2 || fromActivity == 3) {
			if (CacheData.getInstant().getListAttachment().isEmpty()) {
				Log.i("Log1", "Log1");
				takeImageTypeDialog = new TakeImageTypeDialog(
						ChoosePhotoExistActivity.this);
				takeImageTypeDialog.show();
				attachMentAdapter = new AttachmentAdapter(
						ChoosePhotoExistActivity.this,R.layout.attachment_item, new ArrayList<Attachment>(),
						new AttachmentAdapter.OnDeleteAndView() {

							@Override
							public void onDeleteItemAndView(
									Attachment attachment, int type) {
								deleteAndViewAttachment(attachment, type);
							}
						});
				Log.i("UpdateListView", "UpdateListView");
				listview.setAdapter(attachMentAdapter);

			} else {
				Log.i("Log2", "Log2");
				txtTemp.setVisibility(View.INVISIBLE);
				listview.setVisibility(View.VISIBLE);
				attachMentAdapter = new AttachmentAdapter(
						ChoosePhotoExistActivity.this,R.layout.attachment_item, CacheData.getInstant()
								.getListAttachment(),
						new AttachmentAdapter.OnDeleteAndView() {

							@Override
							public void onDeleteItemAndView(
									Attachment attachment, int type) {
								deleteAndViewAttachment(attachment, type);
							}
						});
				listview.setAdapter(attachMentAdapter);

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.choose_photo_exist, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ArrayList<Attachment> listAttachments = CacheData.getInstant()
				.getListAttachment();

		if (fromActivity == 2 || fromActivity == 1 || fromActivity == 3) {
			if (listAttachments != null) {
//				for (int i = listAttachments.size() - 1; i >= 0; i--) {
//					if (listAttachments.get(i).getIsLocal() == 1 && listAttachments.get(i).isCamera() == false && listAttachments.get(i).isResize() == true ) {
//						Utils.deleteFile(listAttachments.get(i).getFilePath());
//					}
//				}
				File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "KaorisanTmp");
				if(mediaStorageDir.exists()){
					for(File file : mediaStorageDir.listFiles()){
						file.delete();
					}
				}
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
		}
		finish();
	}

	@SuppressLint("ShowToast")
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnCancel:
			DebugLog.logd("Button Cancel click");
			onBackPressed();
			break;
		case R.id.btnAttach:
			DebugLog.logd("Button Attachment click");
			if(takeImageTypeDialog !=null){
				if(takeImageTypeDialog.isShowing()){
					takeImageTypeDialog.dismiss();
				}
				takeImageTypeDialog = null;
			}
			takeImageTypeDialog = new TakeImageTypeDialog(ChoosePhotoExistActivity.this);
			takeImageTypeDialog.setCanceledOnTouchOutside(true);
			takeImageTypeDialog.show();
			break;
		// case R.id.btnDelete:
		// DebugLog.logd("Button Delete click");
		// confirmDelete.setPositiveButton(R.string.txt_yes,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// CacheData.getInstant().getListAttachment().clear();
		// CacheData.getInstant().getListAttachmentTmps().clear();
		// btnDelete.setVisibility(View.INVISIBLE);
		// attachMentAdapter.notifyDataSetChanged();
		// }
		// });
		// confirmDelete.show();
		// break;
		case R.id.btnSubmit:
		if(fromActivity == 2 || fromActivity == 3){
			if (CacheData.getInstant().getListAttachmentTmps() == null) {
				return;
			} else if (CacheData.getInstant().getListAttachmentTmps().size() > 0) {
				Log.i("Uploading", "Uploading");
				final TaskManager manager = new TaskManager();
				manager.totalNumber = CacheData.getInstant()
						.getListAttachmentTmps().size();

				showProcess = Utils.createProgressDialog(String.format(
						getResources().getString(R.string.uploading_files_msg),
						manager.totalNumber), ChoosePhotoExistActivity.this);

				showProcess.show();
				CacheData.getInstant().setCurrentProgressDialog(showProcess);
				for (final AttachmentTmp attachment : CacheData.getInstant()
						.getListAttachmentTmps()) {

					manager.uploadPhotoToTask(CacheData.getInstant()
							.getTokenKaorisan(), attachment.getFilePath(),
							String.valueOf(CacheData.getInstant()
									.getCurrentTask().getId()), attachment,

							new TaskManager.OnUploadPhotoToTaskResult() {

								@SuppressLint("DefaultLocale")
								@Override
								public void onUploadPhotoToTaskMethod(
										boolean isSuccess, String message) {

									if (message != null) {
										Utils.showToast(
												ChoosePhotoExistActivity.this,
												message);
									} else {
										int numberFilesFinish = manager.numberOfUploadSucess
												+ manager.numberOfUploadFail;

										String content = String
												.format(getResources()
														.getString(
																R.string.upload_photo_message),
														numberFilesFinish,
														manager.totalNumber,
														manager.numberOfUploadSucess,
														manager.numberOfUploadFail);

										Utils.showToast(
												ChoosePhotoExistActivity.this,
												content);

										if (isSuccess) {
											Log.i("Count",
													""
															+ CacheData
																	.getInstant()
																	.getListAttachmentTmps()
																	.size());
										}

										if (numberFilesFinish >= manager.totalNumber) {
											DebugLog.logd("dismiss");
											Utils.dismissCurrentProgressDialog();
											isDone = true;
											finish();
										}
										// attachMentAdapter = new
										// AttachmentAdapter(
										// getApplicationContext(),
										// R.layout.attachment_item, CacheData
										// .getInstant()
										// .getListAttachment());
										// listview.setAdapter(attachMentAdapter);
									}
								}
							});
				}
			}
			 }
			 else if(fromActivity == 1){
				 finish();
			 }
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				//Log.i("test", "" + takeImageTypeDialog.fileUri.toString());
				Uri uri = Utils.galleryAddPictureToTakePhoto(
						takeImageTypeDialog.fileUri, getApplicationContext());
				Utils.galleryAddPhoto(uri, getApplicationContext());
				if (uri == null) {
					return;
				}
				String filePath = uri.toString();
				// String url = getRealPathFromURI(uri);
				Log.i("Urlactiviti", filePath);
				// if (!(sizeListAttachmentTmp > 2)) {
				sizeListAttachmentTmp++;

				Attachment attach = new Attachment();
				attach.setFilePath(filePath.substring(6));
				attach.setFileName(filePath.substring(
						filePath.lastIndexOf('/') + 1, filePath.length()));
				attach.setType("image");
				attach.setIsLocal(1);
				attach.setTmp(1);
				attach.setId(0);
				attach.setCamera(true);
				attach.setCreateAt("");
				attach.setResize(false);
				AttachmentTmp attachmentTmp = new AttachmentTmp();
				attachmentTmp.setFilePath(filePath.substring(6));
				attachmentTmp.setFileName(filePath.substring(
						filePath.lastIndexOf('/') + 1, filePath.length()));
				attachmentTmp.setCamera(true);
				attachmentTmp.setIsLocal(1);
				attachmentTmp.setResize(false);
				CacheData.getInstant().getListAttachmentTmps()
						.add(attachmentTmp);
				CacheData.getInstant().getListAttachment().add(attach);
				attachMentAdapter.notifyDataSetChanged();

				// } else {
				// Toast.makeText(getApplicationContext(), "Max 3 file",
				// Toast.LENGTH_LONG).show();
				// }
			}
			//takeImageTypeDialog.dismiss();
			if(takeImageTypeDialog !=null){
				if(takeImageTypeDialog.isShowing()){
					takeImageTypeDialog.dismiss();
				}
				takeImageTypeDialog = null;
			}
			break;
		case 2:
			if (data != null) {
//				Uri uri = Utils.galleryAddPictureToLocal(data.getData(),
//						getApplicationContext());
				String url = Utils.galleryAddPictureToLocal(data.getData(),
				getApplicationContext());
				//Utils.galleryAddPhoto(uri, getApplicationContext());
				if (url == null) {
					return;
				}
				//String filePath = uri.toString();
				// String url = getRealPathFromURI(uri);
				// String path = getRealPathFromURI(data.getData());
				Log.i("UriLocal", "" + data.getData().toString());
				
				Attachment attachment = new Attachment();
				attachment.setFileName(url.substring(
						url.lastIndexOf('/') + 1, url.length()));
				attachment.setFilePath(url);
				attachment.setType("image");
				attachment.setIsLocal(1);
				attachment.setTmp(1);
				attachment.setId(0);
				attachment.setCamera(false);
				attachment.setCreateAt("");
				if(Utils.checkIsResize(url)){
					attachment.setResize(true);
				}else{
					attachment.setResize(false);
				}
				// if (!(sizeListAttachmentTmp > 2)) {
				AttachmentTmp attachmentTmp = new AttachmentTmp();
				attachmentTmp.setFilePath(url);
				attachmentTmp.setType("image");
				attachmentTmp.setFileName(attachment.getFileName());
				attachmentTmp.setCamera(false);
				attachmentTmp.setIsLocal(1);
				attachmentTmp.setResize(attachment.isResize());
				CacheData.getInstant().getListAttachmentTmps()
						.add(attachmentTmp);
				sizeListAttachmentTmp++;
				CacheData.getInstant().getListAttachment().add(attachment);
				attachMentAdapter.notifyDataSetChanged();
				// } else {
				// Toast.makeText(getApplicationContext(), "Max 3 file",
				// Toast.LENGTH_LONG).show();
				// DebugLog.logd("Max 3 file");
				// }
				// attachMentAdapter.add(attachment);
				// attachMentAdapter.notifyDataSetChanged();
				//takeImageTypeDialog.dismiss();
			}
			if(takeImageTypeDialog !=null){
				if(takeImageTypeDialog.isShowing()){
					takeImageTypeDialog.dismiss();
				}
				takeImageTypeDialog = null;
			}
			break;
		}
		loadListView();
	}

	public int getWidthScreen() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		DebugLog.logd("Width: " + width);
		return width;
	}

	public int getHeightScreen() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.heightPixels;
		DebugLog.logd("Width: " + width);
		return width;
	}

	@Override
	public void onClick(DialogInterface dialog, int arg1) {
		dialog.cancel();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("Resume", "Vao day 0");
//		if (NewRequestActivity.isNewRequest == false) {
//			if (CacheData.getInstant().getListAttachmentTmps() != null) {
//				if (CacheData.getInstant().getListAttachmentTmps().isEmpty()) {
//					btnSubmit.setVisibility(View.INVISIBLE);
//				} else {
//					btnSubmit.setVisibility(View.VISIBLE);
//				}
//			} else {
//				btnSubmit.setVisibility(View.INVISIBLE);
//			}
//			Log.i("Resume", "Vao day 1");
//		} else {
//			btnSubmit.setVisibility(View.INVISIBLE);
//			// if(fromActivity == 1){
//			// Log.i("Resume", "Vao day 2");
//			// if(!CacheData.getInstant().getListAttachmentTmps().isEmpty()){
//			// btnDelete.setVisibility(View.VISIBLE);
//			// }else{
//			// btnDelete.setVisibility(View.INVISIBLE);
//			// }
//			// }else{
//			// btnDelete.setVisibility(View.INVISIBLE);
//			// }
//		}
		
		if (CacheData.getInstant().getListAttachmentTmps() != null) {
			if (CacheData.getInstant().getListAttachmentTmps().isEmpty()) {
				btnSubmit.setVisibility(View.INVISIBLE);
			} else {
				btnSubmit.setVisibility(View.VISIBLE);
			}
		} else {
			btnSubmit.setVisibility(View.INVISIBLE);
		}
	}
	private void deleteAndViewAttachment(Attachment attachment, int type){
		if (type == isDelete) {
			CacheData.getInstant().getListAttachment()
					.remove(attachment);
			if(attachment.isCamera() == false && attachment.getIsLocal() == 1 && attachment.isResize() == true){
				Utils.deleteFile(attachment.getFilePath());
			}
			Log.i("Size Tmp mot", ""+CacheData.getInstant().getListAttachmentTmps().size());
//			for (AttachmentTmp attachmentTmp : CacheData.getInstant().getListAttachmentTmps()) {
//				if(attachmentTmp.getFilePath().equals(attachment.getFilePath())){
//					CacheData.getInstant().getListAttachmentTmps().remove(attachmentTmp);
//				}
//				break;
//			}
			for(int i = 0 ; i < CacheData.getInstant().getListAttachmentTmps().size(); i++){
				if(CacheData.getInstant().getListAttachmentTmps().get(i).getFilePath().equals(attachment.getFilePath())){
					CacheData.getInstant().getListAttachmentTmps().remove(i);
					break;
				}
			}
			Log.i("Size Tmp hai", ""+CacheData.getInstant().getListAttachmentTmps().size());
			if(fromActivity == 1 && CacheData.getInstant().getListAttachment().isEmpty()){
				txtTemp.setVisibility(View.VISIBLE);
				btnSubmit.setVisibility(View.INVISIBLE);
			}else if(fromActivity == 2 && CacheData.getInstant().getListAttachment().isEmpty()){
				txtTemp.setVisibility(View.VISIBLE);
				btnSubmit.setVisibility(View.INVISIBLE);
			}else if(fromActivity == 2 && CacheData.getInstant().getListAttachmentTmps().isEmpty()){
				btnSubmit.setVisibility(View.INVISIBLE);
			}else if(fromActivity == 3 && CacheData.getInstant().getListAttachment().isEmpty()){
				txtTemp.setVisibility(View.VISIBLE);
				btnSubmit.setVisibility(View.INVISIBLE);
			}else if(fromActivity == 3 && CacheData.getInstant().getListAttachmentTmps().isEmpty()){
				btnSubmit.setVisibility(View.INVISIBLE);
			}
			
			Log.i("UpdateListView", "UpdateListView");
			listview.setAdapter(attachMentAdapter);
			listview.setSelection(listview.getAdapter().getCount() - 1);
		} else {
			try {
				Intent intent = new Intent();
				if (attachment.getType().equals("image")) {
					if (attachment.getIsLocal() == 1 || attachment.getIsLocal() == 2) {
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(
								Uri.parse("file://"
										+ attachment
												.getFilePath()),
								"image/*");

					} else {
						intent.setClass(
								ChoosePhotoExistActivity.this,
								KaorisanPictureDetailActivity.class);
						intent.putExtra(
								"url",
								Utils.replaceHttpsToHttp(attachment
										.getFilePath()));
					}

				} else {
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(Utils
							.replaceHttpsToHttp(attachment
									.getFilePath())),
							"audio/*");
				}
				startActivity(intent);

			} catch (Exception e) {
				DebugLog.loge("StartIntent"
						+ e.getMessage());
			}
		}
	}
	// private String getRealPathFromURI(Uri contentUri) {
	// String[] proj = { MediaStore.Images.Media.DATA };
	// Cursor cursor = managedQuery(contentUri, proj, null, null, null);
	// int column_index = cursor
	// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	// cursor.moveToFirst();
	// return cursor.getString(column_index);
	// }

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		Log.i("Ondestroy", "Ondestroy");
//		ArrayList<Attachment> listAttachments = CacheData.getInstant()
//				.getListAttachment();
//		if (listAttachments != null) {
////			for (int i = listAttachments.size() - 1; i >= 0; i--) {
////				if (listAttachments.get(i).getIsLocal() == 1 && listAttachments.get(i).isCamera() == false && listAttachments.get(i).isResize() == true ) {
////					Utils.deleteFile(listAttachments.get(i).getFilePath());
////				}
////			}
//			File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "KaorisanTmp");
//			if(mediaStorageDir.exists()){
//				for(File file : mediaStorageDir.listFiles()){
//					file.delete();
//				}
//			}
//			for (int i = listAttachments.size() - 1; i >= 0; i--) {
//				if (listAttachments.get(i).getTmp() == 1) {
//					listAttachments.remove(i);
//				} else {
//					break;
//				}
//			}
//		}
//		if (CacheData.getInstant().getListAttachmentTmps() != null) {
//			CacheData.getInstant().getListAttachmentTmps().clear();
//		}
//	}
	
	
}
