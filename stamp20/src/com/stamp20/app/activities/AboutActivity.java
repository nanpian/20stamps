package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.adapter.AboutAdapter;
import com.stamp20.app.util.FontManager;

public class AboutActivity extends Activity implements OnItemClickListener {
    ListView aboutListview;
    AboutAdapter mAboutAdapter;
    private TextView headerTitle;
    private static String titleName = "About";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(titleName);
        aboutListview = (ListView) findViewById(R.id.about_list);
        mAboutAdapter = new AboutAdapter(this);
        aboutListview.setAdapter(mAboutAdapter);
        aboutListview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent();
        switch (position) {
        case 0:
            detailIntent = new Intent(this, AboutDetailsActivity.class);
            detailIntent.putExtra("url", "http://www.20stamps.com/faq");
            detailIntent.putExtra("title", getResources().getString(R.string.about_1));
            break;
        case 1:
            detailIntent = new Intent(this, AboutDetailsActivity.class);
            detailIntent.putExtra("url", "http://www.20stamps.com/faq");
            detailIntent.putExtra("title", getResources().getString(R.string.about_2));
            break;
        case 2:
            // 发送邮件
            detailIntent = new Intent(Intent.ACTION_SENDTO);
            detailIntent.setData(Uri.parse("mailto:support@20stamps.com"));
            break;
        case 3:
            // 访问网站
            detailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.20stamps.com"));
            break;
        case 4:
            //terms of service
            detailIntent = new Intent(this, AboutDetailsActivity.class);
            detailIntent.putExtra("url", "http://www.20stamps.com/terms");
            detailIntent.putExtra("title", getResources().getString(R.string.about_5));
            break;
        case 5:
            //privacy policy
            detailIntent = new Intent(this, AboutDetailsActivity.class);
            detailIntent.putExtra("url", "http://www.20stamps.com/privacy");
            detailIntent.putExtra("title", getResources().getString(R.string.about_6));
            break;
        case 6:
            //shipping & return policy
            detailIntent = new Intent(this, AboutDetailsActivity.class);
            detailIntent.putExtra("url", "http://www.20stamps.com/shipping");
            detailIntent.putExtra("title", getResources().getString(R.string.about_7));
            break;
        case 7:
            break;
        case 8:
            break;
        case 9:
            break;
        }
        startActivity(detailIntent);
    }

}
