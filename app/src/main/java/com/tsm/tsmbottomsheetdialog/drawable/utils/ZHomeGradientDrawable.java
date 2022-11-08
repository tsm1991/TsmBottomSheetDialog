package com.tsm.tsmbottomsheetdialog.drawable.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import com.tsm.tsmbottomsheetdialog.App;
import androidx.annotation.ColorInt;

import static android.graphics.drawable.GradientDrawable.Orientation.BL_TR;
import static android.graphics.drawable.GradientDrawable.Orientation.BOTTOM_TOP;
import static android.graphics.drawable.GradientDrawable.Orientation.BR_TL;
import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;
import static android.graphics.drawable.GradientDrawable.Orientation.RIGHT_LEFT;
import static android.graphics.drawable.GradientDrawable.Orientation.TL_BR;
import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;
import static android.graphics.drawable.GradientDrawable.Orientation.TR_BL;


/**
 *  可以配合 AsyncLayoutInflater  加快速度
 *  或者使用x2c 的方式更好
 *  这种用法不仅可以不用写文件节省了包体积,而且还节省了xml 转 bean 的过程速度更快,
 *
 *
 * 类似drawable.xml 文件的shape 形式
 */
public class ZHomeGradientDrawable extends GradientDrawable {

    public static ZHomeGradientDrawable getInstance() {
        return new ZHomeGradientDrawable();
    }

    /**
     * 圆角  填充颜色
     * @param radius
     * @param color
     * @return
     */
    public  static ZHomeGradientDrawable getRadiusRectColorDrawable(int radius, int color){
        return  ZHomeGradientDrawable.getInstance().drawRadiusRectColor(radius,color);
    }

    /**
     * 圆角
     * 填充颜色
     * 外部实线
     * @param radius
     * @param contentColor
     * @param strokeColor
     * @param strokeWidth
     * @return
     */
    public  static ZHomeGradientDrawable getStrokeRadiusColorDrawable(int radius, int contentColor, int strokeColor, int strokeWidth){
        return  ZHomeGradientDrawable.getInstance().drawRadiusRectColor(radius,contentColor).drawStroke(strokeWidth,strokeColor);
    }


    /**
     * android:shape="rectangle"
     * 矩形
     *
     * @return
     */
    public ZHomeGradientDrawable drawRect() {
        this.setShape(GradientDrawable.RECTANGLE);
        return this;
    }

    /**
     * 圆角
     * <corners android:radius="3dp"/>
     *
     * @param radius
     * @return
     */
    public ZHomeGradientDrawable drawRadius(int radius) {
        this.setCornerRadius(dp2px(App.instance,radius));
        return this;
    }


    public int  dp2px(Context context,int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public ZHomeGradientDrawable drawRadiusDp(float radius) {
        this.setCornerRadius(radius);
        return this;
    }


    /**
     * 内部填充颜色
     * <solid android:color=""></solid>
     *
     * @param color
     * @return
     */
    public ZHomeGradientDrawable drawColor(@ColorInt int color) {
        setColor(color);
        return this;
    }


    public ZHomeGradientDrawable drawColors(int[] colors) {
        setColors(colors);
        return this;
    }


    public ZHomeGradientDrawable drawAngle(int orientation) {
        switch (orientation) {
            case 45:
                setOrientation(BL_TR);
                break;
            case 90:
                setOrientation(BOTTOM_TOP);
                break;
            case 135:
                setOrientation(BR_TL);
                break;
            case 180:
                setOrientation(RIGHT_LEFT);
                break;
            case 225:
                setOrientation(TR_BL);
                break;
            case 270:
                setOrientation(TOP_BOTTOM);
                break;
            case 315:
                setOrientation(TL_BR);
                break;
            default:
                setOrientation(LEFT_RIGHT);
                break;
        }

        return this;
    }


    public ZHomeGradientDrawable size(int width, int height){
        setSize(dp2px(App.instance,width),dp2px(App.instance,height));
        return this;
    }



    /**
     * 前2个是 top left
     * <p>
     * 3-4 是 top right
     * <p>
     * 5-6 bottom left
     * <p>
     * 7-8 bottom right
     * <p>
     * android:topLeftRadius=""
     * android:topRightRadius=""
     * android:bottomLeftRadius=""
     * android:bottomRightRadius=""
     *
     * @param radius
     * @return
     */
    public ZHomeGradientDrawable drawRadius(float[] radius) {
        for (int i = 0; i < radius.length; i++) {
            radius[i] = dp2px(App.instance,(int) radius[i]);
        }
        this.setCornerRadii(radius);
        return this;
    }

    public ZHomeGradientDrawable drawRadiusDp(float[] radius) {
        this.setCornerRadii(radius);
        return this;
    }


    /**
     * 圆角
     * 矩形
     * <corners android:radius="3dp"/>
     * android:shape="rectangle"
     *
     * @param radius
     * @return
     */
    public ZHomeGradientDrawable drawRadiusRect(int radius) {
        drawRect().drawRadius(dp2px(App.instance,radius));
        return this;
    }

    /**
     * 矩形
     * 圆角
     * 颜色
     * <solid android:color=""></solid>
     * <corners android:radius="3dp"/>
     * android:shape="rectangle"
     *
     * @param radius
     * @param color
     * @return
     */
    public ZHomeGradientDrawable drawRadiusRectColor(int radius, @ColorInt int color) {
        drawRect().drawRadius(radius).drawColor(color);
        return this;
    }

    /**
     * 绘制边界线
     * <p>
     * <stroke  android:width=""  android:color=""></stroke>
     *
     * @param width
     * @param color
     * @return
     */
    public ZHomeGradientDrawable drawStroke(int width, @ColorInt int color) {
        this.setStroke(width ,color,0,0);
        return this;
    }


    /**
     * 绘制边界线
     * <p>
     * <stroke  android:width=""  android:color=""></stroke>
     *
     * @param width
     * @param colorStateList
     * @return
     */
    public ZHomeGradientDrawable drawStroke(int width, ColorStateList colorStateList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStroke(dp2px(App.instance,width), colorStateList);
        }
        return this;
    }


    /**
     * 绘制边界线
     * <p>
     * <stroke  android:width=""  android:color=""  android:dashGap="" android:dashWidth="" ></stroke>
     *
     * @param width
     * @param color
     * @param dashWidth
     * @param dashGap
     * @return
     */
    public ZHomeGradientDrawable drawStroke(int width, @ColorInt int color, float dashWidth, float dashGap) {
        setStroke(width, color, dashWidth, dashGap);
        return this;
    }

    /**
     * 绘制边界线
     * <p>
     * <stroke  android:width=""  android:color=""  android:dashGap="" android:dashWidth="" ></stroke>
     *
     * @param context
     * @param width
     * @param colorStateList
     * @param dashWidth
     * @param dashGap
     * @return
     */
    public ZHomeGradientDrawable drawStroke(Context context, int width, ColorStateList colorStateList, float dashWidth, float dashGap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStroke(dp2px(App.instance,width), colorStateList, dashWidth, dashGap);
        }
        return this;
    }

    /**
     * 绘制圆形
     *
     * @return
     */
    public ZHomeGradientDrawable drawaOval() {
        this.setShape(GradientDrawable.OVAL);
        return this;
    }

    /**
     * 绘制图形
     *
     * @param shape
     * @return
     */

    public ZHomeGradientDrawable drawShape(int shape) {
        this.setShape(shape);
        return this;
    }
}
