//package com.stamp20.app.util;
//
//import android.app.Activity;
//import android.content.Context;
//import android.inputmethodservice.Keyboard;
//import android.inputmethodservice.KeyboardView;
//import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
//import android.text.Editable;
//import android.view.View;
//import android.widget.EditText;
//
//import com.stamp20.app.R;
//
//public class KeyboardUtil {
//    private KeyboardView keyboardView;
//    private Keyboard k;// 数字键盘
//    private EditText ed;
//
//    public KeyboardUtil(Activity act, Context ctx, EditText edit) {
//        this.ed = edit;
//        k = new Keyboard(ctx, R.xml.symbols);
//        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
//        keyboardView.setKeyboard(k);
//        keyboardView.setEnabled(true);
//        keyboardView.setPreviewEnabled(true);
//        keyboardView.setVisibility(View.VISIBLE);
//        keyboardView.setOnKeyboardActionListener(listener);
//    }
//
//    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
//        @Override
//        public void swipeUp() {
//        }
//
//        @Override
//        public void swipeRight() {
//        }
//
//        @Override
//        public void swipeLeft() {
//        }
//
//        @Override
//        public void swipeDown() {
//        }
//
//        @Override
//        public void onText(CharSequence text) {
//        }
//
//        @Override
//        public void onRelease(int primaryCode) {
//        }
//
//        @Override
//        public void onPress(int primaryCode) {
//        }
//
//        // 一些特殊操作按键的codes是固定的比如完成、回退等
//        @Override
//        public void onKey(int primaryCode, int[] keyCodes) {
//            Editable editable = ed.getText();
//            int start = ed.getSelectionStart();
//            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
//                if (editable != null && editable.length() > 0) {
//                    if (start > 0) {
//                        editable.delete(start - 1, start);
//                    }
//                }
//            } else if (primaryCode == 4896) {// 清空
//                editable.clear();
//            } else { // 将要输入的数字现在编辑框中
//                editable.insert(start, Character.toString((char) primaryCode));
//            }
//        }
//    };
//
//    public void showKeyboard() {
//        Log.d(this, "showKeyboard()");
////        int visibility = keyboardView.getVisibility();
////        if (visibility == View.GONE || visibility == View.INVISIBLE) {
////            Log.d(this, "show keyboard");
//            keyboardView.setVisibility(View.VISIBLE);
////        }
//    }
//}