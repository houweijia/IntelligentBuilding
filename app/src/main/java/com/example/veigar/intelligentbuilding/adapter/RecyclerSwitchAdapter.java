package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.bean.SwitchTestData;
import com.example.veigar.intelligentbuilding.weight.RecyclerViewDivider;

import java.util.List;

/**
 * Created by veigar on 2017/8/16.
 */

public class RecyclerSwitchAdapter extends RecyclerView.Adapter<RecyclerSwitchAdapter.SwitchViewHolder>{
    private List<EnvironmentData> list;
    private Context context;
    private RecyclerSwitchAdapter.OnItemClickListener mOnItemClickListener;

    public RecyclerSwitchAdapter(Context context,List<EnvironmentData> list){
        this.context = context;
        this.list = list;

    }

    public void setOnItemClickListener(RecyclerSwitchAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public SwitchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SwitchViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_switch_item,parent,false));
    }

    @Override
    public void onBindViewHolder(SwitchViewHolder holder, int position) {
        /*EnvironmentData data = list.get(position);
        if(data.getNodedata().get(2)!=null){
            switch (data.getNodedata().get(2)){
                case "37":
                    break;
                case "38":
                    break;
                case "39":
                    break;
            }
        }*/
        EnvironmentData data = list.get(position);
        if(data.getNodedata().get(2).equals("37")){
            holder.name.setText("灯");
        }

        if(data.getNodedata().get(2).equals("38")){
            holder.name.setText("风扇");
        }

        if(data.getNodedata().get(2).equals("39")){
            holder.name.setText("声光报警");
        }

        if(data.getNodedata().get(3).equals("0")){
            holder.sw.setChecked(false);
            holder.state.setText("关");
        }else{
            holder.sw.setChecked(true);
            holder.state.setText("开");
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class SwitchViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        Button btnSwitch;
        TextView name;
        Switch sw;
        TextView state;

        public SwitchViewHolder(View itemView) {
            super(itemView);
            //btnSwitch = (Button) itemView.findViewById(R.id.btn_switch);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            sw = (Switch) itemView.findViewById(R.id.sw);
            state = (TextView) itemView.findViewById(R.id.tv_state);
            //btnSwitch.setOnClickListener(this);
            sw.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(isChecked,getLayoutPosition());
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(boolean isChecked,int position);
    }


}
