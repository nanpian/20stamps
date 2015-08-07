package com.stamp20.app.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.stamp20.app.adapter.ShopCartItemsAdapter;
import com.stamp20.app.data.Cart;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;

/**
 * 
 * 商品列表页
 * 
 * @author zhudewei
 *
 */
public class ShopCartItemsActivity extends Activity implements OnClickListener {

    private static final String TAG = "ShopCartItemsActivity";
    private TextView textHeaderTile;
    private ListView listCartItems;
    private LayoutInflater layoutInflater;
    private LinearLayout layoutListFooter;
    private LinearLayout layoutAddMore;
    public static final String ADD_ITEMS_TOCAET = "add_new_items";
    private ShopCartItemsAdapter shopItemsAdapter;
    private List<Design> mDesigns;
    private BitmapCache mCache = null;
    private ImageView backHomeView;
    private Button btnPaypal;
    private Button btnCheckout;
    private ImageView headerPrevious;
    private TextView headerTitle;

    public ShopCartItemsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_cartitems_activity);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        // mCache = BitmapCache.getCache();
        // initListView();
        mDesigns = Cart.getInstance().getDesigns();
        initView();
    }

    /*
     * private void initListView() { boolean addNewItems =
     * getIntent().getBooleanExtra(ADD_ITEMS_TOCAET, false); if (addNewItems) {
     * ShopStampItem stapItem = new ShopStampItem(mCache.get(), 0.0f, 0.0f);
     * data.add(stapItem); }
     * 
     * }
     */
    public void initView() {
        layoutInflater = getLayoutInflater();
        // textHeaderTile = (TextView) findViewById(R.id.home_header_title);
        // textHeaderTile.setVisibility(View.VISIBLE);
        // backHomeView = (ImageView) findViewById(R.id.home_header_back);
        // backHomeView.setOnClickListener(this);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setImageResource(R.drawable.main_bottom_tab_home_focus);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.shop_cartitems_title);

        btnPaypal = (Button) findViewById(R.id.shop_cartitems_papal);
        btnPaypal.setOnClickListener(this);
        btnCheckout = (Button) findViewById(R.id.shop_cartitems_checkout);
        btnCheckout.setOnClickListener(this);
        // textHeaderTile.setText(R.string.shop_cartitems_title);
        listCartItems = (ListView) findViewById(R.id.listview_cartitems);
        layoutListFooter = (LinearLayout) layoutInflater.inflate(
                R.layout.shop_cartitems_listview_footer, null);
        FontManager.changeFonts(layoutListFooter, this);
        layoutAddMore = (LinearLayout) layoutListFooter
                .findViewById(R.id.shop_add_more);
        layoutAddMore.setOnClickListener(this);
        // listCartItems.addHeaderView(layoutTest);
        listCartItems.addFooterView(layoutListFooter);
        shopItemsAdapter = new ShopCartItemsAdapter(ShopCartItemsActivity.this,
                mDesigns);
        listCartItems.setAdapter(shopItemsAdapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.header_previous:
        case R.id.shop_add_more:
            intent.setClass(ShopCartItemsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下的如果是BACK，同时没有重复
            startActivity(new Intent(ShopCartItemsActivity.this,
                    HomeActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
