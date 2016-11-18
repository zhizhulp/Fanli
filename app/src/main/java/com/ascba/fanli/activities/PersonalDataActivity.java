package com.ascba.fanli.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ascba.fanli.R;
import com.ascba.fanli.activities.base.BaseActivity;
import com.ascba.fanli.activities.main.MainActivity;
import com.ascba.fanli.utils.LogUtils;
import com.ascba.fanli.utils.ScreenDpiUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    public static final int nickNameRequest= 0x06;
    public static final int sexRequest= 0x05;
    public static final int locationRequest= 0x04;
    public static final int ageRequest= 0x03;
    public static final int GO_CAMERA=0x01;
    private static final int GO_ALBUM=0x02;
    private CircleImageView userIconView;
    private PopupWindow popupWindow;
    private ImageView sexShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initViews();
    }

    private void initViews() {
        userIconView = ((CircleImageView) findViewById(R.id.head_icon));
        sexShow = ((ImageView) findViewById(R.id.show_sex));
    }

    //进入修改昵称界面
    public void personalNickNameChange(View view) {
        Intent intent=new Intent(this, ModifyNicknameActivity.class);
        startActivityForResult(intent,nickNameRequest);
    }

    //进入修改性别的页面
    public void personalSexChange(View view) {
        Intent intent=new Intent(this, SexChangeActivity.class);
        String tag = (String) sexShow.getTag();
        int tagInt = Integer.parseInt(tag);
        intent.putExtra("tag",tagInt);
        startActivityForResult(intent,sexRequest);
    }
    //进入修改地址的页面
    public void personalLocationChange(View view) {
        Intent intent=new Intent(this, LocationActivity.class);
        startActivityForResult(intent,locationRequest);
    }
    //进入修改年龄的页面
    public void personalAgeChange(View view) {
        Intent intent=new Intent(this, AgeChangeActivity.class);
        startActivityForResult(intent,ageRequest);
    }
    //进入修改用户头像的页面
    public void userIconClick(View view) {
        popupWindow=new PopupWindow(this);
        View popView = getLayoutInflater().inflate(R.layout.personal_select_icon_pop, null);
        View camera = popView.findViewById(R.id.head_icon_select_camera);
        View album = popView.findViewById(R.id.head_icon_select_album);
        View cancel = popView.findViewById(R.id.head_icon_select_cancel);
        camera.setOnClickListener(this);//相机拍照
        album.setOnClickListener(this);//相册选择
        cancel.setOnClickListener(this);//取消选择
        popupWindow.setContentView(popView);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.personal_head_icon_select_pop_bg));//外部点击消失
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params=getWindow().getAttributes();
                params.alpha=1.0f;
                getWindow().setAttributes(params);
            }
        });
        int screenWideDp = ScreenDpiUtils.px2dp(this, getResources().getDisplayMetrics().widthPixels);
        int popWide = ScreenDpiUtils.dip2px(this, screenWideDp - 30);
        popupWindow.setWidth(popWide);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        int marinBottom=ScreenDpiUtils.dip2px(this, 15);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,marinBottom);
        //设置背景变暗
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.alpha=0.5f;
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_icon_select_camera:
                popupWindow.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, GO_CAMERA);
                break;
            case R.id.head_icon_select_album:
                popupWindow.dismiss();
                Intent intent2 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2, GO_ALBUM);
                break;
            case R.id.head_icon_select_cancel:
                popupWindow.dismiss();

                break;
        }
    }

    public Bitmap handleBitmap(String picturePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        int scale = (int)( options.outWidth / (float)300);
        if(scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(picturePath, options);
    }
    public Bitmap handleBitmap(Uri uri){
        String path = uri.getPath();
        return handleBitmap(path);
    }

    //处理返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        switch (requestCode){
            case GO_CAMERA:
                Uri uri = data.getData();
                Bitmap bitmap1 = handleBitmap(uri);
                userIconView.setImageBitmap(bitmap1);
                break;
            case GO_ALBUM:
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmap = handleBitmap(picturePath);
                userIconView.setImageBitmap(bitmap);
                break;
            case sexRequest:
                switch (resultCode){
                    case SexChangeActivity.SELECT_MAN:
                        LogUtils.PrintLog("123PersonalDataActivity","man_back");
                        sexShow.setImageResource(R.mipmap.ic_go);
                        sexShow.setTag("1");
                        break;
                    case SexChangeActivity.SELECT_WOMAN:
                        LogUtils.PrintLog("123PersonalDataActivity","woman_back");
                        sexShow.setImageResource(R.mipmap.ic_back_blank);
                        sexShow.setTag("2");
                        break;
                }
                break;
        }
    }
}
