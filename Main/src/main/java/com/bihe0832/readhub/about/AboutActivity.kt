package com.bihe0832.readhub.about

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.bihe0832.readhub.R
import com.bihe0832.readhub.webview.WebviewActivity
import com.ottd.libs.framework.OttdFramework
import com.ottd.libs.framework.network.ReadHubApi
import com.ottd.libs.logger.OttdLog
import com.ottd.libs.utils.APKUtils
import kotlinx.android.synthetic.main.com_bihe0832_readhub_about_activity.*
import okhttp3.MediaType
import okhttp3.RequestBody
import topic.network.enqueue


class AboutActivity : AppCompatActivity() {

    private var isClicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.com_bihe0832_readhub_about_activity)
        titleBar.title = resources.getString(R.string.app_name)//设置主标题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        setSupportActionBar(titleBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        titleBar.setNavigationOnClickListener { finish() }

        aboutReadhub.text = "Readhub V" + APKUtils.getAppVersionName(this) + "." + APKUtils.getAppVersionCode(this)

        aboutReadhubFunc.setOnClickListener{
            if(!isClicked){
                isClicked = true
                WebviewActivity.openNewWeb(getString(R.string.menu_key_about_func), getString(R.string.link_version))
            }
        }

        aboutReadhubUpdate.setOnClickListener{
            OttdFramework.getInstance().checkUpdate(this,false)
        }

        aboutReadhubShare.setOnClickListener{
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            sendIntent.type = "text/plain"
            OttdFramework.getInstance().applicationContext.startActivity(sendIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        isClicked = false
    }


}

