package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.adapter.ChooseBackColorAdapter;
import com.stamp20.app.util.CardBmpCache;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.CardBackView;
import com.stamp20.app.view.CardBackView2;
import com.stamp20.app.view.HorizontalListView;

public class CardBackActivity extends Activity implements OnClickListener {

    private static CardBackActivity instance;
    private CardBackView2 cardBackView;
    private HorizontalListView gallery_choose_back;
    private ChooseBackColorAdapter chooseBackColorAdapter;
    private Button customEnvelope;
    private Button add_blank;
    private Button add_line;
    private ImageView header_previous;
    private TextView header_title;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_back);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        instance = this;
        Intent intent = getIntent();
        mImageUri = intent.getParcelableExtra("imageUri");
        initView();
    }

    private void initView() {
        cardBackView = (CardBackView2) findViewById(R.id.cardbackview);
        cardBackView.setImageUri(mImageUri);
        customEnvelope = (Button) findViewById(R.id.customenvelope);
        customEnvelope.setOnClickListener(this);
        add_blank = (Button) findViewById(R.id.add_blank);
        add_blank.setOnClickListener(this);
        setBtnSelectState(add_blank, Color.parseColor("#f1c40f"),
                R.drawable.activity_card_back_blank_click);
        add_line = (Button) findViewById(R.id.add_line);
        add_line.setOnClickListener(this);
        header_previous = (ImageView) findViewById(R.id.header_previous);
        header_previous.setOnClickListener(this);
        header_title = (TextView) findViewById(R.id.header_title);
        header_title.setText("Customize Back");
        /*
         * cardBackView.setOnMeasuredListener(new onMeasuredListener(){
         * 
         * @Override public void onMeasuredListener(int width, int height) {
         * LayoutParams params = null; params.height = height; params.width =
         * width; cardBackView.setLayoutParams(params);
         * cardBackView.postInvalidate(); } });
         */
        chooseBackColorAdapter = new ChooseBackColorAdapter(
                CardBackActivity.this);
        chooseBackColorAdapter.setImageUri(mImageUri);
        gallery_choose_back = (HorizontalListView) findViewById(R.id.activity_card_back_select_back);
        gallery_choose_back.setSelection(0);
        // gallery_choose_back.setAnimationDuration(3000);
        gallery_choose_back.setAdapter(chooseBackColorAdapter);
        gallery_choose_back
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    private String currentfiltername;

                    public void onItemClick(AdapterView<?> arg0, View arg1,
                            int position, long id) {
                        chooseBackColorAdapter.setSelectItem(position);
                        int color = chooseBackColorAdapter.getColor(position);
                        cardBackView.setCardBackColor(color, position);
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
        super.onDestroy();
    }

    public void setBtnSelectState(Button button, int color, int drawableId) {
        button.setTextColor(color);
        Drawable drawable = this.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        button.setCompoundDrawables(drawable, null, null, null);
        button.setCompoundDrawablePadding(5);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.add_blank:
            cardBackView.setHasLine(false);
            setBtnSelectState(add_blank, Color.parseColor("#f1c40f"),
                    R.drawable.activity_card_back_blank_click);
            setBtnSelectState(add_line, Color.WHITE,
                    R.drawable.activity_card_back_blank_icon);
            cardBackView.invalidate();
            break;
        case R.id.add_line:
            cardBackView.setHasLine(true);
            setBtnSelectState(add_blank, Color.WHITE,
                    R.drawable.activity_card_back_blank_null);
            setBtnSelectState(add_line, Color.parseColor("#f1c40f"),
                    R.drawable.activity_card_back_blank_icon_selected);
            cardBackView.invalidate();
            break;
        case R.id.customenvelope:
            cardBackView.invalidate();
            cardBackView.buildDrawingCache();
            Bitmap bitmap = cardBackView.getDrawingCache();
            CardBmpCache bmpCache = CardBmpCache.getCacheInstance();
            bmpCache.putBack(bitmap);
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), CardEnvelopeActivity.class);
            startActivity(intent);
            /*
             * cardBackView.setCaptureBmp(true); cardBackView.invalidate();
             * cardBackView.setonGeneratedCardBackBmpListener(new
             * onGeneratedCardBackBmpListener() {
             * 
             * @Override public void onGeneratedCardBackBmp() { Intent intent =
             * new Intent(); intent.setClass(getApplicationContext(),
             * CardEnvelopeActivity.class); startActivity(intent); }
             * 
             * });
             */
            break;
        case R.id.header_previous:
            finish();
            break;
        }
    }

}
