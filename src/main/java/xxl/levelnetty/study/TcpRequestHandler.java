package xxl.levelnetty.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author zhangliangbo
 * @since 2021/7/24
 **/


@Slf4j
public class TcpRequestHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        log.info("收到 {}", msg.toString(StandardCharsets.UTF_8));
        Thread.sleep(1000);
        ctx.writeAndFlush(Unpooled.wrappedBuffer("get.".getBytes()));
    }
}
