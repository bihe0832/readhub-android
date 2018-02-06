package topic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by enzowei on 2018/2/5.
 */
class RetrofitCallbackBuilder<T> {

  var onResponse: ((call: Call<T>?, response: Response<T>?) -> Unit)? = null

  var onFailure: ((call: Call<T>?, t: Throwable?) -> Unit)? = null

  fun onResponse(onResponse: (call: Call<T>?, response: Response<T>?) -> Unit) {
    this.onResponse = onResponse
  }

  fun onFailure(onFailure: (call: Call<T>?, t: Throwable?) -> Unit) {
    this.onFailure = onFailure
  }
}

fun <T> Call<T>.enqueue(build: RetrofitCallbackBuilder<T>.() -> Unit) {
  val builder = RetrofitCallbackBuilder<T>().apply(build)
  this.enqueue(object : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
      builder.onFailure?.invoke(call, t)
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
      builder.onResponse?.invoke(call, response)
    }

  })
}