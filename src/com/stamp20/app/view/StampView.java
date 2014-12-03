package com.stamp20.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class StampView extends SurfaceView implements Callback {

    private Paint paint = new Paint();
    private Bitmap bitmap;

    public StampView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public StampView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    public StampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Bitmap bitmap) {
        Log.d(this,"draw()...");
        Canvas canvas = getHolder().lockCanvas();
        canvas.save();
        canvas.drawBitmap(bitmap, getWidth() / 8, getHeight() / 8, null);
        canvas.restore();
        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(this, "surfaceCreated()...");
        if (getBitmap() != null) {
            draw(getBitmap());
        } else {
            Resources res = getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.stamp_bg);
            setBitmap(bmp);
            draw(bmp);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(this, "surfaceChanged()...");
        draw(getBitmap());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(this, "surfaceDestroyed()...");
        // TODO Auto-generated method stub

    }

}
