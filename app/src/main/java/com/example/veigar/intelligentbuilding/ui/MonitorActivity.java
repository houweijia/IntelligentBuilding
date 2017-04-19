package com.example.veigar.intelligentbuilding.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.adapter.SecurityAdapter;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.bean.SecurityData;
import com.example.veigar.intelligentbuilding.util.L;
import com.ezviz.player.EZVoiceTalk;
import com.ezviz.stream.EZStreamClientManager;
import com.ezviz.stream.EZTaskMgr;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIPlayer;
import com.videogo.openapi.EZPlayer;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.stream.EZStreamParamHelp;
import com.videogo.widget.CheckTextButton;
import com.videogo.widget.CustomTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veigar on 2017/4/12.
 */

public class MonitorActivity extends BaseActivity implements View.OnClickListener{
    private int mStatus = RealPlayStatus.STATUS_INIT;//播放状态
    private String url = "ezopen://RDTCFA@open.ys7.com/515427403/1.live";
    private MyOrientationDetector mOrientationDetector;
    private EZUIPlayer mPlayer;
    private long firstTime = 0L;

    private boolean flag = false;

    private ListView mList;
    private List<SecurityData> list = new ArrayList<>();
    private SecurityAdapter adapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    L.e("2222");

                    break;
            }
        }
    };
    private RelativeLayout relative;
    private LinearLayout back;
    private ObjectAnimator object;
    private LinearLayout statusBar;
    private ImageButton realplay_play_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motitor);
        initView();
    }

    private void initView(){
        /*initToolBar();
        setPageTitle(getString(R.string.monitor));*/
        addData();
        mList = (ListView) findViewById(R.id.my_list);
        adapter = new SecurityAdapter(context, list, R.layout.security_list_item) {

            @Override
            public void swlistener(boolean isChecked) {
                L.e("isChecked"+isChecked);
            }

        };
        mList.setAdapter(adapter);
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                L.e("1111");
                return false;
            }
        });
        realplay_play_btn = (ImageButton) findViewById(R.id.realplay_play_btn);
        ImageButton realplay_sound_btn = (ImageButton) findViewById(R.id.realplay_sound_btn);
        CheckTextButton fullscreen_button = (CheckTextButton) findViewById(R.id.fullscreen_button);
        relative = (RelativeLayout) findViewById(R.id.relative_monitor);
        back = (LinearLayout) findViewById(R.id.linear_back);
        LinearLayout linearBack = (LinearLayout) findViewById(R.id.linear_back2);
        statusBar = (LinearLayout) findViewById(R.id.linear_status_bar);

        realplay_play_btn.setOnClickListener(this);
        fullscreen_button.setOnClickListener(this);
        linearBack.setOnClickListener(this);

        mPlayer = (EZUIPlayer) findViewById(R.id.player);

        prePlay();
        flag = true;
        realplay_play_btn.setBackgroundResource(R.drawable.play_stop_selector);
        mOrientationDetector = new MyOrientationDetector(this);
        //


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.realplay_play_btn:
                if(flag){
                    mPlayer.stopPlay();
                    flag = false;
                    realplay_play_btn.setBackgroundResource(R.drawable.play_play_selector);
                }else{
                    prePlay();
                    flag = true;
                }
                //handler.sendEmptyMessage(0);

            break;

            case R.id.fullscreen_button:
                //toolbar.setVisibility(View.GONE);
                boolean heng = mOrientationDetector.isWideScrren();
                L.e("heng"+heng);
                if(!heng){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                    setSurfaceSize();
                    mList.setVisibility(View.GONE);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
                    smoothSwitchScreen();
                }

                //setSurfaceSize();
                break;
            case R.id.linear_back2:
                finish();
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayer.stopPlay();
        mPlayer.releasePlayer();
    }


    private void prePlay(){
        mPlayer.setPlayParams(url, new EZUIPlayer.EZUIPlayerCallBack() {
            @Override
            public void onPlaySuccess() {
                L.e("success");
                realplay_play_btn.setBackgroundResource(R.drawable.play_stop_selector);
                flag = true;
            }

            @Override
            public void onPlayFail(EZUIError ezuiError) {
                L.e("error=="+ezuiError.getErrorString());
                realplay_play_btn.setBackgroundResource(R.drawable.play_play_selector);
                flag = false;
            }

            @Override
            public void onVideoSizeChange(int i, int i1) {

            }
        });
        mPlayer.startPlay();
    }


    private void setSurfaceSize(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        boolean isWideScrren = mOrientationDetector.isWideScrren();
        //竖屏
        if (!isWideScrren) {
            //竖屏调整播放区域大小，宽全屏，高根据视频分辨率自适应
            mPlayer.setSurfaceSize(dm.widthPixels,0);
            statusBar.setVisibility(View.VISIBLE);
            //mList.setVisibility(View.GONE);
        } else {
            //横屏屏调整播放区域大小，宽、高均全屏，播放区域根据视频分辨率自适应
            mPlayer.setSurfaceSize(dm.widthPixels,dm.heightPixels);
            statusBar.setVisibility(View.GONE);

        }
    }



    public class MyOrientationDetector extends OrientationEventListener {

        private WindowManager mWindowManager;
        private int mLastOrientation = 0;

        public MyOrientationDetector(Context context) {
            super(context);
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        public boolean isWideScrren() {
            Display display = mWindowManager.getDefaultDisplay();
            Point pt = new Point();
            display.getSize(pt);
            return pt.x > pt.y;
        }
        @Override
        public void onOrientationChanged(int orientation) {
            int value = getCurentOrientationEx(orientation);
            if (value != mLastOrientation) {
                mLastOrientation = value;
                int current = getRequestedOrientation();
                if (current == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        || current == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }
        }

        private int getCurentOrientationEx(int orientation) {
            int value = 0;
            if (orientation >= 315 || orientation < 45) {
                // 0度
                value = 0;
                return value;
            }
            if (orientation >= 45 && orientation < 135) {
                // 90度
                value = 90;
                return value;
            }
            if (orientation >= 135 && orientation < 225) {
                // 180度
                value = 180;
                return value;
            }
            if (orientation >= 225 && orientation < 315) {
                // 270度
                value = 270;
                return value;
            }
            return value;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.releasePlayer();
    }

    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void smoothSwitchScreen() {
        /*// 5.0以上修复了此bug
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup rootView = ((ViewGroup) this.findViewById(android.R.id.content));
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            rootView.setPadding(0, statusBarHeight, 0, 0);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        WindowManager.LayoutParams params = getWindow().getAttributes();
                         params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                          getWindow().setAttributes(params);
                           getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
            //横屏
            L.e("横屏");
            setSurfaceSize();
            mList.setVisibility(View.GONE);
            setFullScreen();
        }else if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ){
            //竖屏
            smoothSwitchScreen();
            mList.setVisibility(View.VISIBLE);
            L.e("竖屏");

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:

                    if(System.currentTimeMillis()-firstTime>2000){
                        firstTime = System.currentTimeMillis();
                        //L.e("点击了1下");
                    }else{
                        int left = relative.getLeft();

                        L.e("x"+event.getX()+"----"+"y"+event.getY());

                    }

                    break;

            }
        }


        return super.onTouchEvent(event);

    }

    @Override
    public void onBackPressed() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为横屏
        setSurfaceSize();
        statusBar.setVisibility(View.VISIBLE);
        L.e("返回键");
    }

    private void addData(){
        SecurityData data = new SecurityData();
        SecurityData data2 = new SecurityData();
        SecurityData data3 = new SecurityData();
        data.setType(1);
        data2.setType(2);
        data3.setType(2);
        list.add(data);
        list.add(data2);
        list.add(data3);
        list.add(data3);
        list.add(data3);
        list.add(data3);
    }
}
