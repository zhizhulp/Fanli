package com.ascba.rebate.fragments.base;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.activities.main.MainActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.DialogManager2;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {
    private DialogManager2 dm;
    private Callback callback;

    public interface Callback{
        void handle200Data(JSONObject dataObj, String message);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public BaseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
    //执行网络请求
    public void executeNetWork(Request<JSONObject> jsonRequest, String message) {

        FragmentActivity activity = getActivity();
        if(activity instanceof MainActivity){
            dm = ((MainActivity) activity).getDm();
        }else {
            if(dm==null){
                dm=new DialogManager2(getActivity());
            }
        }
        boolean netAva = NetUtils.isNetworkAvailable(getActivity());
        if(!netAva){
            if(activity instanceof MainActivity){
                if(dm.getDialogList().size()!=0){
                    return;
                }else {
                    dm.buildAlertDialog("请打开网络！");
                    return;
                }
            }else {
                dm.buildAlertDialog("请打开网络！");
                return;
            }

        }
        MyApplication.getRequestQueue().add(1, jsonRequest, new NetResponseListener());
        dm.buildWaitDialog(message);
    }

    //取消执行网络请求
    public void cancelNetWork() {

        MyApplication.getRequestQueue().cancelAll();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelNetWork();
    }

    /**
     * 建立网络请求
     * @param url 请求网址
     * @param method 请求方式 0 post 1 get
     * @param defaultParam 是否有默认请求参数
     * @return
     */
    public Request<JSONObject> buildNetRequest(String url, int method, boolean defaultParam) {
        Request<JSONObject> jsonRequest = NoHttp.createJsonObjectRequest(url, method == 0 ? RequestMethod.POST : RequestMethod.GET);
        if(defaultParam){
            int uuid = AppConfig.getInstance().getInt("uuid", -1000);
            String token = AppConfig.getInstance().getString("token", "");
            long expiring_time = AppConfig.getInstance().getLong("expiring_time", -2000);
            jsonRequest.add("sign", UrlEncodeUtils.createSign(url));
            jsonRequest.add("uuid", uuid);
            jsonRequest.add("token", token);
            jsonRequest.add("expiring_time", expiring_time);
        }
        return jsonRequest;
    }


    private class NetResponseListener implements OnResponseListener<JSONObject> {

        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if(dm!=null){
                dm.dismissDialog();
            }
            JSONObject jObj = response.get();
            int status = jObj.optInt("status");
            String message = jObj.optString("msg");
            if(status==200){
                JSONObject dataObj = jObj.optJSONObject("data");
                int update_status = dataObj.optInt("update_status");
                if (update_status == 1) {
                    AppConfig.getInstance().putString("token", dataObj.optString("token"));
                    AppConfig.getInstance().putLong("expiring_time", dataObj.optLong("expiring_time"));
                }
                if(callback!=null){//对于200额外的处理
                    callback.handle200Data(dataObj,message);
                }
            } else if(status==1||status==2||status==3||status == 4||status==5){//缺少sign参数
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                AppConfig.getInstance().putInt("uuid",-1000);
                startActivity(intent);
                ((MyApplication) getActivity().getApplication()).exit();
            } else if(status==404){
                dm.buildAlertDialog(message);
            } else if(status==500){
                dm.buildAlertDialog(message);
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            if(dm!=null){
                dm.dismissDialog();
            }
            Exception exc = response.getException();
            //String msg=exc.getMessage();
            if(exc instanceof NetworkError){
                //dm.buildAlertDialog("请打开网络！");
            }else {
                dm.buildAlertDialog("请求失败");
            }
        }
        @Override
        public void onFinish(int what) {
            if(dm!=null){
                dm.dismissDialog();
            }
        }
    }


}
