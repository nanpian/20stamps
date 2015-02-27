package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.fragments.ShippingAddressFragment;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;

public class BuyWithPaypalShippingActivity extends Activity implements View.OnClickListener{

    private ImageView headerPrevious;
    private TextView headerTitle;
    private TextView tailTitle;
    private TextView headerButton;
    private ViewGroup header;
    private ViewGroup tail;
    
    private TextView shippingMethodStandard;
    private TextView shippingMethodPriority;
    private TextView shippingMethodOneday;
    
    private ShippingAddressFragment mShippingAddressFragment;

    private int paystyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        paystyle = getIntent().getIntExtra(Constant.PAY_STYLE, 0);
        setContentView(R.layout.activity_buy_with_paypay_shipping);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        init();
    }

    private void init() {
        header = (ViewGroup) findViewById(R.id.header);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.shipping_title_1);
        headerButton = (TextView) findViewById(R.id.header_right);
        headerButton.setOnClickListener(this);
        headerButton.setVisibility(View.VISIBLE);
        tail = (ViewGroup) findViewById(R.id.tail);

        tail.setOnClickListener(this);
        tailTitle = (TextView) findViewById(R.id.tail_text);
        tailTitle.setText(R.string.next_review);

        shippingMethodStandard = (TextView) findViewById(R.id.shipping_method_standard);
        shippingMethodPriority = (TextView) findViewById(R.id.shipping_method_priority);
        shippingMethodOneday = (TextView) findViewById(R.id.shipping_method_oneday);
        
        if(mShippingAddressFragment == null){
            mShippingAddressFragment = (ShippingAddressFragment) getFragmentManager().findFragmentById(R.id.shipping_address_fragment);
        }
        setActivityColor();
    }

    private void setActivityColor() {
        // TODO Auto-generated method stub
        switch (paystyle) {
        case 0:
            header.setBackgroundResource(R.color.tabbar_blue_background);
            tail.setBackgroundResource(R.color.tabbar_blue_background);
            break;

        case 1:
            header.setBackgroundResource(R.color.activity_background_gray);
            tail.setBackgroundResource(R.color.tabbar_green_background);
            break;
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.header_right:
        case R.id.tail:
//            if (ValidityCheckUtil.isValidityEmailAddress(emailEditText.getText().toString())) {
                Intent reviewIntent = new Intent(this, paystyle == 0 ? BuyWithPaypalReviewActivity.class : PaymentInfoActivity.class);
                startActivity(reviewIntent);
//            } else {
//                emailHint.setTextColor(Color.RED);
//                emailHint.setBackgroundColor(Color.parseColor("#FFC0CB"));
//                emailEditText.setBackgroundColor(Color.parseColor("#FFC0CB"));
//            }
            break;
        }
    }

}
