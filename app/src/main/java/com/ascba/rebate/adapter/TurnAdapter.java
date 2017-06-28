package com.ascba.rebate.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.auction.AuctionDetailsActivity;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/6/16.
 * 竞拍轮播adapter
 */

public class TurnAdapter extends PagerAdapter {
    private List<AcutionGoodsBean> data;
    private Callback callback;
    public interface Callback{
        void click(AcutionGoodsBean item);
    }
    public TurnAdapter(List<AcutionGoodsBean> data) {
        this.data=data;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项
        position %= data.size();
        if (position < 0) {
            position = data.size() + position;
        }
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_auction_hp, null);
        findViews(view,data.get(position),container);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    private void findViews(View view, final AcutionGoodsBean item, final ViewGroup container) {
        ImageView imageView = (ImageView) view.findViewById(R.id.auction_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(container.getContext(), AuctionDetailsActivity.class);
                intent.putExtra("agb",item);
                container.getContext().startActivity(intent);
            }
        });
        Picasso.with(container.getContext()).load(item.getImgUrl()).error(R.mipmap.loading_rect).placeholder(R.mipmap.loading_rect).into(imageView);
        //剩余时间
        ((TextView) view.findViewById(R.id.auction_text_time)).setText(getTimeRemainning(item));
        //名称
        ((TextView) view.findViewById(R.id.auction_text_name)).setText(item.getName());
        //竞拍保证金
        ((TextView) view.findViewById(R.id.auction_text_person)).setText("￥"+ item.getCashDeposit());
        //价格
        ((TextView) view.findViewById(R.id.auction_text_price)).setText("￥"+ NumberFormatUtils.getNewDouble(item.getEndPrice()));
        TextView textView = (TextView) view.findViewById(R.id.auction_btn_get);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.click(item);
                }
            }
        });
        textView.setText(item.getStrState());
        int state = item.getIntState();
        if(state==2){
            textView.setEnabled(true);
        }else if(state==4){
            textView.setEnabled(true);
        }else if(state==5){
            textView.setEnabled(false);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    private String getTimeRemainning(AcutionGoodsBean item) {
        int leftTime = (int) (item.getEndTime() - System.currentTimeMillis() / 1000);
        if(leftTime<=0){
            return "本场已结束";
        }
        int hour = leftTime % (24 * 3600) / 3600;
        int minute = leftTime % 3600 / 60;
        int second = leftTime % 60;
        return "距离结束:"+hour + "时" + minute + "分" + second + "秒";
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
