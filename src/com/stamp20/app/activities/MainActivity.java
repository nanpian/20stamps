package com.stamp20.app.activities;

import java.io.FileNotFoundException;

import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;

import com.stamp20.app.adapter.ImageFilterAdapter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.filter.Image;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.StampView;

public class MainActivity extends BaseTitleActivity implements View.OnClickListener {

    private static final CharSequence titleName = "Choose a photo";

    private Button btnSelect;
    private Button btnViewCard;
    private StampView mStampView;
    private Bitmap bitmapSource;

    private static final int SELECT_PIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            selectPic();
            break;
        case R.id.btn_view_card:
            break;

        default:
            break;
        }
    }

    private void selectPic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PIC && resultCode == RESULT_OK) {
            Log.d("MainActivity", "resultCode == RESULT_OK");
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                bitmapSource = BitmapFactory.decodeStream(cr.openInputStream(uri)).copy(Bitmap.Config.ARGB_8888, true);
                // draw this bitmap to canvas over stamp background
                mStampView.setBmpStamp(bitmapSource);
                mStampView.invalidate();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
            Image img = null;
            try {
                // Bitmap bitmap =
                // BitmapFactory.decodeResource(activity.getResources(),
                // R.drawable.image);
                Bitmap bitmap = bitmapSource;
                img = new Image(bitmap);
                if (filter != null) {
                    img = filter.process(img);
                    img.copyPixelsFromBuffer();
                }
                return img.getImage();
            } catch (Exception e) {
                if (img != null && img.destImage.isRecycled()) {
                    img.destImage.recycle();
                    img.destImage = null;
                    System.gc(); 
                }
            } finally {
                if (img != null && img.image.isRecycled()) {
                    img.image.recycle();
                    img.image = null;
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
