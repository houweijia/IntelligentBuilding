package com.example.veigar.intelligentbuilding.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.nfc.Tag;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.util.L;

import java.lang.reflect.InvocationTargetException;

public class VideoActivity extends BaseActivity {

    private WebView mWebView;
    private boolean isOnPause = false;
    private RelativeLayout mProgress;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private RelativeLayout relativeWebView;
    private NotificationManager nm;
    private static final int NOTIFICATION_FLAG = 1;
    private Notification notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();


    }

    private void initView() {
       //initToolBar();
        mProgress = (RelativeLayout) findViewById(R.id.my_progress);
        relativeWebView = (RelativeLayout) findViewById(R.id.relative_web_view);
        mProgress.setVisibility(View.VISIBLE);
        mWebView = (WebView) findViewById(R.id.my_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setUseWideViewPort(true);
        //mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.loadUrl("http://115.28.77.207/play.html");

        mWebView.getSettings().setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        L.e("densityDpi = " + mDensity);
        if (mDensity == 240) {
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        WebViewClient viewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                L.e("onPageFinish");
                mWebView.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
            }
        };

        WebChromeClient client = new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100){
                    L.e("onProgressChanged");
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                L.e("onShowCustomView");
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                mCustomView = view;
                relativeWebView.addView(mCustomView);
                mCustomViewCallback = callback;
                mWebView.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onHideCustomView() {
                mWebView.setVisibility(View.VISIBLE);
                if (mCustomView == null) {
                    return;
                }
                mCustomView.setVisibility(View.GONE);
                relativeWebView.removeView(mCustomView);
                mCustomViewCallback.onCustomViewHidden();
                mCustomView = null;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                super.onHideCustomView();
                L.e("onHideCustomView");
            }
        };
        mWebView.setWebViewClient(viewClient);
        mWebView.setWebChromeClient(client);


        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi= PendingIntent.getActivity(this, 0, intent, 0);
        notification = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                .setTicker("您有新短消息，请注意查收！")
                .setContentTitle("报警通知")
                .setContentText("室内温度过高")
                .setContentIntent(pi).setNumber(1).build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。


        //n.setLatestEventInfo(this, title, content, pi);




    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mWebView!=null){
            try {
                mWebView.getClass().getMethod("onPause").invoke(mWebView,(Object[])null);
                isOnPause = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isOnPause) {
                if (mWebView != null) {
                    mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }

    public void btn(View view) {
        nm.notify(NOTIFICATION_FLAG, notification);
    }
}
