package com.tsm.tsmbottomsheetdialog.tsm_bottom_sheet;
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tsm.tsmbottomsheetdialog.R
import com.tsm.tsmbottomsheetdialog.tsm_bottom_sheet.TsmBottomSheetBehavior

/**
 * Create by tsm
 * on 2021/5/24
 */
open class BaseBottomSheetDialog : AppCompatDialog {
    private var mBehavior: TsmBottomSheetBehavior<FrameLayout>? = null
    private var mCancelable = true
    private var mCanceledOnTouchOutside = true
    private var mCanceledOnTouchOutsideSet = false
    protected var mContext: Activity? = null

    constructor(context: Activity) : this(context, 0) {
        this.mContext = context
    }

    constructor(context: Context, @StyleRes theme: Int) : super(
        context,
        getThemeResId(context, theme)
    ) {
        // We hide the title bar for any style configuration. Otherwise, there will be a gap
        // above the bottom sheet when it is expanded.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    protected constructor(
        context: Context, cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        mCancelable = cancelable
    }

    override fun setContentView(@LayoutRes layoutResId: Int) {
        super.setContentView(wrapInBottomSheet(layoutResId, 0, null, null))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


    override fun setContentView(view: View) {
        super.setContentView(wrapInBottomSheet(0, 0, view, null))
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(wrapInBottomSheet(0, 0, view, params))
    }

    fun setContentView(view: View?, @LayoutRes bottom: Int) {
        super.setContentView(wrapInBottomSheet(0, bottom, view, null))
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        if (mCancelable != cancelable) {
            mCancelable = cancelable
            if (mBehavior != null) {
                mBehavior!!.isHideable = cancelable
            }
        }
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        if (cancel && !mCancelable) {
            mCancelable = true
        }
        mCanceledOnTouchOutside = cancel
        mCanceledOnTouchOutsideSet = true
    }

    private fun wrapInBottomSheet(
        layoutResId: Int,
        bottomLayoutId: Int,
        view: View?,
        params: ViewGroup.LayoutParams?
    ): View {
        var view = view
        val parent = View.inflate(context, R.layout.zr_bottom_sheet_dialog_with_bottom, null)
        val coordinator = parent.findViewById<View>(R.id.coordinator) as CoordinatorLayout
        if (layoutResId != 0 && view == null) {
            view = layoutInflater.inflate(layoutResId, coordinator, false)
        }
        if (bottomLayoutId != 0) {
            val bottomView = layoutInflater.inflate(bottomLayoutId, coordinator, false)
            val fl = parent.findViewById<FrameLayout>(R.id.bottom_design_bottom_sheet)
            fl.addView(bottomView)
            coordinator.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    coordinator.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val p2 = coordinator.layoutParams as FrameLayout.LayoutParams
                    p2.setMargins(0, dp2px(context, 60f), 0, bottomView.height)
                    coordinator.layoutParams = p2
                    val p1 = fl.layoutParams
                    p1.height = bottomView.height
                    fl.layoutParams = p1
                }
            })
//            bottomView.isClickable=true
        }
        val bottomSheet = coordinator.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.setOnClickListener { }
        mBehavior = TsmBottomSheetBehavior.from(bottomSheet)
        mBehavior?.setBottomSheetCallback(mBottomSheetCallback)
        mBehavior?.setHideable(mCancelable)
        if (params == null) {
            bottomSheet.addView(view)
        } else {
            bottomSheet.addView(view, params)
        }
        // We treat the CoordinatorLayout as outside the dialog though it is technically inside
        coordinator.findViewById<View>(R.id.touch_outside).setOnClickListener {
            if (mCancelable && isShowing && shouldWindowCloseOnTouchOutside()) {
                cancel()
            }
        }

        parent?.findViewById<View>(R.id.container)?.setOnClickListener {
            if (mCancelable && isShowing && shouldWindowCloseOnTouchOutside()) {
                cancel()
            }
        }

        return parent
    }

    private fun shouldWindowCloseOnTouchOutside(): Boolean {
        if (!mCanceledOnTouchOutsideSet) {
            if (Build.VERSION.SDK_INT < 11) {
                mCanceledOnTouchOutside = true
            } else {
                val a =
                    context.obtainStyledAttributes(intArrayOf(android.R.attr.windowCloseOnTouchOutside))
                mCanceledOnTouchOutside = a.getBoolean(0, true)
                a.recycle()
            }
            mCanceledOnTouchOutsideSet = true
        }
        return mCanceledOnTouchOutside
    }

    private val mBottomSheetCallback: TsmBottomSheetBehavior.TsmBottomSheetCallback = object : TsmBottomSheetBehavior.TsmBottomSheetCallback() {
        override fun onStateChanged(
            bottomSheet: View,
            @BottomSheetBehavior.State newState: Int
        ) {
//            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                dismiss();
//            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    companion object {
        private fun getThemeResId(context: Context, themeId: Int): Int {
            var themeId = themeId
            if (themeId == 0) {
                // If the provided theme is 0, then retrieve the dialogTheme from our theme
                val outValue = TypedValue()
                themeId = if (context.theme.resolveAttribute(
                        R.attr.bottomSheetDialogTheme, outValue, true
                    )
                ) {
                    outValue.resourceId
                } else {
                    // bottomSheetDialogTheme is not provided; we default to our light theme
                    R.style.Theme_Design_Light_BottomSheetDialog
                }
            }
            return themeId
        }
    }


    open fun dp2px(context: Context, dp: Float): Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return (dp * scale + 0.5f).toInt()
    }

}