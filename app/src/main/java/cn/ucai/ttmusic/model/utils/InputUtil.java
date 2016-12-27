package cn.ucai.ttmusic.model.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.ucai.ttmusic.TTApplication;

public class InputUtil {

    public static void hideSoftInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) TTApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
