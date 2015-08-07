package com.stamp20.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class ScollerRelativeView extends RelativeLayout {

    public interface ScrollFinishListener {
        public void scrollFinish(boolean finish);
    }
    private boolean isScrollFinish = false;
    private boolean isViewBeMoved = false;
    public Scroller mScroller;
    private ScrollFinishListener mScrollFinishListener;

    private String TAG = "ScollerRelativeView";

    public ScollerRelativeView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public ScollerRelativeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public ScollerRelativeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    @Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        // TODO Auto-generated method stub
        return super.checkLayoutParams(p);
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        super.computeScroll();
        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                int oldX = getScrollX();
                int oldY = getScrollY();
                int x = mScroller.getCurrX();
                int y = mScroller.getCurrY();
                if (oldX != x || oldY != y) {
                    scrollTo(x, y);
                }
                // Keep on drawing until the animation has finished.
                invalidate();
            }
        } else {
            Log.i("jiangtao4", "in");
            if (mScrollFinishListener != null) {
                mScrollFinishListener.scrollFinish(true);
            }
        }
    }

    public void init() {
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);

        Log.i(TAG, "onLayout is called");

        if (isViewBeMoved) {
            /*
             * 注释下面的Log,因为会报如下的Exception E/AndroidRuntime(27382): FATAL
             * EXCEPTION: main E/AndroidRuntime(27382): Process:
             * com.stamp20.app, PID: 27382 E/AndroidRuntime(27382):
             * java.lang.NullPointerException E/AndroidRuntime(27382): at
             * com.stamp20
             * .app.view.ScollerRelativeView.onLayout(ScollerRelativeView
             * .java:56) E/AndroidRuntime(27382): at
             * android.view.View.layout(View.java:15331)
             * E/AndroidRuntime(27382): at
             * android.view.ViewGroup.layout(ViewGroup.java:4883)
             * E/AndroidRuntime(27382): at
             * android.widget.RelativeLayout.onLayout(RelativeLayout.java:1160)
             * E/AndroidRuntime(27382): at
             * android.view.View.layout(View.java:15331)
             * E/AndroidRuntime(27382): at
             * android.view.ViewGroup.layout(ViewGroup.java:4883)
             */
            /*
             * Log.i(TAG, "2 view height is : "
             * +getChildAt(1).getMeasuredHeight());
             */
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    continue;
                }

                RelativeLayout.LayoutParams params = (LayoutParams) getChildAt(i).getLayoutParams();
                int leftMargin = params.leftMargin;
                int rightMargin = params.rightMargin;
                int topMargin = params.topMargin;
                Log.i(TAG, "Top is : " + getChildAt(i - 1).getBottom());
                getChildAt(i).layout(leftMargin, getChildAt(i - 1).getBottom() + topMargin,
                        getChildAt(i - 1).getWidth() - rightMargin,
                        getChildAt(i - 1).getBottom() + topMargin + getChildAt(i).getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure is called");
        if (isViewBeMoved) {

        }
    }

    @Override
    public void scrollTo(int x, int y) {
        // TODO Auto-generated method stub
        super.scrollTo(x, y);
        postInvalidate();
    }

    public void setScollListener(ScrollFinishListener listener) {
        mScrollFinishListener = listener;
    }

    public void smoothScollToY(int yDis, int duration) {
        if (mScroller != null) {
            Log.i(TAG, "yDis is : " + yDis + "; duration is : " + duration);

            mScroller.startScroll(getScrollX(), getScrollY(), getScrollX(), yDis, duration);
            isViewBeMoved = true;
            invalidate();
        }
    }
}
