package com.example.veigar.intelligentbuilding.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.HomePageAdapter;
import com.example.veigar.intelligentbuilding.base.BaseFragment;
import com.example.veigar.intelligentbuilding.bean.EquipmentInformation;
import com.example.veigar.intelligentbuilding.bean.Result;
import com.example.veigar.intelligentbuilding.network.RequestManager;
import com.example.veigar.intelligentbuilding.ui.DetailActivity;
import com.example.veigar.intelligentbuilding.ui.DetailActivity2;
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

/**
 * Created by veigar on 2017/3/7.
 */

public class HomePageFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView mRecycleView;
    private List<EquipmentInformation> list;
    private String url = "get_dev";
    private List<Result> results = new ArrayList<>();
    private HomePageAdapter adapter;
    private String sJson = "{\"resultcode\":\"200\",\"reason\":\"success\",\"result\":[{\"dev0\":[\"00000001\",\"one\",\"1\",\"1\",\"1\"]},{\"dev0\":[\"00000002\",\"two\",\"3\",\"2\",\"0\"]},{\"dev0\":[\"00000003\",\"three\",\"2\",\"3\",\"0\"]}],\"error_code\":\"0\"}";
    private RelativeLayout mProgress;
    private TextView reloading;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page,container,false);
    }

    @Override
    public void init(View rootView) {
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        mProgress = (RelativeLayout) rootView.findViewById(R.id.my_progress);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(staggeredGridLayoutManager);
        reloading = (TextView) rootView.findViewById(R.id.tv_reloading);
        reloading.setOnClickListener(this);
        //addData();

        mProgress.setVisibility(View.VISIBLE);

        loadData();
    }

    private void loadData(){
        Map<String,String> map = new HashMap<>();
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = (String) SPUtils.get(activity,"userInformation","-1");
        L.e("code===="+userInformation);
        map.put("code",userInformation);
        map.put("random",random);
        RequestManager.getInstance().get(RequestAPI.URL+url, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                mProgress.setVisibility(View.GONE);
                L.e("home"+s);
                JSONObject object1 = null;
                try {
                    object1 = new JSONObject(s);
                    Object resultcode = object1.get("resultcode");
                    if(resultcode.equals("0")){
                        reloading.setVisibility(View.GONE);
                        Object result2 = object1.get("result");
                        results.clear();
                        results.addAll(JSONParseUtils.parseArray(String.valueOf(result2), Result.class));
                        //String s1 = results.get(1).getDev().get(1);
                        //L.e("s1===="+s1);
                        //results.size();
                        L.e("results==="+results.size());

                        adapter = new HomePageAdapter(activity,results);
                        adapter.notifyDataSetChanged();
                        mRecycleView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new HomePageAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                int position = mRecycleView.getChildAdapterPosition(view);
                                int itemViewType = adapter.getItemViewType(position);
                                L.e("position==="+position);
                                L.e("itemViewType==="+itemViewType);
                                if(results.get(position).getDev().get(4).equals("1")){
                                    Intent intent = new Intent(activity, DetailActivity2.class);
                                    L.e("ID==="+results.get(position).getDev().get(0));
                                    intent.putExtra("id",results.get(position).getDev().get(0));
                                    intent.putExtra("name",results.get(position).getDev().get(1));
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(activity,"设备不在线",Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                    }else{
                        reloading.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }


            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgress.setVisibility(View.GONE);
                reloading.setVisibility(View.VISIBLE);
                L.e("失败");
            }
        },map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_reloading:
                loadData();
                break;
        }
    }
}
