package com.stamp20.gallary;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.stamp20.app.R;
import com.stamp20.app.util.Log;
import com.stamp20.gallary.features.*;

public class FeaturedAlbumFragment extends GallaryFragment implements OnItemClickListener, GallaryLoader{
	private Context mContext;
    private ParseQuery<FeaturePhoto> mQuery;
    private GridView mPhotosView;
    private List<Photo> mPhotos;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mQuery = ParseQuery.getQuery(FeaturePhoto.class);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View parent = inflater.inflate(R.layout.tab_photo_album, container, false);
        parent.findViewById(R.id.main_list).setVisibility(View.GONE);
        mPhotosView = (GridView)parent.findViewById(R.id.main_child_grid);
        mPhotosView.setVisibility(View.VISIBLE);
        mPhotosView.setOnItemClickListener(this);
        
        return parent;
    }
    
    @Override
    public void onFront(){
    	fresh();
    }

    private void fresh(){		
		final Dialog pd = GallaryProgressDialog.show(mContext, true, GallaryActivity.OUT_OF_TIME, false, 
				new GallaryProgressDialog.OnCancelListener() {							
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub				
					}
			
					@Override
					public void onTimeCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Log.e("wangpeng", "get feature photos out time, need add response");								
					}
			});

    	mQuery.findInBackground(new FindCallback<FeaturePhoto>(){

			@Override
			public void done(List<FeaturePhoto> photoList, ParseException e) {
				pd.dismiss();
								
				if(e == null){ 
					List<Photo> ps = new ArrayList<Photo>(20);
					for(FeaturePhoto p:photoList){
						String url = p.getParseFile("photo").getUrl();
						Log.i("wangpeng", "Feature photo objectId: " + p.getObjectId() + ", url: " + url);
						ps.add(new Photo(p.getObjectId(), url, Photo.PHOTO_NET_TYPE));						
					}
					mPhotosView.setAdapter(new PhotoAdapter(mContext, mPhotos = ps));
				}else{
					Log.e("wangpeng", "Error: " + e.getMessage());
				}				
			}    		
    	});
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String url = mPhotos.get(position).getUri();          	
    	Log.d("wangpeng", "feature photo position: " + position + " url: " + url);
    	GallaryUtil.goToEffectAfterDownLoad(mContext, url);		
	}
	
	@Override
	public List<Album> getAlbums() {
		return null;
	}

	@Override
	public List<Photo> getPhotos(Album a) {
		return mPhotos;
	}
    
}
