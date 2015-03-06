package com.stamp20.app.activities;

import com.stamp20.app.R;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.CardGLSurfaceView;
import com.stamp20.app.view.CardGLSurfaceView.OnCardBitmapGeneratedListener;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.StampViewConstants;
import com.stamp20.app.view.ZoomImageView;
import com.stamp20.app.view.CardBackView.onGeneratedCardBackBmpListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.EffectContext;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class CardEffect extends Activity implements OnClickListener,OnTouchListener{

	protected static final int REQUEST_CODE = 1001;
	private static final String Tag = "CardEffect";
	private static final CharSequence titleName = "Customize Front";
	protected static final int MSG_SELECT_PICTURE = 1002;
	protected static final int MSG_CHANGE_DESIGN = 1003;
	public static CardEffect instance;
	private Uri imageUri;
	private Bitmap loadedBitmap;
	public ImageView background_envelop, select_photo_button;
	private Gallery galleryFilter;
	private ImageView headerPrevious;
	private TextView headerTitle;
	private ImageEffectAdapter effectAdapter;
	private EffectContext mEffectContext = null;
	private CardGLSurfaceView mGPUImageView;
	public static final String ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID = "activity_result_for_change_template_extra_template_id";
	private int currentfilterID;
	private RelativeLayout buttonLayout;
	private Button choose_photo;
	private Button change_design;
	private RelativeLayout cardview;
	private Button customback;
	private Integer templateId;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SELECT_PICTURE:
				Log.d(Tag, "handleMessage--MSG_SELECT_PICTURE");
				Uri uri = (Uri) msg.obj;
				// bitmap = ImageUtil.loadDownsampledBitmap(CardsActivity.this,
				// uri, 2);
				// zoomImageView.setImageBitmap(bitmap);
				break;
			case MSG_CHANGE_DESIGN:
				Log.d(Tag, "handleMessage--MSG_SELECT_PICTURE");
				templateId = (Integer) msg.obj;
				if (templateId != -1) {
					background_envelop.setImageResource(templateId);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_effect);
		FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
		instance = this;
		initView();
		Intent getFromChooseTemp = getIntent();
		if (getFromChooseTemp != null) {
			int tmplateId = getFromChooseTemp.getIntExtra(
					ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID, -1);
			mHandler.sendMessage(mHandler.obtainMessage(MSG_CHANGE_DESIGN,
					tmplateId));
		}

	}

	private void initView() {

		headerPrevious = (ImageView) findViewById(R.id.header_previous);
		headerPrevious.setOnClickListener(this);
		headerTitle = (TextView) findViewById(R.id.header_title);
		headerTitle.setText(titleName);
		buttonLayout = (RelativeLayout) findViewById(R.id.buttonid);
		ImageView useMyPhoto = (ImageView) findViewById(R.id.activity_main_effects_use_myown_photo);
		useMyPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent data = new Intent();
				data.setClass(getBaseContext(), ImageLoaderActivity.class);
				startActivity(data);
			}
		});
		choose_photo = (Button) findViewById(R.id.choose_photo);
		choose_photo.setOnClickListener(this);
		change_design = (Button) findViewById(R.id.choose_template);
		change_design.setOnClickListener(this);
		background_envelop = (ImageView) findViewById(R.id.background_envelop);
		
		setupEvelopHeight();

		select_photo_button = (ImageView) findViewById(R.id.activity_main_effects_use_myown_photo);
		select_photo_button.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				select_photo_button.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
			}
			
		});
		mGPUImageView = (CardGLSurfaceView) findViewById(R.id.cardgpuimage);
		galleryFilter = (Gallery) findViewById(R.id.galleryFilter);
		effectAdapter = new ImageEffectAdapter(CardEffect.this, mEffectContext);
		mGPUImageView.setEffectAdapter(effectAdapter);
		effectAdapter = new ImageEffectAdapter(CardEffect.this, mEffectContext);
		currentfilterID = 0;
		galleryFilter.setAdapter(effectAdapter);
		galleryFilter.setSelection(5);
		galleryFilter.setAnimationDuration(3000);
		galleryFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					private String currentfiltername;
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						effectAdapter.setSelectItem(position);
						currentfiltername = effectAdapter.getFilterName(position);
						currentfilterID = effectAdapter.getFilterID(position);
						mGPUImageView.setCurrentfilterID(currentfilterID);
						mGPUImageView.setCurrentfiltername(currentfiltername);
						Log.i(Tag, "zhudewei the filter is "+ currentfiltername);
						mGPUImageView.requestRender();
					}
				});
		cardview = (RelativeLayout)findViewById(R.id.cardview);
		cardview.setOnTouchListener(this);
		//galleryFilter.setVisibility(View.GONE);
		customback = (Button)findViewById(R.id.customback);
		customback.setOnClickListener(this);
	}

	private void setupEvelopHeight() {
		int W=getWindowManager().getDefaultDisplay().getWidth();//获取屏幕高度
		Bitmap cardTemplate = BitmapFactory.decodeResource(getResources(),
				R.drawable.cards_christmas);
		int w= cardTemplate.getWidth();
		int h= cardTemplate.getHeight();
		LayoutParams params = new LayoutParams(2*W/3,(h*2*W)/(3*w));
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		background_envelop.setLayoutParams(params );
		background_envelop.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i(Tag, "on new intent");
		setIntent(intent);// must store the new intent unless getIntent() will
		processExtraData();// return the old one
	}

	private void processExtraData() {
		Intent intent = getIntent();
		if (intent != null) {
			imageUri = (Uri) intent.getParcelableExtra("imageUri");
			Log.d(Tag, "uri=" + imageUri);
			if (imageUri != null) {
				if (templateId != -1) {
					background_envelop.setImageResource(templateId);
				}
				loadedBitmap = ImageUtil.loadDownsampledBitmap(this, imageUri,
						2);
				refreshView();
				mGPUImageView.setSourceBitmap(loadedBitmap);
				mGPUImageView.requestRender();
			} else {
			}
		}
	}

	private void refreshView() {
		select_photo_button.setVisibility(View.GONE);
		buttonLayout.setVisibility(View.VISIBLE);
		mGPUImageView.setVisibility(View.VISIBLE);
		galleryFilter.setVisibility(View.VISIBLE);
		galleryFilter.setAdapter(effectAdapter);
		galleryFilter.setSelection(5);
		galleryFilter.setAnimationDuration(3000);
		galleryFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					private String currentfiltername;
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						effectAdapter.setSelectItem(position);
						currentfiltername = effectAdapter.getFilterName(position);
						currentfilterID = effectAdapter.getFilterID(position);
						mGPUImageView.setCurrentfilterID(currentfilterID);
						mGPUImageView.setCurrentfiltername(currentfiltername);
						Log.i(Tag, "zhudewei the filter is "+ currentfiltername);
						mGPUImageView.requestRender();
					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.header_previous:
			finish();
			break;
		case R.id.choose_photo:
            com.stamp20.app.util.Log.d(this, "mChoosePhoto.click");
            selectPicture();
			break;
		case R.id.choose_template:
			select_photo_button.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            com.stamp20.app.util.Log.d(this, "mChooseTemplate.click");
            changeTemplate();
			break;
		case R.id.customback:
	/*		mGPUImageView.setCaptureBmp(true);
			mGPUImageView.setonGeneratedCardBackBmpListener(new onGeneratedCardBackBmpListener() {
				@Override
				public void onGeneratedCardBackBmp() {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), CardEnvelopeActivity.class);
					startActivity(intent);
				}
				
			});*/
			mGPUImageView.setCaptureFront();
			mGPUImageView.requestRender();
			mGPUImageView.setOnCardBitmapGeneratedListener(new OnCardBitmapGeneratedListener() {
				@Override
				public void OnCardBitmapGeneratedListener() {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), CardBackActivity.class);
					startActivity(intent);
				}			
			});
			break;
		default:
			break;
		}
	}
	
    private void changeTemplate(){
        startActivity(new Intent(CardEffect.this,
                CardsTemplateChooseActivity.class));
        finish();
    }
	
    private void selectPicture() {
		Intent data = new Intent();
		data.setClass(getBaseContext(), ImageLoaderActivity.class);
		startActivity(data);
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		mGPUImageView.cardViewonTouchProcessing(this, event);
		return true;
	}


}
