package com.example.userversion.Util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.example.userversion.bean.Package;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XJP on 2017/12/3.
 */
public class Util {

   public static final String EMAIL="email";
    public static final String WIFIID="wifiId";
    public static final String WIFIPSW="wifiPsw";


    public static void showAlpha(final View view){
        view.setVisibility(View.VISIBLE);
       // S alpha = ObjectAnimator.ofFloat(view, "alpha", 1f,0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        alpha.setDuration(1000);//设置动画时间
        alpha.setRepeatCount(0);
        alpha.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        alpha.start();//启动动画。

        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.INVISIBLE);
                    }
                },800);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static String[] getPackages(List<Package> list,String input){
        if(list == null || list.size()==0){
            return null;
        }
        List<String> tempList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            String s="empty";
            switch (input){
                case EMAIL:
                    s=list.get(i).getEmail();
                    break;
                case WIFIID:
                    s=list.get(i).getWifiId();
                    break;
                case WIFIPSW:
                    s=list.get(i).getWifiPsw();
                    break;
                default:
                    return null;
            }
            if(!tempList.contains(s)){
                tempList.add(s);
            }
        }
        String[] array=new String[tempList.size()];
        for(int i=0;i<tempList.size();i++){
            array[i]=tempList.get(i);
        }
        return array;
    }
}
