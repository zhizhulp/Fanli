package com.ascba.rebate.activities.offline_business;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.beans.sweep.CheckSellerEntity;
import com.ascba.rebate.qr.MessageIDs;
import com.ascba.rebate.qr.camera.CameraManager;
import com.ascba.rebate.qr.decoding.CaptureActivityHandler;
import com.ascba.rebate.qr.decoding.InactivityTimer;
import com.ascba.rebate.qr.view.ViewfinderView;
import com.ascba.rebate.utils.UrlUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

//点击扫一扫跳入的页面
//implements Callback, BaseNetActivity.Callback
public class CaptureActivity extends BaseNetActivity implements Callback {
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    // private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    CameraManager cameraManager;
    private String[] permissions = {Manifest.permission.CAMERA};
    private String resutlt;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

//        new CountDownTimer(3500, 2000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                startActivity(new Intent(CaptureActivity.this, OfflinePayActivity.class));
//                finish();
//            }
//        }.start();


    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(final SurfaceHolder surfaceHolder) {
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestAllPermission(permissions, new PermissionCallback() {
                @Override
                public void requestPermissionAndBack(boolean isOk) {
                    if (!isOk) {
                        finish();
                    } else {
                        try {
                            cameraManager.openDriver(surfaceHolder);
                        } catch (IOException ioe) {
                            return;
                        } catch (RuntimeException e) {
                            return;
                        }
                        if (handler == null) {
                            handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet);
                        }
                    }
                }
            });
        } else {
            try {
                cameraManager.openDriver(surfaceHolder);
                if (handler == null) {
                    handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        // showResult(obj, barcode);

        String str = result.getText();
        Log.d("fanxi","-----------"+ str);

        String[]  strs=str.split("/");

        Log.d("fanxi","-----------"+ strs[4]);
        resutlt=strs[4];
        requestNetwork(UrlUtils.checkSeller, 0,strs[4]);
    }

    public void requestNetwork(String url, int what,String result) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);//0代表post请求
        request.add("seller", result);
        request.add("scenetype", 2);
        executeNetWork(what, request, "请稍后");
    }

    //数据返回成功的处理
    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {

        CheckSellerEntity checkSellerEntity = JSON.parseObject(dataObj.toString(), CheckSellerEntity.class);
        CheckSellerEntity.InfoBean info = checkSellerEntity.getInfo();

        Intent intent = new Intent(this, OfflinePayActivity.class);
        Log.d("fanxi","capture"+"-------"+resutlt);
        intent.putExtra("seller",info.getSeller());
        intent.putExtra("seller_cover_logo",info.getSeller_cover_logo());
        intent.putExtra("seller_name",info.getSeller_name());
        intent.putExtra("self_money",info.getSelf_money());

        startActivity(intent);
    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        super.mhandle404(what, object, message);
        restartPreviewAfterDelay(3000);
    }

    //重新扫描
    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            try {
                AssetFileDescriptor fileDescriptor = getAssets().openFd("qrbeep.ogg");
                this.mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                this.mediaPlayer.setVolume(0.1F, 0.1F);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




    private boolean defaultLightOn;//默认关闭

    //灯的图标，打开关闭
    public void exchangeLightIcon(View view) {
        defaultLightOn = !defaultLightOn;
        ImageView imageView = (ImageView) view;
        if (defaultLightOn) {
            imageView.setImageResource(R.mipmap.light_on);
        } else {
            imageView.setImageResource(R.mipmap.light_off);
        }
        cameraManager.switchFlashLight();
    }
}