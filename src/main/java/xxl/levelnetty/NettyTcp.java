package xxl.levelnetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.ByteOrder;

/**
 * 各种tcp数据源
 */
public class NettyTcp {
    /**
     * 带长度字段的编解码
     *
     * @param host
     * @param port
     * @param offset
     * @param length
     * @param adjustment
     * @param strip
     * @return
     */
    public static Flowable<byte[]> lengthField(String host, int port, int offset, int length, int adjustment, int strip) {
        return Flowable.create(emitter -> {
            final NioEventLoopGroup group = new NioEventLoopGroup();
            new Bootstrap().channel(NioSocketChannel.class)
                    .group(group)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, offset, length, adjustment, strip, true));
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    super.channelActive(ctx);
                                    System.out.println("active");
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    super.channelInactive(ctx);
                                    System.out.println("inactive");
                                }

                                @Override
                                public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                    super.channelRegistered(ctx);
                                    System.out.println("registered");
                                }

                                @Override
                                public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                                    super.channelUnregistered(ctx);
                                    System.out.println("unregistered");
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    super.exceptionCaught(ctx, cause);
                                    emitter.onError(cause);
                                }

                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                    byte[] bytes = ByteBufUtil.getBytes(msg);
                                    emitter.onNext(bytes);
                                }
                            });
                        }
                    })
                    .connect(host, port)
                    .sync()
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            System.out.println("connected.");
                        }
                    })
                    .channel()
                    .closeFuture()
                    .sync()
                    .addListener((ChannelFutureListener) future -> {
                        group.shutdownGracefully();
                        emitter.onComplete();
                    });
        }, BackpressureStrategy.BUFFER);
    }
}
