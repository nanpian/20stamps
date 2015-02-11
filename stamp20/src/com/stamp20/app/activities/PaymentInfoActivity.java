package com.stamp20.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.stamp20.app.R;
import com.stamp20.app.fragments.ShippingAddressFragment;
import com.stamp20.app.util.FontManager;

public class PaymentInfoActivity extends Activity implements OnCheckedChangeListener{

    private ShippingAddressFragment mShippingAddressFragment;
    private CheckBox sameAsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        initView();
    }

    private void initView() {
        sameAsCheckBox = (CheckBox) findViewById(R.id.checkbox_same_as_shipping_address);
        sameAsCheckBox.setOnCheckedChangeListener(this);
        if (mShippingAddressFragment == null) {
            mShippingAddressFragment = (ShippingAddressFragment) getFragmentManager().findFragmentById(R.id.shipping_address_fragment);
            mShippingAddressFragment.setVisible(!sameAsCheckBox.isChecked());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if ((CheckBox)buttonView == sameAsCheckBox){
            mShippingAddressFragment.setVisible(!isChecked);
        }
    }


}
