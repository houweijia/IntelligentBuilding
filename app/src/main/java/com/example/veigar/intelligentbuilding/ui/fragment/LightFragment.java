package com.example.veigar.intelligentbuilding.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseFragment;
import com.example.veigar.intelligentbuilding.util.MyUtils;

/**
 * Created by veigar on 2017/4/19.
 */

public class LightFragment extends BaseFragment implements View.OnClickListener{

    private TextView red;
    private TextView green;
    private TextView blue;
    private RelativeLayout mLinear;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light2,container,false);
    }

    @Override
    public void init(View rootView) {
       /* WebView mWebView = (WebView) rootView.findViewById(R.id.my_webView);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl("http://115.28.77.207/play.html");*/
        /*mLinear = (RelativeLayout) rootView.findViewById(R.id.my_linear);
        Bitmap background = BitmapFactory.decodeResource(getResources(),R.mipmap.light_bg);
        Bitmap mohu = MyUtils.fastblur(background,50);
        BitmapDrawable drawable = new BitmapDrawable(mohu);
        mLinear.setBackgroundResource(R.mipmap.red_bg);

        red = (TextView) rootView.findViewById(R.id.tv_red);
        green = (TextView) rootView.findViewById(R.id.tv_green);
        blue = (TextView) rootView.findViewById(R.id.tv_blue);
        red.setBackgroundResource(R.drawable.light_top_tv_press_background);
        red.setOnClickListener(this);
        green.setOnClickListener(this);
        blue.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_red:
                red.setBackgroundResource(R.drawable.light_top_tv_press_background);
                green.setBackgroundResource(R.color.light_background);
                blue.setBackgroundResource(R.drawable.light_bottom_tv_normal_background);
                mLinear.setBackgroundResource(R.mipmap.red_bg);
                break;
            case R.id.tv_green:
                red.setBackgroundResource(R.drawable.light_top_tv_normal_background);
                green.setBackgroundResource(R.color.textColor);
                blue.setBackgroundResource(R.drawable.light_bottom_tv_normal_background);
                mLinear.setBackgroundResource(R.mipmap.green_bg);
                break;
            case R.id.tv_blue:
                red.setBackgroundResource(R.drawable.light_top_tv_normal_background);
                green.setBackgroundResource(R.color.light_background);
                blue.setBackgroundResource(R.drawable.light_bottom_tv_press_background);
                mLinear.setBackgroundResource(R.mipmap.blue_bg);
                break;
        }
    }
}
