package channel

/**
 * 参考netty设计的channel+pipeline
 *
 * channel handler上下文
 * Created by enzowei on 2017/6/28.
 */

abstract class AbstractChannelHandlerContext constructor(private val pipeline: ChannelPipeline, private val inbound: Boolean, private val outbound: Boolean) : ChannelHandlerContext {
  @Volatile
  lateinit var next: AbstractChannelHandlerContext
  @Volatile
  lateinit var prev: AbstractChannelHandlerContext

  override fun write(seqId: Int, msg: Any) {
    val next = this.findContextOutbound()
    if (next !== this) {
      next.invokeWrite(seqId, msg)
    }

  }

  private fun invokeWrite(seqId: Int, msg: Any) {
    try {
      (handler() as ChannelOutboundHandler<Any>).write(this, seqId, msg)
    } catch (e: Exception) {
      invokeExceptionCaught(seqId, handler().getName(), e)
    }

  }

  override fun read(seqId: Int, msg: Any) {
    val prev = this.findContextInbound()
    if (prev !== this) {
      prev.invokeRead(seqId, msg)
    }
  }

  private fun invokeRead(seqId: Int, msg: Any) {
    try {
      (handler() as ChannelInboundHandler<Any>).read(this, seqId, msg)
    } catch (e: Exception) {
      invokeExceptionCaught(seqId, handler().getName(), e)
    }

  }

  override fun exceptionCaught(seqId: Int, exceptionMaker: String, cause: Throwable) {
    prev.invokeExceptionCaught(seqId, exceptionMaker, cause)
  }

  //异常发生在outbound直接跳过后面的outbound handle，跳到队尾tail再通过inbound级往后传递，如果是发生在inbound，则直接逐级往后传递
  /**
   *
   * @param seqId 发生异常的seqId
   * @param exceptionMaker 异常发生的具体handler name
   * @param cause 捕获的异常
   */
  private fun invokeExceptionCaught(seqId: Int, exceptionMaker: String, cause: Throwable) {
    try {
      if (inbound) {
        (handler() as ChannelInboundHandler<*>).exceptionCaught(this, seqId, exceptionMaker, cause)
      } else if (outbound) {
        pipeline.fireExceptionCaught(seqId, exceptionMaker, cause)
      }
    } catch (error: Throwable) {
      error.printStackTrace()
    }

  }

  private fun findContextInbound(): AbstractChannelHandlerContext {
    var ctx = this

    do {
      ctx = ctx.prev
    } while (!ctx.inbound)

    return ctx
  }

  private fun findContextOutbound(): AbstractChannelHandlerContext {
    var ctx = this

    do {
      ctx = ctx.next
    } while (!ctx.outbound)

    return ctx
  }
}
