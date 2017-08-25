package com.grantsome.valuelive.utils;

import android.widget.Toast;

import com.grantsome.valuelive.App;

/**
 * Created by tom on 2017/4/27.
 */

public class ToastUtils {

    private static Toast sToast;

    public static void showError(String error){
        if(null == sToast){
            sToast = Toast.makeText(App.getContext(),error,Toast.LENGTH_SHORT);
        }else {
            sToast.setText(error);
        }
        sToast.show();
    }

    public static void showHint(String result){
        if(null == sToast){
            sToast = Toast.makeText(App.getContext(),result,Toast.LENGTH_SHORT);
        }else {
            sToast.setText(result);
        }
        sToast.show();
    }

    public static boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            showError(e.getMessage());
            return false;
        } else {
            return true;
        }
    }
}
