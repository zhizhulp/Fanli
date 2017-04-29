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
    private List<Dialog> dialogList = new ArrayList<>();
    private ProgressDialog dialogProgress;
    private Dialog dialogAlter;
    private Dialog dialogAlterSure;
    private Context context;
    private Callback dialogClick;

    public DialogHome(Context context) {
        this.context = context;
    }

    public void setCallback(Callback dialogClick) {
        this.dialogClick = dialogClick;
    }

    //创建进度对话框
    public DialogHome buildWaitDialog(String message) {
        dismissDialog();
        dialogProgress = new ProgressDialog(context, R.style.dialog);
        dialogList.add(dialogProgress);
        dialogProgress.setCanceledOnTouchOutside(false);//不可点击，返回键可以取消
        dialogProgress.setCancelable(true);//返还键不可取消
        dialogProgress.setMessage(message);
        dialogProgress.show();
        return this;
    }

    //创建提示对话框
    public DialogHome buildAlertDialog(String message) {
        dismissDialog();
        dialogAlter = new Dialog(context, R.style.AlertDialog);
        dialogList.add(dialogAlter);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        dialogAlter.setContentView(alertView);

        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
            }
        });
        dialogAlter.show();
        return this;
    }
    //可以处理确定的情况
    public DialogHome buildAlertDialog2(String message,final Callback dialogClick) {
        dismissDialog();
        dialogAlter = new Dialog(context, R.style.AlertDialog);
        dialogList.add(dialogAlter);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        dialogAlter.setContentView(alertView);

        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
            }
        });
        dialogAlter.show();
        return this;
    }

    /**
     * 可以处理确定和取消的情况
     */
    public DialogHome buildAlertDialogSure(String message, final Callback dialogClick) {
        dismissDialog();
        dialogAlterSure = new Dialog(context, R.style.AlertDialog);
        dialogList.add(dialogAlterSure);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        dialogAlterSure.setContentView(alertView);

        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
            }
        });
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_sure);//取消按钮
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (dialogClick != null) {
                    dialogClick.handleCancel();
                }
            }
        });

        tvMsg.setText(message);
        dialogAlterSure.show();
        return this;
    }

    public static abstract class Callback {
        public abstract void handleSure();

        public void handleCancel() {
        }
    }

    //隐藏所有对话框
    public void dismissDialog() {
        if (dialogList.size() != 0) {
            for (int i = 0; i < dialogList.size(); i++) {
                Dialog dialog = dialogList.get(i);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialogList.remove(dialog);
            }
        }
    }
}
