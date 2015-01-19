package com.stamp20.app.view;

import java.util.ArrayList;

import com.stamp20.app.R;
import com.stamp20.app.util.Constant;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ChooseRatePopupWindow extends PopupWindow{
    
    /*private ArrayList<Integer> mRateDrawableHorizontal = new ArrayList<Integer>();
    private ArrayList<Integer> mRateDrawableVertical = new ArrayList<Integer>();*/
    private final String[] mRateTitles;
    
    /*private void initRateArrays(){
        TypedArray typedArray = mContext.getResources().obtainTypedArray(R.array.stamp_rate_horizontal);
        if( null != typedArray ){
            Constant.LogXixia("pop", "Horizontal typedArray:"+typedArray.length());
            for(int i=0; i<typedArray.length(); i++){
                mRateDrawableHorizontal.add(typedArray.getResourceId(i, 0));
            }
        }
        
        typedArray = mContext.getResources().obtainTypedArray(R.array.stamp_rate_vertical);
        if( null != typedArray ){
            Constant.LogXixia("pop", "Vertical typedArray:"+typedArray.length());
            for(int i=0; i<typedArray.length(); i++){
                mRateDrawableVertical.add(typedArray.getResourceId(i, 0));
            }
        }
    }*/
    
    private ListView mListView;
    private Button mCancel;
    private Context mContext;
    private View mAncor;
    //标识当前邮票是水平还是垂直的
    private boolean isHorizontal = false;
       
    public ChooseRatePopupWindow(Context context, View ancor, Boolean isH) {
        super(context);
        mContext = context;
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
        //初始化Rate选择队列名
        mRateTitles = mContext.getResources().getStringArray(R.array.stamp_rate_title);
        //initRateArrays();
        isHorizontal = isH;
        
        mListView = com.stamp20.app.util.ViewHolder.findChildView(contentView, R.id.listview);
        mListView.setAdapter(new ChooseRatePopupWindowListViewAdapter(context));
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                android.util.Log.i("xixia", "arg2:"+arg2);
                if(null != listener){
                    listener.onRateSelecedListener(arg2, isHorizontal);
                }
            }
        });
        mCancel = com.stamp20.app.util.ViewHolder.findChildView(contentView, R.id.cancel);
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePopupWindow();
            }
        });
    }
    
    public void show(){
        this.showAtLocation(mAncor, Gravity.BOTTOM, 0, 0);
    }
    
    public void hidePopupWindow(){
        this.dismiss();
    }
         
    private class ChooseRatePopupWindowListViewAdapter extends BaseAdapter{

        private Context mContext;
        public ChooseRatePopupWindowListViewAdapter(Context context) {
            super();
            this.mContext = context;
        }
        
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mRateTitles.length;
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
            Button btn = com.stamp20.app.util.ViewHolder.get(convertView, R.id.item);
            btn.setText(mRateTitles[position]);
            return convertView;
        }
    }
    
    OnRateSelecedListener listener = null;
    public void setOnRateSelecedListener(OnRateSelecedListener l){
        this.listener = l;
    }
    public interface OnRateSelecedListener{
        public void onRateSelecedListener(int id, boolean isH);
    }
}
