package channel

/**
 * 参考netty设计的channel+pipeline
 * Created by enzowei on 2017/6/28.
 */

interface ChannelHandler {
  fun getName(): String
}
