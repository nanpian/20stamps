package com.stamp20.app.activity;

import com.stamp20.app.base.BaseTitleActivity;

import android.os.Bundle;
import android.view.View;

import com.stamp20.app.R;

public class MainActivity extends BaseTitleActivity implements View.OnClickListener {

    private static final CharSequence titleName = "Choose a photo";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        setTitle(titleName);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
