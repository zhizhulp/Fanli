package com.ascba.rebate.utils;

import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/6/14.
 * 数字格式化
 */

public class NumberFormatUtils {
    public static String getNewDouble(Double oriDouble) {
        return new DecimalFormat("##0.00").format(oriDouble);
    }

//    public static double getNewDouble(Double oriDouble) {
//
//        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
//        nf.setGroupingUsed(false);
//
//        return nf.format(oriDouble);
//    }


    public static void formatEtMoney(EditText etMoney, CharSequence s,String preChangeTxt) {

        //只要前面的三位数
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                etMoney.setText(s);
                etMoney.setSelection(s.length());
            }
        }
        //开够输入.自动变为0.x的类型
        if (s.toString().trim().substring(0).equals(".")) {
            if (preChangeTxt.toString().trim().length() == 0) {
                s = "0" + s;
                etMoney.setText(s);
                etMoney.setSelection(2);
            }
        }

        if (s.toString().trim().substring(0).equals("0")) {
            if (preChangeTxt.toString().trim().length() == 0) {
                s = s + ".";
                etMoney.setText(s);
                etMoney.setSelection(2);
            }
        }

        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                etMoney.setText(s.subSequence(0, 1));
                etMoney.setSelection(1);
                return;
            }
        }
        preChangeTxt = s.toString().trim();
    }

}
