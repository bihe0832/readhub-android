package com.bihe0832.readhub.module.readhub.news.request;

import com.bihe0832.readhub.framework.request.HttpRequest;
import com.bihe0832.readhub.framework.request.HttpResponseHandler;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.SafeJSONObject;


public class GetNewsRequest extends HttpRequest {
	private static final String PATH = "/cgi/readhub.php";
	private static final String PARAM_CURSOR = "lastCursor";
	private static final String PARAM_PAGE_SIZE = "mPageSize";

	private static final int DEFAULT_PAGE_SIZE = 5;

    private HttpResponseHandler<GetNewsResponse> mResponseHandler;
	private String mLastCursor = "";
	private int mPageSize = DEFAULT_PAGE_SIZE;

    public GetNewsRequest(String cursor, int pagesize, HttpResponseHandler<GetNewsResponse> handler) {
    	super(PATH);
    	this.mResponseHandler = handler;
		this.mLastCursor = cursor;
		this.mPageSize = pagesize;
    }

	@Override
	protected String getUrl() {
		return "https://api.readhub.me/news?" +
				PARAM_CURSOR + HTTP_REQ_ENTITY_MERGE + this.mLastCursor + HTTP_REQ_ENTITY_JOIN +
				PARAM_PAGE_SIZE + HTTP_REQ_ENTITY_MERGE + this.mPageSize
				;
	}

	@Override
	protected void onRequestSuccess(int statusCode, SafeJSONObject responseJson) {
		GetNewsResponse getNewsResponse = new GetNewsResponse();
		getNewsResponse.parseSuccessResponse(statusCode, responseJson);
		Logger.d("CheckUpdateRequest,statusCode:"+statusCode);
		if (mResponseHandler != null) {
			mResponseHandler.onResponse(getNewsResponse);
		}
	}

	@Override
	protected void onRequestFailure(int statusCode, String errorResponse) {
		GetNewsResponse getNewsResponse = new GetNewsResponse();
		getNewsResponse.parseFailureResponse(statusCode, errorResponse);
		Logger.d("CheckUpdateRequest,statusCode:"+statusCode);
        if(mResponseHandler != null) {
            mResponseHandler.onResponse(getNewsResponse);
        }		
	}

}
