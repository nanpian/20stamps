package com.stamp20.app.activities;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFacebookUtils.Permissions;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.stamp20.app.R;
import com.stamp20.app.Setting;

public class LoginView extends FragmentActivity {/*
                                                  * 
                                                  * 
                                                  * private final static int
                                                  * REQUEST_CODE_BASE = 20;
                                                  * private boolean mVisible;
                                                  * private boolean
                                                  * _isRegisterMode; private
                                                  * TextView status; private
                                                  * View edit_password_repeat;
                                                  * 
                                                  * 
                                                  * 
                                                  * @Override public void
                                                  * onCreate(Bundle
                                                  * savedInstanceState) {
                                                  * super.onCreate
                                                  * (savedInstanceState);
                                                  * if(getResources
                                                  * ().getBoolean(
                                                  * R.bool.portrait_only)){
                                                  * setRequestedOrientation
                                                  * (ActivityInfo
                                                  * .SCREEN_ORIENTATION_PORTRAIT
                                                  * ); }
                                                  * 
                                                  * setContentView(R.layout.
                                                  * loginview);
                                                  * 
                                                  * mSignInFragment =
                                                  * PlusClientFragment
                                                  * .getPlusClientFragment
                                                  * (activity, new String[]{});
                                                  * status =
                                                  * (TextView)findViewById
                                                  * (R.id.login_status);
                                                  * login_title =
                                                  * (TextView)findViewById
                                                  * (R.id.login_title_login);
                                                  * register_title =
                                                  * (TextView)findViewById
                                                  * (R.id.login_title_register);
                                                  * login_title_bottom_view =
                                                  * findViewById
                                                  * (R.id.login_title_login_bottom
                                                  * );
                                                  * register_title_bottom_view =
                                                  * findViewById(R.id.
                                                  * login_title_register_bottom
                                                  * );
                                                  * login_title.setOnClickListener
                                                  * (new View.OnClickListener()
                                                  * {
                                                  * 
                                                  * @Override public void
                                                  * onClick(View v) {
                                                  * changeMode(false); } });
                                                  * register_title
                                                  * .setOnClickListener(new
                                                  * View.OnClickListener() {
                                                  * 
                                                  * @Override public void
                                                  * onClick(View v) {
                                                  * changeMode(true); } });
                                                  * 
                                                  * edit_email = (EditText)
                                                  * findViewById
                                                  * (R.id.login_edit_email);
                                                  * edit_password = (EditText)
                                                  * findViewById
                                                  * (R.id.login_edit_password);
                                                  * edit_password_repeat =
                                                  * (EditText)
                                                  * findViewById(R.id.
                                                  * login_edit_password_repeat);
                                                  * button_login = (Button)
                                                  * findViewById
                                                  * (R.id.login_button_login);
                                                  * button_login
                                                  * .setOnClickListener(new
                                                  * View.OnClickListener() {
                                                  * 
                                                  * @Override public void
                                                  * onClick(View v) { if
                                                  * (_isRegisterMode){
                                                  * 
                                                  * doRegisterClick(); }else{
                                                  * 
                                                  * doLoginClick(); } } });
                                                  * 
                                                  * changeMode(true);//default
                                                  * to register view
                                                  * 
                                                  * }
                                                  * 
                                                  * @Override public void
                                                  * onStart(){ super.onStart();
                                                  * }
                                                  * 
                                                  * @Override public void
                                                  * onResume(){
                                                  * super.onResume(); mVisible =
                                                  * true;
                                                  * if(Setting.isUserLogin(
                                                  * activity) &&
                                                  * !_isLoggingOut){
                                                  * Toast.makeText
                                                  * (getApplicationContext(),
                                                  * "You have logged in.",
                                                  * Toast.LENGTH_SHORT).show();
                                                  * activity
                                                  * .setResult(RESULT_OK);
                                                  * activity.finish(); } }
                                                  * 
                                                  * @Override public void
                                                  * onPause(){ mVisible = false;
                                                  * super.onPause(); }
                                                  * 
                                                  * private void
                                                  * changeMode(boolean
                                                  * registerMode){ if
                                                  * (_isRegisterMode ==
                                                  * registerMode) return;
                                                  * _isRegisterMode =
                                                  * registerMode;
                                                  * status.setText(""); if
                                                  * (_isRegisterMode){
                                                  * edit_password_repeat
                                                  * .setVisibility
                                                  * (View.VISIBLE);
                                                  * edit_password_repeat
                                                  * .setText("");
                                                  * edit_password.setText("");
                                                  * button_login
                                                  * .setText("Register");
                                                  * login_title_bottom_view
                                                  * .setBackgroundColor
                                                  * (getResources
                                                  * ().getColor(R.color
                                                  * .login_header_background));
                                                  * register_title_bottom_view
                                                  * .setBackgroundColor
                                                  * (getResources
                                                  * ().getColor(R.color
                                                  * .sidebar_pressed_row_background
                                                  * )); }else{
                                                  * edit_password_repeat
                                                  * .setVisibility(View.GONE);
                                                  * edit_password_repeat
                                                  * .setText("");
                                                  * edit_password.setText("");
                                                  * button_login
                                                  * .setText("Login");
                                                  * register_title_bottom_view
                                                  * .setBackgroundColor
                                                  * (getResources
                                                  * ().getColor(R.color
                                                  * .login_header_background));
                                                  * login_title_bottom_view
                                                  * .setBackgroundColor
                                                  * (getResources
                                                  * ().getColor(R.color
                                                  * .sidebar_pressed_row_background
                                                  * )); }
                                                  * 
                                                  * } protected void
                                                  * doLoginClick() {
                                                  * status.setText(""); String
                                                  * email =
                                                  * edit_email.getText().
                                                  * toString().trim(); String pw
                                                  * =
                                                  * edit_password.getText().toString
                                                  * ().trim(); if
                                                  * (email.isEmpty() ||
                                                  * pw.isEmpty()){
                                                  * Toast.makeText
                                                  * (getApplicationContext(),
                                                  * "Form is empty...",
                                                  * Toast.LENGTH_SHORT).show();
                                                  * return; }
                                                  * showLoadingDialog(true);
                                                  * ParseUser
                                                  * .logInInBackground(email,
                                                  * pw, new LogInCallback(){
                                                  * 
                                                  * @Override public void
                                                  * done(ParseUser user,
                                                  * ParseException e) {
                                                  * showLoadingDialog(false); if
                                                  * (e==null && user!=null){
                                                  * //logged in
                                                  * Toast.makeText(activity,
                                                  * "Welcome "+user.getEmail(),
                                                  * Toast.LENGTH_LONG).show();
                                                  * loginSuccess(false); }else
                                                  * if (e!=null){
                                                  * status.setText(
                                                  * "Error logging in. "
                                                  * +e.getMessage());
                                                  * status.setVisibility
                                                  * (View.VISIBLE); }else{
                                                  * status.setText(
                                                  * "Error logging in. Please check your username and password."
                                                  * );
                                                  * status.setVisibility(View.
                                                  * VISIBLE); } } }); }
                                                  * 
                                                  * private void
                                                  * doRegisterClick() {
                                                  * status.setText(""); final
                                                  * String email =
                                                  * edit_email.getText
                                                  * ().toString().trim(); String
                                                  * pw1 =
                                                  * edit_password.getText()
                                                  * .toString().trim(); String
                                                  * pw2 =
                                                  * edit_password_repeat.getText
                                                  * ().toString().trim(); if
                                                  * (email.isEmpty() ||
                                                  * pw1.isEmpty() ||
                                                  * pw2.isEmpty()){
                                                  * Toast.makeText
                                                  * (getApplicationContext(),
                                                  * "Form is empty...",
                                                  * Toast.LENGTH_SHORT).show();
                                                  * return; } if
                                                  * (pw1.equals(pw2)) {
                                                  * showLoadingDialog(true);
                                                  * ParseUser currentUser =
                                                  * ParseUser.getCurrentUser();
                                                  * currentUser.setEmail(email);
                                                  * currentUser
                                                  * .setUsername(email);
                                                  * currentUser
                                                  * .setPassword(pw1);
                                                  * currentUser
                                                  * .signUpInBackground(new
                                                  * SignUpCallback(){
                                                  * 
                                                  * @Override public void
                                                  * done(ParseException e) {
                                                  * showLoadingDialog(false); if
                                                  * (e==null){ //signup
                                                  * successful
                                                  * Toast.makeText(activity,
                                                  * "Welcome "+email,
                                                  * Toast.LENGTH_LONG).show();
                                                  * if (!MApplication.ENV_DEV){
                                                  * MATShared
                                                  * .getMAT(activity).trackAction
                                                  * (
                                                  * MATShared.MAT_EVENT_REGISTRATION
                                                  * ); } loginSuccess(true);
                                                  * }else{
                                                  * showLoadingDialog(false);
                                                  * status
                                                  * .setText(e.getLocalizedMessage
                                                  * ());
                                                  * status.setVisibility(View
                                                  * .VISIBLE);
                                                  * 
                                                  * } } }); }else{
                                                  * status.setText
                                                  * ("passwords are not same");
                                                  * status
                                                  * .setVisibility(View.VISIBLE
                                                  * ); } }
                                                  * 
                                                  * 
                                                  * @Override public void
                                                  * onActivityResult(int
                                                  * requestCode, int resultCode,
                                                  * Intent data) { if
                                                  * (requestCode ==
                                                  * REQUEST_CODE_FB){
                                                  * ParseFacebookUtils
                                                  * .finishAuthentication
                                                  * (requestCode, resultCode,
                                                  * data); }else if (requestCode
                                                  * ==
                                                  * REQUEST_CODE_TWITTER_COMPLETE
                                                  * || requestCode ==
                                                  * REQUEST_CODE_FACEBOOK_COMPLETE
                                                  * ){ final ParseUser
                                                  * currentUser =
                                                  * ParseUser.getCurrentUser();
                                                  * final String
                                                  * currentUserCurrentEmail =
                                                  * currentUser.getEmail(); if
                                                  * (resultCode ==
                                                  * Activity.RESULT_OK){ //user
                                                  * and email saved in
                                                  * RegisterEmailActivity
                                                  * Toast.makeText(activity,
                                                  * "Welcome "
                                                  * +currentUserCurrentEmail,
                                                  * Toast.LENGTH_LONG).show();
                                                  * if (!MApplication.ENV_DEV){
                                                  * MATShared
                                                  * .getMAT(activity).trackAction
                                                  * (
                                                  * MATShared.MAT_EVENT_REGISTRATION
                                                  * ); } loginSuccess(true);
                                                  * }else{ //user aborted
                                                  * registration
                                                  * Toast.makeText(activity,
                                                  * "Registration cancelled...",
                                                  * Toast.LENGTH_LONG).show();
                                                  * runOnUiThread(new
                                                  * Runnable(){public void
                                                  * run(){ _isLoggingOut = true;
                                                  * currentUser.remove("email");
                                                  * //unlink facebook user
                                                  * ParseFacebookUtils
                                                  * .unlinkInBackground
                                                  * (currentUser, new
                                                  * SaveCallback(){
                                                  * 
                                                  * @Override public void
                                                  * done(ParseException err) {
                                                  * if (err!=null){
                                                  * Crashlytics.log(Log.ERROR,
                                                  * "case",
                                                  * "LoginView unlinkFacebook error"
                                                  * );
                                                  * Crashlytics.logException(err
                                                  * ); } ParseUser.logOut();
                                                  * _isLoggingOut = false;
                                                  * ParseUser
                                                  * .enableAutomaticUser();
                                                  * ParseUser
                                                  * .getCurrentUser().increment
                                                  * ("RunCount");
                                                  * ParseUser.getCurrentUser
                                                  * ().saveInBackground(); } });
                                                  * }}); } }else if (requestCode
                                                  * ==
                                                  * REQUEST_CODE_PLUS_CLIENT_FRAGMENT
                                                  * ){ mSignInFragment.
                                                  * handleOnActivityResult
                                                  * (requestCode, resultCode,
                                                  * data); }else{
                                                  * super.onActivityResult
                                                  * (requestCode, resultCode,
                                                  * data); } }
                                                  * 
                                                  * public void
                                                  * loginSuccess(boolean
                                                  * isNewUser) { if(!isNewUser)
                                                  * startService(new
                                                  * Intent(this,
                                                  * AccountSyncService.class));
                                                  * activity
                                                  * .setResult(RESULT_OK,
                                                  * getIntent());
                                                  * activity.finish(); }
                                                  * 
                                                  * public void
                                                  * showLoadingDialog(boolean
                                                  * isShown) { if (!mVisible){
                                                  * Crashlytics.log(Log.WARN,
                                                  * "case",
                                                  * "LoginView showLoadingDialog-"
                                                  * +isShown+
                                                  * " ignored, because activity is not visible."
                                                  * ); return; } if (isShown){
                                                  * FragmentTransaction ft =
                                                  * getSupportFragmentManager
                                                  * ().beginTransaction();
                                                  * Fragment prev =
                                                  * getSupportFragmentManager
                                                  * ().findFragmentByTag
                                                  * ("loadingdialog"); if (prev
                                                  * != null) { ft.remove(prev);
                                                  * } LoadingDialogFragment
                                                  * newLoading =
                                                  * LoadingDialogFragment
                                                  * .newInstance
                                                  * ("",LoadingDialogFragment
                                                  * .MODE_INFINITE);
                                                  * newLoading.setCancelable
                                                  * (false); newLoading.show(ft,
                                                  * "loadingdialog"); }else{
                                                  * FragmentTransaction ft =
                                                  * getSupportFragmentManager
                                                  * ().beginTransaction();
                                                  * Fragment prev =
                                                  * getSupportFragmentManager
                                                  * ().findFragmentByTag
                                                  * ("loadingdialog"); if (prev
                                                  * != null) { ft.remove(prev);
                                                  * } ft.commit(); } }
                                                  */

}
