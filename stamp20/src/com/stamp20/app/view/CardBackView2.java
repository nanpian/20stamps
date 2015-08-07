package com.stamp20.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Rect;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class CardBackView2 extends View {
    private static final String TAG = "jiangtao";
    private Bitmap mCardbackBitmap;
    private Bitmap mCardbackBottomBitmap;
    private Bitmap mCardbackLineBitmap;
    private int mViewWidth;
    private int mViewHeight;
    private Rect mRectSrc = new Rect();
    private Rect mRectDes = new Rect();
    private Bitmap mSourceBitmap;
    private int mBackColor;
    private boolean mIsShowLine = false;
    private Context mContext;

    public CardBackView2(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CardBackView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CardBackView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int imWidth = 0;
        int imHeight = 0;
        float radio = 0.0f;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mCardbackBitmap != null) {
            imWidth = mCardbackBitmap.getWidth();
            imHeight = mCardbackBitmap.getHeight();
            radio = imWidth * 1.0f / imHeight;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Math.min(widthSize, imWidth + getPaddingLeft() + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.min(heightSize, imHeight + getPaddingTop() + getPaddingBottom());
        }
        if (radio != 0) {
            height = (int) (width * 1.0f / radio);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            Log.i(TAG, "onlayout change");
            // here we get the viewwidth and viewheight
            mViewWidth = getMeasuredWidth();
            mViewHeight = getMeasuredHeight();
            this.mCardbackBitmap = getRoundAlphaBitmap(getAlphaSrcBitmap());
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCardbackBitmap != null) {
            mRectSrc.set(0, 0, mCardbackBitmap.getWidth(), mCardbackBitmap.getHeight());
            mRectDes.set(getPaddingLeft(), getPaddingTop(), mViewWidth - getPaddingLeft() - getPaddingRight(), mViewHeight - getPaddingTop()
                    - getPaddingBottom());
            canvas.drawBitmap(mCardbackBitmap, mRectSrc, mRectDes, null);

            if (mIsShowLine) {
                Matrix martrix = new Matrix();
                Bitmap dstLinebmp = Bitmap.createScaledBitmap(mCardbackLineBitmap, (int) (mViewWidth), (int) (mCardbackLineBitmap.getHeight()), true);
                canvas.drawBitmap(dstLinebmp, (mViewWidth - dstLinebmp.getWidth()) / 2, 0, null);
                dstLinebmp.recycle();
                dstLinebmp = null;
            }
        }
    }

    public void initView() {
        this.mCardbackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_card_back_shape_white);
        this.mCardbackBottomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_card_back_bottom_logo);
        this.mCardbackLineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lines_back_small);
    }

    public void setImageUri(Uri imageUri) {
        mSourceBitmap = ImageUtil.loadDownsampledBitmap(mContext, imageUri, 2);
    }

    public Bitmap getAlphaSrcBitmap() {
        if (mSourceBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        Bitmap cover = BitmapFactory.decodeResource(getResources(), R.drawable.card_back_view_overlay);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAlpha(200);
        Rect srcRect = new Rect(0, 0, mSourceBitmap.getWidth(), mSourceBitmap.getHeight());
        Rect desRect = new Rect(0, 0, mViewWidth, mViewHeight);
        canvas.drawBitmap(mSourceBitmap, srcRect, desRect, null);
        Matrix matrix = new Matrix();
        matrix.setScale(bitmap.getWidth() * 1.0f / cover.getWidth(), bitmap.getHeight() * 1.0f / cover.getHeight());
        canvas.concat(matrix);
        canvas.drawBitmap(cover, 0, 0, paint);

        return bitmap;
    }

    public Bitmap getRoundAlphaBitmap(Bitmap src) {
        if (src == null)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

        Canvas canvas = new Canvas(bitmap);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        // canvas.drawRoundRect(rectF, 100, 100, paint);
        final Rect rectSrc = new Rect(0, 0, mCardbackBitmap.getWidth(), mCardbackBitmap.getHeight());
        canvas.drawBitmap(mCardbackBitmap, rectSrc, rect, null);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, 0, 0, paint);

        int left = 0;
        int top = src.getHeight() - mCardbackBottomBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(src.getWidth() * 1.0f / mCardbackBottomBitmap.getWidth(), 1.0f);
        canvas.concat(matrix);
        canvas.drawBitmap(mCardbackBottomBitmap, left, top, paint);

        return bitmap;
    }

    public Bitmap getRoundCornerBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_4444);
        Paint paint = new Paint();
        paint.setColor(mBackColor);
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

        Canvas canvas = new Canvas(bitmap);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // final RectF rectF = new RectF(rect);
        // canvas.drawRoundRect(rectF, 70, 70, paint);
        // use colormatrix to set the color
        ColorMatrix colorMatrix = new ColorMatrix();
        int r = Color.red(mBackColor);
        int g = Color.green(mBackColor);
        int b = Color.blue(mBackColor);
        colorMatrix.setScale(r * 1.0f / 255, g * 1.0f / 255, b * 1.0f / 255, 1.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        final Rect rectSrc = new Rect(0, 0, mCardbackBitmap.getWidth(), mCardbackBitmap.getHeight());
        canvas.drawBitmap(mCardbackBitmap, rectSrc, rect, paint);
        paint.reset();
        int left = 0;
        int top = mViewHeight - mCardbackBottomBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(mViewWidth * 1.0f / mCardbackBottomBitmap.getWidth(), 1.0f);
        canvas.concat(matrix);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mCardbackBottomBitmap, left, top, paint);

        return bitmap;
    }

    public void setCardBackColor(int color, int position) {
        this.mBackColor = color;
        if (position == 0) {
            this.mCardbackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_card_back_shape_white);
            this.mCardbackBitmap = getRoundAlphaBitmap(getAlphaSrcBitmap());
        } else {
            this.mCardbackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_card_back_shape_white);
            this.mCardbackBitmap = getRoundCornerBitmap();
        }
    }

    public void setHasLine(boolean b) {
        mIsShowLine = b;
        this.mCardbackLineBitmap = getScaleLineBitmap();
    }

    public Bitmap getScaleLineBitmap() {
        Bitmap bitmap = null;
        if (mViewHeight != 0 && mCardbackLineBitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setScale(1.0f, mViewHeight * 1.0f / mCardbackLineBitmap.getHeight());
            bitmap = Bitmap.createBitmap(mCardbackLineBitmap, 0, 0, mCardbackLineBitmap.getWidth(), mCardbackLineBitmap.getHeight(), matrix, false);
        }
        return bitmap;
    }
}
