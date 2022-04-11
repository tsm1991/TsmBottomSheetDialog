package com.tsm.tsmbottomsheetdialog.skeleton

import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View

/**
 * Create by 田守明 tiansm1@ziroom.com
 * on 2022/2/24
 */
internal class ViewReplacer(val sourceView: View) {
    var targetView: View? = null
        private set
    private var mTargetViewResID = -1
    private var currentView: View?
    private var mSourceParentView: ViewGroup? = null
    private val mSourceViewLayoutParams: ViewGroup.LayoutParams
    private var mSourceViewIndexInParent = 0
    private val mSourceViewId: Int


    init {
        mSourceViewLayoutParams = sourceView.layoutParams
        currentView = sourceView
        mSourceViewId = sourceView.id
        val viewGroup = sourceView.parent as ViewGroup
    }


    fun replace(targetViewResID: Int) {
        if (mTargetViewResID == targetViewResID) {
            return
        }
        if (initParentView()) {
            mTargetViewResID = targetViewResID
            replace(
                LayoutInflater.from(sourceView.context)
                    .inflate(mTargetViewResID, mSourceParentView, false)
            )
        }
    }

    fun replace(targetView: View) {
        if (currentView === targetView) {
            return
        }
        if (targetView.parent != null) {
            (targetView.parent as ViewGroup).removeView(targetView)
        }
        if (initParentView()) {
            this.targetView = targetView
            mSourceParentView!!.removeView(currentView)
            targetView.id = mSourceViewId
            mSourceParentView!!.addView(
                this.targetView,
                mSourceViewIndexInParent,
                mSourceViewLayoutParams
            )
            currentView = this.targetView
        }
    }

    fun restore() {
        if (mSourceParentView != null) {
            mSourceParentView!!.removeView(currentView)
            mSourceParentView!!.addView(
                sourceView,
                mSourceViewIndexInParent,
                mSourceViewLayoutParams
            )
            currentView = sourceView
            targetView = null
            mTargetViewResID = -1
        }
    }

    private fun initParentView(): Boolean {
        if (mSourceParentView == null) {
            mSourceParentView = sourceView.parent as ViewGroup
            if (mSourceParentView == null) {
                return false
            }
            mSourceParentView!!.addOnAttachStateChangeListener(object :
                View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(view: View) {}
                override fun onViewDetachedFromWindow(view: View) {
                    mSourceParentView = null
                }
            })
            val count = mSourceParentView!!.childCount
            for (index in 0 until count) {
                if (sourceView === mSourceParentView!!.getChildAt(index)) {
                    mSourceViewIndexInParent = index
                    break
                }
            }
        }
        return true
    }


}