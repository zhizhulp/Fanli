package com.ascba.rebate.activities.scoring.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.scoring.NewCreditSesameView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/15.
 */

public class ShareDialog{
    private ImageView ivQrcode;
    private Context context;
    private Dialog dialog;
    private NewCreditSesameView sesameView;

    public ShareDialog(Context context) {
        this.context = context;
        init();
    }

    private void init(){
       dialog = new Dialog(context,R.style.dialog);
       dialog.setContentView(R.layout.share_dialog);
       dialog.show();
       initView();
   }

    private void initView() {
        ivQrcode = (ImageView) dialog.findViewById(R.id.iv_qrcode);
        sesameView = (NewCreditSesameView) dialog.findViewById(R.id.scroing_creditsesame);
        sesameView.setSesameValues(695);
        Bitmap bitmap = generateBitmap("http://www.qlqwjr.com/",dp2px(58),dp2px(58));
        ivQrcode.setImageBitmap(bitmap);
    }

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int dp2px(int values) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

}
