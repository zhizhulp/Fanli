package com.ascba.rebate.activities.me_page.business_center_child;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ShowPicActivity extends BaseNetActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        initViews();
        getDataFromIntent();
    }

    private void initViews() {
        imageView = ((ImageView) findViewById(R.id.image));
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            String image = intent.getStringExtra("image");
            if(image!=null){
                Picasso.with(this).load(image).fit().centerInside().placeholder(R.mipmap.busi_apply_show_pic_holder).into(imageView);
            }
        }
    }
}
