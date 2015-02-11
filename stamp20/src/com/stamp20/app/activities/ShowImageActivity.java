package com.stamp20.app.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.activities.MainActivity;
import com.stamp20.app.imageloader.ChildAdapter;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.PhotoFromWhoRecorder;

public class ShowImageActivity extends Activity implements OnItemClickListener {
    private GridView mGridView;
    private List<Uri> list;
    private ChildAdapter adapter;
    private ImageView headerPrevious = null;
    private TextView headerTitle = null;

    private static final int MSG_SELECT_PICTURE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_grid);
        FontManager.changeFonts((LinearLayout)findViewById(R.id.root), this);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.select_a_picture);

        mGridView = (GridView) findViewById(R.id.child_grid);
        list = getIntent().getParcelableArrayListExtra("data");

        adapter = new ChildAdapter(this, list, mGridView);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Uri uri = list.get(position);
//        Intent intent = new Intent(ShowImageActivity.this, MainActivity.class);
        String fromStampOrCard = PhotoFromWhoRecorder.readFromWhich(getApplicationContext());
        Intent intent = null;
        if (fromStampOrCard == null || fromStampOrCard.endsWith("stamp")) {
            intent = new Intent(ShowImageActivity.this, MainEffect.class);
            intent.putExtra("imageUri", uri);
        } else if (fromStampOrCard.equals("card")) {
        	intent = new Intent(ShowImageActivity.this, CardEffect.class);
            intent.putExtra("imageUri", uri);
        }
        try {
            startActivity(intent);
        } catch (Exception e ) {
        	e.printStackTrace();
        }
    }
}
