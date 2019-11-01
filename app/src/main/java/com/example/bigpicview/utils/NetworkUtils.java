package com.example.bigpicview.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

public class NetworkUtils {

    private NetworkUtils(){
        throw  new UnsupportedOperationException("不能初始化....NetworkUtils构造器");
    }
    // TODO 判断是否打开网络
    public static boolean isNetworkAvaliable(Context context){
        boolean isAvaliable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo!= null && networkInfo.isAvailable()){
            isAvaliable = true;
        }
        return isAvaliable;
    }

    // TODO 跳转到网络的设置界面
    public static void getSettings(Context context){
        Intent intent = null;
        //先判断当前系统的版本
        if (Build.VERSION.SDK_INT > 10){//必须大于3.0 os
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        }else{
            intent = new Intent();
            intent.setClassName("com.android.settings"
            ,"com.android.settings.WirelessSettings");
        }
        context.startActivity(intent);
    }

}
