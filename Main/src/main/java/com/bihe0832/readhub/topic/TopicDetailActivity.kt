package com.bihe0832.readhub.topic

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import com.bihe0832.readhub.R
import com.bihe0832.readhub.topic.viewmodel.TopicViewModel
import com.bihe0832.readhub.webview.WebviewActivity
import com.ottd.libs.framework.OttdFramework
import com.ottd.libs.framework.model.News
import com.ottd.libs.framework.model.Topic
import com.ottd.libs.framework.utils.SimpleUtils
import com.ottd.libs.framework.utils.getReadhubTimeStamp
import com.ottd.libs.framework.utils.startWith
import com.ottd.libs.logger.OttdLog
import com.ottd.libs.ui.CommonRecyclerAdapter
import com.ottd.libs.utils.device.ExternalStorage
import kotlinx.android.synthetic.main.activity_topic_detail.*
import kotlinx.android.synthetic.main.activity_topic_detail_news_item.view.*
import kotlinx.android.synthetic.main.activity_topic_detail_timeline_item.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

const val INTENT_EXTRA_KEY_TOPIC_ID = "topic_id"
const val INTENT_EXTRA_KEY_TOPIC_TITLE = "topic_title"

class TopicDetailActivity : AppCompatActivity() {
    private val viewModel: TopicViewModel by lazy { ViewModelProviders.of(this).get(TopicViewModel::class.java) }
    private var newsListData: List<News> by Delegates.observable(emptyList()) { prop, old, new ->
        //    autoNotify(old, new) { o, n -> o.id == n.id }
        newsList.adapter.notifyDataSetChanged()
    }

