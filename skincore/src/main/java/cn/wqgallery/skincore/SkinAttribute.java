package cn.wqgallery.skincore;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.wqgallery.skincore.utils.SkinResources;
import cn.wqgallery.skincore.utils.SkinThemeUtils;
import cn.wqgallery.skincore.utils.SkinViewSupport;

//属性处理类，字段是需要修改的属性
public class SkinAttribute {
    private static final List mAttributes = new ArrayList();

    private Typeface typeface;


    //定义需要换肤的属性
    static {
        mAttributes.add("background");

        mAttributes.add("src");

        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");

        //自定义 属性 标记需要单独设置字体的
        mAttributes.add("skinTypeface");
    }

    //缓存 保存SkinView对象
    private List<SkinView> skinViews = new ArrayList<>();

    /**
     * 筛选需要处理的属性
     */
    public void load(View view, AttributeSet attrs) {
        //保存属性对象
        List<SkinPain> skinPains = new ArrayList<>();

        //attrs.getAttributeCount():返回集合中可用属性的数量
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获取属性名字（例如 background）
            String attributeName = attrs.getAttributeName(i);
            //判断属性名是否是我们需要换肤的属性名
            if (mAttributes.contains(attributeName)) {
                //获取属性对应的值，（比如属性src的值就是@mipmap/ic_launcher）
                String attributeValue = attrs.getAttributeValue(i);
                /**
                 * 处理不同值
                 */
                //#开头的 例如#ffffff
                if (attributeValue.startsWith("#")) {
                    //写死的不处理 跳过这次循环
                    continue;
                }
                //?开头的 例如?attr/searchIcon

                int resId;
                if (attributeValue.startsWith("?")) {
                    //substring(1)截取掉第一个，即去掉？
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    //有些属性值可能有多个，（?attr/searchIcon|?attr/searchViewStyle）
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];

                } else {
                    //@开头
                    resId = Integer.parseInt(attributeValue.substring(1));
                }

                if (resId != 0) {
                    SkinPain skinPain = new SkinPain(attributeName, resId);
                    skinPains.add(skinPain);
                }

            }

        }

        //循环处理完后  (没有上面指定属性的textView也要放进去修改字体)
        if (!skinPains.isEmpty() || view instanceof TextView||view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(view, skinPains);
            //修改皮肤
            skinView.applySkin(typeface);
            //保存到缓存
            skinViews.add(skinView);
        }


    }

    public SkinAttribute(Typeface typeface) {
        this.typeface = typeface;
    }

    /**
     * 字体替换
     */

    public void setSkinTypeFace(Typeface skinTypeFace) {
        this.typeface = skinTypeFace;
    }


    /**
     * 保存属性对象到集合，对不同属性不同处理
     * 核心逻辑
     */
    class SkinView {
        View view;
        List<SkinPain> skinPains;

        public SkinView(View view, List<SkinPain> skinPains) {
            this.view = view;
            this.skinPains = skinPains;
        }

        //遍历属性集合 对不同属性对象做不同处理
        public void applySkin(Typeface typeface) {
            /**
             * 替换字体
             * 注意放前面，要不单独换字体的不生效了
             */
            applyTypeface(typeface);

            /**
             * 替换自定义view颜色
             */
            applySkinViewSupper(view);


            for (SkinPain skinPair : skinPains) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
                        //获取view加载的资源
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        //Color
                        if (background instanceof Integer) {
                            //背景颜色设置
                            view.setBackgroundColor((Integer) background);
                        } else {
                            //背景图片是设置
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair
                                .resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                    background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList
                                (skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;

                    //单个替换字体
                    case "skinTypeface":
                        applyTypeface(SkinResources.getInstance().getTypeface(skinPair.resId));
                        break;
                    default:
                        break;


                }

                if (null != left || null != right || null != top || null != bottom) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        ((TextView) view).setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
                    }
                }


            }

        }

        /**
         * 替换字体
         */
        public void applyTypeface(Typeface typeface) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        }

    }

    /**
     * 自定义view 替换背景颜色
     */
    private void applySkinViewSupper(View view) {
        if (view instanceof SkinViewSupport){
            // 调用接口方法，自定义view就会实现换肤操作
            ((SkinViewSupport)view).applySkin();
        }
    }


    //属性对象
    static class SkinPain {
        //属性全类名
        String attributeName;
        //属性Id
        int resId;

        public SkinPain(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }


    /**
     * 更换皮肤
     */
    public void applySkin() {
        for (SkinView skinView : skinViews) {
            skinView.applySkin(typeface);
        }
    }


}
