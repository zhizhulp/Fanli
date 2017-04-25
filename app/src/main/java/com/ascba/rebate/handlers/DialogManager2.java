package com.ascba.rebate.handlers;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话框管理类
 */

public class DialogManager2 {
    private Context context;
    private List<Dialog> dialogList=new ArrayList<>();
    private Callback callback;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DialogManager2(Context context){
        this.context=context;
    }
    public interface Callback{
        void handleSure();//处理确定的情况
    }


    //创建进度对话框
    public DialogManager2 buildWaitDialog(String message){
        Dialog dialog=new ProgressDialog(context, R.style.dialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogList.remove(dialog);
            }
        });
        dialogList.add(dialog);
        dialog.setCanceledOnTouchOutside(false);//不可点击，返回键可以取消
        dialog.setCancelable(true);//返还键不可取消
        ((ProgressDialog) dialog).setMessage(message);
        dialog.show();
        return this;
    }
    //创建提示对话框
    public DialogManager2 buildAlertDialog(String message) {
        final Dialog dialog=new Dialog(context,R.style.AlertDialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogList.remove(dialog);
            }
        });
        dialogList.add(dialog);
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

    /**
     *可以处理确定和取消的情况
     */
    public DialogManager2 buildAlertDialog1(String message) {
        final Dialog dialog=new Dialog(context,R.style.AlertDialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogList.remove(dialog);
            }
        });
        dialogList.add(dialog);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        dialog.setContentView(alertView);
        dialog.show();
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.handleSure();
                }
            }
        });
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_sure);//取消按钮
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     *只处理确定情况的dialog
     */
    public DialogManager2 buildAlertDialog2(String message) {
        final Dialog dialog=new Dialog(context,R.style.AlertDialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogList.remove(dialog);
            }
        });
        dialogList.add(dialog);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
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
        return this;
    }

    //隐藏所有对话框
    public void dismissDialog(){
        if(dialogList.size()!=0) {
            for (int i = 0; i < dialogList.size(); i++) {
                Dialog dialog = dialogList.get(i);
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }
}
