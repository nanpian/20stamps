package com.stamp20.app.opengl.gamesystem;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.util.Log;

public class AndroidScene extends GlObject {
    Texture2D texture;

    public AndroidScene() {
        super();

        Bitmap androidBitmap = GameSystem.getInstance().getBitmapFromAssets("androida.jpg");
        texture = new Texture2D(androidBitmap);
    }

    public void draw(GL10 gl) {
        texture.draw(gl, 0, 0);
    }
}
