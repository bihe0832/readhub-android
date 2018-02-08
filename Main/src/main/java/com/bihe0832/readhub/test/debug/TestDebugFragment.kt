package com.bihe0832.readhub.test.debug

import android.os.Bundle
import com.bihe0832.readhub.test.base.BaseTestFragment
import com.bihe0832.readhub.test.base.TestItem
import com.ottd.libs.framework.OttdFramework

class TestDebugFragment : BaseTestFragment() {

    override fun getDataList(): List<TestItem> =
            mutableListOf(TestItem("调试入口", { OttdFramework.getInstance().showWaitting() }))

    companion object {
        fun newInstance(): TestDebugFragment = TestDebugFragment().apply { arguments = Bundle() }
    }

}
