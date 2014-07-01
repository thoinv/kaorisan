package com.niw.adv.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adv.activity.R;
import com.niw.adv.object.DetailObj;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

public class DetailAdapter extends ArrayAdapter<DetailObj> {

	private Context mContext;
	private int rowID;
	private List<DetailObj> list;
	private LayoutInflater inflater;
	private String mIconPathDistributor;

	public DetailAdapter(Context context, int resource, List<DetailObj> objects,String iconPathDistributor) {
		super(context, resource, objects);
		mContext = context;
		rowID = resource;
		list = objects;
		mIconPathDistributor=iconPathDistributor;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public DetailObj getItem(int position) {
		return list.get(position);
	}

	@SuppressLint("SimpleDateFormat") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (LinearLayout) inflater.inflate(rowID, null);
		}
		
		// set value to item
		DetailObj obj=(DetailObj)getItem(position);
		
		TextView title=(TextView)convertView.findViewById(R.id.detail_title);
		title.setText(obj.getTitle());
		
		//parse String -> date
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c=Calendar.getInstance();
        try {
            c.setTime(format.parse(obj.getEmpire()));
        } catch (Exception e) {
        	Log.e(mContext.getPackageName(), "can't parse String -> Date");
        }
        Log.d(mContext.getPackageName(),"parse result:"+ c.get(Calendar.HOUR_OF_DAY)+"h"+c.get(Calendar.MINUTE)+" Ngày "+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR));
		TextView expireDate=(TextView)convertView.findViewById(R.id.detail_empire);
		expireDate.setText("Đến hết: "+c.get(Calendar.HOUR_OF_DAY)+"h"+c.get(Calendar.MINUTE)+" Ngày "+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR));
		
		ImageView icon=(ImageView)convertView.findViewById(R.id.detail_iconDistributor);
		List<Bitmap> lbm=MemoryCacheUtil.findCachedBitmapsForImageUri(mIconPathDistributor, ImageLoader.getInstance().getMemoryCache());
		icon.setImageBitmap(lbm.get(0));
		
		return convertView;
	}
}
