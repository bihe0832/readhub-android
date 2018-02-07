package com.bihe0832.readhub.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bihe0832.readhub.R;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_bihe0832_readhub_main_activity);
        handleIntent(getIntent());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    private void handleIntent(Intent intent){
//        String tab = "";
//        if(intent.hasExtra(MainFragment.INTENT_KEY_TAB)) {
//            tab = intent.getStringExtra(MainFragment.INTENT_KEY_TAB);
//        }else{
//            JYLog.d("handle intent, but extra is bad");
//        }
//        JYLog.d("handle intent:" + tab);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.app_main_content, MainFragment.newInstance());
        }else{
            start(MainFragment.newInstance());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }
}
