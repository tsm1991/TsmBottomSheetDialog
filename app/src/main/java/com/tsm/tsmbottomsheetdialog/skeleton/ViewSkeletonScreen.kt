package com.tsm.tsmbottomsheetdialog.skeleton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tsm.tsmbottomsheetdialog.R

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/24
 */
class ViewSkeletonScreen  : SkeletonScreen {
    private val mViewReplacer: ViewReplacer
    private val mActualView: View
    private val mSkeletonResID: Int
    private val mShimmerColor: Int
    private val mShimmerDuration: Int
    private val mShimmerAngle: Int

    private constructor(builder: Builder){
        mActualView = builder.mView
        mSkeletonResID = builder.mSkeletonLayoutResID
        mShimmerDuration = builder.mShimmerDuration
        mShimmerAngle = builder.mShimmerAngle
        mShimmerColor = builder.mShimmerColor
        mViewReplacer = ViewReplacer(builder.mView)
    }




    private fun getShimmerSkeletonLayout(parentView: ViewGroup): ShimmerSkeletonLayout {
        val shimmerLayout = LayoutInflater.from(mActualView.context)
            .inflate(R.layout.layout_shimmer, parentView, false) as ShimmerSkeletonLayout
        shimmerLayout.setShimmerColor(mShimmerColor)
        shimmerLayout.setShimmerAngle(mShimmerAngle)
        shimmerLayout.setShimmerAnimationDuration(mShimmerDuration)
        val innerView =
            LayoutInflater.from(mActualView.context).inflate(mSkeletonResID, shimmerLayout, false)
        val lp = innerView.layoutParams
        if (lp != null) {
            shimmerLayout.layoutParams = lp
        }
        shimmerLayout.addView(innerView)
        shimmerLayout.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                shimmerLayout.startShimmerAnimation()
            }

            override fun onViewDetachedFromWindow(v: View) {
                shimmerLayout.stopShimmerAnimation()
            }
        })
        shimmerLayout.startShimmerAnimation()
        return shimmerLayout
    }

    private fun generateSkeletonLoadingView(): View? {
        val viewParent = mActualView.parent ?: return null
        val parentView = viewParent as ViewGroup
        return getShimmerSkeletonLayout(parentView)
    }

    override fun replace() {
        val skeletonLoadingView = generateSkeletonLoadingView()
        if (skeletonLoadingView != null) {
            mViewReplacer.replace(skeletonLoadingView)
        }
    }

    override fun reset() {
        if (mViewReplacer.targetView is ShimmerSkeletonLayout) {
            (mViewReplacer.targetView as ShimmerSkeletonLayout?)!!.stopShimmerAnimation()
        }
        mViewReplacer.restore()
    }

    class Builder {
        var mSkeletonLayoutResID = 0
            private set
        var mShimmerColor: Int
            private set
        var mShimmerDuration = 1000
            private set
        var mShimmerAngle = 20
            private set
        var mView: View
            private set

        constructor(mView: View){
            this.mView=mView
            mShimmerColor = ContextCompat.getColor(mView.context, R.color.shimmer_color)
        }


        fun replaceWith(@LayoutRes skeletonLayoutResID: Int): Builder {
            mSkeletonLayoutResID = skeletonLayoutResID
            return this
        }

        fun color(@ColorRes shimmerColor: Int): Builder {
            mShimmerColor = ContextCompat.getColor(mView.context, shimmerColor)
            return this
        }

        fun duration(shimmerDuration: Int): Builder {
            mShimmerDuration = shimmerDuration
            return this
        }

        fun angle(@IntRange(from = 0, to = 30) shimmerAngle: Int): Builder {
            mShimmerAngle = shimmerAngle
            return this
        }

        fun replace(): ViewSkeletonScreen {
            val skeletonScreen = ViewSkeletonScreen(this)
            skeletonScreen.replace()
            return skeletonScreen
        }
    }

}