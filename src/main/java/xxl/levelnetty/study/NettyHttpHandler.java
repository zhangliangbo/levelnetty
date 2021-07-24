package xxl.levelnetty.study;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangliangbo
 * @since 2021/7/24
 **/


@Slf4j
public class NettyHttpHandler extends ChannelInitializer<SocketChannel> {

    private EventExecutorGroup handler;

    public NettyHttpHandler(EventExecutorGroup handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(handler,
                new HttpServerCodec(),
                new HttpObjectAggregator(512 * 1024),
                new HttpRequestHandler());
    }

}
