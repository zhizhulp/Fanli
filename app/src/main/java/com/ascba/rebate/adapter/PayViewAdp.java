package com.ascba.rebate.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;

/**
/* 功能说明:最新动态Adapter
 * @author 作者：jarvisT
 * @date 创建日期：2015-1-27 下午10:12:18
 */
public class PayViewAdp extends BaseAdapter {

	private Context context;
	private ArrayList<Map<String, String>> list;

	public PayViewAdp(Context context, ArrayList<Map<String, String>> list) {
		this.context = context;
		this.list=list;


	}

	@Override
	public int getCount() {

		return list.size();

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final HolderView holderView;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_gride, null);
			holderView.btnKey = (TextView) convertView.findViewById(R.id.btn_keys);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.btnKey.setText(list.get(position).get("name"));
		if(position == 9){
			holderView.btnKey.setBackgroundResource(R.drawable.selector_key_del);
			holderView.btnKey.setEnabled(false);
		}
		if(position == 11){
			holderView.btnKey.setBackgroundResource(R.drawable.selector_key_del);
		}
		return convertView;
	}


	private class HolderView {
		ImageView img;
		TextView btnKey,time,money,status;
	}
}
