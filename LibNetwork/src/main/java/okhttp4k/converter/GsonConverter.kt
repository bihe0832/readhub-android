package okhttp4k.converter

import com.google.gson.Gson
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by enzowei on 2017/12/6.
 */
class GsonConverter<out T>(clazz: Class<T>) : Converter<ResponseBody, T> {
  private val gson by lazy { Gson() }
  private val adapter by lazy { gson.getAdapter<T>(clazz) }
  override fun convert(value: ResponseBody): T? =
      try {
        value.use {
          val jsonReader = gson.newJsonReader(it.charStream())
          adapter.read(jsonReader)
        }
      } catch (e: IOException) {
        null
      }

}