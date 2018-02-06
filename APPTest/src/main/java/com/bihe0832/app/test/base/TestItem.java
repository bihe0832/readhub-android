package com.bihe0832.app.test.base;

/**
 * Created by hardyshi on 2017/7/20.
 */

public class TestItem {
    String mTitle;
    OnTestItemClickListener mListener;
    public TestItem(String title,OnTestItemClickListener listener){
        mTitle = title;
        mListener = listener;
    }

    public String getTitle(){
        return mTitle;
    }
}
