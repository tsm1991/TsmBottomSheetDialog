package com.tsm.tsmbottomsheetdialog.tsm_bottom_sheet;
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tsm.tsmbottomsheetdialog.R

abstract class TsmBaseBottomSheetDialog : BaseBottomSheetDialog {

    constructor(context: Activity) :super(context, R.style.bottom_sheet_dilog){
        initDialog()
    }

    open fun initDialog() {
        //需要设置这个才能设置状态栏和导航栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏颜色
            window?.setStatusBarColor(Color.TRANSPARENT)
        }
        //回复默认导航栏颜色
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        setContentView(view, bottomLayoutId)
        behaver = TsmBottomSheetBehavior.from(view.parent as View)
        behaver?.setBottomSheetCallback(object : TsmBottomSheetBehavior.TsmBottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }


    /**
     * 可以控制菜单状态
     */
    protected var behaver: TsmBottomSheetBehavior<View>?=null


    override fun show() {
        initViews()
        super.show()
    }


    protected abstract val layoutId: Int
    protected open val bottomLayoutId: Int
        protected get() = 0

    protected abstract fun initViews()
}