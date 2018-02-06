package com.ottd.libs.framework.fragment;


import me.yokeyword.fragmentation.SupportFragment;

import com.ottd.libs.framework.R;
import com.ottd.libs.ui.ToastUtil;


/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
public abstract class BaseMainFragment extends SupportFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtil.showShort(getContext(),getContext().getString(R.string.press_again_exit));
        }
        return true;
    }
}
