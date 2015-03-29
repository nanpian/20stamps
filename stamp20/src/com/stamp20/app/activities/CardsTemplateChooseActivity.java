package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import com.stamp20.app.R;
import com.stamp20.app.anim.ListView2GridViewLayoutAnimationController;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.ImageUtil;

public class CardsTemplateChooseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static float sSwitchViewAlphaHide = 0.0001f;
    private static float sSwitchViewAlphaShow = 1.0f;
    private boolean isListView = true;
    private GridView mGridView;
    private ListView mListView;
    private static int sInAnimDuration = 240;
    private static int sOutAnimDuration = 180;
    private ImageView mCancel;
    private ImageView mListChange;
    private static boolean isFromMain = true;
	private static int sTemplateList2[] = { R.drawable.x_christmas2_front,
			R.drawable.x_christmas3_front, R.drawable.x_christmas4_front,
			R.drawable.x_christmas5_front, R.drawable.x_congrats1_front,
			R.drawable.x_greeting1_front, R.drawable.x_holiday1_front,
			R.drawable.x_holiday2_front, R.drawable.x_holiday3_front,
			R.drawable.x_holiday4_front, R.drawable.x_invitation1_front,
			R.drawable.x_invitation2_front, R.drawable.x_love1_front,
			R.drawable.x_love3_front, R.drawable.x_love4_front,
			R.drawable.x_new_year2_front, R.drawable.x_new_year4_front,
			R.drawable.x_new_year5_front, R.drawable.x_new_year6_front,
			R.drawable.x_new_year7_front, R.drawable.x_save_the_date1_front,
			R.drawable.x_thanks1_front, R.drawable.x_thanks2_front,
			R.drawable.x_thanks3_front,
    	//R.drawable.x_use_your_photo1_front,
    	//R.drawable.x_use_your_photo2_front, R.drawable.x_use_your_photo3_front,
       // R.drawable.cards_year_sheep,R.drawable.cards_love
        };
	private static int sTemplateList[] = { R.drawable.xx_christmas2_front,
		R.drawable.xx_christmas3_front, R.drawable.xx_christmas4_front,
		R.drawable.xx_christmas5_front, R.drawable.xx_congrats1_front,
		R.drawable.xx_greeting1_front, R.drawable.xx_holiday1_front,
		R.drawable.xx_holiday2_front, R.drawable.xx_holiday3_front,
		R.drawable.xx_holiday4_front, R.drawable.xx_invitation1_front,
		R.drawable.xx_invitation2_front, R.drawable.xx_love1_front,
		R.drawable.xx_love3_front, R.drawable.xx_love4_front,
		R.drawable.xx_new_year2_front, R.drawable.xx_new_year4_front,
		R.drawable.xx_new_year5_front, R.drawable.xx_new_year6_front,
		R.drawable.xx_new_year7_front, R.drawable.xx_save_the_date1_front,
		R.drawable.xx_thanks1_front, R.drawable.xx_thanks2_front,
		R.drawable.xx_thanks3_front,};
    //add for template change
    private Uri mSrcImageUri = null;
    private Bitmap mSrcImage = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_template_choose);
        RelativeLayout root = (RelativeLayout) this.findViewById(R.id.root);
        FontManager.changeFonts(root, this);
        
        //add for template change 
        Intent intent = getIntent();
        if(intent != null) {
        	isFromMain = false;
			mSrcImageUri = (Uri)intent.getParcelableExtra(CardEffect.SRC_IMAGE_URI);
			if (mSrcImageUri != null) {
				mSrcImage = ImageUtil.loadDownsampledBitmap(this, mSrcImageUri,
						2);
			}
			
		} else {
			isFromMain = true;
		}
        
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
        //mCancel.setTextColor(getResources().getColorStateList(R.color.sel_cards_choose_button));
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
            if (mSrcImage != null) {
				iv.setBackground(new BitmapDrawable(mSrcImage));
			}
            iv.setImageResource(sTemplateList[position]);
            return convertView;
        }
    }

    /**
     * 
     * 举个例子你会理解的更快：X, Y两个listview，X里有1,2,3,4这4个item，Y里有a,b,c,d这4个item。
     * 如果你点了b这个item。如下：
     * 
     * public void onItemClick (AdapterView<?> parent, View view, int position, long id )
     *      parent 相当于listview Y适配器的一个指针，可以通过它来获得Y里装着的一切东西，再通俗点就是说告诉你，你点的是Y，不是X - -
     *      view 是你点b item的view的句柄，就是你可以用这个view，来获得b里的控件的id后操作控件
     *      position 是b在Y适配器里的位置（生成listview时，适配器一个一个的做item，然后把他们按顺序排好队，在放到listview里，意思就是这个b是第position号做好的）
     *      id 是b在listview Y里的第几行的位置（很明显是第2行），大部分时候position和id的值是一样的，如果需要的话，你可以自己加个log把position和id都弄出来在logcat里瞅瞅，看了之后心里才踏实。
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String strParent = "";
        if(parent == mListView){
            strParent = "mListView";
        }else if(parent == mGridView){
            strParent = "mGridView";
        }
        Intent data = new Intent();

        data.setClass(this, CardEffect.class);
        if (isFromMain) {
            data.putExtra(CardsActivity.ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID, sTemplateList[position]);
        } else {
        	 data.putExtra(CardsActivity.ACTIVITY_RESULT_FOR_CHANGE_TEMPLATE_EXTRA_TEMPLATE_ID, sTemplateList2[position]);
        }

        if (mSrcImageUri != null) {
        	this.setResult(RESULT_OK, data);
            this.finish();
		}else {
			startActivity(data);
		}

        
    }
}
