package com.ottd.libs.framework;

import android.content.Context;

import com.ottd.libs.ui.ToastUtil;


/**
 * Created by hardyshi on 2017/6/27.
 *
 */

public class OttdFramework {

    private Context mApplicationContext = null;

    //是否为调试版本，主要用于开发中打一些提供给游戏定位问题
    private static final boolean IS_TEST_VERSION = true;
    private static volatile OttdFramework instance = null;
    private OttdFramework(){}
    public static OttdFramework getInstance() {
        if (instance == null) {
            synchronized (OttdFramework.class) {
                if (instance == null) {
                    instance = new OttdFramework();
                }
            }
        }
        return instance;
    }

    // 全局变量的初始化
    public void init(final Context ctx) {
        mApplicationContext = ctx;
        OttdFramework.getInstance().showDebug("测试版本，体验问题请及时联系子勰");
    }

    public Context getApplicationContext(){
        return mApplicationContext;
    }

    public boolean isDebug(){
        return IS_TEST_VERSION;
    }

    public void showWaitting(){
        ToastUtil.showShort(getApplicationContext(),"功能开发中，敬请期待~");
    }

    public void showDebug(String msg){
        if(isDebug()){
            ToastUtil.showShort(getApplicationContext(),msg);
        }
    }
}
