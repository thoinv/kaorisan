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
import com.kaorisan.beans.Task;
import com.kaorisan.common.Utils;

public class TaskAdapter extends ArrayAdapter<Task> {

	private Context context;
	List<Task> objects;
	private int txtId;

	private final String NUMBER_PREFIX = "0";

	private final String EMPTY_STRING = "";

	private final String TASK_COMPLETED = "completed";

	private final String TASK_PROCESSING = "processing";

	private final String TASK_UNASSIGNED = "unassigned";

	private final String TEXT_STATUS_TASK_COMPLETE = "COMPLETED";

	private final String TEXT_STATUS_TASK_PROCESSING = "IN PROCESSING";

	private final String TEXT_STATUS_TASK_TASK_UNASSIGNED = "UNASSIGNED";

	private final String HEX_OF_COLOR_TEXT_STATUS_PROCESSING = "#f5ad39";

	private final String HEX_OF_COLOR_TEXT_STATUS_COMPLETE = "#cccccc";

	private final String HEX_OF_COLOR_TEXT_STATUS_UNASSIGNED = "#929293";

	public TaskAdapter(Context context, int txtId,  List<Task> objects) {
		super(context, txtId, objects);
		this.context = context;
		this.objects= objects;
		this.txtId= txtId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(txtId, null);
		}
		
		Task task = objects.get(position);
		if (objects != null) {
			TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
//			ImageView imageStatus ;
			TextView txtStatus  = (TextView) convertView.findViewById(R.id.txtTaskStatus);
			TextView txtTimeStatus = (TextView) convertView.findViewById(R.id.txtLastState);
			TextView btnReplies = (TextView) convertView.findViewById(R.id.numberOfResponse);
		

		txtTitle.setText(task.getTitle());
		btnReplies.setText(task.getRepliesCount() + EMPTY_STRING);

		if (task.getWorkflowState().equals(TASK_COMPLETED)) {
			txtTimeStatus.setText(Utils.getRelativeTime(context, Long.valueOf(task.getUpdateAt() != null ? 
					task.getUpdateAt() : task.getCreatedAt())));
			txtStatus.setText(TEXT_STATUS_TASK_COMPLETE);
			txtStatus.setTextColor(Color.parseColor(HEX_OF_COLOR_TEXT_STATUS_COMPLETE));
			txtStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.task_complete, 0, 0, 0);

		} else if (task.getWorkflowState().equals(TASK_PROCESSING)) {
			txtTimeStatus.setText(Utils.getRelativeTime(context, Long.valueOf(task.getUpdateAt() != null ?
					task.getUpdateAt() : task.getCreatedAt())));
			txtStatus.setText(TEXT_STATUS_TASK_PROCESSING);
			txtStatus.setTextColor(Color.parseColor(HEX_OF_COLOR_TEXT_STATUS_PROCESSING));
			txtStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.task_in_process, 0, 0, 0);

		} else if (task.getWorkflowState().equals(TASK_UNASSIGNED)) {
			txtTimeStatus.setText(Utils.getRelativeTime(context, Long.valueOf(
					task.getUpdateAt() != null ? task.getUpdateAt() : task.getCreatedAt())));
			txtStatus.setText(TEXT_STATUS_TASK_TASK_UNASSIGNED);
			txtStatus.setTextColor(Color.parseColor(HEX_OF_COLOR_TEXT_STATUS_UNASSIGNED));
			txtStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.task_unassign, 0, 0, 0);
		}

		btnReplies.setText(task.getRepliesCount() < 10 ? NUMBER_PREFIX + task.getRepliesCount() : task.getRepliesCount()	+ EMPTY_STRING);
		}
			return convertView;
	}


}
