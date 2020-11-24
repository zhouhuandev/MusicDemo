package com.hzsoft.musicdemo.utils;

import android.Manifest;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Describe:
 * <p>权限检查</p>
 *
 * @author zhouhuan
 * @Date 2020/11/20
 */
public class PermissionCheckUtil {
    /**
     * 检查定位，存储权限
     *
     * @param activity
     */
    public static void checkLocation(FragmentActivity activity) {
        new RxPermissions(activity).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    ToastUtil.showToast("缺少定位权限、存储权限，这会导致地图、导航、拍照等部分功能无法使用");
                }
            }
        });
    }

    /**
     * 检查网络权限
     *
     * @param activity
     */
    public static void checkInternet(FragmentActivity activity) {
        new RxPermissions(activity).request(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    ToastUtil.showToast("缺少网络权限");
                }
            }
        });
    }
}
