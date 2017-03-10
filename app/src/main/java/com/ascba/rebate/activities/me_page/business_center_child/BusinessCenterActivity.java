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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconManager;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 商户中心 商户资料提交页面
 */
public class BusinessCenterActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {

    private TextView tvName;
    private TextView tvOperName;
    private TextView tvRegMon;
    private TextView tvStatus;
    private TextView tvScope;
    private DialogManager dm;
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
    private int finalType;
    private View authView;
    private String chartered;
    private String warrant;
    private ImageView imWorkIcon;
    private ImageView imAuthIcon;
    private String[] permissions=new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_center);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            int type = intent.getIntExtra("type", -1);
            finalType=type;
            String name = intent.getStringExtra("name");
            String oper_name = intent.getStringExtra("oper_name");
            String regist_capi = intent.getStringExtra("regist_capi");
            String company_status = intent.getStringExtra("company_status");
            String scope = intent.getStringExtra("scope");
            int is_oper_name = intent.getIntExtra("is_oper_name", -1);// 0:与法人信息一致，1：与法人信息不一致
            chartered = intent.getStringExtra("chartered");//营业执照图片链接
            if(chartered!=null){
                Picasso.with(this).load(UrlUtils.baseWebsite+chartered).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.bc_icon).into(imWorkIcon);
            }
            if(is_oper_name==0){
                authView.setVisibility(View.GONE);
            }else if(is_oper_name==1){
                authView.setVisibility(View.VISIBLE);
                String clientele_name = intent.getStringExtra("clientele_name");
                warrant = intent.getStringExtra("warrant");//授权书图片链接
                if(warrant!=null){
                    Picasso.with(this).load(UrlUtils.baseWebsite+warrant).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.bc_icon).into(imAuthIcon);
                }
                edAuthName.setText(clientele_name);
            }
            tvName.setText(name);
            tvOperName.setText(oper_name);
            tvRegMon.setText(regist_capi);
            tvStatus.setText(company_status);
            tvScope.setText(scope);
            if(type==-1){//用户填写的资料
                btnCommit.setText("提交");
                btnCommit.setEnabled(true);
            }else if(type==0){//审核中的资料
                btnCommit.setText("已提交，等待客服审核中");
                btnCommit.setEnabled(false);
                btnCommit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
            }else if(type==1){//资料有误的资料
                btnCommit.setText("资料有误,点击重新审核");
                btnCommit.setEnabled(true);
            }
        }
    }

    private void initViews() {
        dm=new DialogManager(this);
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvOperName = ((TextView) findViewById(R.id.tv_oper_name));
        tvRegMon = ((TextView) findViewById(R.id.tv_regist_capi));
        tvStatus = ((TextView) findViewById(R.id.tv_company_status));
        tvScope= ((TextView) findViewById(R.id.tv_scope));
        btnCommit = ((Button) findViewById(R.id.btn_commit));
        edAuthName = ((TextView) findViewById(R.id.ed_auth_name));
        workPicView = findViewById(R.id.work_pic_container);
        authPicView = findViewById(R.id.auth_pic_container);
        authView = findViewById(R.id.auth_view);
        imWorkIcon = ((ImageView) findViewById(R.id.busi_work_icon));
        imAuthIcon = ((ImageView) findViewById(R.id.busi_auth_icon));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GO_CAMERA_WORK:
                if(fileWork!=null&&fileWork.exists()){
                    Bitmap bitmap = handleBitmap(fileWork);
                    saveBitmapFile(bitmap,fileWork);
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
                    saveBitmapFile(bitmap,fileWork);
                    imWorkIcon.setImageBitmap(bitmap);
                }
                break;
            case GO_CAMERA_AUTH:
                if(fileAuth!=null&&fileAuth.exists()){
                    Bitmap bitmap = handleBitmap(fileAuth);
                    saveBitmapFile(bitmap,fileAuth);
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
                    saveBitmapFile(bitmap,fileAuth);
                    imAuthIcon.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        dm.buildAlertDialog2(message);
        dm.setCallback(new DialogManager.Callback() {
            @Override
            public void handleSure() {
                dm.dismissDialog();
                setResult(RESULT_OK,getIntent());
                finish();
            }
        });
        btnCommit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
        btnCommit.setText("已提交，等待客服审核中");
        workPicView.setEnabled(false);
        authPicView.setEnabled(false);
        btnCommit.setEnabled(false);
    }
    //提交商家资料
    public void goCommit(View view) {
        if(finalType==-1){//提交商家资料
            Request<JSONObject> request = buildNetRequest(UrlUtils.addCompany, 0, true);
            request.add("company_name",tvName.getText().toString());
            if(fileWork==null){
                dm.buildAlertDialog("请上传营业执照");
                return;
            }
            if(authView.getVisibility()==View.VISIBLE && fileAuth==null){
                dm.buildAlertDialog("请上传授权书");
                return;
            }
            request.add("chartered",new FileBinary(fileWork));//营业执照
            if(fileAuth==null){
                request.add("warrant","");
            }else {
                request.add("warrant",new FileBinary(fileAuth));//授权书
            }
            executeNetWork(request,"请稍后");
            setCallback(this);
        }else if(finalType==1){//资料有误
            Request<JSONObject> request = buildNetRequest(UrlUtils.resubmitCompany, 0, true);
            request.add("company_name",tvName.getText().toString());
            if(fileWork==null){
                dm.buildAlertDialog("请上传营业执照");
                return;
            }
            if(authView.getVisibility()==View.VISIBLE && fileAuth==null){
                dm.buildAlertDialog("请上传授权书");
                return;
            }
            request.add("chartered",new FileBinary(fileWork));//营业执照
            if(fileAuth==null){
                request.add("warrant","");
            }else {
                request.add("warrant",new FileBinary(fileAuth));//授权书
            }
            executeNetWork(request,"上传中，请稍后...");
            setCallback(this);
        }

    }
    //选择营业执照
    public void uploadWorkPic(View view) {
        if(finalType==-1||finalType==1){
            type=0;
            checkPermission();


        }else if(finalType==0){//审核中，展示图片
            Intent intent=new Intent(this,ShowPicActivity.class);
            if(chartered!=null){
                intent.putExtra("image",chartered);
                startActivity(intent);
            }

        }
    }
    //选择授权书
    public void uploadAuthPic(View view) {
        if(finalType==-1||finalType==1){
            type=1;
            checkPermission();


        }else if(finalType==0){//审核中，展示图片
            Intent intent=new Intent(this,ShowPicActivity.class);
            if(warrant!=null){
                intent.putExtra("image",warrant);
                startActivity(intent);
            }
        }

    }
    private void checkPermission() {
        if(Build.VERSION.SDK_INT>=23){
            if(ContextCompat.checkSelfPermission(this,permissions[0])!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permissions,1);
            }else{
                showPop(type);
            }
        }else {
            showPop(type);
        }
    }
    //申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //用户同意使用read
            showPop(type);
        }else{
            //用户不同意，自行处理即可
            Toast.makeText(this, "无法使用此功能，因为你拒绝了权限", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void showPop(int type) {
        if(type==0){//显示营业执照
            smWorkPic=new SelectIconManager(this);
            smWorkPic.setCallback(new SelectIconManager.Callback() {
                @Override
                public void clickCamera() {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileWork=getDiskCacheDir();//创建文件
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileWork));
                    startActivityForResult(intent, GO_CAMERA_WORK);
                }

                @Override
                public void clickAlbum() {
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, GO_ALBUM_WORK);
                }
            });
        }else if(type==1){//显示授权书
            smAuthPic=new SelectIconManager(this);
            smAuthPic.setCallback(new SelectIconManager.Callback() {
                @Override
                public void clickCamera() {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileAuth=getDiskCacheDir();//创建文件
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileAuth));
                    startActivityForResult(intent, GO_CAMERA_AUTH);
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
    public void saveBitmapFile(Bitmap bitmap,File file){
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
