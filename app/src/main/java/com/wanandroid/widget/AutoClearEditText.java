package com.wanandroid.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义带删除图标的EditText控件,使用android:drawableRight属性设置删除图标
 */
public class AutoClearEditText extends android.support.v7.widget.AppCompatEditText {

    private Drawable mClearDrawable;

    private OnClearTextListener mOnClearTextListener;

    public AutoClearEditText(Context context) {
        super(context);
        initClearDrawable();
    }

    public AutoClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClearDrawable();
    }

    public AutoClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClearDrawable();
    }

    public void setOnClearTextListener(OnClearTextListener onClearTextListener) {
        mOnClearTextListener = onClearTextListener;
    }

    private void initClearDrawable() {
        Drawable[] drawables = getCompoundDrawables();
        //在这里获取删除图标
        if (mClearDrawable == null) {
            mClearDrawable = drawables[2];
        }
        //默认不显示清空按钮
        setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mClearDrawable == null) {
            return;
        }
        Drawable[] drawables = getCompoundDrawables();
        if (getText().length() > 0) {
            setCompoundDrawables(drawables[0], drawables[1], mClearDrawable, drawables[3]);
        } else {
            setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && mClearDrawable != null) {
            float x = event.getX();
            if (x < getWidth() - getPaddingRight() && x > getWidth() - getTotalPaddingRight()) {
                setText("");
                if (mOnClearTextListener != null) {
                    mOnClearTextListener.onClearText();
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当EditText清空按钮被点击的回调
     */
    public interface OnClearTextListener {
        void onClearText();
    }
}
