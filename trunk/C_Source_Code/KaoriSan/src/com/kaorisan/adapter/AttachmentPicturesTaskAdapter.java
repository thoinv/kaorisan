package com.kaorisan.adapter;

import java.util.ArrayList;
import com.kaorisan.R;
import com.kaorisan.beans.Picture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AttachmentPicturesTaskAdapter extends ArrayAdapter<Picture> {
	
	private LayoutInflater inflater;
	public AttachmentPicturesTaskAdapter(Context context, int textViewResourceId, ArrayList<Picture> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Picture picture = this.getItem(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.attachment_item, null);
			holder.txtImageName = (TextView) convertView.findViewById(R.id.txtPhotoName);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtImageName.setText(picture.getFileName());
		return convertView;
	}
	
	public class ViewHolder{
		TextView txtImageName;
	}
}
