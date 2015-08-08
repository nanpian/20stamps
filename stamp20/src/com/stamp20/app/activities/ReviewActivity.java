package com.stamp20.app.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lenovo.jni.ImageUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.Stamp20Application;
import com.stamp20.app.anim.AnimationUtil;
import com.stamp20.app.data.Cart;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class ReviewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ReviewActivity";

    private BitmapCache mCache = null;

    private ImageView mReviewer = null;

    private ImageView headerPrevious = null;
    private TextView headerTitle = null;

    private TextView tailText = null;

    // private Button saveDesign;
    private Button shareDesign;

    private ImageView blurBlackground;
    private View reviewForeground;
    private BlurBackground blurProcess = null;

    private ProgressBar waitProgressBar;
    private RelativeLayout reviewButton;

    private static final int ADD_TO_CART_SUCCESS = 1001;

    Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ADD_TO_CART_SUCCESS:
                Intent intent = new Intent();
                intent.setClass(ReviewActivity.this, ShopCartItemsActivity.class);
                waitProgressBar.setVisibility(View.GONE);
                tailText.setVisibility(View.VISIBLE);
                reviewButton.setEnabled(true);
                startActivity(intent);
                // finish();
                break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActionBar().hide();
        Stamp20Application.getInstance().addActivity(this);
        setContentView(R.layout.review_activity);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);

        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerTitle = (TextView) findViewById(R.id.header_title);
        tailText = (TextView) findViewById(R.id.tail_text);

        headerPrevious.setOnClickListener(this);

        reviewButton = (RelativeLayout) this.findViewById(R.id.tail);
        reviewButton.setOnClickListener(this);

        headerTitle.setText(R.string.review_title);

        // saveDesign = (Button) findViewById(R.id.btn_save_design);
        shareDesign = (Button) findViewById(R.id.btn_share_design);
        // saveDesign.setOnClickListener(this);
        shareDesign.setOnClickListener(this);

        mCache = BitmapCache.getCache();
        mReviewer = (ImageView) findViewById(R.id.reviewer);
        mReviewer.setImageBitmap(mCache.get());
        // mUploadToParse = new UploadToParse(mCache.get());
        // mUploadToParse.uploadImage();

        waitProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        blurBlackground = (ImageView) findViewById(R.id.blur_background);
        reviewForeground = findViewById(R.id.review_root);
        blurProcess = new BlurBackground(blurBlackground);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.tail:
            tailText.setVisibility(View.GONE);
            reviewButton.setEnabled(false);
            waitProgressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Cart cart = Cart.getInstance();
                    if (!cart.isEmpty()) {
                        cart.addDesign(mCache.get(), 20, Design.TYPE_STAMP);
                        Message message = new Message();
                        message.what = ADD_TO_CART_SUCCESS;
                        uiHandler.sendMessageDelayed(message, 50);
                    }
                }
            }).start();

            break;
        // case R.id.btn_save_design:
        // File pic = saveBitmapToPic(mCache.get());
        // if (pic == null)
        // Log.e(TAG, "save design error");
        // else {
        // Log.d(TAG, "save design ok");
        // /*
        // * Intent send = new Intent();
        // * send.setAction(Intent.ACTION_SEND); send.setType("image/*");
        // * send.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pic));
        // * startActivity(Intent.createChooser(send, "Share"));
        // */
        // Toast.makeText(this, pic.getName() + " saved",
        // Toast.LENGTH_SHORT).show();
        // }
        // break;
        case R.id.btn_share_design:
            File tmp = saveBitmapToTmp(mCache.get());
            if (tmp == null) {
                Log.e(TAG, "save tmp design error");
            } else {
                Log.d(TAG, "save tmp design ok");
                Intent send = new Intent();
                send.setAction(Intent.ACTION_SEND);
                send.setType("image/*");
                final Uri uri = FileProvider.getUriForFile(this, "com.stamp20.fileprovider", tmp);
                send.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(send, "Share"));
            }
            break;
        default:
            break;
        }

    }

    private float mStartAlpha = 0.001f;
    private float mEndAlpha = 1.0f;
    private long mDuration = 1000;

    private void closeBlurWindow() {
        blurBlackground.startAnimation(AnimationUtil.getAlphaAnimation(mEndAlpha, mStartAlpha, false, mDuration,
                new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        blurBlackground.setClickable(false);
                        blurBlackground.setVisibility(View.GONE);
                        reviewForeground.setVisibility(View.VISIBLE);
                    }
                }));
    }

    private void startBlurWindow() {
        reviewForeground.setVisibility(View.GONE);
        blurBlackground.setVisibility(View.VISIBLE);
        blurBlackground.startAnimation(AnimationUtil.getAlphaAnimation(mStartAlpha, mEndAlpha, false, mDuration,
                new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        blurBlackground.setClickable(true);
                    }
                }));
    }

    private class BlurBackground extends AsyncTask<View, Void, Bitmap> {
        ImageView blur;

        BlurBackground(ImageView blur) {
            this.blur = blur;
        }

        @Override
        protected Bitmap doInBackground(View... views) {
            // TODO Auto-generated method stub
            views[0].buildDrawingCache();
            Bitmap source = views[0].getDrawingCache();
            return ImageUtils.fastBlur(source, 100);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            blur.setImageBitmap(result);
        }
    }

    private File saveBitmapToPic(Bitmap src) {
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/stamp20");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        String name = "stamp20_" + sdf.format(new Date(System.currentTimeMillis())) + ".jpeg";
        Log.d(TAG, "path: " + path + ", name: " + name);
        File file = new File(path, name);
        try {
            path.mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            src.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * try{ MediaStore.Images.Media.insertImage(getContentResolver(),
         * file.getAbsolutePath(), name, null); }catch(FileNotFoundException e){
         * Log.e(TAG, "insert image error"); }
         */
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return file;
    }

    private File saveBitmapToTmp(Bitmap src) {
        File path = new File(getFilesDir().getAbsolutePath() + "/tmp");
        path.mkdirs();
        File file = new File(path, "tmp_review.jpeg");
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            src.compress(CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
