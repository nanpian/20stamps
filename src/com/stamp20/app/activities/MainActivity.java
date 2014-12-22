package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.adapter.ImageFilterAdapter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.StampView;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final CharSequence titleName = "Customize";

    // private Button btnSelect;
    private StampView mStampView;
    private Bitmap bitmapSource;
	private static Bitmap bitmap;
    private ImageView headerPrevious;
    private TextView headerTitle;

    private ImageView tailIcon;
    private TextView tailText;

    private Context mContext;

    private ImageFilterAdapter filterAdapter;

    private static final int SELECT_PIC = 1;
    private static final int MSG_SELECT_PICTURE = 1000;

	public static final String Tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.select_picture);
        mStampView = (StampView) findViewById(R.id.view_stamp);

        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(titleName);

        tailIcon = (ImageView) findViewById(R.id.tail_icon);
        tailIcon.setVisibility(View.GONE);
        tailText = (TextView) findViewById(R.id.tail_text);
        tailText.setText(R.string.next_review);
        findViewById(R.id.tail).setOnClickListener(this);

        Uri uri = (Uri) getIntent().getParcelableExtra("imageUri");
        Log.d(this, "uri=" + uri);
        initStampView(uri);

        LoadImageFilter();
    }

    private void LoadImageFilter() {
        // TODO Auto-generated method stub
        Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
        filterAdapter = new ImageFilterAdapter(MainActivity.this);
        gallery.setAdapter(filterAdapter);
        gallery.setSelection(2);
        gallery.setAnimationDuration(3000);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                filterAdapter.setSelectItem(position);
                IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);
                new processImageTask(MainActivity.this, filter).execute();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        // case R.id.btn_select_pic:
        // selectPicture();
        // break;
        case R.id.header_previous:
            finish();
            break;
        case R.id.tail:
            Intent intent = new Intent(this, com.stamp20.app.activities.ReviewActivity.class);
            startActivity(intent);
            break;
        default:
            break;
        }
    }

    private void initStampView(Uri uri) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SELECT_PICTURE, uri));
    }

    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SELECT_PICTURE:
                Log.d(this, "handleMessage--MSG_SELECT_PICTURE");
                Uri uri = (Uri) msg.obj;
                bitmapSource = ImageUtil.loadDownsampledBitmap(mContext, uri, 2);
                if (bitmapSource != null) {
                    mStampView.setBmpStampPhoto(bitmapSource);
                }
                break;

            default:
                break;
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PIC && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.d(this, "uri---" + uri);
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SELECT_PICTURE, uri));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
        private IImageFilter filter;
        private Activity activity = null;

        public processImageTask(Activity activity, IImageFilter imageFilter) {
            this.filter = imageFilter;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        public Bitmap doInBackground(Void... params) {
            try {
                bitmap = bitmapSource;
                Log.i(Tag, "debug exception bitmap is" + bitmap);
                if ((filter != null) && (bitmap!=null)) {
                    bitmap = filter.process(bitmap);
                    Log.i(Tag,"debug exception image is " +bitmap);
                    
                }
                return bitmap;
            } catch (Exception e) {
//            	e.printStackTrace();
//                if (bitmap != null ) {
//                	bitmap.recycle();
//                    System.gc();
//                }
            } finally {
            	Log.i(Tag,"debug exception exception is ");
//                if (bitmap != null) {
//                	bitmap = null;
//                }
            } 
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                super.onPostExecute(result);
                Log.d(this, "onPostExecute---");
                mStampView.setBmpStampPhoto(bitmap);
                mStampView.postInvalidate();

            }
        }
    }

}
