package com.stamp20.app.data;

import java.util.HashMap;
import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.stamp20.app.util.BitmapUtils;
import com.stamp20.app.util.Log;

import android.graphics.Bitmap;

public class Cart {
	static private Cart mInstance = new Cart();
	static public Cart getInstance(){
		return mInstance;
	}
	private boolean mIsEmpty;
	private UserProfile mUser;
	
	private Cart(){
		mIsEmpty = true;
	}
	
	public boolean isEmpty(){
		int c = this.getCount();
		if(c > 0){
			mIsEmpty = false;
		}else{
			mIsEmpty = true;
		}		
		return mIsEmpty;
	}
	
	public int getCount(){
		int c = 0;
		ParseQuery<Design> q = ParseQuery.getQuery(Design.class);
		q.fromLocalDatastore();
		try{
			c = q.count();
		}catch(ParseException e){
			e.printStackTrace();
		}
		return c;
	}
	
	public int getCardsCount(){
		int c = 0;
		ParseQuery<Design> q = ParseQuery.getQuery(Design.class);
		q.fromLocalDatastore();
		q.whereEqualTo("Type", Design.TYPE_CARD);
		try{
			c = q.count();
		}catch(ParseException e){
			e.printStackTrace();
		}
		return c;
	}
	
	public void addDesign(Design d){
		try{
			d.pin();
		}catch(ParseException e){
			Log.e("wangpeng","add to cart error" );	
			e.printStackTrace();
		}
	}
	
	public void addDesign(Bitmap src, int rate, String type){
		Design design = Design.getInstance();
		
		byte[] data = BitmapUtils.Bitmap2Bytes(src);
		design.setReview(data);
		design.setRate(rate);
        design.setUnitPrice(2*rate+3);
        design.setType(type);
        
        this.addDesign(design);
	}
	
	public void deleteDesign(Design d){
		try{
			d.unpin();
		}catch(ParseException e){
			Log.e("wangpeng", "delete design error");
			e.printStackTrace();
		}
	}
	
	public void deleteDesign(String id){
		ParseQuery<Design> q = ParseQuery.getQuery(Design.class);
		q.fromLocalDatastore();
		q.whereEqualTo("objectId", id);
		q.findInBackground(new FindCallback<Design>(){

			@Override
			public void done(List<Design> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if(arg1 != null){
					Log.e("wangpeng", "deleteDesign - find error");
					return;
				}
				deleteDesign(arg0.get(0));
			}});
	}
	
	public void updateDesignCount(String id, final int count){
		ParseQuery<Design> q = ParseQuery.getQuery(Design.class);
		q.fromLocalDatastore();
		q.whereEqualTo("objectId", id);
		q.findInBackground(new FindCallback<Design>(){

			@Override
			public void done(List<Design> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if(arg1 != null){
					Log.e("wangpeng", "updateDesign - find error");
					return;
				}
				updateDesignCount(arg0.get(0), count);
			}});
	}
	
	public void updateDesignCount(Design design, int count){	
		design.setCount(count);
		design.pinInBackground(new SaveCallback(){
			@Override
			public void done(ParseException e) {
				if(e != null)
					Log.e("wangpeng","update design error");						
			}});
		
	}
	
	public void setUser(UserProfile user){
		this.mUser = user;
	}
	
	public UserProfile getUser(){
		return this.mUser;
	}
	
	public List<Design> getDesigns(){
		List<Design> ds = null;
		ParseQuery<Design> q = ParseQuery.getQuery(Design.class);
		q.fromLocalDatastore();
		try{
			ds = q.find();
		}catch(ParseException e){
			ds = null;
			e.printStackTrace();
		}
		if(ds != null)
			Log.i("wangpeng", ds.toString());
		return ds;
	}
		
	public void upStream(SaveCallback s){
		
	}
	
}
