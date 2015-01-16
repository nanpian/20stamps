package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.stamp20.app.R;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.ScollerRelativeView;
import com.stamp20.app.view.ShopCartView;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2014-12-29 下午4:49:47 类说明
 */

public class ShopCartActivity extends Activity implements OnClickListener {

	private static final String TAG = "ShopCartActivity";
	
	private BitmapCache mCache = null;
	private ImageView mItemCheck;
	private ImageView backHome;
	private ShopCartView mShopStampView;
	private LinearLayout mMoveView, mShopviewInsert;
	private ScollerRelativeView mScollerRelativeView;
	private RelativeLayout mHeader;
	private LayoutInflater mLayoutInflater;
	//通过这个handler处理scroller完成后相应慢的问题
	private Handler mHandler;
	private static final int TIME_DURATION = 3000;
	private Button btnSkiptoCart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopcart);
		FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
		mLayoutInflater = getLayoutInflater();
		mHandler = new Handler();
		mCache = BitmapCache.getCache();
		initView();
		displayChart();
		loadShopData();
	}

	/**
	 * 日期 2014-12-31 作者 lenovo 说明 TODO 返回 void
	 */
	private void loadShopData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		    case R.id.btn_shop_skipto:
			    Intent intent = new Intent();
        	    intent.setClass(ShopCartActivity.this, ShopCartItemsActivity.class);
        	    intent.putExtra(ShopCartItemsActivity.ADD_ITEMS_TOCAET, true);
        	    startActivity(intent);
			    break;
		    case R.id.home_header_back:
		    	Intent intent1 = new Intent();
				intent1.setClass(ShopCartActivity.this, HomeActivity.class);
				startActivity(intent1);
			    break;

		    default:
			    break;
		    }
	}

	public void initView() {
		backHome = (ImageView)findViewById(R.id.home_header_back);
		backHome.setOnClickListener(this);
		mItemCheck = (ImageView) findViewById(R.id.item_check_view);
		mMoveView = (LinearLayout) findViewById(R.id.move_view);
		mScollerRelativeView = (ScollerRelativeView) findViewById(R.id.scroll_view);
		mHeader = (RelativeLayout) findViewById(R.id.header);
		AlphaAnimation alpAni = new AlphaAnimation(0.0f, 1.0f);
		alpAni.setDuration(2000);
		mShopviewInsert = (LinearLayout) mLayoutInflater.inflate(R.layout.shop_view_insert, null);
		mShopviewInsert.setAnimation(alpAni);
		mShopStampView = (ShopCartView)mShopviewInsert.findViewById(R.id.shop_stamp_view);
		mShopStampView.setmBpStampSource(mCache.get());
		//mShopStampView.setBackground(getResources().getDrawable(R.drawable.cards_christmas));
		//mShopStampView.setImageBitmap(mCache.get());
		btnSkiptoCart = (Button)mShopviewInsert.findViewById(R.id.btn_shop_skipto);
		btnSkiptoCart.setOnClickListener(this);
	}

	public void displayChart() {
		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.3f, 1.0f,
				1.3f, Animation.RELATIVE_TO_SELF, .5f,
				Animation.RELATIVE_TO_SELF, .5f);
		scaleAnimation.setDuration(1000);
		// 通过CycleInterpolator添加imageview的抖动
		scaleAnimation.setInterpolator(new CycleInterpolator(1f));
		scaleAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				mItemCheck.setVisibility(View.VISIBLE);
				mScollerRelativeView.smoothScollToY(
						(computeScrollDis() < 0) ? 0 : computeScrollDis(),
						TIME_DURATION);

				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
						//如何设置
						params.topMargin = 100;
						params.leftMargin = 60;
						params.rightMargin = 60;
						mScollerRelativeView.removeView(mShopviewInsert);
						mScollerRelativeView.addView(mShopviewInsert, params);
					}
				}, (int)(TIME_DURATION*0.7));
			}
		});

		mItemCheck.startAnimation(scaleAnimation);
	}

	public int computeScrollDis() {
		int scrollDis = 0;
		int currentPos = mMoveView.getTop();
		// 获得购物车滚动的位置
		scrollDis = currentPos - getActionBarHeight() - 40;
		Log.i(TAG, "currentPos is : " + currentPos
				+ " getActionBarHeight is : " + getActionBarHeight()
				+ ", scroll is : " + scrollDis);
		return scrollDis;
	}

	public int getActionBarHeight() {
		int height = 0;
		if (mHeader != null) {
			height = mHeader.getHeight();
		}
		return height;
	}
}