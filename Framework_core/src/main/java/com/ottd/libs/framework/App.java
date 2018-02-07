package com.ottd.libs.framework;

import android.app.Application;

import com.ottd.libs.config.Config;
import com.ottd.libs.logger.OttdLog;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OttdFramework.getInstance().init(getApplicationContext());
    }
}
