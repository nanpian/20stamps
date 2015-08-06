package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.filter.ImageBlurRender;
import com.stamp20.app.filter.PixelBuffer;
import com.stamp20.app.makeramen.RoundedImageView;
import com.stamp20.app.util.BitmapUtils;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.ImageUtil;

public class ImageEffectAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public Effect mEffect;
    private Uri imageUri;
    private Bitmap imageBitmap;
    private EffectContext mEffectContext;
    private ImageBlurRender mImageBlurRender;
    private PixelBuffer mGlPBuffer;
    private Map<String, Bitmap> mBlutImageMap = new HashMap<String, Bitmap>();

    /**
     * @param c
     *            保存Context
     * @author zhudewei
     * @todo 定义多种滤镜，采用android mediaeffect实现
     */
    public ImageEffectAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        mImageBlurRender = new ImageBlurRender(c);
    }

    public ImageEffectAdapter(Context c, EffectContext meffectcontect) {
        // TODO Auto-generated constructor stub
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        mEffectContext = meffectcontect;
        mImageBlurRender = new ImageBlurRender(c);
        filterArray.add(new FilterInfo(0, "normal", null));
        filterArray.add(new FilterInfo(1, "autofix", null));
        filterArray.add(new FilterInfo(2, "bw", null));
        filterArray.add(new FilterInfo(3, "brightness", null));
        filterArray.add(new FilterInfo(4, "contrast", null));
        filterArray.add(new FilterInfo(5, "crossprocess", null));
        filterArray.add(new FilterInfo(6, "documentary", null));
        filterArray.add(new FilterInfo(7, "duotone", null));
        filterArray.add(new FilterInfo(8, "filllight", null));
        filterArray.add(new FilterInfo(9, "fisheye", null));
        filterArray.add(new FilterInfo(10, "flipvert", null));
        filterArray.add(new FilterInfo(11, "fliphor", null));
        filterArray.add(new FilterInfo(12, "grain", null));
        filterArray.add(new FilterInfo(13, "grayscale", null));
        filterArray.add(new FilterInfo(14, "lomoish", null));
        filterArray.add(new FilterInfo(15, "negative", null));
        filterArray.add(new FilterInfo(16, "posterize", null));
        filterArray.add(new FilterInfo(17, "rotate", null));
        filterArray.add(new FilterInfo(18, "saturate", null));
        filterArray.add(new FilterInfo(19, "sepia", null));
        filterArray.add(new FilterInfo(20, "sharpen", null));
        filterArray.add(new FilterInfo(21, "temperature", null));
        filterArray.add(new FilterInfo(22, "tint", null));
        filterArray.add(new FilterInfo(23, "vignette", null));

    }

    public void setImageResource(Uri imageUri) {
        this.imageUri = imageUri;
        // imageBitmap = ImageUtil.loadDownsampledBitmap(mContext, imageUri, 2);
        // for the preview blur imageview, just using 200*200 size to reduce the
        // memorry
        imageBitmap = BitmapUtils.decodeUri(imageUri,
                mContext.getContentResolver(), 200);
        mImageBlurRender.setBlurBitmapSrc(imageBitmap);
    }

    public void clearPreviewHashMap() {
        if (mBlutImageMap != null) {
            mBlutImageMap.clear();
        }
    }

    public Effect createEffect(int currentfilterID,
            EffectContext mEffectContext2) {
        // TODO Auto-generated method stub
        mEffectContext = mEffectContext2;
        return mEffect = createEffect(currentfilterID);
    }

    public Effect createEffect(int mCurrentEffect) {

        EffectFactory effectFactory = null;
        if (mEffectContext == null) {
            try {
                mEffectContext = EffectContext.createWithCurrentGlContext();
                if (mEffect != null) {
                    mEffect.release();
                }
            } catch (Exception e) {
                Log.e("EffectException", "create effect exception");
                e.printStackTrace();
            }
        }
        effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        // Initialize the correct effect based on the selected menu/action item
        if (effectFactory == null) {
            return null;
        }
        switch (mCurrentEffect) {

        case 0:
            break;

        case 1:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
            mEffect.setParameter("scale", 0.5f);
            break;

        case 2:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_BLACKWHITE);
            mEffect.setParameter("black", .1f);
            mEffect.setParameter("white", .7f);
            break;

        case 3:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_BRIGHTNESS);
            mEffect.setParameter("brightness", 2.0f);
            break;

        case 4:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
            mEffect.setParameter("contrast", 1.4f);
            break;

        case 5:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_CROSSPROCESS);
            break;

        case 6:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_DOCUMENTARY);
            break;

        case 7:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DUOTONE);
            mEffect.setParameter("first_color", Color.YELLOW);
            mEffect.setParameter("second_color", Color.DKGRAY);
            break;

        case 8:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_FILLLIGHT);
            mEffect.setParameter("strength", .8f);
            break;

        case 9:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
            mEffect.setParameter("scale", .5f);
            break;

        case 10:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
            mEffect.setParameter("vertical", true);
            break;

        case 11:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
            mEffect.setParameter("horizontal", true);
            break;

        case 12:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
            mEffect.setParameter("strength", 1.0f);
            break;

        case 13:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_GRAYSCALE);
            break;

        case 14:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
            break;

        case 15:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
            break;

        case 16:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_POSTERIZE);
            break;

        case 17:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
            mEffect.setParameter("angle", 180);
            break;

        case 18:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
            mEffect.setParameter("scale", .5f);
            break;

        case 19:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
            break;

        case 20:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
            break;

        case 21:
            mEffect = effectFactory
                    .createEffect(EffectFactory.EFFECT_TEMPERATURE);
            mEffect.setParameter("scale", .9f);
            break;

        case 22:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TINT);
            mEffect.setParameter("tint", Color.MAGENTA);
            break;

        case 23:
            mEffect = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
            mEffect.setParameter("scale", .5f);
            break;

        default:
            break;
        }
        return mEffect;
    }

    private class FilterInfo {
        public int filterID;
        public String filterName;
        public Effect mEffect;
        public int selectItem;

        public FilterInfo(int filterid, String filtername, Effect meffect) {
            this.filterID = filterid;
            this.filterName = filtername;
            this.mEffect = meffect;
        }

    }

    private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();
    private int selectItem = -1;

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return filterArray.size();
    }

    public String getFilterName(int position) {
        // TODO Auto-generated method stub
        return position < filterArray.size() ? filterArray.get(position).filterName
                : null;
    }

    public int getFilterID(int position) {
        return position < filterArray.size() ? filterArray.get(position).filterID
                : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position < filterArray.size() ? filterArray.get(position).mEffect
                : null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gallery_item_layout, null);
            viewHolder.mImageView = (RoundedImageView) convertView
                    .findViewById(R.id.image_item);
            viewHolder.mTextView = (TextView) convertView
                    .findViewById(R.id.text_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FontManager.changeFonts(mContext,
                (LinearLayout) convertView.findViewById(R.id.root));
        Bitmap bitmap = mBlutImageMap.get(filterArray.get(position).filterName);
        String effectName = filterArray.get(position).filterName;
        if (bitmap == null) {
            if (position == 0) {
                viewHolder.mImageView.setImageBitmap(imageBitmap);
            } else {
                viewHolder.mImageView.setBackground(mContext.getResources()
                        .getDrawable(R.drawable.blur_image_null));
                viewHolder.mImageView.setTag(effectName);
                //注意，生成预览图，这里会有egl_bad_match的错误，原因不明，耗费了很多时间在处理这个bug上
                try {
                    new BlurAsyncTask(position, effectName,
                            viewHolder.mImageView).execute(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.mImageView.setImageBitmap(imageBitmap);
                }
            }
        } else {
            viewHolder.mImageView.setImageBitmap(bitmap);
        }

        viewHolder.mTextView.setText(filterArray.get(position).filterName);

        if (this.selectItem == position) {
            Log.i("jiangtao4", "in positon");
            // imageView.setBorderWidth(R.dimen.gallery_imageview_border_width);
            viewHolder.mImageView.setBorderColor(Color.parseColor("#f1c40f"));
            viewHolder.mTextView.setTextColor(Color.parseColor("#f1c40f"));
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(
                    viewHolder.mImageView, "scaleX", 1.1f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(
                    viewHolder.mImageView, "scaleY", 1.1f);
            scaleDownX.setDuration(500);
            scaleDownY.setDuration(500);
            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.play(scaleDownX).with(scaleDownY);
            scaleDown.start();
        } else {
            viewHolder.mTextView.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mImageView
                    .setBackgroundResource(R.drawable.bg_filter_item_selected_no);
        }
        return convertView;
    }

    public class BlurAsyncTask extends AsyncTask<Bitmap, Void, Bitmap> {

        private int mPosition;
        private String mEffctName;
        private RoundedImageView mRoundedImageView;

        public BlurAsyncTask(int pos, String name, RoundedImageView imageView) {
            this.mPosition = pos;
            this.mEffctName = name;
            this.mRoundedImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... param) {
            Bitmap bitmap = param[0];
            mGlPBuffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
            mGlPBuffer.setRenderer(mImageBlurRender);
            mImageBlurRender.mCurrentEffect = mPosition;
            try {
                bitmap = mGlPBuffer.getBitmap(null,null);
            } catch (Exception e) {
                return bitmap;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (((String) mRoundedImageView.getTag()).equals(mEffctName)) {
                mRoundedImageView.setImageBitmap(result);
            }
            mBlutImageMap.put(mEffctName, result);
        }
    }

    public static class ViewHolder {
        public RoundedImageView mImageView;
        public TextView mTextView;
    }

    /**
     * 日期 2014-12-12 作者 zhudewei 说明 得到点击的图片的id 返回 void
     * 
     * @param position
     */
    public void setSelectItem(int selectItemid) {
        Log.i("jiangtao4", "in setSelectItem");
        if (this.selectItem != selectItemid) {
            this.selectItem = selectItemid;
            notifyDataSetChanged();
        }
    }

}
