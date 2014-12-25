package com.stamp20.app.activities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.stamp20.app.R;
import com.stamp20.app.R.layout;
import com.stamp20.app.facebook.FbAlbumResult;
import com.stamp20.app.facebook.FbAlbumStore;
import com.stamp20.app.facebook.FbPhotoResult;
import com.stamp20.app.imageloader.ChildAdapter;
import com.stamp20.app.imageloader.GroupAdapter;
import com.stamp20.app.imageloader.ImageBean;
import com.stamp20.app.util.Image;
import com.stamp20.app.util.Log;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowFBImageActivity extends Activity implements OnItemClickListener {
	private static final String TAG = "Show_Facebook_ImageActivity";
	private final static int LOAD_OK = 1;
	private final static int FILE_TMP_OK = 2;
	
    private GridView mGridView;
    private String mParseCmd;
    private ChildAdapter adapter;
    private ImageView headerPrevious = null;
    private TextView headerTitle = null;
    
    FbPhotoResult mAlbumPhotos = null;
    private Dialog progressDialog;
    
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case LOAD_OK:
            	Log.d(TAG,"Receive Msg LOAD_OK");
                adapter = new ChildAdapter(ShowFBImageActivity.this, mAlbumPhotos, mGridView);
                mGridView.setAdapter(adapter);
                mGridView.setOnItemClickListener(ShowFBImageActivity.this);
                break;
            case FILE_TMP_OK:
            	Log.d(TAG,"Recevie Msg FILE_TMP_OK");
            	break;
            }
        }

    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image_grid);
		
		headerPrevious = (ImageView) findViewById(R.id.header_previous);
		headerPrevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.select_a_picture);

        mGridView = (GridView) findViewById(R.id.child_grid);
        mParseCmd = getIntent().getStringExtra("parse_cmd");

        Log.d(TAG, "receiver cmd: " + mParseCmd);
        
        getFacebookPhotos();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	
    	String url = mAlbumPhotos.get(position).getSourceImageUrl();
    	
    	Log.d(TAG, "photo position: " + position + " url: " + url);
    	
    	progressDialog = ProgressDialog.show(this, "", "Download...", true);
    	
    	new DownloadImageTask().execute(url);                
    }
    
    private class DownloadImageTask extends AsyncTask<String, Void, Intent>{

		@Override
		protected Intent doInBackground(String... urls) {
			Bitmap tmp = Image.LoadImage(urls[0]);
	    	
	    	if(tmp == null)
	    		return null;
	    	
	    	File file = new File(getFilesDir(),"tmp.jpg");
	    	if(file.exists())
	    		file.delete();
	    	try{
	    		FileOutputStream out = new FileOutputStream(file);
	    		tmp.compress(CompressFormat.JPEG, 90, out);
	    		out.flush();
	    		out.close();
	    		Log.d(TAG, "Image tmp file is saved");
	    	} catch (IOException e){
	    		e.printStackTrace();
	    	}
	    	
	        Uri uri = Uri.fromFile(file);
	        Intent intent = new Intent(ShowFBImageActivity.this, MainActivity.class);
	        intent.putExtra("imageUri", uri);
	        
			return intent;
		}
		@Override
		protected void onPostExecute(Intent intent){
			progressDialog.dismiss();
			if(intent == null)
				return;
			startActivity(intent);
		}
    	
    }
    private void getFacebookPhotos() {
    	new Thread(new Runnable(){

			@Override
			public void run() {
				mAlbumPhotos = FbAlbumStore.getPhotos(mParseCmd);				
				mHandler.sendEmptyMessage(LOAD_OK);
			}
    		
    	}).start();		
	}
}
