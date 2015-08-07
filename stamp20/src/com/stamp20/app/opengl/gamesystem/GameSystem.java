package com.stamp20.app.opengl.gamesystem;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameSystem {
    private static GameSystem instance_ = new GameSystem();

    public static GameSystem getInstance() {
        return instance_;
    }

    private int height = 0;

    // activity
    private Activity mainActivity;

    // 场景
    private GlObject runningScene_ = null;

    private int width = 0; // opengl的场景尺寸

    // util
    Bitmap getBitmapFromAssets(String file) {
        Bitmap image = null;
        try {
            AssetManager am = mainActivity.getAssets();
            InputStream is = am.open(file);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {

        }
        return image;
    }
    public Activity getMainActivity() {
        return mainActivity;
    }
    public int getWindowHeight() {
        return this.height;
    }

    public int getWindowWidth() {
        return this.width;
    }

    //
    public void glVisit(GL10 gl) {
        if (runningScene_ != null)
            runningScene_.glVisit(gl);
    }

    public void setMainActivity(Activity act) {
        mainActivity = act;
    }

    // 设定场景
    public void setScene(GlObject scene) {
        runningScene_ = scene;
    }

    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
