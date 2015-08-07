package com.stamp20.app.opengl.gamesystem;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

public class GlObject {
    // 绘制扇形
    public static void drawArc(GL10 gl, float length, float startAngle, float sweepAngle) {
        int lineCount = (int) (sweepAngle);

        FloatBuffer buffer = FloatBuffer.wrap(new float[] { 0, 0, length, 0, });

        gl.glPushMatrix();
        gl.glRotatef(startAngle, 0, 0, -1);
        for (int i = 0; i < lineCount; ++i) {
            gl.glRotatef(1, 0, 0, -1);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, buffer);
            gl.glDrawArrays(GL10.GL_LINES, 0, 2);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
        gl.glPopMatrix();

    }
    // 绘制直线
    public static void drawLine(GL10 gl, float oneX, float oneY, float twoX, float twoY) {
        FloatBuffer buffer = FloatBuffer.wrap(new float[] { oneX, oneY, twoX, twoY, });

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, buffer);
        gl.glDrawArrays(GL10.GL_LINES, 0, 2);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    // //////////////////////////////////////////////////////////////////////////////////////
    // // 一些常用的图元绘制
    // 绘制正方形
    public static void drawQuater(GL10 gl, float left, float top, float right, float bottom) {
        FloatBuffer buffer = FloatBuffer.wrap(new float[] { left, top, // 左上角
                right, top, // 右上角
                left, bottom, // 左下角
                right, bottom, // 右下角
        });

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, buffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    // 绘制三角形
    public static void drawTriangle(GL10 gl, float oneX, float oneY, float twoX, float twoY, float threeX, float threeY) {
        FloatBuffer buffer = FloatBuffer.wrap(new float[] { oneX, oneY, twoX, twoY, threeX, threeY, });

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, buffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    protected LinkedList<GlObject> children = new LinkedList<GlObject>();// 字节点

    protected GlObject parent = null; // 父节点

    private boolean visible = true; // 可访问（对 gl）

    float x = 0;

    float y = 0;

    // 父子链管理
    public void addChild(GlObject obj) {
        children.add(obj);
        obj.parent = this;
    }

    // 绘制自身
    public void draw(GL10 gl) {

    }

    public GlObject getParent() {
        return parent;
    }

    public boolean getVisible() {
        return visible;
    }

    // 坐标
    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    // 外部gl访问接口
    public void glVisit(GL10 gl) {
        if (!visible)
            return;
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        this.draw(gl);

        for (GlObject child : children)
            child.glVisit(gl);
        gl.glPopMatrix();
    }

    public void removeChild(GlObject obj) {
        obj.parent = null;
        children.remove(obj);
    }

    public void setParent(GlObject p) {
        if (parent != null)
            parent.removeChild(this);
        p.addChild(p);
    }

    // visible
    public void setVisible(boolean v) {
        visible = true;
    }

    void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
