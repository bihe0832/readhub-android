package com.bihe0832.readhub.webview;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bihe0832.readhub.R;
import com.ottd.libs.framework.OttdFramework;
import com.ottd.libs.framework.fragment.BaseBackFragment;
import com.ottd.libs.logger.OttdLog;
import com.ottd.libs.utils.APKUtils;
import com.ottd.libs.utils.TextUtils;
import com.ottd.libs.web.X5ObserWebView;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.reflect.Field;


/**
 * @author code@bihe0832.com
 */

public class WebViewFragment extends BaseBackFragment {

    protected X5ObserWebView mWebView;
    protected ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeLayout;

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private static final String URL_USER_AGENT_JYGAME_VERSION = "readhub/android/zixie";

    private String mTitle;
    private String mURL;


    public static WebViewFragment newInstance(String url, String title) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebviewActivity.INTENT_EXTRA_KEY_WEBVIEW_ITEM_URI, url);
        bundle.putString(WebviewActivity.INTENT_EXTRA_KEY_WEBVIEW_ITEM_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(WebviewActivity.INTENT_EXTRA_KEY_WEBVIEW_ITEM_TITLE);
            if(!TextUtils.ckIsEmpty(mTitle)){
                mTitle = OttdFramework.getInstance().getApplicationContext().getResources().getString(R.string.app_name);
            }
            mURL = bundle.getString(WebviewActivity.INTENT_EXTRA_KEY_WEBVIEW_ITEM_URI);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.com_bihe0832_webview_fragment, container, false);
        initView(view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //不在最前端界面显示
        } else {
            //重新显示到最前端
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.loadUrl(getLoadUrl());
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if(canGoBack()){
            goBack();
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mWebView.stopLoading();
        mWebView.clearHistory();
        mWebView.destroy();
        mWebView = null;

        if (Build.VERSION.SDK_INT < 16) {
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getLoadUrl() {
        if(TextUtils.ckIsEmpty(mURL)){
            mURL = OttdFramework.getInstance().getApplicationContext().getString(R.string.link_readhub_page);
        }
        OttdLog.d(mURL);
        return mURL;
    }

    private void initView(View view){
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        mToolbar.setTitle(null);//设置主标题
//        TextView textView = (TextView) view.findViewById(R.id.toolbar_title);
//        textView.setText(mTitle);
//        initToolbarNav(mToolbar);

        mProgressBar = (ProgressBar) view.findViewById(R.id.webview_progressbar);
        mProgressBar.setMax(100);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.webview_swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mWebView.loadUrl(mWebView.getUrl());
            }
        });

        mWebView = (X5ObserWebView) view.findViewById(R.id.webview_webView);

        initWebViewSettings(view);

        mWebView.loadUrl(getLoadUrl());

    }

    private void initWebViewSettings(View view) {
        //增加webview的调试
        if(Build.VERSION.SDK_INT > 19 ){
            if(OttdFramework.getInstance().isDebug()){
                mWebView.setWebContentsDebuggingEnabled(true);
            }else{
                mWebView.setWebContentsDebuggingEnabled(false);
            }
        }

        mWebView.setOnScrollChangedCallback(new X5ObserWebView.OnScrollChangedCallback() {
            public void onScroll(int l, int t) {
                //Log.d(TAG, "We Scrolled etc..." + l + " t =" + t);
                if (t > 0) {
                    //webView不是顶部
                    mSwipeLayout.setEnabled(false);
                } else {
                    //webView在顶部
                    mSwipeLayout.setEnabled(true);
                }
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        setHTMLSupport(webSettings);
        setWebClientSupport(mWebView);
        setUserAgentSupport(webSettings);
    }

    private void setUserAgentSupport(WebSettings webSettings){
        String userAgent = webSettings.getUserAgentString();
        String myAgent = URL_USER_AGENT_JYGAME_VERSION + "/" + APKUtils.getAppVersionName(OttdFramework.getInstance().getApplicationContext()) + "/" +
                        "/" + APKUtils.getAppVersionCode(OttdFramework.getInstance().getApplicationContext());
        if (TextUtils.ckIsEmpty(userAgent)) {
            userAgent = myAgent;
        } else {
            if(userAgent.endsWith("/")){
                userAgent = userAgent + myAgent;
            }else{
                userAgent = userAgent + "/" + myAgent;
            }
        }
        webSettings.setUserAgentString(userAgent);

    }
    private void setWebClientSupport(WebView view) {
        //设置WebView属性，能够执行Javascript脚本
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        //防webview远程代码执行漏洞
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            mWebView.removeJavascriptInterface("accessibility");
            mWebView.removeJavascriptInterface("accessibilityTraversal");
        }
    }

    private void setHTMLSupport(WebSettings webSettings){
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);;
        //设置缓存类型
        resetCacheType();

        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.supportMultipleWindows();  //多窗口
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    private void resetCacheType() {
        //设置缓存类型
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置缓存位置
        String cacheDirPath = OttdFramework.getInstance().getApplicationContext().getFilesDir().getAbsolutePath()+ APP_CACAHE_DIRNAME;
        OttdLog.d("cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);
    }
    //WebViewClient就是帮助WebView处理各种通知、请求事件的。
    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(handleConsoleMessage(url)){
                return true;
            }else{
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        public boolean handleConsoleMessage(String url) {
            if (TextUtils.ckIsEmpty(url)) {
                return false;
            }
            OttdLog.d(url);

            if (url.startsWith("http") || url.startsWith("https")) {
                return false;
            } else if (url.startsWith("file")) {
                return true;
            }else if (url.equals("about:blank;") || url.equals("about:blank")) {
                // 3.0及以下的webview调用jsb时会调用同时call起的空白页面，将这个页面屏蔽掉不出来
                return Build.VERSION.SDK_INT < 11;// Build.VERSION_CODES.HONEYCOMB
            } else {
                return false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    //WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
    class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.e("onProgressChanged", newProgress + "");
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            if (newProgress == 100) {
                //隐藏进度条
                mSwipeLayout.setRefreshing(false);
            } else {
                if (!mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.setRefreshing(true);
                }
            }

            super.onProgressChanged(view, newProgress);
        }
        //获取Web页中的title用来设置自己界面中的title
        //当加载出错的时候，比如无网络，这时onReceiveTitle中获取的标题为 找不到该网页,
        //因此建议当触发onReceiveError时，不要使用获取到的title
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }


        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return true;
        }

        //处理confirm弹出框
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            OttdLog.e(  "onJsPrompt " + url);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        //处理prompt弹出框
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            OttdLog.e("onJsConfirm " + message);
            return super.onJsConfirm(view, url, message, result);
        }
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            OttdLog.e("onJsAlert " + message);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

    }



    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    public boolean canGoBack() {
        if(mWebView != null && !TextUtils.ckIsEmpty(mWebView.getUrl())){
            OttdLog.d(mWebView.getUrl());
            OttdLog.d(getLoadUrl());
            if(mWebView.getUrl().equalsIgnoreCase(getLoadUrl())){
                mWebView.clearHistory();
                return false;
            }else{
                return mWebView.canGoBack();
            }
        }else{
            return false;
        }
    }

    public void goBack() {
        if (mWebView != null) {
            mWebView.goBack();
        }
    }
}
