package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.makeramen.RoundedImageView;
import com.stamp20.app.view.ImageUtil;

public class ChoseEnvelopeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private Bitmap cardBackShape;
    private Bitmap mSourceBitmap;
    private static int sCardsBackList[] = { R.drawable.card_back_white,
            R.drawable.card_back_lite_grey, R.drawable.card_back_red,
            R.drawable.card_back_green };

    public class NamePairs {
        public String name1;
        public String name2;

        public NamePairs(String name11, String name22) {
            name1 = name11;
            name2 = name22;
        }
    }

    public ChoseEnvelopeAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        colorArray.add(new ColorArray(Color.WHITE));
        colorArray.add(new ColorArray(Color.parseColor("#E0EEEE")));
        colorArray.add(new ColorArray(Color.parseColor("#DC143C")));
        colorArray.add(new ColorArray(Color.parseColor("#008B00")));

        namepairArray.add(new NamePairs("Linen White", "Linen White"));
        namepairArray.add(new NamePairs("Linen Crean", "Linen Cream"));
        namepairArray
                .add(new NamePairs("Holiday Red", "Here comes Santa Claus"));
        namepairArray.add(new NamePairs("Holiday Green", "Holiday Green"));

        this.cardBackShape = BitmapFactory.decodeResource(
                mContext.getResources(), R.drawable.activity_card_back_shape);
    }

    public void setImageUri(Uri imageUri) {
        mSourceBitmap = ImageUtil.loadDownsampledBitmap(mContext, imageUri, 2);
        // here we use for add the blur image
        if (mSourceBitmap != null) {
            colorArray.add(0, new ColorArray(Color.WHITE));
            mSourceBitmap = getAlphaSrcBitmap();
        }
    }

    private List<ColorArray> colorArray = new ArrayList<ColorArray>();
    private List<NamePairs> namepairArray = new ArrayList<NamePairs>();
    private int selectItem;

    public void setSelectItem(int selectId) {
        this.selectItem = selectId;
        notifyDataSetChanged();
    }

    private class ColorArray {
        int color; // 颜色

        public ColorArray(int colorxx) {
            this.color = colorxx;
        }
    }

    @Override
    public int getCount() {
        return colorArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position < colorArray.size() ? colorArray.get(position).color
                : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getColor(int position) {
        return position < colorArray.size() ? colorArray.get(position).color
                : null;
    }

    public NamePairs getNamePairs(int position) {
        return position < namepairArray.size() ? namepairArray.get(position)
                : null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.gallery_choose_color_layout,
                null);
        // FontManager.changeFonts(mContext,
        // (LinearLayout)convertView.findViewById(R.id.root));
        RoundedImageView imageView = null;
        TextView textView = null;
        imageView = (RoundedImageView) convertView
                .findViewById(R.id.image_item);
        textView = (TextView) convertView.findViewById(R.id.text_item);
        if (mSourceBitmap != null) {
            if (position == 0) {
                imageView.setImageBitmap(mSourceBitmap);
            } else {
                imageView.setImageResource(sCardsBackList[position - 1]);
            }
        } else {
            imageView.setImageResource(sCardsBackList[position]);
        }
        if (selectItem == position) {
            // 处理点击放大效果，注意，这里还要加入边框效果，需要UI
            imageView.setBorderColor(Color.parseColor("#f1c40f"));
            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(imageView,
                    "scaleX", 1.1f);
            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(imageView,
                    "scaleY", 1.1f);
            xAnimator.setDuration(500);
            yAnimator.setDuration(500);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(xAnimator).with(yAnimator);
            animatorSet.start();
            // textView.setTextColor(Color.parseColor("#f1c40f"));
            // imageView.setBackgroundResource(R.drawable.bg_filter_item_selected);
        } else {
            // imageView.setLayoutParams(new LinearLayout.LayoutParams(width,
            // height));
            textView.setTextColor(Color.parseColor("#ffffff"));
            // imageView.setBackgroundResource(R.drawable.bg_filter_item_selected_no);
        }
        // imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // textView.setText(colorArray.get(position).color);
        return convertView;
    }

    public Bitmap getAlphaSrcBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mSourceBitmap.getWidth(),
                mSourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap cover = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.card_back_view_overlay);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAlpha(200);
        canvas.drawBitmap(mSourceBitmap, 0, 0, null);
        Matrix matrix = new Matrix();
        matrix.setScale(bitmap.getWidth() * 1.0f / cover.getWidth(),
                bitmap.getHeight() * 1.0f / cover.getHeight());
        canvas.concat(matrix);
        canvas.drawBitmap(cover, 0, 0, paint);

        return bitmap;
    }

    public Bitmap maskWithColor(Bitmap cardBackBitmapSource, int maskcolor) {
        int mBitmapHeight = cardBackBitmapSource.getHeight();
        int mBitmapWidth = cardBackBitmapSource.getWidth();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int mArrayColor[] = new int[mArrayColorLengh];
        int count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                // 获得Bitmap 图片中每一个点的color颜色值
                int color = cardBackBitmapSource.getPixel(j, i);
                // 将颜色值存在一个数组中 方便后面修改
                // 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理 笔者在这里就不做更多解释
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int alpha = Color.alpha(color);
                // 如果透明保持，不透明变为白色
                if (alpha < 120) {
                    mArrayColor[count] = color;
                } else {
                    mArrayColor[count] = maskcolor;

                }
                count++;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                Config.ARGB_4444);
        newBmp.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth,
                mBitmapHeight);
        return newBmp;
    }

    public Bitmap maskWithTransparent(Bitmap cardBackBitmapSource) {
        return cardBackBitmapSource;
    }
}
