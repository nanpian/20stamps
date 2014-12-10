package com.stamp20.app.activities;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;

import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import com.stamp20.app.adapter.ImageFilterAdapter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.StampView;

public class MainActivity extends BaseTitleActivity implements View.OnClickListener {

    private static final CharSequence titleName = "Choose a photo";

    private Button btnSelect;
    private Button btnViewCard;
    private StampView mStampView;
    private Bitmap bitmapSource;
    
    private Context mContext;

    private static final int SELECT_PIC = 1;
    private static final int MSG_SELECT_PICTURE = 1000;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.select_picture);
        mStampView = (StampView) findViewById(R.id.view_stamp);
        btnSelect = (Button) findViewById(R.id.btn_select_pic);
        btnSelect.setOnClickListener(this);
        btnViewCard = (Button) findViewById(R.id.btn_view_card);
        btnViewCard.setOnClickListener(this);

        getActionBar().hide();
        setTitle(titleName);

        LoadImageFilter();
    }

    private void LoadImageFilter() {
        // TODO Auto-generated method stub
        Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
        final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(MainActivity.this);
        gallery.setAdapter(new ImageFilterAdapter(this));
        gallery.setSelection(2);
        gallery.setAnimationDuration(3000);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);
                new processImageTask(MainActivity.this, filter).execute();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_select_pic:
            selectPicture();
            break;
        case R.id.btn_view_card:
            break;

        default:
            break;
        }
    }

    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC);
    }


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SELECT_PICTURE:
                Log.d(this, "handleMessage--MSG_SELECT_PICTURE");
                Uri uri = (Uri) msg.obj;
                bitmapSource = ImageUtil.loadDownsampledBitmap(mContext, uri, 2);
                mStampView.setBmpStamp(bitmapSource);
                mStampView.invalidate();
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
            Bitmap img = null;
            try {
                Bitmap bitmap = bitmapSource;
                if (filter != null) {
                    img = filter.process(bitmap);
                }
                return img;
            } catch (Exception e) {
                if (img != null ) {
                    img.recycle();
                    System.gc(); 
                }
            } finally {
                if (img != null ) {
                    img.recycle();
                    System.gc(); 
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                super.onPostExecute(result);
                Log.d(this, "onPostExecute---");
                mStampView.setBmpStamp(result);
            }
        }
    }

}
