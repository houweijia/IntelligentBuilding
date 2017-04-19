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

public class HomePageFragment extends BaseFragment{

    private RecyclerView mRecycleView;
    private List<EquipmentInformation> list;
    private String url = "http://115.28.77.207:8086/webapi/get_dev";
    private List<Result> results;
    private HomePageAdapter adapter;
    private String sJson = "{\"resultcode\":\"200\",\"reason\":\"success\",\"result\":[{\"dev0\":[\"00000001\",\"one\",\"1\",\"1\",\"1\"]},{\"dev0\":[\"00000002\",\"two\",\"3\",\"2\",\"0\"]},{\"dev0\":[\"00000003\",\"three\",\"2\",\"3\",\"0\"]}],\"error_code\":\"0\"}";
    private RelativeLayout mProgress;

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
        //addData();

        mProgress.setVisibility(View.VISIBLE);

        loadData();
    }

    /**
     * 假数据
     */
    /*private void addData() {
        EquipmentInformation data =  new EquipmentInformation();
        EquipmentInformation data2 =  new EquipmentInformation();
        EquipmentInformation data3 =  new EquipmentInformation();
        EquipmentInformation data4 =  new EquipmentInformation();
        EquipmentInformation data5 =  new EquipmentInformation();
        data.setName("小王的家");
        data.setId(101);
        data2.setName("小张的家");
        data2.setId(102);
        data3.setName("小冉的家");
        data3.setId(103);
        data4.setName("小康的家");
        data4.setId(104);
        data5.setName("小刚的家");
        data5.setId(105);
        list = new ArrayList<>();
        list.add(data);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);

    }
*/
    private void loadData(){
        Map<String,String> map = new HashMap<>();
        String random = String.valueOf(SystemClock.currentThreadTimeMillis());
        String userInformation = (String) SPUtils.get(activity,"userInformation","-1");
        if(!userInformation.equals("-1")){
            String code = MD5Utils.md5(userInformation+random);

            map.put("code",code);
        }

        map.put("random",random);
        RequestManager.getInstance().get(url, new RequestManager.ResponseListener() {
            @Override
            public void onResponse(String s) {
                mProgress.setVisibility(View.GONE);
                L.e("home"+s);
                JSONObject object1 = null;
                try {
                    object1 = new JSONObject(s);
                    Object result2 = object1.get("result");
                    results = JSONParseUtils.parseArray(String.valueOf(result2), Result.class);
                    //String s1 = results.get(1).getDev().get(1);
                    //L.e("s1===="+s1);
                    //results.size();
                    L.e("results==="+results.size());

                    adapter = new HomePageAdapter(activity,results);
                    mRecycleView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new HomePageAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position = mRecycleView.getChildAdapterPosition(view);
                            int itemViewType = adapter.getItemViewType(position);
                            L.e("position==="+position);
                            L.e("itemViewType==="+itemViewType);
                            Intent intent = new Intent(activity, DetailActivity2.class);
                            L.e("ID==="+results.get(position).getDev().get(0));
                            intent.putExtra("id",results.get(position).getDev().get(0));
                            startActivity(intent);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }


            }
        }, new RequestManager.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgress.setVisibility(View.GONE);
                L.e("失败");
            }
        },map);
    }
}
