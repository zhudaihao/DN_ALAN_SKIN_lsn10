package cn.wqgallery.skincore.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;

import cn.wqgallery.skincore.R;


public class SkinThemeUtils {
    /**
     * 主要针对引用系统？资源的处理
     * 如果有多个属性值，使用这个工具，只获取第一个属性值
     */
    public static int[] getResId(Context context, int[] attrs) {
        int[] ints = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            ints[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return ints;
    }


    /**
     * 换状态栏颜色
     */
    //v7包状态栏 颜色值（属性值）colorPrimaryDark
    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {android.support.v7.appcompat.R.attr.colorPrimaryDark};
    //v4包状态栏 颜色值（属性值）colorPrimaryDark
    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor,
            android.R.attr.navigationBarColor};


    public static void updateStatusBarColor(Activity activity) {
        //5.0及以上才可以修改字体栏颜色
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }


        int[] statusBarId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS);
        int[] resId = getResId(activity, STATUSBAR_COLOR_ATTRS);
        //判断数组是否有值
        if (statusBarId[0] != 0) {
            //设置状态栏颜色
            activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(statusBarId[0]));
        } else {
            //判断是否设置了颜色值(res >colors> statusBarColor设置了颜色值)
            if (resId[0] != 0) {
                //设置状态栏颜色
                activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(resId[0]));
            }
        }

        //底部导航栏颜色替换
        if (resId[1] != 0) {
            activity.getWindow().setNavigationBarColor(SkinResources.getInstance().getColor(resId[1]));
        }


    }


    /**
     * 换字体
     */
    private static int[] attrs = {R.attr.skinTypeface};
    public static Typeface getSkinTypeFace(Activity activity) {
        //获取字体值id
        int[] resId = getResId(activity, attrs);
        return SkinResources.getInstance().getTypeface(resId[0]);
    }
}
