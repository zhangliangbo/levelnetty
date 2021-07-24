package xxl.levelnetty.study;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangliangbo
 * @since 2021/7/12
 **/

@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        EventLoopGroup worker = new NioEventLoopGroup(2, new DefaultThreadFactory("worker"));
        EventLoopGroup handler = new DefaultEventLoopGroup(new DefaultThreadFactory("handler"));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(5555);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
            serverBootstrap.childHandler(new NettyTcpHandler(handler));
            ChannelFuture bindFuture = serverBootstrap.bind().sync();
            log.info("服务已绑定");
            ChannelFuture closeFuture = bindFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            log.info("服务启动异常", e);
        } finally {
            handler.shutdownGracefully();
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }
}
