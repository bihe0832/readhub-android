package com.ottd.libs.framework

import android.app.Application

import com.ottd.libs.config.Config
import com.ottd.libs.logger.OttdLog


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        OttdFramework.getInstance().init(applicationContext)
    }
}
