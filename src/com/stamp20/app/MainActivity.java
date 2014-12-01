package com.stamp20.app;

import android.os.Bundle;
import android.view.View;

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
