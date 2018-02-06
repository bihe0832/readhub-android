package okhttp4k

import okhttp3.OkHttpClient


/**
 * OkHttpClientManager
 * Created by enzowei on 17/11/14.
 */
class Http {

  private var okHttpClientEither: Either<Any, Any> = Left(OkHttpClient())

  /**
   *  Init okhttp client
   *
   */
  @Suppress("unused")
  fun init(config: OkhttpBuilder.() -> Unit): Http {
    val builder = OkhttpBuilder(okHttpClientEither.fold(this::getOKHttpClient))
    builder.config()
    okHttpClientEither = Right(builder.build())
    return this
  }


  /**
   *  Http get
   *
   */
  @Suppress("unused")
  fun <T> get(config: Request<T>.() -> Unit): Any =
      with(Request<T>(okHttpClientEither.fold(this::getOKHttpClient))) {
        config()
        return get()
      }

  /**
   *  Http post
   *
   */
  @Suppress("unused")
  fun <T> post(config: Request<T>.() -> Unit): Any =
      with(Request<T>(okHttpClientEither.fold(this::getOKHttpClient))) {
        config()
        return post()
      }


  /**
   * Cancel a request by tag
   * @param tag tag
   */
  @Suppress("unused")
  fun cancel(tag: Any) =
      with(okHttpClientEither.fold(this::getOKHttpClient).dispatcher()) {
        (runningCalls() + queuedCalls())
            .filter { tag == it.request().tag() }
            .forEach { it.cancel() }
      }

  private fun getOKHttpClient(obj: Any): OkHttpClient = obj as? OkHttpClient ?: OkHttpClient()
}





