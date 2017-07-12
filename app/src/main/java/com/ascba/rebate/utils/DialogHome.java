package com.ascba.rebate.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ascba.rebate.R;

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
        if(dialogProgress!=null && dialogProgress.isShowing()){
            dialogProgress.dismiss();
        }
        dialogProgress = new ProgressDialog(context, R.style.dialog);
        dialogProgress.setCanceledOnTouchOutside(false);//点击外部不可取消
        dialogProgress.setCancelable(true);//返还键可取消
        dialogProgress.setMessage(message);
        dialogProgress.show();
        return dialogProgress;
    }

    //创建提示对话框
    public Dialog buildAlertDialog(String message) {
        if(dialogAlter!=null && dialogAlter.isShowing()){
            dialogAlter.dismiss();
        }
        dialogAlter = new Dialog(context, R.style.AlertDialog);
        setCancel(dialogAlter);
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
        if(dialogAlter2!=null && dialogAlter2.isShowing()){
            dialogAlter2.dismiss();
        }
        dialogAlter2 = new Dialog(context, R.style.AlertDialog);
        setCancel(dialogAlter2);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        dialogAlter2.setContentView(alertView);

        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
                dialogAlter2.dismiss();
            }
        });
        dialogAlter2.show();
        return dialogAlter2;
    }

    public Dialog buildAlertDialog3(String message,String btnStr,final Callback dialogClick) {
        if(dialogAlter2!=null && dialogAlter2.isShowing()){
            dialogAlter2.dismiss();
        }
        dialogAlter2 = new Dialog(context, R.style.AlertDialog);
        setCancel(dialogAlter2);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        dialogAlter2.setContentView(alertView);

        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setText(btnStr);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
                dialogAlter2.dismiss();
            }
        });
        dialogAlter2.show();
        return dialogAlter2;
    }

    /**
     * 可以处理确定和取消的情况
     */
    public Dialog buildAlertDialogSure(String message, final Callback dialogClick) {
        if(dialogAlterSure!=null && dialogAlterSure.isShowing()){
            dialogAlterSure.dismiss();
        }
        dialogAlterSure = new Dialog(context, R.style.AlertDialog);
        setCancel(dialogAlterSure);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        dialogAlterSure.setContentView(alertView);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
                dialogAlterSure.dismiss();
            }
        });
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_sure);//取消按钮
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleCancel();
                }
                dialogAlterSure.dismiss();
            }
        });

        tvMsg.setText(message);

        Window window = dialogAlterSure.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display display = window.getWindowManager().getDefaultDisplay();
        wlp.width = ScreenDpiUtils.dip2px(context,260);
        window.setAttributes(wlp);

        dialogAlterSure.show();
        return dialogAlterSure;
    }
    /**
     * 可以处理确定和取消的情况，可以自定义button文字
     */
    public Dialog buildAlertDialogSure(String message,String leftBtn,String rightBtn, final Callback dialogClick) {
        if(dialogAlterSure!=null && dialogAlterSure.isShowing()){
            dialogAlterSure.dismiss();
        }
        dialogAlterSure = new Dialog(context, R.style.AlertDialog);
        setCancel(dialogAlterSure);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        dialogAlterSure.setContentView(alertView);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_sure);//取消按钮
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//确定按钮
        btCancel.setText(StringUtils.isEmpty(leftBtn) ? "取消" : leftBtn);
        btSure.setText(StringUtils.isEmpty(rightBtn) ? "确定" : rightBtn);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleSure();
                }
                dialogAlterSure.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleCancel();
                }
                dialogAlterSure.dismiss();
            }
        });

        tvMsg.setText(message);
        dialogAlterSure.show();
        return dialogAlterSure;
    }

    public static abstract class Callback {
        public abstract void handleSure();

       public void handleCancel() {
        }
    }

    private void setCancel(Dialog d){
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(false);
    }
}
