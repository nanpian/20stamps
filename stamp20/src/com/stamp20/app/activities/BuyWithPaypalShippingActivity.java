package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
//import com.stamp20.app.util.KeyboardUtil;
import com.stamp20.app.util.Log;
import com.stamp20.app.util.UserProfile;
import com.stamp20.app.util.ValidityCheckUtil;
import com.stamp20.app.wheel.adapter.AbstractWheelTextAdapter;
import com.stamp20.app.wheel.widget.OnWheelChangedListener;
import com.stamp20.app.wheel.widget.WheelView;

public class BuyWithPaypalShippingActivity extends Activity implements View.OnClickListener, OnWheelChangedListener, OnTouchListener {

    private ImageView headerPrevious;
    private TextView headerTitle;
    private TextView tailTitle;
    private TextView headerButton;
    private ViewGroup header;
    private ViewGroup tail;

    private TextView emailHint;
    private EditText emailEditText;
    private TextView firstNameHint;
    private EditText firstNameEditText;
    private TextView secondNameHint;
    private EditText secondNameEditText;
    private TextView addressLine1Hint;
    private EditText addressLine1EditText;
    private TextView addressLine2Hint;
    private EditText addressLine2EditText;
    private TextView cityHint;
    private EditText cityEditText;
    private TextView stateHint;
    private TextView stateEditText;
    private TextView zipHint;
    private EditText zipEditText;
    private TextView shippingMethodStandard;
    private TextView shippingMethodPriority;
    private TextView shippingMethodOneday;

    private SoundPool sp;
    private int sound;

    private PopupWindow mChooseCityPopupWindow;
    private TextView mDoneTextView;
    private int selectedCityIndex = 0;
    String cities[];

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

        emailHint = (TextView) findViewById(R.id.confirmation_email_hint);
        emailEditText = (EditText) findViewById(R.id.editText_confirmation_email);
        // emailEditText.setOnFocusChangeListener(this);
        firstNameHint = (TextView) findViewById(R.id.shipping_address_first_name_hint);
        firstNameEditText = (EditText) findViewById(R.id.shipping_address_first_name);
        // firstNameEditText.setOnFocusChangeListener(this);
        secondNameHint = (TextView) findViewById(R.id.shipping_address_second_name_hint);
        secondNameEditText = (EditText) findViewById(R.id.shipping_address_second_name);
        // secondNameEditText.setOnFocusChangeListener(this);
        addressLine1Hint = (TextView) findViewById(R.id.shipping_address_line1_hint);
        addressLine1EditText = (EditText) findViewById(R.id.shipping_address_line1);
        // addressLine1EditText.setOnFocusChangeListener(this);
        addressLine2Hint = (TextView) findViewById(R.id.shipping_address_line2_hint);
        addressLine2EditText = (EditText) findViewById(R.id.shipping_address_line2);
        // addressLine2EditText.setOnFocusChangeListener(this);
        cityHint = (TextView) findViewById(R.id.shipping_address_city_hint);
        cityEditText = (EditText) findViewById(R.id.shipping_address_city);
        // cityEditText.setOnFocusChangeListener(this);
        stateHint = (TextView) findViewById(R.id.shipping_address_state_hint);
        stateEditText = (TextView) findViewById(R.id.shipping_address_state);
        stateEditText.setOnClickListener(this);
        // stateEditText.setOnFocusChangeListener(this);
        zipHint = (TextView) findViewById(R.id.shipping_address_zip_hint);
        zipEditText = (EditText) findViewById(R.id.shipping_address_zip);
        // zipEditText.setOnTouchListener(this);
        // zipEditText.setOnFocusChangeListener(this);
        shippingMethodStandard = (TextView) findViewById(R.id.shipping_method_standard);
        shippingMethodPriority = (TextView) findViewById(R.id.shipping_method_priority);
        shippingMethodOneday = (TextView) findViewById(R.id.shipping_method_oneday);

        addEdittextChangeListener();
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

        sound = sp.load(this, R.raw.scroll, 1);
        cities = getResources().getStringArray(R.array.city_names);
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
        getData(UserProfile.USER_EMAIL, emailHint, R.string.email_confirmed, emailEditText);
        getData(UserProfile.USER_FIRST_NAME, firstNameHint, R.string.first_name, firstNameEditText);
        getData(UserProfile.USER_SECOND_NAME, secondNameHint, R.string.second_name, secondNameEditText);
        getData(UserProfile.USER_ADDRESS_LINE_1, addressLine1Hint, R.string.address_line_1, addressLine1EditText);
        getData(UserProfile.USER_ADDRESS_LINE_2, addressLine2Hint, R.string.address_line_2, addressLine2EditText);
        getData(UserProfile.USER_CITY, cityHint, R.string.address_city, cityEditText);
        getData(UserProfile.USER_STATE, stateHint, R.string.address_state, stateEditText);
        getData(UserProfile.USER_ZIP, zipHint, R.string.address_zip, zipEditText);

