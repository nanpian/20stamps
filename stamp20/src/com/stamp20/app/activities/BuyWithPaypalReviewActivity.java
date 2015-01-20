package com.stamp20.app.activities;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.stamp20.app.R;
import com.stamp20.app.util.FontManager;

public class BuyWithPaypalReviewActivity extends Activity implements View.OnClickListener {

    private ImageView headerPrevious;
    private TextView headerTitle;
    private ImageView tailPrevious;
    private TextView tailTitle;
    private ViewGroup header;
    private ViewGroup tail;

    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test
     * credentials from https://developer.paypal.com
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox
    // environments.
    private static final String CONFIG_CLIENT_ID = "credential from developer.paypal.com";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hipster Store").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_with_paypay_review);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        initView();
        startPayService();
    }

    private void initView() {
        header = (ViewGroup) findViewById(R.id.header);
        header.setBackgroundResource(R.color.tabbar_blue_background);
        tail = (ViewGroup) findViewById(R.id.tail);
        tail.setBackgroundResource(R.color.tabbar_blue_background);
        tail.setOnClickListener(this);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.shipping_title_2);
        tailPrevious = (ImageView) findViewById(R.id.tail_icon);
        tailPrevious.setImageResource(R.drawable.icon_lock);
        tailTitle = (TextView) findViewById(R.id.tail_text);
        tailTitle.setText(R.string.pay);
    }

    private void startPayService() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.tail:
            onBuyPressed();
            break;
        }
    }

    private void onBuyPressed() {
        /* 
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to 
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         * 
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("1.75"), "USD", "Order#1003352",
                paymentIntent);
    }

}
