package com.hzsoft.musicdemo.utils;

import android.widget.Toast;

import com.hzsoft.musicdemo.MyApp;


/**
 * <p>吐司工具类</p>
 *
 * @author zhouhuan
 * @Data 2020/11/20
 */
public class ToastUtil {

    public static void showToast(String message) {
        Toast.makeText(MyApp.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int resid) {
        Toast.makeText(MyApp.getInstance(), MyApp.getInstance().getString(resid), Toast.LENGTH_SHORT)
                .show();
    }
}