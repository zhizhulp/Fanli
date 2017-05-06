package com.ascba.rebate.activities.me_page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.CashGetBillActivity;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardActivity;
import com.ascba.rebate.activities.me_page.cash_get_child.CashGetSuccActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.adapter.BankAdapter;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.fragments.MoneyFragment;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 财富-现金账户-提现
 */
public class CashGetActivity extends BaseNetActivity implements View.OnClickListener, BaseNetActivity.Callback
,MoneyBar.CallBack{

    private View cardView;
    private View noCardView;
    private String realname;
    private EditText edMoney;
    private int finalScene;
    private TextView tvLeftMoney;
    private TextView tvSeviFee;
    private List<Card> mList;
    private TextView tvBankName;
    private TextView tvBankNum;
    private String money;
    private String totalMoney;
    private PopupWindow pop;
    private ListView bankListView;
    private Card selectCard;
    private int bankId;//要传的银行卡id参数
    private MoneyBar mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_get);
        initViews();
        getDataFromIntent();
    }

    private void initViews() {
        pop = new PopupWindow(this);
        mList = new ArrayList<>();
        mb = ((MoneyBar) findViewById(R.id.mb));
        mb.setTailTitle("提现记录");
        mb.setCallBack(this);
        edMoney = ((EditText) findViewById(R.id.money));
        cardView = findViewById(R.id.when_has_card);
        cardView.setOnClickListener(this);//点击显示银行卡列表
        noCardView = findViewById(R.id.when_no_card);
        noCardView.setOnClickListener(this);//点击绑定银行卡
        tvLeftMoney = ((TextView) findViewById(R.id.tv_left_money));
        tvSeviFee = ((TextView) findViewById(R.id.tv_sevice_fee));
        tvBankName = ((TextView) findViewById(R.id.tv_bank_name));
        tvBankNum = ((TextView) findViewById(R.id.tv_bank_number));
        requestHasCard(2);
    }


    //获取银行卡数，显示不同界面
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            int bank_card_number = intent.getIntExtra("bank_card_number", -1);
            if (bank_card_number != -1) {
                if (bank_card_number == 0) {
                    cardView.setVisibility(View.GONE);
                    noCardView.setVisibility(View.VISIBLE);
                } else {
                    noCardView.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                }
            }
            realname = intent.getStringExtra("realname");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.when_has_card:

                pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                pop.setOutsideTouchable(true);
                pop.setFocusable(true);
                pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setHeight(ScreenDpiUtils.dip2px(this, 180));
                pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                });
                View popView = getLayoutInflater().inflate(R.layout.pop_bank_list, null);
                bankListView = ((ListView) popView.findViewById(R.id.listview));
                BankAdapter bankAdapter = new BankAdapter(this, mList);
                bankListView.setAdapter(bankAdapter);
                bankListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        selectCard = mList.get(i);
                        bankId=selectCard.getId();
                        pop.dismiss();
                        tvBankName.setText(selectCard.getName());
                        String tail4Num = getTail4Num(selectCard.getNumber());
                        tvBankNum.setText(selectCard.getType() + "-尾号" + "(" + tail4Num + ")");
                    }
                });
                pop.setContentView(popView);
                pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                //设置背景变暗
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                break;
            case R.id.when_no_card:
                Intent intent = new Intent(this, AddCardActivity.class);
                intent.putExtra("realname", realname);
                startActivity(intent);
                break;
        }

    }

    //去提现
    public void goGetCash(View view) {
        //判断有没有绑定银行卡
        money = edMoney.getText().toString();
        if ("".equals(money)) {
            getDm().buildAlertDialog("请输入提现金额");
            return;
        }
        requestHasCard(1);

    }

    private void requestHasCard(int scene) {
        finalScene = scene;
        if (finalScene == 1) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (finalScene == 2) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.getTillsMoney, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (finalScene == 3) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.tillsMoney, 0, true);
            request.add("money", money);
            request.add("bank_id", bankId);
            executeNetWork(request, "请稍后");
            setCallback(this);
        }

    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 1) {
            int isBankCard = dataObj.optInt("isBankCard");//银行卡数量
            int isCardId = dataObj.optInt("isCardId");//是否实名认证
            if (isBankCard == 0) {
                if (isCardId == 0) {
                    getDm().buildAlertDialogSure("您还没有实名认证，是否立即实名？",new DialogHome.Callback() {
                        @Override
                        public void handleSure() {
                            Intent intent = new Intent(CashGetActivity.this, RealNameCofirmActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                    final String realname = cardObj.optString("realname");
                    getDm().buildAlertDialogSure("暂未绑定银行卡，是否立即绑定？",new DialogHome.Callback() {
                        @Override
                        public void handleSure() {
                            Intent intent = new Intent(CashGetActivity.this, AddCardActivity.class);
                            intent.putExtra("realname", realname);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            } else {
                getDm().buildAlertDialogSure("确定提现吗？",new DialogHome.Callback() {
                    @Override
                    public void handleSure() {
                        requestHasCard(3);
                    }
                });

            }
        } else if (finalScene == 2) {
            JSONObject moneyObj = dataObj.optJSONObject("getTillsMoney");
            totalMoney = moneyObj.optString("money");//账户余额
            String cash_tax_rate = moneyObj.optString("cash_tax_rate");//服务费
            tvLeftMoney.setText("账户余额 ¥ " + totalMoney);
            tvSeviFee.setText("("+cash_tax_rate+")");
            JSONArray bankList = dataObj.optJSONArray("bank_info");
            mList.clear();
            for (int i = 0; i < bankList.length(); i++) {
                JSONObject bankObj = bankList.optJSONObject(i);
                int id = bankObj.optInt("id");//银行卡id
                String bank = bankObj.optString("bank");//银行名称
                String type = bankObj.optString("type");//银行类型
                String bank_card = bankObj.optString("bank_card");//银行卡号
                int is_default = bankObj.optInt("is_default");//1 默认显示银行卡
                if (is_default == 1) {
                    bankId=id;
                    String tail4Num = getTail4Num(bank_card);
                    tvBankName.setText(bank);
                    tvBankNum.setText(type + "-尾号" + "(" + tail4Num + ")");
                }
                Card card = new Card(id, bank, type, bank_card, is_default);
                mList.add(card);
            }
        } else if (finalScene == 3) {
            Intent intent=new Intent(this,CashGetSuccActivity.class);
            startActivityForResult(intent, MoneyFragment.REQUEST_CASH_GET);
        }
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {

    }

    private String getTail4Num(String bank_card) {
        return bank_card.substring(bank_card.length() - 4, bank_card.length() );
    }

    public void getAll(View view) {
        edMoney.setText(totalMoney);
        edMoney.setSelection(totalMoney.length());
    }

    @Override
    public void clickImage(View im) {

    }

    @Override
    public void clickComplete(View tv) {
        startActivity(new Intent(this, CashGetBillActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case MoneyFragment.REQUEST_CASH_GET:
                setResult(RESULT_OK,getIntent());
                finish();
                break;
        }
    }
}
