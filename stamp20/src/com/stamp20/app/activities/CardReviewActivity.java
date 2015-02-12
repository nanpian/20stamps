package com.stamp20.app.activities;

import com.stamp20.app.R;
import com.stamp20.app.util.FontManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_review);
		FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
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
		activity_envelope_img = (ImageView)findViewById(R.id.activity_envelope_img);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_previous:
			finish();
			break;
		case R.id.display_front:
			
			break;
		case R.id.display_back:
			break;
		case R.id.card_add_to_cart:
			Intent intent = new Intent();
			intent.setClass(this, ShopCartItemsActivity.class);
			startActivity(intent);
			break;
		}
	}
}
