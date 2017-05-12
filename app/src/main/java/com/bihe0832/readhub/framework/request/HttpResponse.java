package com.bihe0832.readhub.framework.request;


import com.bihe0832.readhub.framework.common.BaseRet;
import com.bihe0832.readhub.framework.common.eFlag;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.SafeJSONObject;
import com.bihe0832.readhub.libware.util.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class HttpResponse {
    public static final String TAG = "HTTPRSP";
	public static final String HTTP_RESP_PARAM_RET = "ret";
    public static final String HTTP_RESP_PARAM_CODE = "errcode";
	public static final String HTTP_RESP_PARAM_MSG = "msg";
	public static final String HTTP_RESP_PARAM_ERRCODE = "flag";
	
    public int ret = BaseRet.RET_FAIL;
    public int flag;
    public String msg;
    
    public HttpResponse() {}
    
    public HttpResponse(int ret, int flag, String msg) {
        this.ret = ret;
        this.flag = flag;
        this.msg = msg;
    }
    
    public void parseSuccessResponse(int ret, SafeJSONObject responseJson) {
		if(responseJson == null) {
			this.ret = BaseRet.RET_FAIL;
			this.flag = eFlag.HttpRespNull;
			this.msg = "msg body is null, statusCode:" + ret;
            Logger.e(this.msg);
		} else {
            this.parseJson(responseJson);
		}
    }
    
    public void  parseFailureResponse(int statusCode,String errorResponse) {
		this.ret = BaseRet.RET_FAIL;
		this.flag = processNetErrorCode(statusCode);
        if(!TextUtils.ckIsEmpty(errorResponse)){
            this.msg = errorResponse;
        }
    }
    
    public abstract void parseJson(SafeJSONObject json);
    
    public void parseBaseJson(JSONObject json) {
        try {
            int ret = json.getInt(HTTP_RESP_PARAM_RET);
            msg = json.getString(HTTP_RESP_PARAM_MSG);
            if (ret == 0) {
            	this.ret = BaseRet.RET_SUCC;
                if(json.has(HTTP_RESP_PARAM_CODE)){
                    this.flag = json.getInt(HTTP_RESP_PARAM_CODE);
                }else{
                    this.flag = eFlag.Succ;
                }
            } else {
                this.ret = BaseRet.RET_FAIL;
                if(json.has(HTTP_RESP_PARAM_CODE)){
                    this.flag = json.getInt(HTTP_RESP_PARAM_CODE);
                }else{
                    this.flag = ret;
                }
                Logger.e(TAG,"=======================================");
                Logger.e(TAG,this.getClass().getName());
                Logger.e(TAG,"Server Error,ret:"+ret+";flag:"+ flag +";msg:"+msg);
                Logger.e(TAG,"=======================================");
            }
        } catch (JSONException e) {
            Logger.d(TAG,"Response JSONException : " + json.toString());
            ret = BaseRet.RET_FAIL;
            flag = eFlag.HttpRespParseError;
            msg = "Response JsonException:" + e.getMessage();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ret=" + ret);
        builder.append("&flag=" + flag);
        builder.append("&msg=" + msg);
        return builder.toString();
    }
    
	/**
	 * @param statusCode
	 *            当其值为0时说明是异常类错误；当其值为大于300时，说明是http错误；否则的话按默认系统错误处理 网络请求模块的错误处理
	 */
	private int processNetErrorCode(int statusCode) {
		int code = eFlag.NetWorkException;
		if (statusCode == 0) {
			code = eFlag.NetWorkException;
		} else if (statusCode > 300) {
			code = eFlag.HttpSatutsError;
		}
		return code;
	}
}
