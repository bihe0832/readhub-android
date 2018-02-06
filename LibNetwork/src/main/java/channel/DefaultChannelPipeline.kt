package channel


import android.util.Log

/**
 * 参考netty设计的channel+pipeline
 * 具体的管道实现
 * Created by enzowei on 2017/6/28.
 */

class DefaultChannelPipeline(private val channel: Channel) : ChannelPipeline {
  private val head: AbstractChannelHandlerContext
  private val tail: AbstractChannelHandlerContext

  init {
    this.tail = TailContext(this)
    this.head = HeadContext(this)
    this.head.next = this.tail
    this.tail.prev = this.head
  }

  override fun channel(): Channel = this.channel


  inner class HeadContext(pipeline: DefaultChannelPipeline) : AbstractChannelHandlerContext(pipeline, true, false), ChannelInboundHandler<Any> {

    override fun handler(): ChannelHandler = this

    @Throws(Exception::class)
    override fun read(ctx: ChannelHandlerContext, seqId: Int, msg: Any) {
      Log.d("Channel", "HeadContext read: seqId:$seqId content:$msg")
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, seqId: Int, exceptionMaker: String, cause: Throwable) {
      Log.d("Channel", "HeadContext exceptionCaught: seqId:$seqId exceptionMaker: $exceptionMaker cause:$cause")
    }

    override fun getName(): String = TailContext::class.java.name
  }

  inner class TailContext(pipeline: DefaultChannelPipeline) : AbstractChannelHandlerContext(pipeline, true, true), ChannelOutboundHandler<Any>, ChannelInboundHandler<Any> {

    override fun handler(): ChannelHandler = this

    @Throws(Exception::class)
    override fun write(ctx: ChannelHandlerContext, seqId: Int, msg: Any) {
      Log.d("Channel", "TailContext write: seqId:$seqId content:$msg")
      channel.unsafe().internalWrite(seqId, msg)
    }

    @Throws(Exception::class)
    override fun read(ctx: ChannelHandlerContext, seqId: Int, msg: Any) {
      Log.d("Channel", "TailContext read: seqId:$seqId content:$msg")
      ctx.read(seqId, msg)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, seqId: Int, exceptionMaker: String, cause: Throwable) {
      Log.d("Channel", "TailContext exceptionCaught: seqId:$seqId exceptionMaker: $exceptionMaker cause:$cause")
      ctx.exceptionCaught(seqId, exceptionMaker, cause)
    }

    override fun getName(): String = TailContext::class.java.name
  }

  override fun addFirst(handler: ChannelHandler): ChannelPipeline {
    if (checkMultiplicity(handler)) {
      val newCtx = newContext(handler)
      this.addFirst0(newCtx)
    }

    return this
  }

  private fun addFirst0(newCtx: AbstractChannelHandlerContext) {
    val nextCtx = this.head.next
    newCtx.prev = this.head
    newCtx.next = nextCtx
    this.head.next = newCtx
    nextCtx.prev = newCtx
  }

  override fun addLast(handler: ChannelHandler): ChannelPipeline {
    if (checkMultiplicity(handler)) {
      val newCtx = newContext(handler)
      this.addLast0(newCtx)
    }

    return this
  }

  private fun addLast0(newCtx: AbstractChannelHandlerContext) {
    val prev = this.tail.prev
    newCtx.prev = prev
    newCtx.next = this.tail
    prev.next = newCtx
    this.tail.prev = newCtx
  }

  override fun write(seqId: Int, msg: Any) {
    this.head.write(seqId, msg)
  }

  override fun read(seqId: Int, msg: Any) {
    this.tail.read(seqId, msg)
  }

  override fun cancel(seqId: Int) {
    this.channel.cancel(seqId)
  }

  override fun fireExceptionCaught(seqId: Int, exceptionMaker: String, cause: Throwable) {
    this.tail.exceptionCaught(seqId, exceptionMaker, cause)
  }

  private fun checkMultiplicity(handler: ChannelHandler): Boolean {
    if (handler is ChannelHandlerAdapter) {
      if (!handler.added) {
        handler.added = true
        return true
      }
    }

    return false
  }

  private fun newContext(handler: ChannelHandler): AbstractChannelHandlerContext =
      DefaultChannelHandlerContext(this, handler)
}
