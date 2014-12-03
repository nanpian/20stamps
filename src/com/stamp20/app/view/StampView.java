package com.stamp20.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class StampView extends SurfaceView implements Callback {

    private Bitmap bmpStampBackground = null;
    private Bitmap bmpStamp = null;

    public StampView(Context context) {
        super(context);
        getHolder().addCallback(this);
        Resources res = getResources();
        bmpStampBackground = BitmapFactory.decodeResource(res, R.drawable.stamp_bg);
    }

    public StampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        Resources res = getResources();
        bmpStampBackground = BitmapFactory.decodeResource(res, R.drawable.stamp_bg);
    }

    public StampView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
        Resources res = getResources();
        bmpStampBackground = BitmapFactory.decodeResource(res, R.drawable.stamp_bg);
    }

    public Bitmap getBmpStampBackground() {
        return bmpStampBackground;
    }

    public void setBmpStampBackground(Bitmap bmpStampBackground) {
        this.bmpStampBackground = bmpStampBackground;
    }

    public Bitmap getBmpStamp() {
        return bmpStamp;
    }

    public void setBmpStamp(Bitmap bmpStamp) {
        this.bmpStamp = bmpStamp;
    }

    public void draw(Bitmap bitmap) {
        Log.d(this, "draw()...");
        Canvas canvas = getHolder().lockCanvas();
        canvas.save();
        canvas.drawColor(Color.parseColor("#282828"));
        // draw stamp background
        canvas.drawBitmap(getBmpStampBackground(), getWidth() / 8, getHeight() / 8, null);
        //draw stamp
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, getWidth() / 4, getHeight() / 4, null);
        }
        canvas.restore();
        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(this, "surfaceCreated()...");
        draw(getBmpStamp());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(this, "surfaceChanged()...");
        draw(getBmpStamp());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(this, "surfaceDestroyed()...");
    }

}
