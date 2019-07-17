package cn.wqgallery.skincore;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Observable;

import cn.wqgallery.skincore.utils.SkinPreference;
import cn.wqgallery.skincore.utils.SkinResources;

public class SkinManager extends Observable {
    private static SkinManager instance;
    private Application application;

    //初始化
    public static void init(Application application) {
        synchronized (SkinManager.class) {
            if (instance == null) {
                instance = new SkinManager(application);
            }
        }

    }

    //获取单利对象
    public static SkinManager getInstance() {
        return instance;
    }



    private SkinManager(Application application) {
        this.application = application;
        //初始化 记录当前使用的皮肤 工具类
        SkinPreference.init(application);
        //初始化 皮肤资源类 （返回view需要加载的资源）
        SkinResources.init(application);

        //注册所有activity回调监听接口
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycle());

        //加载资源皮肤
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    /**
     * 换肤
     */
    public void loadSkin(String path) {
        if (TextUtils.isEmpty(path)) {
            //清空存储数据
            SkinPreference.getInstance().setSkin("");
            //恢复默认皮肤
            SkinResources.getInstance().reset();
        } else {
            try {
                //反射获取assetManage对象
                AssetManager assetManager = AssetManager.class.newInstance();
                //反射获取assetmanage里面的方法addassetpath
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                //反射 给方法赋值
                addAssetPath.invoke(assetManager, path);

                //获取 app资源对象
                Resources appResources = application.getResources();
                //创建 皮肤包 资源对象
                Resources skinResources = new Resources(
                        assetManager,
                        appResources.getDisplayMetrics(),
                        appResources.getConfiguration());
                //记录 皮肤包
                SkinPreference.getInstance().setSkin(path);

                //获取 皮肤包的 包名
                PackageManager packageManager = application.getPackageManager();
                //获取包信息
                PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                //获取包名
                String packageName = packageArchiveInfo.packageName;

                //应用皮肤包
                SkinResources.getInstance().applySkin(skinResources,packageName);
            } catch (Exception e) {

            }

        }

        //代码执行到这里说明 view加载资源对象 已获取，通知工厂对需要处理的view进行资源加载

        //这里使用观察者模式，skinManager 继承observable 为被观察者

        //更新
        setChanged();
        //通知观察者
        notifyObservers();



    }


}

