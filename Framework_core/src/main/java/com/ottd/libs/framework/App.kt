package com.ottd.libs.framework

import android.app.Application
import com.tencent.mta.track.StatisticsDataAPI
import com.tencent.stat.StatService


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        OttdFramework.getInstance().init(applicationContext)
        StatService.startStatService(this, null, com.tencent.stat.common.StatConstants.VERSION)
        StatisticsDataAPI.instance(this);
    }
}
