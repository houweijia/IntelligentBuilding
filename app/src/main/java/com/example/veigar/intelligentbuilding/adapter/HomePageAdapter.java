package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.bean.EquipmentInformation;
import com.example.veigar.intelligentbuilding.bean.Result;
import com.example.veigar.intelligentbuilding.ui.fragment.HomePageFragment;
import com.example.veigar.intelligentbuilding.util.L;

import java.util.List;

/**
 * Created by veigar on 2017/3/7.
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>{
    private Context context;
    private List<Result> list;
    private OnItemClickListener mOnItemClickListener;

    public HomePageAdapter(Context context,List<Result> list){
        this.context = context;
        this.list = list;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public HomePageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomePageViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_home_page_item,parent,false));
    }

    @Override
    public void onBindViewHolder(HomePageViewHolder holder, int position) {
        Result data = list.get(position);
        //L.e("data==="+data.getDev());
        holder.equipmentName.setText(data.getDev().get(1));//设备名称
        holder.lastTime.setText(data.getDev().get(3));//上次登录时间
        if(data.getDev().get(4).equals("0")){
            holder.whetherOnline.setImageResource(R.mipmap.circle_red);
        }else{
            holder.whetherOnline.setImageResource(R.mipmap.circle_green);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class HomePageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView equipmentName,lastTime;
        ImageView whetherOnline;

        public HomePageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            equipmentName = (TextView) itemView.findViewById(R.id.equipment_name);
            lastTime = (TextView) itemView.findViewById(R.id.tv_last_login_time);
            whetherOnline = (ImageView) itemView.findViewById(R.id.whether_online);
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
