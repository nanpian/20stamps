package com.stamp20.gallary.features;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("FeaturePhoto")
public class FeaturePhoto extends ParseObject {
	public static String TAG = "FeaturePhoto";
	public static String Name = "FeaturePhoto";
	
	public ParseFile getPhoto(){
		return null;
	}
}
