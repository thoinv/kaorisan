package com.kaorisan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import com.kaorisan.R;
import com.kaorisan.common.DebugLog;
import com.kaorisan.common.Utils;

public class TakeImageTypeDialog extends Dialog {

	Activity activity;
	Button btnChoosePhotoExist;
	Button btnTakePhoto;
	private static int REQUEST_CODE_CAMERA = 1;
	public Uri fileUri;

	public TakeImageTypeDialog(Activity context) {
		super(context);
		this.activity = context;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		setContentView(R.layout.pop_up_take_image_type);

		btnChoosePhotoExist = (Button) findViewById(R.id.btnChooseExisting);
		btnChoosePhotoExist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				DebugLog.logd("Button Choose Exist click");
//				Intent intentChooseExist = new Intent(getContext(),
//						LocalGalleryActivity.class);
			    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	             intent.setType("image/*");
	             activity.startActivityForResult(intent,2);
	             dismiss();
			}
		});

		btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
		btnTakePhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				DebugLog.logd("Button Take Photo click");
//				Intent intentTakePhoto = new Intent(getContext(),
//						CameraActivity.class);
//				activity.startActivity(intentTakePhoto);
//				activity.finish();
				Intent intentChooseExist = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = Utils.getOutputMediaFileUri(1);
				intentChooseExist.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				activity.startActivityForResult(intentChooseExist, REQUEST_CODE_CAMERA);	
				dismiss();

			}
		});
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		dismiss();
	}

}