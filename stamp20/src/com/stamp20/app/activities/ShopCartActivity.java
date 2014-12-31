package com.stamp20.app.activities; 

import com.stamp20.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-29 下午4:49:47 
 * 类说明 
 */

public class ShopCartActivity  extends Activity implements OnClickListener{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopcart);
		loadShopData();
	}

	/**
	*  日期 2014-12-31
	*  作者 lenovo
	*  说明 TODO
	*  返回 void
	 */
	private void loadShopData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	public void onClick(View arg0) {

		
	}

}
 