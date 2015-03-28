package com.stamp20.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class RoundNumber extends View {

    private Paint numberPaint, roundPaint, backgroundPaint;
    private int numberColor, roundColor, backgroundColor;
    private String number;
    private float numberSize;

    private float width, height;
    private float mRadius;
    private FontMetrics fontMetrics;
    private float fontTotalHeight;
    private float offY;

    public RoundNumber(Context context) {
        this(context, null);
    }

    public RoundNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundNumber, defStyleAttr, 0);
        backgroundColor = a.getColor(R.styleable.RoundNumber_background, 0);
        mRadius = a.getDimension(R.styleable.RoundNumber_radius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics()));
        roundColor = a.getColor(R.styleable.RoundNumber_cycleColor, 0);
        numberColor = a.getColor(R.styleable.RoundNumber_textColor, 0);
        number = a.getString(R.styleable.RoundNumber_text);
        numberSize = a.getDimension(R.styleable.RoundNumber_textSize, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics()));
        
        if (a != null) {
            a.recycle();
        }

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);

        roundPaint = new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setColor(roundColor);

        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(numberColor);
        numberPaint.setTextSize(numberSize);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans.ttf");
        numberPaint.setTypeface(typeface);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = numberPaint.getFontMetrics();
        fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        offY = fontTotalHeight / 2 - fontMetrics.descent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();
        Log.d(this, "width : " + width + ", height: " + height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);
        canvas.drawCircle(width / 2, height / 2, mRadius, roundPaint);
        canvas.drawText(number, width / 2, height / 2 + offY, numberPaint);
    }

    public void setText(String number) {
        this.number = number;
        invalidate();
    }
}
