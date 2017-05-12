package com.bihe0832.readhub.module.readhub.topic.request;


import com.bihe0832.readhub.framework.request.HttpResponse;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.SafeJSONObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetTopicResponse extends HttpResponse {

    public ArrayList<TopicInfo> mInfoList = new ArrayList<>();
public int pageSize = 0;
    public String cursor = "";

    public GetTopicResponse() {
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
                    TopicInfo info = getTopicInfo(new SafeJSONObject(o));
                    if(null !=  info){
                        mInfoList.add(info);
                        cursor = String.valueOf(info.mId);
                    }else{
                        //TODO
                    }
                }
                pageSize = json.optInt("pageSize");
            }catch(Exception e) {
                Logger.d("JSONException : " + json.toString());
                e.printStackTrace();
            }
        } else{
            Logger.w("parseCheckUpdateRespones json is null or don't has data");
        }
    }

    private TopicInfo getTopicInfo(SafeJSONObject json){
        if(null != json){

            TopicInfo info = new TopicInfo();
            info.mId = json.getString("id");
            info.mTitle = json.getString("title");
            info.mSummary = json.getString("summary");
            info.mPublishDate = json.getString("publishDate");
            info.mOrder = json.getLong("order");
            if(json.has("newsArray")){
                try {
                    JSONArray tempJson = json.getJSONArray("newsArray");
                    for (int i = 0; i < tempJson.length(); i++) {
                        JSONObject o = tempJson.getJSONObject(i);
                        TopicInfoDetail infodetail = getTopicInfoDetail(new SafeJSONObject(o));
                        if(null !=  info){
                            info.mNewsArrayList.add(infodetail);
                        }else{
                            //TODO
                        }
                    }
                }catch (Exception e){
                    //TODO
                    e.printStackTrace();
                }
            }
            return info;
        }else{
            return null;
        }
    }

    private TopicInfoDetail getTopicInfoDetail(SafeJSONObject json){
        if(null != json){
            TopicInfoDetail info = new TopicInfoDetail();
            info.mId = json.getLong("id");
            info.mTitle = json.getString("title");
            info.mUrl = json.getString("url");
            info.mSummary = json.getString("summary");
            info.mSummaryAuto = json.getString("summary");
            info.mAuthorName = json.getString("authorName");
            info.mMobileUrl = json.getString("mobileUrl");
            info.mPublishDate = json.getString("publishDate");
            info.mSiteName = json.getString("siteName");
            return info;
        }else{
            return null;
        }

    }
}
