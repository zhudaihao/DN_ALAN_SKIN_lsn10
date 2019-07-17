package com.dn_alan.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.dn_alan.myapplication.night.NightModeConfig;
import com.dn_alan.myapplication.skin.Skin;
import com.dn_alan.myapplication.skin.SkinUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.wqgallery.skincore.SkinManager;

//import com.dn_alan.skin_core1.SkinManager;

//import cn.wqgallery.skincore.SkinManager;


public class SkinActivity extends AppCompatActivity {


    /**
     * 从服务器拉取的皮肤表
     */
    List<Skin> skins = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        //内置换肤 把皮肤包放到 assets文件夹
        skins.add(new Skin("e0893ca73a972d82bcfc3a5a7a83666d", "1111111.skin", "app-skin-debug" + ".apk"));

    }


    /**
     * 下载皮肤包 换肤
     */

    public void changeDownload(View view) {
        //下载换肤 (记得要把皮肤包 下载到SD卡里面或者测试放到SD卡里面)
        String path = Environment.getExternalStorageDirectory() + File.separator + "app_skin-debug.skin";
        SkinManager.getInstance().loadSkin(path);
    }

    /**
     * 内置皮肤包 换肤
     * 内置皮肤包 放在 assets文件夹下面
     */
    public void change(View view) {
        //内置换肤 使用第0个皮肤
        Skin skin = skins.get(0);
        Log.e("zdh","---------->>>"+skin.path);
        selectSkin(skin);
        //换肤
        Log.e("zdh","----------"+skin.path);
        SkinManager.getInstance().loadSkin(skin.path);
    }

    /**
     * 内置换肤
     * 下载皮肤包
     */
    private void selectSkin(Skin skin) {
//        getCacheDir()方法用于获取/data/data//cache目录
//        getFilesDir()方法用于获取/data/data//files目录

        File theme = new File(getFilesDir(), "theme");
        if (theme.exists() && theme.isFile()) {
            theme.delete();
        }
        theme.mkdirs();
        File skinFile = skin.getSkinFile(theme);
        if (skinFile.exists()) {
            Log.e("SkinActivity", "皮肤已存在,开始换肤");
            return;
        }
        Log.e("SkinActivity", "皮肤不存在,开始下载");
        FileOutputStream fos = null;
        InputStream is = null;
        //临时文件
        File tempSkin = new File(skinFile.getParentFile(), skin.name + ".temp");
        try {
            fos = new FileOutputStream(tempSkin);
            //假设下载皮肤包
            is = getAssets().open(skin.url);
            byte[] bytes = new byte[10240];
            int len;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
            //下载成功，将皮肤包信息insert已下载数据库
            Log.e("SkinActivity", "皮肤包下载完成开始校验");
            //皮肤包的md5校验 防止下载文件损坏(但是会减慢速度。从数据库查询已下载皮肤表数据库中保留md5字段)
            if (TextUtils.equals(SkinUtils.getSkinMD5(tempSkin), skin.md5)) {
                Log.e("SkinActivity", "校验成功,修改文件名。");
                tempSkin.renameTo(skinFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tempSkin.delete();
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 还原皮肤
     */

    public void restore(View view) {
        SkinManager.getInstance().loadSkin(null);
    }

    /**
     * 日间皮肤
     * 注意activity继承appcompatactivity
     */
    public void day(View view) {
        //获取当前的模式，设置相反的模式，这里只使用了，夜间和非夜间模式
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentMode == Configuration.UI_MODE_NIGHT_YES) {
            //设置日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //保存当前模式
            NightModeConfig.getInstance().setNightMode(getApplicationContext(), false);
            recreate();//需要recreate才能生效
        }


    }

    /**
     * 夜间皮肤
     * 注意activity继承AppCompatActivity
     */
    public void night(View view) {
        //获取当前的模式，设置相反的模式，这里只使用了，夜间和非夜间模式
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentMode == Configuration.UI_MODE_NIGHT_NO) {
            //保存夜间模式状态,Application中可以根据这个值判断是否设置夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //ThemeConfig主题配置，这里只是保存了是否是夜间模式的boolean值
            NightModeConfig.getInstance().setNightMode(getApplicationContext(), true);
            recreate();//需要recreate才能生效
        }


    }

}
