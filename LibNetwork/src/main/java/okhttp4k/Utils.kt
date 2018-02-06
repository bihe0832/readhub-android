package okhttp4k

import android.os.Handler
import org.json.JSONObject


fun <T> T.postOn(handler: Handler? = null, post: (T) -> Unit) {
  when (handler) {
    null -> post(this)
    else -> handler.post { post(this) }
  }
}

fun Map<String, String>.toJsonString(): String {
  return JSONObject(this).toString()
}