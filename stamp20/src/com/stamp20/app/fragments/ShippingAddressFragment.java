package com.stamp20.app.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;
import com.stamp20.app.util.UserProfile;
import com.stamp20.app.wheel.adapter.CityAdapter;
import com.stamp20.app.wheel.widget.OnWheelChangedListener;
import com.stamp20.app.wheel.widget.WheelView;

public class ShippingAddressFragment extends Fragment implements OnClickListener, OnWheelChangedListener {

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
    
    private SoundPool sp;
    private int sound;

    private PopupWindow mChooseCityPopupWindow;
    private TextView mDoneTextView;
    private int selectedCityIndex = 0;
    String chooseCities[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View getView() {
        // TODO Auto-generated method stub
        return super.getView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        final View parent = inflater.inflate(R.layout.layout_shipping_address, container, false);

        firstNameHint = (TextView) parent.findViewById(R.id.shipping_address_first_name_hint);
        firstNameEditText = (EditText) parent.findViewById(R.id.shipping_address_first_name);
        secondNameHint = (TextView) parent.findViewById(R.id.shipping_address_second_name_hint);
        secondNameEditText = (EditText) parent.findViewById(R.id.shipping_address_second_name);
        addressLine1Hint = (TextView) parent.findViewById(R.id.shipping_address_line1_hint);
        addressLine1EditText = (EditText) parent.findViewById(R.id.shipping_address_line1);
        addressLine2Hint = (TextView) parent.findViewById(R.id.shipping_address_line2_hint);
        addressLine2EditText = (EditText) parent.findViewById(R.id.shipping_address_line2);
        cityHint = (TextView) parent.findViewById(R.id.shipping_address_city_hint);
        cityEditText = (EditText) parent.findViewById(R.id.shipping_address_city);
        stateHint = (TextView) parent.findViewById(R.id.shipping_address_state_hint);
        stateEditText = (TextView) parent.findViewById(R.id.shipping_address_state);
        stateEditText.setOnClickListener(this);
        zipHint = (TextView) parent.findViewById(R.id.shipping_address_zip_hint);
        zipEditText = (EditText) parent.findViewById(R.id.shipping_address_zip);
        
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

        sound = sp.load(getActivity().getApplicationContext(), R.raw.scroll, 1);
        chooseCities = getResources().getStringArray(R.array.city_names);

//        loadUserProfile();

        return parent;
    }

    private void loadUserProfile() {
        getData(UserProfile.USER_FIRST_NAME, firstNameHint, R.string.first_name, firstNameEditText);
        getData(UserProfile.USER_SECOND_NAME, secondNameHint, R.string.second_name, secondNameEditText);
        getData(UserProfile.USER_ADDRESS_LINE_1, addressLine1Hint, R.string.address_line_1, addressLine1EditText);
        getData(UserProfile.USER_ADDRESS_LINE_2, addressLine2Hint, R.string.address_line_2, addressLine2EditText);
        getData(UserProfile.USER_CITY, cityHint, R.string.address_city, cityEditText);
        getData(UserProfile.USER_STATE, stateHint, R.string.address_state, stateEditText);
        getData(UserProfile.USER_ZIP, zipHint, R.string.address_zip, zipEditText);
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

    private String getUserProfile(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return sp.getString(key, "");
    }

    public void setVisible(boolean show) {
        if (getView() == null)
            return;
        getView().setVisibility(show ? View.VISIBLE : View.GONE);
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
//            saveUserProfile(UserProfile.USER_STATE, city);
            break;
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

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        // 播放声音
        Log.d(this, "onChanged, oldValue: " + oldValue + ", newValue: " + newValue);
        selectedCityIndex = newValue;
        sp.play(sound, 1, 1, 0, 0, 1);
        
    }
}

