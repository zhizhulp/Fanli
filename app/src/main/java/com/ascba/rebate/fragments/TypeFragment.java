package com.ascba.rebate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.fragments.base.BaseNetFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.DialogHome;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

/**
 * 商城分类
 */
public class TypeFragment extends BaseNetFragment {

    private DialogHome dialogHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_type, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogHome = new DialogHome(getActivity());
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHome.buildWaitDialog("buildWaitDialog");
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHome.buildAlertDialog("buildAlertDialog");
            }
        });
        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHome.buildAlertDialogSure("buildAlertDialogSure", new DialogHome.Callback() {
                    @Override
                    public void handleSure() {
                        showToast("确定了");
                    }

                    @Override
                    public void handleCancel() {
                        showToast("取消了");

                    }

                });
            }
        });


    }

    @Override
    protected boolean hasCache() {
        return false;
    }

    private void initRequst() {
        Request<JSONObject> request = buildNetRequest(UrlUtils.getOrderList, 1, true);
        request.add("status", "all");
        executeNetWork(1, request, "请稍后");
        executeNetWork(request, "请稍后");
        setCallback(new Callback() {
            @Override
            public void handle200Data(JSONObject dataObj, String message) {

            }

            @Override
            public void handleReqFailed() {

            }

            @Override
            public void handle404(String message, JSONObject dataObj) {

            }

            @Override
            public void handleReLogin() {

            }

            @Override
            public void handleNoNetWork() {

            }
        });

        setCallbackWhat(new CallbackWhat() {
            @Override
            public void handle200Data(int what, JSONObject dataObj, String message) {

            }

            @Override
            public void handleReqFailed(int what) {

            }

            @Override
            public void handle404(int what, String message, JSONObject dataObj) {

            }

            @Override
            public void handleReLogin() {

            }

            @Override
            public void handleNoNetWork() {

            }
        });
    }


}
