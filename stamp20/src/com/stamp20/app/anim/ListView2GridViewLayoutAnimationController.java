package com.stamp20.app.anim;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ListView;

import com.stamp20.app.R;

public class ListView2GridViewLayoutAnimationController extends LayoutAnimationController {

    public static int sAnimationDuration = 180;
    /**
     * 必须添加这个时长和ListView2GridViewLayoutAnimationController中的动画时长一样的动画
     * 要不然，LayoutAnimationController.ORDER_REVERSE情况下计算的每个子View的delay会出现错误
     * 
     * @return
     */
    private static Animation getEmptyAnimation() {
        Animation a = new AlphaAnimation(0.0f, 0.0f);
        a.setDuration(ListView2GridViewLayoutAnimationController.sAnimationDuration);
        return a;
    }
    private boolean isListView = false;
    private GridView mGridView;

    private ListView mListView;

    public ListView2GridViewLayoutAnimationController(float delay, ListView lv, GridView gv, boolean isLV) {
        super(getEmptyAnimation(), delay);
        mListView = lv;
        mGridView = gv;
        isListView = isLV;
    }

    Animation getAnimation(View from, View to, int index) {
        /*
         * 获取的分别是两个View（from，to）在他们父亲控件里面的相对位移，这两个父亲分别是ListView和GridView
         * 因为在activity_cards_template_choose布局中
         * ，ListView和GridView是属于同一个RelativeLayout的。
         * 
         * 因此这两个View（from，to）的四个参数fromXDelta、toXDelta、fromYDelta、toYDelta可认为属于同一个坐标系
         */
        float fromXDelta = from.getX();
        float toXDelta = to.getX();
        float fromYDelta = from.getY();
        float toYDelta = to.getY();

        float fromWidth = from.getWidth();
        float toWidth = to.getWidth();
        float formHeight = from.getHeight();
        float toHeight = to.getHeight();

        float scaleX = (toWidth * 1f) / (fromWidth * 1f);
        float scaleY = (toHeight * 1f) / (formHeight * 1f);

        float listViewHeight = mListView.getHeight();
        float listViewWidth = mListView.getWidth();
        float gridViewHeight = mGridView.getHeight();
        float gridViewWidth = mGridView.getWidth();

        Log.i("xixia", "index:" + index + ",fX:" + fromXDelta + ",fY:" + fromYDelta + ",tX:" + toXDelta + ",tY:"
                + toYDelta + ",fromWidth:" + fromWidth + ",toWidth:" + toWidth + ",formHeight:" + formHeight
                + ",toHeight:" + toHeight);
        Log.i("xixia", "---listViewHeight:" + listViewHeight + ",listViewWidth:" + listViewWidth + ",gridViewHeight:"
                + gridViewHeight + ",gridViewWidth:" + gridViewWidth);

        AnimationSet animationSet = new AnimationSet(true);
        /*
         * 将当前动画View（from）进行移动操作到终结View（to），移动的距离就是(toXDelta - fromXDelta,
         * toYDelta - fromYDelta) 这也是动画View（from）的左上角的移动距离
         */
        animationSet.addAnimation(getTranslateAnimation(toXDelta - fromXDelta, toYDelta - fromYDelta));
        /*
         * 不理解pivotX和pivotY参数，为什么要这么传递？？？？？？ 但是目前的效果是对了
         */
        animationSet.addAnimation(getScaleAnimation(1.0f, scaleX, 1.0f, scaleY, toXDelta - fromXDelta, toYDelta
                - fromYDelta));

        /* 结束以后将当前View定住 */
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /*
     * child animation delay = child index * delay
     */
    @Override
    protected long getDelayForView(View view) {
        // TODO Auto-generated method stub
        long l = super.getDelayForView(view);
        if (isListView) {
            Log.i("xixia", "ListView getDelayForView : " + l);
        } else {
            Log.i("xixia", "GridView getDelayForView : " + l);
        }
        return l;
    }

    Animation getGrid2List(int index) {
        /* 获取的是ListView和GridView中的Item中包含的那个ImageView */
        Log.i("xixia", "mListView.getFirstVisiblePosition() is : " + mListView.getFirstVisiblePosition());
        Log.i("xixia", "mListView.getLastVisiblePosition() is : " + mListView.getLastVisiblePosition());
        if (index >= mListView.getChildCount()) {
            index = mListView.getChildCount() - 1;
        }
        View to = mListView.getChildAt(index).findViewById(R.id.image);
        View from = mGridView.getChildAt(index).findViewById(R.id.image);
        return getAnimation(from, to, index);
    }

    Animation getList2Grid(int index) {
        /* 获取的是ListView和GridView中的Item中包含的那个ImageView */
        View from = mListView.getChildAt(index).findViewById(R.id.image);
        View to = mGridView.getChildAt(index).findViewById(R.id.image);
        return getAnimation(from, to, index);
    }

    Animation getScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotX, float pivotY) {
        Animation animation = new ScaleAnimation(fromX, toX, fromY, toY, pivotX, pivotY);
        animation.setDuration(sAnimationDuration);
        return animation;
    }

    @Override
    protected int getTransformedIndex(AnimationParameters params) {
        Log.i("xixia", "getTransformedIndex, count:" + params.count + ", index:" + params.index);
        if (isListView) {
            setAnimation(getList2Grid(params.index));
        } else {
            setAnimation(getGrid2List(params.index));
        }
        return super.getTransformedIndex(params);
    }

    Animation getTranslateAnimation(float toXDelta, float toYDelta) {
        /*
         * 绝对位移
         * 
         * 参数1、参数2 起点的X相对于动画View的左上角的偏移量 起点的Y相对于动画View的左上角的偏移量 0.0f 0.0f
         * 说明起点没有和动画View有偏差
         * 
         * 参数3、参数4 终点的X相对于动画View的左上角的偏移量 终点的Y相对于动画View的左上角的偏移量
         */
        Animation animation = new TranslateAnimation(
        /* Animation.RELATIVE_TO_PARENT, */0.0f,
        /* Animation.RELATIVE_TO_PARENT, */toXDelta,
        /* Animation.RELATIVE_TO_PARENT, */0.0f,
        /* Animation.RELATIVE_TO_PARENT, */toYDelta);
        animation.setFillAfter(true);
        animation.setDuration(sAnimationDuration);
        return animation;
    }
}