        if (ValidityCheckUtil.isValidityEmailAddress(getUserProfile(UserProfile.USER_EMAIL))) {
            emailHint.setTextColor(Color.parseColor("#009cde"));
            emailHint.setBackgroundColor(Color.parseColor("#FFFFFF"));
            emailEditText.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            emailHint.setTextColor(Color.RED);
            emailHint.setBackgroundColor(Color.parseColor("#FFC0CB"));
            emailEditText.setBackgroundColor(Color.parseColor("#FFC0CB"));
        }

    }

    private void getData(String userProfile, TextView hint, int resId, EditText editText) {
        String value = getUserProfile(userProfile);
        if (!value.isEmpty()) {
            hint.setText(resId);
            editText.setText(value);
        } else {
            hint.setText("");
        }
    }

    private void getData(String userProfile, TextView hint, int resId, TextView editText) {
        String value = getUserProfile(userProfile);
        if (!value.isEmpty()) {
            hint.setText(resId);
            editText.setText(value);
        } else {
            hint.setText("");
        }
    }

    private void addEdittextChangeListener() {
        emailEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    emailHint.setText("");
                } else {
                    emailHint.setText(R.string.email_confirmed);
                }
                saveUserProfile(UserProfile.USER_EMAIL, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        firstNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    firstNameHint.setText("");
                } else {
                    firstNameHint.setText(R.string.first_name);
                }
                saveUserProfile(UserProfile.USER_FIRST_NAME, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        secondNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    secondNameHint.setText("");
                } else {
                    secondNameHint.setText(R.string.second_name);
                }
                saveUserProfile(UserProfile.USER_SECOND_NAME, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        addressLine1EditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    addressLine1Hint.setText("");
                } else {
                    addressLine1Hint.setText(R.string.address_line_1);
                }
                saveUserProfile(UserProfile.USER_ADDRESS_LINE_1, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        addressLine2EditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    addressLine2Hint.setText("");
                } else {
                    addressLine2Hint.setText(R.string.address_line_2);
                }
                saveUserProfile(UserProfile.USER_ADDRESS_LINE_2, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        cityEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    cityHint.setText("");
                } else {
                    cityHint.setText(R.string.address_city);
                }
                saveUserProfile(UserProfile.USER_CITY, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        stateEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    stateHint.setText("");
                } else {
                    stateHint.setText(R.string.address_state);
                }
                saveUserProfile(UserProfile.USER_STATE, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        zipEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() == 0) {
                    zipHint.setText("");
                } else {
                    zipHint.setText(R.string.address_zip);
                }
                saveUserProfile(UserProfile.USER_ZIP, s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void saveUserProfile(String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Editor e = sp.edit();
        e.putString(key, value);
        e.commit();
    }

    private String getUserProfile(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString(key, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.header_right:
        case R.id.tail:
            if (ValidityCheckUtil.isValidityEmailAddress(emailEditText.getText().toString())) {
                Intent reviewIntent = new Intent(this, paystyle == 0 ? BuyWithPaypalReviewActivity.class : PaymentInfoActivity.class);
                startActivity(reviewIntent);
            } else {
                emailHint.setTextColor(Color.RED);
                emailHint.setBackgroundColor(Color.parseColor("#FFC0CB"));
                emailEditText.setBackgroundColor(Color.parseColor("#FFC0CB"));
            }
            break;
        case R.id.choose_city_done:
            Log.d(this, "choose_city_done");
            if (mChooseCityPopupWindow != null) {
                mChooseCityPopupWindow.dismiss();
            }
            String city = cities[selectedCityIndex];
            stateEditText.setText(city);
            saveUserProfile(UserProfile.USER_STATE, city);
            break;
        case R.id.shipping_address_state:
            getPopupWindowInstance();
            mChooseCityPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            break;
        }
    }

    /**
     * 获取PopupWindow实例
     */
    private void getPopupWindowInstance() {
        if (null != mChooseCityPopupWindow) {
            mChooseCityPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    /*
     * 创建PopupWindow
     */
    private void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupWindow = layoutInflater.inflate(R.layout.choose_city_popup_window, null);
        final WheelView cities = (WheelView) popupWindow.findViewById(R.id.city_picker_wheel_view);
        mDoneTextView = (TextView) popupWindow.findViewById(R.id.choose_city_done);
        mDoneTextView.setOnClickListener(this);
        FontManager.changeFontsBlod(this, (LinearLayout) popupWindow);
        cities.setViewAdapter(new CityAdapter(this));
        cities.setVisibleItems(5);
        cities.addChangingListener(this);
        int mWindowWidth = getWindowManager().getDefaultDisplay().getWidth();
        int mWindowHeight = getWindowManager().getDefaultDisplay().getHeight();

        mChooseCityPopupWindow = new PopupWindow(popupWindow, mWindowWidth, mWindowHeight * 2 / 5);
    }

    /**
     * Adapter for countries
     */
    private class CityAdapter extends AbstractWheelTextAdapter {
        // City names
        // final String cities[] = new String[] {"New York", "Washington",
        // "Chicago", "Atlanta", "Orlando"};
        // final String cities[] =
        // getResources().getStringArray(R.array.city_names);

        /**
         * Constructor
         */
        protected CityAdapter(Context context) {
            super(context, R.layout.city_layout, NO_RESOURCE);
            // FontManager.changeFonts(this, );
            setItemTextResource(R.id.city_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return cities.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return cities[index];
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        // 播放声音
        Log.d(this, "onChanged, oldValue: " + oldValue + ", newValue: " + newValue);
        selectedCityIndex = newValue;
        sp.play(sound, 1, 1, 0, 0, 1);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // // TODO Auto-generated method stub
        // // 这样是在触摸到控件时，软键盘才会显示出来
        // Log.d(this, "onTouch......");
        // // int inputback = zipEditText.getInputType();
        // zipEditText.setInputType(EditorInfo.TYPE_NULL);
        // new KeyboardUtil(this, this, zipEditText).showKeyboard();
        // // zipEditText.setInputType(inputback);
        return false;
    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // // TODO Auto-generated method stub
    // //屏蔽后退键
    // if(KeyEvent.KEYCODE_BACK == event.getKeyCode())
    // {
    // return true;//阻止事件继续向下分发
    // }
    // return super.onKeyDown(keyCode, event);
    // }

}
