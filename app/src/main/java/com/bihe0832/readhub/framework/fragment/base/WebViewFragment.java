package com.bihe0832.readhub.framework.fragment.base;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.ui.ToastUtil;

/**
 * @author code@bihe0832.com
 */

public abstract class WebViewFragment extends BaseFragment {

    protected WebView mWebView;
    protected ProgressBar mProgressBar;

    private SwipeRefreshLayout mSwipeLayout;

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.com_bihe0832_shakeba_webview_fragment, container, false);
    }

    protected abstract String getLoadUrl();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.clearHistory();
        mWebView.destroy();
        mWebView = null;
    }

    @Override
    protected void initView() {
        mProgressBar = (ProgressBar) getContentView().findViewById(R.id.progressbar);
        mWebView = (WebView) getContentView().findViewById(R.id.webView);

        initWebViewSettings();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mProgressBar.setMax(100);

        mWebView.loadUrl(getLoadUrl());

        mSwipeLayout = customFindViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                resetCacheType();
                //重新刷新页面
                mWebView.loadUrl(mWebView.getUrl());
            }
        });
    }

    private void initWebViewSettings() {
        //屏蔽掉长按事件
        mWebView.requestFocusFromTouch();
        //增加webview的调试
        if(Build.VERSION.SDK_INT > 19 ){
            if(Shakeba.getInstance().isDebug()){
                mWebView.setWebContentsDebuggingEnabled(true);
            }else{
                mWebView.setWebContentsDebuggingEnabled(false);
            }
        }

        WebSettings webSettings = mWebView.getSettings();
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
        //设置缓存位置
        String cacheDirPath = Shakeba.getInstance().getApplicationContext().getFilesDir().getAbsolutePath()+ APP_CACAHE_DIRNAME;
        Logger.d("cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
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

    public boolean canGoBack() {
        return mWebView != null && mWebView.canGoBack();
    }

    public void goBack() {
        if (mWebView != null) {
            mWebView.goBack();
        }
    }

    private void resetCacheType() {
        //设置缓存类型
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }
    //WebViewClient就是帮助WebView处理各种通知、请求事件的。
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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

        @Override
        public void onCloseWindow(WebView window) {
        }

        //处理confirm弹出框
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult
                result) {
            Logger.e(  "onJsPrompt " + url);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        //处理prompt弹出框
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Logger.e("onJsConfirm " + message);
            return super.onJsConfirm(view, url, message, result);
        }
    }
}
