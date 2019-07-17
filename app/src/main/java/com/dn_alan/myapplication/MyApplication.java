package com.dn_alan.myapplication;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.dn_alan.myapplication.night.NightModeConfig;

import cn.wqgallery.skincore.SkinManager;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);

        //设置上次换的是夜间模式 还是日间模式
        boolean nightMode = NightModeConfig.getInstance().getNightMode(getApplicationContext());
        if (nightMode) {
            //夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            //日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
