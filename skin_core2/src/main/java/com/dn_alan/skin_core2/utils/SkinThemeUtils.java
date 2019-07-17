package com.dn_alan.skin_core2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

public class SkinThemeUtils {

    public static int[] getResId(Context context, int[] attrs){
        int[] ints = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            ints[i] =  typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return ints;
    }




    //v7包状态栏 颜色值（属性值）colorPrimaryDark
    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark
    };
    //v4包状态栏 颜色值（属性值）colorPrimaryDark
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor,
                                                  android.R.attr.navigationBarColor};


    
    //替换状态栏
    public static void updataStatusBarColor(Activity activity){
        //5.0 以上才能修改
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        //获取statusBarColor与navigationBarColor  颜色值
        int[] statusBarId = getResId(activity, STATUSBAR_COLOR_ATTRS);

        //如果statusBarColor 配置颜色值， 就换肤
        if(statusBarId[0] != 0){
                activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(statusBarId[0]));
        } else {
            //获取colorPrimaryDark
            int resId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if(resId != 0){
                activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(resId));
            }
        }

        if(statusBarId[1] != 0){
            activity.getWindow().setNavigationBarColor(SkinResources.getInstance().getColor(statusBarId[1]));
        }

    }
}
