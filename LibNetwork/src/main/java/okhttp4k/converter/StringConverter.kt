package okhttp4k.converter

import okhttp3.ResponseBody

/**
 * Created by enzowei on 2017/12/6.
 */
class StringConverter : Converter<ResponseBody, String> {
  override fun convert(value: ResponseBody): String = value.use { it.string() }
}