package com.stamp20.app.activities;

import java.math.BigDecimal;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stamp20.app.R;
import com.stamp20.app.Stamp20Application;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class BuyWithPaypalReviewActivity extends Activity implements View.OnClickListener {

    private ImageView headerPrevious;
    private TextView headerTitle;
    private ImageView tailPrevious;
    private TextView tailTitle;
    private ViewGroup header;
    private ViewGroup tail;
    private int payStyle;

    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test
     * credentials from https://developer.paypal.com
     * 
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox
    // environments.
    private static final String CONFIG_CLIENT_ID = "AWbLyslZ28sq-VkbWOs1jpCHyE-i6HON8-LMD4gIE7t2olYiGcIKi8n6yZ0NBKRY8XLTHWp5LGPX8n9j";

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
        Stamp20Application.getInstance().addActivity(this);
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
         * Change PAYMENT_INTENT_SALE to - PAYMENT_INTENT_AUTHORIZE to only
         * authorize payment and capture funds later. - PAYMENT_INTENT_ORDER to
         * create a payment for authorization and capture later via calls from
         * your server.
         * 
         * Also, to include additional payment details and an item list, see
         * getStuffToBuy() below.
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
        return new PayPalPayment(new BigDecimal("1.75"), "USD", "Order#1003352", paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.d(this, confirm.toJSONObject().toString(4));
                        Log.i(this, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         * TODO: send 'confirm' (and possibly
                         * confirm.getPayment() to your server for verification
                         * or consent completion. See
                         * https://developer.paypal.com
                         * /webapps/developer/docs/integration
                         * /mobile/verify-mobile-payment/ for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github
                         * .com/paypal/rest-api-sdk-python/tree/master
                         * /samples/mobile_backend
                         */
                        Toast.makeText(getApplicationContext(), "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e(this, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(this, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(this, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(), "Future Payment code received from PayPal", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(), "Profile Sharing code received from PayPal", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         * 
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         * 
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/
         * rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

}
