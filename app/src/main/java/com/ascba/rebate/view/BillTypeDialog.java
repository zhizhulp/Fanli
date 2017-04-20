package com.ascba.rebate.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ascba.rebate.R;
import com.ascba.rebate.beans.BillType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 李平 on 2017/4/20 0020.15:09
 */

public class BillTypeDialog extends Dialog {
    private RecyclerView rv;
    private BillTypeAdapter adapter;
    private List<BillType> data;
    public interface Callback{
        void onClick(BillType bt,int position);
    }

    private Callback callback;//选择回调
    public Callback getCallback() {
        return callback;
    }
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public BillTypeDialog(@NonNull Context context,List<BillType> data) {
        super(context);
        this.data=data;
        init();
    }

    public BillTypeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

    }

    protected BillTypeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init() {
        setContentView(R.layout.bill_dialog);
        initViews();
    }


    private void initViews() {
        rv = ((RecyclerView) findViewById(R.id.rv));
        adapter = new BillTypeAdapter(R.layout.bill_dialog_item,data);
        rv.setLayoutManager(new GridLayoutManager(getContext(),3));
        rv.setAdapter(adapter);
        initCancel();
    }

    /**
     * 取消选择
     */
    private void initCancel() {
        (findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void showMyDialog() {
        show();
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.goods_profile_anim);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wlp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            wlp.width = d.getWidth();
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
        }
    }


    private class BillTypeAdapter extends BaseQuickAdapter<BillType,BaseViewHolder>{

        public BillTypeAdapter(int layoutResId, List<BillType> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BillType item) {
            helper.setText(R.id.tv_title,item.title);
            helper.setTextColor(R.id.tv_title,item.hasSelect? getColorId(R.color.white) : getColorId(R.color.main_text_normal));

            helper.setText(R.id.tv_content,item.count);
            helper.setTextColor(R.id.tv_content,item.hasSelect? getColorId(R.color.white) :
                    getColorId(R.color.main_text_normal));
            //背景色
            helper.setBackgroundColor(R.id.bill_lat,item.hasSelect? getColorId(R.color.main_red_normal) :
                    getColorId(R.color.main_line_light_gray));
            helper.setOnClickListener(R.id.bill_lat,createOnClickListener(item,this,helper));

        }
    }

    private View.OnClickListener createOnClickListener(final BillType item, final BillTypeAdapter adapter, final BaseViewHolder helper) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.hasSelect){
                    item.hasSelect=true;
                    for (BillType bt: data) {
                        if(bt!=item && bt.hasSelect){
                            bt.hasSelect=false;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                if(callback!=null){
                    callback.onClick(item,helper.getPosition());
                }
            }
        };
    }

    private int getColorId(int color){
        return getContext().getResources().getColor(color);
    }


}
