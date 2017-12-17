package com.example.userversion.Util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
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

   /* public static String[] getPackages(List<Package> list,String input){
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
    }*/

  public static void pgTopg_email(Package pg,Package mPg){
      pg.setEmail1(mPg.getEmail1());
      pg.setEmail2(mPg.getEmail2());
      pg.setEmail3(mPg.getEmail3());
      pg.setEmail4(mPg.getEmail4());
      pg.setEmail5(mPg.getEmail5());
      pg.setEmail6(mPg.getEmail6());
      pg.setEmail7(mPg.getEmail7());
      pg.setEmail8(mPg.getEmail8());
      pg.setEmail9(mPg.getEmail9());
      pg.setEmail10(mPg.getEmail10());
  }

    public static int getPgEmailSize(Package pg){
        int size=0;
        if(!TextUtils.isEmpty(pg.getEmail1()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail2()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail3()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail4()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail5()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail6()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail7()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail8()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail9()))
            size+=1;
        if(!TextUtils.isEmpty(pg.getEmail10()))
            size+=1;
        return size;
    }

    public static List<String> getPgEmailList(Package pg){
        List<String> list=new ArrayList<>();
        if(!TextUtils.isEmpty(pg.getEmail1()))
            list.add(pg.getEmail1());
        if(!TextUtils.isEmpty(pg.getEmail2()))
            list.add(pg.getEmail2());
        if(!TextUtils.isEmpty(pg.getEmail3()))
            list.add(pg.getEmail3());
        if(!TextUtils.isEmpty(pg.getEmail4()))
            list.add(pg.getEmail4());
        if(!TextUtils.isEmpty(pg.getEmail5()))
            list.add(pg.getEmail5());
        if(!TextUtils.isEmpty(pg.getEmail6()))
            list.add(pg.getEmail6());
        if(!TextUtils.isEmpty(pg.getEmail7()))
            list.add(pg.getEmail7());
        if(!TextUtils.isEmpty(pg.getEmail8()))
            list.add(pg.getEmail8());
        if(!TextUtils.isEmpty(pg.getEmail9()))
            list.add(pg.getEmail9());
        if(!TextUtils.isEmpty(pg.getEmail10()))
            list.add(pg.getEmail10());
        return list;
    }
}
