package com.tsm.tsmbottomsheetdialog.skeleton

import androidx.recyclerview.widget.RecyclerView
import com.tsm.tsmbottomsheetdialog.R
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.annotation.LayoutRes

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/24
 */
class RecyclerViewSkeletonScreen  : SkeletonScreen {
    private val mRecyclerView: RecyclerView
    private val mRealAdapter: RecyclerView.Adapter<*>
    private val mSkeletonAdapter: SkeletonAdapter
    override fun replace() {
        mRecyclerView.adapter = mSkeletonAdapter
        if (!mRecyclerView.isComputingLayout) {
            mRecyclerView.isLayoutFrozen = true
        }
    }

    override fun reset() {
        mRecyclerView.adapter = mRealAdapter
    }


    private constructor(builder: Builder){
        mRecyclerView = builder.mRecyclerView
        mRealAdapter = builder.mRealAdapter
        mSkeletonAdapter = SkeletonAdapter()
        mSkeletonAdapter.itemCount = builder.mItemCount
        mSkeletonAdapter.setLayoutReference(builder.mItemResID)
        mSkeletonAdapter.setShimmerColor(builder.mShimmerColor)
        mSkeletonAdapter.setShimmerAngle(builder.mShimmerAngle)
        mSkeletonAdapter.setShimmerDuration(builder.mShimmerDuration)
    }




    class Builder {
        var mItemCount = 10
            private set
        var mItemResID = R.layout.default_skeleton_item
            private set
        var mShimmerColor: Int
            private set
        var mShimmerDuration = 1000
            private set
        var mShimmerAngle = 20
            private set
        var mRecyclerView: RecyclerView
            private set
        var mRealAdapter: RecyclerView.Adapter<*>
            private set
        constructor(
             mRecyclerView: RecyclerView,
            mRealAdapter: RecyclerView.Adapter<*>
        ){
            this.mRecyclerView=mRecyclerView
            this.mRealAdapter=mRealAdapter
            mShimmerColor = ContextCompat.getColor(mRecyclerView.context, R.color.shimmer_color)
        }


        fun count(itemCount: Int): Builder {
            mItemCount = itemCount
            return this
        }

        fun duration(shimmerDuration: Int): Builder {
            mShimmerDuration = shimmerDuration
            return this
        }

        fun color(@ColorRes shimmerColor: Int): Builder {
            mShimmerColor = ContextCompat.getColor(mRecyclerView.context, shimmerColor)
            return this
        }

        fun angle(@IntRange(from = 0, to = 30) shimmerAngle: Int): Builder {
            mShimmerAngle = shimmerAngle
            return this
        }

        fun replaceWith(@LayoutRes skeletonLayoutResID: Int): Builder {
            mItemResID = skeletonLayoutResID
            return this
        }

        fun replace(): RecyclerViewSkeletonScreen {
            val recyclerViewSkeleton = RecyclerViewSkeletonScreen(this)
            recyclerViewSkeleton.replace()
            return recyclerViewSkeleton
        }
    }


}