package com.ottd.libs.framework.fragment


import android.os.Bundle
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment

/**
 * Created by YoKeyword on 16/2/7.
 */
open class BaseBackFragment : SwipeBackFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setParallaxOffset(0.5f)
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        //        StatHelper.reportFragmentShow(this.getClass().getName());
    }

    companion object {
        private val TAG = "Fragmentation"
    }
}
