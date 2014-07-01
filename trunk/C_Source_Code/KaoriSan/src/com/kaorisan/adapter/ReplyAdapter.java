package com.kaorisan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaorisan.R;
import com.kaorisan.beans.Reply;
import com.kaorisan.common.Utils;

public class ReplyAdapter extends ArrayAdapter<Reply> {

	private String ASSISTANT_BACKGROUND_COLOR = "#eb4289";
	private String BACKGROUND_COLOR_DEFAULT = "#696969";
	private LayoutInflater inflater;

	private final String ASSISANT = "Kaori-san";
	private Context context = null;
	private final String SENDER = "You";

	public ReplyAdapter(Context context, int textViewResourceId, List<Reply> replies) {
		super(context, textViewResourceId, replies);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Reply reply = this.getItem(position);
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.reply_item, null);
			holder.txtUserType = (TextView) convertView.findViewById(R.id.txtUser);
			holder.txtTime = (TextView) convertView.findViewById(R.id.timeCreate);
			holder.txtReplyContent = (TextView) convertView.findViewById(R.id.txtReplyContent);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtUserType.setText(reply.isAssistant() ? ASSISANT : SENDER);

		holder.txtUserType.setBackgroundColor(Color.parseColor(reply.isAssistant() ? ASSISTANT_BACKGROUND_COLOR : BACKGROUND_COLOR_DEFAULT));

		holder.txtTime.setBackgroundColor(Color.parseColor(reply.isAssistant() ? ASSISTANT_BACKGROUND_COLOR : BACKGROUND_COLOR_DEFAULT));

		holder.txtTime.setText(Utils.getRelativeTime(context, Long.valueOf(reply.getCreatedAt())));
		holder.txtReplyContent.setText(reply.getBody());

		return convertView;

	}

	public class ViewHolder {
		TextView txtTime;
		TextView txtReplyContent;
		TextView txtUserType;
	}

}
