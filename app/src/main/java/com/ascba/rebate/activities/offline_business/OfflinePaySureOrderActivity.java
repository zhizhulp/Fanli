package com.ascba.rebate.activities.offline_business;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.view.RoundImageView;

import org.json.JSONObject;

/**
 * 线下支付-用户付款成功详情
 */
public class OfflinePaySureOrderActivity extends BaseNetActivity implements View.OnClickListener {

    private RoundImageView userIcon;
    private TextView tvName;
    private TextView tvCost;
    private TextView tvPayType;
    private TextView tvAccount;
    private TextView tvScore;
    private TextView tvTime;
    private TextView tvTradeNumber;
    private TextView tvConfirm, tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_sure_order);
        initViews();

    }

    private void initViews() {
        userIcon = ((RoundImageView) findViewById(R.id.im_user_icon));
        tvName = ((TextView) findViewById(R.id.tv_user_name));
        tvCost = ((TextView) findViewById(R.id.tv_user_cost));
        tvPayType = ((TextView) findViewById(R.id.tv_pay_type));
        tvAccount = ((TextView) findViewById(R.id.tv_account11));
        tvScore = ((TextView) findViewById(R.id.tv_score));
        tvTime = ((TextView) findViewById(R.id.tv_time));
        tvTradeNumber = ((TextView) findViewById(R.id.tv_trade_number));
        tvConfirm = (TextView) findViewById(R.id.sure_order_confirm);
        tvCancel = (TextView) findViewById(R.id.sure_order_cancel);
        tvConfirm.setOnClickListener(this);
        tvCancel.setOnClickListener(this);


    }



    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_order_confirm:
                break;
            case R.id.sure_order_cancel://取消的dialog
                Dialog dialog1 = getDm().buildAlertDialog2("确定取消此笔订单吗？", new DialogHome.Callback() {
                    @Override
                    public void handleSure() {//点击取消时，商家重新请求支付。
                    }
                });
                dialog1.show();
                break;
        }
    }





    public void setEvent(final AlertDialog dialog) {
        ImageView ivCancel = (ImageView) dialog.findViewById(R.id.sureorder_dialog_cancel);
        TextView tvConfirm = (TextView) dialog.findViewById(R.id.sureorder_dialog_confirm);

        //点击取消，dialog消失
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
      //点击确定，取消订单重新发起订单
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

}
