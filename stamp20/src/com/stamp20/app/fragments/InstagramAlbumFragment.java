package com.stamp20.app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.stamp20.app.activities.*;
import com.stamp20.app.Setting;
import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class InstagramAlbumFragment extends Fragment implements OnClickListener{
    private View mInstagramView;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(this, "onCreateView");
        
        mInstagramView = inflater.inflate(R.layout.tab_instagram_album, container, false);
        ((Button)mInstagramView.findViewById(R.id.instagram_login_button)).setOnClickListener(this);
        ((Button)mInstagramView.findViewById(R.id.instagram_logout_button)).setOnClickListener(this);
        
        return mInstagramView;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(this, "onCreate");
        super.onCreate(savedInstanceState);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.instagram_login_button :
			break;
		case R.id.instagram_logout_button:
			 doSelectInstagramClick();
			break;
		default:
			break;
		}
	}
	
	private void  doSelectInstagramClick() {
	    if (getActivity()==null){
	      return; //so it won't throw NullPointerException after user leaves app
	    }
		if (Setting.isUserInstagramLinked(getActivity())){
			//Intent instagramIntent = new Intent(getActivity(),InstagramPhotosView.class);
			//startActivityForResult(instagramIntent,REQUEST_CODE_SELECT_INSTAGRAM);
		}else{
			if(Setting.isUserLogin(getActivity())){
			  InstagramAuthRequestDialog dialogFrag = new InstagramAuthRequestDialog();
		    	dialogFrag.show(getFragmentManager(), "instagram_auth_request_dialog");
			}else{
			  AccountRequestDialog dialogFrag = new AccountRequestDialog();
		    	dialogFrag.show(getFragmentManager(), "instagram_register_account_dialog");
			}
		}
	
	}
	
	//Dialog for request user to setup an account 
	public class AccountRequestDialog extends DialogFragment{
		public AccountRequestDialog(){
			//empty constructor required
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Account Required");
			builder.setMessage("Please register a Pic2Press account before we can connect to your Instagram.");
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}});
			builder.setPositiveButton("Register", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Intent loginIntent = new Intent(getActivity(), LoginView.class);
					//startActivityForResult(loginIntent,REQUEST_CODE_SELECT_INSTAGRAM_REGISTER);
					dialog.dismiss();
				}});
			return builder.create();
		}
	}
	
	//Dialog for auth with instagram
	public class InstagramAuthRequestDialog extends DialogFragment {
		public InstagramAuthRequestDialog(){
			//empty constructor required
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Connect Instagram");
			builder.setMessage("Connect your Instagram account to choose photos from your Instagram.");
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}});
			builder.setPositiveButton("Connect Now", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//doInstagramAuth();
					dialog.dismiss();
				}});
			return builder.create();
		}
	}

	  private void doInstagramAuto()  {
	      //Intent i = new Intent(getActivity(),InstagramAuthView.class);
	      //startActivityForResult(i,REQUEST_CODE_SELECT_INSTAGRAM_AUTH);
	  }

}
