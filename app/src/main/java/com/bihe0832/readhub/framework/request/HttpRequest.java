package com.bihe0832.readhub.framework.request;


import com.bihe0832.readhub.framework.common.eFlag;
import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.SafeJSONObject;
import com.bihe0832.readhub.libware.util.TextUtils;

import org.json.JSONException;

import java.util.HashMap;

/**
 * @author code@bihe0832.com
 */
public abstract class HttpRequest {

	public static final String HTTP_REQ_ENTITY_MERGE = "=";
	public static final String HTTP_REQ_ENTITY_JOIN = "&";

	public static final String PARAM_VERSION_NAME = "versionName";
	public static final String PARAM_VERSION_CODE = "versionCode";
	public static final String PARAM_OS = "os";

	private static final String LOG_TAG = "YSDK_REQUEST";
	protected static String HTTP_DOMAIN = "http://shakeba.bihe0832.com";

	protected String path = "";
	protected long requestTime = 0;
	public byte[] data = null;
	public HashMap<String,String> cookieInfo = new HashMap<>();

	protected boolean reportIMEI = false;

	protected abstract String getUrl();

	protected abstract void onRequestSuccess(int statusCode, SafeJSONObject responseJson);

	protected abstract void onRequestFailure(int statusCode, String errorResponse);

	protected HttpRequest(String cgi){
		if(TextUtils.ckIsEmpty(cgi)){
			Logger.d(LOG_TAG,"path is null");
			path = "";
		}else{
			path = cgi;
		}
	}

	private String getDomain() {
		return HTTP_DOMAIN;
    }

	protected SafeJSONObject getBaseBody() {
		SafeJSONObject json = new SafeJSONObject();

		try {
			json.put(PARAM_OS, "android");
			json.put(PARAM_VERSION_NAME, Shakeba.getInstance().getVersionName());
			json.put(PARAM_VERSION_CODE, Shakeba.getInstance().getVersionCode());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	protected String getBaseUrl() {
		return getDomain() + path;
	}

	public HTTPRequestHandler mHttpResponseHandler = new HTTPRequestHandler() {

		@Override
		public void onSuccess(int statusCode,String response) {
			try {
				if(TextUtils.ckIsEmpty(response)){
					Logger.e(LOG_TAG,"responseBody is null");
				}else{
					int flag = eFlag.Error;
					try {
						SafeJSONObject json = new SafeJSONObject(response);
						int ret = json.getInt(HttpResponse.HTTP_RESP_PARAM_RET);
						if (ret == 0) {
							flag = eFlag.Succ;
						} else {
							flag = ret;
						}
						onRequestSuccess(ret, json);
					} catch (JSONException e) {
						flag = eFlag.HttpRespParseError;
						onRequestFailure(flag, response);
					}
				}
			}catch (Exception e){
				Logger.d(e.toString());
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, String responseBody) {
			if (null == responseBody) {
				Logger.e(LOG_TAG,"responseBody is null");
				onRequestFailure(statusCode, null);
			}else{
				onRequestFailure(statusCode, responseBody);
			}
			int code = eFlag.NetWorkException;
			if (statusCode == 0) {
				code = eFlag.NetWorkException;
			} else if (statusCode > 300) {
				code = eFlag.HttpSatutsError;
			}
		}
	};
}
