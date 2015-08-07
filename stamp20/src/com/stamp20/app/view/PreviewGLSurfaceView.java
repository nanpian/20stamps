package com.stamp20.app.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.stamp20.app.util.GLBaseUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class PreviewGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private int bmpWidth;
    private int bmpHeight;
    private Context mContext;

    public PreviewGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        if (!GLBaseUtil.supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        mContext = context;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setSourceBitmap(Bitmap sourceBitmap) {
        bmpWidth = sourceBitmap.getWidth();
        bmpHeight = sourceBitmap.getHeight();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub

    }

}
