package com.stamp20.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.ShopStampItem;
import com.stamp20.app.adapter.ShopCartItemsAdapter;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;

public class ShopCartItemsActivity extends Activity implements OnClickListener {

    private static final String TAG = "ShopCartItemsActivity";
    private TextView textHeaderTile;
    private ListView listCartItems;
    private LayoutInflater layoutInflater;
    private LinearLayout layoutListFooter;
    private LinearLayout layoutAddMore;
    public static final String ADD_ITEMS_TOCAET = "add_new_items";
    private ShopCartItemsAdapter shopItemsAdapter;
    private static ArrayList<ShopStampItem> data = new ArrayList<ShopStampItem>();
    private BitmapCache mCache = null;
    private ImageView backHomeView;
    private Button btnPaypal;
    private Button btnCheckout;

    public ShopCartItemsActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_cartitems_activity);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        mCache = BitmapCache.getCache();
        boolean addNewItems = getIntent().getBooleanExtra(ADD_ITEMS_TOCAET, false);
        if (addNewItems) {
            ShopStampItem stapItem = new ShopStampItem(mCache.get(), 0.0f, 0.0f);
            data.add(stapItem);
        }
        initView();
    }

    public void initView() {
        layoutInflater = getLayoutInflater();
        textHeaderTile = (TextView) findViewById(R.id.home_header_title);
        textHeaderTile.setVisibility(View.VISIBLE);
        backHomeView = (ImageView) findViewById(R.id.home_header_back);
        backHomeView.setOnClickListener(this);
        btnPaypal = (Button) findViewById(R.id.shop_cartitems_papal);
        btnPaypal.setOnClickListener(this);
        btnCheckout = (Button) findViewById(R.id.shop_cartitems_checkout);
        btnCheckout.setOnClickListener(this);
        textHeaderTile.setText(R.string.shop_cartitems_title);
        listCartItems = (ListView) findViewById(R.id.listview_cartitems);
        layoutListFooter = (LinearLayout) layoutInflater.inflate(R.layout.shop_cartitems_listview_footer, null);
        FontManager.changeFonts(layoutListFooter, this);
        layoutAddMore = (LinearLayout) layoutListFooter.findViewById(R.id.shop_add_more);
        layoutAddMore.setOnClickListener(this);
        // listCartItems.addHeaderView(layoutTest);
        listCartItems.addFooterView(layoutListFooter);
        shopItemsAdapter = new ShopCartItemsAdapter(ShopCartItemsActivity.this, data);
        listCartItems.setAdapter(shopItemsAdapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.home_header_back:
            intent.setClass(ShopCartItemsActivity.this, HomeActivity.class);
            startActivity(intent);
            break;
        case R.id.shop_cartitems_papal:
            intent.putExtra(Constant.PAY_STYLE, 0);
            intent.setClass(this, BuyWithPaypalShippingActivity.class);
            startActivity(intent);
            break;
        case R.id.shop_cartitems_checkout:
            intent.putExtra(Constant.PAY_STYLE, 1);
            intent.setClass(this, BuyWithPaypalShippingActivity.class);
            startActivity(intent);
            break;
        }
        // if(v.getId() == backHomeView.getId()){
        // Intent intent = new Intent();
        // intent.setClass(ShopCartItemsActivity.this, HomeActivity.class);
        // startActivity(intent);
        // }
    }

}
