package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.CusAdapter;
import com.example.veigar.intelligentbuilding.base.ViewHolder;
import com.example.veigar.intelligentbuilding.bean.PersonAttendanceData;
import com.example.veigar.intelligentbuilding.bean.Result1;
import com.example.veigar.intelligentbuilding.util.L;

import java.util.List;

/**
 * Created by veigar on 2017/3/24.
 */

public class AttendanceAdapter extends CusAdapter<Result1>{

    public AttendanceAdapter(Context context, List<Result1> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    @Override
    public View getCustomView(int position, View itemView) {
        TextView tv = ViewHolder.get(itemView,R.id.content);
        tv.setText(list.get(position).getTime());
        return itemView;
    }
}
