package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.bean.SwitchTestData;
import com.example.veigar.intelligentbuilding.util.MyUtils;

import java.util.List;

/**
 * Created by veigar on 2017/8/16.
 */

public class IntelligentFactoryRecyclerAdapter extends RecyclerView.Adapter<IntelligentFactoryRecyclerAdapter.IntelligentFactoryViewHolder>{
    private List<EnvironmentData> list;
    private Context context;
    private int height;
    private IntelligentFactoryRecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public IntelligentFactoryRecyclerAdapter(Context context,List<EnvironmentData> list,int height){
        this.context = context;
        this.list = list;
        this.height = height;
    }

    public void setOnItemClickListener(IntelligentFactoryRecyclerAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    @Override
    public IntelligentFactoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IntelligentFactoryViewHolder(LayoutInflater.from(context).inflate(R.layout.factory_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(IntelligentFactoryViewHolder holder, int position) {
        EnvironmentData data = list.get(position);
        if(data!=null){
            holder.sensorName.setText(MyUtils.toUtf8(data.getNodedata().get(1)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class IntelligentFactoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView sensorName;
        LinearLayout linearLayout;
        public IntelligentFactoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            sensorName = (TextView) itemView.findViewById(R.id.tv_sensor_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            //layoutParams.width = width/2;
            layoutParams.height = height/2;
            linearLayout.setLayoutParams(layoutParams);

        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v);
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view);
    }
}
