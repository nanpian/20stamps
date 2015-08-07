package com.stamp20.app.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.data.UserProfile;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;
import com.stamp20.app.wheel.adapter.CityAdapter;
import com.stamp20.app.wheel.widget.OnWheelChangedListener;
import com.stamp20.app.wheel.widget.WheelView;

public class ShippingAddressFragment extends Fragment implements OnClickListener, OnWheelChangedListener,
        View.OnFocusChangeListener {

    private EditText addressLine1EditText;
    TextWatcher addressLine1EditTextTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                setHintText(addressLine1Hint, "");
            } else {
                setHintText(addressLine1Hint, R.string.address_line_1);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
    };
    private TextView addressLine1Hint;
    private EditText addressLine2EditText;
    TextWatcher addressLine2EditTextTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                setHintText(addressLine2Hint, "");
            } else {
                setHintText(addressLine2Hint, R.string.address_line_2);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
    };
    private TextView addressLine2Hint;
    String chooseCities[];
    private EditText cityEditText;
    TextWatcher cityEditTextTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                setHintText(cityHint, "");
            } else {
                setHintText(cityHint, R.string.address_city);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
    };
    private TextView cityHint;
    private EditText firstNameEditText;
    TextWatcher firstNameEditTextTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                setHintText(firstNameHint, "");
            } else {
                setHintText(firstNameHint, R.string.first_name);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
    };
    private TextView firstNameHint;
    private PopupWindow mChooseCityPopupWindow;

    private TextView mDoneTextView;
    private EditText secondNameEditText;

    private TextView secondNameHint;
    TextWatcher secondtNameEditTextTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                setHintText(secondNameHint, "");
            } else {
                setHintText(secondNameHint, R.string.second_name);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
    };
    private int selectedCityIndex = 0;
    private int sound;

    private SoundPool sp;

    private TextView stateEditText;

    TextWatcher stateEditTextTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                setHintText(stateHint, "");
            } else {
                setHintText(stateHint, R.string.address_state);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
    };

    private TextView stateHint;

    UserProfile userParseObject;

    private EditText zipEditText;

    TextWatcher zipEditTextTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                setHintText(zipHint, "");
            } else {
                setHintText(zipHint, R.string.address_zip);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }
    };

    private TextView zipHint;

    private void getData(String userProfile, TextView hint, int resId, TextView editText) {
        String value = getUserProfile(userProfile);
        if (value != null && !value.isEmpty()) {
            hint.setText(resId);
            editText.setText(value);
        } else {
            hint.setText("");
        }
    }

    private void getPopupWindowInstance() {
        if (null != mChooseCityPopupWindow) {
            mChooseCityPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    public String getUserProfile(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sp.getString(key, "");
    }

    @Override
    public View getView() {
        // TODO Auto-generated method stub
        return super.getView();
    }

    private void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
        View popupWindow = layoutInflater.inflate(R.layout.choose_city_popup_window, null);
        final WheelView cities = (WheelView) popupWindow.findViewById(R.id.city_picker_wheel_view);
        mDoneTextView = (TextView) popupWindow.findViewById(R.id.choose_city_done);
        mDoneTextView.setOnClickListener(this);
        FontManager.changeFontsBlod(getActivity().getApplicationContext(), (LinearLayout) popupWindow);
        cities.setViewAdapter(new CityAdapter(getActivity().getApplicationContext(), chooseCities));
        cities.setVisibleItems(5);
        cities.addChangingListener(this);
        int mWindowWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int mWindowHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();

        mChooseCityPopupWindow = new PopupWindow(popupWindow, mWindowWidth, mWindowHeight * 2 / 5);
    }

    private void loadUserProfile() {
        getData(com.stamp20.app.util.UserProfile.FIRST_NAME, firstNameHint, R.string.first_name, firstNameEditText);
        getData(com.stamp20.app.util.UserProfile.SECOND_NAME, secondNameHint, R.string.second_name, secondNameEditText);
        getData(com.stamp20.app.util.UserProfile.ADDRESS_LINE_1, addressLine1Hint, R.string.address_line_1,
                addressLine1EditText);
        getData(com.stamp20.app.util.UserProfile.ADDRESS_LINE_2, addressLine2Hint, R.string.address_line_2,
                addressLine2EditText);
        getData(com.stamp20.app.util.UserProfile.COUNTRY, cityHint, R.string.address_city, cityEditText);
        getData(com.stamp20.app.util.UserProfile.STATE, stateHint, R.string.address_state, stateEditText);
        getData(com.stamp20.app.util.UserProfile.ZIP, zipHint, R.string.address_zip, zipEditText);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

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
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.shipping_address_state_hint:
        case R.id.shipping_address_state:
            getPopupWindowInstance();
            mChooseCityPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            break;
        case R.id.choose_city_done:
            Log.d(this, "choose_city_done");
            if (mChooseCityPopupWindow != null) {
                mChooseCityPopupWindow.dismiss();
            }
            String city = chooseCities[selectedCityIndex];
            stateEditText.setText(city);
            // saveUserProfile(UserProfile.USER_STATE, city);
            break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.d(this, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.d(this, "onCreateView");
        final View parent = inflater.inflate(R.layout.layout_shipping_address, container, false);

        firstNameHint = (TextView) parent.findViewById(R.id.shipping_address_first_name_hint);
        firstNameEditText = (EditText) parent.findViewById(R.id.shipping_address_first_name);
        firstNameEditText.setOnFocusChangeListener(this);
        firstNameEditText.addTextChangedListener(firstNameEditTextTextWatcher);

        secondNameHint = (TextView) parent.findViewById(R.id.shipping_address_second_name_hint);
        secondNameEditText = (EditText) parent.findViewById(R.id.shipping_address_second_name);
        secondNameEditText.setOnFocusChangeListener(this);
        secondNameEditText.addTextChangedListener(secondtNameEditTextTextWatcher);

        addressLine1Hint = (TextView) parent.findViewById(R.id.shipping_address_line1_hint);
        addressLine1EditText = (EditText) parent.findViewById(R.id.shipping_address_line1);
        addressLine1EditText.setOnFocusChangeListener(this);
        addressLine1EditText.addTextChangedListener(addressLine1EditTextTextWatcher);

        addressLine2Hint = (TextView) parent.findViewById(R.id.shipping_address_line2_hint);
        addressLine2EditText = (EditText) parent.findViewById(R.id.shipping_address_line2);
        addressLine2EditText.setOnFocusChangeListener(this);
        addressLine2EditText.addTextChangedListener(addressLine2EditTextTextWatcher);

        cityHint = (TextView) parent.findViewById(R.id.shipping_address_city_hint);
        cityEditText = (EditText) parent.findViewById(R.id.shipping_address_city);
        cityEditText.setOnFocusChangeListener(this);
        cityEditText.addTextChangedListener(cityEditTextTextWatcher);

        stateHint = (TextView) parent.findViewById(R.id.shipping_address_state_hint);
        stateEditText = (TextView) parent.findViewById(R.id.shipping_address_state);
        stateEditText.setOnFocusChangeListener(this);
        stateEditText.setOnClickListener(this);
        stateEditText.addTextChangedListener(stateEditTextTextWatcher);

        zipHint = (TextView) parent.findViewById(R.id.shipping_address_zip_hint);
        zipEditText = (EditText) parent.findViewById(R.id.shipping_address_zip);
        zipEditText.setOnFocusChangeListener(this);
        zipEditText.addTextChangedListener(zipEditTextTextWatcher);

        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        sound = sp.load(getActivity().getApplicationContext(), R.raw.scroll, 1);
        chooseCities = getResources().getStringArray(R.array.city_names);

        loadUserProfile();
        return parent;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.shipping_address_first_name:
            setHintTextColor(firstNameHint, hasFocus);
            break;
        case R.id.shipping_address_second_name:
            setHintTextColor(secondNameHint, hasFocus);
            break;
        case R.id.shipping_address_line1:
            setHintTextColor(addressLine1Hint, hasFocus);
            break;
        case R.id.shipping_address_line2:
            setHintTextColor(addressLine2Hint, hasFocus);
            break;
        case R.id.shipping_address_city:
            setHintTextColor(cityHint, hasFocus);
            break;
        case R.id.shipping_address_state:
            setHintTextColor(stateHint, hasFocus);
            break;
        case R.id.shipping_address_zip:
            setHintTextColor(zipHint, hasFocus);
            break;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
    }

    public void saveUserProfile() {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(com.stamp20.app.util.UserProfile.FIRST_NAME, firstNameEditText.getText().toString());
        editor.putString(com.stamp20.app.util.UserProfile.SECOND_NAME, secondNameEditText.getText().toString());
        editor.putString(com.stamp20.app.util.UserProfile.ADDRESS_LINE_1, addressLine1EditText.getText().toString());
        editor.putString(com.stamp20.app.util.UserProfile.ADDRESS_LINE_2, addressLine2EditText.getText().toString());
        editor.putString(com.stamp20.app.util.UserProfile.COUNTRY, cityEditText.getText().toString());
        editor.putString(com.stamp20.app.util.UserProfile.STATE, stateEditText.getText().toString());
        editor.putString(com.stamp20.app.util.UserProfile.ZIP, zipEditText.getText().toString());
        editor.commit();
    }

    private void setHintText(TextView textView, int resId) {
        textView.setText(resId);
    }

    private void setHintText(TextView textView, String s) {
        textView.setText(s);
    }

    private void setHintTextColor(TextView hintTextView, boolean hasFocus) {
        hintTextView.setTextColor(hasFocus ? getResources().getColor(R.color.paypal_text_hint_color_blue_focus)
                : getResources().getColor(R.color.paypal_text_hint_color));
    }

    public void setVisible(boolean show) {
        if (getView() == null)
            return;
        getView().setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
