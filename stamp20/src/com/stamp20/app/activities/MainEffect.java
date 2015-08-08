package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.effect.EffectContext;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.Stamp20Application;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.HorizontalListView;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.StampGLSurfaceView;
import com.stamp20.app.view.StampGLSurfaceView.OnStampBitmapGeneratedListener;
import com.stamp20.app.view.StampGLSurfaceView.ChangeUIInterface;

public class MainEffect extends Activity implements OnTouchListener, OnStampBitmapGeneratedListener, ChangeUIInterface, OnClickListener {

    private Context mContext;
    private Bitmap bitmap;

    private static final CharSequence titleName = "Customize";
    public StampGLSurfaceView mGPUImageView;
    private FrameLayout touchArea;
    private static final int MSG_SELECT_PICTURE = 1000;
    private static final String Tag = "MainEffect";

    private EffectContext mEffectContext;
    private String currentfiltername;
    private int currentfilterID = 0;

    private ImageView headerPrevious;
    private TextView headerTitle;

    private ImageView tailIcon;
    private TextView tailText;
    private ImageEffectAdapter effectAdapter;
    public ImageView mStampFrame;
    private ImageView mRotateView;
    private RelativeLayout mFrameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Stamp20Application.getInstance().addActivity(this);
        mContext = this;
        setContentView(R.layout.main_effects_view);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(titleName);

        tailIcon = (ImageView) findViewById(R.id.tail_icon);
        tailIcon.setVisibility(View.GONE);
        tailText = (TextView) findViewById(R.id.tail_text);
        tailText.setText(R.string.next_step);
        findViewById(R.id.tail).setOnClickListener(this);

        mGPUImageView = (StampGLSurfaceView) findViewById(R.id.zoomgpuimage);
        mStampFrame = (ImageView) findViewById(R.id.background_pic);
        mRotateView = (ImageView) findViewById(R.id.rotateimage);
        mRotateView.setOnClickListener(this);
        mFrameLayout = (RelativeLayout) findViewById(R.id.rotateframe);
        Uri uri = (Uri) getIntent().getParcelableExtra("imageUri");
        // Log.d(this, "uri=" + uri);
        LoadImageFilter(uri);

        touchArea = (FrameLayout) findViewById(R.id.pic_area);
        touchArea.setOnTouchListener(this);

        mGPUImageView.setStampFrame(mStampFrame);
        mGPUImageView.setChangeUIInterface(this);
        initImage(uri);
    }

    private void LoadImageFilter(Uri imageUri) {
        // TODO Auto-generated method stub
        HorizontalListView gallery = (HorizontalListView) findViewById(R.id.galleryFilter);
        effectAdapter = new ImageEffectAdapter(MainEffect.this, mEffectContext);
        effectAdapter.setImageResource(imageUri);
        effectAdapter.setSelectItem(0);
        mGPUImageView.setEffectAdapter(effectAdapter);
        currentfilterID = 0;
        gallery.setAdapter(effectAdapter);
        gallery.setSelection(0);
        // gallery.setAnimationDuration(3000);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                effectAdapter.setSelectItem(position);
                // mCurrentEffect = (Effect) effectAdapter.getItem(position);
                currentfiltername = effectAdapter.getFilterName(position);
                currentfilterID = effectAdapter.getFilterID(position);
                mGPUImageView.setCurrentfilterID(currentfilterID);
                mGPUImageView.setCurrentfiltername(currentfiltername);
                Log.i(Tag, "zhudewei the filter name is " + currentfiltername);
                Log.i(Tag, "zhudewei the filter id is " + currentfilterID);
                mGPUImageView.requestRender();
            }
        });
    }

    private void initImage(Uri uri) {
        bitmap = ImageUtil.loadDownsampledBitmap(mContext, uri, 2);
        mGPUImageView.setSourceBitmap(bitmap);
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SELECT_PICTURE:
                // Log.d(this, "handleMessage--MSG_SELECT_PICTURE");
                Uri uri = (Uri) msg.obj;
                bitmap = ImageUtil.loadDownsampledBitmap(mContext, uri, 2);
                mGPUImageView.setSourceBitmap(bitmap);
                break;
            default:
                break;
            }
        }

    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        mGPUImageView.stampViewonTouchProcessing(mContext, event);
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.tail:
            // mStampView.isHorizontal());
            generateStamp();
            mGPUImageView.setOnStampBitmapGeneratedListener(this);
            break;
        case R.id.rotateimage:
            mGPUImageView.onRotateClick(mContext, mFrameLayout);
            break;
        default:
            break;
        }
    }

    public void generateStamp() {
        mGPUImageView.setStatus(StampGLSurfaceView.STATUS_CAPTURE);
        mGPUImageView.requestRender();
    }

    @Override
    public void OnStampBitmapGeneratedListener() {
        // TODO Auto-generated method stubs
        Log.d(Tag, "MainEffect before start ChooseRateActivity, mGPUImageView.isHorizontal:" + mGPUImageView.isHorizontal);
        Intent intent = new Intent(this, ChooseRateActivity.class);
        intent.putExtra(Constant.STAMP_IS_HORIZONTAL, mGPUImageView.isHorizontal);
        startActivity(intent);
        finish();
        mGPUImageView.setOnStampBitmapGeneratedListener(null);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        // 回收内存
        if (effectAdapter != null)
            effectAdapter.clearPreviewHashMap();
        if (mGPUImageView != null) {
            Bitmap tmpForRecycle = mGPUImageView.getSourceBitmap();
            if (tmpForRecycle != null) {
                tmpForRecycle.recycle();
                tmpForRecycle = null;
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mGPUImageView.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        mGPUImageView.onResume();
        super.onResume();
    }

    // 变换相框
    @Override
    public void changeStampFrame(int resId) {
        // TODO Auto-generated method stub
        mStampFrame.setImageResource(resId);
    }

    // 变换相框
    @Override
    public void changeStampFrame(Bitmap bitmap) {
        // TODO Auto-generated method stub
        mStampFrame.setImageBitmap(bitmap);
    }

}
