package com.ascba.rebate.activities.me_page.business_center_child;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ShowPicActivity extends BaseNetWorkActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.moneyBarColor));
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
                Picasso.with(this).load(UrlUtils.baseWebsite+image).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.busi_apply_show_pic_holder).into(imageView);

            }
        }
    }
}
