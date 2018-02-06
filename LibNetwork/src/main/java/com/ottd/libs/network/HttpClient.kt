package com.ottd.libs.network

import android.content.Context
import com.ottd.libs.cache.CacheManager
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import okhttp4k.Http
import java.io.File

/**
 * Created by enzowei on 2018/1/30.
 */
const val TIME_OUT_READ = 5000L
const val TIME_OUT_CONNECTION = 5000L
const val TIME_OUT_WRITE = 5000L

const val CACHE_SIZE_DISK = (1024 * 1024 * 5).toLong()
const val CACHE_SIZE_MEMORY = 1024 * 1024 * 5
object HttpClient {
  lateinit var http:Http
  fun init(context: Context, debug: Boolean = false):Http {
    CacheManager.getInstance().init(context, CACHE_SIZE_DISK, CACHE_SIZE_MEMORY, File(context.applicationContext.cacheDir, "response").absolutePath)
    http = Http().init {
      readTimeout = TIME_OUT_READ
      connectTimeout = TIME_OUT_CONNECTION
      writeTimeout = TIME_OUT_WRITE
      cache {
        val httpCacheDirectory = File(context.applicationContext.cacheDir, "responses")
        Cache(httpCacheDirectory, CACHE_SIZE_DISK)
      }
      networkInterceptor {
        HttpLoggingInterceptor().apply {
          level = if (debug) {
            HttpLoggingInterceptor.Level.BODY
          } else {
            HttpLoggingInterceptor.Level.BASIC
          }
        }
      }
    }
    return http
  }
}
