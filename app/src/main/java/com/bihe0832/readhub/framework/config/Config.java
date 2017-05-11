package com.bihe0832.readhub.framework.config;

/**
 * @author code@bihe0832.com
 */

public class Config {
	/*********************全局配置  START******************/
	//公用文件路径
	public static final String EXT_DIR_PATH = "zixie";
	//配置文件名称
	public static final String CONFIG_FILE_NAME = "config.ini";
	/*********************全局配置  END******************/

	//配置文件日志开关
	public static final String NEED_LOCAL_LOG = "SB_LOG_TYPE";

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

	public static boolean isSwitchEnabled (String switchKey, boolean defValue){
		return ConfigManager.getInstance().isSwitchEnabled(switchKey,defValue);
	}

	public static void init (){
		ConfigManager.getInstance().init();
	}
}
