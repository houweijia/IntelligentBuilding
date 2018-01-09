package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.CusAdapter;
import com.example.veigar.intelligentbuilding.base.ViewHolder;
import com.example.veigar.intelligentbuilding.bean.EnvironmentDetailTableData;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MyUtils;

import java.util.List;

/**
 * Created by veigar on 2017/3/31.
 */

public class EnvironmentDetailTableAdapter extends CusAdapter<EnvironmentDetailTableData>{
    public EnvironmentDetailTableAdapter(Context context, List<EnvironmentDetailTableData> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(int position, View itemView) {
        TextView sensorName = ViewHolder.get(itemView, R.id.tv_sensor_name);
        TextView sensorId = ViewHolder.get(itemView, R.id.tv_sensor_id);
        TextView sensorTime = ViewHolder.get(itemView, R.id.tv_time);
        TextView sensorData = ViewHolder.get(itemView, R.id.tv_sensor_data);
        RelativeLayout relative = ViewHolder.get(itemView,R.id.relative);


        EnvironmentDetailTableData data = list.get(position);
        if(data.getNodedata().get(0) != null){
            sensorId.setText(data.getNodedata().get(0));
        }

        if(data.getNodedata().get(1) != null){
            sensorName.setText(MyUtils.toUtf8(data.getNodedata().get(1)));
        }

        if(data.getNodedata().get(3) != null){
            sensorData.setText(data.getNodedata().get(2));
        }

        if(data.getNodedata().get(4) != null){
            String time = MyUtils.timet(data.getNodedata().get(4));
            sensorTime.setText(time);
        }

        if(position%2==0){
            relative.setBackgroundResource(R.color.textWhite);
        }else{
            relative.setBackgroundResource(R.color.grey);
        }
        return itemView;
    }
}
