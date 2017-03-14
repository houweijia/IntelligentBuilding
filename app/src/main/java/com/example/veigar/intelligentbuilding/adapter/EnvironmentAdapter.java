package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

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
        ImageView transducerType = ViewHolder.get(itemView, R.id.transducer_type);
        ImageView transducerData = ViewHolder.get(itemView, R.id.transducer_data);

        EnvironmentData data = getItem(position);
        return itemView;
    }
}
