package xxl.levelnetty.study;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangliangbo
 * @since 2021/7/24
 **/

@Slf4j
public class NettyTcpHandler extends ChannelInitializer<SocketChannel> {

    private EventExecutorGroup handler;

    public NettyTcpHandler(EventExecutorGroup handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(handler,
                new DelimiterBasedFrameDecoder(1024, Unpooled.wrappedBuffer(".".getBytes())),
                new TcpRequestHandler());
    }

}
