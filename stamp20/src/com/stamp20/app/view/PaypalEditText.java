package com.stamp20.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;

public class PaypalEditText extends LinearLayout {

    private LayoutParams editParams;
    private EditText editText;

    private int editTextColor;
    private int editTextSize;
    private String editTextText;

    private TextView hint;
    private LayoutParams hintParams;
    private String hintText;

    private int hintTextColor;
    private int hintTextSize;

    public PaypalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PaypalEditText);

        hintTextColor = ta.getColor(R.styleable.PaypalEditText_hintTextColor, 0);
        hintTextSize = ta.getDimensionPixelSize(R.styleable.PaypalEditText_hintTextSize, 0);
        hintText = ta.getString(R.styleable.PaypalEditText_hintText);

        editTextColor = ta.getColor(R.styleable.PaypalEditText_editTextColor, 0);
        editTextSize = ta.getDimensionPixelSize(R.styleable.PaypalEditText_editTextSize, 0);
        editTextText = ta.getString(R.styleable.PaypalEditText_editText);

        ta.recycle();

        hint = new TextView(context);
        editText = new EditText(context);

        hint.setTextColor(hintTextColor);
        hint.setTextSize(hintTextSize);
        hint.setText(hintText);

        editText.setTextColor(editTextColor);
        editText.setTextSize(editTextSize);
        editText.setText(editTextText);

        setBackgroundColor(0xffffffff);

        hintParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        editParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(hint, hintParams);
        addView(editText, editParams);
    }

}
