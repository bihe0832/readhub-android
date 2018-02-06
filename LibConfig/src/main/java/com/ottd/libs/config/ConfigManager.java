package com.ottd.libs.config;

import android.content.Context;
import android.content.SharedPreferences;


import com.ottd.libs.logger.OttdLog;
import com.ottd.libs.utils.CommonUtils;
import com.ottd.libs.utils.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author code@bihe0832.com
 */

public class ConfigManager {
	private static volatile ConfigManager instance = null;

	//配置文件配置
	private Properties mLocalConfig = null;
	//内存中的配置
	private HashMap<String,String> mConfigInfoInCache = new HashMap<>();
	//云端的配置
	private SharedPreferences mCloudConfigSP = null;

	public static ConfigManager getInstance() {
		if (instance == null) {
			synchronized (ConfigManager.class) {
				if (instance == null) {
					instance = new ConfigManager();
				}
			}
		}
		return instance;
	}
	
	public void init(Context ctx, String file) {
	    if (ctx == null) {
			OttdLog.w("context is null");
	        return;
	    }

	    InputStream inputStream = null;
		try {
			mLocalConfig = new Properties();
			mCloudConfigSP = ctx.getSharedPreferences(file.toUpperCase(), Context.MODE_PRIVATE);
			inputStream = ctx.getResources().getAssets().open(file);
			mLocalConfig.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			OttdLog.e("ERROR: config file");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 从缓存的Properties获取编译apk预置的config
	private String readLocalConfig (String key) {
		if (mLocalConfig.containsKey(key) != true) {
			return null;
		}
		
		String value = null;
		try {
			value = mLocalConfig.getProperty(key, null);
			
			if (value == null || value.length() == 0) {
				OttdLog.w("key value is empty: " + key);
				return value;
			}
			return value.trim();
		} catch (Exception e) {
			e.printStackTrace();
			OttdLog.w("readLocalConfig failed");
			return value;
		}
	}

	// 从云端推送保存到本地SharedPreferences的config，云端配置读取不使用默认值
	private String readCloudConfig (String key) {
		String value = null;
		try {
			if (null != mCloudConfigSP &&  mCloudConfigSP.contains(key)) {
				return mCloudConfigSP.getString(key, null);
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
			OttdLog.w("readCloudConfig failed");
	    }
		return value;
	}

	protected String readConfig (String key, String defValue) {
		String value = null;
		if(mConfigInfoInCache.containsKey(key)){
			value = mConfigInfoInCache.get(key);
		}
		if(!TextUtils.ckIsEmpty(value)){
			return value;
		}
		value = readCloudConfig(key);
		if(TextUtils.ckIsEmpty(value)){
			value = readLocalConfig(key);
		}

		if (value == null || value.length() == 0) {
			value = defValue;
		}
		mConfigInfoInCache.put(key,value);
		OttdLog.d("readConfig: key=" + key + ";value="+ value);
		return value;
	}

	protected boolean isSwitchEnabled (String switchKey, boolean defValue) {
		String value = readConfig(switchKey,"");
		if(!TextUtils.ckIsEmpty(value)){
			if(Config.VALUE_SWITCH_ON.equalsIgnoreCase(value)){
				return true;
			}else if(Config.VALUE_SWITCH_OFF.equalsIgnoreCase(value)){
				return false;
			}else{
				return defValue;
			}
		}else{
			return defValue;
		}
	}

	protected int readConfig(String key, int defaultValue) {
		String configInterval = readConfig(key,"");
		return CommonUtils.parseInt(configInterval,defaultValue);
	}

	protected long readConfig(String key, long defaultValue) {
		String configInterval = readConfig(key,"");
		return CommonUtils.parseLong(configInterval,defaultValue);
	}

	protected boolean writeConfig(String key, String value) {
		try {
			if(TextUtils.ckIsEmpty(value)){
				OttdLog.d("writeConfig, value is null:"+ key);
				return false;
			}
			if(null != mConfigInfoInCache){
				mConfigInfoInCache.put(key,value);
			}else {
				return false;
			}

			if(null == mCloudConfigSP){
				OttdLog.d("writeConfig, sp is null:");
				return false;
			}

			SharedPreferences.Editor editor = mCloudConfigSP.edit();
			editor.putString(key,value);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
