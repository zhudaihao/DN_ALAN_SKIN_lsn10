package com.dn_alan.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dn_alan.myapplication.R;

import cn.wqgallery.skincore.utils.SkinResources;
import cn.wqgallery.skincore.utils.SkinViewSupport;

//import com.dn_alan.skin_core1.utils.SkinResources;

//import com.dn_alan.skin_core1.SkinViewSupport;

/**
 * @author Lance
 * @date 2018/3/12
 * <p>
 * 修改自定义属性 通过定义个接口来作为标记获取自定义对象，
 */

public class CircleView extends View implements SkinViewSupport {

    private AttributeSet attrs;
    //画笔
    private Paint mTextPain;
    //半径
    private int radius;


    private int corcleColorResId;

    public CircleView(Context context) {
        this(context, null, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        corcleColorResId = typedArray.getResourceId(R.styleable.CircleView_corcleColor, 0);

        typedArray.recycle();
        mTextPain = new Paint();
        mTextPain.setColor(getResources().getColor(corcleColorResId));
        //开启抗锯齿，平滑文字和圆弧的边缘
        mTextPain.setAntiAlias(true);
        //设置文本位于相对于原点的中间
        mTextPain.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取宽度一半
        int width = getWidth() / 2;
        //获取高度一半
        int height = getHeight() / 2;
        //设置半径为宽或者高的最小值
        radius = Math.min(width, height);
        //利用canvas画一个圆
        canvas.drawCircle(width, height, radius, mTextPain);

    }

    public void setCorcleColor(@ColorInt int color) {
        mTextPain.setColor(color);
        invalidate();
    }


    /**
     * 自定义需要修改的属性在这里操作
     */
    @Override
    public void applySkin() {
        if (corcleColorResId != 0) {
            //获取资源
            int color = SkinResources.getInstance().getColor(corcleColorResId);
            setCorcleColor(color);
        }
    }


//    @Override
//    public void applySkin() {
//        if (corcleColorResId != 0) {
//            int color = SkinResources.getInstance().getColor(corcleColorResId);
//            setCorcleColor(color);
//        }
//    }
}
