package com.kaorisan.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.kaorisan.R;
import com.kaorisan.dataLayer.CacheData;

public class Utils {
	// private static final String EMPTY_STRING = "";
	public static final int MEDIA_TYPE_IMAGE = 1;

	public interface OnBitmapResult {
		void onBitmapResult(boolean isSuccess, String message, Bitmap bitmap);
	}

	public static ProgressDialog createProgressDialog(String message, Context context) {
		ProgressDialog showProcess = new ProgressDialog(context);
		showProcess.setMessage(message);
		showProcess.setCancelable(false);
		showProcess.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.my_progress_indeterminate));
		return showProcess;
	}

	public static void makeProgressUploadDownloadToast(Context context, String content) {
		Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setMargin(0, 0.08f);
		toast.show();
	}

	public static void showToast(final Context context, final String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT); 
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	public static void showToast(final Activity activity, final String message) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
				toast.setDuration(0);
				toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		});
	}

	public static void resetCacheData() {
		if(CacheData.getInstant().getListAttachment() != null){
			CacheData.getInstant().getListAttachment().clear();
		}
		
		if(CacheData.getInstant().getListAttachmentAudio() != null){
			CacheData.getInstant().getListAttachmentAudio().clear();
		}
		
		if(CacheData.getInstant().getListAttachmentPicture() != null){
			CacheData.getInstant().getListAttachmentPicture().clear();
		}
		
		if(CacheData.getInstant().getListAttachmentTmps() != null){
			CacheData.getInstant().getListAttachmentTmps().clear();
		}
		
		if(CacheData.getInstant().getRecommendTasks() != null){
			CacheData.getInstant().getRecommendTasks().clear();
		}
		CacheData.getInstant().setListTask(null);
		CacheData.getInstant().setCurrentUser(null);
		CacheData.getInstant().setTokenKaorisan("");
		CacheData.getInstant().setDashBoard(null);
		CacheData.getInstant().setAccountDashBoard(null);
		CacheData.getInstant().setCurrentTask(null);
	}

	public static String getFileExtension(String fileName) {
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}

		return extension;
	}

	public static void dismissCurrentProgressDialog() {
		if (CacheData.getInstant().getCurrentProgressDialog() != null) {
			CacheData.getInstant().getCurrentProgressDialog().dismiss();
			CacheData.getInstant().setCurrentProgressDialog(null);
		}
	}

	public static boolean fileExist(String filePath) {
		if (!filePath.isEmpty()) {
			File file = new File(filePath);
			return file.exists();
		}
		return false;
	}

	public static void deleteFile(String filePath) {
		if (fileExist(filePath)) {
			File file = new File(filePath);
			file.delete();
		}
	}

	public static void loadBitmap(String link,
			final OnBitmapResult onBitmapResult) {
		new AsyncTask<String, Void, Bitmap>() {
			Bitmap bitmap;
			InputStream in;

			@Override
			protected Bitmap doInBackground(String... params) {
				try {
					in = new URL(params[0]).openStream();
					bitmap = BitmapFactory.decodeStream(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (result == null) {
					onBitmapResult.onBitmapResult(false, null, null);
				} else {
					onBitmapResult.onBitmapResult(true, null, result);
				}
			}
		}.execute(link);
	}

	public static String convertHTML(String url, int sizeWidth) {
		return "<br /><img align=\"bottom\" width=\"" + sizeWidth + "\" src=\""
				+ url + "\"/><br /><br />";
	}

	public static void getAvatarFaceBook(String link,
			final OnBitmapResult onBitmapResult) {
		new AsyncTask<String, Void, Bitmap>() {
			Bitmap bitmap;
			URL urlAvatar = null;

			@Override
			protected Bitmap doInBackground(String... params) {
				try {
					urlAvatar = new URL(params[0]);
					bitmap = BitmapFactory.decodeStream(urlAvatar.openConnection().getInputStream());
					// in = new URL(params[0]).openStream();
					// bitmap = BitmapFactory.decodeStream(in);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (result == null) {
					onBitmapResult.onBitmapResult(false, null, null);
				} else {
					onBitmapResult.onBitmapResult(true, null, result);
				}
			}
		}.execute(link);
	}

	public static String convertMillisecondsToDate(long milliseconds) {
		Date date = new Date(milliseconds);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd 'at' hh:mma",
				Locale.US);
		return sdf.format(date);
	}

	public static Camera.Size getBestSupportPictureSizeWithSurface(
			Camera.Parameters parameters, int width, int height) {
		Camera.Size bestSize = null;
		int minrecommendSize = 0;
		int maxrecommendSize = 10000000;
		double recommendRatio = (double) height / width;
		double ratioDistance = 1000;

		DebugLog.logd("Start getbest : width " + width + " height: " + height);

		List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();
		bestSize = sizeList.get(0);

		// find the best
		for (Size size : sizeList) {
			if (size.width * size.height > minrecommendSize
					&& size.width * size.height < maxrecommendSize) {
				double currentRatio = (double) size.width / size.height;
				double currentRatioDistance = Math.abs(currentRatio - recommendRatio);
				if (currentRatioDistance < ratioDistance) {
					if (ratioDistance < 0.01) {
						if (size.width * size.height > bestSize.width * bestSize.height) {
							bestSize = size;
							ratioDistance = currentRatioDistance;
							DebugLog.logd("Try distance :" + ratioDistance);
							DebugLog.logd("Try getBestPictureSize : width " + size.width + " height: " + size.height);
						}
					} else {
						ratioDistance = currentRatioDistance;
						bestSize = size;
						DebugLog.logd("Try distance :" + ratioDistance);
						DebugLog.logd("Try getBestPictureSize : width " + size.width + " height: " + size.height);
					}
				} else if (currentRatioDistance == ratioDistance) {
					if (size.width * size.height > bestSize.width * bestSize.height) {
						bestSize = size;
						DebugLog.logd("Try distance :" + ratioDistance);
						DebugLog.logd("Try getBestPictureSize : width " + size.width + " height: " + size.height);
					}
				}
			}
		}

		DebugLog.logd("AAA getBestPictureSize : width " + bestSize.width + " height: " + bestSize.height);

		return bestSize;
	}

	public static Camera.Size getBestPreviewSize(Camera.Parameters parameters, Camera.Size bestPictureSize) {
		Camera.Size bestSize = null;
		List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
		double minDistance = 1000;

		for (Size size : sizeList) {
			double intRatioPreview = (double) size.width / size.height;
			double intRatioPicture = (double) bestPictureSize.width / bestPictureSize.height;
			if (Math.abs(intRatioPicture - intRatioPreview) < minDistance) {
				minDistance = Math.abs(intRatioPicture - intRatioPreview);
				bestSize = size;
			}
		}

		DebugLog.logd("AAA getBestPreviewSize : width " + bestSize.width + " height: " + bestSize.height);

		return bestSize;
	}

	public static Bitmap rotateBitmapAndResize(Bitmap sourceBitmap, int rotate,	int width, int height) {
		Matrix rotateLeft = new Matrix();
		rotateLeft.preRotate(rotate);

		float scale_w = ((float) width) / sourceBitmap.getWidth();
		float scale_h = ((float) height) / sourceBitmap.getHeight();

		rotateLeft.preScale(scale_w, scale_h);
		Bitmap destinationBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), rotateLeft, false);
		return destinationBitmap;
	}

	public static boolean checkFileType(String fileType) {
		if (fileType.equals(".aac")) {
			return false;
		} else if (fileType.equals(".mp3")) {
			return false;
		} else if (fileType.equals(".m4a")) {
			return false;
		} else if (fileType.equals(".flv")) {
			return false;
		} else if (fileType.equals(".wav")) {
			return false;
		} else if (fileType.equals(".webma")) {
			return false;
		} else if (fileType.equals(".webmv")) {
			return false;
		} else if (fileType.equals(".ogv")) {
			return false;
		} else if (fileType.equals(".oga")) {
			return false;
		} else if (fileType.equals(".fla")) {
			return false;
		} else if (fileType.equals(".mp4")) {
			return false;
		} else if (fileType.equals(".webm")) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkInternetConnect(final Context cxt) {
		final ConnectivityManager conMgr = (ConnectivityManager) cxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
			return true;
		else
			return false;
	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		}
		if ("".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static String getRelativeTime(Context context, long time) {
		
		TimeUtils timeAgo = new TimeUtils(context);
		return timeAgo.timeAgo(time * 1000L);
	}
	
	public static boolean checkIsResize(String path){
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), "KaorisanTmp");
		Log.i("Folder",""+mediaStorageDir.toString());
		Log.i("url", ""+path);
		String url = path.substring(1,path.lastIndexOf('/'));
		Log.i("url", ""+url);
		if(url.equals(mediaStorageDir.toString())){
			return true;
		}
		return false;
	}
	@SuppressLint("SimpleDateFormat")
	public static File getOutputMediaFile(int type) {
		// File mediaStorageDir = new
		// File(Environment.getExternalStoragePublicDirectory(
		// Environment.DIRECTORY_PICTURES), "MyCameraApp");
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), "Kaorisan");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			// mediaFile = new File(mediaStorageDir.getPath() + File.separator +
			// "IMG_"+ timeStamp + ".jpg");
			String imagePath = "Kaorisan/Kaorisan_" + timeStamp + ".jpg";
			mediaFile = new File(Environment.getExternalStorageDirectory(), imagePath);
			// galleryAddPic(imagePath, context);
			return mediaFile;
		} else {
			return null;
		}
	}

	public static Uri getOutputMediaFileUri(int paramInt) {
		return Uri.fromFile(getOutputMediaFile(paramInt));
	}

	public static void galleryAddPhoto(Uri uri, Context context) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(uri);
		context.sendBroadcast(mediaScanIntent);
	}
		
	public static Uri galleryAddPictureToTakePhoto(Uri uri, Context context){
		try {
			Bitmap mBitmap;
			Uri mUri;
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
	        if(options.outHeight > 800 || options.outWidth > 800){
		        options.inSampleSize = calculateInSampleSize(options, 800, 600);
		        options.inJustDecodeBounds = false;
		        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null, options);
		        //Bitmap bitmap = BitmapFactory.decodeFile(uri.toString().substring(6), options);
		        Matrix matrix = new Matrix();
		        matrix.postRotate(getCameraPhotoOrientation(context, uri));
		        mBitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), matrix, false);
				mUri = writeData(mBitmap,90, context);
				deleteFile(uri.toString().substring(6));
				mBitmap.recycle();
				mBitmap = null;
		        bitmap.recycle();
		        bitmap = null;
				return mUri;
	        }else{
	        	Bitmap bitmap = Media.getBitmap(context.getContentResolver(), uri);
				mUri = writeData(bitmap,100, context);
				bitmap.recycle();
				bitmap = null;
				deleteFile(uri.toString().substring(6));
				return mUri;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String galleryAddPictureToLocal(Uri uri, Context context){
		try {
			Bitmap mBitmap;
			Uri mUri;
			String url;
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
	        if(options.outHeight > 800 || options.outWidth > 800){
		        options.inSampleSize = calculateInSampleSize(options, 800, 600);
		        options.inJustDecodeBounds = false;
		        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null, options);
		        Matrix matrix = new Matrix();
		        matrix.postRotate(getPhotoLocalOrientation(context, uri));
		        mBitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), matrix, false);
		        mUri = writeDataToLocal(mBitmap,90, context);
				mBitmap.recycle();
				mBitmap = null;
		        bitmap.recycle();
		        bitmap = null;
				url = mUri.toString().substring(6);
				Log.i("UriTest1", ""+mUri.toString());
				return url;
	        }else{
				url = getRealPathFromURI(uri, context);
				return url;
	        }

	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight){
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
//	public static Uri writeData(Bitmap bitmap,int quatily,Context context){
//		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//		.format(new Date());
//		String title = "Kaorisan_" + timeStamp + ".jpg";
//		ContentValues values = new ContentValues();
//		values.put(Images.Media.TITLE, title);
//		values.put(Images.Media.DISPLAY_NAME,title);
//		values.put(Images.Media.DESCRIPTION, "Kaorisan");
//		values.put(Images.Media.MIME_TYPE, "image/jpg");
//		values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, timeStamp);
//
//		Uri uri =context.getContentResolver().insert(
//				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//		try {
//			OutputStream imageOut = context.getContentResolver().openOutputStream(
//					uri);
//			try {
//				//imageOut.write(data);
//				bitmap.compress(CompressFormat.JPEG,quatily, imageOut);
//				imageOut.flush();
//				imageOut.close();
//				return uri;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		return null;
//	}
	
	public static String getRealPathFromURI(Uri contentUri,Context context) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	@SuppressLint("SimpleDateFormat")
	public static Uri writeData(Bitmap bitmap,int quatily,Context context){
		
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "KaorisanApp");
		Log.i("Folder",""+mediaStorageDir.toString());
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
			// mediaFile = new File(mediaStorageDir.getPath() + File.separator +
			// "IMG_"+ timeStamp + ".jpg");
			String imagePath = "KaorisanApp/Kaorisan_" + timeStamp + ".jpg";
			mediaFile = new File(Environment.getExternalStorageDirectory(),	imagePath);
			Uri uri = Uri.fromFile(mediaFile);
		
		try {
			OutputStream imageOut = context.getContentResolver().openOutputStream(uri);
			try {
				//imageOut.write(data);
				bitmap.compress(CompressFormat.JPEG,quatily, imageOut);
				imageOut.flush();
				imageOut.close();
				return uri;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	@SuppressLint("SimpleDateFormat")
	public static Uri writeDataToLocal(Bitmap bitmap,int quatily,Context context){
		
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "KaorisanTmp");
		Log.i("Folder",""+mediaStorageDir.toString());
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
			// mediaFile = new File(mediaStorageDir.getPath() + File.separator +
			// "IMG_"+ timeStamp + ".jpg");
			String imagePath = "KaorisanTmp/Kaorisan_" + timeStamp + ".jpg";
			mediaFile = new File(Environment.getExternalStorageDirectory(),	imagePath);
			Uri uri = Uri.fromFile(mediaFile);
		
		try {
			OutputStream imageOut = context.getContentResolver().openOutputStream(uri);
			try {
				//imageOut.write(data);
				bitmap.compress(CompressFormat.JPEG,quatily, imageOut);
				imageOut.flush();
				imageOut.close();
				return uri;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	public static int getPhotoLocalOrientation(Context context, Uri uri){
	    int rotate = 0;
	    String imagePath = getRealPathFromURI(uri, context);
	    try {
	        File imageFile = new File(imagePath);

	        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
	        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

	        switch (orientation) {
	        case ExifInterface.ORIENTATION_ROTATE_270:
	            rotate = 270;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_180:
	            rotate = 180;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_90:
	            rotate = 90;
	            break;
	        }

	        Log.i("RotateImage", "Exif orientation: " + orientation);
	        Log.i("RotateImage", "Rotate value: " + rotate);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return rotate;
	}
	
	public static int getCameraPhotoOrientation(Context context, Uri uri){
	    int rotate = 0;
	    String imagePath = uri.toString().substring(6);
	    try {
	        File imageFile = new File(imagePath);

	        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
	        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

	        switch (orientation) {
	        case ExifInterface.ORIENTATION_ROTATE_270:
	            rotate = 270;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_180:
	            rotate = 180;
	            break;
	        case ExifInterface.ORIENTATION_ROTATE_90:
	            rotate = 90;
	            break;
	        }

	        Log.i("RotateImage", "Exif orientation: " + orientation);
	        Log.i("RotateImage", "Rotate value: " + rotate);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return rotate;
	}
	
	public static String replaceHttpsToHttp(String https){
		String http = https.replace("https", "http");
		return http;
	}
	public enum Social {
		FACEBOOK, GOOGLE
	}

	public enum CameraOrientation {
		Portrait, Lanscape, PortraitInvert, LanscapeInvert,
	}
}
