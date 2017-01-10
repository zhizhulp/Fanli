package com.ascba.rebate.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetWorkActivity;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconManager;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.File;

public class BusinessDataActivity extends BaseNetWorkActivity implements BaseNetWorkActivity.Callback {
    public static final int REQUEST_BUSINESS_NAME=0;
    public static final int REQUEST_BUSINESS_TAG=1;
    public static final int REQUEST_BUSINESS_LOCATION=2;
    public static final int REQUEST_BUSINESS_PHONE=3;
    public static final int REQUEST_BUSINESS_TIME=4;
    public static final int REQUEST_BUSINESS_RATE=5;
    public static final int REQUEST_BUSINESS_DESC=6;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data);
        StatusBarUtil.setColor(this, 0xffe52020);
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            seller_name = intent.getStringExtra("seller_name");
            String seller_cover_logo = intent.getStringExtra("seller_cover_logo");
            String seller_image = intent.getStringExtra("seller_image");
            seller_taglib = intent.getStringExtra("seller_taglib");
            String seller_address = intent.getStringExtra("seller_address");
            String seller_lon = intent.getStringExtra("seller_lon");
            String seller_lat = intent.getStringExtra("seller_lat");
            if(seller_lon!=null&&!"".equals(seller_lon)){
                lon = Double.parseDouble(seller_lon);
            }
            if(seller_lat!=null&&!"".equals(seller_lat)){
                lat = Double.parseDouble(seller_lat);
            }
            seller_tel = intent.getStringExtra("seller_tel");
            seller_business_hours = intent.getStringExtra("seller_business_hours");
            String seller_return_ratio = intent.getStringExtra("seller_return_ratio");
            desc = intent.getStringExtra("seller_description");
            tvName.setText(seller_name);
            Picasso.with(this).load(UrlUtils.baseWebsite+seller_cover_logo).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).into(imBusLogo);
            Picasso.with(this).load(UrlUtils.baseWebsite+seller_image).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).into(imBusPic);
            tvType.setText(seller_taglib);
            tvLocation.setText(seller_address);
            tvPhone.setText(seller_tel);
            tvTime.setText(seller_business_hours);
            if(seller_return_ratio!=null&&!"".equals(seller_return_ratio)){
                double v = Double.parseDouble(seller_return_ratio);
                tvRate.setText((v*100)+"%");
            }

        }
    }

    private void initViews() {
        tvName = ((TextView) findViewById(R.id.business_data_name));
        tvType = ((TextView) findViewById(R.id.business_data_type));
        tvLocation = ((TextView) findViewById(R.id.business_data_location));
        tvPhone = ((TextView) findViewById(R.id.business_data_phone));
        tvTime = ((TextView) findViewById(R.id.business_data_time));
        tvRate = ((TextView) findViewById(R.id.business_data_rate));
        imBusPic = ((ImageView) findViewById(R.id.im_busi_logo));
        imBusLogo = ((ImageView) findViewById(R.id.im_busi_logo_mini));
    }
    public void goBusinessName(View view) {
        Intent intent=new Intent(this,BusinessNameActivity.class);
        if(seller_name!=null){
            intent.putExtra("seller_name",seller_name);
        }
        startActivityForResult(intent,REQUEST_BUSINESS_NAME);
    }

    public void goBusinessTag(View view) {
        Intent intent=new Intent(this,BusinessTagActivity.class);
        if(seller_taglib!=null){
            intent.putExtra("seller_taglib",seller_taglib);
        }
        startActivityForResult(intent,REQUEST_BUSINESS_TAG);
    }

    public void goBusinessLocation(View view) {
        //商家地理位置，此处接入高德地图
        Intent intent=new Intent(this,GaoDeSearch.class);
        intent.putExtra("lon",lon);
        intent.putExtra("lat",lat);
        startActivityForResult(intent,REQUEST_BUSINESS_LOCATION);

    }

    public void goBusinessPhone(View view) {
        Intent intent=new Intent(this,BusinessPhoneActivity.class);
        if(seller_tel!=null){
            intent.putExtra("seller_tel",seller_tel);
        }
        startActivityForResult(intent,REQUEST_BUSINESS_PHONE);
    }

    public void goBusinessTime(View view) {
        Intent intent=new Intent(this,BusinessTimeActivity.class);
        if(seller_business_hours!=null){
            intent.putExtra("seller_business_hours",seller_business_hours);
        }
        startActivityForResult(intent,REQUEST_BUSINESS_TIME);
    }

    public void goBusinessRate(View view) {
        Intent intent=new Intent(this,EmployeeRateActivity.class);
        if(!tvRate.getText().toString().equals("")){
            intent.putExtra("seller_return_ratio",tvRate.getText().toString());
        }
        startActivityForResult(intent,REQUEST_BUSINESS_RATE);
    }

    public void goBusinessDetail(View view) {
        Intent intent=new Intent(this,BusinessDescriptionActivity.class);
        if(desc!=null){
            intent.putExtra("desc",desc);
        }
        startActivityForResult(intent,REQUEST_BUSINESS_DESC);
    }
    //商家店招
    public void goBusinessPic(View view) {
        sm=new SelectIconManager(this);
        sm.setCallback(new SelectIconManager.Callback() {
            @Override
            public void clickCamera() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file=getDiskCacheDir();//创建文件
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, GO_CAMERA_PIC);
            }

            @Override
            public void clickAlbum() {
                Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2, GO_ALBUM_PIC);
            }
        });
    }
    //商家logo
    public void goBusinessLogo(View view) {
        sm=new SelectIconManager(this);
        sm.setCallback(new SelectIconManager.Callback() {
            @Override
            public void clickCamera() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileLogo=getDiskCacheDir();//创建文件
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileLogo));
                startActivityForResult(intent, GO_CAMERA_LOGO);
            }

            @Override
            public void clickAlbum() {
                Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2, GO_ALBUM_LOGO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GO_CAMERA_PIC:
                if(file != null && file.exists()){
                    Bitmap bitmap=handleBitmap(file);
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
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                file=new File(picturePath);
                cursor.close();
                Bitmap bitmap = handleBitmap(file);
                imBusPic.setImageBitmap(bitmap);
                break;
            case GO_CAMERA_LOGO:
                if(fileLogo != null && fileLogo.exists()){
                    Bitmap bitmap2=handleBitmap(fileLogo);
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
                cursor2.moveToFirst();
                int columnIndex2 = cursor2.getColumnIndex(filePathColumn2[0]);
                String picturePath2 = cursor2.getString(columnIndex2);
                fileLogo=new File(picturePath2);
                cursor2.close();
                Bitmap bitmap2 = handleBitmap(fileLogo);
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
        objRequest.add("seller_tel",tvPhone.getText().toString());
        objRequest.add("seller_business_hours",tvTime.getText().toString());
        String rate = tvRate.getText().toString();
        if(!"".equals(rate)){
            String substring = rate.substring(0, rate.length()-1);
            double i = Double.parseDouble(substring);
            objRequest.add("seller_return_ratio",i+"");
        }
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
    }

    @Override
    public void handle200Data(JSONObject dataObj, String message) {

        Toast.makeText(BusinessDataActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    public File getDiskCacheDir() {
        File file=null;
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


}
