package com.kaorisan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.beans.RecommendTask;

public class RecommendTaskAdapter extends ArrayAdapter<RecommendTask> {
	
	private LayoutInflater inflater;
	private ArrayList<RecommendTask> listRecommendTasks;
	public RecommendTaskAdapter(Context context, int textViewResourceId, ArrayList<RecommendTask> objects) {
		super(context, textViewResourceId, objects);
		this.listRecommendTasks = objects;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final RecommendTask recommendTask = this.getItem(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.recommend_task_item, null);
			holder.txtRecommendTask = (TextView) convertView.findViewById(R.id.txtRecommendTask);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtRecommendTask.setText(recommendTask.getTitle());
		return convertView;
	}
	
 
	
	public class ViewHolder{
		TextView txtRecommendTask;
	}
	
	@Override
	public int getCount() {
		if(listRecommendTasks.size() >= 2){
			return 2;
		}else{
			return this.listRecommendTasks.size();
		}
	}
}
