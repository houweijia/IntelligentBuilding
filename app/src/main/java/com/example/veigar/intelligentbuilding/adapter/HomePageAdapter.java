package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.bean.EquipmentInformation;
import com.example.veigar.intelligentbuilding.ui.fragment.HomePageFragment;

import java.util.List;

/**
 * Created by veigar on 2017/3/7.
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder>{
    private Context context;
    private List<EquipmentInformation> list;
    private OnItemClickListener mOnItemClickListener;

    public HomePageAdapter(Context context,List<EquipmentInformation> list){
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
        EquipmentInformation data = list.get(position);
        holder.equipmentName.setText(data.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class HomePageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView equipmentName;

        public HomePageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            equipmentName = (TextView) itemView.findViewById(R.id.equipment_name);
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
