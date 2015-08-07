package com.stamp20.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.stamp20.app.R;
import com.stamp20.app.util.*;

public class WaitProgressBar extends ProgressBar {

    public WaitProgressBar(Context context) {
        super(context);
        init();
    }

    public WaitProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaitProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        final LayoutParams layoutParams = new LayoutParams(DPIUtil.dip2px(34),
                DPIUtil.dip2px(34));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.setLayoutParams(layoutParams);
        this.setIndeterminateDrawable(this.getResources().getDrawable(
                R.drawable.wait_progress));
        this.setIndeterminate(true);

    }
}
