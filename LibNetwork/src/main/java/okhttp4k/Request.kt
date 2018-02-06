package okhttp4k

import android.os.Handler
import okhttp4k.converter.ByteArrayConverter
import okhttp4k.converter.Converter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by enzowei on 2017/11/14.
 */
class Request<T>(private val okHttpClient: OkHttpClient) {
  var tag: Any = hashCode()
  var url: String? = null
  var contentType: String? = null
  var converter: Converter<ResponseBody, *> = ByteArrayConverter()
  var observeHandler: Handler? = null
  var async: Boolean = true
  var json: Boolean = false
  var rawBody: ByteArray? = null

  private val params by lazy { mutableMapOf<String, String>() }
  private val headers by lazy { mutableMapOf<String, String>() }
  private var callResponse: (response: Response<T>) -> Unit = {}
  private var callSuccess: (T) -> Unit = {}
  private var callFailure: (e: Throwable?, errorMsg: String) -> Unit = { _, _ -> }

  internal fun request(method: String, async: Boolean): Any {
    val call = makeCall(method)
    return if (async) {
      enqueue(call)
    } else {
      execute(call)
    }
  }

  @Suppress("unused")
  internal fun get(): Any = request("GET", async)

  @Suppress("unused")
  internal fun post(): Any = request("POST", async)

  @Suppress("unused")
  fun params(makePairs: RequestPairs.() -> Unit) = params.fromPairs(makePairs)

  @Suppress("unused")
  fun headers(makePairs: RequestPairs.() -> Unit) = headers.fromPairs(makePairs)

  @Suppress("unused")
  fun onResponse(onResponse: (response: Response<T>) -> Unit) {
    callResponse = onResponse
  }

  @Suppress("unused")
  fun onSuccess(onSuccess: (T) -> Unit) {
    callSuccess = onSuccess
  }

  @Suppress("unused")
  fun onFailure(onFailure: (e: Throwable?, errorMsg: String) -> Unit) {
    callFailure = onFailure
  }

  private fun makeCall(method: String): Call {
    if (url.isNullOrBlank()) {
      throw IllegalArgumentException("url can not be null !")
    }

    var body: RequestBody? = null
    if (params.isNotEmpty()) {
      when (method) {
        "GET", "DELETE", "HEAD" -> {
          url = url?.appendParams(params)
        }
        else -> {
          val contentType = if (json) {
            "application/json; charset=utf-8"
          } else {
            contentType ?: headers["Content-Type"]
          }

          when (contentType) {
            null, "application/x-www-form-urlencoded" -> body = FormBody.Builder().appendParams(params).build()
            "application/json; charset=utf-8" -> body = RequestBody.create(MediaType.parse(contentType), params.toJsonString())
          }

        }
      }
    } else if (rawBody != null && method == "POST") {
      if (contentType == "application/octet-stream") {
        body = RequestBody.create(MediaType.parse(contentType), rawBody)
      }
    }

    val request = okhttp3.Request.Builder()
        .tag(tag)
        .url(url)
        .appendHeaders(headers)
        .method(method, body)
        .build()
    return okHttpClient.newCall(request)
  }

  private fun enqueue(call: Call): Any {
    try {
      call.enqueue(object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {
          e?.postOn(observeHandler) {
            callFailure(e, e.localizedMessage)
          }
        }

        override fun onResponse(call: Call?, response: okhttp3.Response?) {
          try {
            val finalResponse = response?.parse()
            finalResponse?.let {
              it.postOn(observeHandler, callResponse)
              it.body?.postOn(observeHandler, callSuccess)
              it.errorBody?.postOn(observeHandler) {
                callFailure(null, it)
              }
            }
          } catch (e: Throwable) {
            e.postOn(observeHandler) {
              callFailure(e, e.localizedMessage)
            }
          }
        }
      })

    } catch (e: Throwable) {
      e.postOn(observeHandler) {
        callFailure(e, e.localizedMessage)
      }
    }
    return tag
  }

  private fun execute(call: Call): Any {
    try {
      val response = call.execute()
      val finalResponse = response?.parse()
      finalResponse?.let {
        it.postOn(observeHandler, callResponse)
        it.body?.postOn(observeHandler, callSuccess)
        it.errorBody?.postOn(observeHandler) {
          callFailure(null, it)
        }
      }
    } catch (e: Throwable) {
      e.postOn(observeHandler) {
        callFailure(e, e.localizedMessage)
      }
    }
    return tag
  }

  private fun okhttp3.Response.parse(): Response<T> {
    val rawBody = this.body()
    val code = this.code()
    //see https://tools.ietf.org/html/rfc2616#section-6.1.1
    return when {
      code < 200 || code >= 300 -> {
        Response.buildWith<T>(this).apply { errorBody = rawBody?.use { it.string() } ?: "" }
      }
      code == 204 || code == 205 -> {
        //204 No Content && 205 No Content
        rawBody?.close()
        Response.buildWith(this)
      }
      else -> {
        Response.buildWith<T>(this).apply { body = rawBody?.let(converter::convert) as? T }
      }
    }
  }

  private fun MutableMap<String, String>.fromPairs(makePairs: RequestPairs.() -> Unit) {
    RequestPairs(this).makePairs()
  }

  //append headers into builder
  private fun okhttp3.Request.Builder.appendHeaders(headers: Map<String, String>): okhttp3.Request.Builder {
    if (headers.isNotEmpty()) {
      val headerBuilder = Headers.Builder()
      headers.forEach { entry -> headerBuilder.add(entry.key, entry.value) }
      this.headers(headerBuilder.build())
    }
    return this
  }

  //append params to url
  private fun String.appendParams(params: Map<String, String>): String {
    val sb = StringBuilder().append(this)
    if (!this.contains("?")) {
      sb.append("?")
    }
    sb.append(params.map { "${it.key}=${it.value}" }.joinToString("&"))
    return sb.toString()
  }

  //append params to form builder
  private fun FormBody.Builder.appendParams(params: Map<String, String>): FormBody.Builder {
    params.forEach { entry -> this.add(entry.key, entry.value) }
    return this
  }

  class RequestPairs(private val map: MutableMap<String, String>) {
    infix fun String.to(value: String) {
      map.put(this, value)
    }

    infix fun String.to(value: Int) {
      map.put(this, "$value")
    }
  }
}

