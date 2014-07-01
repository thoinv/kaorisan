package com.kaorisan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.beans.CommonRequest;

public class CommonRequestAdapter extends BaseExpandableListAdapter {
	private static final String PARENT_BACKGROUND_RECOMMEND_TASK_LIST_CLICKED = "#ec4289";
	private static final String PARENT_TEXT_COLOR_RECOMMEND_TASK_LIST_CLICKED = "#ffffff";
	private static final String PARENT_TEXT_COLOR_RECOMMEND_TASK_LIST = "#000000";
	private static final String PARENT_BACKGROUND_RECOMMEND_TASK_LIST = "#ffffff";
	private LayoutInflater inflater;
	private ArrayList<CommonRequest> mParent;

	public CommonRequestAdapter(Context context, ArrayList<CommonRequest> parent) {
		mParent = parent;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getGroupCount() {
		return mParent.size();
	}

	@Override
	public int getChildrenCount(int i) {
		return mParent.get(i).getArrayChildren().size();
	}

	@Override
	public Object getGroup(int i) {
		return mParent.get(i).getTitle();
	}

	@Override
	public Object getChild(int i, int i1) {
		return mParent.get(i).getArrayChildren().get(i1);
	}

	@Override
	public long getGroupId(int i) {
		return i;
	}

	@Override
	public long getChildId(int i, int i1) {
		return i1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
		
		
		ViewHolder holder = new ViewHolder();
		holder.groupPosition = i;

		if (view == null) {
			view = inflater.inflate(R.layout.common_request_group_item, viewGroup, false);
		}

		TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);

		textView.setText(getGroup(i).toString());
		
		if (isExpanded) {
			textView.setTextColor(Color.parseColor(PARENT_TEXT_COLOR_RECOMMEND_TASK_LIST_CLICKED));
			textView.setBackgroundColor(Color.parseColor(PARENT_BACKGROUND_RECOMMEND_TASK_LIST_CLICKED));
		  }else {
			textView.setTextColor(Color.parseColor(PARENT_TEXT_COLOR_RECOMMEND_TASK_LIST));
			textView.setBackgroundColor(Color.parseColor(PARENT_BACKGROUND_RECOMMEND_TASK_LIST));
		  }
		
		view.setTag(holder);
		
		return view;
	}

	@Override
	public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

		ViewHolder holder = new ViewHolder();
		holder.childPosition = i1;
		holder.groupPosition = i;

		if (view == null) {
			view = inflater.inflate(R.layout.common_request_item, viewGroup, false);
		}

		TextView textView = (TextView) view.findViewById(R.id.list_item_text_child);

		textView.setText(mParent.get(i).getArrayChildren().get(i1));

		view.setTag(holder);

		return view;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

		super.registerDataSetObserver(observer);
	}

	protected class ViewHolder {
		protected int childPosition;
		protected int groupPosition;
		protected Button button;
	}
}
