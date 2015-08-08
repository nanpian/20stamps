package com.stamp20.app.activities;

import javax.microedition.khronos.opengles.GL10;

import com.stamp20.app.R;
import com.stamp20.app.Stamp20Application;
import com.stamp20.app.opengl.gamesystem.GameSystem;
import com.stamp20.app.opengl.gamesystem.GlObject;
import com.stamp20.app.opengl.gamesystem.MyRenderer;
import com.stamp20.app.opengl.gamesystem.Texture2D;
import com.stamp20.app.opengl.toucheventhelper.TouchEventHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

//import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener;

public class OpenGL10ZoomMoveDemo extends Activity {

    MyScene mMyScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stamp20Application.getInstance().addActivity(this);
        // 游戏全屏幕
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 初始化游戏系统:
        GameSystem.getInstance().setMainActivity(this);
        // ... 屏幕大小
        {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            GameSystem.getInstance().setWindowSize(dm.widthPixels, dm.heightPixels);
        }

        mMyScene = new MyScene();

        // ...设定初始场景
        GameSystem.getInstance().setScene(mMyScene);

        // setContentView(R.layout.main);
        Renderer render = new MyRenderer();
        MyGLSurfaceView glView = new MyGLSurfaceView(this, mMyScene);
        glView.setRenderer(render);
        setContentView(glView);
    }

    class MyGLSurfaceView extends GLSurfaceView {
        TouchEventHelper mTouchEventHelper;
        MyScene mMyScene;

        public MyGLSurfaceView(Context context, MyScene m) {
            super(context);
            mMyScene = m;
            mTouchEventHelper = new TouchEventHelper(5.0f, 0.5f, mMyScene.getCanvasWidth(), mMyScene.getCanvasHeight(), mMyScene.getBitmapWidth(),
                    mMyScene.getBitmapHeight(), mMyScene);
        }

        public boolean onTouchEvent(final MotionEvent event) {
            queueEvent(new Runnable() {
                public void run() {
                    mTouchEventHelper.onTouchEvent(event);
                }
            });
            return true;
        }
    }

    class MyScene extends GlObject implements TouchEventHelper.InvalidateCallback {

        Texture2D texture;
        float ratio = 1.0f;
        float translateY = 0f;
        float translateX = 0f;

        public int getCanvasWidth() {
            return GameSystem.getInstance().getWindowWidth();
        }

        public int getCanvasHeight() {
            return GameSystem.getInstance().getWindowHeight();
        }

        public int getBitmapWidth() {
            return texture.getWidth();
        }

        public int getBitmapHeight() {
            return texture.getHeight();
        }

        public MyScene() {
            super();

            Bitmap androidBitmap /*
                                  * =
                                  * GameSystem.getInstance().getBitmapFromAssets
                                  * ("androida.jpg");
                                  */
            = BitmapFactory.decodeResource(getResources(), R.drawable.background_home_birthday);
            texture = new Texture2D(androidBitmap);
        }

        public void draw(GL10 gl) {
            gl.glPushMatrix();
            gl.glTranslatef(translateX, translateY, 0.0f);
            gl.glScalef(ratio, ratio, 1.0f);
            texture.draw(gl, 0, 0);
            gl.glPopMatrix();
        }

        @Override
        public void invalidate(float ratio, float translateX, float translateY) {
            // TODO Auto-generated method stub
            this.ratio = ratio;
            this.translateX = translateX;
            this.translateY = translateY;
        }
    }
}
