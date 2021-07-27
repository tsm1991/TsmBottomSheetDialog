package com.tsm.tsmbottomsheetdialog.tsm_bottom_sheet;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.resources.MaterialResources;
import com.tsm.tsmbottomsheetdialog.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.widget.ViewDragHelper;

public class TsmBottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_SETTLING = 2;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_HALF_EXPANDED = 6;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int SAVE_PEEK_HEIGHT = 1;
    public static final int SAVE_FIT_TO_CONTENTS = 2;
    public static final int SAVE_HIDEABLE = 4;
    public static final int SAVE_SKIP_COLLAPSED = 8;
    public static final int SAVE_ALL = -1;
    public static final int SAVE_NONE = 0;
    private static final String TAG = "TsmBottomSheetBehavior";
    private int saveFlags = 0;
    private static final float HIDE_THRESHOLD = 0.5F;
    private static final float HIDE_FRICTION = 0.1F;
    private static final int CORNER_ANIMATION_DURATION = 500;
    private boolean fitToContents = true;
    private float maximumVelocity;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private boolean shapeThemingEnabled;
//    private MaterialShapeDrawable materialShapeDrawable;
//    private ShapeAppearanceModel shapeAppearanceModelDefault;
    private boolean isShapeExpanded;
    private TsmBottomSheetBehavior<V>.TsmSettleRunnable TsmSettleRunnable = null;
    @Nullable
    private ValueAnimator interpolatorAnimator;
    private static final int DEF_STYLE_RES;
    int expandedOffset;
    int fitToContentsOffset;
    int halfExpandedOffset;
    float halfExpandedRatio = 0.5F;
    int collapsedOffset;
    float elevation = -1.0F;
    boolean hideable;
    private boolean skipCollapsed;
    int state = 4;
    @Nullable
    ViewDragHelper viewDragHelper;
    private boolean ignoreEvents;
    private int lastNestedScrollDy;
    private boolean nestedScrolled;
    int parentWidth;
    int parentHeight;
    @Nullable
    WeakReference<V> viewRef;
    @Nullable
    WeakReference<View> nestedScrollingChildRef;
    @NonNull
    private final ArrayList<TsmBottomSheetBehavior.TsmBottomSheetCallback> callbacks = new ArrayList();
    @Nullable
    private VelocityTracker velocityTracker;
    int activePointerId;
    private int initialY;
    boolean touchingScrollingChild;
    @Nullable
    private Map<View, Integer> importantForAccessibilityMap;
    private final ViewDragHelper.Callback dragCallback;

    public TsmBottomSheetBehavior() {
        this.dragCallback = new TsmNamelessClass_1();
    }

    public TsmBottomSheetBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        this.dragCallback = new TsmNamelessClass_1();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetBehavior_Layout);
        this.shapeThemingEnabled = a.hasValue(R.styleable.BottomSheetBehavior_Layout_shapeAppearance);
        boolean hasBackgroundTint = a.hasValue(R.styleable.BottomSheetBehavior_Layout_backgroundTint);
        if (hasBackgroundTint) {
            @SuppressLint("RestrictedApi")
            ColorStateList bottomSheetColor = MaterialResources.getColorStateList(context, a, R.styleable.BottomSheetBehavior_Layout_backgroundTint);
            this.createMaterialShapeDrawable(context, attrs, hasBackgroundTint, bottomSheetColor);
        } else {
            this.createMaterialShapeDrawable(context, attrs, hasBackgroundTint);
        }

        this.createShapeValueAnimator();
        if (Build.VERSION.SDK_INT >= 21) {
            this.elevation = a.getDimension(R.styleable.BottomSheetBehavior_Layout_android_elevation, -1.0F);
        }

        TypedValue value = a.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (value != null && value.data == -1) {
            this.setPeekHeight(value.data);
        } else {
            this.setPeekHeight(a.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        }

        this.setHideable(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        this.setFitToContents(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        this.setSkipCollapsed(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        this.setSaveFlags(a.getInt(R.styleable.BottomSheetBehavior_Layout_behavior_saveFlags, 0));
        this.setHalfExpandedRatio(a.getFloat(R.styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5F));
        this.setExpandedOffset(a.getInt(R.styleable.BottomSheetBehavior_Layout_behavior_expandedOffset, 0));
        a.recycle();
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.maximumVelocity = (float)configuration.getScaledMaximumFlingVelocity();
    }

    @NonNull
    public Parcelable onSaveInstanceState(@NonNull CoordinatorLayout parent, @NonNull V child) {
        return new TsmBottomSheetBehavior.SavedState(super.onSaveInstanceState(parent, child), this);
    }

    public void onRestoreInstanceState(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull Parcelable state) {
        TsmBottomSheetBehavior.SavedState ss = (TsmBottomSheetBehavior.SavedState)state;
        super.onRestoreInstanceState(parent, child, ss.getSuperState());
        this.restoreOptionalState(ss);
        if (ss.state != 1 && ss.state != STATE_SETTLING) {
            this.state = ss.state;
        } else {
            this.state = STATE_COLLAPSED;
        }

    }

    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams layoutParams) {
        super.onAttachedToLayoutParams(layoutParams);
        this.viewRef = null;
        this.viewDragHelper = null;
    }

    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        this.viewRef = null;
        this.viewDragHelper = null;
    }

    @SuppressLint("WrongConstant")
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull V child, int layoutDirection) {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            child.setFitsSystemWindows(true);
        }

        if (this.viewRef == null) {
            this.peekHeightMin = parent.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            this.viewRef = new WeakReference(child);
//            if (this.shapeThemingEnabled && this.materialShapeDrawable != null) {
//                ViewCompat.setBackground(child, this.materialShapeDrawable);
//            }
//
//            if (this.materialShapeDrawable != null) {
//                this.materialShapeDrawable.setElevation(this.elevation == -1.0F ? ViewCompat.getElevation(child) : this.elevation);
//                this.isShapeExpanded = this.state == 3;
//                this.materialShapeDrawable.setInterpolation(this.isShapeExpanded ? 0.0F : 1.0F);
//            }

            this.updateAccessibilityActions();
            if (ViewCompat.getImportantForAccessibility(child) == 0) {
                ViewCompat.setImportantForAccessibility(child, 1);
            }
        }

        if (this.viewDragHelper == null) {
            this.viewDragHelper = ViewDragHelper.create(parent, this.dragCallback);
        }

        int savedTop = child.getTop();
        parent.onLayoutChild(child, layoutDirection);
        this.parentWidth = parent.getWidth();
        this.parentHeight = parent.getHeight();
        this.fitToContentsOffset = Math.max(0, this.parentHeight - child.getHeight());
        this.calculateHalfExpandedOffset();
        this.calculateCollapsedOffset();
        ViewCompat.offsetTopAndBottom(child, this.getExpandedOffset());
