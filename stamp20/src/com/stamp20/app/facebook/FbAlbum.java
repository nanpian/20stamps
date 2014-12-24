package com.stamp20.app.facebook;

import android.os.Bundle;

public class FbAlbum {
	private String id;
	private String title;
	private int count;
	private String coverId;
	private String coverThumbnailUrl;
	
	
	public FbAlbum(String id, String title, int count, String coverId,
			String coverThumbnailUrl) {
		this.id = id;
		this.title = title;
		this.count = count;
		this.coverId = coverId;
		this.coverThumbnailUrl = coverThumbnailUrl;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public String getCoverId() {
		return coverId;
	}


	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}


	public String getCoverThumbnailUrl() {
		return coverThumbnailUrl;
	}


	public void setCoverThumbnailUrl(String coverThumbnailUrl) {
		this.coverThumbnailUrl = coverThumbnailUrl;
	}

	public Bundle toBundle(){
		Bundle b = new Bundle();
		b.putString("id", id);
		b.putString("title", title);
		b.putInt("count", count);
		b.putString("coverId", coverId);
		b.putString("coverThumbnailUrl", coverThumbnailUrl);
		return b;
	}
	
	public static FbAlbum fromBundle(Bundle b){
		return new FbAlbum(b.getString("id"),b.getString("title"),
				b.getInt("count"),b.getString("coverId"),b.getString("coverThumbnailUrl"));
	}

}
