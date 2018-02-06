package com.ottd.libs.framework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;


import com.ottd.libs.framework.R;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by YoKeyword on 16/2/7.
 */
public class BaseBackFragment extends SwipeBackFragment {
    private static final String TAG = "Fragmentation";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setParallaxOffset(0.5f);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        StatHelper.reportFragmentShow(this.getClass().getName());
    }
}
