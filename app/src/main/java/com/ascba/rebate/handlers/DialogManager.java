package com.ascba.rebate.handlers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;

/**
 * 对话框管理类
 */

public class DialogManager {
    private Context context;
    private Dialog dialog;
    private Callback callback;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DialogManager(Context context){
        this.context=context;
    }
    public interface Callback{
        void handleSure();//处理确定的情况
    }


    //创建进度对话框
    public DialogManager buildWaitDialog(String message){
        dialog=new ProgressDialog(context, R.style.dialog);
        //dialog.setCanceledOnTouchOutside(true);//不可点击，返回键可以取消
        dialog.setCancelable(true);//返还键不可取消
        ((ProgressDialog) dialog).setMessage(message);
        return this;
    }
    //创建提示对话框1
    public DialogManager buildAlertDialog(String message) {
        dialog=new Dialog(context,R.style.AlertDialog);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        dialog.setContentView(alertView);
        dialog.show();
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return this;
    }
    //创建提示对话框1
    public DialogManager buildAlertDialog1(String message) {

        dialog=new Dialog(context,R.style.AlertDialog);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        dialog.setContentView(alertView);
        dialog.show();
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.handleSure();
                }
            }
        });
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//取消按钮
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null){
                    dialog.dismiss();
                }

            }
        });
        return this;
    }
    //显示对话框
    public void showDialog(){
        if(dialog!=null){
            dialog.show();
        }

    }
    //隐藏对话框
    public void dismissDialog(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}
