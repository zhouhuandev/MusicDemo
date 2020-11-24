package com.hzsoft.musicdemo.utils

import android.Manifest
import android.annotation.SuppressLint
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * <p>权限检查</p>
 *
 * @author zhouhuan
 * @Data 2020/11/19
 */
object PermissionCheckUtil {
    /**
     * 检测权限，其中一个不能为 null
     */
    @SuppressLint("CheckResult")
    fun check(activity: androidx.fragment.app.FragmentActivity?) {
        var rxPermissions: RxPermissions? = null
        if (activity != null) {
            rxPermissions = RxPermissions(activity)
        }
        rxPermissions!!.request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { aBoolean ->
            if (!aBoolean) {
                ToastUtil.showToast("缺少存储权限，将会导致部分功能无法使用")
            }
        }
    }
}
