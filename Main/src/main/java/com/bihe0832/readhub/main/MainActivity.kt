package com.bihe0832.readhub.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.bihe0832.base.permission.PermissionsActivity
import com.bihe0832.base.permission.PermissionsChecker
import com.bihe0832.readhub.R
import com.ottd.libs.framework.OttdFramework
import kotlinx.android.synthetic.main.com_bihe0832_readhub_main_activity.*
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator


class MainActivity : SupportActivity() {

    private val REQUEST_CODE = 0 // 请求码
    // 所需的全部权限
    val PERMISSIONS = arrayOf<String>(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var mPermissionsChecker: PermissionsChecker? = null // 权限检测器

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.com_bihe0832_readhub_main_activity)
        handleIntent(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        my_toolbar.title = ""
        setSupportActionBar(my_toolbar)
        mPermissionsChecker =  PermissionsChecker(this)

        OttdFramework.getInstance().checkUpdate(this, true)
    }

    private fun handleIntent(intent: Intent) {
        //        String tab = "";
        //        if(intent.hasExtra(MainFragment.INTENT_KEY_TAB)) {
        //            tab = intent.getStringExtra(MainFragment.INTENT_KEY_TAB);
        //        }else{
        //            JYLog.d("handle intent, but extra is bad");
        //        }
        //        JYLog.d("handle intent:" + tab);
        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.app_main_content, MainFragment.newInstance())
        } else {
            start(MainFragment.newInstance())
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport()
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    override fun onResume() {
        super.onResume()
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker!!.lacksPermissions(permissions = *PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, permissions = *PERMISSIONS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish()
        }
    }
}
