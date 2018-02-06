package com.ottd.libs.web.jsbridge;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ottd.libs.logger.OttdLog;
import com.ottd.libs.thread.ThreadManager;
import com.ottd.libs.ui.ToastUtil;
import com.ottd.libs.utils.APKUtils;
import com.ottd.libs.utils.TextUtils;
import com.ottd.libs.utils.device.APN;
import com.ottd.libs.utils.device.APNUtil;
import com.ottd.libs.utils.device.DeviceUtils;
import com.ottd.libs.utils.device.ExternalStorage;
import com.ottd.libs.utils.device.NetInfo;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public abstract class BaseJsBridgeProxy {

    public final String STATUS_OK = "1"; //通用状态，很多外部传"1"时表示要执行或要显示等
    public final String STATUS_NO = "0"; //通用状态，很多方法外部传”0“时表示不需要执行或不显示等

    public final int PAGE_CONTROL_RELOAD = 0;
    public final int PAGE_CONTROL_GO_BACK = 1;
    public final int PAGE_CONTROL_GO_FORWARD = 2;
    private static final String ACTIVITY_STATE_CHANGE_CALLBACK = "activityStateCallback";
    protected JsBridge mJsBridge;
    protected WebView mWebView;
    protected Context mContext;

    public BaseJsBridgeProxy(WebView webView, Context context) {
        mWebView = webView;
        mContext = context;
        this.mJsBridge = new JsBridge(mWebView,mContext);
    }

    /**
     * 负责url的调用处理逻辑
     *
     * @param url
     */
    public void invoke(final String url) {
        ThreadManager.getInstance().start(new Runnable() {
            @Override
            public void run() {
                invokeAsync(url);
            }
        });
    }

    private void invokeAsync(String url) {
        Uri uri = Uri.parse(url);
        //将URI中的host作为方法名，path中的第一个作为回call的方法名，如果没有回call的方法名，则不回call
        String hostAsMethodName = uri.getHost();
        if (TextUtils.ckIsEmpty(hostAsMethodName)) {
            return;
        }
        List<String> paths = uri.getPathSegments();
        int seqid = 0;//系列号，任何请求都要系列号，因为是异步调用，不然无法关联上
        String callbackName = null;
        if (paths != null && paths.size() > 0) {
            try{
                seqid = Integer.parseInt(paths.get(0));
            }catch (Exception e){
                e.printStackTrace();
                seqid = 0;
            }
            if (paths.size() > 1) {
                callbackName = paths.get(1);
            }
        }

        if (hostAsMethodName.equals(JsBridge.CALL_BATCH_NAME)) {
            try {
                String param = uri.getQueryParameter("param");
                JSONArray jsonArray = new JSONArray(param);
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String method = jsonObject.getString("method");
                    int seqidOfCall = jsonObject.getInt("seqid");
                    String callback = null;
                    if(jsonObject.has("callback")){
                        callback = jsonObject.optString("callback");
                    }
                    StringBuilder uriBuilder = new StringBuilder();
                    uriBuilder.append(JsBridge.JS_BRIDGE_SCHEME).append(method).
                            append("/").append(seqidOfCall).append("/").
                            append(!TextUtils.ckIsEmpty(callback) ? callback : "").append("?");

                    if(jsonObject.has("args")){
                        JSONObject args = jsonObject.getJSONObject("args");
                        if (args != null) {
                            Iterator iterator = args.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                String value = Uri.decode(args.getString(key));
                                uriBuilder.append(key).append("=").append(Uri.encode(value)).append("&");
                            }
                        }
                    }

                    Uri uriForCall = Uri.parse(uriBuilder.toString());
                    callAMethod(uriForCall, method, seqidOfCall, callback);
                }
            } catch (Exception ex) {
                OttdLog.d(ex.toString());
                ex.printStackTrace();
            }
        } else {
            callAMethod(uri, hostAsMethodName, seqid, callbackName);
        }
    }

    private void callAMethod(Uri uri, String hostAsMethodName, int seqid, String callbackName) {
        try {
            if (!TextUtils.ckIsEmpty(hostAsMethodName)) {
                Object obj = this;
                Method method = this.getClass().getMethod(hostAsMethodName, Uri.class, Integer.TYPE, String.class, String.class);
                method.invoke(obj, uri, seqid, hostAsMethodName, callbackName);

            } else {
                if (!TextUtils.ckIsEmpty(callbackName)) {
                    mJsBridge.responseFail(callbackName,seqid,hostAsMethodName, JsResult.Code_None);
                }
            }
        } catch (Exception ex) {
            OttdLog.e("JSBridge method has error");
            OttdLog.d(ex.toString());
            if (!TextUtils.ckIsEmpty(callbackName)) {
                mJsBridge.responseFail(callbackName,seqid,hostAsMethodName, JsResult.Code_Java_Exception);
            }
        }
    }

    private static class QueryController {
        public final static String QUERY_FIELDS = "queryfields"; // 保留字段，H5可以指定查询具体的字段返回，提高效率
        public final static String QUERY_FIELDS_SPLIT = "\\|";
        public final static int QUERY_TYPE_ALL = 0;
        public final static int QUERY_TYPE_PART = 1;
        public String[] queryFields;
        public int queryType = QUERY_TYPE_ALL;

        public QueryController(Uri uri) {
            if (uri != null) {
                String queryFieldStr = uri.getQueryParameter(QUERY_FIELDS);
                if (queryFieldStr != null) {
                    queryFields = queryFieldStr.split(QUERY_FIELDS_SPLIT);
                }
                if (queryFields != null && queryFields.length > 0) {
                    queryType = QUERY_TYPE_PART;
                }
            }
        }

        public boolean canQuery(String field) {
            if (TextUtils.ckIsEmpty(field)) {
                return false;
            }
            if (queryType == QUERY_TYPE_ALL) {
                return true;
            }
            if (queryFields != null) {
                for (int i = 0; i < queryFields.length; i++) {
                    if (field.equalsIgnoreCase(queryFields[i])) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void pageControl(final Uri uri, final int seqid, final String method, final String function) {
        int type = PAGE_CONTROL_RELOAD;
        try{
            type = Integer.parseInt(uri.getQueryParameter("type"));
        }catch (Exception e){
            e.printStackTrace();
        }
        if (mWebView != null) {
            if (type == PAGE_CONTROL_GO_BACK) {
                mWebView.goBack();
            } else if (type == PAGE_CONTROL_GO_FORWARD) {
                mWebView.goForward();
            } else {
                mWebView.reload();
            }
        }
        mJsBridge.response(function, seqid, method, "");
    }

    public void toast(final Uri uri, final int seqid, String method, final String callbackFun) {
        int def_duration = 0;
        try {
            String durationStr = uri.getQueryParameter("duration");
            if (!TextUtils.ckIsEmpty(durationStr)) {
                def_duration = Integer.parseInt(durationStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        final int duration = def_duration;
        final String text = uri.getQueryParameter("text");
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            ThreadManager.getInstance().runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(mContext, text, duration == 1 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                }
            });
        } else {
            ToastUtil.show(mContext, text, duration == 1 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        }

    }

    public void onResume(){
        HashMap<String,String> map = new HashMap<>();
        map.put("state",STATUS_OK);
        mJsBridge.response(ACTIVITY_STATE_CHANGE_CALLBACK,0,null,"onResume",map);
    }

    public void onPause(){
        mJsBridge.response(ACTIVITY_STATE_CHANGE_CALLBACK,0,null,"onPause");
    }

    public void getPrivateMobileInfo(final Uri uri, final int seqid, final String method, final String function) {
        Log.d("imei", "调用了getPrivateMobileInfo==========");
        JSONObject json = new JSONObject();
        try {
            JSONObject terminal = new JSONObject();
            terminal.put("androidId", DeviceUtils.getAndroidIdInPhone(mContext));
            terminal.put("imei", DeviceUtils.getImei(mContext));
            terminal.put("imsi", DeviceUtils.getImsi(mContext));
            terminal.put("macAdress", DeviceUtils.getLocalMacAddress(mContext));
            json.put("terminal", terminal);
            json.put("versionName", APKUtils.getAppVersionName(mContext));
            json.put("versionCode", APKUtils.getAppVersionCode(mContext));
            json.put("mark", android.os.Build.MANUFACTURER + "_" + android.os.Build.MODEL);
            OttdLog.d("imei", "imei:" + DeviceUtils.getImei(mContext) + ", imsi:" + DeviceUtils.getImsi(mContext));
            mJsBridge.response(function, seqid, method, json.toString());
        } catch (Exception e) {
            mJsBridge.responseFail(function, seqid, method, JsResult.Code_Java_Exception);
        }
    }

    public void getMobileInfo(final Uri uri, final int seqid, final String method, final String function) {
        JSONObject result = new JSONObject();
        QueryController controller = new QueryController(uri);
        try {
            if (controller.canQuery("osVer")) {
                result.put("osVer", android.os.Build.VERSION.SDK_INT);
            }
            if (controller.canQuery("resolution")) {
                result.put("resolution", DeviceUtils.getResolution(mContext));
            }
            if (controller.canQuery("network")) {
                JSONObject netJson = new JSONObject();
                NetInfo netInfo = APNUtil.getNetInfo(mContext);
                if (netInfo.apn == APN.UN_DETECT) {
                    APNUtil.refreshNetwork(mContext);
                }
                netJson.put("apn", netInfo.apn);
                netJson.put("isWap", netInfo.isWap ? 1 : 0);
                netJson.put("groupNetType", APNUtil.getGroupNetType(mContext));
                netJson.put("networkOperator", netInfo.networkOperator);
                netJson.put("networkType", netInfo.networkType);
                netJson.put("wifiBssid", netInfo.bssid);
                result.put("network", netJson);
            }
            if (controller.canQuery("extSDAvailable")) {
                result.put("extSDAvailable", ExternalStorage.isAvailable() ? 1 : 0);
            }
            mJsBridge.response(function, seqid, method, result.toString());
        } catch (Exception e) {
            mJsBridge.responseFail(function, seqid, method, JsResult.Code_Java_Exception);
        }
    }

    public void getAppInfo(final Uri uri, final int seqid, final String method, final String callbackFun) {
        final String packageName = uri.getQueryParameter("packagename");

        if (android.text.TextUtils.isEmpty(packageName) || android.text.TextUtils.isEmpty(callbackFun)) {
            return;
        }

        JSONObject result = new JSONObject();
        String pkgName = packageName.trim();
        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> packageList = pm.getInstalledPackages(0);

        if (null == packageList) {
            return ;
        }

        for (PackageInfo item : packageList) {
            JSONObject json = new JSONObject();
            try{
                if (item.applicationInfo.packageName.equalsIgnoreCase(pkgName)) {
                    json.put("install", 1);
                    json.put("appName", item.applicationInfo.loadLabel(pm).toString().trim());
                    json.put("verCode", item.versionCode);
                    json.put("verName", item.versionName == null ? "" : item.versionName);
                    result.put(pkgName, json);
                    mJsBridge.response(callbackFun, seqid, method, result.toString());
                    return;
                } else {
                    json.put("install", 0);
                }
                result.put(pkgName, json);
            }catch (Exception e) {
                mJsBridge.responseFail(callbackFun, seqid, method, JsResult.Code_Java_Exception);
            }
        }
        mJsBridge.response(callbackFun, seqid, method, result.toString());
    }
}
