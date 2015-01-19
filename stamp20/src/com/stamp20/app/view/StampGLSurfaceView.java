package com.stamp20.app.view;

import static javax.microedition.khronos.opengles.GL10.GL_RGBA;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.stamp20.app.R;
import com.stamp20.app.activities.GLToolbox;
import com.stamp20.app.activities.MainEffect;
import com.stamp20.app.activities.TextureRenderer;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.ZoomImageView.OnMoveOrZoomListener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class StampGLSurfaceView extends GLSurfaceView implements
		GLSurfaceView.Renderer {

	private static final String Tag = "StampGLSurfaceView";

	public Context mContext;

	private boolean mInitialized = false;
	private EffectContext mEffectContext;
	private int mImageWidth;
	private int mImageHeight;
	private Effect mCurrentEffect = null;
	private String currentfiltername;
	private int currentfilterID = 0;
	private int[] mTextures = new int[2];
	private Bitmap sourceBitmap;
	private TextureRenderer mTexRenderer = new TextureRenderer();
	private ImageEffectAdapter effectAdapter;
	private ImageView stampFrame;
	private Bitmap stampFrameBitmap;
	private Bitmap glBitmap;
	private int mWidth;
	private int mHeight;

	/**
	 * 当前邮票是横屏还是竖屏界面
	 */
	private boolean isHorizontal = true;

	public ImageView getStampFrame() {
		return stampFrame;
	}

	public void setStampFrame(ImageView stampFrame) {
		this.stampFrame = stampFrame;
	}

	public ImageView getRotateButton() {
		return rotateButton;
	}

	public void setRotateButton(ImageView rotateButton) {
		this.rotateButton = rotateButton;
	}

	public void setStatus(int status) {
		currentStatus = status;
	}

	private ImageView rotateButton;

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
	public static final int STATUS_NONE = 5;

	public static final int STATUS_CAPTURE = 6;

	private static final int UPDATE_FRAME = 7;

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

	/**
	 * 记录两指同时放在屏幕上时，中心点的横坐标值
	 */
	private float centerPointX;

	/**
	 * 记录两指同时放在屏幕上时，中心点的纵坐标值
	 */
	private float centerPointY;

	private float currentBitmapWidth;

	private float currentBitmapHeight;

	private int ratio;

	private int deltaW = 15;

	private int deltaH = 15;

	private int surfaceWidth;

	private int surfaceHeight;
	// the stamp bitmap finally produce
	private Bitmap bitmap;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == UPDATE_FRAME) {
				MainEffect.instance.mStampFrame.setImageBitmap(bitmap);
			} else if (msg.what == INIT_FRAME) {
				if (isHorizontal) {
					MainEffect.instance.mStampFrame
							.setImageBitmap(BitmapFactory.decodeResource(
									mContext.getResources(),
									R.drawable.background_stamp_h_transparent_pierced));
				} else {
					MainEffect.instance.mStampFrame
							.setImageBitmap(BitmapFactory.decodeResource(
									mContext.getResources(),
									R.drawable.background_stamp_v_transparent_pierced));
				}
			}
		}

	};

	public Bitmap getSourceBitmap() {
		return sourceBitmap;
	}

	public void setSourceBitmap(Bitmap sourceBitmap) {

		currentStatus = STATUS_INIT;

		this.sourceBitmap = sourceBitmap;

		int bitmapWidth = sourceBitmap.getWidth();
		int bitmapHeight = sourceBitmap.getHeight();
		int stampFrameWidth = stampFrame.getWidth();
		int stampFrameHeight = stampFrame.getHeight();
		// 计算邮票宽与图片宽比率，背景高与图片高比率，取较大值作为比率，使得缩放宽度与邮票框宽度一致
		// float ratio = Math.max(stampBackgroundWidth / (bitmapWidth * 1.0f),
		// stampBackgroundHeight / (bitmapHeight * 1.0f));
		float translateX = (getWidth() - (stampFrameWidth * 1)) / 2f;
		float translateY = (getHeight() - (stampFrameHeight * 1)) / 2f;
		totalTranslateX = translateX;
		totalTranslateY = translateY;

		ratio = stampFrameWidth / bitmapWidth;

		totalRatio = initRatio = ratio = 1;

		// currentBitmapWidth = bitmapWidth * initRatio;
		// currentBitmapHeight = bitmapHeight * initRatio;

	}

	public String getCurrentfiltername() {
		return currentfiltername;
	}

	public void setCurrentfiltername(String currentfiltername) {
		this.currentfiltername = currentfiltername;
	}

	public int getCurrentfilterID() {
		return currentfilterID;
	}

	public void setCurrentfilterID(int currentfilterID) {
		this.currentfilterID = currentfilterID;
	}

	public ImageEffectAdapter getEffectAdapter() {
		return effectAdapter;
	}

	public void setEffectAdapter(ImageEffectAdapter effectAdapter) {
		this.effectAdapter = effectAdapter;
	}

	public StampGLSurfaceView(Context context) {
		super(context);
		mContext = context;
		this.setEGLContextClientVersion(2);
		this.setRenderer(this);
		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public StampGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.setEGLContextClientVersion(2);
		this.setRenderer(this);
		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	private void renderResult() {
		if (mCurrentEffect != null) {
			// if no effect is chosen, just render the original bitmap
			Log.i(Tag, "the render result is not null");
			mTexRenderer.renderTexture(mTextures[1], totalRatio,
					(int) totalTranslateX, (int) totalTranslateY);
		} else {
			// render the result of applyEffect()
			mTexRenderer.renderTexture(mTextures[0], totalRatio,
					(int) totalTranslateX, (int) totalTranslateY);
			Log.i(Tag, "the render result is null");
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		if (!mInitialized) {
			// Only need to do this once
			mEffectContext = EffectContext.createWithCurrentGlContext();
			mTexRenderer.init();
			loadTextures();
			mInitialized = true;
		}

		if (currentStatus == STATUS_INIT || currentStatus == STATUS_NONE
				|| currentStatus == STATUS_CAPTURE) {
			// if an effect is chosen initialize it and apply it to the texture
			if (currentfilterID != 0) {
				Log.i(Tag, "onDrawFrame the filter name is "
						+ currentfiltername);
				mCurrentEffect = effectAdapter.createEffect(currentfilterID,
						mEffectContext);
				applyEffect();
			}
			renderResult();
			generateStamp(gl);
			mHandler.sendEmptyMessage(UPDATE_FRAME);
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
			// if an effect is chosen initialize it and apply it to the texture
			/*
			 * if (currentfilterID != 0) { Log.i(Tag,
			 * "onDrawFrame the filter name is " + currentfiltername);
			 * mCurrentEffect = effectAdapter.createEffect(currentfilterID,
			 * mEffectContext); applyEffect(); } else { mCurrentEffect = null; }
			 */

			gl.glLoadIdentity(); // 重置当前的模型观察矩阵
			gl.glPushMatrix();
			renderResult();
			gl.glPopMatrix();
			/*
			 * if (currentStatus == STATUS_CAPTURE) { Log.i(Tag,
			 * "onDrawFrame , generateStamp the gl is " + gl);
			 * generateStamp(gl); if (stamplistener != null) {
			 * notifyStampGernerated(); Log.i(Tag,
			 * "onDrawFrame ,notify stamp generated!"); } }
			 */
		}

	}

	private void notifyStampGernerated() {
		// TODO Auto-generated method stub
		stamplistener.OnStampBitmapGeneratedListener();
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
		IntBuffer ib = IntBuffer.allocate((mWidth - 2 * deltaW)
				* (mHeight - 2 * deltaH));
		mGL.glReadPixels((surfaceWidth - mWidth) / 2 + deltaW,
				(surfaceHeight - mHeight) / 2 + deltaH, mWidth - 2 * deltaW,
				mHeight - 2 * deltaH, GL_RGBA, GL_UNSIGNED_BYTE, ib);
		int[] ia = ib.array();

		// Convert upside down mirror-reversed image to right-side up normal
		// image.
		int tempHeight = mHeight - 2 * deltaH;
		int tempWidth = mWidth - 2 * deltaW;
		for (int i = 0; i < tempHeight; i++) {
			for (int j = 0; j < tempWidth; j++) {
				iat[(tempHeight - i - 1) * tempWidth + j] = ia[i * tempWidth
						+ j];
			}
		}

		glBitmap = Bitmap.createBitmap((mWidth - 2 * deltaW),
				(mHeight - 2 * deltaH), Bitmap.Config.ARGB_8888);
		glBitmap.copyPixelsFromBuffer(IntBuffer.wrap(iat));
		if (glBitmap != null) {
			Log.i(Tag, "onDrawFrame,glBitmap width is " + glBitmap.getWidth());
		} else {
			Log.i(Tag, "onDrawFrame,glBitmap is null");
		}

		bitmap = Bitmap.createBitmap(frameBitmap.getWidth(),
				frameBitmap.getHeight(), Config.ARGB_8888);
		try {
			Canvas cv = new Canvas(bitmap);
			cv.drawBitmap(glBitmap, deltaW, deltaH, null);
			cv.drawBitmap(frameBitmap, 0, 0, null);

			cv.save(Canvas.ALL_SAVE_FLAG);
			cv.restore();
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

	private void applyEffect() {
		mCurrentEffect.apply(mTextures[0], mImageWidth, mImageHeight,
				mTextures[1]);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		if (mTexRenderer != null) {
			mTexRenderer.updateViewSize(width, height);
		}
		surfaceWidth = width;
		surfaceHeight = height;
		Log.i(Tag, "onSurfaceChanged, the view width is " + width
				+ "the view height is " + height);
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
		mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

		// Upload to texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, sourceBitmap, 0);

		// Set texture parameters
		GLToolbox.initTexParams();
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		// TODO Auto-generated method stub

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
				// 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例的1/2
				if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio)
						|| (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio / 2)) {
					scaledRatio = (float) (fingerDis / lastFingerDis);
					totalRatio = totalRatio * scaledRatio;
					if (totalRatio > 4 * initRatio) {
						totalRatio = 4 * initRatio;
					} else if (totalRatio < initRatio / 2) {
						totalRatio = initRatio / 2;
					}
					lastFingerDis = fingerDis;
				}

				stampFrame.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
				zoomGLSurfaceView(totalRatio);
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

	private void moveGLSurfaceView() {
		// TODO Auto-generated method stub
		float translateX = totalTranslateX + movedDistanceX;
		Log.d(this, "move,totalX:" + totalTranslateX + ", movedX:"
				+ movedDistanceX + ", X:" + translateX);
		float translateY = totalTranslateY + movedDistanceY;

		totalTranslateX = translateX;
		totalTranslateY = translateY;

		// totalTranslateX = translateX*totalRatio;
		// totalTranslateY = translateY*totalRatio;

		this.requestRender();
	}

	private void zoomGLSurfaceView(float ratio) {
		// TODO Auto-generated method stub
		this.requestRender();
	}

	private void onTouchDown(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();
		/*
		 * if (x >= btnRotatePoints.getX() - 5 && x <= btnRotatePoints.getX() +
		 * getBmpBtnReversal().getWidth() + 5 && y >= btnRotatePoints.getY() - 5
		 * && y <= btnRotatePoints.getY() + getBmpBtnReversal().getHeight() + 5)
		 * { Log.d(this, "rotate...."); isBtnReversalClicked = true;
		 * setHorizontal(!isHorizontal); currentStatus = STATUS_INIT;
		 * invalidate(); } else { isBtnReversalClicked = false; }
		 */

	}

	OnStampBitmapGeneratedListener stamplistener = null;

	public void setOnStampBitmapGeneratedListener(
			OnStampBitmapGeneratedListener stamplistener2) {
		stamplistener = stamplistener2;
	}

	OnMoveOrZoomListener listener = null;

	public void setOnMoveOrZoomListener(OnMoveOrZoomListener l) {
		listener = l;
	}

	public interface OnMoveOrZoomListener {
		public void onMoveOrZoomListener(boolean flag, float ratio,
				float currentRatio);
	}

	public interface OnStampBitmapGeneratedListener {
		public void OnStampBitmapGeneratedListener();
	}

	public void onRotateClick(Context mContext, RelativeLayout frameLayout) {
		// TODO Auto-generated method stub
		if (isHorizontal) {
			MainEffect.instance.mStampFrame.setImageBitmap(BitmapFactory
					.decodeResource(mContext.getResources(),
							R.drawable.background_stamp_h_transparent_pierced));
			showAnimation(frameLayout, true);

			isHorizontal = false;
			// 旋转后，重新刷新GLSurfaceView
			this.requestRender();
		} else {
			MainEffect.instance.mStampFrame.setImageBitmap(BitmapFactory
					.decodeResource(mContext.getResources(),
							R.drawable.background_stamp_v_transparent_pierced));
			showAnimation(frameLayout, false);

			isHorizontal = true;
			this.requestRender();
		}
	}

	public void showAnimation(RelativeLayout mView, final boolean isHorizontal) {
		/*
		 * final float centerX = mView.getWidth() / 2.0f; final float centerY =
		 * mView.getHeight() / 2.0f; RotateAnimation rotateAnimation; //
		 * 这个是设置需要旋转的角度 if (isHorizontal) { rotateAnimation = new
		 * RotateAnimation(0, -90, centerX, centerY); } else { rotateAnimation =
		 * new RotateAnimation(0, 90, centerX, centerY); }
		 * 
		 * // 这个是设置动画时间的 rotateAnimation.setDuration(500);
		 * //rotateAnimation.setFillAfter(true);
		 * rotateAnimation.setAnimationListener(new AnimationListener() {
		 * 
		 * @Override public void onAnimationEnd(Animation arg0) {
		 */
		// TODO Auto-generated method stub
		if (isHorizontal) {
			MainEffect.instance.mStampFrame.setImageBitmap(BitmapFactory
					.decodeResource(mContext.getResources(),
							R.drawable.background_stamp_v_transparent_pierced));
			MainEffect.instance.mGPUImageView.requestRender();
			currentStatus = STATUS_INIT;
		} else {
			MainEffect.instance.mStampFrame.setImageBitmap(BitmapFactory
					.decodeResource(mContext.getResources(),
							R.drawable.background_stamp_h_transparent_pierced));
			MainEffect.instance.mGPUImageView.requestRender();
			currentStatus = STATUS_INIT;
		}
		// }
		/*
		 * @Override public void onAnimationRepeat(Animation arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onAnimationStart(Animation arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * }); mView.startAnimation(rotateAnimation);
		 */
	}

}