//        if (this.state == 3||this.state == 6) {
//            ViewCompat.offsetTopAndBottom(child, this.getExpandedOffset());
//        }
//
////        去除onLayout 中的折叠状态
//
////        else if (this.state == 6) {
////            ViewCompat.offsetTopAndBottom(child, this.halfExpandedOffset);
////        }
//        else if (this.hideable && this.state == 5) {
//            ViewCompat.offsetTopAndBottom(child, this.parentHeight);
//        } else if (this.state == 4) {
//            ViewCompat.offsetTopAndBottom(child, this.collapsedOffset);
//        } else if (this.state == 1 || this.state == 2) {
//            ViewCompat.offsetTopAndBottom(child, savedTop - child.getTop());
//        }

        this.nestedScrollingChildRef = new WeakReference(this.findScrollingChild(child));
        return true;
    }

    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
        if (!child.isShown()) {
            this.ignoreEvents = true;
            return false;
        } else {
            int action = event.getActionMasked();
            if (action == 0) {
                this.reset();
            }

            if (this.velocityTracker == null) {
                this.velocityTracker = VelocityTracker.obtain();
            }

            this.velocityTracker.addMovement(event);
            switch(action) {
                case 0:
                    int initialX = (int)event.getX();
                    this.initialY = (int)event.getY();
                    if (this.state != STATE_SETTLING) {
                        View scroll = this.nestedScrollingChildRef != null ? (View)this.nestedScrollingChildRef.get() : null;
                        if (scroll != null && parent.isPointInChildBounds(scroll, initialX, this.initialY)) {
                            this.activePointerId = event.getPointerId(event.getActionIndex());
                            this.touchingScrollingChild = true;
                        }
                    }

                    this.ignoreEvents = this.activePointerId == -1 && !parent.isPointInChildBounds(child, initialX, this.initialY);
                    break;
                case 1:
                case STATE_EXPANDED:
                    this.touchingScrollingChild = false;
                    this.activePointerId = -1;
                    if (this.ignoreEvents) {
                        this.ignoreEvents = false;
                        return false;
                    }
                case STATE_SETTLING:
            }

            if (!this.ignoreEvents && this.viewDragHelper != null && this.viewDragHelper.shouldInterceptTouchEvent(event)) {
                return true;
            } else {
                View scroll = this.nestedScrollingChildRef != null ? (View)this.nestedScrollingChildRef.get() : null;
                return action == 2 && scroll != null && !this.ignoreEvents && this.state != 1 && !parent.isPointInChildBounds(scroll, (int)event.getX(), (int)event.getY()) && this.viewDragHelper != null && Math.abs((float)this.initialY - event.getY()) > (float)this.viewDragHelper.getTouchSlop();
            }
        }
    }

    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
        if (!child.isShown()) {
            return false;
        } else {
            int action = event.getActionMasked();
            if (this.state == 1 && action == 0) {
                return true;
            } else {
                if (this.viewDragHelper != null) {
                    this.viewDragHelper.processTouchEvent(event);
                }

                if (action == 0) {
                    this.reset();
                }

                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }

                this.velocityTracker.addMovement(event);
                if (action == 2 && !this.ignoreEvents && Math.abs((float)this.initialY - event.getY()) > (float)this.viewDragHelper.getTouchSlop()) {
                    this.viewDragHelper.captureChildView(child, event.getPointerId(event.getActionIndex()));
                }

                return !this.ignoreEvents;
            }
        }
    }

    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        return (axes & 2) != 0;
    }

    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (type != 1) {
            View scrollingChild = this.nestedScrollingChildRef != null ? (View)this.nestedScrollingChildRef.get() : null;
            if (target == scrollingChild) {
                int currentTop = child.getTop();
                int newTop = currentTop - dy;
                if (dy > 0) {
                    if (newTop < this.getExpandedOffset()) {
                        consumed[1] = currentTop - this.getExpandedOffset();
                        ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                        this.setStateInternal(STATE_EXPANDED);
                    } else {
                        consumed[1] = dy;
                        ViewCompat.offsetTopAndBottom(child, -dy);
                        this.setStateInternal(1);
                    }
                } else if (dy < 0 && !target.canScrollVertically(-1)) {
                    if (newTop > this.collapsedOffset && !this.hideable) {
                        consumed[1] = currentTop - this.collapsedOffset;
                        ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                        this.setStateInternal(STATE_COLLAPSED);
                    } else {
                        consumed[1] = dy;
                        ViewCompat.offsetTopAndBottom(child, -dy);
                        this.setStateInternal(1);
                    }
                }

                this.dispatchOnSlide(child.getTop());
                this.lastNestedScrollDy = dy;
                this.nestedScrolled = true;
            }
        }
    }

    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (child.getTop() == this.getExpandedOffset()) {
            this.setStateInternal(STATE_EXPANDED);
        } else if (this.nestedScrollingChildRef != null && target == this.nestedScrollingChildRef.get() && this.nestedScrolled) {
            int top;
            byte targetState;
            if (this.lastNestedScrollDy > 0) {
                top = this.getExpandedOffset();
                targetState = STATE_EXPANDED;
            } else if (this.hideable && this.shouldHide(child, this.getYVelocity())) {
                top = this.parentHeight;
                targetState = STATE_HIDDEN;
            } else {
                int currentTop;
                if (this.lastNestedScrollDy == 0) {
                    currentTop = child.getTop();
                    if (currentTop < this.halfExpandedOffset){
                        targetState = STATE_HIDDEN;
                        top = this.parentHeight;
                    }else{
                        top = this.getExpandedOffset();
                        targetState = STATE_EXPANDED;
                    }
                }else {
                    currentTop = child.getTop();
                    if (Math.abs(currentTop - this.halfExpandedOffset) < Math.abs(currentTop - this.collapsedOffset)) {
                        top = this.getExpandedOffset();
                        targetState = STATE_EXPANDED;
                    } else {
                        top = this.parentHeight;
                        targetState = STATE_HIDDEN;
                    }
                }
            }
            this.startSettlingAnimation(child, targetState, top, false);
            this.nestedScrolled = false;
        }
    }

    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
    }

    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        if (this.nestedScrollingChildRef == null) {
            return false;
        } else {
            return target == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY));
        }
    }

    public boolean isFitToContents() {
        return this.fitToContents;
    }

    public void setFitToContents(boolean fitToContents) {
        if (this.fitToContents != fitToContents) {
            this.fitToContents = fitToContents;
            if (this.viewRef != null) {
                this.calculateCollapsedOffset();
            }

            this.setStateInternal(this.fitToContents && this.state == STATE_HALF_EXPANDED ? STATE_EXPANDED : this.state);
            this.updateAccessibilityActions();
        }
    }

    public void setPeekHeight(int peekHeight) {
        this.setPeekHeight(peekHeight, false);
    }

    public final void setPeekHeight(int peekHeight, boolean animate) {
        boolean layout = false;
        if (peekHeight == -1) {
            if (!this.peekHeightAuto) {
                this.peekHeightAuto = true;
                layout = true;
            }
        } else if (this.peekHeightAuto || this.peekHeight != peekHeight) {
            this.peekHeightAuto = false;
            this.peekHeight = Math.max(0, peekHeight);
            layout = true;
        }

        if (layout && this.viewRef != null) {
            this.calculateCollapsedOffset();
            if (this.state == STATE_COLLAPSED) {
                V view = (V)this.viewRef.get();
                if (view != null) {
                    if (animate) {
                        this.settleToStatePendingLayout(this.state);
                    } else {
                        view.requestLayout();
                    }
                }
            }
        }

    }

    public int getPeekHeight() {
        return this.peekHeightAuto ? -1 : this.peekHeight;
    }

    public void setHalfExpandedRatio(@FloatRange(from = 0.0D,to = 1.0D) float ratio) {
        if (ratio > 0.0F && ratio < 1.0F) {
            this.halfExpandedRatio = ratio;
        } else {
            throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
        }
    }

    public void setExpandedOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be greater than or equal to 0");
        } else {
            this.expandedOffset = offset;
        }
    }

    @FloatRange(
            from = 0.0D,
            to = 1.0D
    )
    public float getHalfExpandedRatio() {
        return this.halfExpandedRatio;
    }

    public void setHideable(boolean hideable) {
        if (this.hideable != hideable) {
            this.hideable = hideable;
            if (!hideable && this.state == STATE_HIDDEN) {
                this.setState(STATE_COLLAPSED);
            }

            this.updateAccessibilityActions();
        }

    }

    public boolean isHideable() {
        return this.hideable;
    }

    public void setSkipCollapsed(boolean skipCollapsed) {
        this.skipCollapsed = skipCollapsed;
    }

    public boolean getSkipCollapsed() {
        return this.skipCollapsed;
    }

    public void setSaveFlags(int flags) {
        this.saveFlags = flags;
    }

    public int getSaveFlags() {
        return this.saveFlags;
    }

    /** @deprecated */
    @Deprecated
    public void setBottomSheetCallback(TsmBottomSheetBehavior.TsmBottomSheetCallback callback) {
        Log.w("TsmBottomSheetBehavior", "TsmBottomSheetBehavior now supports multiple callbacks. `setBottomSheetCallback()` removes all existing callbacks, including ones set internally by library authors, which may result in unintended behavior. This may change in the future. Please use `addBottomSheetCallback()` and `removeBottomSheetCallback()` instead to set your own callbacks.");
        this.callbacks.clear();
        if (callback != null) {
            this.callbacks.add(callback);
        }

    }

    public void addBottomSheetCallback(@NonNull TsmBottomSheetBehavior.TsmBottomSheetCallback callback) {
        if (!this.callbacks.contains(callback)) {
            this.callbacks.add(callback);
        }

    }

    public void removeBottomSheetCallback(@NonNull TsmBottomSheetBehavior.TsmBottomSheetCallback callback) {
        this.callbacks.remove(callback);
    }

    public void setState(int state) {
        if (state != this.state) {
            if (this.viewRef != null) {
                this.settleToStatePendingLayout(state);
            } else {
                if (state == STATE_COLLAPSED || state == STATE_EXPANDED || state == STATE_HALF_EXPANDED || this.hideable && state == STATE_HIDDEN) {
                    this.state = state;
                }

            }
        }
    }

    private void settleToStatePendingLayout(final int state) {
        final V child = (V)this.viewRef.get();
        if (child != null) {
            ViewParent parent = child.getParent();
            if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow(child)) {
                child.post(new Runnable() {
                    public void run() {
                        TsmBottomSheetBehavior.this.settleToState(child, state);
                    }
                });
            } else {
                this.settleToState(child, state);
            }

        }
    }

    public int getState() {
        return this.state;
    }

    void setStateInternal(int state) {
        if (this.state != state) {
            this.state = state;
            if (this.viewRef != null) {
                View bottomSheet = (View)this.viewRef.get();
                if (bottomSheet != null) {
                    if (state != STATE_HALF_EXPANDED && state != 3) {
                        if (state == 5 || state == STATE_COLLAPSED) {
                            this.updateImportantForAccessibility(false);
                        }
                    } else {
                        this.updateImportantForAccessibility(true);
                    }

                    this.updateDrawableForTargetState(state);

                    for(int i = 0; i < this.callbacks.size(); ++i) {
                        ((TsmBottomSheetBehavior.TsmBottomSheetCallback)this.callbacks.get(i)).onStateChanged(bottomSheet, state);
                    }

                    this.updateAccessibilityActions();
                }
            }
        }
    }

    private void updateDrawableForTargetState(int state) {
        if (state != STATE_SETTLING) {
            boolean expand = state == STATE_EXPANDED;
            if (this.isShapeExpanded != expand) {
                this.isShapeExpanded = expand;
//                if (this.materialShapeDrawable != null && this.interpolatorAnimator != null) {
//                    if (this.interpolatorAnimator.isRunning()) {
//                        this.interpolatorAnimator.reverse();
//                    } else {
//                        float to = expand ? 0.0F : 1.0F;
//                        float from = 1.0F - to;
//                        this.interpolatorAnimator.setFloatValues(new float[]{from, to});
//                        this.interpolatorAnimator.start();
//                    }
//                }
            }

        }
    }

    private void calculateCollapsedOffset() {
        int peek;
        if (this.peekHeightAuto) {
            peek = Math.max(this.peekHeightMin, this.parentHeight - this.parentWidth * 9 / 16);
        } else {
            peek = this.peekHeight;
        }

        if (this.fitToContents) {
            this.collapsedOffset = Math.max(this.parentHeight - peek, this.fitToContentsOffset);
        } else {
            this.collapsedOffset = this.parentHeight - peek;
        }

    }

    private void calculateHalfExpandedOffset() {
        this.halfExpandedOffset = (int)((float)this.parentHeight * (1.0F - this.halfExpandedRatio));
    }

    private void reset() {
        this.activePointerId = -1;
        if (this.velocityTracker != null) {
            this.velocityTracker.recycle();
            this.velocityTracker = null;
        }

    }

    private void restoreOptionalState(@NonNull TsmBottomSheetBehavior.SavedState ss) {
        if (this.saveFlags != 0) {
            if (this.saveFlags == -1 || (this.saveFlags & 1) == 1) {
                this.peekHeight = ss.peekHeight;
            }

            if (this.saveFlags == -1 || (this.saveFlags & STATE_SETTLING) == STATE_SETTLING) {
                this.fitToContents = ss.fitToContents;
            }

            if (this.saveFlags == -1 || (this.saveFlags & STATE_COLLAPSED) == STATE_COLLAPSED) {
                this.hideable = ss.hideable;
            }

            if (this.saveFlags == -1 || (this.saveFlags & 8) == 8) {
                this.skipCollapsed = ss.skipCollapsed;
            }

        }
    }

    boolean shouldHide(@NonNull View child, float yvel) {
        if (this.skipCollapsed) {
            return true;
        } else if (child.getTop() < this.collapsedOffset) {
            return false;
        } else {
            float newTop = (float)child.getTop() + yvel * 0.1F;
            return Math.abs(newTop - (float)this.collapsedOffset) / (float)this.peekHeight > 0.5F;
        }
    }

    @Nullable
    @VisibleForTesting
    View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        } else {
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup)view;
                int i = 0;

                for(int count = group.getChildCount(); i < count; ++i) {
                    View scrollingChild = this.findScrollingChild(group.getChildAt(i));
                    if (scrollingChild != null) {
                        return scrollingChild;
                    }
                }
            }

            return null;
        }
    }

    private void createMaterialShapeDrawable(@NonNull Context context, AttributeSet attrs, boolean hasBackgroundTint) {
        this.createMaterialShapeDrawable(context, attrs, hasBackgroundTint, (ColorStateList)null);
    }

    private void createMaterialShapeDrawable(@NonNull Context context, AttributeSet attrs, boolean hasBackgroundTint, @Nullable ColorStateList bottomSheetColor) {
//        if (this.shapeThemingEnabled) {
//            this.shapeAppearanceModelDefault = ShapeAppearanceModel.builder(context, attrs, R.attr.bottomSheetStyle, DEF_STYLE_RES).build();
//            this.materialShapeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModelDefault);
//            this.materialShapeDrawable.initializeElevationOverlay(context);
//            if (hasBackgroundTint && bottomSheetColor != null) {
//                this.materialShapeDrawable.setFillColor(bottomSheetColor);
//            } else {
//                TypedValue defaultColor = new TypedValue();
//                context.getTheme().resolveAttribute(16842801, defaultColor, true);
//                this.materialShapeDrawable.setTint(defaultColor.data);
//            }
//        }

    }

    private void createShapeValueAnimator() {
//        this.interpolatorAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
//        this.interpolatorAnimator.setDuration(500L);
//        this.interpolatorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
//                float value = (Float)animation.getAnimatedValue();
//                if (TsmBottomSheetBehavior.this.materialShapeDrawable != null) {
//                    TsmBottomSheetBehavior.this.materialShapeDrawable.setInterpolation(value);
//                }
//
//            }
//        });
    }

    private float getYVelocity() {
        if (this.velocityTracker == null) {
            return 0.0F;
        } else {
            this.velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
            return this.velocityTracker.getYVelocity(this.activePointerId);
        }
    }

    private int getExpandedOffset() {
        return this.fitToContents ? this.fitToContentsOffset : this.expandedOffset;
    }

    void settleToState(@NonNull View child, int state) {
        int top;
        if (state == STATE_COLLAPSED) {
            top = this.collapsedOffset;
        } else if (state == STATE_HALF_EXPANDED) {
            top = this.halfExpandedOffset;
            if (this.fitToContents && top <= this.fitToContentsOffset) {
                state = STATE_EXPANDED;
                top = this.fitToContentsOffset;
            }
        } else if (state == STATE_EXPANDED) {
            top = this.getExpandedOffset();
        } else {
            if (!this.hideable || state != STATE_HIDDEN) {
                throw new IllegalArgumentException("Illegal state argument: " + state);
            }

            top = this.parentHeight;
        }

        this.startSettlingAnimation(child, state, top, false);
    }

    void startSettlingAnimation(View child, int state, int top, boolean settleFromViewDragHelper) {
        boolean startedSettling = settleFromViewDragHelper ? this.viewDragHelper.settleCapturedViewAt(child.getLeft(), top) : this.viewDragHelper.smoothSlideViewTo(child, child.getLeft(), top);
        if (startedSettling) {
            this.setStateInternal(STATE_SETTLING);
            this.updateDrawableForTargetState(state);
            if (this.TsmSettleRunnable == null) {
                this.TsmSettleRunnable = new TsmBottomSheetBehavior.TsmSettleRunnable(child, state);
            }

            if (!this.TsmSettleRunnable.isPosted) {
                this.TsmSettleRunnable.targetState = state;
                ViewCompat.postOnAnimation(child, this.TsmSettleRunnable);
                this.TsmSettleRunnable.isPosted = true;
            } else {
                this.TsmSettleRunnable.targetState = state;
            }
        } else {
            this.setStateInternal(state);
        }

    }

    void dispatchOnSlide(int top) {
        View bottomSheet = (View)this.viewRef.get();
        if (bottomSheet != null && !this.callbacks.isEmpty()) {
            float slideOffset = top > this.collapsedOffset ? (float)(this.collapsedOffset - top) / (float)(this.parentHeight - this.collapsedOffset) : (float)(this.collapsedOffset - top) / (float)(this.collapsedOffset - this.getExpandedOffset());

            for(int i = 0; i < this.callbacks.size(); ++i) {
                ((TsmBottomSheetBehavior.TsmBottomSheetCallback)this.callbacks.get(i)).onSlide(bottomSheet, slideOffset);
            }
        }

    }

    @VisibleForTesting
    int getPeekHeightMin() {
        return this.peekHeightMin;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    @VisibleForTesting
    public void disableShapeAnimations() {
        this.interpolatorAnimator = null;
    }

    @NonNull
    public static <V extends View> TsmBottomSheetBehavior<V> from(@NonNull V view) {
        android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        } else {
            CoordinatorLayout.Behavior<?> behavior = ((CoordinatorLayout.LayoutParams)params).getBehavior();
            if (!(behavior instanceof TsmBottomSheetBehavior)) {
                throw new IllegalArgumentException("The view is not associated with TsmBottomSheetBehavior");
            } else {
                return (TsmBottomSheetBehavior)behavior;
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void updateImportantForAccessibility(boolean expanded) {
        if (this.viewRef != null) {
            ViewParent viewParent = ((View)this.viewRef.get()).getParent();
            if (viewParent instanceof CoordinatorLayout) {
                CoordinatorLayout parent = (CoordinatorLayout)viewParent;
                int childCount = parent.getChildCount();
                if (Build.VERSION.SDK_INT >= 16 && expanded) {
                    if (this.importantForAccessibilityMap != null) {
                        return;
                    }

                    this.importantForAccessibilityMap = new HashMap(childCount);
                }

                for(int i = 0; i < childCount; ++i) {
                    View child = parent.getChildAt(i);
                    if (child != this.viewRef.get()) {
                        if (!expanded) {
                            if (this.importantForAccessibilityMap != null && this.importantForAccessibilityMap.containsKey(child)) {
                                ViewCompat.setImportantForAccessibility(child, (Integer)this.importantForAccessibilityMap.get(child));
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= 16) {
                                this.importantForAccessibilityMap.put(child, child.getImportantForAccessibility());
                            }

                            ViewCompat.setImportantForAccessibility(child, STATE_COLLAPSED);
                        }
                    }
                }

                if (!expanded) {
                    this.importantForAccessibilityMap = null;
                }

            }
        }
    }

    private void updateAccessibilityActions() {
        if (this.viewRef != null) {
            V child = (V)this.viewRef.get();
            if (child != null) {
                ViewCompat.removeAccessibilityAction(child, 524288);
                ViewCompat.removeAccessibilityAction(child, 262144);
                ViewCompat.removeAccessibilityAction(child, 1048576);
                if (this.hideable && this.state != STATE_HIDDEN) {
                    this.addAccessibilityActionForState(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, 5);
                }

                int nextState;
                switch(this.state) {
                    case STATE_EXPANDED:
                        nextState = this.fitToContents ? STATE_COLLAPSED : STATE_HALF_EXPANDED;
                        this.addAccessibilityActionForState(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, nextState);
                        break;
                    case STATE_COLLAPSED:
                        nextState = this.fitToContents ? STATE_EXPANDED : STATE_HALF_EXPANDED;
                        this.addAccessibilityActionForState(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, nextState);
                    case 5:
                    default:
                        break;
                    case STATE_HALF_EXPANDED:
                        this.addAccessibilityActionForState(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, STATE_COLLAPSED);
                        this.addAccessibilityActionForState(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, 3);
                }

            }
        }
    }

    private void addAccessibilityActionForState(V child, AccessibilityNodeInfoCompat.AccessibilityActionCompat action, final int state) {
        ViewCompat.replaceAccessibilityAction(child, action, (CharSequence)null, new AccessibilityViewCommand() {
            public boolean perform(@NonNull View view, @Nullable CommandArguments arguments) {
                TsmBottomSheetBehavior.this.setState(state);
                return true;
            }
        });
    }

    static {
        DEF_STYLE_RES = R.style.Widget_Design_BottomSheet_Modal;
    }

    protected static class SavedState extends AbsSavedState {
        final int state;
        int peekHeight;
        boolean fitToContents;
        boolean hideable;
        boolean skipCollapsed;
        public static final Creator<TsmBottomSheetBehavior.SavedState> CREATOR = new ClassLoaderCreator<TsmBottomSheetBehavior.SavedState>() {
            @NonNull
            public TsmBottomSheetBehavior.SavedState createFromParcel(@NonNull Parcel in, ClassLoader loader) {
                return new TsmBottomSheetBehavior.SavedState(in, loader);
            }

            @Nullable
            public TsmBottomSheetBehavior.SavedState createFromParcel(@NonNull Parcel in) {
                return new TsmBottomSheetBehavior.SavedState(in, (ClassLoader)null);
            }

            @NonNull
            public TsmBottomSheetBehavior.SavedState[] newArray(int size) {
                return new TsmBottomSheetBehavior.SavedState[size];
            }
        };

        public SavedState(@NonNull Parcel source) {
            this((Parcel)source, (ClassLoader)null);
        }

        public SavedState(@NonNull Parcel source, ClassLoader loader) {
            super(source, loader);
            this.state = source.readInt();
            this.peekHeight = source.readInt();
            this.fitToContents = source.readInt() == 1;
            this.hideable = source.readInt() == 1;
            this.skipCollapsed = source.readInt() == 1;
        }

        public SavedState(Parcelable superState, @NonNull TsmBottomSheetBehavior<?> behavior) {
            super(superState);
            this.state = behavior.state;
            this.peekHeight = behavior.peekHeight;
            this.fitToContents = behavior.fitToContents;
            this.hideable = behavior.hideable;
            this.skipCollapsed = behavior.skipCollapsed;
        }

        /** @deprecated */
        @Deprecated
        public SavedState(Parcelable superstate, int state) {
            super(superstate);
            this.state = state;
        }

        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.state);
            out.writeInt(this.peekHeight);
            out.writeInt(this.fitToContents ? 1 : 0);
            out.writeInt(this.hideable ? 1 : 0);
            out.writeInt(this.skipCollapsed ? 1 : 0);
        }
    }

    private class TsmSettleRunnable implements Runnable {
        private final View view;
        private boolean isPosted;
        int targetState;

        TsmSettleRunnable(View view, int targetState) {
            this.view = view;
            this.targetState = targetState;
        }

        public void run() {
            if (TsmBottomSheetBehavior.this.viewDragHelper != null && TsmBottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, this);
            } else {
                TsmBottomSheetBehavior.this.setStateInternal(this.targetState);
            }

            this.isPosted = false;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface SaveFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface State {
    }

    public abstract static class TsmBottomSheetCallback {
        public TsmBottomSheetCallback() {}

        public abstract void onStateChanged(@NonNull View var1, int var2);

        public abstract void onSlide(@NonNull View var1, float var2);
    }

    private class TsmNamelessClass_1 extends ViewDragHelper.Callback {
        TsmNamelessClass_1() {
        }

        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            if (TsmBottomSheetBehavior.this.state == 1) {
                return false;
            } else if (TsmBottomSheetBehavior.this.touchingScrollingChild) {
                return false;
            } else {
                if (TsmBottomSheetBehavior.this.state == 3 && TsmBottomSheetBehavior.this.activePointerId == pointerId) {
                    View scroll = TsmBottomSheetBehavior.this.nestedScrollingChildRef != null ? (View)TsmBottomSheetBehavior.this.nestedScrollingChildRef.get() : null;
                    if (scroll != null && scroll.canScrollVertically(-1)) {
                        return false;
                    }
                }

                return TsmBottomSheetBehavior.this.viewRef != null && TsmBottomSheetBehavior.this.viewRef.get() == child;
            }
        }

        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            TsmBottomSheetBehavior.this.dispatchOnSlide(top);
        }

        public void onViewDragStateChanged(int state) {
            if (state == 1) {
                TsmBottomSheetBehavior.this.setStateInternal(1);
            }

        }

        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int top;
            byte targetState;
            int currentTop;
            if (yvel < 0.0F) {
                if (TsmBottomSheetBehavior.this.fitToContents) {
                    top = TsmBottomSheetBehavior.this.fitToContentsOffset;
                    targetState = STATE_EXPANDED;
                } else {
                    currentTop = releasedChild.getTop();
                    if (currentTop > TsmBottomSheetBehavior.this.halfExpandedOffset) {
                        top = TsmBottomSheetBehavior.this.halfExpandedOffset;
                        targetState = STATE_EXPANDED;
                    } else {
                        top = TsmBottomSheetBehavior.this.expandedOffset;
                        targetState = STATE_EXPANDED;
                    }
                }
            } else if (TsmBottomSheetBehavior.this.hideable && TsmBottomSheetBehavior.this.shouldHide(releasedChild, yvel) && (releasedChild.getTop() > TsmBottomSheetBehavior.this.collapsedOffset || Math.abs(xvel) < Math.abs(yvel))) {
                top = TsmBottomSheetBehavior.this.parentHeight;
                targetState = STATE_HIDDEN;
            } else if (yvel != 0.0F && Math.abs(xvel) <= Math.abs(yvel)) {
                if (TsmBottomSheetBehavior.this.fitToContents) {
                    top = TsmBottomSheetBehavior.this.parentHeight;
                    targetState = STATE_HIDDEN;
                } else {
                    currentTop = releasedChild.getTop();
                    if (Math.abs(currentTop - TsmBottomSheetBehavior.this.halfExpandedOffset) < Math.abs(currentTop - TsmBottomSheetBehavior.this.collapsedOffset)) {
                        top = TsmBottomSheetBehavior.this.expandedOffset;
                        targetState = STATE_EXPANDED;
                    } else {
                        top = TsmBottomSheetBehavior.this.parentHeight;
                        targetState = STATE_HIDDEN;
                    }
                }
            } else {
                currentTop = releasedChild.getTop();
                if (TsmBottomSheetBehavior.this.fitToContents) {
                    if (Math.abs(currentTop - TsmBottomSheetBehavior.this.fitToContentsOffset) < Math.abs(currentTop - TsmBottomSheetBehavior.this.collapsedOffset)) {
                        top = TsmBottomSheetBehavior.this.fitToContentsOffset;
                        targetState = STATE_EXPANDED;
                    } else {
                        top = TsmBottomSheetBehavior.this.parentHeight;
                        targetState = STATE_HIDDEN;
                    }
                } else if (currentTop < TsmBottomSheetBehavior.this.halfExpandedOffset) {
                    if (currentTop < Math.abs(currentTop - TsmBottomSheetBehavior.this.collapsedOffset)) {
                        top = TsmBottomSheetBehavior.this.expandedOffset;
                        targetState = STATE_EXPANDED;
                    } else {
                        top = TsmBottomSheetBehavior.this.parentHeight;
                        targetState = STATE_HIDDEN;
                    }
                } else if (Math.abs(currentTop - TsmBottomSheetBehavior.this.halfExpandedOffset) < Math.abs(currentTop - TsmBottomSheetBehavior.this.collapsedOffset)) {
                    top = TsmBottomSheetBehavior.this.parentHeight;
                    targetState = STATE_HIDDEN;
                } else {
                    top = TsmBottomSheetBehavior.this.parentHeight;
                    targetState = STATE_HIDDEN;
                }
            }
            TsmBottomSheetBehavior.this.startSettlingAnimation(releasedChild, targetState, top, true);
        }

        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return MathUtils.clamp(top, TsmBottomSheetBehavior.this.getExpandedOffset(), TsmBottomSheetBehavior.this.hideable ? TsmBottomSheetBehavior.this.parentHeight : TsmBottomSheetBehavior.this.collapsedOffset);
        }

        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return child.getLeft();
        }

        public int getViewVerticalDragRange(@NonNull View child) {
            return TsmBottomSheetBehavior.this.hideable ? TsmBottomSheetBehavior.this.parentHeight : TsmBottomSheetBehavior.this.collapsedOffset;
        }
    }
}