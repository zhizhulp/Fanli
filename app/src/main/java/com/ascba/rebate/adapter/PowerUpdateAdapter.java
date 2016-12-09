package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.Proxy;
import com.ascba.rebate.beans.UpdateTitle;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 特权升级adpter
 */

public class PowerUpdateAdapter extends BaseAdapter {
    private List<UpdateTitle> mList;
    private Context context;
    private LayoutInflater mInflater;
    private static final int LIST_TYPE_ACCOUNT=2;
    private static final int TYPE_TITLE=0;
    private static final int TYPE_CONTENT=1;
    private Callback callback;
    public interface Callback{
        void clickProtocol(int position);
        void clickOpen(int position);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public PowerUpdateAdapter(List<UpdateTitle> mList, Context context) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mList != null) {
            for (UpdateTitle type : mList) {
                count += type.size();
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        int head = 0;  //标题位置
        for (UpdateTitle type : mList) {
            int size = type.size();
            int current = position - head;
            if (current < size) {
                //返回对应位置的值
                return type.getItem(current);
            }
            head += size;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return LIST_TYPE_ACCOUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int head = 0;
        for (UpdateTitle type : mList) {
            int size = type.size();
            int current = position - head;
            if (current == 0) {
                return TYPE_TITLE;
            }
            head += size;
        }
        return TYPE_CONTENT;
    }
    /**
     * 判断当前的item是否可以点击
     * @param position
     * @return
     */
    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_TITLE;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        switch (getItemViewType(position)){
            case TYPE_CONTENT:
                if(convertView==null){
                    convertView=mInflater.inflate(R.layout.power_update_list_item,null);
                    viewHolder=new ViewHolder(convertView,0);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder= (ViewHolder) convertView.getTag();
                }
                Proxy proxy = (Proxy) getItem(position);
                Picasso.with(context).load(proxy.getIcon()).into( viewHolder.imTypeIcon);
                viewHolder.tvType.setText(proxy.getType());
                viewHolder.tvMoney.setText(proxy.getMoney());
                viewHolder.tvOpen.setText(proxy.isOpen()? "已开通":"暂未开通");
                viewHolder.tvOpen.setTextColor(proxy.isOpen()? Color.GRAY : 0xff2e82ff );
                viewHolder.tvOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.clickOpen(position);
                    }
                });
                viewHolder.tv01.setText(proxy.getDesc());
                viewHolder.tv01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.clickProtocol(position);
                    }
                });
                break;
            case TYPE_TITLE:
                if(convertView==null){
                    convertView=mInflater.inflate(R.layout.power_update_title_item,null);
                    viewHolder=new ViewHolder(convertView,1);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder= (ViewHolder) convertView.getTag();
                }
                String item = (String) getItem(position);
                viewHolder.tvTitle.setText(item);
                break;
        }
        return convertView;
    }
    class ViewHolder{
        ImageView imTypeIcon;
        TextView tvType;
        TextView tvMoney;
        TextView tvOpen;
        TextView tvTitle;
        TextView tv01;
        ViewHolder(View root, int type){
            switch (type){
                case 0:
                    if(imTypeIcon==null){
                        imTypeIcon= (ImageView) root.findViewById(R.id.im_power_update_type);
                    }
                    if(tvType==null){
                        tvType= (TextView) root.findViewById(R.id.tv_power_update_type);
                    }
                    if(tvMoney==null){
                        tvMoney= (TextView) root.findViewById(R.id.tv_power_update_fee);
                    }
                    if(tvOpen==null){
                        tvOpen= (TextView) root.findViewById(R.id.tv_power_update_open);
                    }
                    if(tv01==null){
                        tv01= (TextView) root.findViewById(R.id.see);
                    }

                    break;
                case 1:
                    if(tvTitle==null){
                        tvTitle= (TextView) root.findViewById(R.id.tv_power_update_title);
                    }
                    break;
            }

        }
    }
}
