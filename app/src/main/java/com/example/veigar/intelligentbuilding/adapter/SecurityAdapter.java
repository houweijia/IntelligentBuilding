package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.CusAdapter;
import com.example.veigar.intelligentbuilding.base.ViewHolder;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.bean.SecurityData;
import com.example.veigar.intelligentbuilding.util.MyUtils;

import java.util.List;

/**
 * Created by veigar on 2017/4/5.
 */

public class SecurityAdapter extends CusAdapter<EnvironmentData>{
    public SecurityAdapter(Context context, List<EnvironmentData> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(int position, View itemView) {

        TextView havePeople = ViewHolder.get(itemView,R.id.tv_have_people);
        TextView sensorName = ViewHolder.get(itemView,R.id.sensor_name);

        EnvironmentData data = list.get(position);
        if(data.getNodedata().get(1)!=null){
            sensorName.setText(MyUtils.toUtf8(data.getNodedata().get(1)));
        }
        if(data.getNodedata().get(3)!=null){
            if(data.getNodedata().get(3).equals("0")){
                havePeople.setText("没人");
            }else{
                havePeople.setText("有人");
            }
        }
        return itemView;
    }
}
