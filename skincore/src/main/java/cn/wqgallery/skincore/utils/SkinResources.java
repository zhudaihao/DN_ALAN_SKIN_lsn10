package cn.wqgallery.skincore.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * resources资源管理类
 */
public class SkinResources {
    //单利
    private static SkinResources instance;

    //皮肤包资源
    private Resources mSkinResources;
    private String mSkinPkgName;
    private boolean isDefaultSkin = true;//是否 使用默认皮肤包

    //系统资源
    private Resources mAppResources;

    private SkinResources(Context context) {
        //获取系统资源对象
        mAppResources = context.getResources();
    }

    //带参数 的单利
    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null) {
                    instance = new SkinResources(context);
                }
            }
        }
    }

    //获取实例
    public static SkinResources getInstance() {
        return instance;
    }


    //还原数据为 默认皮肤
    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    //初始化 皮肤包对象
    public void applySkin(Resources skinResources, String pkgName) {
        mSkinResources = skinResources;
        mSkinPkgName = pkgName;
        //是否使用默认皮肤包(pkgName为空 或者 resources对象为空就使用默认皮肤)
        isDefaultSkin = TextUtils.isEmpty(pkgName) || skinResources == null;

    }

    /**
     * 获取 资源ID（系统的/皮肤包的）
     */
    public int getIdentifier(int resId) {
        //是否使用默认皮肤包
        if (isDefaultSkin) {
            return resId;
        }

        //通过系统资源对象，获取资源ID的名称(ic_launcher)，类型（drawable），
        String resName = mAppResources.getResourceEntryName(resId);//资源ID的名称ic_launcher
        String resType = mAppResources.getResourceTypeName(resId);

        //获取皮肤包的资源ID
        int skinId = mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }


    /**
     * 颜色值
     */
    //获取设置view的 颜色值
    public int getColor(int resId) {
        //默认 颜色值
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        //获取 皮肤包资源ID
        int skinId = getIdentifier(resId);

        //皮肤包找不到  设置默认的
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }

        //返回 皮肤包颜色值
        return mSkinResources.getColor(skinId);
    }




    // colorStateList 其实就颜色集合 比如res > color >button_text.xml文件里面就是个colorStateList
    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }

        //获取 皮肤包资源ID
        int skinId = getIdentifier(resId);

        //皮肤包找不到  设置默认的
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        //返回 皮肤包集合颜色值
        return mSkinResources.getColorStateList(skinId);
    }


    /**
     * 图片对象
     */
    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }

        //获取 皮肤包资源ID
        int skinId = getIdentifier(resId);

        //皮肤包找不到  设置默认的
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        //返回 皮肤包图片对象
        return mSkinResources.getDrawable(skinId);

    }

    public Object getBackground(int resId) {
        //根据id获取资源对应名称，区分是颜色还是图片
        String resourceTypeName = mAppResources.getResourceTypeName(resId);
        if (resourceTypeName.equals("color")) {
            //资源id是颜色值
            return getColor(resId);
        } else {
            //资源id是图片
            return getDrawable(resId);
        }

    }


    /**
     * 文字
     */
    public String getString(int resId) {
        try {
            if (isDefaultSkin) {
                return mAppResources.getString(resId);
            }

            //获取 皮肤包资源ID
            int skinId = getIdentifier(resId);

            //皮肤包找不到  设置默认的
            if (skinId == 0) {
                return mAppResources.getString(resId);
            }
            //返回 皮肤包图片对象
            return mSkinResources.getString(skinId);

        } catch (Resources.NotFoundException e) {

        }

        return null;
    }

    /**
     * 字体
     */
    public Typeface getTypeface(int resId) {
        String skinTypefacePath = getString(resId);
        //系统默认
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }

        try {
            Typeface typeface;
            if (isDefaultSkin) {
                //设置APP 默认设置的
                typeface = Typeface.createFromAsset(mAppResources.getAssets(), skinTypefacePath);
                return typeface;
            }

            //皮肤包的 字体
            typeface = Typeface.createFromAsset(mSkinResources.getAssets(), skinTypefacePath);
            return typeface;

        } catch (RuntimeException e) {

        }

        return Typeface.DEFAULT;
    }


}
