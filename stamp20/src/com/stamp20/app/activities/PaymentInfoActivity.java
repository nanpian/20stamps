package com.stamp20.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.stamp20.app.R;
import com.stamp20.app.Stamp20Application;
import com.stamp20.app.fragments.ShippingAddressFragment;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class PaymentInfoActivity extends Activity implements View.OnClickListener, OnCheckedChangeListener {

    private ShippingAddressFragment mShippingAddressFragment;
    private CheckBox sameAsCheckBox;
    private TextView headerTitle;
    private ImageView headerPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stamp20Application.getInstance().addActivity(this);
        setContentView(R.layout.activity_payment_info);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        initView();
    }

    private void initView() {
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.shipping_title_2);
        sameAsCheckBox = (CheckBox) findViewById(R.id.checkbox_same_as_shipping_address);
        sameAsCheckBox.setOnCheckedChangeListener(this);
        if (mShippingAddressFragment == null) {
            mShippingAddressFragment = (ShippingAddressFragment) getFragmentManager().findFragmentById(
                    R.id.shipping_address_fragment);
            mShippingAddressFragment.setVisible(!sameAsCheckBox.isChecked());
        }

        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if ((CheckBox) buttonView == sameAsCheckBox) {
            mShippingAddressFragment.setVisible(!isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d(this, "onClick, v" + v + ", " + v.getId());
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        }
    }

}
