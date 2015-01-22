package com.stamp20.app.activities;

import com.stamp20.app.R;
import com.stamp20.app.anim.ListView2GridViewLayoutAnimationController;
import com.stamp20.app.util.FontManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardsTemplateChooseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static float sSwitchViewAlphaHide = 0.0001f;
    private static float sSwitchViewAlphaShow = 1.0f;
    private boolean isListView = true;
    private GridView mGridView;
    private ListView mListView;
    private static int sInAnimDuration = 240;
    private static int sOutAnimDuration = 180;
    
    private TextView mCancel;
    private ImageView mListChange;
    private static int sTemplateList[] = {R.drawable.cards_new_year, R.drawable.cards_christmas, 
        R.drawable.cards_new_year}; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_template_choose);
        RelativeLayout root = (RelativeLayout) this.findViewById(R.id.root);
        FontManager.changeFonts(root, this);
        mListChange = com.stamp20.app.util.ViewHolder.findChildView(root, R.id.list_change);
        mListChange.setOnClickListener(this);
        mGridView = com.stamp20.app.util.ViewHolder.findChildView(root, R.id.gridview);
        mListView = com.stamp20.app.util.ViewHolder.findChildView(root, R.id.listview);
        if(isListView){
            mListView.setAlpha(sSwitchViewAlphaShow);
            mGridView.setAlpha(sSwitchViewAlphaHide);
        } else {
            mListView.setAlpha(sSwitchViewAlphaHide);
            mGridView.setAlpha(sSwitchViewAlphaShow);
        }
        mGridView.setAdapter(new TemplateAdapter(this));
        mGridView.setOnItemClickListener(this);
        mListView.setAdapter(new TemplateAdapter(this));
        mListView.setOnItemClickListener(this);
        
        mCancel = com.stamp20.app.util.ViewHolder.findChildView(root, R.id.cancel);
        mCancel.setOnClickListener(this);
        mCancel.setTextColor(getResources().getColorStateList(R.color.sel_cards_choose_button));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mListChange.getId()){
            isListView = !isListView;
            if(!isListView){
                mListView.setLayoutAnimation(getListViewAnimOut());
                mListView.setLayoutAnimationListener(new TemplateAnimationListener(mListView, mGridView));
                mListView.startLayoutAnimation();

                mListChange.setImageResource(R.drawable.sel_cards_templat_change_enlarge_button);
            }else{
                mGridView.setLayoutAnimation(getGridlayoutAnimOut());
                mGridView.setLayoutAnimationListener(new TemplateAnimationListener(mGridView, mListView));
                mGridView.startLayoutAnimation();
                
                mListChange.setImageResource(R.drawable.sel_cards_templat_change_shrink_button);
            }
        }else if(v.getId() == mCancel.getId()){
            CardsTemplateChooseActivity.this.setResult(RESULT_CANCELED);
            CardsTemplateChooseActivity.this.finish();
        }
    }
    
    private class TemplateAnimationListener implements AnimationListener{

        private ViewGroup mCurrentView;
        private ViewGroup mNextView;
        
        public TemplateAnimationListener(ViewGroup currentView, ViewGroup nextView){
            mCurrentView = currentView;
            mNextView = nextView;
        }
        
        @Override
        public void onAnimationEnd(Animation animation) {
            mCurrentView.setAlpha(sSwitchViewAlphaHide);
            mNextView.setAlpha(sSwitchViewAlphaShow);
            /*清除ListView或者GridView内部的子View的动画效果，要不然他们永远保持他们动画结束时的状态*/
            for(int i=0; i<mCurrentView.getChildCount(); i++){
                final View v = mCurrentView.getChildAt(i);
                v.clearAnimation();
                int l = v.getLeft();
                int t = v.getTop();
                int r = v.getRight();
                int b = v.getBottom();
                v.layout(l, t, r, b);
            }
            /*让下一个待显示的ListView或者GridView获取焦点*/
            mNextView.bringToFront();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    }
    
    private LayoutAnimationController getListViewAnimOut() {
        ListView2GridViewLayoutAnimationController controller;
        controller = new ListView2GridViewLayoutAnimationController(0.5f, mListView, mGridView, true);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    public LayoutAnimationController getGridlayoutAnimOut() {
        ListView2GridViewLayoutAnimationController controller;
        controller = new ListView2GridViewLayoutAnimationController(0.5f, mListView, mGridView, false);
        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
        return controller;
    }
    
    // 新建一个类继承BaseAdapter，实现视图与数据的绑定
    private class TemplateAdapter extends BaseAdapter {
        private Context mContext;
        public TemplateAdapter(Context context) {
            super();
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return sTemplateList.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.cards_choose_template_adapter_item, null);
            } 
            
            ImageView iv = com.stamp20.app.util.ViewHolder.get(convertView, R.id.image);
            iv.setImageResource(sTemplateList[position]);
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String strParent = "";
        if(parent == mListView){
            strParent = "mListView";
        }else if(parent == mGridView){
            strParent = "mGridView";
        }
        Log.i("xixia", strParent + " --- position : " + position);
        Intent data = new Intent();
        data.setClass(this, CardsActivity.class);
        data.putExtra(CardsActivity.ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID, sTemplateList[position]);
        //setResult(RESULT_OK, data); //这理有2个参数(int resultCode, Intent intent)
        startActivity(data);
    }
}
