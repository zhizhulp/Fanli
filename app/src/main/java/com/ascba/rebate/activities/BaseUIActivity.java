package com.ascba.rebate.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ascba.rebate.R;

public class BaseUIActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_ui);

        initToolBar();
    }

    private void initToolBar() {
        toolbar = ((Toolbar) findViewById(R.id.toolbar));
        toolbar.setTitle("title");
        toolbar.setNavigationIcon(R.mipmap.abar_back);
        setSupportActionBar(toolbar);
    }
}
