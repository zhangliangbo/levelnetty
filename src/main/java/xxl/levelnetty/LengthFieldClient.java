package xxl.levelnetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

public class LengthFieldClient {
    public static void main(String[] args) throws InterruptedException {
        String[] keys = new String[]{};
        final String host = "192.168.24.101";
        final int port = 9000;
        final NioEventLoopGroup group = new NioEventLoopGroup();
        System.err.println("start connect.");
        new Bootstrap().channel(NioSocketChannel.class)
                .group(group)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 6, 4, 0, 10, true));
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                super.channelActive(ctx);
                                System.err.println("active");
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                super.channelInactive(ctx);
                                System.err.println("inactive");
                            }

                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                super.channelRegistered(ctx);
                                System.err.println("registered");
                            }

                            @Override
                            public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                                super.channelUnregistered(ctx);
                                System.err.println("unregistered");
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                super.exceptionCaught(ctx, cause);
                                System.err.println("error:" + cause.getMessage());
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                byte[] bytes = ByteBufUtil.getBytes(msg);
                                String string = new String(bytes);
                                System.err.println(string);
                            }
                        });
                    }
                })
                .connect(host, port)
                .sync()
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.err.println("connected.");
                    }
                })
                .channel()
                .closeFuture()
                .addListener((ChannelFutureListener) future -> {
                    group.shutdownGracefully();
                })
                .sync();
        System.err.println("end connect.");
    }
}
