package com.example.veigar.intelligentbuilding.ui;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.util.L;

public class NbIotActivity extends BaseActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nb_iot);
        initView();
    }

    private void initView() {
        initToolBar();
        setPageTitle("NB-IOT应用");
        mWebView = (WebView) findViewById(R.id.web_view);
        //mProgress.setVisibility(View.VISIBLE);
        mWebView.clearCache(true);
        mWebView.clearHistory();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存
        settings.setDomStorageEnabled(true);//设置适应HTML5的一些方法
        settings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE,new android.graphics.Paint());

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });
        mWebView.loadUrl("http://119.29.139.179:8080/devmagnetic/mapapp.do");
        //mWebView.loadUrl("http:www.baidu.com");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView!=null){
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.freeMemory();
            mWebView.pauseTimers();
            mWebView = null;
        }
    }
}
