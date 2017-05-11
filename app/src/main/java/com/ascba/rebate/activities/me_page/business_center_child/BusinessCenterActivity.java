package com.ascba.rebate.activities.me_page.business_center_child;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.debug.hv.ViewServer;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.DialogHome;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconManager;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 商户中心 商户资料提交页面
 */
public class BusinessCenterActivity extends BaseNetActivity implements BaseNetActivity.Callback {

    private TextView tvName;
    private TextView tvOperName;
    private TextView tvRegMon;
    private TextView tvStatus;
    private TextView tvScope;
    private Button btnCommit;
    private SelectIconManager smWorkPic;
    private SelectIconManager smAuthPic;
    private File fileWork;
    private File fileAuth;
    private static final int GO_CAMERA_WORK = 0x01;
    private static final int GO_ALBUM_WORK = 0x02;
    private static final int GO_CAMERA_AUTH = 0x03;
    private static final int GO_ALBUM_AUTH = 0x04;
    private TextView edAuthName;
    private View workPicView;
    private View authPicView;
    private View authView;
    private String chartered;
    private String warrant;
    private ImageView imWorkIcon;
    private ImageView imAuthIcon;
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private int scene;//区分网络请求
    private int type;//区分公司状态


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewServer.get(this).addWindow(this);
        setContentView(R.layout.activity_business_center);
        initViews();
        requestNetwork();
    }

    private void requestNetwork() {
        scene=1;
        Request<JSONObject> request = buildNetRequest(UrlUtils.getCompany, 0, true);
        executeNetWork(request,"请稍后");
        setCallback(this);
    }

    private void initViews() {
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvOperName = ((TextView) findViewById(R.id.tv_oper_name));
        tvRegMon = ((TextView) findViewById(R.id.tv_regist_capi));
        tvStatus = ((TextView) findViewById(R.id.tv_company_status));
        tvScope = ((TextView) findViewById(R.id.tv_scope));
        btnCommit = ((Button) findViewById(R.id.btn_commit));
        edAuthName = ((TextView) findViewById(R.id.ed_auth_name));
        workPicView = findViewById(R.id.work_pic_container);
        authPicView = findViewById(R.id.auth_pic_container);
        authView = findViewById(R.id.auth_view);
        imWorkIcon = ((ImageView) findViewById(R.id.busi_work_icon));
        imAuthIcon = ((ImageView) findViewById(R.id.busi_auth_icon));
    }


    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(scene==1){
            JSONObject intent = dataObj.optJSONObject("company");
            String name = intent.optString("name");
            String oper_name = intent.optString("oper_name");
            String regist_capi = intent.optString("regist_capi");
            String company_status = intent.optString("company_status");
            String scope = intent.optString("scope");
            int is_oper_name = intent.optInt("is_oper_name", -1);// 0:与法人信息一致，1：与法人信息不一致
            chartered = intent.optString("chartered");//营业执照图片链接
            if (chartered != null) {
                Picasso.with(this).load(UrlUtils.baseWebsite + chartered).placeholder(R.mipmap.bc_icon).into(imWorkIcon);
            }
            if (is_oper_name == 0) {
                authView.setVisibility(View.GONE);
            } else if (is_oper_name == 1) {
                authView.setVisibility(View.VISIBLE);
                String clientele_name = intent.optString("clientele_name");
                warrant = intent.optString("warrant");//授权书图片链接
                if (warrant != null) {
                    Picasso.with(this).load(UrlUtils.baseWebsite + warrant).placeholder(R.mipmap.bc_icon).into(imAuthIcon);
                }
                edAuthName.setText(clientele_name);
            }
            tvName.setText(name);
            tvOperName.setText(oper_name);
            tvRegMon.setText(regist_capi);
            tvStatus.setText(company_status);
            tvScope.setText(scope);
            if (type == -1) {//用户填写的资料
                btnCommit.setText("提交");
                btnCommit.setEnabled(true);
            } else if (type == 0) {//审核中的资料
                btnCommit.setText("已提交，等待客服审核中");
                btnCommit.setEnabled(false);
                btnCommit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
            } else if (type == 1) {//资料有误的资料
                btnCommit.setText("资料有误,点击重新审核");
                btnCommit.setEnabled(true);
            }
        }else {
            getDm().buildAlertDialogSure(message,new DialogHome.Callback() {
                @Override
                public void handleSure() {
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            });
            btnCommit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
            btnCommit.setText("已提交，等待客服审核中");
            workPicView.setEnabled(false);
            authPicView.setEnabled(false);
            btnCommit.setEnabled(false);
        }
    }

    @Override
    public void handle404(String message) {
        getDm().buildAlertDialog(message);
    }

    @Override
    public void handleNoNetWork() {
    }

    //提交商家资料
    public void goCommit(View view) {
        if (type == -1) {//提交商家资料
            Request<JSONObject> request = buildNetRequest(UrlUtils.addCompany, 0, true);
            request.add("company_name", tvName.getText().toString());
            if (fileWork == null) {
                getDm().buildAlertDialog("请上传营业执照");
                return;
            }
            if (authView.getVisibility() == View.VISIBLE && fileAuth == null) {
                getDm().buildAlertDialog("请上传授权书");
                return;
            }
            request.add("chartered", new FileBinary(fileWork));//营业执照
            if (fileAuth == null) {
                request.add("warrant", "");
            } else {
                request.add("warrant", new FileBinary(fileAuth));//授权书
            }
            executeNetWork(request, "请稍后");
            setCallback(this);
        } else if (type == 1) {//资料有误
            Request<JSONObject> request = buildNetRequest(UrlUtils.resubmitCompany, 0, true);
            request.add("company_name", tvName.getText().toString());
            if (fileWork == null) {
                getDm().buildAlertDialog("请上传营业执照");
                return;
            }
            if (authView.getVisibility() == View.VISIBLE && fileAuth == null) {
                getDm().buildAlertDialog("请上传授权书");
                return;
            }
            request.add("chartered", new FileBinary(fileWork));//营业执照
            if (fileAuth == null) {
                request.add("warrant", "");
            } else {
                request.add("warrant", new FileBinary(fileAuth));//授权书
            }
            executeNetWork(request, "上传中，请稍后...");
            setCallback(this);
        }

    }

    //选择营业执照
    public void uploadWorkPic(View view) {
        if (type == -1 || type == 1) {
            checkPermission();
        } else if (type == 0) {//审核中，展示图片
            Intent intent = new Intent(this, ShowPicActivity.class);
            if (chartered != null) {
                intent.putExtra("image", chartered);
                startActivity(intent);
            }
        }
    }

    //选择授权书
    public void uploadAuthPic(View view) {
        if (type == -1 || type == 1) {
            type = 1;
            checkPermission();
        } else if (type == 0) {//审核中，展示图片
            Intent intent = new Intent(this, ShowPicActivity.class);
            if (warrant != null) {
                intent.putExtra("image", warrant);
                startActivity(intent);
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                showPop(type);
            }
        } else {
            showPop(type);
        }
    }

    //申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //用户同意使用read
            showPop(type);
        } else {
            //用户不同意，自行处理即可
            Toast.makeText(this, "无法使用此功能，因为你拒绝了权限", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showPop(int type) {
        if (type == 0) {//显示营业执照
            smWorkPic = new SelectIconManager(this);
            smWorkPic.setCallback(new SelectIconManager.Callback() {
                @Override
                public void clickCamera() {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileWork = getDiskCacheDir();//创建文件
                    if (Build.VERSION.SDK_INT > 23) {//处理7.0的情况
                        Uri uri = FileProvider.getUriForFile(BusinessCenterActivity.this, "com.ascba.rebate.provider", fileWork);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, GO_CAMERA_WORK);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileWork));
                        startActivityForResult(intent, GO_CAMERA_WORK);
                    }

                }

                @Override
                public void clickAlbum() {
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, GO_ALBUM_WORK);
                }
            });
        } else if (type == 1) {//显示授权书
            smAuthPic = new SelectIconManager(this);
            smAuthPic.setCallback(new SelectIconManager.Callback() {
                @Override
                public void clickCamera() {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileAuth = getDiskCacheDir();//创建文件
                    if (Build.VERSION.SDK_INT > 23) {//处理7.0的情况
                        Uri uri = FileProvider.getUriForFile(BusinessCenterActivity.this, "com.ascba.rebate.provider", fileAuth);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, GO_CAMERA_AUTH);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileAuth));
                        startActivityForResult(intent, GO_CAMERA_AUTH);
                    }


                }

                @Override
                public void clickAlbum() {
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, GO_ALBUM_AUTH);
                }
            });
        }
    }

    private File getDiskCacheDir() {
        File file;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File dst = new File(Environment.getExternalStorageDirectory(), "com.ascba.rebate");

            if (!dst.exists()) {
                dst.mkdirs();
            }

            file = new File(dst, "com" + System.currentTimeMillis() + ".png");
        } else {
            file = getFilesDir();
        }
        return file;
    }

    public Bitmap handleBitmap(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        int scale = (int) (options.outWidth / (float) 300);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GO_CAMERA_WORK:
                if (fileWork != null && fileWork.exists()) {
                    Bitmap bitmap = handleBitmap(fileWork);
                    saveBitmapFile(bitmap, fileWork);
                    imWorkIcon.setImageBitmap(bitmap);
                }
                break;
            case GO_ALBUM_WORK:
                if (data == null) {
                    return;
                }
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    fileWork = new File(picturePath);
                    Bitmap bitmap = handleBitmap(fileWork);
                    saveBitmapFile(bitmap, fileWork);
                    imWorkIcon.setImageBitmap(bitmap);
                }
                break;
            case GO_CAMERA_AUTH:
                if (fileAuth != null && fileAuth.exists()) {
                    Bitmap bitmap = handleBitmap(fileAuth);
                    saveBitmapFile(bitmap, fileAuth);
                    imAuthIcon.setImageBitmap(bitmap);
                }
                break;
            case GO_ALBUM_AUTH:
                if (data == null) {
                    return;
                }
                Uri selectedImage2 = data.getData();
                String[] filePathColumn2 = {MediaStore.Images.Media.DATA};
                Cursor cursor2 = getContentResolver().query(selectedImage2,
                        filePathColumn2, null, null, null);
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    int columnIndex = cursor2.getColumnIndex(filePathColumn2[0]);
                    String picturePath = cursor2.getString(columnIndex);
                    cursor2.close();
                    fileAuth = new File(picturePath);
                    Bitmap bitmap = handleBitmap(fileAuth);
                    saveBitmapFile(bitmap, fileAuth);
                    imAuthIcon.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }
}
