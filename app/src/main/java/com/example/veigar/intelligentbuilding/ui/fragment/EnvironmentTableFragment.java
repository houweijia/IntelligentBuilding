package com.example.veigar.intelligentbuilding.ui.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.EnvironmentDetailTableAdapter;
import com.example.veigar.intelligentbuilding.base.BaseFragment;
import com.example.veigar.intelligentbuilding.bean.EnvironmentDetailTableData;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.util.JSONParseUtils;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.LoadType;
import com.example.veigar.intelligentbuilding.util.MD5Utils;
import com.example.veigar.intelligentbuilding.util.RefreshLayout;
import com.example.veigar.intelligentbuilding.util.RequestAPI;
import com.example.veigar.intelligentbuilding.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by veigar on 2017/3/30.
 */

public class EnvironmentTableFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener{

    private ListView mList;
    private RefreshLayout refreshLayout;
    private RelativeLayout progressLayout;
    private EnvironmentDetailTableAdapter adapter;
    private List<EnvironmentDetailTableData> list;
    private List timeList = new LinkedList();
    private int count = 0;
    private String url = "dev_data";
    private String time;
    private String id,type;
    private TextView noData;


    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two,container,false);
    }

    @Override
    public void init(View rootView) {
        id = (String) SPUtils.get(activity,"id","0");
        type = (String) SPUtils.get(activity,"type","0");
        L.e(id);
        L.e(type);
        mList = (ListView) rootView.findViewById(R.id.my_list);
        noData = (TextView) rootView.findViewById(R.id.tv_no_data);
        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        progressLayout = (RelativeLayout) rootView.findViewById(R.id.my_progress);
        if(adapter == null && activity != null){
            time = String.valueOf(System.currentTimeMillis()).substring(0,10);
            timeList.add(time);
            //loadData(LoadType.FIRST,timeList.get(0).toString());
            loadData(LoadType.FIRST,"1490779662");
        }

    }


    private void loadData(final LoadType loadType,String t){
        showLoading(loadType);
        Map<String,String> map = new HashMap<>();
        map.put("flag","0");
        if(id!=null && type!=null){
            map.put("sensorid","00000001000000001");
            //map.put("sensorid",id);

            //map.put("type",type);
            map.put("type","8");
        }
        L.e("t==="+timeList.get(0));
        //String time = String.format("%010d",t);



        //L.e(time);

        map.put("time",t);

        map.put("count",String.valueOf(count));
        String random = String.valueOf(System.currentTimeMillis());
        String userInformation = String.valueOf(SPUtils.get(activity,"userInformation","1111"));
        L.e(userInformation+"---user---");
        if(!userInformation.equals(-1)){
            String code = MD5Utils.md5(userInformation+random);
            map.put("code",code);
        }
        RequestManager.getInstance().get(RequestAPI.URL+url,  new RequestManager.ResponseListener() {

            @Override
            public void onResponse(String s) {
                L.e("s====="+s);
                try {
                    JSONObject object = new JSONObject(s);
                    Object result = object.get("result");

                    list = JSONParseUtils.parseArray(String.valueOf(result), EnvironmentDetailTableData.class);
                    time = list.get(0).getNodedata().get(4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (loadType){
                    case REFRESH://下拉刷新
                        if(list == null || list.isEmpty()){
                            return;
                        }

                        List<EnvironmentDetailTableData> tempList = new ArrayList<>();
                        for(EnvironmentDetailTableData et : list){
                            tempList.add(et);
                        }
                        if(tempList != null && tempList.isEmpty()){
                            adapter.addAll(0,tempList);
                        }
                        noData.setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);

                        progressLayout.setVisibility(View.GONE);
                        break;
                    case PAGE://上拉加载
                        if((list == null || list.isEmpty()) || list.size()!=30){
                            refreshLayout.completePageData();
                            return;
                        }
                        noData.setVisibility(View.GONE);
                        adapter.addAll(list);
                        //隐藏上拉加载布局
                        refreshLayout.setLoading(false);
                        count++;
                        break;
                    case FIRST://第一次加载
                        if(list == null || list.isEmpty()){
                            L.e("无数据");
                            noData.setVisibility(View.VISIBLE);
                            return;
                        }
                        count++;
                        adapter = new EnvironmentDetailTableAdapter(activity,list, R.layout.environment_table_list_item);
                        mList.setAdapter(adapter);
                        progressLayout.setVisibility(View.GONE);
                        noData.setVisibility(View.GONE);
                        break;
                }
            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.e("失败"+volleyError);
                //refreshLayout.setLoading(false);
                //noData.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                Toast.makeText(activity,"没有最新数据",Toast.LENGTH_SHORT).show();
            }
        },map);

    }


    @Override
    public void onRefresh() {
        loadData(LoadType.REFRESH,time);
    }

    @Override
    public void onPageLoad() {
        loadData(LoadType.PAGE,timeList.get(0).toString());
        L.e("1111111");
    }

    /**
     * 显示进度条
     * @param loadType
     */
    public void showLoading(LoadType loadType) {
        switch (loadType) {
            case REFRESH: //下拉刷新
                progressLayout.setVisibility(View.GONE);
                break;
            case PAGE: //上拉加载
                progressLayout.setVisibility(View.GONE);
                break;
            case FIRST: //第一次加载
                //显示加载进度条
                progressLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
