package com.bihe0832.readhub.me

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bihe0832.readhub.ConfigProxy
import com.bihe0832.readhub.R
import com.bihe0832.readhub.topic.CONFIG_KEY_NEWS_HAS_EN
import com.bihe0832.readhub.topic.CONFIG_KEY_TOPIC_VIEW_TYPE
import com.bihe0832.readhub.topic.TOPIC_VIEW_TYPE_LIST
import com.bihe0832.readhub.topic.TOPIC_VIEW_TYPE_SUMMARY
import com.ottd.libs.framework.fragment.BaseBackFragment
import com.bihe0832.readhub.webview.WebviewActivity
import com.ottd.libs.config.Config
import com.ottd.libs.framework.OttdFramework
import com.ottd.libs.ui.ToastUtil
import kotlinx.android.synthetic.main.fragment_me.*


class MyFragment : BaseBackFragment() {

    private val topicType by ConfigProxy(CONFIG_KEY_TOPIC_VIEW_TYPE, TOPIC_VIEW_TYPE_LIST)
    private val newsHasEn by ConfigProxy(CONFIG_KEY_NEWS_HAS_EN, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_me, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        aboutReadhubCompany.setOnClickListener {
            WebviewActivity.openNewWeb(getString(R.string.menu_key_readhub_company), getString(R.string.link_readhub_desc))
        }

        aboutReadhubWeb.setOnClickListener {
            WebviewActivity.openNewWeb(getString(R.string.menu_key_readhub_web), getString(R.string.link_readhub_page))
        }


        aboutAppTopicViewTypeSwitch.apply {
            fun onTrue() {
                aboutAppTopicViewTypeText.text = "摘要"
                Config.setCloudConfig(CONFIG_KEY_TOPIC_VIEW_TYPE, "" + TOPIC_VIEW_TYPE_SUMMARY)
            }
            fun onFalse() {
                aboutAppTopicViewTypeText.text = "列表"
                Config.setCloudConfig(CONFIG_KEY_TOPIC_VIEW_TYPE, "" + TOPIC_VIEW_TYPE_LIST)
            }

            setOnClickListener {
                isChecked = !isChecked
                updateContentWithSwitch(isChecked, ::onTrue, ::onFalse)
                ToastUtil.showLong(context,"设置成功，下次应用启动时生效~")
            }
            isChecked = topicType == TOPIC_VIEW_TYPE_SUMMARY
            updateContentWithSwitch(isChecked, ::onTrue, ::onFalse)
        }

        aboutAppNewsWithENSwitch.apply {
            fun onTrue() {
                aboutAppNewsWithENText.text = "展示"
                Config.setCloudConfig(CONFIG_KEY_NEWS_HAS_EN, Config.VALUE_SWITCH_ON)
            }
            fun onFalse() {
                aboutAppNewsWithENText.text = "不展示"
                Config.setCloudConfig(CONFIG_KEY_NEWS_HAS_EN, Config.VALUE_SWITCH_OFF)
            }

            setOnClickListener {
                isChecked = !isChecked
                updateContentWithSwitch(isChecked, ::onTrue, ::onFalse)
                ToastUtil.showLong(context,"设置成功，下次应用启动时生效~")
            }
            isChecked = newsHasEn
            updateContentWithSwitch(isChecked, ::onTrue, ::onFalse)
        }

        aboutAppMySub.setOnClickListener {
            //TODO hardy 我的关注
            OttdFramework.getInstance().showWaitting()
        }

        aboutAppUpdate.setOnClickListener {
            //TODO hardy 我的关注
            OttdFramework.getInstance().showWaitting()
        }

        aboutDevInfo.setOnClickListener {
            WebviewActivity.openNewWeb(getString(R.string.menu_key_dev_info), getString(R.string.link_author))
        }
    }

    private fun updateContentWithSwitch(predicate: Boolean, onTrue: () -> Unit, onFalse: () -> Unit) {
        when (predicate) {
            true -> onTrue()
            else ->  onFalse()
        }
    }

    companion object {
        val TAG = "MeFragment"
        @JvmStatic
        fun newInstance(): MyFragment = MyFragment()
    }
}