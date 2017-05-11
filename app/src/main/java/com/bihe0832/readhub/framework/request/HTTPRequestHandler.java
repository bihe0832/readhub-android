package com.bihe0832.readhub.framework.request;

/**
 * @author code@bihe0832.com
 */
public abstract class HTTPRequestHandler {
    public abstract void onSuccess(int statusCode,String response);
    public abstract void onFailure(int statusCode, String responseBody);
}
