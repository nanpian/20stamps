package com.stamp20.app.facebook;

import android.os.Bundle;

public class FbPhoto {
	private String id;
	private String sourceImageUrl;
	private String thumbnailUrl;
	private int width;
	private int height;
	
	public FbPhoto(String id, String sourceImageUrl, String thumbnailUrl,
			int width, int height) {
		this.id = id;
		this.thumbnailUrl = thumbnailUrl;
		this.sourceImageUrl = sourceImageUrl;
		this.width = width;
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public String getSourceImageUrl() {
		return sourceImageUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}


	public void setId(String id) {
		this.id = id;
	}

	public void setSourceImageUrl(String sourceImageUrl) {
		this.sourceImageUrl = sourceImageUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}


	public Bundle toBundle(){
		Bundle b = new Bundle();
		b.putString("id", id);
		b.putString("sourceImageUrl", sourceImageUrl);
		b.putString("thumbnailUrl", thumbnailUrl);
		b.putInt("width", width);
		b.putInt("height", height);
		return b;
	}
	
	public static FbPhoto fromBundle(Bundle b){
		return new FbPhoto(b.getString("id"),b.getString("sourceImageUrl"),b.getString("thumbnailUrl"),
				b.getInt("width"),b.getInt("height"));
	}
}
