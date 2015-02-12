package com.stamp20.app.activities;

import com.stamp20.app.adapter.ChooseBackColorAdapter;
import com.stamp20.app.adapter.ImageEffectAdapter;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.CardBackView;
import com.stamp20.app.view.CardBackView.onMeasuredListener;

import com.stamp20.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CardBackActivity extends Activity implements OnClickListener {	

	private static CardBackActivity instance;
	private CardBackView cardBackView;
	private Gallery gallery_choose_back;
	private ChooseBackColorAdapter chooseBackColorAdapter;
	private Button customEnvelope;
	private Button add_blank;
	private Button add_line;
	private ImageView header_previous;
	private TextView header_title;

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
		customEnvelope = (Button)findViewById(R.id.customenvelope);
		customEnvelope.setOnClickListener(this);
		add_blank = (Button)findViewById(R.id.add_blank);
		add_blank.setOnClickListener(this);
		add_line = (Button)findViewById(R.id.add_line);
		add_line.setOnClickListener(this);
		header_previous = (ImageView)findViewById(R.id.header_previous);
		header_previous.setOnClickListener(this);
		header_title = (TextView)findViewById(R.id.header_title);
		header_title.setText("Review");
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
		gallery_choose_back.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			private String currentfiltername;
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				chooseBackColorAdapter.setSelectItem(position);
				int color = chooseBackColorAdapter.getColor(position);
				cardBackView.setCardBackColor(color);
				cardBackView.invalidate();
			}
		});
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_blank:
			cardBackView.setHasLine(false);
			cardBackView.invalidate();
			break;
		case R.id.add_line:
			cardBackView.setHasLine(true);
			cardBackView.invalidate();
			break;
		case R.id.customenvelope:
			Intent intent = new Intent();
			intent.setClass(this, CardEnvelopeActivity.class);
			startActivity(intent);
			break;
		case R.id.header_previous:
			finish();
			break;
		}
	}

}
