package com.example.veigar.intelligentbuilding.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.base.BaseActivity;
import com.example.veigar.intelligentbuilding.util.AppConst;
import com.example.veigar.intelligentbuilding.util.L;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity implements View.OnClickListener{
    private int[] mRes = {R.id.img_environment,R.id.img_safety,R.id.img_kaoqin,
            R.id.img_music,R.id.img_energy};
    private List<TextView> imgList = new ArrayList<>();
    private boolean flag = true;
    private ImageView imgA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        //AppConst.EQUIPMENT_ID = intent.getIntExtra("id",-1);
        L.e("Detail_id==="+ AppConst.EQUIPMENT_ID);
        initView();
    }

    private void initView() {
        initToolBar();
        //setPageTitle();
        imgA = (ImageView) findViewById(R.id.img_a);
        imgA.setOnClickListener(this);
        for(int i = 0 ; i < mRes.length ; i++){
            TextView tv = (TextView) findViewById(mRes[i]);
            tv.setOnClickListener(this);
            imgList.add(tv);

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.img_a:
                if(flag){
                    startAnimation();
                    flag = false;
                }else{
                    closeAnimation();
                    flag = true;
                }

                break;
            case R.id.img_environment:
                intent = new Intent(this,EnvironmentActivity.class);
                intent.putExtra(AppConst.MODULE_ID,AppConst.ENVIRONMENT_ID);
                intent.putExtra(AppConst.EQUIPMENT,AppConst.EQUIPMENT_ID);
                startActivity(intent);
                break;
            case R.id.img_energy:
                intent = new Intent(this,EnvironmentActivity.class);
                intent.putExtra(AppConst.MODULE_ID,AppConst.ENERGY_ID);//模块id
                intent.putExtra(AppConst.EQUIPMENT,AppConst.EQUIPMENT_ID);//设备id
                startActivity(intent);
                break;
        }

    }

    private void startAnimation(){
        ObjectAnimator obj = ObjectAnimator.ofFloat(imgA,"alpha",1.0f,0.5f);
        ObjectAnimator objEnvironment = ObjectAnimator.ofFloat(imgList.get(0),"translationX",1f,400f);
        ObjectAnimator objSafety = ObjectAnimator.ofFloat(imgList.get(1),"translationX",1f,-400f);
        ObjectAnimator objKaoqin = ObjectAnimator.ofFloat(imgList.get(2),"translationY",1f,-400f);
        ObjectAnimator objMusicX = ObjectAnimator.ofFloat(imgList.get(3),"translationX",1f,300f);
        ObjectAnimator objMusicY = ObjectAnimator.ofFloat(imgList.get(3),"translationY",1f,350f);
        ObjectAnimator objEnergyX = ObjectAnimator.ofFloat(imgList.get(4),"translationX",1f,-300f);
        ObjectAnimator objEnergyY = ObjectAnimator.ofFloat(imgList.get(4),"translationY",1f,350f);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new BounceInterpolator());

        set.playTogether(obj,objEnvironment,objSafety,objKaoqin,objMusicX,objMusicY,
                objEnergyX,objEnergyY);
        set.start();
    }

    private void closeAnimation(){
        ObjectAnimator obj = ObjectAnimator.ofFloat(imgA,"alpha",0.5f,15f);
        ObjectAnimator objEnviroment = ObjectAnimator.ofFloat(imgList.get(0),"translationX",400f,1f);
        ObjectAnimator objSafety = ObjectAnimator.ofFloat(imgList.get(1),"translationX",-400f,1f);
        ObjectAnimator objKaoqin = ObjectAnimator.ofFloat(imgList.get(2),"translationY",-400f,1f);
        ObjectAnimator objMusicX = ObjectAnimator.ofFloat(imgList.get(3),"translationX",300f,1f);
        ObjectAnimator objMusicY = ObjectAnimator.ofFloat(imgList.get(3),"translationY",350f,1f);
        ObjectAnimator objEnergyX = ObjectAnimator.ofFloat(imgList.get(4),"translationX",-300f,1f);
        ObjectAnimator objEnergyY = ObjectAnimator.ofFloat(imgList.get(4),"translationY",350f,1f);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new BounceInterpolator());

        set.playTogether(obj,objEnviroment,objSafety,objKaoqin,objMusicX,objMusicY,
                objEnergyX,objEnergyY);
        set.start();

    }
}
