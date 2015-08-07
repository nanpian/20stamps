package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.fragments.ShippingAddressFragment;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.Constant.Pay_method;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;
import com.stamp20.app.util.UserProfile;

public class BuyWithPaypalShippingActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private ImageView headerPrevious;
    private TextView headerTitle;
    private TextView tailTitle;
    private TextView headerButton;
    private ViewGroup header;
    private ViewGroup tail;
    private TextView emailTextViewHint;
    private TextView emailTextView;
    private TextView shippingMethodStandard;
    private TextView shippingMethodPriority;
    private TextView shippingMethodOneday;
    private SharedPreferences mSharedPreferences;
    private ShippingAddressFragment mShippingAddressFragment;

    private int paystyle;
    private Pay_method paymethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        paystyle = getIntent().getIntExtra(Constant.PAY_STYLE, 0);
        setContentView(R.layout.activity_buy_with_paypay_shipping);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        paymethod = Constant.Pay_method.Standard;
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

        emailTextViewHint = (TextView) findViewById(R.id.confirmation_email_hint);
        emailTextView = (TextView) findViewById(R.id.editText_confirmation_email);
        getData(UserProfile.EMAIL, emailTextViewHint, R.string.email_confirmed, emailTextView);
        emailTextView.addTextChangedListener(emailTextViewWatcher);
        emailTextView.setOnFocusChangeListener(this);

        shippingMethodStandard = (TextView) findViewById(R.id.shipping_method_standard);
        shippingMethodPriority = (TextView) findViewById(R.id.shipping_method_priority);
        shippingMethodOneday = (TextView) findViewById(R.id.shipping_method_oneday);

        shippingMethodStandard.setOnClickListener(this);
        shippingMethodPriority.setOnClickListener(this);
        shippingMethodOneday.setOnClickListener(this);

        if (mShippingAddressFragment == null) {
            mShippingAddressFragment = (ShippingAddressFragment) getFragmentManager().findFragmentById(R.id.shipping_address_fragment);
        }
        setActivityColor();
    }

    TextWatcher emailTextViewWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() != 0) {
                emailTextViewHint.setText(R.string.email_confirmed);
            } else {
                emailTextViewHint.setText("");
            }
        }
    };

    private void getData(String userProfile, TextView hint, int resId, TextView editText) {
        String value = mSharedPreferences.getString(UserProfile.EMAIL, "");
        if (!value.isEmpty()) {
            hint.setText(resId);
            editText.setText(value);
        } else {
            hint.setText("");
        }
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
        Log.d(this, "onClick, v" + v + ", " + v.getId());
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.header_right:
        case R.id.tail:
            saveUserProfile();
            // if
            // (ValidityCheckUtil.isValidityEmailAddress(emailEditText.getText().toString()))
            // {
            Intent reviewIntent = new Intent(this, paystyle == 0 ? BuyWithPaypalReviewActivity.class : PaymentInfoActivity.class);
            startActivity(reviewIntent);
            // } else {
            // emailHint.setTextColor(Color.RED);
            // emailHint.setBackgroundColor(Color.parseColor("#FFC0CB"));
            // emailEditText.setBackgroundColor(Color.parseColor("#FFC0CB"));
            // }
            break;
        case R.id.shipping_method_standard:
            paymethod = Constant.Pay_method.Standard;
            resetPaymethodUI(paymethod);
            break;
        case R.id.shipping_method_priority:
            paymethod = Constant.Pay_method.Priority;
            resetPaymethodUI(paymethod);
            break;
        case R.id.shipping_method_oneday:
            paymethod = Constant.Pay_method.Oneday;
            resetPaymethodUI(paymethod);
            break;

        }
    }

    private void saveUserProfile() {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(UserProfile.EMAIL, emailTextView.getText().toString()).putString(UserProfile.SHIP_METHOD, paymethod.toString()).commit();
        Log.d(this, "paymethod:" + paymethod.toString());
        mShippingAddressFragment.saveUserProfile();
    }

    private void resetPaymethodUI(Pay_method method) {
        Log.d(this, "resetPaymethodUI method:" + method);
        shippingMethodStandard.setCompoundDrawablesRelative(null, null, null, null);
        shippingMethodPriority.setCompoundDrawablesRelative(null, null, null, null);
        shippingMethodOneday.setCompoundDrawablesRelative(null, null, null, null);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_checkmark);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (method == Constant.Pay_method.Standard) {
            shippingMethodStandard.setCompoundDrawablesRelative(null, null, drawable, null);
        } else if (method == Constant.Pay_method.Priority) {
            shippingMethodPriority.setCompoundDrawablesRelative(null, null, drawable, null);
        } else if (method == Constant.Pay_method.Oneday) {
            shippingMethodOneday.setCompoundDrawablesRelative(null, null, drawable, null);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if (hasFocus) {
            emailTextViewHint.setTextColor(getResources().getColor(R.color.paypal_text_hint_color_blue_focus));
        } else {
            emailTextViewHint.setTextColor(getResources().getColor(R.color.paypal_text_hint_color));
        }
    }

}
