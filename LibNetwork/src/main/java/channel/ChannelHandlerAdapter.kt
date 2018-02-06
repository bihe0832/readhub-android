package channel

/**
 * 参考netty设计的channel+pipeline
 * Created by enzowei on 2017/6/28.
 */

abstract class ChannelHandlerAdapter : ChannelHandler {
  var added: Boolean = false

  override fun getName(): String = javaClass.name
}
