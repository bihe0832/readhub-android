package com.bihe0832.readhub.framework.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author code@bihe0832.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceID());
        init();
        initView();
        initData();
    }

    protected void initData() {
    }

    /***
     * 用于在初始化View之前做一些事
     */
    protected void init() {

    }

    protected abstract void initView();

    protected abstract int getLayoutResourceID();

    protected <T extends View> T customFindViewById(int id) {
        return (T) super.findViewById(id);
    }


    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
