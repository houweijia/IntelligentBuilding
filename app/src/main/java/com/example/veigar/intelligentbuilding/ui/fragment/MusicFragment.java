package com.example.veigar.intelligentbuilding.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.MusicAdapter;
import com.example.veigar.intelligentbuilding.base.BaseFragment;
import com.example.veigar.intelligentbuilding.bean.MusicData;
import com.example.veigar.intelligentbuilding.ui.VideoActivity;
import com.example.veigar.intelligentbuilding.util.L;
import com.example.veigar.intelligentbuilding.util.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veigar on 2017/4/19.
 */

public class MusicFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private AlertDialog alertDialog;
    private Player player;
    private SeekBar seekBar;
    private ImageView start;
    private ImageView stop;
    private List<MusicData> list;
    private int index;

    @Override
    public View getRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music,container,false);
    }

    @Override
    public void init(View rootView) {


        initDialog();
        loadData();
        final ListView mList = (ListView) rootView.findViewById(R.id.my_list);
        MusicAdapter musicAdapter = new MusicAdapter(activity, list, R.layout.music_item, new MusicAdapter.ClickListener() {
            @Override
            public void btnClick(int position) {
                if(list.get(position).getType().equals("0")){
                    alertDialog.show();
                    WindowManager windowManager = activity.getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                    layoutParams.height = (int)(display.getHeight()*0.4);
                    layoutParams.width = (int)(display.getWidth()*0.65);
                    alertDialog.getWindow().setAttributes(layoutParams);
                    if(player==null){
                        player = new Player(seekBar);
                        seekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                player.playUrl("http://www.iothinking.com/Butterfly.mp3");
                            }
                        }).start();
                    }
                }else{
                    Intent intent = new Intent(activity, VideoActivity.class);
                    startActivity(intent);
                }
            }
        });

        mList.setAdapter(musicAdapter);
        mList.setOnItemClickListener(this);
        final ImageView play = (ImageView) rootView.findViewById(R.id.image_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    private void loadData(){
        MusicData data = new MusicData();
        MusicData data2 = new MusicData();
        MusicData data3 = new MusicData();
        MusicData data4 = new MusicData();
        data.setDuration("4:22");
        data.setMusicName("ButterFly");
        data.setType("0");

        data2.setDuration("3:51");
        data2.setMusicName("七月上");
        data2.setType("0");

        data3.setDuration("2:49");
        data3.setMusicName("光辉岁月");
        data3.setType("0");

        data4.setDuration("4:49");
        data4.setMusicName("焦点访谈");
        data4.setType("1");

        list = new ArrayList<>();
        list.add(data);
        list.add(data2);
        list.add(data3);
        list.add(data4);
    }


    private void initDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(activity,R.layout.dialog_layout,null);
        builder.setView(view);
        alertDialog = builder.create();
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        start = (ImageView) view.findViewById(R.id.image_start);
        stop = (ImageView) view.findViewById(R.id.image_stop);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(player!=null){
                    player.stop();
                    player=null;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_start:
                start.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                if(player!=null){
                    player.pause();
                }

                break;
            case R.id.image_stop:
                if(player!=null){
                    player.play();
                }
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * listView点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        index = position;
        L.e("listView");
    }



    // 进度改变
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(player!=null){
            player.stop();
            player=null;
        }

    }
}
