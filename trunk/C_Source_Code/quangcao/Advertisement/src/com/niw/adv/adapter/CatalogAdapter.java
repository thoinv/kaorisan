package com.niw.adv.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adv.activity.R;
import com.niw.adv.object.CatalogObj;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class CatalogAdapter extends ArrayAdapter<CatalogObj> {

	private ImageLoader imageLoader;

	public CatalogAdapter(Context context, int resource,
			List<CatalogObj> objects) {
		super(context, resource, objects);
		mContext = context;
		rowID = resource;
		list = objects;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}

	private Context mContext;
	private int rowID;
	private List<CatalogObj> list;
	private LayoutInflater inflater;

	@Override
	public CatalogObj getItem(int position) {
		return list.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (LinearLayout) inflater.inflate(rowID, null);
		}

		// set value to item 
		CatalogObj obj = (CatalogObj) getItem(position);
		
		TextView title = (TextView) convertView.findViewById(R.id.base_title);
		title.setText(obj.getName());

		ImageView icon = (ImageView) convertView.findViewById(R.id.base_icon);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				
				// option to round image
				.displayer(new RoundedBitmapDisplayer(20))
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

		TextView records = (TextView) convertView.findViewById(R.id.base_item_records);
		records.setText(Integer.toString(obj.getRecords()));
		
		imageLoader.displayImage(obj.getIcon(), icon, options);
		return convertView;
	}

}
