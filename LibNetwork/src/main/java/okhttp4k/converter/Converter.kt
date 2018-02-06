package okhttp4k.converter

/**
 * Created by enzowei on 2017/12/6.
 */
interface Converter<in F, out T> {
  fun convert(value: F): T?
}