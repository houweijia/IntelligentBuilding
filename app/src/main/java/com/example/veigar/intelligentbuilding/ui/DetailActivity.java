package com.example.veigar.intelligentbuilding.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.example.veigar.intelligentbuilding.R;
import com.example.veigar.intelligentbuilding.util.L;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    private int[] mRes = {R.id.img_a,R.id.img_enviroment,R.id.img_safety,R.id.img_kaoqin,
            R.id.img_music,R.id.img_energy};
    private List<ImageView> imgList = new ArrayList<>();
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",-1);
        L.e("Detail_id==="+id);
        initView();
    }

    private void initView() {
        for(int i = 0 ; i < mRes.length ; i++){
            ImageView image = (ImageView) findViewById(mRes[i]);
            image.setOnClickListener(this);
            imgList.add(image);

        }
    }

    @Override
    public void onClick(View v) {
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
            case R.id.img_enviroment:

                break;
        }

    }

    private void startAnimation(){
        ObjectAnimator obj = ObjectAnimator.ofFloat(imgList.get(0),"alpha",1.0f,0.5f);
        ObjectAnimator objEnviroment = ObjectAnimator.ofFloat(imgList.get(1),"translationX",1f,400f);
        ObjectAnimator objSafety = ObjectAnimator.ofFloat(imgList.get(2),"translationX",1f,-400f);
        ObjectAnimator objKaoqin = ObjectAnimator.ofFloat(imgList.get(3),"translationY",1f,-400f);
        ObjectAnimator objMusicX = ObjectAnimator.ofFloat(imgList.get(4),"translationX",1f,300f);
        ObjectAnimator objMusicY = ObjectAnimator.ofFloat(imgList.get(4),"translationY",1f,300f);
        ObjectAnimator objEnergyX = ObjectAnimator.ofFloat(imgList.get(5),"translationX",1f,-300f);
        ObjectAnimator objEnergyY = ObjectAnimator.ofFloat(imgList.get(5),"translationY",1f,300f);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new BounceInterpolator());

        set.playTogether(obj,objEnviroment,objSafety,objKaoqin,objMusicX,objMusicY,
                objEnergyX,objEnergyY);
        set.start();
    }

    private void closeAnimation(){
        ObjectAnimator obj = ObjectAnimator.ofFloat(imgList.get(0),"alpha",0.5f,15f);
        ObjectAnimator objEnviroment = ObjectAnimator.ofFloat(imgList.get(1),"translationX",400f,1f);
        ObjectAnimator objSafety = ObjectAnimator.ofFloat(imgList.get(2),"translationX",-400f,1f);
        ObjectAnimator objKaoqin = ObjectAnimator.ofFloat(imgList.get(3),"translationY",-400f,1f);
        ObjectAnimator objMusicX = ObjectAnimator.ofFloat(imgList.get(4),"translationX",300f,1f);
        ObjectAnimator objMusicY = ObjectAnimator.ofFloat(imgList.get(4),"translationY",300f,1f);
        ObjectAnimator objEnergyX = ObjectAnimator.ofFloat(imgList.get(5),"translationX",-300f,1f);
        ObjectAnimator objEnergyY = ObjectAnimator.ofFloat(imgList.get(5),"translationY",300f,1f);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new BounceInterpolator());

        set.playTogether(obj,objEnviroment,objSafety,objKaoqin,objMusicX,objMusicY,
                objEnergyX,objEnergyY);
        set.start();

    }
}
