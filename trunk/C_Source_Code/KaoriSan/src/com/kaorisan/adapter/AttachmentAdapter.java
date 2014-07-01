package com.kaorisan.adapter;

import java.util.ArrayList;
import com.kaorisan.R;
import com.kaorisan.beans.Attachment;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AttachmentAdapter extends ArrayAdapter<Attachment> implements DialogInterface.OnClickListener {
	
	private LayoutInflater inflater;
	
	private AlertDialog.Builder confirmDelete;
	
	private OnDeleteAndView callBackDeleteAndView;
	
	private static int isDelete = 1;
	
	private static int isView = 2;
	
	public AttachmentAdapter(Context context, int textViewResourceId, ArrayList<Attachment> objects,OnDeleteAndView onChangeDataCallBack) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callBackDeleteAndView = onChangeDataCallBack;
		this.confirmDelete = new AlertDialog.Builder(context);
		this.confirmDelete.setMessage(R.string.label_confirm_delete_attachment);
		this.confirmDelete.setCancelable(false);
		this.confirmDelete.setNegativeButton(R.string.txt_no,this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Attachment attachment = this.getItem(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.attachment_item, null);
			holder.txtImageName = (TextView) convertView.findViewById(R.id.txtPhotoName);
			holder.imaButtonDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtImageName.setText(attachment.getFileName());
		if(attachment.getIsLocal() == 0 || attachment.getIsLocal() == 2){
			holder.imaButtonDelete.setVisibility(View.INVISIBLE);
		}else{
			holder.imaButtonDelete.setVisibility(View.VISIBLE);
			holder.imaButtonDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					confirmDelete.setPositiveButton(R.string.txt_yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									callBackDeleteAndView.onDeleteItemAndView(attachment, isDelete);

								}
							});
					confirmDelete.show();
					Log.i("Name", attachment.getFileName());
					
				}
			});
		}
		
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				callBackDeleteAndView.onDeleteItemAndView(attachment,isView);
			}
		};
		holder.txtImageName.setOnClickListener(onClickListener);
		return convertView;
	}
	
	public class ViewHolder{
		TextView txtImageName;
		ImageButton imaButtonDelete;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
		
	}
	
	public interface OnDeleteAndView {
		void onDeleteItemAndView(Attachment attachment, int type);
	}
}
