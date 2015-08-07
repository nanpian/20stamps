package com.stamp20.app.view;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.stamp20.app.R;
import com.stamp20.app.activities.CardEffect;
import com.stamp20.app.activities.GLToolbox;
import com.stamp20.app.activities.TextureRenderer;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.util.CardBmpCache;
import com.stamp20.app.util.GLBaseUtil;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.StampGLSurfaceView.ChangeUIInterface;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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

public class CardGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private static final String Tag = "CardGLSurfaceView";
    private static final int STATUS_NONE = 0;
    private static final int STATUS_CAPTURE = 11;
    private int[] mTextures = new int[2];
    private Bitmap sourceBitmap;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private boolean mInitialized = false;
    private EffectContext mEffectContext;
    private ImageEffectAdapter effectAdapter;
    private int cardTemplateWidth;
    private int cardTemplateHeight;
    private int currentfilterID = 0;
    private String currentfiltername;
    private Effect mCurrentEffect = null;
    public Context mContext;
    private int mImageWidth;
    private int mImageHeight;
    private float centerPointX;
    private float centerPointY;
    private Integer templatedid = R.drawable.cards_new_year;
    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;

    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;

    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;

    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_MOVE = 4;

    /**
     * 松开手指状态常量
     */

    private static final int UPDATE_CARD = 7;

    private static final int INIT_FRAME = 8;

    /**
     * 记录上次手指移动时的横坐标
     */
    private float lastXMove = -1;

    /**
     * 记录上次手指移动时的纵坐标
     */
    private float lastYMove = -1;

    /**
     * 记录手指在横坐标方向上的移动距离
     */
    private float movedDistanceX;

    /**
     * 记录手指在纵坐标方向上的移动距离
     */
    private float movedDistanceY;

    /**
     * 记录图片在矩阵上的横向偏移值
     */
    private float totalTranslateX;

    /**
     * 记录图片在矩阵上的纵向偏移值
     */
    private float totalTranslateY;

    /**
     * 记录图片在矩阵上的总缩放比例
     */
    private float totalRatio;

    /**
     * 记录手指移动的距离所造成的缩放比例
     */
    private float scaledRatio;

    /**
     * 记录图片初始化时的缩放比例
     */
    private float initRatio;

    /**
     * 记录上次两指之间的距离
     */
    private double lastFingerDis;

    /**
     * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
     */
    private int currentStatus;

    private int deltaW = 0;

    private int deltaH = 0;

    private int surfaceWidth;

    private int surfaceHeight;
    // the stamp bitmap finally produce
    private Bitmap resultBitmap;

    private OnCardBitmapGeneratedListener cardlistener;

    public void setOnCardBitmapGeneratedListener(OnCardBitmapGeneratedListener l) {
        cardlistener = l;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == UPDATE_CARD) {
                background_envelop.setImageBitmap(resultBitmap);
            } else if (msg.what == INIT_FRAME) {
            }
        }

    };

    public void setCaptureFront() {
        currentStatus = STATUS_CAPTURE;
    }

    public Bitmap getSourceBitmap() {
        return this.sourceBitmap;
    }

    public void setSourceBitmap(Bitmap sourceBitmap) {
        currentStatus = STATUS_INIT;
        Log.i(Tag, "set source bitmap init");
        this.sourceBitmap = sourceBitmap;
        totalTranslateX = 0;
        totalTranslateY = 0;
        Log.i(Tag, "the totaltranlate x is " + totalTranslateX);
        Log.i(Tag, "the totaltranlate y is " + totalTranslateY);

        totalRatio = initRatio = 1;

        // 获取贺卡实际宽度，作为GLSurfaceView绘制的宽度
        ImageView cardBitmapView = (ImageView) ((Activity) mContext).findViewById(R.id.background_envelop);
        cardTemplateWidth = cardBitmapView.getWidth();
        cardTemplateHeight = cardBitmapView.getHeight();

        this.requestRender();
    }

    public CardGLSurfaceView(Context context) {
        super(context);
        if (!GLBaseUtil.supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        Log.i(Tag, "CardGLSurface view init1 ");
        mContext = context;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public CardGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!GLBaseUtil.supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        Log.i(Tag, "CardGLSurface view init2");
        mContext = context;
        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onResume() {
        super.onResume();
        mInitialized = false;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            // Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
            Log.i(Tag, "onDrawFrame inited ");
        }

        if (currentStatus == STATUS_INIT || currentStatus == STATUS_NONE || currentStatus == STATUS_CAPTURE) {
            // if an effect is chosen initialize it and apply it to the texture
            if (currentfilterID != 0) {
                Log.i(Tag, "onDrawFrame the filter name is " + currentfiltername);
                try {
                    mCurrentEffect = effectAdapter.createEffect(currentfilterID, mEffectContext);
                    applyEffect();
                } catch (Exception e) {
                    mCurrentEffect = null;
                }
            } else {
                mCurrentEffect = null;
            }
            renderResult();
            synchronized (this) {
                generateCard(gl);
                mHandler.sendEmptyMessage(UPDATE_CARD);
            }
            GLES20.glClearColor(0.15686f, 0.15686f, 0.15686f, 1.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            if (currentStatus == STATUS_CAPTURE) {
                Log.i(Tag, "onDrawFrame , generateStamp the gl is " + gl);
                if (cardlistener != null) {
                    notifyCardGernerated();
                    Log.i(Tag, "onDrawFrame ,notify stamp generated!");
                }
            }
        } else {
            mHandler.sendEmptyMessage(INIT_FRAME);
            gl.glLoadIdentity(); // 重置当前的模型观察矩阵
            gl.glPushMatrix();
            renderResult();
            gl.glPopMatrix();
        }
    }

    public void changetemplate(Integer templateId) {
        templatedid = templateId;
    }

    public Bitmap SavePixels(int x, int y, int w, int h, GL10 gl) {
        int b[] = new int[w * h];
        int bt[] = new int[w * h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - i - 1) * w + j] = pix1;
            }
        }
        Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;
    }

    public void generateCard(GL10 mGL) {
        // 得到GLSurfaceView图片后，要进行叠加运算
        Bitmap cardBitmap = null;
        cardBitmap = BitmapFactory.decodeResource(mContext.getResources(), templatedid);
        ImageView cardBitmapView = (ImageView) ((Activity) mContext).findViewById(R.id.background_envelop);
        // deltaW为白色边框的条宽度

        int mWidth = cardBitmapView.getWidth();
        int mHeight = cardBitmapView.getHeight();

        Bitmap glBitmap = SavePixels((surfaceWidth - mWidth) / 2, (surfaceHeight - mHeight) / 2, mWidth, mHeight, mGL);
        Bitmap cardScaledBitmap = Bitmap.createScaledBitmap(cardBitmap, mWidth, mHeight, true);

        if (resultBitmap != null) {
            resultBitmap.recycle();
            resultBitmap = null;
        }

        resultBitmap = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);

        try {
            Canvas cv = new Canvas(resultBitmap);
            cv.drawBitmap(glBitmap, 0, 0, null);
            cv.drawBitmap(cardScaledBitmap, 0, 0, null);

            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
            glBitmap.recycle();
            glBitmap = null;
        } catch (Exception e) {
            Log.i("zhudewei", "onDrawFrame, the bitmap exception");
            resultBitmap = null;
            e.getStackTrace();
        }
        Log.i("zhudewei", "onDrawFrame, the bitmap is " + resultBitmap);
        // 放入系统内存中
        Bitmap resultBitmapCorner = toRoundCorner2(resultBitmap);
        CardBmpCache mCache = CardBmpCache.getCacheInstance();

        if (cardBitmap != null) {
            cardBitmap.recycle();
            cardBitmap = null;
        }

        mCache.putFront(resultBitmapCorner);
    }

    public Bitmap toRoundCorner2(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Bitmap cover = BitmapFactory.decodeResource(getResources(), R.drawable.activity_card_back_shape_white);
        Rect rectSrc = new Rect(0, 0, cover.getWidth(), cover.getHeight());
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(cover, rectSrc, rect, null);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        cover.recycle();
        cover = null;
        return output;
    }

    public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap cover = BitmapFactory.decodeResource(mContext.getResources(), templatedid);
        Bitmap output = Bitmap.createBitmap(cover.getWidth(), cover.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, cover.getWidth(), cover.getHeight());
        final Rect rectSrc = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rectSrc, rect, paint);

        cover.recycle();
        cover = null;
        return output;
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
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight, cardTemplateWidth, cardTemplateHeight);
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

    @SuppressWarnings("deprecation")
    public void cardViewonTouchProcessing(Context mContext, MotionEvent event) {
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
                background_envelop.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
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
                if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 2 * initRatio) || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio / 2)) {
                    scaledRatio = (float) (fingerDis / lastFingerDis);
                    totalRatio = totalRatio * scaledRatio;
                    if (totalRatio > 2 * initRatio) {
                        totalRatio = 2 * initRatio;
                    } else if (totalRatio < initRatio / 2) {
                        totalRatio = initRatio / 2;
                    }
                    background_envelop.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
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
            background_envelop.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            currentStatus = STATUS_NONE;
            this.requestRender();
            break;
        case MotionEvent.ACTION_UP:
            // 手指离开屏幕时将临时值还原
            lastXMove = -1;
            lastYMove = -1;
            background_envelop.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            currentStatus = STATUS_NONE;
            this.requestRender();
            break;
        default:
            break;
        }

    }

    private void zoomGLSurfaceView(float ratio) {
        // TODO Auto-generated method stub
        float translateX = 0f;
        float translateY = 0f;
        // translateX = totalTranslateX * ratio - centerPointX * (1 - ratio);
        // translateY = totalTranslateY * ratio - centerPointY * (1 - ratio);
        totalTranslateX = totalTranslateX;
        totalTranslateY = totalTranslateY;
        this.requestRender();
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

    private void moveGLSurfaceView() {
        // TODO Auto-generated method stub
        float translateX = totalTranslateX + movedDistanceX;
        Log.d(this, "move,totalX:" + totalTranslateX + ", movedX:" + movedDistanceX + ", X:" + translateX);
        float translateY = totalTranslateY + movedDistanceY;

        totalTranslateX = translateX;
        totalTranslateY = translateY;

        currentStatus = STATUS_MOVE;
        // totalTranslateX = translateX*totalRatio;
        // totalTranslateY = translateY*totalRatio;

        this.requestRender();
    }

    private void onTouchDown(MotionEvent event) {
        event.getX();
        event.getY();

    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        // TODO Auto-generated method stub

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

    ChangeUIInterface changeUiInterface = null;

    public void setChangeUIInterface(ChangeUIInterface uiInterface) {
        this.changeUiInterface = uiInterface;
    }

    public interface ChangeUIInterface {
        public void changeBackEnvelop(Bitmap bitmap);

        public void changeBackEnvelop(int resId);
    }

    public interface OnCardBitmapGeneratedListener {
        public void OnCardBitmapGeneratedListener();
    }

    private void notifyCardGernerated() {
        // TODO Auto-generated method stub
        cardlistener.OnCardBitmapGeneratedListener();
    }

    public void setCurrentfilterID(int currentfilterID2) {
        this.currentfilterID = currentfilterID2;
    }

    public void setCurrentfiltername(String currentfiltername2) {
        this.currentfiltername = currentfiltername2;
    }

    public void setEffectAdapter(ImageEffectAdapter effectAdapter) {
        this.effectAdapter = effectAdapter;
    }

    private void applyEffect() {
        mCurrentEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    public ImageView background_envelop;

    public void setEnvelop(ImageView imageView) {
        background_envelop = imageView;
    }

}
