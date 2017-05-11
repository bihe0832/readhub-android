package com.bihe0832.readhub.module.update;

import com.bihe0832.readhub.framework.request.HttpRequest;
import com.bihe0832.readhub.framework.request.HttpResponseHandler;
import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.SafeJSONObject;

import org.json.JSONException;


public class CheckUpdateRequest extends HttpRequest {
	private static final String PATH = "/cgi/readhub.php";
	private static final String PARAM_CMD = "cmd";
	private static final String PARAM_VERSIONCODE = "versionCode";


    private HttpResponseHandler<CheckUpdateResponse> mResponseHandler;

    public CheckUpdateRequest (HttpResponseHandler<CheckUpdateResponse> handler) {
    	super(PATH);
    	this.mResponseHandler = handler;
    }

	@Override
	protected String getUrl() {
		return getBaseUrl();
	}

	public SafeJSONObject getBody() {
        SafeJSONObject json = getBaseBody();
		try {
			json.put(PARAM_CMD, 101001);
			json.put(PARAM_VERSIONCODE, Shakeba.getInstance().getVersionCode());
		}catch(JSONException e) {
			Logger.d("JSONException : " + json.toString());
			e.printStackTrace();
		}
		return json;
	}

	@Override
	protected void onRequestSuccess(int statusCode, SafeJSONObject responseJson) {
		CheckUpdateResponse checkUpdateResponse = new CheckUpdateResponse();
		checkUpdateResponse.parseSuccessResponse(statusCode, responseJson);
		Logger.d("CheckUpdateRequest,statusCode:"+statusCode);
		if (mResponseHandler != null) {
			mResponseHandler.onResponse(checkUpdateResponse);
		}
	}

	@Override
	protected void onRequestFailure(int statusCode, String errorResponse) {
		CheckUpdateResponse checkUpdateResponse = new CheckUpdateResponse();
		checkUpdateResponse.parseFailureResponse(statusCode, errorResponse);
		Logger.d("CheckUpdateRequest,statusCode:"+statusCode);
        if(mResponseHandler != null) {
            mResponseHandler.onResponse(checkUpdateResponse);
        }		
	}

}
