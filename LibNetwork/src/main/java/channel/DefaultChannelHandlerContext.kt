package channel

/**
 * 参考netty设计的channel+pipeline
 * Created by enzowei on 2017/6/28.
 */

class DefaultChannelHandlerContext constructor(pipeline: DefaultChannelPipeline, private val handler: ChannelHandler) :
    AbstractChannelHandlerContext(pipeline, handler.isInbound(), handler.isOutbound()) {

  override fun handler(): ChannelHandler = handler

  companion object {
    private fun ChannelHandler.isInbound(): Boolean = this is ChannelInboundHandler<*>

    private fun ChannelHandler.isOutbound(): Boolean = this is ChannelOutboundHandler<*>
  }
}
