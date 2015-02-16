package com.stamp20.app.activities;

import com.stamp20.app.R;
import com.stamp20.app.anim.Rotate3dAnimation;
import com.stamp20.app.util.CardBmpCache;
import com.stamp20.app.util.FontManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardReviewActivity extends Activity implements OnClickListener {
	private ImageView header_previous;
	private TextView header_title;
	private Button review_button;
	private Button display_front;
	private Button display_back;
	private ImageView activity_envelope_img;
	private Bitmap cardBmpBack;
	private Bitmap cardBmpFront;
	private boolean isFrontNow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_review);
		// FontManager.changeFonts((RelativeLayout) findViewById(R.id.root),
		// this);
		initView();
	}

	private void initView() {
		header_previous = (ImageView) findViewById(R.id.header_previous);
		header_previous.setOnClickListener(this);
		header_title = (TextView) findViewById(R.id.header_title);
		header_title.setText("Review");
		review_button = (Button) findViewById(R.id.card_add_to_cart);
		review_button.setOnClickListener(this);
		display_front = (Button) findViewById(R.id.display_front);
		display_front.setOnClickListener(this);
		display_back = (Button) findViewById(R.id.display_back);
		display_back.setOnClickListener(this);
		activity_envelope_img = (ImageView) findViewById(R.id.activity_envelope_img2);
		CardBmpCache mCache = CardBmpCache.getCacheInstance();
		cardBmpFront = mCache.getFront();
		activity_envelope_img.setImageBitmap(cardBmpFront);
		CardBmpCache bmpCache = CardBmpCache.getCacheInstance();
		cardBmpBack = bmpCache.getBack();
		isFrontNow = true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_previous:
			finish();
			break;
		case R.id.display_front:
			 if(!isFrontNow)applyRotation(0,90,0);
			break;
		case R.id.display_back:
			 if(isFrontNow)applyRotation(0,90,0);
			break;
		case R.id.card_add_to_cart:
			Intent intent = new Intent();
			intent.setClass(this, ShopCartItemsActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void applyRotation(float start, float end, final int viewId) {
		final float centerX = activity_envelope_img.getWidth() / 2.0f;
		final float centerY = activity_envelope_img.getHeight() / 2.0f;
		Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX,
				centerY, 200.0f, true);
		rotation.setDuration(500);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) { // 动画结束
				activity_envelope_img.post(new Runnable() {
					@Override
					public void run() {
						// 图片是用android:src不是android:background,所以使用setImageResource就可以实现图片翻转了。
						// 实现翻转后再翻回来，要设置正反面标志位
						if (isFrontNow) {
							activity_envelope_img.setImageBitmap(cardBmpBack);
							isFrontNow = false;
						} else {
							isFrontNow = true;
							activity_envelope_img.setImageBitmap(cardBmpFront);
						}
						Rotate3dAnimation rotatiomAnimation = new Rotate3dAnimation(
								-90, 0, centerX, centerY, 200.0f, false);
						rotatiomAnimation.setDuration(500);
						rotatiomAnimation.setInterpolator(new DecelerateInterpolator());
						activity_envelope_img.startAnimation(rotatiomAnimation);
					}
				});

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
		});
		activity_envelope_img.startAnimation(rotation);

	}

}
