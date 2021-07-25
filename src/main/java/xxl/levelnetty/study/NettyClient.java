package xxl.levelnetty.study;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangliangbo
 * @since 2021/7/25
 **/

@Slf4j
public class NettyClient {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(boss);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress("localhost", 5555);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ClientTcpHandler(null));
            ChannelFuture connectFuture = bootstrap.connect().sync();
            log.info("客户端已连接");
            ChannelFuture closeFuture = connectFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            log.info("客户端连接异常", e);
        } finally {
            boss.shutdownGracefully();
        }

    }
}
