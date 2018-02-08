package com.bihe0832.readhub.test

import android.content.Intent
import android.os.Bundle


import com.bihe0832.readhub.R

import me.yokeyword.fragmentation.SupportActivity

class TestActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.com_bihe0832_test_main_activity)
        //        JYGame.getInstance().onCreate(this);
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.fragment_content, MainFragment.newInstance(MainFragment.DEFAULT_TAB))
        }

    }
}