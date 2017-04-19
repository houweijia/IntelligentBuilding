package com.example.veigar.intelligentbuilding.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.SecurityAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.SecurityData;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class SecurityActivity extends BaseActivity {

    private ListView mList;
    private List<SecurityData> list = new ArrayList<>();
    private SecurityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        addData();
        initView();


    }

    private void initView() {
        initToolBar();
        setPageTitle("楼宇安防");
        mList = (ListView) findViewById(R.id.my_list);
       /* Bitmap background = BitmapFactory.decodeResource(getResources(),R.mipmap.snow);
        Bitmap mohu = MyUtils.fastblur(background,40);
        BitmapDrawable drawable = new BitmapDrawable(mohu);
        mList.setBackground(drawable);*/
        adapter = new SecurityAdapter(context, list, R.layout.security_list_item) {

            @Override
            public void swlistener(boolean isChecked) {
                L.e("isChecked"+isChecked);
            }

        };
        mList.setAdapter(adapter);
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                L.e("1111");
                return false;
            }
        });
    }

    private void addData(){
        SecurityData data = new SecurityData();
        SecurityData data2 = new SecurityData();
        SecurityData data3 = new SecurityData();
        data.setType(1);
        data2.setType(2);
        data3.setType(2);
        list.add(data);
        list.add(data2);
        list.add(data3);
        list.add(data3);
        list.add(data3);
        list.add(data3);
    }
}
