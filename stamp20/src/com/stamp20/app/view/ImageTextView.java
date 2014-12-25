package com.stamp20.app.view;

import com.stamp20.app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class ImageTextView extends LinearLayout  {
    private Context context;
    private LayoutInflater inflater;
    private ImageView mImageView;
    private TextView mTextView;
    private String mText;
    private View view;

    public ImageTextView(Context context) {
        super(context);
        initView(context,null);
    }

    //在父控件中添加android:clickable=“true” android:focusable=“true”，
    //而在子控件中添加android:duplicateParentState=“true”子控件就能获得父控件的点击事件
    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.widget_image_text, null);

        mImageView = (ImageView) view.findViewById(R.id.iv_button_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_button_text);
        
        this.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        this.setOrientation(HORIZONTAL);
        this.setPadding(5, 5, 5, 5);

        view.setClickable(true);
        view.setFocusable(true);
        view.setOnClickListener(ocl);
        view.setOnTouchListener(otl);
 
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ImageTextView);
        float textSize = 15;//a.getDimension(R.styleable.ImageViewText_textSize, 14);
        mText = a.getString(R.styleable.ImageTextView_text);
        float textMarginLeft = 20;//a.getDimension(R.styleable.ImageViewText_textMargin, 10);
        float image_size = 100;//a.getDimension(R.styleable.ImageViewText_image_size, 10);
        
        mTextView.setText(mText);
        mTextView.setTextSize(textSize);
        mTextView.setTextColor(getResources().getColorStateList(a.getResourceId(R.styleable.ImageTextView_textViewColorStateList, -1)));
 
        LayoutParams params  = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int) textMarginLeft;
        mTextView.setLayoutParams(params);
        mImageView.getLayoutParams().height=(int) image_size;
        mImageView.getLayoutParams().width=(int) image_size;
        mImageView.setImageResource(a.getResourceId(R.styleable.ImageTextView_imageViewSelectorDrawable, -1));
         
        a.recycle();// 
        view.setBackgroundColor(android.R.color.transparent);
        addView(view,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    public void setText(String text){
        mText = text;
        mTextView.setText(mText);
    }
    
    public View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            com.stamp20.app.util.Log.d(this, "click : " + mText);
            if(null != listener){
                listener.onClick(v, ImageTextView.this.getId(), mText);
            }
        }
    };
    

    /**
     * 设置颜色
     */
    public OnTouchListener otl = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
            }
            return false;
        }
    };
    
    public OnClickListenerImageTextView listener;

    public void setOnClickListenerImageViewText(OnClickListenerImageTextView listenerView) {
        this.listener = listenerView;
    }

    public interface OnClickListenerImageTextView {
        public void onClick(View v, int id, String text);
    }

}