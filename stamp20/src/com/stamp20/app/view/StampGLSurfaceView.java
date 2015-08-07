package com.stamp20.app.view;

import static javax.microedition.khronos.opengles.GL10.GL_RGBA;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stamp20.app.R;
import com.stamp20.app.activities.GLToolbox;
import com.stamp20.app.activities.TextureRenderer;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.GLBaseUtil;
import com.stamp20.app.util.Log;

public class StampGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    public interface ChangeUIInterface {
        public void changeStampFrame(Bitmap bitmap);

        public void changeStampFrame(int resId);
    }

    public interface OnMoveOrZoomListener {
        public void onMoveOrZoomListener(boolean flag, float ratio, float currentRatio);
    }

    public interface OnStampBitmapGeneratedListener {
        public void OnStampBitmapGeneratedListener();
    }
    private static final int INIT_FRAME = 8;
    private static EffectContext mEffectContext;
    public static final int STATUS_CAPTURE = 6;
    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;
    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_MOVE = 4;
    /**
     * 松开手指状态常量
     */
    public static final int STATUS_NONE = 5;
    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;
    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;
    private static final String Tag = "StampGLSurfaceView";
    private static final int UPDATE_FRAME = 7;
    // the stamp bitmap finally produce
    private Bitmap bitmap;
    private float centerPointX;
    private float centerPointY;
    ChangeUIInterface changeUiInterface = null;
    private int currentfilterID = 0;
    private String currentfiltername;

    /**
     * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
     */
    private int currentStatus;

    private int deltaH = 15;
    private int deltaW = 15;

    private ImageEffectAdapter effectAdapter;

    private Bitmap glBitmap;

    /**
     * 记录图片初始化时的缩放比例
     */
    private float initRatio;

    /**
     * 当前邮票是横屏还是竖屏界面
     */
    public boolean isHorizontal = true;

    /**
     * 记录上次两指之间的距离
     */
    private double lastFingerDis;

    /**
     * 记录上次手指移动时的横坐标
     */
    private float lastXMove = -1;

    /**
     * 记录上次手指移动时的纵坐标
     */
    private float lastYMove = -1;

    OnMoveOrZoomListener listener = null;

    public Context mContext;

    private Effect mCurrentEffect = null;

    /**
     * 选好照片后生成一个Design
     */
    private Design mDesign = Design.getInstance();

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == UPDATE_FRAME) {
                // MainEffect.instance.mStampFrame.setImageBitmap(bitmap);
                changeUiInterface.changeStampFrame(bitmap);
            } else if (msg.what == INIT_FRAME) {
                if (isHorizontal) {
                    changeUiInterface.changeStampFrame(R.drawable.background_stamp_h_transparent_pierced);
                } else {
                    changeUiInterface.changeStampFrame(R.drawable.background_stamp_v_transparent_pierced);

                }
            }
        }

    };

    private int mHeight;

    private int mImageHeight;

    private int mImageWidth;

    private boolean mInitialized = false;

    /**
     * 记录手指在横坐标方向上的移动距离
     */
    private float movedDistanceX;

    /**
     * 记录手指在纵坐标方向上的移动距离
     */
    private float movedDistanceY;

    private TextureRenderer mTexRenderer = new TextureRenderer();

    private int[] mTextures = new int[2];

    private int mWidth;

    private ImageView rotateButton;
    /**
     * 记录手指移动的距离所造成的缩放比例
     */
    private float scaledRatio;

    private Bitmap sourceBitmap;

    private ImageView stampFrame;

    private Bitmap stampFrameBitmap;

    private int stampFrameHeight;

    private int stampFrameWidth;

    OnStampBitmapGeneratedListener stamplistener = null;

    private int surfaceHeight;

    private int surfaceWidth;

    /**
     * 记录图片在矩阵上的总缩放比例
     */
    private float totalRatio;

    /**
     * 记录图片在矩阵上的横向偏移值
     */
    private float totalTranslateX;

    /**
     * 记录图片在矩阵上的纵向偏移值
     */
    private float totalTranslateY;

    public StampGLSurfaceView(Context context) {
        this(context, null);
        // if (!supportsOpenGLES2(context)) {
        // throw new
        // IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        // }
        // mContext = context;
        // this.setEGLContextClientVersion(2);
        // this.setRenderer(this);
        // this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public StampGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!GLBaseUtil.supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        mContext = context;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mDesign.setOrientation(isHorizontal ? "H" : "V");
        mDesign.setUnitPrice(getDefaultPrice());
    }

    private void applyEffect() {
        mCurrentEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    /**
     * 计算两个手指之间中心点的坐标。
     * 
     * @param event
     */
    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        centerPointX = (xPoint0 + xPoint1) / 2;
        centerPointY = (yPoint0 + yPoint1) / 2;
    }

    /**
     * 计算两个手指之间的距离。
     * 
     * @param event
     * @return 两个手指之间的距离
     */
    private double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    public void generateStamp(GL10 mGL) {
        // 得到GLSurfaceView图片后，要进行叠加运算
        Bitmap frameBitmap = null;
        if (isHorizontal) {
            frameBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.background_stamp_h_transparent_pierced);
        } else {
            frameBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.background_stamp_v_transparent_pierced);
        }
        // deltaW为白色边框的条宽度
        mWidth = frameBitmap.getWidth();
        mHeight = frameBitmap.getHeight();

        int[] iat = new int[(mWidth - 2 * deltaW) * (mHeight - 2 * deltaH)];
        IntBuffer ib = IntBuffer.allocate((mWidth - 2 * deltaW) * (mHeight - 2 * deltaH));
        mGL.glReadPixels((surfaceWidth - mWidth) / 2 + deltaW, (surfaceHeight - mHeight) / 2 + deltaH, mWidth - 2
                * deltaW, mHeight - 2 * deltaH, GL_RGBA, GL_UNSIGNED_BYTE, ib);
        int[] ia = ib.array();

        // Convert upside down mirror-reversed image to right-side up normal
        // image.
        int tempHeight = mHeight - 2 * deltaH;
        int tempWidth = mWidth - 2 * deltaW;
        for (int i = 0; i < tempHeight; i++) {
            for (int j = 0; j < tempWidth; j++) {
                iat[(tempHeight - i - 1) * tempWidth + j] = ia[i * tempWidth + j];
            }
        }

        glBitmap = Bitmap.createBitmap((mWidth - 2 * deltaW), (mHeight - 2 * deltaH), Bitmap.Config.ARGB_8888);
        glBitmap.copyPixelsFromBuffer(IntBuffer.wrap(iat));
        if (glBitmap != null) {
            Log.i(Tag, "onDrawFrame,glBitmap width is " + glBitmap.getWidth());
        } else {
            Log.i(Tag, "onDrawFrame,glBitmap is null");
        }

        bitmap = Bitmap.createBitmap(frameBitmap.getWidth(), frameBitmap.getHeight(), Config.ARGB_8888);
        try {
            Canvas cv = new Canvas(bitmap);
            // Notice!!! the delta w and delta h -5 is set manually!!!
            cv.drawBitmap(glBitmap, deltaW, deltaH, null);
            cv.drawBitmap(frameBitmap, 0, 0, null);

            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
            frameBitmap.recycle();
            frameBitmap = null;
            glBitmap.recycle();
            glBitmap = null;
        } catch (Exception e) {
            Log.i("zhudewei", "onDrawFrame, the bitmap exception");
            bitmap = null;
            e.getStackTrace();
        }
        Log.i("zhudewei", "onDrawFrame, the bitmap is " + bitmap);
        BitmapCache mCache = BitmapCache.getCache();
        mCache.put(bitmap);
    }

    public int getCurrentfilterID() {
        return currentfilterID;
    }

    public String getCurrentfiltername() {
        return currentfiltername;
    }

    private int getDefaultPrice() {
        int[] price = getResources().getIntArray(R.array.stamp_rate_price);
        return price[0];
    }

    public ImageEffectAdapter getEffectAdapter() {
        return effectAdapter;
    }

    public ImageView getRotateButton() {
        return rotateButton;
    }

    public Bitmap getSourceBitmap() {
        return sourceBitmap;
    }

    public ImageView getStampFrame() {
        return stampFrame;
    }

    private void loadTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);

        // Load input bitmap
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
        // R.drawable.puppy);
        if (sourceBitmap == null)
            return;
        mImageWidth = sourceBitmap.getWidth();
        mImageHeight = sourceBitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight, stampFrameWidth, stampFrameHeight);
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        /*
         * float translateX = (surfaceWidth- (stampFrameWidth * 1)) / 2f; float
         * translateY = (surfaceHeight - (stampFrameHeight * 1)) / 2f;
         * totalTranslateX = translateX; totalTranslateY = translateY;
         */

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, sourceBitmap, 0);

        // Set texture parameters
        GLToolbox.initTexParams();
    }

    private void moveGLSurfaceView() {
        // TODO Auto-generated method stub
        float translateX = totalTranslateX + movedDistanceX;
        Log.d(this, "move,totalX:" + totalTranslateX + ", movedX:" + movedDistanceX + ", X:" + translateX);
        float translateY = totalTranslateY + movedDistanceY;

        totalTranslateX = translateX;
        totalTranslateY = translateY;

        // totalTranslateX = translateX*totalRatio;
        // totalTranslateY = translateY*totalRatio;

        this.requestRender();
    }

    private void notifyStampGernerated() {
        // TODO Auto-generated method stub
        stamplistener.OnStampBitmapGeneratedListener();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        if (!mInitialized) {
            // Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            Log.i(Tag, "mInitialized is : " + mInitialized);
            mInitialized = true;

        }

        if (currentStatus == STATUS_INIT || currentStatus == STATUS_NONE || currentStatus == STATUS_CAPTURE) {
            // if an effect is chosen initialize it and apply it to the texture
            if (currentfilterID != 0) {
                Log.i(Tag, "onDrawFrame the filter name is " + currentfiltername + "the currentfilterID is : "
                        + currentfilterID);
                try {
                    mCurrentEffect = effectAdapter.createEffect(currentfilterID, mEffectContext);
                    applyEffect();
                } catch (Exception e) {
                    Log.i(Tag, "Exception is : " + e);
                    mCurrentEffect = null;
                }
            } else {
                // when currentfilterID is 0, we need to set the testure to
                // normal
                mCurrentEffect = null;
            }
            renderResult();
            synchronized (this) {
                generateStamp(gl);
                mHandler.sendEmptyMessage(UPDATE_FRAME);
            }
            GLES20.glClearColor(0.15686f, 0.15686f, 0.15686f, 1.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            if (currentStatus == STATUS_CAPTURE) {
                Log.i(Tag, "onDrawFrame , generateStamp the gl is " + gl);
                if (stamplistener != null) {
                    notifyStampGernerated();
                    Log.i(Tag, "onDrawFrame ,notify stamp generated!");
                }
            }
        } else {
            mHandler.sendEmptyMessage(INIT_FRAME);

            gl.glLoadIdentity(); // 重置当前的模型观察矩阵
            gl.glPushMatrix();
            try {
                renderResult();
            } catch (Exception e) {
                try {
                    renderResult();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            gl.glPopMatrix();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // when resume, we need to initialize the GL
        // GLSurfaceView clients
        // are required to call {@link #onPause()} when the activity pauses and
        // {@link #onResume()} when the activity resumes. These calls allow
        // GLSurfaceView to
        // pause and resume the rendering thread, and also allow GLSurfaceView
        // to release and recreate
        // the OpenGL display.
        Log.i(Tag, "onresume mInitialized is : " + mInitialized);
        mInitialized = false;
    }

    public void onRotateClick(Context mContext, RelativeLayout frameLayout) {
        // TODO Auto-generated method stub
        isHorizontal = !isHorizontal;
        mDesign.setOrientation(isHorizontal ? "H" : "V");
        if (isHorizontal) {
            showAnimation(frameLayout, true);
            // 旋转后，重新刷新GLSurfaceView
            // this.requestRender();
        } else {
            showAnimation(frameLayout, false);
            // this.requestRender();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(width, height);
        }
        surfaceWidth = width;
        surfaceHeight = height;
        Log.i(Tag, "onSurfaceChanged, the view width is " + width + "the view height is " + height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
        // TODO Auto-generated method stub
        // gl.glDisable(GL10.GL_DITHER);
        // gl.glEnable(GL10.GL_DEPTH_TEST);
    }

    private void onTouchDown(MotionEvent event) {
        event.getX();
        event.getY();
    }

    private void renderResult() {
        if (mCurrentEffect != null) {
            // if no effect is chosen, just render the original bitmap
            Log.i(Tag, "the render result is not null");
            mTexRenderer.renderTexture(mTextures[1], totalRatio, (int) totalTranslateX, (int) totalTranslateY);
        } else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0], totalRatio, (int) totalTranslateX, (int) totalTranslateY);
            Log.i(Tag, "the render result is null");
        }
    }

    public void setChangeUIInterface(ChangeUIInterface uiInterface) {
        this.changeUiInterface = uiInterface;
    }

    public void setCurrentfilterID(int currentfilterID) {
        this.currentfilterID = currentfilterID;
    }

    public void setCurrentfiltername(String currentfiltername) {
        this.currentfiltername = currentfiltername;
    }

    public void setEffectAdapter(ImageEffectAdapter effectAdapter) {
        this.effectAdapter = effectAdapter;
    }

    public void setOnMoveOrZoomListener(OnMoveOrZoomListener l) {
        listener = l;
    }

    public void setOnStampBitmapGeneratedListener(OnStampBitmapGeneratedListener stamplistener2) {
        stamplistener = stamplistener2;
    }

    public void setRotateButton(ImageView rotateButton) {
        this.rotateButton = rotateButton;
    }

    public void setSourceBitmap(Bitmap sourceBitmap) {

        currentStatus = STATUS_INIT;
        if (this.sourceBitmap != null) {
            this.sourceBitmap.recycle();
            this.sourceBitmap = null;
        }
        this.sourceBitmap = sourceBitmap;

        int bitmapWidth = sourceBitmap.getWidth();
        int bitmapHeight = sourceBitmap.getHeight();
        stampFrameBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.background_stamp_h_transparent_pierced);
        stampFrameWidth = stampFrameBitmap.getWidth();
        stampFrameHeight = stampFrameBitmap.getHeight();
        // recycle stamp frame memory.
        stampFrameBitmap.recycle();
        stampFrameBitmap = null;
        Log.i(Tag, "the bitmap width is " + bitmapWidth + " the bitmap height is " + bitmapHeight);
        Log.i(Tag, "the stamp framewidth is " + stampFrameWidth + " the stamp frame height is " + stampFrameHeight);
        totalTranslateX = 0;
        totalTranslateY = 0;

        totalRatio = initRatio = 1;

        this.requestRender();
    }

    public void setStampFrame(ImageView stampFrame) {
        this.stampFrame = stampFrame;
    }

    public void setStatus(int status) {
        currentStatus = status;
    }

    public void showAnimation(RelativeLayout mView, boolean isHorizontal) {
        if (isHorizontal) {
            changeUiInterface.changeStampFrame(R.drawable.background_stamp_v_transparent_pierced);
            if (changeUiInterface != null) {
                changeUiInterface.changeStampFrame(bitmap);
            }
            isHorizontal = false;
            currentStatus = STATUS_INIT;
            this.requestRender();
        } else {
            changeUiInterface.changeStampFrame(R.drawable.background_stamp_h_transparent_pierced);
            isHorizontal = true;
            currentStatus = STATUS_INIT;
            this.requestRender();
        }
    }

    public void stampViewonTouchProcessing(Context mContext, MotionEvent event) {
        // TODO Auto-generated method stub

        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            Log.d(this, "ACTION_DOWN");
            onTouchDown(event);
        case MotionEvent.ACTION_POINTER_DOWN:
            Log.d(this, "ACTION_POINTER_DOWN");
            if (event.getPointerCount() == 2) {
                // 当有两个手指按在屏幕上时，计算两指之间的距离
                lastFingerDis = distanceBetweenFingers(event);
            }
            lastXMove = -1;
            lastYMove = -1;
            break;
        case MotionEvent.ACTION_MOVE:
            Log.d(this, "onTouch,move");
            if (event.getPointerCount() == 1) {
                // 只有单指按在屏幕上移动时，为拖动状态
                float xMove = event.getX();
                float yMove = event.getY();
                if (lastXMove == -1 && lastYMove == -1) {
                    lastXMove = xMove;
                    lastYMove = yMove;
                }
                currentStatus = STATUS_MOVE;
                movedDistanceX = xMove - lastXMove;
                Log.d(this, "movedDistanceX:" + movedDistanceX);
                movedDistanceY = yMove - lastYMove;
                lastXMove = xMove;
                lastYMove = yMove;
                stampFrame.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
                moveGLSurfaceView();
            } else if (event.getPointerCount() == 2) {
                // 有两个手指按在屏幕上移动时，为缩放状态
                centerPointBetweenFingers(event);
                double fingerDis = distanceBetweenFingers(event);
                if (fingerDis - lastFingerDis > 15) {
                    currentStatus = STATUS_ZOOM_OUT;
                } else if (lastFingerDis - fingerDis > 15) {
                    currentStatus = STATUS_ZOOM_IN;
                }
                initRatio = 1.0f;
                // 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例的1/2
                if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 2 * initRatio)
                        || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio / 2)) {
                    scaledRatio = (float) (fingerDis / lastFingerDis);
                    totalRatio = totalRatio * scaledRatio;
                    if (totalRatio > 2 * initRatio) {
                        totalRatio = 2 * initRatio;
                    } else if (totalRatio < initRatio / 2) {
                        totalRatio = initRatio / 2;
                    }
                    stampFrame.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
                    zoomGLSurfaceView(totalRatio);
                    lastFingerDis = fingerDis;
                }

            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            if (event.getPointerCount() == 2) {
                // 手指离开屏幕时将临时值还原
                lastXMove = -1;
                lastYMove = -1;
            }
            stampFrame.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            currentStatus = STATUS_NONE;
            this.requestRender();
            break;
        case MotionEvent.ACTION_UP:
            // 手指离开屏幕时将临时值还原
            lastXMove = -1;
            lastYMove = -1;
            stampFrame.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            currentStatus = STATUS_NONE;
            this.requestRender();
            break;
        default:
            break;
        }

    }

    private void zoomGLSurfaceView(float ratio) {
        // translateX = totalTranslateX * ratio + centerPointX * (1 - ratio);
        // translateY = totalTranslateY * ratio + centerPointY * (1 - ratio);
        totalTranslateX = totalTranslateX;
        totalTranslateY = totalTranslateY;
        this.requestRender();
    }

}