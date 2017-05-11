package com.bihe0832.readhub.framework.request;
public interface HttpResponseHandler<T> {
	public void onResponse(T response);
}
