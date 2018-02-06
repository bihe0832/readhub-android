package okhttp4k.converter

import okhttp3.ResponseBody

/**
 * Created by enzowei on 2017/12/6.
 */
class ByteArrayConverter : Converter<ResponseBody, ByteArray> {
  override fun convert(value: ResponseBody): ByteArray = value.use { value.bytes() }
}