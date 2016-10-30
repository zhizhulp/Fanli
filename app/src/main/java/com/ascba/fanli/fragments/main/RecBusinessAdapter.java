package com.ascba.fanli.fragments.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.fanli.R;
import com.ascba.fanli.beans.Business;

import java.util.List;

/**
 * 主页商家列表适配器
 */

public class RecBusinessAdapter extends BaseAdapter {
    private List<Business> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public RecBusinessAdapter(List<Business> mList, Context mContext) {
        this.mList = mList;
        this.mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.main_bussiness_list_item,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder= ((ViewHolder) convertView.getTag());
        Business business = mList.get(position);
        viewHolder.mLogo.setImageResource(business.getLogo());
        viewHolder.bName.setText(business.getbName());
        viewHolder.bCategory.setText(business.getbCategory());
        viewHolder.bCategoryIcon.setImageResource(business.getbCategoryIcon());
        viewHolder.goodComm.setText(business.getGoodComm());
        viewHolder.distance.setText(business.getDistance());
        return convertView;
    }

    class ViewHolder{
        View root;
        ImageView mLogo;
        TextView bName;
        TextView bCategory;
        ImageView bCategoryIcon;
        TextView goodComm;
        TextView distance;
        public ViewHolder(View root){
            mLogo= ((ImageView) root.findViewById(R.id.iv_main_business_logo));
            bName= ((TextView) root.findViewById(R.id.tv_main_business_name));
            bCategory= ((TextView) root.findViewById(R.id.tv_main_business_category));
            bCategoryIcon= ((ImageView) root.findViewById(R.id.iv_main_business_category_icon));
            goodComm= ((TextView) root.findViewById(R.id.tv_main_business_goodjob));
            distance= ((TextView) root.findViewById(R.id.tv_main_business_distance));
        }
    }
}
