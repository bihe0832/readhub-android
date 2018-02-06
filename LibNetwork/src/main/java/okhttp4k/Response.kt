package okhttp4k

import okhttp3.Headers
import okhttp4k.converter.Converter

/**
 * Created by enzowei on 2017/12/7.
 */
class Response<T> {
  val statusCode: Int
  val protocol: String
  val message: String
  val headers: Map<String, String>
  val contentType: String
  val contentLength: Long
  var body: T? = null
  var errorBody: String? = null

  private constructor(rawResponse: okhttp3.Response) {
    statusCode = rawResponse.code()
    protocol = rawResponse.protocol().toString()
    message = rawResponse.message()
    headers = parseHeaders(rawResponse.headers())
    val rawBody = rawResponse.body()
    if (rawBody == null) {
      contentType = ""
      contentLength = 0
    } else {
      contentType = rawBody.contentType().toString()
      contentLength = rawBody.contentLength()
    }
  }

  fun body():T? = body

  fun isSuccessful(): Boolean = statusCode in 200..299

  private fun parseHeaders(headers: Headers): Map<String, String> =
      (0 until headers.size()).map {
        val name = headers.name(it)
        name to (headers.get(name) ?: "")
      }.toMap()

  companion object {
    fun <T> buildWith(rawResponse: okhttp3.Response): Response<T> = Response(rawResponse)
  }
}