package com.bihe0832.readhub.module.readhub.news.request;


import com.bihe0832.readhub.framework.request.HttpResponse;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.SafeJSONObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;


public class GetNewsResponse extends HttpResponse {

    public ArrayList<NewsInfo> mInfoList = new ArrayList<>();
    public int pageSize = 0;
    public String cursor = "";

    public GetNewsResponse() {
    	super();
    }
    
    @Override
    public void parseJson(SafeJSONObject json) {
        Logger.d("parseJson");
        if (null != json && json.has("data")) {
            try {
                JSONArray tempJson = json.getJSONArray("data");
                for (int i = 0; i < tempJson.length(); i++) {
                    JSONObject o = tempJson.getJSONObject(i);
                    NewsInfo info = getNewsInfo(new SafeJSONObject(o));
                    if(null !=  info){
                        mInfoList.add(info);
                    }else{
                        //TODO
                    }
                }
                pageSize = json.optInt("pageSize");
                cursor = getCursor(mInfoList.get(mInfoList.size() - 1).mPublishDate);

            }catch(Exception e) {
                Logger.d("JSONException : " + json.toString());
                e.printStackTrace();
            }
        } else{
            Logger.w("parseCheckUpdateRespones json is null or don't has data");
        }
    }

    private NewsInfo getNewsInfo(SafeJSONObject json){
        if(null != json){

            NewsInfo info = new NewsInfo();
            info.mId = json.getString("id");
            info.mSiteName = json.getString("siteName");
            info.mAuthorName = json.getString("authorName");
            info.mUrl = json.getString("url");
            info.mSummary = json.getString("summary");
            info.mTitle = json.getString("title");
            info.mPublishDate = json.getString("publishDate");

            return info;
        }else{
            return null;
        }
    }

    private String getCursor(String date){
        date = date.replace("T"," ");
        int a = date.lastIndexOf(".");
        date = date.substring(0, a);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(date);
            return String.valueOf(ts.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
