package model.utils;

import android.widget.Toast;

import view.activity.SugarApplication;

//import cn.ucai.fulicenter.SugarApplication;

public class CommonUtils {
    public static void showLongToast(String msg){
        Toast.makeText(SugarApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortToast(String msg){
        Toast.makeText(SugarApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(int rId){
        showLongToast(SugarApplication.getInstance().getString(rId));
    }
    public static void showShortToast(int rId){
        showShortToast(SugarApplication.getInstance().getString(rId));
    }
}
