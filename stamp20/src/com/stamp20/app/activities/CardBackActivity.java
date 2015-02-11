package com.stamp20.app.activities;

import com.stamp20.app.adapter.ChooseBackColorAdapter;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.CardBackView;
import com.stamp20.app.view.CardBackView.onMeasuredListener;

import com.stamp20.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;

public class CardBackActivity extends Activity implements OnClickListener {	

	private static CardBackActivity instance;
	private CardBackView cardBackView;
	private Gallery gallery_choose_back;
	private ChooseBackColorAdapter chooseBackColorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_back);
		FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
		instance = this;
		initView();
	}
	
	private void initView() {
		cardBackView = (CardBackView) findViewById(R.id.cardbackview);
/*		cardBackView.setOnMeasuredListener(new onMeasuredListener(){
			@Override
			public void onMeasuredListener(int width, int height) {
				LayoutParams params = null;
				params.height = height;
				params.width  = width;
				cardBackView.setLayoutParams(params);
				cardBackView.postInvalidate();
			}
		});*/
		chooseBackColorAdapter = new ChooseBackColorAdapter(CardBackActivity.this);
		gallery_choose_back = (Gallery)findViewById(R.id.activity_card_back_select_back);
		gallery_choose_back.setAdapter(chooseBackColorAdapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
