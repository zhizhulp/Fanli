package com.ascba.rebate.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.ascba.rebate.R;
import java.util.List;

/**
 * Created by 李平 on 2017/5/15 0015.18:20
 * 从底部抽出的dialog(地区切换)
 */

public class MyBottomSheet extends BottomSheetDialog {
    private List<String> data;
    private LayoutInflater inflater;
    public RadioGroup rg;
    private int index;//被选中的

    public MyBottomSheet(@NonNull Context context, List<String> data,int index) {
        super(context);
        this.data=data;
        this.index=index;
        init(context);
    }

    public MyBottomSheet(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        init(context);
    }

    protected MyBottomSheet(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init(Context context) {
        inflater=LayoutInflater.from(context);
        setContentView(R.layout.radio_group);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        if(rg!=null){
            for (int i = 0; i < data.size(); i++) {
                RadioButton rb = (RadioButton) inflater.inflate(R.layout.proxy_region_item, rg ,false);
                rb.setText(data.get(i));
                rb.setId(i);
                if(i==index){
                    rb.setChecked(true);
                }
                rg.addView(rb);
            }
        }
    }
}
