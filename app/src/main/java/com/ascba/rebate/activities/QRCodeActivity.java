package com.ascba.rebate.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseActivity;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class QRCodeActivity extends BaseActivity {


    private ImageView qrImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initViews();
    }

    private void initViews() {
        qrImg = ((ImageView) findViewById(R.id.qrcode));
        int wh=ScreenDpiUtils.dip2px(this,250);
        qrImg.setImageBitmap(createQrCodeBitmap("phone：15510115653，uuid:1232",wh,wh));
    }

    private Bitmap createQrCodeBitmap(String content, int width, int height) {
        //1 获取ZXing中的二维码生成类QRCodeWriter
        QRCodeWriter writer = new QRCodeWriter();
        //设置文本信息的编码格式
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            /**
             * 调用encode方法之后，会将字符串以二维码的形式显示在矩阵当中
             * 通过此矩阵的get(x,y)方法能够判断出当前点是否有像素
             */
            BitMatrix bitMatrix = writer.encode(
                    content, BarcodeFormat.QR_CODE, width, height, hints);
            //初始化需要被填充为黑白像素的数组
            int[] pixels = new int[width * height];
            //循环遍历数组中的每一个像素点，并分配黑白颜色值
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (bitMatrix.get(j, i)) {  //说明当前点有像素
                        pixels[width * i + j] = 0x00000000;
                    } else {
                        pixels[width * i + j] = 0xffffffff;
                    }
                }
            }
            //通过填充像素数组返回Bitmap对象
            Bitmap qrBitmap = Bitmap.createBitmap(
                    pixels, 0, width, width, height, Bitmap.Config.RGB_565);
            return qrBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }
}
