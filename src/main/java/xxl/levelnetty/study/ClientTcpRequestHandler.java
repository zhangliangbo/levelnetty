package xxl.levelnetty.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangliangbo
 * @since 2021/7/25
 **/


@Slf4j
public class ClientTcpRequestHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.executor().scheduleAtFixedRate(() -> ctx.writeAndFlush(Unpooled.wrappedBuffer("hello.".getBytes())), 1, 5, TimeUnit.SECONDS);
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.info("收到 {}", msg.toString(StandardCharsets.UTF_8));
    }

}
