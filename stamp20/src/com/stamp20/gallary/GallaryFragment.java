package com.stamp20.gallary;

import com.stamp20.app.util.Log;

import android.support.v4.app.Fragment;

public class GallaryFragment extends Fragment {
	private boolean isFront = false;
	private final String name = getClass().getName();
	public void onFront(){
		this.isFront = true;
		Log.i("wangpeng",name + ": onFront");
	}
	public void onBackground(){
		this.isFront = false;
		Log.i("wangpeng", name + ": onBackground");
	}
	public boolean isFront(){
		return this.isFront;
	}
	
	public boolean onBackClick(){
		return false;
	}
}
