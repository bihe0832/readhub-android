package com.bihe0832.readhub.module.readhub.topic.request;

import com.bihe0832.readhub.framework.request.HttpRequest;
import com.bihe0832.readhub.framework.request.HttpResponseHandler;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.SafeJSONObject;

import org.json.JSONException;


public class GetTopicRequest extends HttpRequest {
	private static final String PATH = "/cgi/readhub.php";
	private static final String PARAM_CURSOR = "lastCursor";
	private static final String PARAM_PAGE_SIZE = "mPageSize";

	private static final int DEFAULT_PAGE_SIZE = 5;

    private HttpResponseHandler<GetTopicResponse> mResponseHandler;
	private String mLastCursor = "";
	private int mPageSize = DEFAULT_PAGE_SIZE;

    public GetTopicRequest(String cursor, int pagesize, HttpResponseHandler<GetTopicResponse> handler) {
    	super(PATH);
    	this.mResponseHandler = handler;
		this.mLastCursor = cursor;
		this.mPageSize = pagesize;
    }

	@Override
	protected String getUrl() {
		return "https://api.readhub.me/topic?" +
				PARAM_CURSOR + HTTP_REQ_ENTITY_MERGE + this.mLastCursor + HTTP_REQ_ENTITY_JOIN +
				PARAM_PAGE_SIZE + HTTP_REQ_ENTITY_MERGE + this.mPageSize
				;
	}

	@Override
	protected void onRequestSuccess(int statusCode, SafeJSONObject responseJson) {
		GetTopicResponse getTopicResponse = new GetTopicResponse();
		getTopicResponse.parseSuccessResponse(statusCode, responseJson);
		Logger.d("CheckUpdateRequest,statusCode:"+statusCode);
		if (mResponseHandler != null) {
			mResponseHandler.onResponse(getTopicResponse);
		}
	}

	@Override
	protected void onRequestFailure(int statusCode, String errorResponse) {
		GetTopicResponse getTopicResponse = new GetTopicResponse();
		getTopicResponse.parseFailureResponse(statusCode, errorResponse);
		Logger.d("CheckUpdateRequest,statusCode:"+statusCode);
        if(mResponseHandler != null) {
            mResponseHandler.onResponse(getTopicResponse);
        }		
	}

}
