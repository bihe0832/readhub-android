package channel

/**
 * 参考netty设计的channel+pipeline
 * Created by enzowei on 2017/6/28.
 */
interface Channel {
  fun pipeline(): ChannelPipeline
  fun unsafe(): unsafe
  fun write(seqId: Int, msg: Any)

  fun cancel(seqId: Int)

  interface unsafe {
    fun internalWrite(seqId: Int, content: Any)
  }
}
