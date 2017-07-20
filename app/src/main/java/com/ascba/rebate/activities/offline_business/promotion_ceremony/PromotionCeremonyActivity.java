package com.ascba.rebate.activities.offline_business.promotion_ceremony;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.base.BaseNetActivity;
import com.ascba.rebate.adapter.sweep.promotion.PromotionCeremoneyAdapter;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.beans.sweep.promotion_ceremony.PromotionCeremoneyEntity;
import com.ascba.rebate.utils.QrUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.ViewUtils;
import com.ascba.rebate.view.MoneyBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PromotionCeremonyActivity extends BaseNetActivity {
    private RecyclerView recycler;
    private TextView promotion_weixin, promotion_friends_circle, promotion_qr_code;
    private TextView promotion_total_money, promotion_total_people;
    private TextView promotion_ceremoney_nodata;
    private View head;
    private String courtesy_url;
    private List<PromotionCeremoneyEntity.RefereeListBean> data = new ArrayList<>();
    private int now_page = 1;
    private PromotionCeremoneyEntity.CourtesyBean courtesy = new PromotionCeremoneyEntity.CourtesyBean();
    private MoneyBar moneybar;
    private int changeHeight = 500;
    private int moneyBarColor, red, green, blue;
    private LinearLayoutManager layoutManager;
    private Bitmap bitmap;
    private ImageView promotion_head_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_ceremony);
        initRefreshLayout();
        initSystemBar();
        initView();
        requestNetWork(UrlUtils.courtesyRecommended, 0);
    }

    public void requestNetwork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        request.add("now_page", now_page);
        executeNetWork(what, request, "请稍后");
    }

    private void initView() {
        recycler = (RecyclerView) findViewById(R.id.promotion_recycler);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        baseAdapter = new PromotionCeremoneyAdapter(R.layout.item_promotion_ceremoney, data);
        promotion_ceremoney_nodata = (TextView) findViewById(R.id.promotion_ceremoney_nodata);
        moneybar = (MoneyBar) findViewById(R.id.ceremony_moneybar);
        head = ViewUtils.getView(this, R.layout.activity_promotion_ceremoney_head);
        baseAdapter.addHeaderView(head);
        recycler.setAdapter(baseAdapter);
        promotion_qr_code = (TextView) head.findViewById(R.id.promotion_qr_code);//邀请微信好友
        promotion_weixin = (TextView) head.findViewById(R.id.promotion_weixin);//邀请微信朋友圈
        promotion_friends_circle = (TextView) head.findViewById(R.id.promotion_friends_circle);//朋友圈
        promotion_total_money = (TextView) head.findViewById(R.id.promotion_total_money);
        promotion_total_people = (TextView) head.findViewById(R.id.promotion_total_people);
        promotion_head_iv = (ImageView) head.findViewById(R.id.promotion_head_iv);
        promotion_head_iv.setScaleType(ImageView.ScaleType.FIT_XY);

        loadRequestor = new LoadRequestor() {
            @Override
            public void loadMore() {
                requestNetWork(UrlUtils.courtesyRecommended, 0);
            }

            @Override
            public void pullToRefresh() {
                requestNetWork(UrlUtils.courtesyRecommended, 0);
            }
        };
        initLoadMoreRequest();
        initListeners();
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        moneyBarColor = Color.parseColor("#96E9F7");
        red = Color.red(moneyBarColor);
        green = Color.green(moneyBarColor);
        blue = Color.blue(moneyBarColor);
        tintManager.setTintColor(moneyBarColor);
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {
        ViewTreeObserver vto = moneybar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                moneybar.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int y = getScollYDistance();
                        if (y <= 0) {   //设置标题的背景颜色
                            moneybar.setBackgroundColor(Color.argb((int) 0, red, green, blue));
                        } else if (y > 0 && y <= changeHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                            float scale = (float) y / changeHeight;
                            float alpha = (255 * scale);
                            moneybar.setBackgroundColor(Color.argb((int) alpha, red, green, blue));
                        } else {    //滑动到banner下面设置普通颜色
                            moneybar.setBackgroundColor(Color.argb((int) 255, red, green, blue));
                        }
                    }
                });
            }
        });
    }

    public int getScollYDistance() {
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.promotion_weixin:  //邀请微信朋友
                if(courtesy.getCourtesy_url()!=null){
                    wechatShare(0, courtesy.getCourtesy_url(), courtesy.getTitle(), courtesy.getSubtitle());
                }
                break;
            case R.id.promotion_friends_circle://分享朋友圈
                if (courtesy.getCourtesy_url()!=null) {
                    wechatShare(1, courtesy.getCourtesy_url(), courtesy.getTitle(), courtesy.getSubtitle());
                }
                break;
            case R.id.promotion_qr_code://二维码的弹窗
                showQRDialog();
                break;
        }
    }

    /**
     * 微信分享
     *
     * @param flag 0,分享给朋友，1分享给朋友圈
     */
    private void wechatShare(int flag, String url, String title, String content) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        //  = BitmapFactory.decodeResource(getResources(),R.mipmap.share_icon);

        downloadFile(UrlUtils.baseWebsite + courtesy.getCourtesy_img(), new ICallBack() {
            @Override
            public void callBack(final byte[] array) {
                PromotionCeremonyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
                    }
                });

            }
        });
        if (bitmap != null) {
            //分享链接图片资源
            msg.setThumbImage(bitmap);
        }else {
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.share_icon);
         //   msg.thumbData= bmpToByteArray(bitmap,true);
            msg.setThumbImage(bitmap1);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;

        //发送到聊天界面——WXSceneSession
        //发送到朋友圈——WXSceneTimeline
        //添加到微信收藏——WXSceneFavorite
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        ((MyApplication) getApplication()).msgApi.sendReq(req);
    }

    //二维码的弹窗
    private void showQRDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(PromotionCeremonyActivity.this, R.style.fullScreenDialog)
                .setView(R.layout.promotion_qr_dialog)
                .create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        //生成二维码的图片
        ImageView iv_qr = (ImageView) alertDialog.findViewById(R.id.promotion_qr_iv);
        ImageButton ivClose = (ImageButton) alertDialog.findViewById(R.id.ib_close);
        Bitmap qrCode = createQRCode(courtesy_url, 170, 170);
        iv_qr.setImageBitmap(qrCode);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    public Bitmap createQRCode(String content, int width, int height) {
        int dpWidth = ScreenDpiUtils.dip2px(this, width);
        int dpHeight = ScreenDpiUtils.dip2px(this, height);
        return QrUtils.createQRImage(content, dpWidth, dpHeight, getApplicationContext());
    }

    private void requestNetWork(String url, int what) {
        Request<JSONObject> request = buildNetRequest(url, 0, true);
        executeNetWork(what, request, "请稍后");
    }

    @Override
    protected void mhandle200Data(int what, JSONObject object, JSONObject dataObj, String message) {
        super.mhandle200Data(what, object, dataObj, message);
        PromotionCeremoneyEntity promotionCeremoneyEntity = JSON.parseObject(dataObj.toString(), PromotionCeremoneyEntity.class);
        promotion_total_money.setText(promotionCeremoneyEntity.getTotal_money() + "");
        promotion_total_people.setText(promotionCeremoneyEntity.getPeople_num() + "");
        courtesy = promotionCeremoneyEntity.getCourtesy();
        Picasso.with(this).load(UrlUtils.baseWebsite + courtesy.getImage()).placeholder(R.mipmap.banner_loading).into(promotion_head_iv);
        courtesy_url = courtesy.getCourtesy_url();
        if (isRefreshing) {
            if (data.size() != 0) {
                data.clear();
            }
        }
        data.addAll(promotionCeremoneyEntity.getReferee_list());
        baseAdapter.notifyDataSetChanged();
        if (data.size() == 0) {
            promotion_ceremoney_nodata.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 下载方法
     *
     * @param url 地址
     */
    public void downloadFile(final String url, final ICallBack result) {
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        byte[] bytes = new byte[1024 * 500];
                        int len = -1;
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((len = inputStream.read(bytes)) != -1) {
                            baos.write(bytes, 0, len);
                        }
                        result.callBack(baos.toByteArray());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public interface ICallBack {
        void callBack(byte[] array);
    }

    @Override
    protected void mhandle404(int what, JSONObject object, String message) {
        super.mhandle404(what, object, message);
        if (what == 0) {
            finish();
        }

    }

    @Override
    protected void mhandleReLogin(int what) {
        super.mhandleReLogin(what);
        if (what == 0) {
            finish();
        }
    }

//    @Override
//    protected void mhandleNoNetWord() {
//        super.mhandleNoNetWord();
//
//    }
}

