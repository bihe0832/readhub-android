package channel

/**
 * 参考netty设计的channel+pipeline
 * 处理通过channel read的流的handler接口
 * Created by enzowei on 2017/6/28.
 */
interface ChannelOutboundHandler<in T> : ChannelHandler {
  @Throws(Exception::class)
  fun write(ctx: ChannelHandlerContext, seqId: Int, msg: T)
}
