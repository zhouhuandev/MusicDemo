package com.hzsoft.musicdemo

import android.app.Application
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

/**
 * Describe:
 * <p></p>
 *
 * @author zhouhuan
 * @Date 2020/11/19
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initUm()
    }

    fun initUm() {
        UMConfigure.init(
            this,
            "5fb738981e29ca3d7bdef32a",
            "Hzsoft_Kotlin",
            UMConfigure.DEVICE_TYPE_PHONE,
            ""
        )
        //选择AUTO页面采集模式，统计SDK基础指标无需手动埋点可自动采集
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }

    companion object {
        var instance: MyApp? = null
            private set
    }
}