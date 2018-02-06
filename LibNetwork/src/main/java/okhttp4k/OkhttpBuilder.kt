package okhttp4k

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp4k.ssl.SSLContextBuilder
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Created by enzowei on 2017/12/15.
 */
@HttpDslMarker
class OkhttpBuilder(private val okHttpClient: OkHttpClient) {
  private val builder by lazy { okHttpClient.newBuilder() }

  /**
   * Sets the default connect timeout for new connections. A value of 0 means no timeout,
   * otherwise values must be between 1 and {@link Integer#MAX_VALUE} when converted to
   * milliseconds.
   */
  var connectTimeout: Long
    get() = okHttpClient.connectTimeoutMillis().toLong()
    set(value) {
      builder.connectTimeout(value, TimeUnit.MILLISECONDS)
    }

  /**
   * Sets the default read timeout for new connections. A value of 0 means no timeout, otherwise
   * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
   */
  var readTimeout: Long
    get() = okHttpClient.readTimeoutMillis().toLong()
    set(value) {
      builder.readTimeout(value, TimeUnit.MILLISECONDS)
    }

  /**
   * Sets the default write timeout for new connections. A value of 0 means no timeout, otherwise
   * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
   */
  @Suppress("unused")
  var writeTimeout: Long
    get() = okHttpClient.writeTimeoutMillis().toLong()
    set(value) {
      builder.writeTimeout(value, TimeUnit.MILLISECONDS)
    }

  /** Sets the response cache to be used to read and write cached responses. */
  @Suppress("unused")
  fun cache(cache: () -> Cache) {
    builder.cache(cache())
  }

  fun networkInterceptor(networkInterceptor: () -> Interceptor) {
    builder.addNetworkInterceptor(networkInterceptor())
  }

  fun interceptor(interceptor: () -> Interceptor) {
    builder.addInterceptor(interceptor())
  }

  @Suppress("unused")
  fun sslSocketFactory(protocol: String = SSLContextBuilder.defaultTLSProtocol, config: SSLContextBuilder.() -> Unit) {
    val (context, trustManager) = SSLContextBuilder().apply(config).createSSLContext(protocol)
    builder.sslSocketFactory(context.socketFactory, trustManager)
  }

  /**
   * Verify that the host name is an acceptable match with
   * the server's authentication scheme.
   *
   * @param hostname the host name
   * @param session SSLSession used on the connection to host
   * @return true if the host name is acceptable
   */
  @Suppress("unused")
  fun hostnameVerifier(verify: (hostname: String, session: SSLSession) -> Boolean) {
    builder.hostnameVerifier(HostnameVerifier(verify))
  }

  internal fun build(): OkHttpClient {
    return builder.build()
  }
}

@DslMarker
annotation class HttpDslMarker

