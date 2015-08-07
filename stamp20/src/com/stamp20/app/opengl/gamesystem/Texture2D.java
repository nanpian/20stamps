package com.stamp20.app.opengl.gamesystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLUtils;

public class Texture2D {
    public static int pow2(int size) {
        int small = (int) (Math.log(size) / Math.log(2.0f));
        if ((1 << small) >= size)
            return 1 << small;
        else
            return 1 << (small + 1);
    }
    private float maxU = 1.0f;
    private float maxV = 1.0f;
    private Bitmap mBitmap = null;
    private int mHeight;
    private int mPow2Height;

    private int mPow2Width;

    private int mWidth;

    private int textureId = 0;

    // 构建，推迟到第一次绑定时
    public Texture2D(Bitmap bmp) {
        // mBitmap = bmp;
        mWidth = bmp.getWidth();
        mHeight = bmp.getHeight();

        mPow2Height = pow2(mHeight);
        mPow2Width = pow2(mWidth);

        if (mWidth == mPow2Width && mHeight == mPow2Height)
            return;

        maxU = mWidth / (float) mPow2Width;
        maxV = mHeight / (float) mPow2Height;

        Bitmap bitmap = Bitmap.createBitmap(mPow2Width, mPow2Height, bmp.hasAlpha() ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, 0, 0, null);
        mBitmap = bitmap;
    }

    // 第一次会加载纹理数据
    public void bind(GL10 gl) {
        if (textureId == 0) {
            int[] textures = new int[1];
            gl.glGenTextures(1, textures, 0);
            textureId = textures[0];

            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);

            mBitmap.recycle();
            mBitmap = null;
        }

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    // 删除纹理数据
    public void delete(GL10 gl) {
        if (textureId != 0) {
            gl.glDeleteTextures(1, new int[] { textureId }, 0);
            textureId = 0;
        }

        // bitmap
        if (mBitmap != null) {
            if (mBitmap.isRecycled())
                mBitmap.recycle();
            mBitmap = null;
        }

    }

    // 绘制到屏幕上
    public void draw(GL10 gl, float x, float y) {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //  绑定
        this.bind(gl);

        // 映射
        FloatBuffer verticleBuffer = floatBufferUtil(new float[] { x, y, x + mWidth, 0, x, y + mHeight, x + mWidth,
                y + mHeight, });
        FloatBuffer coordBuffer = floatBufferUtil(new float[] { 0, 0, maxU, 0, 0, maxV, maxU, maxV, });

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticleBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    public void draw(GL10 gl, float x, float y, float width, float height) {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //  绑定
        bind(gl);

        // 映射
        // 映射
        FloatBuffer verticleBuffer = floatBufferUtil(new float[] { x, y, x + width, 0, x, y + height, x + width,
                y + height, });
        FloatBuffer coordBuffer = floatBufferUtil(new float[] { 0, 0, maxU, 0, 0, maxV, maxU, maxV, });

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticleBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);

    }

    // 定义一个工具方法，将float[]数组转换为OpenGL ES所需的FloatBuffer
    private FloatBuffer floatBufferUtil(float[] arr) {
        FloatBuffer mBuffer;
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mBuffer = qbb.asFloatBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }
}
