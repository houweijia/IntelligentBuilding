package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.bean.EnvironmentData;

import java.util.List;

/**
 * Created by veigar on 2017/3/31.
 */

public class ListViewSlideAdapter extends BaseAdapter{

    private List<EnvironmentData> list;
    private Context context;
    private OnClickListenerEditOrDelete onClickListenerEditOrDelete;

    public ListViewSlideAdapter(Context context, List<EnvironmentData> list){
        this.list =list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.enviroment_list_item,null);
            holder.tvData = (TextView) convertView.findViewById(R.id.transducer_data);
            holder.tvName = (TextView) convertView.findViewById(R.id.transducer_type);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvData.setText("湿度");
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListenerEditOrDelete!=null){
                    onClickListenerEditOrDelete.OnClickListenerEdit(position);
                }
            }
        });
        return convertView;
    }

    public interface OnClickListenerEditOrDelete{
        void OnClickListenerEdit(int position);
        void OnClickListenerDelete(int position);
    }

    public void setOnClickListenerEditOrDelete(OnClickListenerEditOrDelete onClickListenerEditOrDelete1){
        this.onClickListenerEditOrDelete=onClickListenerEditOrDelete1;
    }


    private class ViewHolder{
        TextView tvName,tvEdit,tvData,tvDel;
        ImageView img;
    }
}
