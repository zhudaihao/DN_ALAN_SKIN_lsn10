package cn.wqgallery.skincore;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.lang.reflect.Field;
import java.util.HashMap;

import cn.wqgallery.skincore.utils.SkinThemeUtils;

//所有activity生命周期回调
public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    HashMap<Activity, SkinFactory> hashMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /**
         * 状态栏
         */
        SkinThemeUtils.updateStatusBarColor(activity);
        /**
         * 字体
         */
        Typeface skinTypeFace = SkinThemeUtils.getSkinTypeFace(activity);//返回字体对象


        /**
         * 皮肤
         */
        //根据对应activity获取layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        //反射修改mFactorySet为false可以设置工厂

        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);//设置权限
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //设factory2为自定义的工厂
        SkinFactory skinFactory = new SkinFactory(activity,skinTypeFace);
        layoutInflater.setFactory2(skinFactory);



        //注册观察者模式
        SkinManager.getInstance().addObserver(skinFactory);
        //保存观察者和被观察者
        hashMap.put(activity, skinFactory);

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //移除观察者
        SkinFactory remove = hashMap.remove(activity);
        //移除被观察者
        SkinManager.getInstance().deleteObserver(remove);

    }
}
