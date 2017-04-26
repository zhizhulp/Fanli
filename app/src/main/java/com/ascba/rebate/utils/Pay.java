package com.ascba.rebate.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.PayTypeAdapter;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.PayType;
import com.ascba.rebate.view.pay.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 李鹏 on 2017/04/25 0025.
 */

public class Pay {

    private Dialog dialog;
    private Activity context;
    private String price;//价格
    private DialogHome dialogHome;

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jObj = new JSONObject(resultInfo);
                            JSONObject trObj = jObj.optJSONObject("alipay_trade_app_pay_response");
                            String total_amount = trObj.optString("total_amount");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        dialogHome.buildAlertDialog("网络有问题");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        dialogHome.buildAlertDialog("您已经取消支付");
                    } else {
                        dialogHome.buildAlertDialog("支付失败");
                    }
                    context.setResult(RESULT_OK);
                    context.finish();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    public Pay(Activity activity, String price) {
        this.context = activity;
        this.price = price;
        dialogHome = new DialogHome(context);
    }

    public interface OnCreatOrder {
        void onCreatOrder(String payType);
    }


    //选择支付方式页面
    public void showDialog(final OnCreatOrder onCreatOrder) {
        final String[] type = {"balance"};
        dialog = new Dialog(context, R.style.AlertDialog);
        dialog.setContentView(R.layout.layout_pay_pop);
        ((TextView) dialog.findViewById(R.id.dlg_tv_total_cash)).setText(price);
        //关闭对话框
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //去付款
        dialog.findViewById(R.id.go_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreatOrder.onCreatOrder(type[0]);
            }
        });
        //列表
        RecyclerView rvTypes = (RecyclerView) dialog.findViewById(R.id.pay_type_list);
        List<PayType> types = new ArrayList<>();
        initPayTypesData(types);
        PayTypeAdapter pt = new PayTypeAdapter(R.layout.pay_type_item, types);
        pt.setCallback(new PayTypeAdapter.Callback() {
            @Override
            public void onClicked(String payType) {
                type[0] = payType;
            }
        });
        rvTypes.setLayoutManager(new LinearLayoutManager(context));
        rvTypes.setAdapter(pt);
        //显示对话框
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.goods_profile_anim);
            WindowManager.LayoutParams wlp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            wlp.width = d.getWidth();
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
        }
    }

    private void initPayTypesData(List<PayType> types) {
        types.add(new PayType(true, R.mipmap.pay_left, "账户余额支付", "快捷支付", "balance"));
        types.add(new PayType(false, R.mipmap.pay_ali, "支付宝支付", "大额支付，支持银行卡、信用卡", "alipay"));
        types.add(new PayType(false, R.mipmap.pay_weixin, "微信支付", "大额支付，支持银行卡、信用卡", "wxpay"));
    }

    //调起支付宝
    public void requestForAli(final String payInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //调起微信
    public void requestForWX(JSONObject wxpay) {
        try {
            PayReq req = new PayReq();
            req.appId = wxpay.getString("appid");
            req.nonceStr = wxpay.getString("noncestr");
            req.packageValue = wxpay.getString("package");
            req.partnerId = wxpay.getString("partnerid");
            req.prepayId = wxpay.getString("prepayid");
            req.timeStamp = String.valueOf(wxpay.getLong("timestamp"));
            req.sign = wxpay.getString("sign");
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean hasWXApp = WXAPIFactory.createWXAPI(context, IDsUtils.WX_PAY_APP_ID).sendReq(req);
            if (!hasWXApp) {
                Toast.makeText(context, "您可能没有安装微信客户端", Toast.LENGTH_SHORT).show();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            MyApplication.payType = 1;
            context.finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
