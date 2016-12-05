package com.ascba.rebate.activities;

import android.app.Activity;
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
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.activities.login.LoginActivity;
import com.ascba.rebate.handlers.CheckThread;
import com.ascba.rebate.handlers.PhoneHandler;
import com.ascba.rebate.utils.LogUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlEncodeUtils;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.BitmapBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    public static final int nickNameRequest = 0x06;
    public static final int sexRequest = 0x05;
    public static final int locationRequest = 0x04;
    public static final int ageRequest = 0x03;
    public static final int GO_CAMERA = 0x01;
    private static final int GO_ALBUM = 0x02;
    private CircleImageView userIconView;
    private PopupWindow popupWindow;
    private TextView sexShow;
    private PhoneHandler phoneHandler;
    private CheckThread checkThread;
    private RequestQueue requestQueue;
    private SharedPreferences sf;
    private TextView tvMobile;
    private TextView tvNickname;
    private TextView tvAge;
    private TextView tvLocation;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initViews();
    }

    private void initViews() {
        sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        userIconView = ((CircleImageView) findViewById(R.id.head_icon));
        tvMobile = ((TextView) findViewById(R.id.personal_data_mobile));
        tvNickname = ((TextView) findViewById(R.id.personal_data_nickname));
        sexShow = ((TextView) findViewById(R.id.show_sex));
        tvAge = ((TextView) findViewById(R.id.personal_data_age));
        tvLocation = ((TextView) findViewById(R.id.personal_data_location));
        sendMsgToSevr("http://api.qlqwgw.com/v1/userSet", 0);
    }

    //进入修改昵称界面
    public void personalNickNameChange(View view) {
        Intent intent = new Intent(this, ModifyNicknameActivity.class);
        startActivityForResult(intent, nickNameRequest);
    }

    //进入修改性别的页面
    public void personalSexChange(View view) {
        Intent intent = new Intent(this, SexChangeActivity.class);
        CharSequence sex = sexShow.getText();
        intent.putExtra("tag", sex);
        startActivityForResult(intent, sexRequest);
    }

    //进入修改地址的页面
    public void personalLocationChange(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, locationRequest);
    }

    //进入修改年龄的页面
    public void personalAgeChange(View view) {
        Intent intent = new Intent(this, AgeChangeActivity.class);
        startActivityForResult(intent, ageRequest);
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

    public Bitmap handleBitmap(Uri uri) {
        String path = uri.getPath();
        return handleBitmap(path);
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
                Uri uri = data.getData();
                bitmap = handleBitmap(uri);
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

    private void sendMsgToSevr(String baseUrl, final int type) {
        int uuid = sf.getInt("uuid", -1000);
        String token = sf.getString("token", "");
        long expiring_time = sf.getLong("expiring_time", -2000);

        requestQueue = NoHttp.newRequestQueue();
        final ProgressDialog dialog = new ProgressDialog(this, R.style.dialog);
        dialog.setMessage("请稍后");
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest(baseUrl + "?", RequestMethod.POST);
        objRequest.add("sign", UrlEncodeUtils.createSign(baseUrl));
        objRequest.add("uuid", uuid);
        objRequest.add("token", token);
        objRequest.add("expiring_time", expiring_time);
        if (type == 1) {
            objRequest.add("avatar", new BitmapBinary(bitmap, "headicon"));
            objRequest.add("nickname", tvNickname.getText().toString());
            String s = sexShow.getText().toString();
            if (s.equals("男")) {
                objRequest.add("sex", 1);
            } else if (s.equals("女")) {
                objRequest.add("sex", 0);
            } else {
                objRequest.add("sex", 2);
            }
            objRequest.add("age", tvAge.getText().toString());
            objRequest.add("location", tvLocation.getText().toString());
        }
        phoneHandler = new PhoneHandler(this);
        phoneHandler.setCallback(new PhoneHandler.Callback() {
            @Override
            public void getMessage(Message msg) {
                dialog.dismiss();
                JSONObject jObj = (JSONObject) msg.obj;
                LogUtils.PrintLog("123PersonalActivity", jObj.toString());
                try {
                    int status = jObj.optInt("status");
                    JSONObject dataObj = jObj.optJSONObject("data");
                    int update_status = dataObj.optInt("update_status");
                    if (status == 200) {
                        if (type == 1) {
                            Toast.makeText(PersonalDataActivity.this, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject userInfo = dataObj.getJSONObject("userInfo");
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
                            if (update_status == 1) {
                                sf.edit()
                                        .putString("token", dataObj.optString("token"))
                                        .putLong("expiring_time", dataObj.optLong("expiring_time"))
                                        .apply();
                            }
                        }
                    } else if (status == 5) {
                        Toast.makeText(PersonalDataActivity.this, jObj.optString("msg"), Toast.LENGTH_SHORT).show();
                    } else if (status == 3) {
                        Intent intent = new Intent(PersonalDataActivity.this, LoginActivity.class);
                        sf.edit().putInt("uuid", -1000).apply();
                        startActivity(intent);
                        finish();
                    } else if (status == 404) {
                        Toast.makeText(PersonalDataActivity.this, jObj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PersonalDataActivity.this, "未知原因", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        checkThread = new CheckThread(requestQueue, phoneHandler, objRequest);
        checkThread.start();
        //登录中对话框
        dialog.show();
    }

    //上传个人资料
    public void saveData(View view) {
        sendMsgToSevr("http://api.qlqwgw.com/v1/updateSet", 1);
    }
}
