package com.kaorisan.activity;
import com.kaorisan.R;
import com.kaorisan.common.DebugLog;
import com.kaorisan.lazyload.ImageLoader;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

public class KaorisanPictureDetailActivity extends Activity{
	
	private String url;
	ImageView imageView;
	ProgressDialog showProcess;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kaorisanpicture_detail);
		imageView = (ImageView) findViewById(R.id.imagedetail);
		Drawable toRecycle= imageView.getDrawable();
		if (toRecycle != null) {
			((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
		}
//		imageView.setMaxWidth(getWidthScreen());
//		imageView.setMaxHeight(getHeightScreen());
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			url = extras.getString("url");
			Log.i("Url", url);
		}
		ImageLoader imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.DisplayImage(url, imageView);
		imageLoader.clearCache();
//		File cacheDir;
//		 ImageLoader imageLoader = ImageLoader.getInstance();
//		 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
//		        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"neongall");
//		    else
//		        cacheDir=getApplicationContext().getCacheDir();
//		    if(!cacheDir.exists())
//		        cacheDir.mkdirs();
//         DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
//         .showStubImage(R.drawable.logo)
//         .imageScaleType(ImageScaleType.EXACTLY)
//         .bitmapConfig(Bitmap.Config.RGB_565)
//         .displayer(new FadeInBitmapDisplayer(750))
//         .build();
//         //ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(displayImageOptions).build();
//		 
//		 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//		 .threadPriority(Thread.MAX_PRIORITY - 1)
//		 .memoryCache(new WeakMemoryCache())
//		 .denyCacheImageMultipleSizesInMemory().threadPoolSize(1)
//		 .discCacheExtraOptions(getWidthScreen(),getHeightScreen(),CompressFormat.PNG, 0,null)
////		 .discCache(new UnlimitedDiscCache(cacheDir))
//		 .defaultDisplayImageOptions(displayImageOptions)
//		 .tasksProcessingOrder(QueueProcessingType.LIFO)
//		 .build();
//		    
////		    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())
////		    .threadPriority(Thread.NORM_PRIORITY - 2)
////		    .denyCacheImageMultipleSizesInMemory()
////		    .discCacheFileNameGenerator(new Md5FileNameGenerator())
////		    .discCacheExtraOptions(getWidthScreen(),getHeightScreen(), CompressFormat.PNG, 0,null)
////		    .tasksProcessingOrder(QueueProcessingType.LIFO)
////		    .build();
//         imageLoader.init(config);
//         imageLoader.loadImage(url, new ImageLoadingListener() {
//			
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				showProcess = new ProgressDialog(KaorisanPictureDetailActivity.this);
//				showProcess.setCancelable(true);
//				showProcess.setTitle("Loading...");
//
//				showProcess.show();
//				
//			}
//			
//			@Override
//			public void onLoadingFailed(String imageUri, View view,
//					FailReason failReason) {
//				showProcess.dismiss();
//				showProcess = null;
//				
//			}
//			
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				imageView.setImageDrawable(new BitmapDrawable(loadedImage));
//				showProcess.dismiss();
//				showProcess = null;
//			}
//			
//			@Override
//			public void onLoadingCancelled(String imageUri, View view) {
//				showProcess.dismiss();
//				showProcess = null;
//				
//			}
//		});
         
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
	public void onBackPressed() {
		super.onBackPressed();
		System.gc();
		finish();
	}
}
