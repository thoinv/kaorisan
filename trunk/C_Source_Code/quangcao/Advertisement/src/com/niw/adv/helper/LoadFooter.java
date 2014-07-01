package com.niw.adv.helper;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.niw.adv.object.AdObj;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

public class LoadFooter extends AsyncTask<String, Integer, String> {

	private static final String BANNER_LINK_CACHE = "bannerlinkcache";
	private Context context;
	private ImageView image;
	private ImageLoader imageLoader;
	private SharedPreferences spref;

	public LoadFooter(Context context, ImageView imageView) {
		this.context = context;
		this.image = imageView;

		spref=context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	public void loadCache() {
		List<Bitmap> lbm=MemoryCacheUtil.findCachedBitmapsForImageUri(spref.getString(BANNER_LINK_CACHE, ""), ImageLoader.getInstance().getMemoryCache());
		image.setImageBitmap(lbm.get(0));
	}
	
	@Override
	protected String doInBackground(String... url) {
		JsonFuncs jsonFuncs = new JsonFuncs(context, url[0]);
		return jsonFuncs.getJson();
	}

	@Override
	protected void onPostExecute(String result) {
		try {
			JSONArray array = new JSONArray(result);
			// array.getJSONObject(0).getString(AdObj.LINK_COLUMN);
			Log.d(context.getPackageName(), array.getJSONObject(0).getString(AdObj.LINK_COLUMN));
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
					.build();

			imageLoader.displayImage(array.getJSONObject(0).getString(AdObj.LINK_COLUMN), image, options);
			SharedPreferences.Editor editor=spref.edit();
			editor.putString(BANNER_LINK_CACHE, array.getJSONObject(0).getString(AdObj.LINK_COLUMN));
			editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
