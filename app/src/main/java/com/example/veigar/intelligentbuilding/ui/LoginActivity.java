package com.example.veigar.intelligentbuilding.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MyUtils;
import com.example.veigar.intelligentbuilding.util.UiHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private EditText btnUsername;
    private EditText btnPassword;
    private String url = "http://git.oschina.net/api/v3/session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        Bitmap background = BitmapFactory.decodeResource(getResources(),R.mipmap.snow);
        Bitmap mohu = MyUtils.fastblur(background,40);
        BitmapDrawable drawable = new BitmapDrawable(mohu);
        LinearLayout activityLogin = (LinearLayout) findViewById(R.id.activity_login);
        activityLogin.setBackground(drawable);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(listener);
        btnUsername = (EditText) findViewById(R.id.username);
        btnPassword = (EditText) findViewById(R.id.password);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = btnUsername.getText().toString();
            String password = btnPassword.getText().toString();
            L.e(username+"----"+password);
            Map<String,String> params = new HashMap<>();
            params.put("email",username);
            params.put("password",password);
            RequestManager.getInstance().post(url, new RequestManager.ResponseListener() {
                @Override
                public void onResponse(String s) {
                    L.e(s);
                }
            }, new RequestManager.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    L.e("失败"+volleyError);
                }
            },params);


           //UiHelper.forwardMainActivity(context);
        }
    };
}
