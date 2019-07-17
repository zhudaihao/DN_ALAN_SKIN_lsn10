package cn.wqgallery.skincore;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import cn.wqgallery.skincore.utils.SkinThemeUtils;

/**
 * 自定义factory
 * 换肤的第一步，获取XML里面的view（系统view和自定义view），筛选view需要处理的属性
 */
public class SkinFactory implements LayoutInflater.Factory2, Observer {
    //系统原生view 放的所有文件夹有三种可能
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit.",
    };

    //构造方法参数(指定我们需要的构造方法参数)  Constructor
    private static final Class[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};


    //保存view到缓存，避免，重复反射获取同样的view对象
    private static final HashMap<String, Constructor<? extends View>> map = new HashMap<>();

    //属性处理类
    private SkinAttribute skinAttribute;
    private Activity activity;

    public SkinFactory(Activity activity, Typeface skinTypeFace) {
        this.activity = activity;
        skinAttribute = new SkinAttribute(skinTypeFace);
    }

    /**
     * 采集需要换肤的view信息
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //创建view 最总是通过反射创建view
        View view = createViewFromTag(name, context, attrs);

        //自定义view的name就是全类名，所以不需要遍历集合
        if (view == null) {
            view = createView(name, context, attrs);
        }

        //筛选view需要处理的属性
        skinAttribute.load(view, attrs);
        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        //判断是否是自定义view区分处理
        if (-1 != name.indexOf(".")) {
            return null;
        }

        View view = null;
        //不是自定义view，即系统原生的view ,系统view可能存在三个文件夹里面所有需要遍历，
        for (int i = 0; i < mClassPrefixList.length; i++) {
            //mClassPrefixList[i] + name就是view的全类名，
            view = createView(mClassPrefixList[i] + name, context, attrs);
            if (view != null) {
                break;
            }
        }

        return view;
    }


    //attrs就是构造方法参数集合类似，下面createView（）方法括号里面的string，context，AttributeSet，具体信息的集合
    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        //先从缓存获取，缓存没有机创建保存到缓存
        Constructor<? extends View> mConstructor = map.get(name);
        if (mConstructor == null) {
            try {
                //反射创建view
                //1创建class对象
                Class<?> aClass = context.getClassLoader().loadClass(name);
                //转换对应view类型
                Class<? extends View> aClass1 = aClass.asSubclass(View.class);
                //通过反射 指定构造方法参数 返回指定类型构造函数
                mConstructor = aClass1.getConstructor(mConstructorSignature);
                //保存到缓存
                map.put(name, mConstructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mConstructor != null) {
            //不为空 ,反射获取
            try {
                //attrs保存view的宽，高等等信息的具体值的集合
                view = mConstructor.newInstance(context, attrs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return view;
    }


    /**
     *
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }


    /**
     * 观察者 回调方法
     */
    @Override
    public void update(Observable o, Object arg) {
        //执行这方法 说明资源对象加载完毕 可以执行设置资源

        //替换状态栏颜色
        SkinThemeUtils.updateStatusBarColor(activity);

        //替换字体
        Typeface skinTypeFace = SkinThemeUtils.getSkinTypeFace(activity);
        skinAttribute.setSkinTypeFace(skinTypeFace);

        //更换皮肤
        skinAttribute.applySkin();

    }


}
