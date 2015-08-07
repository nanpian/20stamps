package com.stamp20.app.activities;

import com.stamp20.app.R;
import com.stamp20.app.adapter.ChoseEnvelopeAdapter;
import com.stamp20.app.adapter.ChoseEnvelopeAdapter.NamePairs;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.CardBmpCache;
import com.stamp20.app.view.HorizontalListView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class CardEnvelopeActivity extends Activity implements OnClickListener {

    private ImageView header_previous;
    private ImageView activity_envelope_img;
    private TextView header_title;
    private Button review_button;
    private HorizontalListView gallery_choose_envelope;
    private ChoseEnvelopeAdapter choseEnvelopeAdapter;
    private Bitmap envolopBitmap;
    private CardBmpCache Envelopcache;
    private TextView name1, name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_envelope);
        envolopBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.activity_envelope_1);
        Envelopcache.getCacheInstance().putEnve(envolopBitmap);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        initView();
    }

    private void initView() {
        header_previous = (ImageView) findViewById(R.id.header_previous);
        header_previous.setOnClickListener(this);
        header_title = (TextView) findViewById(R.id.header_title);
        header_title.setText("Select Envelope");
        review_button = (Button) findViewById(R.id.review_card);
        review_button.setOnClickListener(this);

        // 改变信封的颜色
        activity_envelope_img = (ImageView) findViewById(R.id.activity_envelope_img);
        activity_envelope_img.setOnClickListener(this);
        setupEnvelopeImageHeSize();

        // 改变说明
        name1 = (TextView) findViewById(R.id.envelope_name);
        name2 = (TextView) findViewById(R.id.envelope_name2);

        choseEnvelopeAdapter = new ChoseEnvelopeAdapter(
                CardEnvelopeActivity.this);
        gallery_choose_envelope = (HorizontalListView) findViewById(R.id.activity_card_envelope);
        gallery_choose_envelope.setSelection(0);
        // gallery_choose_envelope.setAnimationDuration(3000);
        gallery_choose_envelope.setAdapter(choseEnvelopeAdapter);
        gallery_choose_envelope
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                            int position, long id) {
                        choseEnvelopeAdapter.setSelectItem(position);
                        int color = choseEnvelopeAdapter.getColor(position);
                        NamePairs namepairs = choseEnvelopeAdapter
                                .getNamePairs(position);
                        name1.setText(namepairs.name1);
                        name2.setText(namepairs.name2);
                        setEnvelopecolor(color);
                    }
                });
    }

    private void setupEnvelopeImageHeSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        Bitmap cardTemplate = BitmapFactory.decodeResource(getResources(),
                R.drawable.cards_christmas);
        int w = cardTemplate.getWidth();
        int h = cardTemplate.getHeight();

        LayoutParams params = new LayoutParams(5 * screenWidth / 6,
                (h * 5 * screenWidth) / (6 * w));
        params.topMargin = 300;
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        activity_envelope_img.setLayoutParams(params);
        activity_envelope_img.setVisibility(View.VISIBLE);

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

    // renjun1 add 改变信封颜色
    private void setEnvelopecolor(int color) {
        int mBitmapHeight = envolopBitmap.getHeight();
        int mBitmapWidth = envolopBitmap.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                envolopBitmap.getConfig());
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        Canvas canvas = new Canvas(bitmap);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(r * 1.0f / 255, g * 1.0f / 255, b * 1.0f / 255,
                1.0f);
        Paint paint = new Paint();
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(
                colorMatrix);

        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(envolopBitmap, 0, 0, paint);
        Envelopcache.getCacheInstance().putEnve(bitmap);
        activity_envelope_img.setImageBitmap(bitmap);

    }

}
