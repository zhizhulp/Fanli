package com.ascba.rebate.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李鹏 on 2017/5/22.
 * 拍卖首页列表适配器
 */

public class AcutionHPAdapter extends BaseQuickAdapter<AcutionGoodsBean, BaseViewHolder> {

    private Context context;
    private Callback callback;
    public interface Callback{
        void timeToUpdate();
    }

    public AcutionHPAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<AcutionGoodsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder  helper, AcutionGoodsBean item) {
        ImageView imageView = helper.getView(R.id.auction_img);
        Picasso.with(context).load(item.getImgUrl()).error(R.mipmap.banner_loading).placeholder(R.mipmap.banner_loading).into(imageView);
        //剩余时间
        helper.setText(R.id.auction_text_time, getTimeRemainning(item));
        //名称
        helper.setText(R.id.auction_text_name, item.getName());
        //竞拍保证金
        helper.setText(R.id.auction_text_person, "￥"+ item.getCashDeposit());
        //价格
        helper.setText(R.id.auction_text_price, "￥"+ NumberFormatUtils.getNewDouble(item.getEndPrice()));
        helper.addOnClickListener(R.id.auction_btn_get);
        helper.setText(R.id.auction_btn_get,item.getStrState());
        View view = helper.getView(R.id.auction_btn_get);
        int state = item.getIntState();
        if(state==2){
            view.setEnabled(true);
        }else if(state==4){
            view.setEnabled(true);
        }else if(state==5){
            view.setEnabled(false);
        }else if(state==6){
            view.setEnabled(true);
        }else if(state==7){
            view.setEnabled(false);
        }
    }

    private String getTimeRemainning(AcutionGoodsBean item) {
        boolean isAllTimeDown=true;
        for (int i = 0; i < mData.size(); i++) {
            AcutionGoodsBean agb = mData.get(i);
            int leftTime = (int) (agb.getEndTime() - System.currentTimeMillis() / 1000 + 3);
            if(leftTime > 0){
                isAllTimeDown = false;
                break;
            }
        }
        Log.d("turn", "isAllTimeDown: "+isAllTimeDown);
        if(isAllTimeDown){
            if(callback!=null){
                callback.timeToUpdate();
            }
            return "本场已结束";
        }else {
            int leftTime = (int) (item.getEndTime() - System.currentTimeMillis() / 1000);
            int hour = leftTime % (24 * 3600) / 3600;
            int minute = leftTime % 3600 / 60;
            int second = leftTime % 60;
            return "距离结束:" + hour + "时" + minute + "分" + second + "秒";
        }
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
