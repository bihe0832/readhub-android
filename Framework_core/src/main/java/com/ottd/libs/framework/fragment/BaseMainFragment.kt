package com.ottd.libs.framework.fragment


import com.ottd.libs.framework.R
import com.ottd.libs.ui.ToastUtil
import me.yokeyword.fragmentation.SupportFragment


/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
abstract class BaseMainFragment : SupportFragment() {
    private var TOUCH_TIME: Long = 0

    /**
     * 处理回退事件
     *
     * @return
     */
    override fun onBackPressedSupport(): Boolean {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish()
        } else {
            TOUCH_TIME = System.currentTimeMillis()
            ToastUtil.showShort(context, context.getString(R.string.press_again_exit))
        }
        return true
    }

    companion object {
        // 再点一次退出程序时间设置
        private val WAIT_TIME = 2000L
    }
}
