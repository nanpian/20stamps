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

public class CardEnvelopeActivity extends Activity implements OnClickListener {

	private ImageView header_previous;
	private TextView header_title;
	private Button review_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_envelope);
		FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
		initView();
	}

	private void initView() {
		header_previous = (ImageView) findViewById(R.id.header_previous);
		header_previous.setOnClickListener(this);
		header_title = (TextView) findViewById(R.id.header_title);
		header_title.setText("Select Envelope");
		review_button = (Button) findViewById(R.id.review_card);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_previous:
			finish();
			break;
		case R.id.review_card:
			Intent intent = new Intent();
			intent.setClass(this, CardReviewActivity.class);
			startActivity(intent);
			break;
		}
	}

}
