package com.example.veigar.intelligentbuilding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;

import java.util.LinkedHashMap;
import java.util.Map;

public class EachEnergyActivity extends BaseActivity implements View.OnClickListener{


    private String id;
    private String name;
    private String data;
    private String url = "control";
    private boolean flag = false; //开关状态
    private ImageView powerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_energy);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        L.e("id===="+id);
        name = intent.getStringExtra("name");
        data = intent.getStringExtra("data");
        initView();

    }

    private void initView() {
        initToolBar();
        if(name!=null){
            setPageTitle(name);

        }
        powerSwitch = (ImageView) findViewById(R.id.power_switch);
        TextView sensorData = (TextView) findViewById(R.id.tv_sensor_data);
        DashedCircularProgress dashedCircularProgress = (DashedCircularProgress) findViewById(R.id.dash_progress);
        dashedCircularProgress.setMax(Float.valueOf(data)*5*1000);
        dashedCircularProgress.setValue(Float.valueOf(data)*1000);
        sensorData.setText(Float.valueOf(data)*1000+"W");

        powerSwitch.setOnClickListener(this);
    }

    private void cmd(String param,String nodeId,String gwid){

        Map<String,String> map = new LinkedHashMap<>();
        map.put("gwid",gwid);
        map.put("nodeid",nodeId);
        map.put("cmd","1");
        map.put("param",param);


        RequestManager.getInstance().post(RequestAPI.URL + url, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                L.e(s);
                flag = !flag;
                if(flag){
                    powerSwitch.setImageResource(R.mipmap.close);
                }else{
                    powerSwitch.setImageResource(R.mipmap.open);
                }


            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e("volleyError"+volleyError.getMessage());

            }
        },map);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.power_switch:
                L.e("gwid"+id.substring(0,8));
                if(flag){
                    cmd("0",id,id.substring(0,8));

                }else{
                    cmd("1",id,id.substring(0,8));
                }
                break;
        }
    }
}
