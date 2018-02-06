package channel

/**
 * 参考netty设计的channel+pipeline
 * Created by enzowei on 2017/6/28.
 */

interface ChannelPipeline {
  fun channel(): Channel

  fun addFirst(handler: ChannelHandler): ChannelPipeline
  fun addLast(handler: ChannelHandler): ChannelPipeline

  fun write(seqId: Int, msg: Any)

  fun read(seqId: Int, msg: Any)

  fun cancel(seqId: Int)

  fun fireExceptionCaught(seqId: Int, exceptionMaker: String, cause: Throwable)
}
