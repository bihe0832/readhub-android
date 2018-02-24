package com.ottd.libs.config;

import android.content.Context;


/**
 * @author code@bihe0832.com
 */

public class Config {

	// 开关值为开启
	public static final String VALUE_SWITCH_ON = "true";
	// 开关值为关闭
	public static final String VALUE_SWITCH_OFF = "false";

	public static String readConfig (String key, String defValue){
		return ConfigManager.getInstance().readConfig(key,defValue);
	}

	public static int readConfig (String key, int defValue){
		return ConfigManager.getInstance().readConfig(key,defValue);
	}

	public static long readConfig (String key, long defValue){
		return ConfigManager.getInstance().readConfig(key,defValue);
	}

	public static boolean isSwitchEnabled (String switchKey, boolean defValue){
		return ConfigManager.getInstance().isSwitchEnabled(switchKey,defValue);
	}

	public static boolean setCloudConfig (String key, String defValue){
		return ConfigManager.getInstance().writeConfig(key,defValue);
	}

	public static void init (Context ctx, String file){
		ConfigManager.getInstance().init(ctx,file);
	}
}
