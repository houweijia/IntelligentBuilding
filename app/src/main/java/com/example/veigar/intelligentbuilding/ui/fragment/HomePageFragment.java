package com.example.veigar.intelligentbuilding.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.HomePageAdapter;
import com.example.veigar.intelligentbuilding.base.BaseFragment;
import com.example.veigar.intelligentbuilding.bean.EquipmentInformation;
import com.example.veigar.intelligentbuilding.ui.DetailActivity;
import com.example.veigar.intelligentbuilding.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veigar on 2017/3/7.
 */

public class HomePageFragment extends BaseFragment{

    private RecyclerView mRecycleView;
    private List<EquipmentInformation> list;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page,container,false);
    }

    @Override
    public void init(View rootView) {
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(staggeredGridLayoutManager);
        addData();
        final HomePageAdapter adapter = new HomePageAdapter(activity,list);
        adapter.setOnItemClickListener(new HomePageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = mRecycleView.getChildAdapterPosition(view);
                int itemViewType = adapter.getItemViewType(position);
                long id = mRecycleView.getChildItemId(view);
                L.e("position==="+position);
                L.e("itemViewType==="+itemViewType);
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);

            }
        });
        mRecycleView.setAdapter(adapter);
    }

    private void addData() {
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
}
