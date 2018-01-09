package com.example.veigar.intelligentbuilding.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseFragment;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by veigar on 2017/3/7.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener{

    private EditText serialNo;
    private EditText confirmationNo;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting,container,false);
    }

    @Override
    public void init(View rootView) {
        //序列号
        serialNo = (EditText) rootView.findViewById(R.id.et_serial_no);
        //验证码
        confirmationNo = (EditText) rootView.findViewById(R.id.et_confirmation_no);
        Button btn_serial_get = (Button) rootView.findViewById(R.id.btn_serial_get);//序列号按钮
        Button btn_confirmation_get = (Button) rootView.findViewById(R.id.btn_confirmation_get);//验证码按钮

        btn_serial_get.setOnClickListener(this);
        btn_confirmation_get.setOnClickListener(this);
        if(!SPUtils.get(activity,"confirmationNo","0").equals("0")){
            confirmationNo.setText((String)SPUtils.get(activity,"confirmationNo","0"));
        }
        if(!SPUtils.get(activity,"serialNo","0").equals("0")){
            serialNo.setText((String)SPUtils.get(activity,"serialNo","0"));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirmation_get:

                SPUtils.put(activity,"confirmationNo",confirmationNo.getText()+"");
                break;
            case R.id.btn_serial_get:
                SPUtils.put(activity,"serialNo",serialNo.getText()+"");
                break;
        }
    }


}
