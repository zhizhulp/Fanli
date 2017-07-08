package com.ascba.rebate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.auction.AuctionDetailsActivity;
import com.ascba.rebate.activities.auction.AuctionListActivity;
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
        void timeToUpdate();
    }
    TurnAdapter(List<AcutionGoodsBean> data) {
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
        findViews(view,position,container);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    private void findViews(View view, final int position, final ViewGroup container) {
        ImageView imageView = (ImageView) view.findViewById(R.id.auction_img);
        final AcutionGoodsBean item = data.get(position);
        final Context context = container.getContext();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AuctionListActivity.class);
                intent.putExtra("type",item.getType());
                intent.putExtra("client_key",position);
                container.getContext().startActivity(intent);
            }
        });
        Picasso.with(container.getContext()).load(item.getImgUrl()).error(R.mipmap.banner_loading).placeholder(R.mipmap.banner_loading).into(imageView);
        //名称
        ((TextView) view.findViewById(R.id.auction_text_name)).setText(item.getName());
        //竞拍保证金
        ((TextView) view.findViewById(R.id.auction_text_person)).setText("￥"+ item.getCashDeposit());
        //价格
        ((TextView) view.findViewById(R.id.auction_text_price)).setText("￥"+ NumberFormatUtils.getNewDouble(item.getEndPrice()));
        //剩余时间
        TextView tvTime = (TextView) view.findViewById(R.id.auction_text_time);
        //当前价
        TextView tvPriceDesc = (TextView) view.findViewById(R.id.tv_price_desc);
        //已抢光
        View imAlreadyRush = view.findViewById(R.id.im_already_rush);
        //正在进行
        ((TextView) view.findViewById(R.id.auction_text_state)).setText(item.getStrState());

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
        tvTime.setText(state==5? item.getCartStatusTip():getTimeRemainning(item));
        Drawable drawableTop1 = context.getResources().getDrawable(R.mipmap.already_auction);//已拍
        drawableTop1.setBounds(0,0,drawableTop1.getMinimumWidth(),drawableTop1.getMinimumHeight());
        Drawable drawableTop2 = context.getResources().getDrawable(R.mipmap.icon_auction);//未拍
        drawableTop2.setBounds(0,0,drawableTop2.getMinimumWidth(),drawableTop2.getMinimumHeight());

        if(state==2){
            setViewStatus(textView,true,R.color.main_red_normal,drawableTop2, tvPriceDesc,"当前价",imAlreadyRush,false);
        }else if(state==4){
            setViewStatus(textView,true,R.color.main_red_normal,drawableTop2, tvPriceDesc,"当前价",imAlreadyRush,false);
        }else if(state==5){
            setViewStatus(textView,false,R.color.main_text_gary,drawableTop1, tvPriceDesc,"成交价",imAlreadyRush,true);
        }else if(state==6){
            setViewStatus(textView,true,R.color.main_red_normal,drawableTop2, tvPriceDesc,"待支付",imAlreadyRush,false);
        }else if(state==7){
            setViewStatus(textView,false,R.color.main_text_gary,drawableTop1, tvPriceDesc,"已支付",imAlreadyRush,false);
        }
    }

    private void setViewStatus(TextView view,boolean enable,int color,Drawable drawable,
                               TextView priceDescId, String stateText, View imAlreadyRush,boolean isVisible ){
        view.setEnabled(enable);
        view.setTextColor(view.getContext().getResources().getColor(color));
        view.setCompoundDrawables(null,drawable,null,null);
        priceDescId.setText(stateText);
        imAlreadyRush.setVisibility(isVisible? View.VISIBLE : View.GONE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    private String getTimeRemainning(AcutionGoodsBean item) {
        int leftTime = (int) (item.getEndTime() - System.currentTimeMillis() / 1000);
        if(leftTime > 0){
            int goodsLeftTime = (int) (item.getGoodsEndTime() - System.currentTimeMillis() / 1000);
            if( goodsLeftTime>0){
                int hour = goodsLeftTime % (24 * 3600) / 3600;
                int minute = goodsLeftTime % 3600 / 60;
                int second = goodsLeftTime % 60;
                return "距离结束:"+hour + "小时" + minute + "分钟" + second + "秒";
            }else {
                return "商品拍卖结束";
            }

        }else {
            if(callback!=null){
                callback.timeToUpdate();
                Log.d("time_down", "it is time to update");
            }
            return "商品拍卖结束";
        }
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
