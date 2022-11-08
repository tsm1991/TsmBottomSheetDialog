package com.tsm.tsmbottomsheetdialog.drawable.utils;

import android.graphics.drawable.Drawable
import android.util.LruCache
import java.lang.ref.WeakReference

/**
 * Create by 田守明
 * on 2021/5/13
 */
class ZHomeDrawableCacheHelper {

    /**
     * 弱引用缓存
     */
    private var linkedHashMap: LruCache<ZHomeDrawableModel, WeakReference<Drawable.ConstantState>>? = null


    private constructor() {
        /**
         * 最大100个
         */
        linkedHashMap = LruCache(100)
    }


    companion object {
        @Volatile
        private var helper: ZHomeDrawableCacheHelper? = null
        fun getInstance(): ZHomeDrawableCacheHelper {
            if (helper == null) {
                synchronized(ZHomeDrawableCacheHelper::class.java) {
                    if (helper == null) {
                        helper = ZHomeDrawableCacheHelper()
                    }
                }
            }
            return helper!!
        }
    }

    /**
     * 获取
     */
    fun getDrawable(model:ZHomeDrawableModel):Drawable?{
        return linkedHashMap?.get(model)?.get()?.newDrawable()
    }

    fun cacheDrawable(model:ZHomeDrawableModel?, draw : Drawable?){
        model?.let {
            draw?.let {
                linkedHashMap?.put(model,WeakReference<Drawable.ConstantState>(draw.constantState))
            }
        }
    }



}