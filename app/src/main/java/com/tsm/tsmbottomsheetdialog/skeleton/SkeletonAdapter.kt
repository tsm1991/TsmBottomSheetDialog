package com.tsm.tsmbottomsheetdialog.skeleton

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.annotation.IntRange
import com.tsm.tsmbottomsheetdialog.skeleton.ShimmerViewHolder
import com.tsm.tsmbottomsheetdialog.skeleton.ShimmerSkeletonLayout

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/24
 */
class SkeletonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mItemCount = 0
    private var mLayoutReference = 0
    private var mColor = 0
    private var mShimmerDuration = 0
    private var mShimmerAngle = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ShimmerViewHolder(inflater, parent, mLayoutReference)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val layout = holder.itemView as ShimmerSkeletonLayout
        layout.setShimmerAnimationDuration(mShimmerDuration)
        layout.setShimmerAngle(mShimmerAngle)
        layout.setShimmerColor(mColor)
        layout.startShimmerAnimation()
    }

    override fun getItemCount(): Int {
        return mItemCount
    }

    fun setLayoutReference(layoutReference: Int) {
        mLayoutReference = layoutReference
    }

    fun setItemCount(itemCount: Int) {
        mItemCount = itemCount
    }

    fun setShimmerColor(color: Int) {
        mColor = color
    }

    fun setShimmerDuration(shimmerDuration: Int) {
        mShimmerDuration = shimmerDuration
    }

    fun setShimmerAngle(@IntRange(from = 0, to = 30) shimmerAngle: Int) {
        mShimmerAngle = shimmerAngle
    }
}