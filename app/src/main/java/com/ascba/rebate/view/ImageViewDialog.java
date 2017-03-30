package com.ascba.rebate.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ViewPagerAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李鹏 on 2017/03/30 0030.
 *
 */

public class ImageViewDialog {

    private Context context;
    private Dialog dialog;
    private ViewPager viewPager;
    private List<String> imgList;
    private ViewPagerAdapter viewpagerAdpter;
    private List<View> viewList;


    public ImageViewDialog(Context context, List<String> imgList) {
        this.context = context;
        this.imgList = imgList;
        initDialog();
    }

    private void initDialog() {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.dialog_imageview);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_viewpager, null);
            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            dialog.setContentView(view);
        }
        viewList = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
            View imgView = LayoutInflater.from(context).inflate(R.layout.dialog_imageview, null);
            PhotoView photoView = (PhotoView) imgView.findViewById(R.id.photo_view);
            Picasso.with(context).load(imgList.get(i)).into(photoView);
            TextView textView = (TextView) imgView.findViewById(R.id.num);
            textView.setText((i + 1) + "/" + imgList.size());
            viewList.add(imgView);
        }
        viewpagerAdpter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(viewpagerAdpter);

        dialog.show();
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
        initDialog();
    }

    public Dialog getDialog() {
        return dialog;
    }
}
