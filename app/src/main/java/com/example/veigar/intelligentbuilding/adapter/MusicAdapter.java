package com.example.veigar.intelligentbuilding.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.CusAdapter;
import com.example.veigar.intelligentbuilding.base.ViewHolder;
import com.example.veigar.intelligentbuilding.bean.MusicData;

import java.util.List;

/**
 * Created by veigar on 2017/5/15.
 */

public class MusicAdapter extends CusAdapter<MusicData>{
    private ClickListener clickListener;

    public MusicAdapter(Context context, List<MusicData> list, int itemLayoutRes) {
        super(context, list, itemLayoutRes);
    }

    public MusicAdapter(Context context, List<MusicData> list, int itemLayoutRes,ClickListener clickListener) {
        super(context, list, itemLayoutRes);
        this.clickListener = clickListener;
    }

    @Override
    public View getCustomView(final int position, View itemView) {
        TextView musicName = ViewHolder.get(itemView, R.id.tv_music_name);
        TextView musicDuration = ViewHolder.get(itemView, R.id.tv_music_duration);
        ImageView type = ViewHolder.get(itemView,R.id.img_type);
        Button btnView = ViewHolder.get(itemView,R.id.btn_view);
        RelativeLayout relative = ViewHolder.get(itemView,R.id.relative);
        if(list!=null){
            musicName.setText(list.get(position).getMusicName());
            musicDuration.setText(list.get(position).getDuration());
            if(list.get(position).getType().equals("0")){
                type.setImageResource(R.mipmap.logo_music);
            }else{
                type.setImageResource(R.mipmap.logo_video);
            }
        }

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.btnClick(position);

            }
        });
        /*if(position%2==0){
            relative.setBackgroundResource(R.color.textWhite);
        }else{
            relative.setBackgroundResource(R.color.grey);
        }*/
        return itemView;
    }


        public interface ClickListener{
            void btnClick(int position);
        }
}
