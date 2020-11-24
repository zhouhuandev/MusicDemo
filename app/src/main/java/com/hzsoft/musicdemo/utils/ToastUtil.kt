package com.hzsoft.musicdemo.utils

import android.widget.Toast

import com.hzsoft.musicdemo.MyApp

/**
 * <p>吐司工具类</p>
 *
 * @author zhouhuan
 * @Data 2020/11/19
 */
object ToastUtil {

    fun showToast(message: String) {
        Toast.makeText(MyApp.instance, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resid: Int) {
        Toast.makeText(MyApp.instance, MyApp.instance?.getString(resid), Toast.LENGTH_SHORT)
                .show()
    }
}