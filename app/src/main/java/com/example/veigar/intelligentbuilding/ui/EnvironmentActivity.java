package com.example.veigar.intelligentbuilding.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.EnvironmentAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.AppConst;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EnvironmentActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout mProgress;
    private ListView mList;
    private PullRefreshLayout swipeRefreshLayout;
    private String equipmentID;
    private String getUrl = "dev_data";

    private PopupWindow popup;
    private RelativeLayout relative;
    private EditText sensorName;
    private EnvironmentAdapter adapter;
    private String url = "config_name";
    private String a = "8,9,12";
    private List<EnvironmentData> environmentDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviroment);
        initView();

    }

    private void initView() {

        initToolBar();
        Intent intent = getIntent();
        equipmentID = intent.getStringExtra(AppConst.EQUIPMENT)+"";
        //moduleID = intent.getIntExtra(AppConst.MODULE_ID,-1)+"";
        L.e("equipmentID="+equipmentID);

        initToolBar();
        setPageTitle(getString(R.string.environmental_monitoring));
        relative = (RelativeLayout) findViewById(R.id.activity_enviroment);
        mProgress = (RelativeLayout) findViewById(R.id.my_progress);
        mList = (ListView)findViewById(R.id.my_list);
        swipeRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        loadData();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter!=null){
                    String sensorId = adapter.getItem(position).getNodedata().get(0);
                    String sensorType = adapter.getItem(position).getNodedata().get(2);
                    Intent intent1 = new Intent(context,EnvironmentDetailActivity.class);
                    intent1.putExtra("sensorid",sensorId);
                    intent1.putExtra("sensortype",sensorType);
                    startActivity(intent1);
                }
            }
        });

        /**
         * list长按点击事件
         */
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showPopupWindow();

                return false;
            }
        });

    }



    /**
     * 上拉刷新监听
     */
    private PullRefreshLayout.OnRefreshListener refreshListener = new PullRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
        if(environmentDatas != null){
            environmentDatas.clear();
            loadData();
        }
        }
    };

    /**
     * 加载数据
     */
    private void loadData(){
        mProgress.setVisibility(View.VISIBLE);
        Map<String,String> map = new HashMap<>();
        map.put("devid","00000001");
        //map.put("sensorid","0000000100000001");//修改
        map.put("type",a);//修改
        map.put("flag","2");
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(context,"userInformation","1111"));
        L.e(userInformation+"---user---");
        if(!userInformation.equals(-1)){
             String code = MD5Utils.md5(userInformation+random);
             map.put("code",code);
        }

        map.put("random",random);
        RequestManager.getInstance().get(RequestAPI.URL+getUrl, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                mProgress.setVisibility(View.INVISIBLE);
                L.e("sssssss"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    Object object2 = object.get("result");
                    environmentDatas = JSONParseUtils.parseArray(String.valueOf(object2), EnvironmentData.class);
                    String data = environmentDatas.get(0).getNodedata().get(1);
                    L.e("data==="+data);
                    adapter = new EnvironmentAdapter(context,environmentDatas, R.layout.enviroment_list_item);
                    mList.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //environmentDataList = JSONParseUtils.parseArray(s, EnvironmentData.class);
                //EnvironmentAdapter adapter = new EnvironmentAdapter(context, environmentDataList,R.layout.enviroment_list_item);
                //mList.setAdapter(adapter);
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgress.setVisibility(View.INVISIBLE);
            }
        },map);
    }








    public void showPopupWindow(){
        View view = getLayoutInflater().inflate(R.layout.pop_window_layout,null);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        sensorName = (EditText) view.findViewById(R.id.et_sensor_name);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        if(popup == null){
            popup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);//这里给它设置了弹出的时间，
        imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
        if(popup.isShowing()){
            popup.dismiss();
        }else{
            popup.showAtLocation(relative, Gravity.CENTER , 0, 0);
            backgroundAlpha(0.5f);
        }

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);

            }
        });

    }

    /**
     * 设置屏幕透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure:
                String name = sensorName.getText().toString();
                L.e("aaaaaa");
                changeName(name);

                break;
            case R.id.tv_cancel:
                if(popup.isShowing()&&popup!=null){
                    popup.dismiss();

                }

                break;
        }
    }


    private void changeName(String name){
        Map<String,String> map = new HashMap<>();
        map.put("sensorname",name);
        map.put("sensorid","1");
        map.put("sensortype","1");
        RequestManager.getInstance().get(RequestAPI.URL+url, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                //loadData();//改完名之后再刷新下数据
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },map);

    }
}
