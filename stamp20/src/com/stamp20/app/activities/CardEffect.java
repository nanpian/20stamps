package com.stamp20.app.activities;

import com.stamp20.app.R;
import com.stamp20.app.R.string;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.CardGLSurfaceView;
import com.stamp20.app.view.CardGLSurfaceView.OnCardBitmapGeneratedListener;
import com.stamp20.app.view.HorizontalListView;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.ScollerRelativeView;
import com.stamp20.app.view.StampViewConstants;
import com.stamp20.app.view.ZoomImageView;
import com.stamp20.app.view.CardBackView.onGeneratedCardBackBmpListener;
import com.stamp20.gallary.GallaryActivity;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.effect.EffectContext;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
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
	private HorizontalListView galleryFilter;
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
	private RelativeLayout cardview, background_layout;
	private com.stamp20.app.view.ScollerRelativeView scrollPicArea;
	private Button customback;
	private Integer templateId;
	
	//add for change the background templte
	public static final int REQUEST_CODE_FOR_TEMPLATE = 1;
	public static final String SRC_IMAGE_URI = "imageUri";
	private boolean mIsChangingPhoto = false;

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
                     background_envelop.setImageBitmap(getAlphaBackView(templateId));
					//background_envelop.setImageResource(templateId);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_card_effect);
		FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
		instance = this;
		initView();
		mIsChangingPhoto = false;
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
				data.setClass(getBaseContext(), GallaryActivity.class);
				startActivity(data);
			}
		});
		choose_photo = (Button) findViewById(R.id.choose_photo);
		choose_photo.setOnClickListener(this);
		change_design = (Button) findViewById(R.id.choose_template);
		change_design.setOnClickListener(this);
		background_envelop = (ImageView) findViewById(R.id.background_envelop);
        background_layout = (RelativeLayout)findViewById(R.id.background_layout);
        cardview = (RelativeLayout)findViewById(R.id.cardview);
        cardview.setOnTouchListener(this);
		setupEvelopHeight();

		select_photo_button = (ImageView) findViewById(R.id.activity_main_effects_use_myown_photo);
		select_photo_button.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus){
                    select_photo_button.setAlpha(.3f);
                }
			}
			
		});
		
		select_photo_button.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                    	select_photo_button.setAlpha(.3f);
                    }else if(event.getAction() == MotionEvent.ACTION_UP){
                    	select_photo_button.setAlpha(1f);
                    }
                    return false;
            }
            
    });
		mGPUImageView = (CardGLSurfaceView) findViewById(R.id.cardgpuimage);
		galleryFilter = (HorizontalListView) findViewById(R.id.galleryFilter);
		effectAdapter = new ImageEffectAdapter(CardEffect.this, mEffectContext);
		mGPUImageView.setEffectAdapter(effectAdapter);
		effectAdapter = new ImageEffectAdapter(CardEffect.this, mEffectContext);
		currentfilterID = 0;
		effectAdapter.setSelectItem(0);
		galleryFilter.setAdapter(effectAdapter);
		galleryFilter.setSelection(5);
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
		//galleryFilter.setVisibility(View.GONE);
		customback = (Button)findViewById(R.id.customback);
		customback.setOnClickListener(this);
		
		//首先需要设置背景色为深色
		customback.setBackgroundDrawable(getResources().getDrawable(R.drawable.dra_home_green_button_pressed_true));
		scrollPicArea = (ScollerRelativeView) findViewById(R.id.pic_area);
	}

    public Bitmap getAlphaBackView(int viewId){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), viewId);
        Bitmap back = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(back);
        Paint paint = new Paint();
        paint.setAlpha(150);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return back;
    }

	private void setupEvelopHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
		Bitmap cardTemplate = BitmapFactory.decodeResource(getResources(),
				R.drawable.cards_christmas);
		int w= cardTemplate.getWidth();
		int h= cardTemplate.getHeight();

		LayoutParams params = new LayoutParams(5*screenWidth/6,(h*5*screenWidth)/(6*w));
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
        background_envelop.setLayoutParams(params);
		background_envelop.setVisibility(View.VISIBLE);

        resetChoseLayout(110);
	}

    public void resetChoseLayout(int distance){
        LayoutParams layout = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (distance == 0){
            layout.addRule(RelativeLayout.CENTER_IN_PARENT);
        }else {
            layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layout.topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, distance, getResources().getDisplayMetrics());
        }
        background_layout.setLayoutParams(layout);
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
            //here we need to ajust the layout
            resetChoseLayout(0);
			imageUri = (Uri) intent.getParcelableExtra("imageUri");
			Log.d(Tag, "uri=" + imageUri);
			if (imageUri != null) {
				if (templateId != -1) {
					background_envelop.setImageResource(templateId);
					mGPUImageView.changetemplate(templateId);//change backguard 
				}
				loadedBitmap = ImageUtil.loadDownsampledBitmap(this, imageUri,2);
				refreshView();
				
				effectAdapter.setImageResource(imageUri);
				//click change photo button, need to init some data
				if (mIsChangingPhoto) {
					effectAdapter.clearPreviewHashMap();
					String filtername = effectAdapter.getFilterName(0);
					currentfilterID = effectAdapter.getFilterID(0);
					Log.i(Tag, "change photo id is : " + currentfilterID + "filtername is : " + filtername);
					mGPUImageView.setCurrentfilterID(currentfilterID);
					mGPUImageView.setCurrentfiltername(filtername);
				}
				mGPUImageView.setSourceBitmap(loadedBitmap);
				//mGPUImageView.requestRender();
			} else {
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGPUImageView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mGPUImageView.onPause();
	}
	
	private void refreshView() {
		select_photo_button.setVisibility(View.GONE);
		buttonLayout.setVisibility(View.VISIBLE);
		mGPUImageView.setVisibility(View.VISIBLE);
		galleryFilter.setVisibility(View.VISIBLE);
		galleryFilter.setAdapter(effectAdapter);
		
		galleryFilter.setSelection(0);
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
		//恢复浅色调
		customback.setBackgroundDrawable(getResources().getDrawable(R.drawable.sel_home_green_button));
		customback.setTextColor(0xffffffff);
		scrollPicArea.scrollBy(0, -800);
		scrollPicArea.smoothScollToY(800, 3000);
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
                    intent.putExtra("imageUri", imageUri);
					startActivity(intent);
					mGPUImageView.setOnCardBitmapGeneratedListener(null);
				}			
			});
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(Tag, "in result activity!!");
		if (requestCode == REQUEST_CODE_FOR_TEMPLATE && resultCode == RESULT_OK) {
			int tempid = data.getIntExtra(
					ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID, -1);
			templateId = tempid;
			background_envelop.setImageResource(tempid);
			mGPUImageView.changetemplate(tempid);//change backguard
			mGPUImageView.requestRender();
		}
	};
	
    private void changeTemplate(){
//        startActivity(new Intent(CardEffect.this,
//                CardsTemplateChooseActivity.class));
//        finish();
    	mIsChangingPhoto = false;
    	Intent intent = new Intent();
    	intent.setClass(CardEffect.this, CardsTemplateChooseActivity.class);
    	intent.putExtra(SRC_IMAGE_URI, imageUri);
    	startActivityForResult(intent, REQUEST_CODE_FOR_TEMPLATE);
    }
	
    private void selectPicture() {
    	mIsChangingPhoto = true;
		Intent data = new Intent();
		data.setClass(getBaseContext(), GallaryActivity.class);
		startActivity(data);
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		mGPUImageView.cardViewonTouchProcessing(this, event);
		return true;
	}


}
