package com.gly.collagelf.adapter;

import java.util.List;

import com.gly.collagelf.R;
import com.gly.collagelf.bean.Comment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CommentListViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<Comment> list;
	private LayoutInflater inflater; 

	public CommentListViewAdapter(Context context,List<Comment> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		if (v == null) {
			v = inflater.inflate(R.layout.act_comment, null);
		}
		TextView username = (TextView) v.findViewById(R.id.act_comment_username);
		TextView message = (TextView) v.findViewById(R.id.act_comment_message);
//		Log.i("MyTag", list.toString()+";"+list.size());
		username.setText(list.get(position).getCommentUserNick()+":");
		message.setText(list.get(position).getCommentInfos());
		return v;
	}

}
