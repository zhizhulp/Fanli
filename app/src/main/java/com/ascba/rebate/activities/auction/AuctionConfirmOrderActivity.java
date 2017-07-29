package com.ascba.rebate.activities.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.PayPsdSettingActivity;
import com.ascba.rebate.activities.SelectAddrssUpdateActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.adapter.AuctionConfirmOrderAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.beans.AcutionGoodsBean;
import com.ascba.rebate.beans.ReceiveAddressBean;
import com.ascba.rebate.handlers.OnPasswordInput;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.PayUtils;
import com.ascba.rebate.utils.PsdUtils;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.MarqueeTextView;
import com.ascba.rebate.view.PsdDialog;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞拍确认订单界面
 */
public class AuctionConfirmOrderActivity extends BaseNetActivity {

    private TextView tvTotalPrice;
    private AuctionConfirmOrderAdapter adapter;
    private List<AcutionGoodsBean> beanList;
    private RelativeLayout receiveAddress;
    private RelativeLayout noReceiveAddress;
    private TextView username;
    private TextView userPhone;
    private TextView userAddressDet;
    private TextView tvShippingFee;
    private TextView tvTicket;
    private TextView tvScore;
    private TextView vCount;
    private TextView tvMoney;
    private String goods_id;
    private int is_pay_money;//余额是否够支付  0：不够，1：够
    private int address_id;
    private String total_price;
    private String total_points;
    private String password;
    private boolean needRefresh = false;
    private View attentionView;
    private MarqueeTextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_confirm_order);
        getParams();
        initViews();
        requestNetwork(UrlUtils.payAuctionOrder, 0);
    }

    private void getParams() {
        Intent intent = getIntent();
        if (intent != null) {
            goods_id = intent.getStringExtra("goods_id");
        }
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("goods_id", goods_id);
        if (what == 1) {
            request.add("pay_type", "balance");
            request.add("total_price", total_price);
            request.add("total_points", total_points);
            request.add("address_id", address_id);
        } else if (what == 2) {
            request.add("pay_type", "balance");
            request.add("total_price", total_price);
            request.add("total_points", total_points);
            request.add("pay_password", password);
        }
        executeNetWork(what, request, "请稍后");
    }

    private void initViews() {
        initRecyclerView();
        initBtmView();
        initAttentionView();
    }

    private void initAttentionView() {
        attentionView = findViewById(R.id.auction_order_head);
        attentionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AuctionConfirmOrderActivity.this,MyAuctionActivity.class);
                startActivity(intent);
            }
        });
        tvMsg = ((MarqueeTextView) findViewById(R.id.tv_msg));
    }

    private void initBtmView() {
        tvTotalPrice = ((TextView) findViewById(R.id.confir_order_text_total_price));
        TextView tvApply = ((TextView) findViewById(R.id.confir_order_btn_commit));
        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address_id == 0) {
                    showToast("请设置收获地址");
                    return;
                }

                if (is_pay_money == 0) {//余额不足
                    getDm().buildAlertDialogSure("余额不足，是否去充值", "取消", "确定", new DialogHome.Callback() {
                        @Override
                        public void handleSure() {
                            needRefresh = true;
                            Intent intent = new Intent(AuctionConfirmOrderActivity.this, AccountRechargeActivity.class);
                            startActivity(intent);
                        }
                    });
                    return;
                }

                if (AppConfig.getInstance().getInt("is_level_pwd", 0) == 0) {
                    getDm().buildAlertDialogSure("您还未设置支付密码", "取消", "设置", new DialogHome.Callback() {
                        @Override
                        public void handleSure() {
                            Intent intent = new Intent(AuctionConfirmOrderActivity.this, PayPsdSettingActivity.class);
                            startActivity(intent);
                        }
                    });
                    return;
                }
                requestNetwork(UrlUtils.payAuctionMent, 1);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.recyclerView));
        beanList = new ArrayList<>();
        adapter = new AuctionConfirmOrderAdapter(R.layout.aution_confirm_order_item, beanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        initHeadView();
        initTailView();
    }

    private void initTailView() {
        View tailView = ViewUtils.getView(this, R.layout.auction_confirm_order_footer);
        tvShippingFee = ((TextView) tailView.findViewById(R.id.tv_shipping_fee));
        tvTicket = ((TextView) tailView.findViewById(R.id.tv_ticket));
        tvScore = ((TextView) tailView.findViewById(R.id.tv_score));
        vCount = ((TextView) tailView.findViewById(R.id.tv_total_count));
        tvMoney = ((TextView) tailView.findViewById(R.id.tv_total_money));
        adapter.addFooterView(tailView);
    }

    private void initHeadView() {
        View headView = ViewUtils.getView(this, R.layout.auction_confirm_order_header_address);
        //收货人信息
        receiveAddress = (RelativeLayout) headView.findViewById(R.id.confirm_order_addrss_rl);
        receiveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuctionConfirmOrderActivity.this, SelectAddrssUpdateActivity.class);
                startActivityForResult(intent, SelectAddrssUpdateActivity.REQUEST_ADDRESS);
            }
        });
        noReceiveAddress = (RelativeLayout) headView.findViewById(R.id.confirm_order_addrss_rl2);
        noReceiveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuctionConfirmOrderActivity.this, SelectAddrssUpdateActivity.class);
                startActivityForResult(intent, SelectAddrssUpdateActivity.REQUEST_ADDRESS);
            }
        });
        username = (TextView) headView.findViewById(R.id.confirm_order_username);
        userPhone = (TextView) headView.findViewById(R.id.confirm_order_phone);
        userAddressDet = (TextView) headView.findViewById(R.id.confirm_order_address_details);
        adapter.addHeaderView(headView);
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if (what == 0) {
            int is_level_pwd = dataObj.optInt("is_level_pwd");
            AppConfig.getInstance().putInt("is_level_pwd", is_level_pwd);
            is_pay_money = dataObj.optInt("is_pay_money");
            refreshAttentionData(dataObj);
            JSONObject addressObj = dataObj.optJSONObject("address");
            if (addressObj != null) {
                receiveAddress.setVisibility(View.VISIBLE);
                noReceiveAddress.setVisibility(View.GONE);
                refreshHeadData(addressObj);
            } else {
                receiveAddress.setVisibility(View.GONE);
                noReceiveAddress.setVisibility(View.VISIBLE);
            }
            refreshData(dataObj.optJSONArray("order_list"));
            refreshFootData(dataObj);
            adapter.notifyDataSetChanged();
        } else if (what == 1) {
            String notify_url = dataObj.optString("notify_url");
            showPasswordDialog(notify_url);
        } else if (what == 2) {
            showToast(message);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    private void refreshAttentionData(JSONObject dataObj) {
        int warning_status = dataObj.optInt("warning_status");
        if (warning_status == 1) {//显示
            attentionView.setVisibility(View.VISIBLE);
            tvMsg.setText(dataObj.optString("warning_tip"));
        } else {
            attentionView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        if (what == 0) {
            finish();
        }
    }

    private void showPasswordDialog(final String notify_url) {
        final PsdDialog psdDialog = new PsdDialog(this, R.style.AlertDialog);
        psdDialog.setOnPasswordInputFinish(new OnPasswordInput() {
            @Override
            public void inputFinish(String number) {
                psdDialog.dismiss();
                if (!StringUtils.isEmpty(number)) {
                    AuctionConfirmOrderActivity.this.password = PsdUtils.getPayPsd(number);
                    requestNetwork(notify_url, 2);
                }
            }

            @Override
            public void inputCancel() {
                psdDialog.dismiss();
                showToast("支付取消");
            }

            @Override
            public void forgetPsd() {
                AppConfig.getInstance().putInt("is_level_pwd", 0);
                Intent intent = new Intent(AuctionConfirmOrderActivity.this, PayPsdSettingActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        psdDialog.showMyDialog();
    }

    private void refreshData(JSONArray array) {
        if (beanList.size() > 0) {
            beanList.clear();
        }
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                AcutionGoodsBean agb = new AcutionGoodsBean();
                agb.setId(obj.optInt("id"));
                agb.setName(obj.optString("name"));
                agb.setPrice(obj.optDouble("reserve_money"));
                agb.setStartTimeStr(obj.optString("starttime"));
                agb.setImgUrl(UrlUtils.getNewUrl(obj.optString("imghead")));
                agb.setScore(obj.optString("points"));
                beanList.add(agb);
            }
        }
    }

    private void refreshFootData(JSONObject dataObj) {
        tvShippingFee.setText("快递￥" + dataObj.optString("express_price"));
        tvTicket.setText(dataObj.optString("invoice_tip"));
        total_points = dataObj.optString("total_points");
        tvScore.setText(total_points + "分");
        vCount.setText("共" + dataObj.optInt("list_count") + "件商品  合计：");
        tvMoney.setText(dataObj.optString("total_price") + "元");
        total_price = dataObj.optString("total_price");
        tvTotalPrice.setText("￥" + total_price);
    }

    private void refreshHeadData(JSONObject addressObj) {
        username.setText(addressObj.optString("consignee"));
        userPhone.setText(addressObj.optString("mobile"));
        userAddressDet.setText(addressObj.optString("address_detail"));

        address_id = addressObj.optInt("id");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SelectAddrssUpdateActivity.REQUEST_ADDRESS:
                if (resultCode == RESULT_OK && data != null) {
                    ReceiveAddressBean addressBean = data.getParcelableExtra("address");
                    receiveAddress.setVisibility(View.VISIBLE);
                    noReceiveAddress.setVisibility(View.GONE);
                    setReceiveData(addressBean);
                    address_id = Integer.parseInt(addressBean.getId());
                }
                break;
        }
    }

    private void setReceiveData(ReceiveAddressBean addressBean) {
        username.setText(addressBean.getName());
        userPhone.setText(addressBean.getPhone());
        userAddressDet.setText(addressBean.getAddressDetl());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConfig.getInstance().getInt("uuid", -1000) != -1000 && needRefresh) {
            requestNetwork(UrlUtils.payAuctionOrder, 0);
            needRefresh = false;
        }
    }
}
