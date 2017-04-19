package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.CusAdapter;
import com.example.veigar.intelligentbuilding.base.ViewHolder;
import com.example.veigar.intelligentbuilding.bean.SecurityData;

import java.util.List;

/**
 * Created by veigar on 2017/4/5.
 */

public abstract class SecurityAdapter extends CusAdapter<SecurityData>{
    public SecurityAdapter(Context context, List<SecurityData> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(int position, View itemView) {

        Switch sw = ViewHolder.get(itemView,R.id.sw);
        ImageView img = ViewHolder.get(itemView,R.id.have_people_img);

        switch (list.get(position).getType()){
            case 1:
                img.setVisibility(View.VISIBLE);
                sw.setVisibility(View.GONE);

                break;
            case 2:
                img.setVisibility(View.GONE);
                sw.setVisibility(View.VISIBLE);
                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        swlistener(isChecked);
                    }
                });
                break;
        }
        return itemView;
    }

    public abstract void swlistener(boolean isChecked);

    ;
}
