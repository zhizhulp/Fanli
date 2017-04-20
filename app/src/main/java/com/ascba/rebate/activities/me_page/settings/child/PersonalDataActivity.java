package com.ascba.rebate.activities.me_page.settings.child;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.activities.me_page.settings.child.personal_data_child.AgeChangeActivity;
import com.ascba.rebate.activities.me_page.settings.child.personal_data_child.LocationActivity;
import com.ascba.rebate.activities.me_page.settings.child.personal_data_child.ModifyNicknameActivity;
import com.ascba.rebate.activities.me_page.settings.child.personal_data_child.SexChangeActivity;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.RoundImageView;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PersonalDataActivity extends BaseNetWorkActivity implements View.OnClickListener, BaseNetWorkActivity.Callback {
    public static final int nickNameRequest = 0x06;
    public static final int sexRequest = 0x05;
    public static final int locationRequest = 0x04;
    public static final int ageRequest = 0x03;
    private static final int GO_CAMERA = 0x01;
    private static final int GO_ALBUM = 0x02;
    private RoundImageView userIconView;
    private PopupWindow popupWindow;
    private TextView sexShow;
    private TextView tvMobile;
    private TextView tvNickname;
    private TextView tvAge;
    private TextView tvLocation;
    private int finalScene;
    private int isCardId;
    private DialogManager dm;
    private String picturePath;
    private File file;
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        //StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
    }

    private void initViews() {
        dm = new DialogManager(this);
        userIconView = ((RoundImageView) findViewById(R.id.head_icon));
        tvMobile = ((TextView) findViewById(R.id.personal_data_mobile));
        tvNickname = ((TextView) findViewById(R.id.personal_data_nickname));
        sexShow = ((TextView) findViewById(R.id.show_sex));
        tvAge = ((TextView) findViewById(R.id.personal_data_age));
        tvLocation = ((TextView) findViewById(R.id.personal_data_location));
        requestPData(0);
    }

    private void requestPData(int scene) {
        finalScene = scene;
        if (scene == 0) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.userSet, 0, true);
            executeNetWork(request, "请稍候");
            setCallback(this);
        } else if (scene == 1) {
            Request<JSONObject> request = buildNetRequest(UrlUtils.updateSet, 0, true);
            if (picturePath != null) {
                file = new File(picturePath);
                saveBitmapFile(bitmap, file);
                request.add("avatar", new FileBinary(file));
            } else {
                request.add("avatar", "");
            }
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
            executeNetWork(request, "请稍候");
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
        if (isCardId == 1) {
            dm.buildAlertDialog("实名后不可修改性别");
        } else {
            Intent intent = new Intent(this, SexChangeActivity.class);
            CharSequence sex = sexShow.getText();
            intent.putExtra("tag", sex);
            startActivityForResult(intent, sexRequest);
        }
    }

    //进入修改地址的页面
    public void personalLocationChange(View view) {
        if (isCardId == 1) {
            dm.buildAlertDialog("实名后不可修改地址");
        } else {
            Intent intent = new Intent(this, LocationActivity.class);
            startActivityForResult(intent, locationRequest);
        }
    }

    //进入修改年龄的页面
    public void personalAgeChange(View view) {
        if (isCardId == 1) {
            dm.buildAlertDialog("实名后不可修改年龄");
        } else {
            Intent intent = new Intent(this, AgeChangeActivity.class);
            startActivityForResult(intent, ageRequest);
        }
    }

    //进入修改用户头像的页面
    public void userIconClick(View view) {
        //检查权限
        checkPermission();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                showPop();
            }
        } else {
            showPop();
        }
    }

    //申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //用户同意使用read
            showPop();
        } else {
            //用户不同意，自行处理即可
            Toast.makeText(this, "无法使用此功能，因为你拒绝了权限", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showPop() {
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
                getDiskCacheDir();//创建文件
                if (Build.VERSION.SDK_INT > 23) {//处理7.0的情况
                    Uri uri = FileProvider.getUriForFile(this, "com.ascba.rebate.provider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, GO_CAMERA);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, GO_CAMERA);
                }
                break;
            case R.id.head_icon_select_album:
                popupWindow.dismiss();
                Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        BitmapFactory.decodeFile(picturePath, options);
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
        switch (requestCode) {
            case GO_CAMERA:
                if (file != null && file.exists()) {
                    picturePath = file.getPath();
                    bitmap = handleBitmap(picturePath);
                    userIconView.setImageBitmap(bitmap);
                }
                break;
            case GO_ALBUM:
                if (data == null) {
                    return;
                }
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                bitmap = handleBitmap(picturePath);
                userIconView.setImageBitmap(bitmap);
                break;
            case sexRequest:
                if (data == null) {
                    return;
                }
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
                if (data == null) {
                    return;
                }
                String nickname = data.getStringExtra("nickname");
                tvNickname.setText(nickname);
                break;
            case ageRequest:
                if (data == null) {
                    return;
                }
                String age = data.getStringExtra("age");
                tvAge.setText(age);
                break;
            case locationRequest:
                if (data == null) {
                    return;
                }
                String location = data.getStringExtra("location");
                tvLocation.setText(location);
                break;
        }
    }

    //上传个人资料
    public void saveData(View view) {
        requestPData(1);
        MyApplication.isPersonalData = true;
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if (finalScene == 0) {
            JSONObject userInfo = dataObj.optJSONObject("userInfo");
            isCardId = dataObj.optInt("isCardId");
            String avatar = userInfo.optString("avatar");
            String mobile = userInfo.optString("mobile");
            String nickname = userInfo.optString("nickname");
            int sex = userInfo.optInt("sex");
            int age = userInfo.optInt("age");
            String location = userInfo.optString("location");
            Picasso.with(PersonalDataActivity.this).load(UrlUtils.baseWebsite + avatar).error(R.mipmap.logo).noPlaceholder().into(userIconView);
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
        } else if (finalScene == 1) {//修改成功的提示
            Toast.makeText(PersonalDataActivity.this, message, Toast.LENGTH_SHORT).show();
        }

    }

    public void getDiskCacheDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File dst = new File(Environment.getExternalStorageDirectory(), "com.ascba.rebate");

            if (!dst.exists()) {
                dst.mkdirs();
            }

            file = new File(dst, "com" + System.currentTimeMillis() + ".png");
        } else {
            file = getFilesDir();
        }
    }

    public void saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
