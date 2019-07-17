package com.dn_alan.myapplication.night;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 夜间/日间模式 切换
 */
public class NightModeConfig {
    private SharedPreferences mSharedPreference;
    private static final String NIGHT_MODE = "Night_Mode";
    public static final String IS_NIGHT_MODE = "Is_Night_Mode";

    //是否是夜间模式
    private boolean mIsNightMode;

    private  SharedPreferences.Editor mEditor;

    private static NightModeConfig sModeConfig;

    public static NightModeConfig getInstance(){

        return sModeConfig !=null?sModeConfig : new NightModeConfig();
    }

    public boolean getNightMode(Context context){

        if (mSharedPreference == null) {
            mSharedPreference = context.getSharedPreferences(NIGHT_MODE,context.MODE_PRIVATE);
        }
        mIsNightMode = mSharedPreference.getBoolean(IS_NIGHT_MODE, false);
        return mIsNightMode;
    }

    public void setNightMode(Context context, boolean isNightMode){
        if (mSharedPreference == null) {
            mSharedPreference = context.getSharedPreferences(NIGHT_MODE,context.MODE_PRIVATE);
        }
        mEditor = mSharedPreference.edit();

        mEditor.putBoolean(IS_NIGHT_MODE,isNightMode);
        mEditor.commit();
    }
}
