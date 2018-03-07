package com.ottd.libs.framework

import android.app.Application
import com.ottd.libs.crash.JYCrash
import com.tencent.mta.track.StatisticsDataAPI
import com.tencent.stat.StatService


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        OttdFramework.getInstance().init(applicationContext)
        JYCrash.init(getApplicationContext(), "eda6c0c655",  OttdFramework.getInstance().isDebug)
        StatService.startStatService(this, null, com.tencent.stat.common.StatConstants.VERSION)
        StatisticsDataAPI.instance(this)
    }
}
