package com.ascba.rebate.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.Base2Activity;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.NetUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.BitmapBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDataActivity extends Base2Activity implements View.OnClickListener,Base2Activity.Callback {
    public static final int nickNameRequest = 0x06;
    public static final int sexRequest = 0x05;
    public static final int locationRequest = 0x04;
    public static final int ageRequest = 0x03;
    public static final int GO_CAMERA = 0x01;
    private static final int GO_ALBUM = 0x02;
    private CircleImageView userIconView;
    private PopupWindow popupWindow;
    private TextView sexShow;
    private TextView tvMobile;
    private TextView tvNickname;
    private TextView tvAge;
    private TextView tvLocation;
    private Bitmap bitmap;
    private int finalScene;
    private int isCardId;
    private DialogManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initViews();
    }

    private void initViews() {
        dm=new DialogManager(this);
        userIconView = ((CircleImageView) findViewById(R.id.head_icon));
        tvMobile = ((TextView) findViewById(R.id.personal_data_mobile));
        tvNickname = ((TextView) findViewById(R.id.personal_data_nickname));
        sexShow = ((TextView) findViewById(R.id.show_sex));
        tvAge = ((TextView) findViewById(R.id.personal_data_age));
        tvLocation = ((TextView) findViewById(R.id.personal_data_location));
        requestPData(0);
    }

    private void requestPData(int scene) {
        finalScene=scene;
        if(scene==0){
            Request<JSONObject> request = buildNetRequest(UrlUtils.userSet, 0, true);
            executeNetWork(request,"请稍候");
            setCallback(this);
        }else if(scene==1){
            Request<JSONObject> request = buildNetRequest(UrlUtils.updateSet, 0, true);
            request.add("avatar", new BitmapBinary(bitmap, "headicon"));
            request.add("nickname", tvNickname.getText().toString());
            String s = sexShow.getText().toString();
            if (s.equals("男")) {
                request.add("sex", 1);
            } else if (s.equals("女")) {
                request.add("sex", 0);
            } else {
                request.add("sex", 2);
            }
            request.add("age", tvAge.getText().toString());
            request.add("location", tvLocation.getText().toString());
            executeNetWork(request,"请稍候");
            setCallback(this);
        }
    }

    //进入修改昵称界面
    public void personalNickNameChange(View view) {
        Intent intent = new Intent(this, ModifyNicknameActivity.class);
        startActivityForResult(intent, nickNameRequest);
    }

    //进入修改性别的页面
    public void personalSexChange(View view) {
        if(isCardId==1){
            dm.buildAlertDialog("实名后不可修改性别");
        }else{
            Intent intent = new Intent(this, SexChangeActivity.class);
            CharSequence sex = sexShow.getText();
            intent.putExtra("tag", sex);
            startActivityForResult(intent, sexRequest);
        }

    }

    //进入修改地址的页面
    public void personalLocationChange(View view) {
        if(isCardId==1){
            dm.buildAlertDialog("实名后不可修改地址");
        }else{
            Intent intent = new Intent(this, LocationActivity.class);
            startActivityForResult(intent, locationRequest);
        }
    }

    //进入修改年龄的页面
    public void personalAgeChange(View view) {
        if(isCardId==1){
            dm.buildAlertDialog("实名后不可修改年龄");
        }else{
            Intent intent = new Intent(this, AgeChangeActivity.class);
            startActivityForResult(intent, ageRequest);
        }
    }

    //进入修改用户头像的页面
    public void userIconClick(View view) {
        popupWindow = new PopupWindow(this);
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
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });
        int screenWideDp = ScreenDpiUtils.px2dp(this, getResources().getDisplayMetrics().widthPixels);
        int popWide = ScreenDpiUtils.dip2px(this, screenWideDp - 30);
        popupWindow.setWidth(popWide);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        int marinBottom = ScreenDpiUtils.dip2px(this, 15);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, marinBottom);
        //设置背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    public Bitmap handleBitmap(String picturePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        int scale = (int) (options.outWidth / (float) 300);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(picturePath, options);
    }


    //处理返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case GO_CAMERA:
                Bundle extras = data.getExtras();
                bitmap= (Bitmap) extras.get("data");
                userIconView.setImageBitmap(bitmap);
                break;
            case GO_ALBUM:
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                bitmap = handleBitmap(picturePath);
                userIconView.setImageBitmap(bitmap);
                break;
            case sexRequest:
                switch (resultCode) {
                    case SexChangeActivity.SELECT_MAN:
                        sexShow.setText("男");
                        break;
                    case SexChangeActivity.SELECT_WOMAN:
                        sexShow.setText("女");
                        break;
                    case SexChangeActivity.SELECT_PRI:
                        sexShow.setText("保密");
                        break;
                }
                break;
            case nickNameRequest:
                String nickname = data.getStringExtra("nickname");
                tvNickname.setText(nickname);
                break;
            case ageRequest:
                String age = data.getStringExtra("age");
                tvAge.setText(age);
                break;
            case locationRequest:
                String location = data.getStringExtra("location");
                tvLocation.setText(location);
                break;
        }
    }
    //上传个人资料
    public void saveData(View view) {
        requestPData(1);
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){
            JSONObject userInfo = dataObj.optJSONObject("userInfo");
            isCardId = dataObj.optInt("isCardId");
            String avatar = userInfo.optString("avatar");
            String mobile = userInfo.optString("mobile");
            String nickname = userInfo.optString("nickname");
            int sex = userInfo.optInt("sex");
            int age = userInfo.optInt("age");
            String location = userInfo.optString("location");
            Picasso.with(PersonalDataActivity.this).load(avatar).into(userIconView);
            tvMobile.setText(mobile);
            tvNickname.setText(nickname);
            if (sex == 0) {
                sexShow.setText("女");
            } else if (sex == 1) {
                sexShow.setText("男");
            } else {
                sexShow.setText("保密");
            }
            tvAge.setText(age + "");
            if (location != null) {
                tvLocation.setText(location);
            }
        }else if(finalScene==1){//修改成功的提示
            Toast.makeText(PersonalDataActivity.this, message, Toast.LENGTH_SHORT).show();
        }

    }
}