package com.ascba.rebate.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.PsdUtils;


public class TestActivity extends BaseNetActivity {

    private EditText editText;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        editText = (EditText) findViewById(R.id.edit);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = PsdUtils.encryptPsd(editText.getText().toString());
                textView.setText(text + "\n" + PsdUtils.getPayPsd(editText.getText().toString()));
            }
        });
    }
}