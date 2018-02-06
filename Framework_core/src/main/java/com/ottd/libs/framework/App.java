package com.ottd.libs.framework;

import android.app.Application;

import com.ottd.libs.config.Config;
import com.ottd.libs.logger.OttdLog;


public class App extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        OttdFramework.getInstance().init(getApplicationContext());

        final String config_FILE = "config.ini";
        Config.init(this,config_FILE);

        OttdLog.init(this,OttdFramework.getInstance().isDebug());
    }
}
