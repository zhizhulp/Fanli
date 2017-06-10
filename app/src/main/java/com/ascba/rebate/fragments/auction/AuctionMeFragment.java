package com.ascba.rebate.fragments.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.auction.MyAuctionActivity;
import com.ascba.rebate.activities.auction.MyCashDepositActivity;
import com.ascba.rebate.activities.auction.MyGetAuction2Activity;
import com.ascba.rebate.activities.me_page.AccountRechargeActivity;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.RoundImageView;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * Created by 李鹏 on 2017/5/24.
 */

public class AuctionMeFragment extends BaseNetFragment implements View.OnClickListener {

    private RoundImageView imUserIcon;
    private TextView tvNickName;
    private TextView tvCategory;
    private TextView tvCahLeft;
    private TextView tvScore;
    private TextView tvMyAuction;
    private TextView tvReAuction;
    private TextView tvAttention;
    private TextView tvEnsureCash;

    @Override
    protected int setContentView() {
        return R.layout.fragment_auction_me;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        requestNetwork(UrlUtils.auctionInfo,0);
    }

    private void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(what,request,"请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        if(what==0){
            initMemberInfo(dataObj);
            initCount(dataObj);
        }
    }

    private void initCount(JSONObject dataObj) {
        tvMyAuction.setText(dataObj.optInt("auction_count")+"件");
        tvReAuction.setText(dataObj.optInt("winning_count")+"件");
        tvAttention.setText(dataObj.optInt("remind_count")+"件");
        tvEnsureCash.setText(dataObj.optInt("bond_count")+"笔");
    }

    private void initMemberInfo(JSONObject dataObj) {
        JSONObject memberInfo = dataObj.optJSONObject("member_info");
        Picasso.with(getActivity()).load(UrlUtils.baseWebsite+memberInfo.optString("avatar")).placeholder(R.mipmap.busi_loading).into(imUserIcon);
        tvNickName.setText(memberInfo.optString("nickname"));
        tvCahLeft.setText(memberInfo.optString("money"));
        tvScore.setText(memberInfo.optString("white_score")+"积分");
        tvCategory.setText("（"+memberInfo.optString("group_name")+"）");
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_auction_me_bao_zheng_jin).setOnClickListener(this);
        view.findViewById(R.id.btn_auction_me_jing_pai).setOnClickListener(this);
        view.findViewById(R.id.btn_auction_me_huo_pai).setOnClickListener(this);
        view.findViewById(R.id.tv_recharge).setOnClickListener(this);

        imUserIcon = ((RoundImageView) view.findViewById(R.id.me_user_img));
        tvNickName = ((TextView) view.findViewById(R.id.tv_nick_name));
        tvCategory = ((TextView) view.findViewById(R.id.tv_user_category));
        tvCahLeft = ((TextView) view.findViewById(R.id.tv_cash_left));
        tvScore = ((TextView) view.findViewById(R.id.tv_score));
        tvMyAuction = ((TextView) view.findViewById(R.id.tv_my_auction));
        tvReAuction = ((TextView) view.findViewById(R.id.tv_receive_auction));
        tvAttention = ((TextView) view.findViewById(R.id.tv_my_attention));
        tvEnsureCash = ((TextView) view.findViewById(R.id.tv_my_ensure_cash));

        initRefreshLayout(view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNetwork(UrlUtils.auctionInfo,0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_auction_me_bao_zheng_jin:
                //我的保证金
                startActivity(new Intent(getActivity(), MyCashDepositActivity.class));
                break;
            case R.id.btn_auction_me_jing_pai:
                //我的竞拍
                startActivity(new Intent(getActivity(), MyAuctionActivity.class));
                break;
            case R.id.btn_auction_me_huo_pai:
                //我的获拍
                startActivity(new Intent(getActivity(),MyGetAuction2Activity.class));
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(getActivity(), AccountRechargeActivity.class));
                break;
        }
    }
}