    private var topicList: List<Topic> by Delegates.observable(emptyList()) { prop, old, new ->
        //    autoNotify(old, new) { o, n -> o.id == n.id }
        timelineList.adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.com_tencent_jygame_app_share_toolbar, menu)
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra(INTENT_EXTRA_KEY_TOPIC_ID)
        val topicTitle = intent.getStringExtra(INTENT_EXTRA_KEY_TOPIC_TITLE)
        if (id.isNullOrBlank()) {
            finish()
        }
        setContentView(R.layout.activity_topic_detail)

//        titleBar.title = when (topicTitle) {
//            null -> getString(R.string.app_name)
//            else -> topicTitle
//        }
        titleBar.title = ""
        setSupportActionBar(titleBar)
        titleBar.apply {
            setNavigationOnClickListener {
                finish()
            }
            setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
                if (item.itemId == R.id.share) {
                    shareTopic(id)
                }
                false
            })
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        timelineList.apply {
            adapter = CommonRecyclerAdapter<Topic> {
                onLayout { _ -> R.layout.activity_topic_detail_timeline_item }
                onCount { topicList.size }
                onItem { position -> topicList[position] }
                onBind { topic ->
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val dateFormat = format.format(topic.createdAt.getReadhubTimeStamp())
                    val tempCalendar = Calendar.getInstance()
                    tempCalendar.time = format.parse(dateFormat)
                    date.text =
                            when (tempCalendar.get(Calendar.MONTH) > 8) {
                                true -> "" + (tempCalendar.get(Calendar.MONTH) + 1)
                                else -> "0" + (tempCalendar.get(Calendar.MONTH) + 1)
                            } + "." +
                                    when (tempCalendar.get(Calendar.DAY_OF_MONTH) > 9) {
                                        true -> "" + (tempCalendar.get(Calendar.DAY_OF_MONTH))
                                        else -> "0" + (tempCalendar.get(Calendar.DAY_OF_MONTH))
                                    }
                    year.text = "" + tempCalendar.get(Calendar.YEAR)
                    timelineTitle.text = topic.title
                    if (id != topic.id) {
                        timelineTitle.setOnClickListener {
                            finish()
                            Intent().apply {
                                setClass(OttdFramework.getInstance().applicationContext, TopicDetailActivity::class.java)
                                putExtra(INTENT_EXTRA_KEY_TOPIC_ID, topic.id)
                                putExtra(INTENT_EXTRA_KEY_TOPIC_TITLE, topic.title)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }.startWith(OttdFramework.getInstance().applicationContext)
                        }
                    } else {
                        timelineTitle.setTextColor(resources.getColor(R.color.secondary_text))
                    }


                    when (topicList.indexOf(topic)) {
                        0 -> timelineTopLine.visibility = View.GONE
                        topicList.size - 1 -> timelineBottomLine.visibility = View.GONE
                    }
                }
            }
            layoutManager = LinearLayoutManager(this@TopicDetailActivity)
        }

        newsList.apply {
            adapter = CommonRecyclerAdapter<News> {
                onLayout { _ -> R.layout.activity_topic_detail_news_item }
                onCount { newsListData.size }
                onItem { position -> newsListData[position] }
                onBind { news ->
                    newsTitle.text = news.title
                    newsFrom.text = news.siteName
                    setOnClickListener {
                        WebviewActivity.openNewWeb(context.resources.getString(R.string.app_name),
                                news.mobileUrl ?: news.url)
                    }
                }
            }
            layoutManager = LinearLayoutManager(this@TopicDetailActivity)
        }

        viewModel.topicDetail.observe(::getLifecycle) { detail ->
            detail?.let {
                Log.d(TopicListFragment.TAG, "observe:${it.id}")
                detailTitleView.text = it.title
                detailSummaryView.text = it.summary
                if (it.timeline == null || it.timeline?.topics?.size == 0) {
                    timelineDsc.visibility = View.GONE
                    timelineList.visibility = View.GONE
                } else {
                    it.timeline?.let {
                        timelineDsc.visibility = View.VISIBLE
                        timelineList.visibility = View.VISIBLE
                        topicList = it.topics
                    }
                }
                if (it.newsArray == null || it.newsArray?.size == 0) {
                    newsDsc.visibility = View.GONE
                    newsList.visibility = View.GONE
                } else {
                    it.newsArray?.let {
                        newsDsc.visibility = View.VISIBLE
                        newsList.visibility = View.VISIBLE
                        newsListData = it
                    }
                }
                timelineList.invalidate()
                newsList.invalidate()
                mainLayout.invalidate()
            }
        }
        viewModel.getTopicDetail(id)
    }

    private fun shareTopic(id: String){
        var outStream: FileOutputStream? = null
        val file = File(ExternalStorage.getCommonRootDir(applicationContext) + "/" + id + ".jpg")
        if (!file.isDirectory()) {//如果是目录不允许保存
            try {
                outStream = FileOutputStream(file)
                val topicBitmap = SimpleUtils.shotScrollView(topicInfoScrollView)
//                val headerBitmap = BitmapFactory.decodeResource(resources, R.drawable.share_header)
//                val footerBitmap = BitmapFactory.decodeResource(resources, R.drawable.share_footer)
                val headerBitmap = SimpleUtils.getHeader()
                val footerBitmap = SimpleUtils.getFooter(this,id)
                var tempBitmap = SimpleUtils.mergeBitmap_TB(headerBitmap,topicBitmap,false)
                tempBitmap = SimpleUtils.mergeBitmap_TB(tempBitmap,footerBitmap,false)
                tempBitmap.compress(Bitmap.CompressFormat.JPEG, 35, outStream)
                outStream!!.flush()
                tempBitmap.recycle()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (outStream != null) {
                        outStream.close()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if (file.exists()) {
            OttdLog.d("test", file.length())
            val intent = Intent(Intent.ACTION_VIEW)
            val photoURI = FileProvider.getUriForFile(applicationContext,
                    applicationContext.packageName + ".provider",
                    file)
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM, photoURI)
            intent.type = "image/*"
            startActivity(Intent.createChooser(intent, "分享到"))
        }
    }
}