package com.bihe0832.readhub.framework.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.CommonUtils;
import com.bihe0832.readhub.libware.util.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author code@bihe0832.com
 */

public class ConfigManager {
	private static volatile ConfigManager instance = null;
	private static final String TAG = "ConfigManager";

	private Properties mLocalConfig = null;
	private HashMap<String,String> mConfigInfoInCache = new HashMap<>();
	private SharedPreferences mCloudAllConfigSP = null;
	private SharedPreferences mCloudPlatConfigSP = null;
	private SharedPreferences mCloudUserConfigSP = null;
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
	
	public void init() {
		Context ctx = Shakeba.getInstance().getApplicationContext();
	    if (ctx == null) {
	        Logger.e(TAG, "context is null");
	        return;
	    }

	    InputStream inputStream = null;
		try {
			mLocalConfig = new Properties();
			inputStream = ctx.getResources().getAssets().open(Config.CONFIG_FILE_NAME);
			mLocalConfig.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.e("YSDK_DOCTOR", "ERROR-YSDK config file");
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
				Logger.w(TAG, "key value is empty: " + key);
				return value;
			}
			return value.trim();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.w(TAG, "readLocalConfig failed");
			
			return value;
		}
	}

	// 从云端推送保存到本地SharedPreferences的config，云端配置读取不使用默认值
	private String readCloudConfig (String key) {
		String value = null;
		try {
			//先看opendid
			if (null != mCloudUserConfigSP &&  mCloudUserConfigSP.contains(key)) {
				return mCloudUserConfigSP.getString(key, null);
			}
			//再看平台
			if (null != mCloudPlatConfigSP && mCloudPlatConfigSP.contains(key)) {
				return mCloudPlatConfigSP.getString(key, null);
			}
			//最后看公共的
			if (null != mCloudAllConfigSP &&  mCloudAllConfigSP.contains(key)) {
				return mCloudAllConfigSP.getString(key, null);
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	Logger.w(TAG, "readCloudConfig failed");
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
	


	/**
	 * 从assert下指定文件获取value，除非必须不要使用该方法
	 *
	 * @param ctx
	 *            上下文
	 * @param key
	 *            获取的key值
	 * @return value
	 */
	@Deprecated
	public static String readConfigFromFile (Context ctx, String fileName, String key, String defValue) {
		String value = defValue;

	    if (ctx == null) {
	        Logger.e(TAG, "readConfigFromFile context is null");
	        return value;
	    }

	    InputStream inputStream = null;
		try {
			inputStream = ctx.getResources().getAssets()
					.open(fileName);
			Properties properties = new Properties();
			properties.load(inputStream);
			value = properties.getProperty(key, "");
			if (value == null || value.length() == 0) {
				Logger.d(TAG, "no key: " + key);
				return "";
			}

			return value.trim();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
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

	protected int readConfig(String key, int defaultValue) {
		String configInterval = readConfig(key,"");
		return CommonUtils.parseInt(configInterval,defaultValue);
	}
}
