package com.ascba.rebate.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.CommissionActivity;
import com.ascba.rebate.activities.TransactionRecordsActivity;
import com.ascba.rebate.activities.me_page.CashGetActivity;
import com.ascba.rebate.activities.me_page.bank_card_child.AddCardActivity;
import com.ascba.rebate.activities.me_page.cash_get_child.CashGetSuccActivity;
import com.ascba.rebate.activities.me_page.settings.child.RealNameCofirmActivity;
import com.ascba.rebate.adapter.BankAdapter;
import com.ascba.rebate.beans.Card;
import com.ascba.rebate.fragments.base.Base2Fragment;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/04/01 0001.
 * 财富-返佣账户-转到银行卡
 */

public class GoBankCardFragment extends Base2Fragment implements Base2Fragment.Callback,
        View.OnClickListener{
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
    private DialogManager dm;
    private Button btnApply;
    private MoneyBar mb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_go_bankcard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        dm=new DialogManager(getActivity());
        pop = new PopupWindow(getActivity());
        mList = new ArrayList<>();

        edMoney = ((EditText) view.findViewById(R.id.money));
        cardView = view.findViewById(R.id.when_has_card);
        cardView.setOnClickListener(this);//点击显示银行卡列表
        noCardView = view.findViewById(R.id.when_no_card);
        noCardView.setOnClickListener(this);//点击绑定银行卡
        tvLeftMoney = ((TextView) view.findViewById(R.id.tv_left_money));
        tvSeviFee = ((TextView) view.findViewById(R.id.tv_sevice_fee));
        tvBankName = ((TextView) view.findViewById(R.id.tv_bank_name));
        tvBankNum = ((TextView) view.findViewById(R.id.tv_bank_number));

        view.findViewById(R.id.tv_get_all).setOnClickListener(this);//全部提现
        btnApply = ((Button) view.findViewById(R.id.btn_go_cash));
        btnApply.setOnClickListener(this);
        requestHasCard(4);//检查是否有银行卡

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
        } else if (finalScene == 4) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.checkCardId, 0, true);
            executeNetWork(request, "请稍后");
            setCallback(this);
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
                pop.setHeight(ScreenDpiUtils.dip2px(getActivity(), 180));
                pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                        params.alpha = 1.0f;
                        getActivity().getWindow().setAttributes(params);
                    }
                });
                View popView = getActivity().getLayoutInflater().inflate(R.layout.pop_bank_list, null);
                bankListView = ((ListView) popView.findViewById(R.id.listview));
                BankAdapter bankAdapter = new BankAdapter(getActivity(), mList);
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
                pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                //设置背景变暗
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 0.5f;
                getActivity().getWindow().setAttributes(params);
                break;
            case R.id.when_no_card:
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                intent.putExtra("realname", realname);
                startActivity(intent);
                break;
            case R.id.tv_get_all://点击全部提现
                edMoney.setText(totalMoney);
                edMoney.setSelection(totalMoney.length());
                break;
            case R.id.btn_go_cash:
                money = edMoney.getText().toString();
                if ("".equals(money)) {
                    dm.buildAlertDialog("请输入提现金额");
                    return;
                }
                requestHasCard(1);
                break;
        }

    }

    private String getTail4Num(String bank_card) {
        return bank_card.substring(bank_card.length() - 4, bank_card.length() );
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 1) {
            int isBankCard = dataObj.optInt("isBankCard");//银行卡数量
            int isCardId = dataObj.optInt("isCardId");//是否实名认证
            if (isBankCard == 0) {
                if (isCardId == 0) {
                    dm.buildAlertDialog1("您还没有实名认证，是否立即实名？");
                    dm.setCallback(new DialogManager.Callback() {
                        @Override
                        public void handleSure() {
                            Intent intent = new Intent(getActivity(), RealNameCofirmActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                    });
                } else {
                    JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                    final String realname = cardObj.optString("realname");
                    dm.buildAlertDialog1("暂未绑定银行卡，是否立即绑定？");
                    dm.setCallback(new DialogManager.Callback() {
                        @Override
                        public void handleSure() {
                            dm.dismissDialog();
                            Intent intent = new Intent(getActivity(), AddCardActivity.class);
                            intent.putExtra("realname", realname);
                            startActivity(intent);
                            //finish();
                        }
                    });
                }

            } else {
                dm.buildAlertDialog1("确定提现吗？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        dm.dismissDialog();
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
            Intent intent=new Intent(getActivity(),CashGetSuccActivity.class);
            startActivityForResult(intent, FourthFragment.REQUEST_CASH_GET);
        } else if (finalScene == 4){
            int isCardId = dataObj.optInt("isCardId");
            int isBankCard = dataObj.optInt("isBankCard");
            if (isCardId == 0) {
                final DialogManager dm = new DialogManager(getActivity());
                dm.buildAlertDialog1("暂未实名认证，是否立即实名认证？");
                dm.setCallback(new DialogManager.Callback() {
                    @Override
                    public void handleSure() {
                        dm.dismissDialog();
                        Intent intent = new Intent(getActivity(), RealNameCofirmActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                JSONObject cardObj = dataObj.optJSONObject("cardInfo");
                realname=cardObj.optString("realname");
                if (isBankCard == 0) {
                    cardView.setVisibility(View.GONE);
                    noCardView.setVisibility(View.VISIBLE);
                } else {
                    noCardView.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                }
                requestHasCard(2);
            }
        }
    }

    @Override
    public void handleReqFailed() {

    }

    @Override
    public void handle404(String message) {

    }

    @Override
    public void handleReLogin() {

    }

    @Override
    public void handleNoNetWork() {

    }
}
