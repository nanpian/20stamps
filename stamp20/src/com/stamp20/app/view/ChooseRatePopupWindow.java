package com.stamp20.app.view;

import com.stamp20.app.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ChooseRatePopupWindow extends PopupWindow{

    private int mListViewImages[] = {R.drawable.icon_letter_selected, 
            R.drawable.icon_letter_selected_white,
            R.drawable.icon_letter_selected, 
            R.drawable.icon_letter_selected_white};
    
    private String mListViewTexts[] = {"Post Card",
            "1st Class Letter 1oz",
            "1st Class Letter 2oz or 1oz odd",
            "1st Class Letter 3oz or 2oz odd"};
    private ListView mListView;
    
    private View mAncor;
       
    public ChooseRatePopupWindow(Context context, View ancor) {
        super(context);
        mAncor = ancor;
        View contentView = LayoutInflater.from(context)  
                .inflate(R.layout.choose_rate_popup_menu, null);  
        this.setContentView(contentView);
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //必须这样设置，才能实现该PopupWindow是透明背景
        setBackgroundDrawable(new BitmapDrawable());
        //设置可以获得焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true); 
        //设置弹窗外可点击
        setOutsideTouchable(true);
        
        this.setAnimationStyle(R.style.popupAnimation);
        
        mListView = com.stamp20.app.util.ViewHolder.findChildView(contentView, R.id.listview);
        mListView.setAdapter(new ChooseRatePopupWindowListViewAdapter(context));
    }
    
    public void show(){
        this.showAtLocation(mAncor, Gravity.BOTTOM, 0, 0);
    }
    
    private ImageTextView.OnClickListenerImageTextView onClickListenerImageTextView = new ImageTextView.OnClickListenerImageTextView() {
        @Override
        public void onClick(View arg0, int id, String text) {
            Log.i("xixia", "text:"+text);
        }
    };
    
    private class ChooseRatePopupWindowListViewAdapter extends BaseAdapter{

        private Context mContext;
        public ChooseRatePopupWindowListViewAdapter(Context context) {
            super();
            this.mContext = context;
        }
        
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mListViewImages.length;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.choose_rate_popupwindow_adapter_item, null);
            } 
            ImageTextView itv = com.stamp20.app.util.ViewHolder.get(convertView, R.id.item);
            itv.setText(mListViewTexts[position]);
            itv.setOnClickListenerImageViewText(onClickListenerImageTextView);
            return convertView;
        }
    }
}
