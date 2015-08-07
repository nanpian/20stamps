/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stamp20.app.activities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class TextureRenderer {

    private static final int FLOAT_SIZE_BYTES = 4;
    private static final String FRAGMENT_SHADER = "precision mediump float;\n" + "uniform sampler2D tex_sampler;\n"
            + "varying vec2 v_texcoord;\n" + "void main() {\n"
            + "  gl_FragColor = texture2D(tex_sampler, v_texcoord);\n" + "}\n";
    private static final float[] POS_VERTICES = { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
    private static final String Tag = "TextureRender";

    private static final float[] TEX_VERTICES = { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
    private static final String VERTEX_SHADER = "attribute vec4 a_position;\n" + "attribute vec2 a_texcoord;\n"
            + "varying vec2 v_texcoord;\n" + "void main() {\n" + "  gl_Position = a_position;\n"
            + "  v_texcoord = a_texcoord;\n" + "}\n";

    private int mPosCoordHandle;
    private FloatBuffer mPosVertices;

    private int mProgram;
    private int mstampFrameHeight;
    private int mstampFrameWidth;
    private int mTexCoordHandle;

    private int mTexHeight;

    private int mTexSamplerHandle;

    private FloatBuffer mTexVertices;

    private int mTexWidth;

    private int mViewHeight;
    private int mViewWidth;

    private void computeOutputVertices() {
        if (mPosVertices != null) {
            float imgAspectRatio = mTexWidth / (float) mTexHeight;
            float viewAspectRatio = mViewWidth / (float) mViewHeight;
            float relativeAspectRatio = viewAspectRatio / imgAspectRatio;
            float x0, y0, x1, y1;
            if (relativeAspectRatio > 1.0f) {
                // x0 = -1.0f / relativeAspectRatio;
                x0 = -1.0f * mstampFrameWidth / mViewWidth;
                // x0 =
                // -1.0f*(float)mstampFrameWidth*relativeAspectRatio/(float)mViewWidth;
                y0 = -1.0f * mstampFrameWidth * relativeAspectRatio / mViewWidth;
                x1 = 1.0f * mstampFrameWidth / mViewWidth;
                // x1 = 1.0f;
                y1 = mstampFrameWidth * relativeAspectRatio / mViewWidth;
                Log.i(Tag, ">1, mstampFrameWidth is " + mstampFrameWidth + " mViewWidth is " + mViewWidth + "x0 is "
                        + x0 + " y0 is" + y0 + " x1 is" + x1 + " y1 is" + y1);
            } else {
                x0 = -1.0f * mstampFrameWidth / mViewWidth;
                // y0 = -relativeAspectRatio;
                y0 = -1.0f * mstampFrameWidth * relativeAspectRatio / mViewWidth;
                x1 = 1.0f * mstampFrameWidth / mViewWidth;
                y1 = 1.0f * mstampFrameWidth * relativeAspectRatio / mViewWidth;
                // y1 = relativeAspectRatio;
                Log.i(Tag, "<1, mstampFrameWidth is " + mstampFrameWidth + " mViewWidth is " + mViewWidth + "x0 is "
                        + x0 + " y0 is" + y0 + " x1 is" + x1 + " y1 is" + y1);
            }
            float[] coords = new float[] { x0, y0, x1, y0, x0, y1, x1, y1 };
            mPosVertices.put(coords).position(0);
        }
    }

    public void init() {
        // Create program
        mProgram = GLToolbox.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        // Bind attributes and uniforms
        mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "tex_sampler");
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texcoord");
        mPosCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_position");

        // Setup coordinate buffers
        mTexVertices = ByteBuffer.allocateDirect(TEX_VERTICES.length * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTexVertices.put(TEX_VERTICES).position(0);
        mPosVertices = ByteBuffer.allocateDirect(POS_VERTICES.length * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mPosVertices.put(POS_VERTICES).position(0);
    }

    public void renderTexture(int texId) {
        // Bind default FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // Use our shader program
        GLES20.glUseProgram(mProgram);
        GLToolbox.checkGlError("glUseProgram");

        // Set viewport
        GLES20.glViewport(0, 0, mViewWidth, mViewHeight);
        GLToolbox.checkGlError("glViewport");

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);

        // Set the vertex attributes
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTexVertices);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mPosCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mPosVertices);
        GLES20.glEnableVertexAttribArray(mPosCoordHandle);
        GLToolbox.checkGlError("vertex attribute setup");

        // Set the input texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLToolbox.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(mTexSamplerHandle, 0);

        // Draw
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void renderTexture(int texId, float ratio) {
        // Bind default FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // Use our shader program
        GLES20.glUseProgram(mProgram);
        GLToolbox.checkGlError("glUseProgram");

        // Set viewport
        GLES20.glViewport(0, 0, (int) (mViewWidth * ratio / 2), (int) (mViewHeight * ratio / 2));
        GLToolbox.checkGlError("glViewport");

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);

        // Set the vertex attributes
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTexVertices);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mPosCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mPosVertices);
        GLES20.glEnableVertexAttribArray(mPosCoordHandle);
        GLToolbox.checkGlError("vertex attribute setup");

        // Set the input texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLToolbox.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(mTexSamplerHandle, 0);

        // Draw
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void renderTexture(int texId, float totalRatio, int totalTranslateX, int totalTranslateY) {
        // TODO Auto-generated method stub
        // Bind default FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // Use our shader program
        GLES20.glUseProgram(mProgram);
        GLToolbox.checkGlError("glUseProgram");

        // Set viewport
        Log.i(Tag, "total translate x is " + totalTranslateX + "total translate y is " + totalTranslateY
                + " totalratio is " + totalRatio);
        GLES20.glViewport(totalTranslateX, -totalTranslateY, (int) (mViewWidth * totalRatio),
                (int) (mViewHeight * totalRatio));
        GLToolbox.checkGlError("glViewport");

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);

        // Set the vertex attributes
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTexVertices);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mPosCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mPosVertices);
        GLES20.glEnableVertexAttribArray(mPosCoordHandle);
        GLToolbox.checkGlError("vertex attribute setup");

        // Set the input texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLToolbox.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(mTexSamplerHandle, 0);

        // Draw
        // Background color value ff282828
        // GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearColor(0.15686f, 0.15686f, 0.15686f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void tearDown() {
        GLES20.glDeleteProgram(mProgram);
    }

    public void updateTextureSize(int texWidth, int texHeight) {
        mTexWidth = texWidth;
        mTexHeight = texHeight;
        computeOutputVertices();
    }

    public void updateTextureSize(int mImageWidth, int mImageHeight, int stampFrameWidth, int stampFrameHeight) {
        // TODO Auto-generated method stub
        mstampFrameWidth = stampFrameWidth;
        mstampFrameHeight = stampFrameHeight;

    }

    public void updateViewSize(int viewWidth, int viewHeight) {
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        // computeOutputVertices();
    }

}
