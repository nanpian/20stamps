package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.util.CardsTemplateUtils;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.ZoomImageView;

public class CardsActivity extends Activity implements ZoomImageView.OnMoveOrZoomListener, View.OnClickListener {

    public static final int ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE = ACTIVITY_RESULT_FOR_SELECT_PIC + 1;

    public static final String ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID = "activity_result_for_change_template_extra_template_id";

    private static final int ACTIVITY_RESULT_FOR_SELECT_PIC = 1;
    private static final int MSG_CHANGE_DESIGN = MSG_SELECT_PICTURE + 1;
    private static final int MSG_SELECT_PICTURE = 1000;
    private static final CharSequence titleName = "Customize Front";
    boolean bCurrentBackgroundPicAlhaFlag = false;
    /**
     * 待展示的图片
     */
    private Bitmap bitmap;
    private ImageView headerPrevious;
    private TextView headerTitle;

    private ImageView mBackgroundPic;

    private Button mChoosePhoto;
    private Button mChooseTemplate;
    private RelativeLayout mCross;
    private int mCurrentBackgroundPicId = R.drawable.cards_new_year;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SELECT_PICTURE:
                Log.d(this, "handleMessage--MSG_SELECT_PICTURE");
                Uri uri = (Uri) msg.obj;
                bitmap = ImageUtil.loadDownsampledBitmap(CardsActivity.this, uri, 2);
                zoomImageView.setImageBitmap(bitmap);
                break;
            case MSG_CHANGE_DESIGN:
                Log.d(this, "handleMessage--MSG_SELECT_PICTURE");
                int templateId = (Integer) msg.obj;
                if (templateId != -1) {
                    mBackgroundPic.setImageResource(templateId);
                }
                break;
            default:
                break;
            }
        }

    };
    private RelativeLayout mTextTip;

    private TextView mTextViewCurrentRatio;

    private TextView mTextViewInitRatio;

    /**
     * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
     */
    private ZoomImageView zoomImageView;

    private void changeTemplate() {
        Intent intent = new Intent();
        intent.setClass(CardsActivity.this, CardsTemplateChooseActivity.class);
        startActivityForResult(new Intent(CardsActivity.this, CardsTemplateChooseActivity.class),
                ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_RESULT_FOR_SELECT_PIC && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SELECT_PICTURE, uri));
        } else if (requestCode == ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE && resultCode == RESULT_OK) {
            int temPos = data.getIntExtra(ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID, -1);
            mHandler.sendMessage(mHandler.obtainMessage(MSG_CHANGE_DESIGN,
                    CardsTemplateUtils.getTransTemplateId(temPos)));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mChoosePhoto.getId()) {
            com.stamp20.app.util.Log.d(this, "mChoosePhoto.click");
            selectPicture();
        } else if (id == mChooseTemplate.getId()) {
            com.stamp20.app.util.Log.d(this, "mChooseTemplate.click");
            changeTemplate();
        } else if (id == R.id.header_previous) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_main);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(titleName);
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_home_wedding);
        zoomImageView.setImageBitmap(bitmap);

        mBackgroundPic = (ImageView) this.findViewById(R.id.background_pic);
        zoomImageView.setOnMoveOrZoomListener(this);

        mCross = (RelativeLayout) this.findViewById(R.id.cross);
        mTextTip = (RelativeLayout) this.findViewById(R.id.tip);
        mTextViewCurrentRatio = (TextView) this.findViewById(R.id.tip_currentratio);
        mTextViewInitRatio = (TextView) this.findViewById(R.id.tip_initratio);
        mChoosePhoto = (Button) this.findViewById(R.id.choose_photo);
        mChoosePhoto.setOnClickListener(this);
        mChooseTemplate = (Button) this.findViewById(R.id.choose_template);
        mChooseTemplate.setOnClickListener(this);

        Intent getFromChooseTemp = getIntent();
        if (getFromChooseTemp != null) {
            int tmplateId = getFromChooseTemp.getIntExtra(ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID, -1);
            mHandler.sendMessage(mHandler.obtainMessage(MSG_CHANGE_DESIGN, tmplateId));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 记得将Bitmap对象回收掉
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    @Override
    public void onMoveOrZoomListener(boolean flag, float initRatio, float totalRatio) {

        mTextViewInitRatio.setText("初始缩放:" + initRatio);
        if (initRatio * 4f == totalRatio) {
            mTextViewCurrentRatio.setText("当前缩放:已达最大缩放(初始缩放的4倍)");
        } else {
            mTextViewCurrentRatio.setText("当前缩放:" + totalRatio);
        }

        if (flag == bCurrentBackgroundPicAlhaFlag)
            return;
        bCurrentBackgroundPicAlhaFlag = flag;
        if (bCurrentBackgroundPicAlhaFlag) {
            mTextTip.setVisibility(View.VISIBLE);
            mCross.setVisibility(View.VISIBLE);
            mBackgroundPic.setAlpha(100);
        } else {
            mTextTip.setVisibility(View.INVISIBLE);
            mCross.setVisibility(View.INVISIBLE);
            mBackgroundPic.setAlpha(255);
        }
    }

    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ACTIVITY_RESULT_FOR_SELECT_PIC);
    }
}
