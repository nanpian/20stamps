package com.stamp20.app;

import android.graphics.Bitmap;

public class ShopStampItem {

	private Bitmap bmpItemView;
	private float singleItemPrice;
	private float totalItemPrice;
	private float unitPrice;
	private int itemSize;
	
	public ShopStampItem(Bitmap bmpItemView, float singleItemPrice, float unitPrice) {
		// TODO Auto-generated constructor stub
		this.bmpItemView = bmpItemView;
		this.singleItemPrice = singleItemPrice;
		this.totalItemPrice = 0.0f;
		this.unitPrice = unitPrice;
		this.itemSize = 1;
	}

	public float getTotalPrice(){
		float totalPrice = singleItemPrice * itemSize;
		
		return totalPrice;
	}
	public Bitmap getBmpItemView() {
		return bmpItemView;
	}

	public void setBmpItemView(Bitmap bmpItemView) {
		this.bmpItemView = bmpItemView;
	}

	public float getSingleItemPrice() {
		return singleItemPrice;
	}

	public void setSingleItemPrice(float singleItemPrice) {
		this.singleItemPrice = singleItemPrice;
	}

	public float getTotalItemPrice() {
		return totalItemPrice;
	}

	public void setTotalItemPrice(float totalItemPrice) {
		this.totalItemPrice = totalItemPrice;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getItemSize() {
		return itemSize;
	}

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}
	
	

}
