package com.stamp20.app.opengl.gamesystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class MyRenderer implements Renderer{
  
	public void onDrawFrame(GL10 gl) {
		// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// 重置模型矩阵
		gl.glLoadIdentity();
		
		// 游戏系统渲染
		GameSystem.getInstance().glVisit(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {		
		// 设置 OpenGL 场景的大小
		gl.glViewport(0, 0, width, height);
		
		// 设置投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// 重置投影矩阵
		gl.glLoadIdentity();
		// 设置视口的大小
		// gl.glOrthof(0, width, height, 0, -1000, 1000);
		GLU.gluOrtho2D(gl, 0, width, height, 0);
		// 重置模型矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// 告诉系统对透视进行修正
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		// 背景黑色
		gl.glClearColor(0, 0, 0, 1);
		// 启用阴影平滑
		gl.glShadeModel(GL10.GL_SMOOTH);
		// 设置深度缓存
		gl.glClearDepthf(1.0f);
		// 启用深度测试
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// 所做深度测试的类型
		gl.glDepthFunc(GL10.GL_LEQUAL);

	}
	

}
