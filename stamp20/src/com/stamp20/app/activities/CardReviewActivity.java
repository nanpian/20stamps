package com.stamp20.app.activities;

import com.stamp20.app.R;
import com.stamp20.app.anim.Rotate3dAnimation;
import com.stamp20.app.data.Cart;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.CardBmpCache;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.WaitProgressBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CardReviewActivity extends Activity implements OnClickListener {
    private ImageView header_previous;
    private TextView header_title;
    private RelativeLayout review_button;
    private Button display_front;
    private Button display_back;
    private ImageView activity_envelope_img;
    private Bitmap cardBmpBack;
    private Bitmap cardBmpFront;
    private Bitmap cardBmpEnvelop;
    private boolean isFrontNow;
    private ImageView backgroundEvelopImage;
    private Button mShareDesign;
    private WaitProgressBar waitProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_review);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        initView();
    }

    private void initView() {
        mShareDesign = (Button) findViewById(R.id.card_back_share);
        mShareDesign.setOnClickListener(this);
        header_previous = (ImageView) findViewById(R.id.header_previous);
        header_previous.setOnClickListener(this);
        header_title = (TextView) findViewById(R.id.header_title);
        header_title.setText("Review");
        review_button = (RelativeLayout) findViewById(R.id.tail);
        review_button.setOnClickListener(this);
        display_front = (Button) findViewById(R.id.display_front);
        display_front.setOnClickListener(this);
        setBtnSelectState(display_front, Color.parseColor("#f1c40f"),
                R.drawable.activity_card_review_click);
        display_back = (Button) findViewById(R.id.display_back);
        display_back.setOnClickListener(this);

        backgroundEvelopImage = (ImageView) findViewById(R.id.activity_envelope_img);
        // setupBackLocAndSize();

        activity_envelope_img = (ImageView) findViewById(R.id.activity_envelope_img2);
        // setupImageLocSize();

        waitProgressBar = (WaitProgressBar) this.findViewById(R.id.progress_bar);

        CardBmpCache mCache = CardBmpCache.getCacheInstance();
        cardBmpFront = mCache.getFront();
        activity_envelope_img.setImageBitmap(cardBmpFront);
        CardBmpCache bmpCache = CardBmpCache.getCacheInstance();
        cardBmpBack = bmpCache.getBack();
        backgroundEvelopImage.setImageBitmap(bmpCache.getEnve());
        isFrontNow = true;
    }

    private void setupBackLocAndSize() {
        int W = getWindowManager().getDefaultDisplay().getWidth();// 获取屏幕高度
        Bitmap cardTemplate = BitmapFactory.decodeResource(getResources(),
                R.drawable.cards_christmas);
        int w = cardTemplate.getWidth();
        int h = cardTemplate.getHeight();
        LayoutParams params = new LayoutParams(2 * W / 3, (h * 2 * W) / (3 * w));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backgroundEvelopImage.setLayoutParams(params);
        backgroundEvelopImage.setVisibility(View.VISIBLE);
    }

    private void setupImageLocSize2() {
        int w = cardBmpBack.getWidth();
        int h = cardBmpBack.getHeight();
        LayoutParams params = new LayoutParams(w, h);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        activity_envelope_img.setLayoutParams(params);
        activity_envelope_img.setVisibility(View.VISIBLE);
    }

    private void setupImageLocSize() {
        int W = getWindowManager().getDefaultDisplay().getWidth();// 获取屏幕高度
        Bitmap cardBackBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.activity_card_back_shape);
        int w = cardBackBitmap.getWidth();
        int h = cardBackBitmap.getHeight();
        LayoutParams params = new LayoutParams(w, h);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        activity_envelope_img.setLayoutParams(params);
        activity_envelope_img.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.display_front:
            if (!isFrontNow) {
                setBtnSelectState(display_front, Color.parseColor("#f1c40f"),
                        R.drawable.activity_card_review_click);
                setBtnSelectState(display_back, Color.WHITE,
                        R.drawable.activity_card_back_blank_null);
                applyRotation(0, 90, 0);
            }
            break;
        case R.id.display_back:
            if (isFrontNow) {
                setBtnSelectState(display_back, Color.parseColor("#f1c40f"),
                        R.drawable.activity_card_back_blank_click);
                setBtnSelectState(display_front, Color.WHITE,
                        R.drawable.activity_card_review_null);
                applyRotation(0, 90, 0);
            }
            break;
        case R.id.tail:
            // add to cart
            review_button.setEnabled(false);
            waitProgressBar.setVisibility(View.VISIBLE);
            //new Thread(new Runnable() {

              //  @Override
              //  public void run() {
                    // TODO Auto-generated method stub
                    Cart cart = Cart.getInstance();
                    cart.addDesign(cardBmpFront, 20, Design.TYPE_CARD);
             //   }
           // }).start();

            Intent intent = new Intent();
            intent.setClass(this, ShopCartItemsActivity.class);
            intent.putExtra(ShopCartItemsActivity.ADD_ITEMS_TOCAET, true);
            waitProgressBar.setVisibility(View.GONE);
            startActivity(intent);
            review_button.setEnabled(true);
            finish();
            break;
        case R.id.card_back_share:
            File file = saveFontBitmap(CardBmpCache.getCacheInstance()
                    .getFront());
            Intent intentShare = new Intent();
            intentShare.setAction(Intent.ACTION_SEND);
            intentShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intentShare.setType("image/png");
            startActivity(Intent.createChooser(intentShare, "Share"));
            break;
        }
    }

    /**
     * 图片分享后有黑圆角，只能保存为png格式
     * 
     * @param bitmap
     * @return
     */
    public File saveFontBitmap(Bitmap bitmap) {
        File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath()
                + "/stamp20");
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss");
        String name = "stamp20_" + dateFormat.format(new Date()) + ".png";
        File file = new File(path, name);
        if (!path.exists()) {
            path.mkdirs();
        }

        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 更新系统图库
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
        return file;
    }

    private void applyRotation(float start, float end, final int viewId) {
        final float centerX = activity_envelope_img.getWidth() / 2.0f;
        final float centerY = activity_envelope_img.getHeight() / 2.0f;
        Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX,
                centerY, 300.0f, true);
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
                            // setupImageLocSize2();
                            isFrontNow = false;
                        } else {
                            isFrontNow = true;
                            // setupImageLocSize();
                            activity_envelope_img.setImageBitmap(cardBmpFront);
                        }
                        Rotate3dAnimation rotatiomAnimation = new Rotate3dAnimation(
                                -90, 0, centerX, centerY, 300.0f, false);
                        rotatiomAnimation.setDuration(500);
                        rotatiomAnimation
                                .setInterpolator(new DecelerateInterpolator());
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

    public void setBtnSelectState(Button button, int color, int drawableId) {
        button.setTextColor(color);
        Drawable drawable = this.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        button.setCompoundDrawables(drawable, null, null, null);
        button.setCompoundDrawablePadding(5);
    }

}
