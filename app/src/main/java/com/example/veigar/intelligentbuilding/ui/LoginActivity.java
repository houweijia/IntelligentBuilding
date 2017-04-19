package com.example.veigar.intelligentbuilding.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.LoginData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.DialogUtils;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.MyUtils;
import com.example.veigar.intelligentbuilding.util.SPUtils;
import com.example.veigar.intelligentbuilding.util.UiHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;

public class LoginActivity extends BaseActivity {

    private EditText btnUsername;
    private EditText btnPassword;
    private String url = "http://115.28.77.207:8086/webapi/login";
    private ProgressDialog progressDialog;


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

            progressDialog = DialogUtils.showProgressDialog(context, R.string.logining);
            final String username = btnUsername.getText().toString();
            final String password = btnPassword.getText().toString();
            String random = String.valueOf(System.currentTimeMillis());
            String str = username+password+random;
            L.e(str);
            String code = MD5Utils.md5(str);
            L.e(code);
            L.e(username+"----"+password);
            Map<String,String> params = new HashMap<>();
            params.put("username",username);
            params.put("code",code);
            params.put("random",random);
            RequestManager.getInstance().post(url, new RequestManager.ResponseListener() {
                @Override
                public void onResponse(String s) {
                    SPUtils.put(context,"username",username);
                    SPUtils.put(context,"password",password);
                    if(username != null && password != null)
                    SPUtils.put(context,"userInformation",username+password);
                    progressDialog.dismiss();
                    LoginData loginData = JSONParseUtils.parseObject(s, LoginData.class);
                    if(loginData.getMsg().equals("SUCCESS"))
                    UiHelper.forwardMainActivity(context);
                    L.e(s);

                }
            }, new RequestManager.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progressDialog.dismiss();
                    Toast.makeText(context,R.string.login_fail,Toast.LENGTH_SHORT).show();
                    L.e("失败"+volleyError);
                }
            },params);


        }
    };
}
