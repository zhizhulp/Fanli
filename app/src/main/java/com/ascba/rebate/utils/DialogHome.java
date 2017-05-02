package com.ascba.rebate.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/04/26 0026.
 */

public class DialogHome {
    private ProgressDialog dialogProgress;
    private Dialog dialogAlter;
    private Dialog dialogAlterSure;
    private Context context;
    private Callback dialogClick;
    private Dialog dialogAlter2;

    public DialogHome(Context context) {
        this.context = context;
    }

    public void setCallback(Callback dialogClick) {
        this.dialogClick = dialogClick;
    }

    //创建进度对话框
    public Dialog buildWaitDialog(String message) {
        dialogProgress = new ProgressDialog(context, R.style.dialog);
        dialogProgress.setCanceledOnTouchOutside(false);//不可点击，返回键可以取消
        dialogProgress.setCancelable(true);//返还键不可取消
        dialogProgress.setMessage(message);
        dialogProgress.show();
        return dialogProgress;
    }

    //创建提示对话框
    public Dialog buildAlertDialog(String message) {
        dialogAlter = new Dialog(context, R.style.AlertDialog);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        dialogAlter.setContentView(alertView);

        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlter.dismiss();
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
            }
        });
        dialogAlter.show();
        return dialogAlter;
    }
    //可以处理确定的情况
    public Dialog buildAlertDialog2(String message,final Callback dialogClick) {
        dialogAlter2 = new Dialog(context, R.style.AlertDialog);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        dialogAlter.setContentView(alertView);

        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlter2.dismiss();
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
            }
        });
        dialogAlter.show();
        return dialogAlter2;
    }

    /**
     * 可以处理确定和取消的情况
     */
    public Dialog buildAlertDialogSure(String message, final Callback dialogClick) {
        dialogAlterSure = new Dialog(context, R.style.AlertDialog);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        dialogAlterSure.setContentView(alertView);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlterSure.dismiss();
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
            }
        });
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_sure);//取消按钮
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlterSure.dismiss();
                if (dialogClick != null) {
                    dialogClick.handleCancel();
                }
            }
        });

        tvMsg.setText(message);
        dialogAlterSure.show();
        return dialogAlterSure;
    }

    public static abstract class Callback {
        public abstract void handleSure();

        void handleCancel() {
        }
    }
}
