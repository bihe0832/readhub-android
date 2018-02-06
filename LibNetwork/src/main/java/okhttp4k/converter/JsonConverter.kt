package okhttp4k.converter

import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by enzowei on 2017/12/6.
 */
class JsonConverter : Converter<ResponseBody, JSONObject> {
  override fun convert(value: ResponseBody): JSONObject? {
    return try {
      value.use {
        JSONObject(it.string())
      }
    } catch (e: JSONException) {
      e.printStackTrace()
      null
    }
  }
}