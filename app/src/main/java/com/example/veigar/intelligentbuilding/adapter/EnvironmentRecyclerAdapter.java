package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.MyUtils;

import java.util.List;

/**
 * Created by veigar on 2017/4/21.
 */

public class EnvironmentRecyclerAdapter extends RecyclerView.Adapter<EnvironmentRecyclerAdapter.EnvironmentViewHolder>{
    private List<EnvironmentData> list;
    private Context context;
    private EnvironmentRecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public EnvironmentRecyclerAdapter(List<EnvironmentData> list, Context context){
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(EnvironmentRecyclerAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    @Override
    public EnvironmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EnvironmentViewHolder(LayoutInflater.from(context).inflate(R.layout.environment_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(EnvironmentViewHolder holder, int position) {
        EnvironmentData data = list.get(position);
        if(data.getNodedata().get(2)!=null){
            switch (data.getNodedata().get(2)){
                case "8":
                    holder.sensorName.setText("温度");
                    holder.sensorImg.setImageResource(R.mipmap.img_tem);
                    break;
                case "9":
                    holder.sensorName.setText("湿度");
                    holder.sensorImg.setImageResource(R.mipmap.img_humidity);
                    break;
                case "11":
                    holder.sensorName.setText("光照强度");
                    holder.sensorImg.setImageResource(R.mipmap.img_sunshine);
                    break;
                case "13":
                    holder.sensorName.setText("烟雾");
                    holder.sensorImg.setImageResource(R.mipmap.img_smoke);
                    break;
                case "14":
                    holder.sensorName.setText("气压");
                    holder.sensorImg.setImageResource(R.mipmap.img_air_pressure);
                    L.e("气压");
                    break;
                case "15":
                    holder.sensorName.setText("海拔");
                    holder.sensorImg.setImageResource(R.mipmap.img_height);
                    break;
                case "17":
                    holder.sensorName.setText("噪声");
                    holder.sensorImg.setImageResource(R.mipmap.img_noise);
                    break;
                case "19":
                    holder.sensorName.setText("火焰");
                    holder.sensorImg.setImageResource(R.mipmap.img_flame);
                    break;
                case "20":
                    holder.sensorName.setText("雨滴");
                    holder.sensorImg.setImageResource(R.mipmap.img_rain);
                    break;
                case "21":
                    holder.sensorName.setText("一氧化碳");
                    holder.sensorImg.setImageResource(R.mipmap.img_co);
                    break;
                case "22":
                    holder.sensorName.setText("甲醛");
                    holder.sensorImg.setImageResource(R.mipmap.img_jq);
                    break;
                case "52":
                    holder.sensorName.setText("风速");
                    holder.sensorImg.setImageResource(R.mipmap.img_wind_speed);
                    break;
                case "53":
                    holder.sensorName.setText("风向");
                    holder.sensorImg.setImageResource(R.mipmap.img_wind_direction2);
                    break;

            }
        }

        //holder.sensorName.setText(MyUtils.toUtf8(data.getNodedata().get(1)));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class EnvironmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView sensorImg;
        TextView sensorName;

        public EnvironmentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            sensorImg = (ImageView) itemView.findViewById(R.id.sensor_img);
            sensorName = (TextView) itemView.findViewById(R.id.sensor_name);
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
