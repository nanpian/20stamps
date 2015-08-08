package com.stamp20.app.activities;

import com.stamp20.app.Stamp20Application;

import android.app.Activity;
import android.os.Bundle;

public class PayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Stamp20Application.getInstance().addActivity(this);
    }

}
