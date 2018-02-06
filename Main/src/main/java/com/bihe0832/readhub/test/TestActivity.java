package com.bihe0832.readhub.test;

import android.content.Intent;
import android.os.Bundle;

import com.bihe0832.readhub.app.test.R;

import me.yokeyword.fragmentation.SupportActivity;

public class TestActivity extends SupportActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_bihe0832_test_main_activity);
//        JYGame.getInstance().onCreate(this);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent){
        int tab = MainFragment.DEFAULT_TAB;

        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fragment_content, MainFragment.newInstance(tab));
        }

    }
}