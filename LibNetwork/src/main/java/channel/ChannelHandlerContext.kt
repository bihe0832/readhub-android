package channel

/**
 * 参考netty设计的channel+pipeline
 * Created by enzowei on 2017/6/28.
 */

interface ChannelHandlerContext {
  fun handler(): ChannelHandler
  fun write(seqId: Int, msg: Any)
  fun read(seqId: Int, msg: Any)
  fun exceptionCaught(seqId: Int, exceptionMaker: String, cause: Throwable)
}
