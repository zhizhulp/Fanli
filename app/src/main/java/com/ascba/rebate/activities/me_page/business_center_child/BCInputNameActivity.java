package com.ascba.rebate.activities.me_page.business_center_child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.fragments.me.FourthFragment;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.EditTextWithCustomHint;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;


/**
 * 商户中心，输入公司名称
 */
public class BCInputNameActivity extends BaseNetActivity implements BaseNetActivity.Callback {

    private EditTextWithCustomHint edName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcinput_name);
        //StatusBarUtil.setColor(this,getResources().getColor(R.color.moneyBarColor));
        initviews();
    }

    private void initviews() {
        edName = ((EditTextWithCustomHint) findViewById(R.id.company_name));
    }

    public void goNext(View view) {
        String s = edName.getText().toString();
        if ("".equals(s.trim())) {
            getDm().buildAlertDialog("公司名称不能为空");
            return;
        }
        Request<JSONObject> request = buildNetRequest(UrlUtils.findCompany, 0, true);
        request.add("company_name", s);
        executeNetWork(request, "请稍后");
        setCallback(this);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        JSONObject comObj = dataObj.optJSONObject("company_info");
        String name = comObj.optString("name");//公司名称
        String oper_name = comObj.optString("oper_name");//法人名字
        String regist_capi = comObj.optString("regist_capi");//注册资本
        String company_status = comObj.optString("company_status");//开业状态
        String scope = comObj.optString("scope");//经营范围
        int is_oper_name = comObj.optInt("is_oper_name");//0:与法人信息一致，1：与法人信息不一致
        Intent intent = new Intent(this, BusinessCenterActivity.class);
        if (is_oper_name == 1) {
            String clientele_name = comObj.optString("clientele_name");
            intent.putExtra("clientele_name", clientele_name);
        }
        intent.putExtra("name", name);
        intent.putExtra("oper_name", oper_name);
        intent.putExtra("regist_capi", regist_capi);
        intent.putExtra("company_status", company_status);
        intent.putExtra("scope", scope);
        intent.putExtra("is_oper_name", is_oper_name);
        startActivityForResult(intent, FourthFragment.REQUEST_APPLY);
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case FourthFragment.REQUEST_APPLY:
                    setResult(RESULT_OK, getIntent());
                    finish();
                    break;
            }
        }
    }
}
