package com.bihe0832.readhub.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.bihe0832.readhub.R;
import com.ottd.libs.framework.OttdFramework;
import com.ottd.libs.logger.OttdLog;
import com.ottd.libs.utils.TextUtils;

import me.yokeyword.fragmentation.SupportActivity;

public class WebviewActivity extends SupportActivity {

    public static final String INTENT_EXTRA_KEY_WEBVIEW_ITEM_URI = WebviewActivity.class.getName() +"URL";
    public static final String INTENT_EXTRA_KEY_WEBVIEW_ITEM_TITLE = WebviewActivity.class.getName() +"TITLE";

    //标题栏
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_bihe0832_web_activity);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        String url = "";
        String titleName = "";
        if(intent.hasExtra(INTENT_EXTRA_KEY_WEBVIEW_ITEM_URI)
                && intent.hasExtra(INTENT_EXTRA_KEY_WEBVIEW_ITEM_TITLE)) {
            url = intent.getStringExtra(INTENT_EXTRA_KEY_WEBVIEW_ITEM_URI);
            titleName = intent.getStringExtra(INTENT_EXTRA_KEY_WEBVIEW_ITEM_TITLE);
        }else{
            OttdLog.d("handle intent, but extra is bad");
            finish();
            return;
        }

        if(TextUtils.ckIsEmpty(url) || TextUtils.ckIsEmpty(titleName)){
            OttdLog.d("handle intent, extra is good, but value is bad");
            finish();
            return;
        }else{
            if (findFragment(WebViewFragment.class) == null) {
                loadRootFragment(R.id.fragment_content, WebViewFragment.newInstance(Uri.decode(url),titleName));
            }else{
                start(WebViewFragment.newInstance(url,titleName));
            }
            initToolbar(titleName);
        }
    }

    private void initToolbar(String titleName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        mToolbar = (Toolbar) findViewById(R.id.app_webview_toolbar);
        mToolbar.setTitle(titleName);//设置主标题
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        this.finish();
    }


    public static void openNewWeb(String title, String url) {
        Intent intent = new Intent(OttdFramework.getInstance().getApplicationContext(),WebviewActivity.class);
        intent.putExtra(WebviewActivity.INTENT_EXTRA_KEY_WEBVIEW_ITEM_URI, url);
        intent.putExtra(WebviewActivity.INTENT_EXTRA_KEY_WEBVIEW_ITEM_TITLE, title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        OttdFramework.getInstance().getApplicationContext().startActivity(intent);
    }
}