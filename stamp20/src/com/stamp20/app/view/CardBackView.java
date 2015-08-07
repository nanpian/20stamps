package com.stamp20.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class CardBackView extends View {

    public interface onGeneratedCardBackBmpListener {
        public void onGeneratedCardBackBmp();
    }
    public interface onMeasuredListener {
        public void onMeasuredListener(int width, int height);
    }
    private static final String Tag = "CardBackView";
    // 背景画笔
    Paint backgroundPaint = new Paint();
    // 背景颜色
    public int cardBackColor;
    // 这是背景形状图片
    private Bitmap cardBackShape, cardBackBitmap, cardBackLineBitmap, cardBackBottomLogo;
    // 整个view的宽度和高度
    private int cardBackWidth, cardBackHeight, logoWidth, logoHeight;
    private boolean isCaptureBmp = false;
    private boolean isHasLine = false;
    public onMeasuredListener listener = null;
    public onGeneratedCardBackBmpListener listener2 = null;

    private Rect mImageRect;

    // 第一次初始化
    private boolean mInitialized = true;

    private Bitmap mSourceBitmap;

    public CardBackView(Context context) {
        super(context);
        initView();
    }

    public CardBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public Bitmap colorBitmap(Bitmap bitmapSource, int color) {
        int width = bitmapSource.getWidth();
        int height = bitmapSource.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        Color.alpha(color);
        colorMatrix.setScale(r * 1.0f / 255, g * 1.0f / 255, b * 1.0f / 255, 1.0f);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmapSource, 0, 0, paint);
        paint.reset();
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(width * 1.0f / logoWidth, 1.0f);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int left = 0;
        int top = height - logoHeight;
        canvas.concat(scaleMatrix);
        canvas.drawBitmap(cardBackBottomLogo, left, top, paint);
        mInitialized = true;
        return bmp;
    }

    private Bitmap createWhiteBitmap(Bitmap cardBackBitmapSource) {
        int mBitmapHeight = cardBackBitmapSource.getHeight();
        int mBitmapWidth = cardBackBitmapSource.getWidth();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int mArrayColor[] = new int[mArrayColorLengh];
        int count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                // 获得Bitmap 图片中每一个点的color颜色值
                int color = cardBackBitmapSource.getPixel(j, i);
                // 将颜色值存在一个数组中 方便后面修改
                /*
                 * int r = Color.red(color); int g = Color.green(color); int b =
                 * Color.blue(color);
                 */
                int alpha = Color.alpha(color);
                // 如果透明保持，不透明变为白色
                if (alpha < 120) {
                    mArrayColor[count] = color;
                } else {
                    mArrayColor[count] = Color.WHITE;
                }
                count++;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.ARGB_4444);
        newBmp.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth, mBitmapHeight);

        cardBackBitmap = newBmp;
        return newBmp;
    }

    public void generateCardBackBmp() {

    }

    public Bitmap getAlphaSrcBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mSourceBitmap.getWidth(), mSourceBitmap.getHeight(),
                mSourceBitmap.getConfig());
        Bitmap cover = BitmapFactory.decodeResource(getResources(), R.drawable.card_back_view_overlay);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAlpha(200);
        canvas.drawBitmap(mSourceBitmap, 0, 0, null);
        Matrix matrix = new Matrix();
        matrix.setScale(bitmap.getWidth() * 1.0f / cover.getWidth(), bitmap.getHeight() * 1.0f / cover.getHeight());
        canvas.concat(matrix);
        canvas.drawBitmap(cover, 0, 0, paint);

        return bitmap;
    }

    private void initView() {
        Log.i(Tag, "init View ");
        this.cardBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_card_back_shape);
        this.cardBackShape = BitmapFactory.decodeResource(getResources(), R.drawable.activity_card_back_shape);
        if (listener != null) {
            // listener.onMeasuredListener(cardBackShape.getWidth(),
            // cardBackShape.getHeight());
        }
        this.cardBackBottomLogo = BitmapFactory.decodeResource(getResources(),
                R.drawable.activity_card_back_bottom_logo);
        this.cardBackLineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lines_back_small);
        logoWidth = cardBackBottomLogo.getWidth();
        logoHeight = cardBackBottomLogo.getHeight();
        if (cardBackBitmap != null) {

        }
        this.cardBackColor = Color.WHITE;

        // this.cardBackBitmap = createWhiteBitmap(cardBackBitmap);
        // this.cardBackBitmap = colorBitmap(cardBackBitmap, Color.WHITE);

        // test color mask function
        // this.cardBackBitmap = maskWithColor(cardBackBitmap, Color.GREEN);
    }

    public Bitmap maskWithColor(Bitmap cardBackBitmapSource, int maskcolor) {
        int mBitmapHeight = cardBackBitmapSource.getHeight();
        int mBitmapWidth = cardBackBitmapSource.getWidth();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int mArrayColor[] = new int[mArrayColorLengh];
        int count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                // 获得Bitmap 图片中每一个点的color颜色值
                int color = cardBackBitmapSource.getPixel(j, i);
                Color.red(color);
                Color.green(color);
                Color.blue(color);
                int alpha = Color.alpha(color);
                if (i > mBitmapHeight - logoHeight) {
                    // 如果透明保持，不透明变为白色
                    if (alpha < 120) {
                        mArrayColor[count] = color;
                    } else {
                        mArrayColor[count] = Color.WHITE;
                    }
                } else {
                    // 如果透明保持，不透明变为白色
                    if (alpha < 120) {
                        mArrayColor[count] = color;
                    } else {
                        mArrayColor[count] = maskcolor;
                    }
                }
                count++;
            }
        }

        Bitmap newBmp = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.ARGB_4444);
        newBmp.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth, mBitmapHeight);
        mInitialized = true;
        return newBmp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mInitialized) {
            Paint paintShape = new Paint();

            paintShape.setColor(cardBackColor);
            if (cardBackWidth > logoWidth) {
            } else {
            }
            cardBackBitmap.getHeight();
            cardBackBitmap.getHeight();

            // draw the shape background
            canvas.drawBitmap(cardBackBitmap, null,
                    new Rect(getPaddingLeft(), (cardBackHeight - cardBackBitmap.getHeight()) / 2, cardBackWidth
                            - getPaddingRight(), cardBackHeight), paintShape);

            // draw the bottom 20stamp logo ,first we get the location the logo
            // canvas.drawBitmap(cardBackBottomLogo, left, top, null);

            // draw the line if it exists
            if (isHasLine) {
                canvas.drawBitmap(cardBackLineBitmap, (cardBackWidth - cardBackLineBitmap.getWidth()) / 2, 20, null);
            }

            // mInitialized = false;

            /*
             * if (isCaptureBmp) { Bitmap generatedCardBackBmp =
             * Bitmap.createBitmap(cardBackWidth,cardBackHeight,
             * Bitmap.Config.ARGB_4444); canvas.setBitmap(generatedCardBackBmp);
             * CardBmpCache bmpCache = CardBmpCache.getCacheInstance();
             * bmpCache.putBack(generatedCardBackBmp);
             * listener2.onGeneratedCardBackBmp(); }
             */
        } else {

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(this, "onLayout...");
        cardBackWidth = getMeasuredWidth();
        cardBackHeight = getMeasuredHeight();
        Log.i("jiangtao4", "cardBackWidth is : " + cardBackWidth + "cardBackHeight is : " + cardBackHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int imWidth = 0;
        int imHeight = 0;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (cardBackBitmap != null) {
            imWidth = cardBackBitmap.getWidth();
            imHeight = cardBackBitmap.getHeight();
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
        setMeasuredDimension(width, height);
    }

    public void setCaptureBmp(boolean isCp) {
        isCaptureBmp = true;
    }

    public void setCardBackBitmap(Bitmap cardBitmap) {
        this.cardBackBitmap = cardBitmap;
    }

    public void setCardBackColor(int cardcolor) {
        this.cardBackColor = cardcolor;
        // this.cardBackBitmap = maskWithColor(cardBackBitmap, cardcolor);
        this.cardBackBitmap = createWhiteBitmap(cardBackBitmap);
        this.cardBackBitmap = colorBitmap(cardBackBitmap, cardcolor);
    }

    public void setCardBackLine(boolean istrue, Bitmap cardBackLineBitmap) {

    }

    public void setHasLine(boolean b) {
        isHasLine = b;
        mInitialized = true;
    }

    public void setImageUri(Uri imageUri) {
        mSourceBitmap = ImageUtil.loadDownsampledBitmap(getContext(), imageUri, 2);
        this.cardBackBitmap = getAlphaSrcBitmap();
    }

    public void setonGeneratedCardBackBmpListener(onGeneratedCardBackBmpListener l) {
        this.listener2 = l;
    }

    public void setOnMeasuredListener(onMeasuredListener l) {
        this.listener = l;
    }

}
