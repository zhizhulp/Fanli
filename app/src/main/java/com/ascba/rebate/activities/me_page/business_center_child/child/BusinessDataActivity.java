package com.ascba.rebate.activities.me_page.business_center_child.child;

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
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.utils.StringUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.Request;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BusinessDataActivity extends BaseNetActivity implements BaseNetActivity.Callback {
    public static final int REQUEST_BUSINESS_NAME=0;
    public static final int REQUEST_BUSINESS_TAG=1;
    public static final int REQUEST_BUSINESS_LOCATION=2;
    public static final int REQUEST_BUSINESS_PHONE=3;
    public static final int REQUEST_BUSINESS_TIME=4;
    public static final int REQUEST_BUSINESS_RATE=5;
    public static final int REQUEST_BUSINESS_DESC=6;
    public static final int REQUEST_BUSINESS_LOCATION_DETAILS=11;
    private TextView tvName;
    private TextView tvType;
    private TextView tvLocation;
    private TextView tvPhone;
    private TextView tvTime;
    private TextView tvRate;
    private String desc;//商家描述
    private String seller_name;
    private String seller_taglib;
    private String seller_tel;
    private String seller_business_hours;
    private String seller_return_ratio;
    private double longitude;
    private double latitude;
    private ImageView imBusPic;
    private SelectIconManager sm;
    private static final int GO_CAMERA_PIC=7;
    private static final int GO_ALBUM_PIC=8;
    private static final int GO_CAMERA_LOGO=9;
    private static final int GO_ALBUM_LOGO=10;
    private File file;
    private File fileLogo;
    private ImageView imBusLogo;
    private double lon;
    private double lat;
    private String street;
    private String[] permissions=new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private int type;
    private Button btnCommit;
    private String btnEnable;
    private static final String noMdf="暂时不可修改！";
    private TextView tvLocDet;
    private String backRate;
    private int finalScene;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data);
        initViews();
        finalScene=0;
        requestNetwork(UrlUtils.getCompany);
    }

    private void requestNetwork(String url) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(request, "请稍候");
        setCallback(this);
    }

    private String getHandleStr(String str) {
        String[] split1 = str.split("-");
        String type = split1[0];
        String rate = split1[1];
        String user = split1[2];
        String bus = split1[3];
        return type+"类型佣金"+rate+"%,赠返"+bus+"%";
    }

    private void initViews() {
        tvName = ((TextView) findViewById(R.id.business_data_name));
        tvType = ((TextView) findViewById(R.id.business_data_type));
        tvLocation = ((TextView) findViewById(R.id.business_data_location));
        tvLocDet = (TextView) findViewById(R.id.business_data_location_details);
        tvPhone = ((TextView) findViewById(R.id.business_data_phone));
        tvTime = ((TextView) findViewById(R.id.business_data_time));
        tvRate = ((TextView) findViewById(R.id.business_data_rate));
        imBusPic = ((ImageView) findViewById(R.id.im_busi_logo));
        imBusLogo = ((ImageView) findViewById(R.id.im_busi_logo_mini));
        btnCommit = ((Button) findViewById(R.id.btn_commit));
    }
    public void goBusinessName(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            Intent intent=new Intent(this,BusinessNameActivity.class);
            if(seller_name!=null){
                intent.putExtra("seller_name",seller_name);
            }
            startActivityForResult(intent,REQUEST_BUSINESS_NAME);
        }else{
            getDm().buildAlertDialog(noMdf);
        }

    }

    public void goBusinessTag(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){

            Intent intent=new Intent(this,BusinessTagActivity.class);
            if(seller_taglib!=null){
                intent.putExtra("seller_taglib",seller_taglib);
            }
            startActivityForResult(intent,REQUEST_BUSINESS_TAG);
        }else {
            getDm().buildAlertDialog(noMdf);
        }
    }

    public void goBusinessLocation(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            //商家地理位置，此处接入高德地图
            Intent intent=new Intent(this,GaoDeSearchUpdate.class);
            intent.putExtra("lon",lon);
            intent.putExtra("lat",lat);
            startActivityForResult(intent,REQUEST_BUSINESS_LOCATION);
        }else {
            getDm().buildAlertDialog(noMdf);
        }


    }

    public void goBusinessPhone(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            Intent intent=new Intent(this,BusinessPhoneActivity.class);
            if(seller_tel!=null){
                intent.putExtra("seller_tel",seller_tel);
            }
            startActivityForResult(intent,REQUEST_BUSINESS_PHONE);
        }else {
            getDm().buildAlertDialog(noMdf);
        }

    }

    public void goBusinessTime(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            Intent intent=new Intent(this,BusinessTimeActivity.class);
            intent.putExtra("seller_business_hours",seller_business_hours);
            startActivityForResult(intent,REQUEST_BUSINESS_TIME);
        }else {
            getDm().buildAlertDialog(noMdf);
        }

    }

    public void goBusinessRate(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            Intent intent=new Intent(this,EmployeeRateActivity.class);
            if(!tvRate.getText().toString().equals("")){
                intent.putExtra("seller_return_ratio",seller_return_ratio);
            }
            startActivityForResult(intent,REQUEST_BUSINESS_RATE);
        }else {
            getDm().buildAlertDialog(noMdf);
        }

    }
    //详细地址的页面
    public void goBusinessLocationDetails(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            Intent intent=new Intent(this,BusLocDetActivity.class);
            if(!tvLocDet.getText().toString().equals("")){
                intent.putExtra("seller_localhost",tvLocDet.getText().toString());
            }
            startActivityForResult(intent,REQUEST_BUSINESS_LOCATION_DETAILS);
        }else {
            getDm().buildAlertDialog(noMdf);
        }
    }

    public void goBusinessDetail(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            Intent intent=new Intent(this,BusinessDescriptionActivity.class);
            if(desc!=null){
                intent.putExtra("desc",desc);
            }
            startActivityForResult(intent,REQUEST_BUSINESS_DESC);
        }else {
            getDm().buildAlertDialog(noMdf);
        }

    }
    //商家店招
    public void goBusinessPic(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            type=0;
            checkPermission();

        }else {
            getDm().buildAlertDialog(noMdf);
        }

    }
    //商家logo
    public void goBusinessLogo(View view) {
        if(btnEnable.equals("0") ||btnEnable.equals("1")){
            type=1;
            checkPermission();
        }else {
            getDm().buildAlertDialog(noMdf);
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
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        if(type==0){//显示店招pop
            sm=new SelectIconManager(this);
            sm.setCallback(new SelectIconManager.Callback() {
                @Override
                public void clickCamera() {
                     Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file=getDiskCacheDir();//创建文件
                    if(Build.VERSION.SDK_INT>23){//处理7.0的情况
                        Uri uri = FileProvider.getUriForFile(BusinessDataActivity.this, "com.ascba.rebate.provider", file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, GO_CAMERA_PIC);
                    }else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, GO_CAMERA_PIC);
                    }
                }
                @Override
                public void clickAlbum() {
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, GO_ALBUM_PIC);
                }
            });
        }else if(type==1){//显示logo_pop
            sm=new SelectIconManager(this);
            sm.setCallback(new SelectIconManager.Callback() {
                @Override
                public void clickCamera() {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileLogo=getDiskCacheDir();//创建文件
                    if(Build.VERSION.SDK_INT>23){//处理7.0的情况
                        Uri uri = FileProvider.getUriForFile(BusinessDataActivity.this, "com.ascba.rebate.provider", fileLogo);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, GO_CAMERA_LOGO);
                    }else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileLogo));
                        startActivityForResult(intent, GO_CAMERA_LOGO);
                    }
                }

                @Override
                public void clickAlbum() {
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, GO_ALBUM_LOGO);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GO_CAMERA_PIC:
                if(file != null && file.exists()){
                    Bitmap bitmap=handleBitmap(file);
                    saveBitmapFile(bitmap,file);
                    imBusPic.setImageBitmap(bitmap);
                }
                break;
            case GO_ALBUM_PIC:
                if(data==null){
                    return;
                }
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                file=new File(picturePath);
                cursor.close();
                Bitmap bitmap = handleBitmap(file);
                saveBitmapFile(bitmap,file);
                imBusPic.setImageBitmap(bitmap);
                break;
            case GO_CAMERA_LOGO:
                if(fileLogo != null && fileLogo.exists()){
                    Bitmap bitmap2=handleBitmap(fileLogo);
                    saveBitmapFile(bitmap2,fileLogo);
                    imBusLogo.setImageBitmap(bitmap2);
                }
                break;
            case GO_ALBUM_LOGO:
                if(data==null){
                    return;
                }
                Uri selectedImage2 = data.getData();
                String[] filePathColumn2 = {MediaStore.Images.Media.DATA};
                Cursor cursor2 = getContentResolver().query(selectedImage2,
                        filePathColumn2, null, null, null);
                assert cursor2 != null;
                cursor2.moveToFirst();
                int columnIndex2 = cursor2.getColumnIndex(filePathColumn2[0]);
                String picturePath2 = cursor2.getString(columnIndex2);
                fileLogo=new File(picturePath2);
                cursor2.close();
                Bitmap bitmap2 = handleBitmap(fileLogo);
                saveBitmapFile(bitmap2,fileLogo);
                imBusLogo.setImageBitmap(bitmap2);
                break;
            case REQUEST_BUSINESS_NAME:
                if(data==null){
                    return;
                }
                tvName.setText(data.getStringExtra("business_data_name"));
                break;
            case REQUEST_BUSINESS_TAG:
                if(data==null){
                    return;
                }
                tvType.setText(data.getStringExtra("business_data_type"));
                break;
            case REQUEST_BUSINESS_LOCATION:
                if(data==null){
                    return;
                }
                longitude = data.getDoubleExtra("longitude",116.397726);//经度 0-180度
                latitude = data.getDoubleExtra("latitude",39.903767);//纬度 0-90度
                tvLocation.setText(data.getStringExtra("location"));
                street = data.getStringExtra("street");
                break;
            case REQUEST_BUSINESS_LOCATION_DETAILS:
                if(data==null){
                    return;
                }
                tvLocDet.setText(data.getStringExtra("seller_localhost"));
                break;
            case REQUEST_BUSINESS_PHONE:
                if(data==null){
                    return;
                }
                tvPhone.setText(data.getStringExtra("business_data_phone"));
                break;
            case REQUEST_BUSINESS_TIME:
                if(data==null){
                    return;
                }
                tvTime.setText(data.getStringExtra("business_data_time"));
                break;
            case REQUEST_BUSINESS_RATE:
                if(data==null){
                    return;
                }
                tvRate.setText(data.getStringExtra("business_data_rate"));
                backRate = data.getStringExtra("business_data_rate_type");
                break;
            case REQUEST_BUSINESS_DESC:
                if(data==null){
                    return;
                }
                desc = data.getStringExtra("desc");//返回的商家描述
                break;
        }
    }

    //提交商家资料
    public void businessDataGo(View view) {
        Request<JSONObject> objRequest = buildNetRequest(UrlUtils.setTenants, 0, true);
        objRequest.add("seller_name",tvName.getText().toString());
        objRequest.add("seller_taglib",tvType.getText().toString());
        objRequest.add("seller_address",tvLocation.getText().toString());
        objRequest.add("seller_lon",longitude);
        objRequest.add("seller_lat",latitude);
        objRequest.add("region_name",street);
        objRequest.add("seller_tel",tvPhone.getText().toString());
        objRequest.add("seller_business_hours",tvTime.getText().toString());
        String rate = tvRate.getText().toString();
        if(!"".equals(rate)){
            if(backRate!=null){
                objRequest.add("seller_return_ratio",backRate);
            }
        }
        objRequest.add("seller_localhost",tvLocDet.getText().toString());
        objRequest.add("seller_description",desc);
        if(file!=null){
            objRequest.add("seller_images",new FileBinary(file));
        }else {
            objRequest.add("seller_images","");
        }
        if(fileLogo!=null){
            objRequest.add("seller_cover_logo",new FileBinary(fileLogo));
        }else {
            objRequest.add("seller_cover_logo","");
        }
        executeNetWork(objRequest,"请稍后");
        setCallback(this);
        finalScene=1;
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {
        if(finalScene==0){//界面数据
            JSONObject company = dataObj.optJSONObject("company");
            btnEnable = company.optString("submit_status");
            String submit_tip = company.optString("submit_tip");
            String seller_name = company.optString("seller_name");
            String seller_cover_logo = company.optString("seller_cover_logo");
            String seller_image = company.optString("seller_image");
            String seller_taglib = company.optString("seller_taglib");
            String seller_address = company.optString("seller_address");
            String seller_localhost = company.optString("seller_localhost");
            String seller_lon = company.optString("seller_lon");
            String seller_lat = company.optString("seller_lat");
            seller_tel = company.optString("seller_tel");
            seller_business_hours = company.optString("seller_business_hours");
            seller_return_ratio = company.optString("seller_return_ratio");
            String seller_return_ratio_tip = company.optString("seller_return_ratio_tip");
            desc = company.optString("seller_description");

            if(!StringUtils.isEmpty(seller_lon)){
                lon = Double.parseDouble(seller_lon);
            }
            if(!StringUtils.isEmpty(seller_lat)){
                lat = Double.parseDouble(seller_lat);
            }
            tvName.setText(seller_name);
            Picasso.with(this).load(UrlUtils.baseWebsite+seller_cover_logo).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).into(imBusLogo);
            Picasso.with(this).load(UrlUtils.baseWebsite+seller_image).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).into(imBusPic);
            tvType.setText(seller_taglib);
            tvLocation.setText(seller_address);
            tvLocDet.setText(StringUtils.isEmpty(seller_localhost)?null:seller_localhost);
            tvPhone.setText(seller_tel);
            tvTime.setText(seller_business_hours);
            if(!StringUtils.isEmpty(seller_return_ratio_tip)){
                tvRate.setText(getHandleStr(seller_return_ratio_tip));
            }
            btnCommit.setText(submit_tip);
            if(btnEnable.equals("0")){//可以提交
                btnCommit.setEnabled(true);
            }else if(btnEnable.equals("2")) {//等待审核
                btnCommit.setEnabled(false);
                btnCommit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
            }else if(btnEnable.equals("3")) {//距离修改时间
                btnCommit.setEnabled(false);
                btnCommit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_no_shop_bg));
            }else if(btnEnable.equals("1")){//资料有误
                btnCommit.setEnabled(true);
            }

        }else if(finalScene==1){
            getDm().buildAlertDialog(message);
            finish();
        }

    }

    @Override
    public void handle404(String message) {
    }

    @Override
    public void handleNoNetWork() {

    }

    public File getDiskCacheDir() {
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
