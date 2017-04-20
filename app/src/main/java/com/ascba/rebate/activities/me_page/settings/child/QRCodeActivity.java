package com.ascba.rebate.activities.me_page.settings.child;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.handlers.DialogManager;
import com.ascba.rebate.utils.QrUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCodeActivity extends BaseNetActivity {


    private ImageView qrImg;
    private SharedPreferences sf;
    private TextView tvSave;
    private Bitmap mBitmap;
    private File saveFile;
    private DialogManager dm;
    final MediaScannerConnection msc = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {

        public void onMediaScannerConnected() {
            msc.scanFile(saveFile.getAbsolutePath(), "image/jpeg");
        }

        public void onScanCompleted(String path, Uri uri) {
            msc.disconnect();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        StatusBarUtil.setTranslucent(this);
        initViews();
    }

    private void initViews() {
        dm=new DialogManager(this);
        qrImg = ((ImageView) findViewById(R.id.qrcode));
        tvSave = ((TextView) findViewById(R.id.tv_save_to_album));

        initQr();//生成二维码
        initSave();//二维码保存到手机

    }

    private void initSave() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 首先保存图片
                File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String fileName = "qlqw_business_qr" + ".jpg";
                File file = new File(appDir, fileName);
                saveFile = file;
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(),
                            file.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                msc.connect();
                dm.buildAlertDialog("成功保存到相册");
            }
        });
    }

    private void initQr() {
        sf = getSharedPreferences("first_login_success_name_password", MODE_PRIVATE);
        int uuid = sf.getInt("uuid", -1000);
        if (uuid != -1000) {
            mBitmap = createQRCode(uuid + "", 163, 163);
            qrImg.setImageBitmap(mBitmap);
        }
    }


    public Bitmap createQRCode(String content, int width, int height) {
        int dpWidth = ScreenDpiUtils.dip2px(this, width);
        int dpHeight = ScreenDpiUtils.dip2px(this, height);
        //return CodeUtils.createImage(content, dpWidth, dpHeight, null);
        return QrUtils.createImage(content, dpWidth, dpHeight, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));//带图片
    }
}
