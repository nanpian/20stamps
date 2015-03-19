package com.stamp20.gallary.facebook;

import java.util.ArrayList;

public class FbPhotoResult extends ArrayList<FbPhoto>{

	private static final long serialVersionUID = 7015865819074499128L;
	
	private String nextPhotosUrl;
	
	public FbPhotoResult(int cap){
		super(cap);
	}
	
	public String getNextPhotosUrl() {
		return nextPhotosUrl;
	}

	public void setNextPhotosUrl(String nextPhotosUrl) {
		this.nextPhotosUrl = nextPhotosUrl;
	}

	public void appendMoreFbPhotos(FbPhotoResult more){
		this.addAll(more);
		setNextPhotosUrl(more.getNextPhotosUrl());
	}
}
