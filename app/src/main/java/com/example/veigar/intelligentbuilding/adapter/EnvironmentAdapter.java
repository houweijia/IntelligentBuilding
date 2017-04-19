package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.CusAdapter;
import com.example.veigar.intelligentbuilding.base.ViewHolder;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;

import java.util.List;

/**
 * Created by veigar on 2017/3/11.
 */

public class EnvironmentAdapter extends CusAdapter<EnvironmentData>{
    public EnvironmentAdapter(Context context, List<EnvironmentData> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(int position, View itemView) {
        ImageView transducerImg = ViewHolder.get(itemView, R.id.transducer_img);
        TextView transducerType = ViewHolder.get(itemView, R.id.transducer_type);
        TextView transducerData = ViewHolder.get(itemView, R.id.transducer_data);
        LinearLayout linear = ViewHolder.get(itemView,R.id.linear);

        EnvironmentData data = getItem(position);
        if(data.getNodedata().get(2)!=null){
            transducerType.setText(data.getNodedata().get(2));
        }

        if(data.getNodedata().get(3)!=null){
            transducerData.setText(data.getNodedata().get(3));
        }


        if(position%2==0){
            linear.setBackgroundResource(R.color.textWhite);
        }else{
            linear.setBackgroundResource(R.color.grey);
        }
        return itemView;
    }
}
