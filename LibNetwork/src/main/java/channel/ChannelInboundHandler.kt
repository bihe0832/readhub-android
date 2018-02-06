package channel

/**
 * 参考netty设计的channel+pipeline
 * 处理通过channel write的流的handler接口
 * Created by enzowei on 2017/6/28.
 */
interface ChannelInboundHandler<in T> : ChannelHandler {
  @Throws(Exception::class)
  fun read(ctx: ChannelHandlerContext, seqId: Int, msg: T)

  @Throws(Exception::class)
  fun exceptionCaught(ctx: ChannelHandlerContext, seqId: Int, exceptionMaker: String, cause: Throwable)
}
