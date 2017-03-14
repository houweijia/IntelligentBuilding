package com.example.veigar.intelligentbuilding.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.util.UiHelper;

/**
 * Created by veigar on 2017/3/6.
 */

public class WelcomeActivity extends AppCompatActivity{

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        setFullScreen();
        initView();
    }

    private void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UiHelper.forwardLoginActivity(context);
                finish();
            }
        },3000L);
    }

    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
