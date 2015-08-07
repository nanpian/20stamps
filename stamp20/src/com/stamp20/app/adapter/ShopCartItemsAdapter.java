package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.squareup.picasso.Picasso;
import com.stamp20.app.R;
import com.stamp20.app.data.Cart;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class ShopCartItemsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<Design> mDesigns;

    public ShopCartItemsAdapter(Context context, List<Design> designs) {
        // TODO Auto-generated constructor stub
        mContext = context;
        this.mDesigns = designs;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDesigns.size() < 0 ? 0 : mDesigns.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        final int pos = position;
        if (view == null) {
            Log.d(this, "view == null");
            view = layoutInflater.inflate(R.layout.shop_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.addView = (ImageView) view.findViewById(R.id.shop_listview_num_add);
            viewHolder.reduceView = (ImageView) view.findViewById(R.id.shop_listview_num_reduce);
            viewHolder.textItemSize = (TextView) view.findViewById(R.id.shop_listview_num);
            viewHolder.stampItemView = (ImageView) view.findViewById(R.id.shop_list_itemview);
            viewHolder.itemDeleteView = (ImageView) view.findViewById(R.id.shop_listview_num_delete);
            viewHolder.itemName = (TextView) view.findViewById(R.id.shop_listview_itemname);
            viewHolder.itemPrice = (TextView) view.findViewById(R.id.shop_listview_itemprice);
            viewHolder.itemPersheet = (TextView) view.findViewById(R.id.shop_listview_persheet);
            viewHolder.itemUnitPrice = (TextView) view.findViewById(R.id.shop_listview_unitprice);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        FontManager.changeFonts(mContext, viewHolder.textItemSize);
        FontManager.changeFonts(mContext, viewHolder.itemName);
        FontManager.changeFonts(mContext, viewHolder.itemPrice);
        FontManager.changeFonts(mContext, viewHolder.itemPersheet);
        FontManager.changeFonts(mContext, viewHolder.itemUnitPrice);

        byte[] review = mDesigns.get(position).getReview();
        Bitmap src = BitmapFactory.decodeByteArray(review, 0, review.length);
        if (src != null)
            viewHolder.stampItemView.setImageBitmap(src);
        else {
            viewHolder.stampItemView.setImageResource(R.drawable.activity_card_review_click);
        }

        viewHolder.addView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int stampSize = mDesigns.get(pos).getCount();
                stampSize += 1;
                mDesigns.get(pos).setCount(stampSize);
                Cart.getInstance().updateDesignCount(mDesigns.get(pos), stampSize);
                // 更新方式？？
                notifyDataSetChanged();
            }
        });
        viewHolder.reduceView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int stampSize = mDesigns.get(pos).getCount();
                stampSize -= 1;
                if (stampSize <= 1) {
                    stampSize = 1;
                }
                mDesigns.get(pos).setCount(stampSize);
                Cart.getInstance().updateDesignCount(mDesigns.get(pos), stampSize);
                // 更新方式？？
                notifyDataSetChanged();
            }
        });
        viewHolder.textItemSize.setText(Integer.toString(mDesigns.get(pos).getCount()));
        viewHolder.itemDeleteView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Builder dialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog.setTitle("Delete design");
                dialog.setMessage("Are you sure to delete this design?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        deleteItems(pos);
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
        return view;
    }

    private void deleteItems(int posotion) {
        Cart.getInstance().deleteDesign(mDesigns.get(posotion));
        mDesigns.remove(posotion);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        private ImageView stampItemView;
        private ImageView addView;
        private ImageView reduceView;
        private TextView textItemSize;
        private ImageView itemDeleteView;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemPersheet;
        private TextView itemUnitPrice;
    }

}
